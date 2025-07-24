package me.nathan.opensense.file;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static String extractPdfText(Context context, Uri uri) throws IOException {
        // 1) Open the PDF as an InputStream via ContentResolver
        try (InputStream in = context.getContentResolver().openInputStream(uri)) {
            // 2) Load with PDFBox
            PDDocument document = PDDocument.load(in);

            // 3) Use PDFTextStripper to pull out the text
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            document.close();
            return text;
        }
    }

    @SuppressLint("Range")
    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static FileType getFileType(String fileName) {
        String[] chunks = fileName.split("\\.");
        if(chunks.length > 1) {
            String type = chunks[chunks.length - 1].toLowerCase();
            switch (type) {
                case "pdf":
                    return FileType.PDF;
                case "txt":
                    return FileType.TXT;
                case "xbrl":
                    return FileType.XBRL;
                case "docx":
                    return FileType.DOCX;
                default:
                    return FileType.OTHER;
            }
        }
        return FileType.OTHER;
    }

    public enum FileType {
        PDF,
        TXT,
        XBRL,
        DOCX,
        OTHER
    }
}
