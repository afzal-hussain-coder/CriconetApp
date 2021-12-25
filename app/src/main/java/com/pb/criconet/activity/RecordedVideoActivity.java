package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.adapters.FRecordedVideoAdapter;
import com.pb.criconet.adapters.LiveMatchAdapter;
import com.pb.criconet.models.LiveStreamingModel;
import com.pb.criconet.models.RecodedVideo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecordedVideoActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private RecyclerView weeklist;
    private ArrayList<RecodedVideo> data;
    RecodedVideo recodedVideo;
    private FRecordedVideoAdapter adapter;
    private RequestQueue queue;
    private TextView notfound;
    CustomLoaderView loaderView;
    Context mContext;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded_video);
        mActivity=this;
        mContext=this;

        intializeView();
    }

    private void intializeView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(getResources().getString(R.string.recorded_video).toUpperCase());
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        loaderView = CustomLoaderView.initialize(mContext);
        queue = Volley.newRequestQueue(mContext);

        weeklist = findViewById(R.id.week_list);
        weeklist.setLayoutManager(new LinearLayoutManager(mContext));
        notfound = (TextView) findViewById(R.id.notfound);
        if (Global.isOnline(mContext)) {
            getVideoList();
        } else {
            Global.showDialog(mActivity);
        }
    }

    private void getVideoList() {
        //progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_RECORDED_VIDEO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loaderView.hideLoader();
                        //progress.dismiss();
                        try {
                            Log.d("Live Strean Response",response);
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                data =new ArrayList<>();
                                JSONArray jsonArray= jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++){
                                    recodedVideo=new RecodedVideo(jsonArray.getJSONObject(i));
                                    data.add(recodedVideo);
                                }


                                if (data.size() == 0) {
                                    notfound.setVisibility(View.VISIBLE);
                                } else {
                                    notfound.setVisibility(View.GONE);
                                    adapter = new FRecordedVideoAdapter(data,mContext);
                                    weeklist.setAdapter(adapter);
                                }


                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
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
                        //progress.dismiss();
                        loaderView.hideLoader();
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
//                Global.msgDialog(getActivity(), "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));

                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}