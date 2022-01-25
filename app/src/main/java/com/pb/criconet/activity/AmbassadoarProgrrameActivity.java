package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;

public class AmbassadoarProgrrameActivity extends AppCompatActivity {
    ExtendedFloatingActionButton extendedFAB;
    WebView web;
    String url="";
    TextView mTitle;
    SharedPreferences prefs;
    Activity mActivity;
    String FROM="";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambassadoar_progrrame);
        mActivity = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent= getIntent();
        if(intent!=null){
            FROM = intent.getExtras().getString("From");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FROM.equalsIgnoreCase("1")){
                    Intent intent = new Intent(AmbassadoarProgrrameActivity.this, AmbassdorRewardsActivity.class);
                    intent.putExtra("From","1");
                    startActivity(intent);
                    finish();
                }else{

                    Intent intent = new Intent(AmbassadoarProgrrameActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
        mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        extendedFAB = findViewById(R.id.extFab);

        if(SessionManager.get_is_ambassador(prefs).equalsIgnoreCase("1")){
            extendedFAB.setVisibility(View.GONE);
        }else{
            extendedFAB.setVisibility(View.VISIBLE);
        }

        web = findViewById(R.id.web);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(Global.GET_CAMPUS_AMBASSADOAR);

        mTitle.setText(R.string.campus_ambassdoar_progrrame);

        extendedFAB.setOnClickListener(view -> {
            Intent intentt = new Intent(AmbassadoarProgrrameActivity.this, JoinAmbassadorActivity.class);
            intentt.putExtra("From","2");
            startActivity(intentt);
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(FROM.equalsIgnoreCase("1")){
            Intent intent = new Intent(AmbassadoarProgrrameActivity.this, AmbassdorRewardsActivity.class);
            intent.putExtra("From","1");
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(AmbassadoarProgrrameActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}