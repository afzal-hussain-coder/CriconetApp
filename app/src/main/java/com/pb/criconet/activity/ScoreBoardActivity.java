package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.PlayesrListAdapter;
import com.pb.criconet.adapters.PlayesrScoreListAdapter;
import com.pb.criconet.models.MyContestListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class ScoreBoardActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private RequestQueue queue;
    Activity mActivity;
    CustomLoaderView loaderView;
    private RecyclerView rv_player_score;
    private PlayesrScoreListAdapter adapter;
    private ArrayList<MyContestListModel> myContestListModelslist;
    String leaugeName="";
    String FROM="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        loaderView = CustomLoaderView.initialize(this);
        mActivity = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            leaugeName = extras.getString("League");
            FROM = extras.getString("FROM");

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FROM.equalsIgnoreCase("1")){
                    Intent intent = new Intent(mActivity, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(FROM.equalsIgnoreCase("2")){
                    finish();
                }


            }
        });
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(getResources().getString(R.string.player_team_ranking));

        queue = Volley.newRequestQueue(mActivity);
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);



        if (Global.isOnline(mActivity)) {
            getMyContest();
        } else {
            Global.showDialog(mActivity);
        }

        rv_player_score = findViewById(R.id.rv_player_score);
        rv_player_score.setLayoutManager(new LinearLayoutManager(mActivity));

    }
    private void getMyContest() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL+ Global.My_FANTASY_CONTEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.d(response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                                JSONArray jsonArray= jsonObject.getJSONArray("data");
                                MyContestListModel myContestListModel;
                                myContestListModelslist = new ArrayList<>();
                                for(int i=0;i<jsonArray.length();i++){
                                    myContestListModel = new MyContestListModel(jsonArray.getJSONObject(i));
                                    myContestListModelslist.add(myContestListModel);
                                }
                                adapter = new PlayesrScoreListAdapter(mActivity,myContestListModelslist,SessionManager.get_leaugeName(prefs));
                                rv_player_score.setAdapter(adapter);

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
                        loaderView.hideLoader();
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
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
        if(FROM.equalsIgnoreCase("1")){
            Intent intent = new Intent(mActivity, MainActivity.class);
            startActivity(intent);
            finish();
        }else if(FROM.equalsIgnoreCase("2")){
            finish();
        }
    }
}