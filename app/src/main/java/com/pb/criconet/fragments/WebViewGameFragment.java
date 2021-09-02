package com.pb.criconet.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.activity.MainActivity;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;


public class WebViewGameFragment extends Fragment {
    private View rootView;
    private SharedPreferences prefs;
    private ProgressBar progress;
    private RequestQueue queue;
    private TextView notfound;
    private WebView webView;

    public static WebViewGameFragment newInstance() {
        return new WebViewGameFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.webview_layout, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText("Game");
        ImageView img_addpost = toolbar.findViewById(R.id.img_addpost);
        TextView tv_post = toolbar.findViewById(R.id.tv_post);
        tv_post.setVisibility(View.GONE);
        img_addpost.setVisibility(View.GONE);
        img_addpost.setVisibility(View.GONE);
        ImageView img_close = toolbar.findViewById(R.id.img_close);
        img_close.setVisibility(View.GONE);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        queue = Volley.newRequestQueue(getActivity());

        notfound = (TextView) rootView.findViewById(R.id.notfound);
        webView = (WebView) rootView.findViewById(R.id.webView1);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        progress = (ProgressBar) rootView.findViewById(R.id.progressBar);
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
                        ((MainActivity) getActivity()).onBackPressed();
                    }
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }


    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            WebViewGameFragment.this.setValue(newProgress);
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
        if (Global.isOnline(getActivity())) {
            webView.setVisibility(View.VISIBLE);
            notfound.setVisibility(View.GONE);
        } else {
            webView.setVisibility(View.GONE);
            notfound.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

}
