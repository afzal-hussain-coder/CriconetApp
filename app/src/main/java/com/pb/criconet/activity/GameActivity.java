package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.fragments.WebViewGameFragment;

public class GameActivity extends AppCompatActivity {
    private View rootView;
    private SharedPreferences prefs;
    private ProgressBar progress;
    private RequestQueue queue;
    private TextView notfound;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(R.string.game);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        notfound = (TextView) findViewById(R.id.notfound);
        webView = (WebView) findViewById(R.id.webView1);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(100);
        webView.setWebChromeClient(new MyWebViewClient());
        webView.loadUrl(Global.GameURL + "user_id=" + SessionManager.get_user_id(prefs) + "");
//        webView.loadUrl(Global.websiteURL_demo);

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //This is the filter
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        (GameActivity.this).onBackPressed();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            GameActivity.this.setValue(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    public void setValue(int progress) {
        this.progress.setProgress(progress);
        if (progress == 100) {
            this.progress.setVisibility(View.GONE);
        }
    }


    @Override
    public void onResume() {
        if (Global.isOnline(this)) {
            webView.setVisibility(View.VISIBLE);
            notfound.setVisibility(View.GONE);
        } else {
            webView.setVisibility(View.GONE);
            notfound.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}