package com.jd.newsapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HeadlinesPageAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    public HeadlinesPageAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new WorldHeadlinesFragment();
            case 1:
                return new BusinessHeadlinesFragment();
            case 2:
                return new PoliticsHeadlinesFragment();
            case 3:
                return new SportsHeadlinesFragment();
            case 4:
                return new TechnologyHeadlinesFragment();
            case 5:
                return new ScienceHeadlinesFragment();
            case 6:
                return null;
        }
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) { return POSITION_NONE; }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
