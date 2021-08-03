package com.pb.criconet.Utills;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public  class IntroSliderAdapter extends FragmentStateAdapter {
    private  ArrayList fragmentList;

    public int getItemCount() {
        return fragmentList.size();
    }

    @NotNull
    public Fragment createFragment(int position) {
        Object var10000 = fragmentList.get(position);
        return (Fragment)var10000;
    }

    public  void setFragmentList(List list) {
        fragmentList.clear();
        fragmentList.addAll(list);
        notifyDataSetChanged();
    }

    public IntroSliderAdapter(FragmentActivity fa) {
        super(fa);
        this.fragmentList = new ArrayList();
    }
}
