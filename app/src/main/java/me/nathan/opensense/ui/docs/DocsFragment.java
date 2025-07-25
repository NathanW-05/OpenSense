package me.nathan.opensense.ui.docs;

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
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.util.ArrayList;

import me.nathan.opensense.R;
import me.nathan.opensense.file.Document;
import me.nathan.opensense.file.FileUtil;
import me.nathan.opensense.log.Logger;

public class DocsFragment extends Fragment {

    private final int REQUEST_CODE_PICK_FILE = 1001;
    private static final String[] TAB_TITLES = { "Insights", "Highlights" };

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

        TabLayout tabLayout   = docsView.findViewById(R.id.docsTabLayout);
        ViewPager2 viewPager  = docsView.findViewById(R.id.docsViewPager);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override public int getItemCount() {
                return TAB_TITLES.length;
            }
            @NonNull @Override
            public Fragment createFragment(int position) {
                if (position == 0) return new InsightsFragment();
                else             return new HighlightsFragment();
            }
        });

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, pos) -> tab.setText(TAB_TITLES[pos])
        ).attach();

        //todo: make sure this sets the current document if one has been selected
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
    //todo: add support for more file types, rn is just pdfs
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

    private void setViewingFile(Document document) {
        updateViewingFileText(document);
    }

    private void updateViewingFileText(Document document) {
        if(document != null) {
            String name = document.getName();
            //todo: probably should rewrite this logic it's hacky
            if(document.getName().length() > 18) {
                name = name.substring(0, 18) + "...";
            }
            String sourceString = "<b>" + "Viewing:" + "</b> " + name;
            viewingFileText.setText(Html.fromHtml(sourceString));
        } else {
            String sourceString = "<b>" + "Viewing:" + "</b> No File Selected";
            viewingFileText.setText(Html.fromHtml(sourceString));
        }
    }
}
