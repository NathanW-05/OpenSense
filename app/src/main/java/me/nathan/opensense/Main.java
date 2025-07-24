package me.nathan.opensense;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;

import java.util.Arrays;
import java.util.List;

import me.nathan.opensense.ui.ViewPagerAdapter;

public class Main extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public static Context applicationContext = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // Initialize views
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        applicationContext = getApplicationContext();

        // List of tab titles & icons
        List<String> tabTitles = Arrays.asList("Home", "News", "Docs");
        int[] tabIcons = {R.drawable.home, R.drawable.news, R.drawable.document};

        // Initialize PDF util
        PDFBoxResourceLoader.init(getApplicationContext());

        viewPager.setAdapter(new ViewPagerAdapter(this, tabTitles));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(tabTitles.get(position));
            tab.setIcon(ContextCompat.getDrawable(this, tabIcons[position]));
        }).attach();
    }
}
