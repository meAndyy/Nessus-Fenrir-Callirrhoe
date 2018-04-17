package com.example.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.fragments.LogFragment;
import com.example.fragments.MainFragment;
import com.example.fragments.RWFragment;

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
            case 2:

                return new LogFragment();

        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Contacts";
            case 1:
            default:
                return "Home";
            case 2:
                return "Log";


        }
    }
    @Override
    public int getCount() {
        return 3;
    }
}
