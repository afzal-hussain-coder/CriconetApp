package com.pb.criconet.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


/**
 * Created by user1 on 3/18/2016.
 */
public class About extends AppCompatActivity {

    TextView text, hdn, mTitle;
    RequestQueue queue;
    String status;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });
        queue = Volley.newRequestQueue(About.this);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            status = bundle.getString("status");
        }

        hdn = (TextView) findViewById(R.id.hdn);
        text = (TextView) findViewById(R.id.text);


        progress = new ProgressDialog(About.this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        getProfile();


    }

    public void getProfile() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "static_page",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.v(response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {

                                mTitle.setText(Global.capitizeString(jsonObject.optString("title")));
                                hdn.setText(jsonObject.optString("title"));
                                text.setText(Html.fromHtml(jsonObject.getJSONObject("content").optString("text")));

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(About.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(About.this, getResources().getString(R.string.error_server));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.dismiss();
                        Global.msgDialog(About.this, getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("page", status);
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(About.this, Settings.class);
        startActivity(intent);
        finish();
    }
}