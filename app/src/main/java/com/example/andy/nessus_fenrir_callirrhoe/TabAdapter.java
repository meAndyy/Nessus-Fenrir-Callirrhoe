package com.example.andy.nessus_fenrir_callirrhoe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Andy on 11/12/2017.
 */

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return new MainFragment();
            case 1:

                return new RWFragment();

        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Home";
            case 1:
            default:
                return "Tag";
        }
    }
    @Override
    public int getCount() {
        return 2;
    }
}
