package com.pb.criconet.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;

import timber.log.Timber;


public class WebView_Activity extends AppCompatActivity {
    SharedPreferences prefs;
    ProgressBar progress;
    RequestQueue queue;
    TextView txtView_call, txtView_id;
    WebView webView;
    public static final String WebURL = "WebURL";
    String url;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
//        mTitle.setText(R.string.update_profile);

//        if (savedInstanceState == null) {
//            url = getIntent().getDataString();
//        }

        prefs = PreferenceManager.getDefaultSharedPreferences(WebView_Activity.this);
        queue = Volley.newRequestQueue(WebView_Activity.this);

        webView = (WebView) findViewById(R.id.webView1);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(100);
        webView.setWebChromeClient(new MyWebViewClient());

//        if (url.equals("")) {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        url = bundle.getString(WebURL);
        Timber.e(url);
//        }
        webView.loadUrl(url.replace("http://","https://"));
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
                        onBackPressed();
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
            WebView_Activity.this.setValue(newProgress);
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
        super.onResume();
    }

}
