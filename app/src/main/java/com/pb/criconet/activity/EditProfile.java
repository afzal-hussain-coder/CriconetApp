package com.pb.criconet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

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
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.MultipartRequest;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.adapters.ViewPagerAdapter;
import com.pb.criconet.fragments.FragmentAvility;
import com.pb.criconet.fragments.FragmentEditProfile;
import com.pb.criconet.fragments.FragmentExperienceSetting;
import com.pb.criconet.models.UserData;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class EditProfile extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    public static boolean isMenu;
    private SharedPreferences prefs;
    private AppCompatActivity mActivity;
    private String from_where="";
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        mActivity=EditProfile.this;
        Toolbar toolbar = (Toolbar) EditProfile.this.findViewById(R.id.toolbar);
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

                Intent intent = new Intent(EditProfile.this, Settings.class);
                startActivity(intent);
                finish();
            }else{

                Intent intent = new Intent(EditProfile.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }


        });

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(getResources().getString(R.string.Edit_Profile));

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        view = findViewById(R.id.view);
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),mActivity); //here used child fragment manager


        adapter.addFragment(new FragmentEditProfile(), "Personal Information");
        if(SessionManager.get_profiletype(prefs).equalsIgnoreCase("Coach")) {
            adapter.addFragment(new FragmentExperienceSetting(), "Coach Specialization");
            adapter.addFragment(new FragmentAvility(), "Slot Availability");
            tabLayout.setVisibility(View.GONE);
            tabLayout.setupWithViewPager(viewPager);
            view.setVisibility(View.GONE);
        }else {
            view.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
        }
        viewPager.setAdapter(adapter);
        if(isMenu){
            viewPager.setCurrentItem(2);
        }
    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(from_where.equalsIgnoreCase("2")){

            Intent intent = new Intent(EditProfile.this, Settings.class);
            startActivity(intent);
            finish();
        }else{

            Intent intent = new Intent(EditProfile.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
