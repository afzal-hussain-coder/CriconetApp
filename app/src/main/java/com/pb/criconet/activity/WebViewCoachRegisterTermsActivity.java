package com.pb.criconet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pb.criconet.R;
import com.pb.criconet.fragments.FragmentExperienceSetting;

public class WebViewCoachRegisterTermsActivity extends AppCompatActivity {
    TextView mTitle;
    WebView web;
    String url="";
    String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WebViewCoachRegisterTermsActivity.this, CoachProfessionalInfoActivity.class);
                //intent.putExtra("FROM", "4");
                startActivity(intent);
                finish();
            }
        });
        mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        web = findViewById(R.id.web);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("URL");
            web.getSettings().setJavaScriptEnabled(true);
            web.loadUrl(url);
            title = bundle.getString("title");
        }
        Log.d("URL",url);
        mTitle.setText(title);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(WebViewCoachRegisterTermsActivity.this, CoachProfessionalInfoActivity.class);
        //intent.putExtra("FROM", "4");
        startActivity(intent);
        finish();
    }
}