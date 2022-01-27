package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.MultipartRequest;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.ViewPagerAdapter;
import com.pb.criconet.fragments.FragmentAvility;
import com.pb.criconet.fragments.FragmentCoachEditProfile;
import com.pb.criconet.fragments.FragmentEditProfile;
import com.pb.criconet.fragments.FragmentExperienceSetting;
import com.pb.criconet.models.City;
import com.pb.criconet.models.Country;
import com.pb.criconet.models.Language;
import com.pb.criconet.models.States;
import com.pb.criconet.models.UserData;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CoachRegistrationActivity extends AppCompatActivity implements FragmentCoachEditProfile.navigateListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    public static boolean isMenu;
    private SharedPreferences prefs;
    private AppCompatActivity mActivity;
    private String from_where="";
    private View view;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_registration);
        mActivity=CoachRegistrationActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            from_where = bundle.getString("FROM");
        }
        toolbar.setNavigationOnClickListener(v -> {

            if(from_where.equalsIgnoreCase("2")){

                Intent intent = new Intent(mActivity, Settings.class);
                startActivity(intent);
                finish();
            }else if(from_where.equalsIgnoreCase("3")){
                Intent intent = new Intent(mActivity, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{

                Intent intent = new Intent(mActivity, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(getResources().getString(R.string.join_as_coach));

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        view = findViewById(R.id.view);
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),mActivity); //here used child fragment manager


//        adapter.addFragment(new FragmentCoachEditProfile(), "Personal Information");
//        adapter.addFragment(new FragmentExperienceSetting(), "Professional Qualifications");
//        adapter.addFragment(new FragmentAvility(), "Available Date & Session");
//
//        tabLayout.setVisibility(View.VISIBLE);
//        tabLayout.setupWithViewPager(viewPager);

        if(SessionManager.getProfileType(prefs).equalsIgnoreCase("coach")){

            adapter.addFragment(new FragmentCoachEditProfile(), "Personal Information");
            adapter.addFragment(new FragmentExperienceSetting(), "Professional Qualifications");
            adapter.addFragment(new FragmentAvility(), "Available Date & Session");

            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
        }else{

            adapter.addFragment(new FragmentCoachEditProfile(), "Personal Information");
            //adapter.addFragment(new FragmentExperienceSetting(), "Professional Qualifications");
            //adapter.addFragment(new FragmentAvility(), "Available Date & Session");

            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
        }



        //view.setVisibility(View.VISIBLE);
        viewPager.setAdapter(adapter);

        if(from_where.equalsIgnoreCase("4")){
            viewPager.setCurrentItem(1);
        }else if(from_where.equalsIgnoreCase("5")){
            viewPager.setCurrentItem(2);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(from_where.equalsIgnoreCase("2")){

            Intent intent = new Intent(mActivity, Settings.class);
            startActivity(intent);
            finish();
        }else if(from_where.equalsIgnoreCase("3")){
            Intent intent = new Intent(mActivity, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{

            Intent intent = new Intent(mActivity, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void callbackMetod(String type) {
        Toaster.customToast(type);
        from_where = type;
        adapter.addFragment(new FragmentExperienceSetting(), "Professional Qualifications");

    }
}