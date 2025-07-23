package me.nathan.opensense.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

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

    // Creates and returns a fragment for a given position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("title", titles.get(position));
        fragment.setArguments(args);
        return fragment;
    }
}
