package me.nathan.opensense;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

import me.nathan.opensense.ui.ViewPagerAdapter;

public class Main extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // Initialize views
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        // List of tab titles & icons
        List<String> tabTitles = Arrays.asList("Home", "News", "Docs");
        int[] tabIcons = {R.drawable.home, R.drawable.news, R.drawable.document};

        viewPager.setAdapter(new ViewPagerAdapter(this, tabTitles));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(tabTitles.get(position));
            tab.setIcon(ContextCompat.getDrawable(this, tabIcons[position]));
        }).attach();
    }
}
