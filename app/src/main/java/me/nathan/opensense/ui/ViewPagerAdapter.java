package me.nathan.opensense.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import me.nathan.opensense.Main;
import me.nathan.opensense.log.Logger;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final List<String> titles;

    // Constructor
    public ViewPagerAdapter(@NonNull FragmentActivity activity, List<String> titles) {
        super(activity);
        this.titles = titles;
    }

    // Returns the number of items (fragments) in ViewPager
    @Override
    public int getItemCount() {
        return titles.size();
    }

    // Creates and returns a unique fragment for a given position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
            case 1:
                fragment = new MainFragment(); break;
            case 2:
                fragment = new DocsFragment(); break;
        }
        Bundle args = new Bundle();
        assert fragment != null;
        fragment.setArguments(args);
        return fragment;
    }
}
