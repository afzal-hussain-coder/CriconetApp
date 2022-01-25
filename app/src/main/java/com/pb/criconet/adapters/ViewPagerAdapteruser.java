package com.pb.criconet.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pb.criconet.Utills.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapteruser extends FragmentPagerAdapter {
    private final List<Fragment> mList = new ArrayList<>();
    private final List<String> mTitleList = new ArrayList<>();
    SharedPreferences prefs;

    public ViewPagerAdapteruser(FragmentManager supportFragmentManager, Context context) {
        super(supportFragmentManager);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

    }
    @Override
    public Fragment getItem(int i) {
        return mList.get(i);
    }
    @Override
    public int getCount() {
        if(SessionManager.get_profiletype(prefs).equalsIgnoreCase("Coach")) {
            return 3;
        }else {
            return 1;
        }
    }
    public void addFragment(Fragment fragment, String title) {
        mList.add(fragment);
        mTitleList.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}


