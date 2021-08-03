package com.pb.criconet.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.google.android.material.tabs.TabLayout;
import com.pb.criconet.R;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.activity.MainActivity;
import com.pb.criconet.adapters.ViewPagerAdapter;

public class FragmentPager extends Fragment {
    private View rootView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    public static boolean isMenu;
    private SharedPreferences prefs;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        adapter = new ViewPagerAdapter(getChildFragmentManager(),activity); //here used child fragment manager
    }

    public static FragmentPager newInstance(boolean isValue) {
        isMenu=isValue;
        return new FragmentPager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.pager, container, false);




        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabLayout=rootView.findViewById(R.id.tabLayout);
        viewPager=rootView.findViewById(R.id.viewPager);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Toolbar toolbar =getActivity().findViewById(R.id.toolbar);
        TextView mTitle =toolbar.findViewById(R.id.toolbartext);
        mTitle.setText("Edit Profile");

        adapter.addFragment(new FragmentEditProfile(), "Personal Information");
        if(SessionManager.get_profiletype(prefs).equalsIgnoreCase("Coach")) {
            adapter.addFragment(new FragmentExperienceSetting(), "Coach Specialization");
            adapter.addFragment(new FragmentAvility(), "Slot Availability");
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
        }else {
            tabLayout.setVisibility(View.GONE);
        }
        viewPager.setAdapter(adapter);
        if(isMenu){
            viewPager.setCurrentItem(2);
        }
    }
}
