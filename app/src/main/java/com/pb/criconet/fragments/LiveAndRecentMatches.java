package com.pb.criconet.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.datatransport.runtime.dagger.Reusable;
import com.google.android.material.tabs.TabLayout;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.activity.MainActivity;
import com.pb.criconet.adapters.LiveMatchAdapter;
import com.pb.criconet.models.LiveStreamingModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LiveAndRecentMatches extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View rootView;
    Activity mActivity;

    public static LiveAndRecentMatches newInstance() {
        return new LiveAndRecentMatches();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.live_and_recent_matches, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // mActivity = getActivity();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(getResources().getString(R.string.Matches).toUpperCase());
        ImageView img_addpost = toolbar.findViewById(R.id.img_addpost);
        TextView tv_post = toolbar.findViewById(R.id.tv_post);
        tv_post.setVisibility(View.GONE);
        img_addpost.setVisibility(View.GONE);
        img_addpost.setVisibility(View.GONE);
        ImageView img_close = toolbar.findViewById(R.id.img_close);
        img_close.setVisibility(View.GONE);

        tabLayout=rootView.findViewById(R.id.tabLayoutt);
        viewPager=rootView.findViewById(R.id.viewPagerr);
        addTabs(viewPager);
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdpterMatches adapter = new ViewPagerAdpterMatches(getChildFragmentManager());
        adapter.addFragment(new LiveMatches(), getString(R.string.Live_Matches));
        adapter.addFragment(new RecMatches(), getString(R.string.Recorded_Matches));
        viewPager.setAdapter(adapter);
    }
    public class ViewPagerAdpterMatches extends FragmentPagerAdapter {
        private final List<Fragment> mList = new ArrayList<>();
        private final List<String> mTitleList = new ArrayList<>();
        SharedPreferences prefs;

        public ViewPagerAdpterMatches(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
           //refs = PreferenceManager.getDefaultSharedPreferences(context);

        }
        @Override
        public @NotNull Fragment getItem(int i) {
            return mList.get(i);
        }
        @Override
        public int getCount() {
            return mList.size();

        }
        public void addFragment(Fragment fragment, String title) {
            mList.add(fragment);
            mTitleList.add(title);
        }
        public void removeCurrentItem(Fragment fragment, String title) {
            mList.remove(fragment);
            mTitleList.remove(title);
            //notifyDataSetChanged();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //MainActivity.bottomNavigation.setSelectedIndex(2, true);
        //dTabs(viewPager);
        //bLayout.setupWithViewPager(viewPager);
    }

}
