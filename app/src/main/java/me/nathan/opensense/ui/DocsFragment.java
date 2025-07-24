package me.nathan.opensense.ui;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;

import me.nathan.opensense.R;
import me.nathan.opensense.file.Document;
import me.nathan.opensense.file.FileUtil;
import me.nathan.opensense.log.Logger;

public class DocsFragment extends Fragment {

    private final int REQUEST_CODE_PICK_FILE = 1001;

    private final ArrayList<Document> loadedDocuments = new ArrayList<>();
    private View docsView;
    private TextView viewingFileText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        docsView = inflater.inflate(R.layout.docs_layout, container, false);
        Bundle args = getArguments();
        String title = "";
        if (args != null) {
            title = args.getString("title");
        }

        docsView.findViewById(R.id.selectFileButton).setOnClickListener(v -> openFilePicker());
        viewingFileText = docsView.findViewById(R.id.viewingFileText);

        updateViewingFileText(null);

        return docsView;
    }

    // opens the file explorer so the user can pick a document they want to analyze
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    // Extracts the text from selected file, loads it to list, sets it as viewing file
    //todo: handle this extraction async or cap file size.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            assert uri != null;
            String fileName = FileUtil.getFileName(this.getContext(), uri);
            FileUtil.FileType fileType = FileUtil.getFileType(fileName);
            String contents = "";

            for(Document doc : loadedDocuments) {
                if(doc.getName().equals(fileName)) {
                    assert this.getContext() != null;
                    Logger.appMessage(this.getContext(), "Document already loaded. Select it instead.");
                    return;
                }
            }

            switch (fileType) {
                case PDF:
                    try {
                        contents = FileUtil.extractPdfText(this.getContext(), uri);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case OTHER:
                    assert this.getContext() != null;
                    Logger.appMessage(this.getContext(), "Unsupported File Type");
                    break;
                default:
                    assert this.getContext() != null;
                    Logger.appMessage(this.getContext(), "Invalid File");
                    break;
            }
            if(!contents.isEmpty()) {
                Document document = new Document(fileName, contents);
                loadedDocuments.add(document);
                assert this.getContext() != null;
                setViewingFile(document);
                Logger.appMessage(this.getContext(), fileName + " loaded");
            } else {
                assert this.getContext() != null;
                Logger.appMessage(this.getContext(), "Document is empty");
            }
        }
    }

    //todo: this resets when the device rotates. i think this whole thing does too. want to make sure
    // some stuff saves
    private void setViewingFile(Document document) {
        updateViewingFileText(document);
    }

    //todo: see above. will need to be ran each time this thing gets reset
    //todo: make sure the length of the file name is capped so it doesn't overlap with the button
    private void updateViewingFileText(Document document) {
        if(document != null) {
            String sourceString = "<b>" + "Viewing:" + "</b> " + document.getName();
            viewingFileText.setText(Html.fromHtml(sourceString));
        } else {
            String sourceString = "<b>" + "Viewing:" + "</b> No File Selected";
            viewingFileText.setText(Html.fromHtml(sourceString));
        }
    }
}
