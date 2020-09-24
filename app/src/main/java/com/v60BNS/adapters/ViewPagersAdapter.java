package com.v60BNS.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagersAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> titles;
    public ViewPagersAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();

    }

    public void addFragments(List<Fragment> fragments)
    {
        this.fragmentList.addAll(fragments);
    }

    public void addTitles(List<String> titles)
    {
        this.titles.addAll(titles);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);

    }
}
