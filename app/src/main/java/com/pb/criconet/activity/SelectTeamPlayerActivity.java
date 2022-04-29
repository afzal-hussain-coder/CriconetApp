package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.PlayesrListAdapter;
import com.pb.criconet.models.PlayerModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class SelectTeamPlayerActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private RequestQueue queue;
    Activity mActivity;
    CustomLoaderView loaderView;
    private RecyclerView rv_player;
    private PlayesrListAdapter adapter;
    TextView tv_selectedPlayerCount;
    Button btn_submit;
    int count = 0;
    String URL = "";
    String team1Logo = "";
    String team2Logo = "";
    ArrayList<PlayerModel> playerModelArrayList;
    CircleImageView im_team2, im_team1;
    CountDownTimer mCountDownTimer;
    String matchTime = "";
    String team1Name = "";
    String team2Name = "";
    TextView team1_namee, team2_namee;
    TextView team1_playerCount, team2_playerCount;
    Animation anim=null;
    String matchId="";
    JSONArray jsonArraySelectedItem=null;
    String league="";


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_team_player);

        loaderView = CustomLoaderView.initialize(this);
        mActivity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            URL = extras.getString("URL");
            team1Logo = extras.getString("Team1Logo");
            team2Logo = extras.getString("Team2Logo");
            matchTime = extras.getString("MatchTime");
            team1Name = extras.getString("Team1Name");
            team2Name = extras.getString("Team2Name");
            matchId = extras.getString("MatchId");
            league = extras.getString("League");

        }

        im_team2 = findViewById(R.id.im_team2);
        im_team1 = findViewById(R.id.im_team1);
        Glide.with(mActivity).load(team1Logo).dontAnimate().placeholder(getDrawable(R.drawable.ipllogo))
                .into(im_team1);
        Glide.with(mActivity).load(team2Logo).dontAnimate().placeholder(getDrawable(R.drawable.ipllogo))
                .into(im_team2);

        team1_namee = findViewById(R.id.team1_namee);
        team1_namee.setText(team1Name);
        team2_namee = findViewById(R.id.team2_namee);
        team2_namee.setText(team2Name);

        team1_playerCount = findViewById(R.id.team1_playerCount);
        team2_playerCount = findViewById(R.id.team2_playerCount);

        queue = Volley.newRequestQueue(mActivity);
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        long startTime = System.currentTimeMillis();

        mCountDownTimer = new CountDownTimer(getTimer(matchTime), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                //startTime= startTime -1;
                long serverUptimeSeconds = (millisUntilFinished - startTime) / 1000;

                @SuppressLint("DefaultLocale") String daysLeft = String.format("%d", serverUptimeSeconds / 86400);

                @SuppressLint("DefaultLocale") String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);

                @SuppressLint("DefaultLocale") String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);

                @SuppressLint("DefaultLocale") String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                if(Integer.parseInt(secondsLeft)<0){
                    mTitle.setText(mActivity.getResources().getString(R.string.Match_Started));
                    mTitle.setTextColor(mActivity.getResources().getColor(R.color.green));
                    mTitle.startAnimation(anim);
                }else{
                    String timerFinal1 = daysLeft + "d" + " : " + hoursLeft + "h" + " : " + minutesLeft + "m" + " : " + secondsLeft + "s";
                    mTitle.setText(timerFinal1);
                    mTitle.setTextColor(mActivity.getResources().getColor(R.color.white_smoke));
                }


            }

            @Override
            public void onFinish() {

            }
        }.start();


        tv_selectedPlayerCount = findViewById(R.id.tv_selectedPlayerCount);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(v -> {

            if(btn_submit.getText().toString().equalsIgnoreCase("Go to your score board")){
                startActivity(new Intent(mActivity, ScoreBoardActivity.class).putExtra("League",league).putExtra("FROM","2"));
            }else{
                if (Global.isOnline(mActivity)) {
                    getJoinContest();
                } else {
                    Global.showDialog(mActivity);
                }
            }

        });

        rv_player = findViewById(R.id.rv_player);
        rv_player.setLayoutManager(new LinearLayoutManager(mActivity));

        if (Global.isOnline(mActivity)) {
            getPlayerList();
        } else {
            Global.showDialog(mActivity);
        }

    }


    private void getPlayerList() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    Timber.d(response);
                    loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("status").equalsIgnoreCase("success")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            JSONObject jsonObjectTeam1;
                            JSONObject jsonObjectTeam2;
                            String teamName1 = "";
                            String teamName2 = "";

                            jsonObjectTeam1 = jsonArray.getJSONObject(0);
                            jsonObjectTeam2 = jsonArray.getJSONObject(1);
                            teamName1 = jsonObjectTeam1.getString("teamName");
                            teamName2 = jsonObjectTeam2.getString("teamName");

                            JSONArray jsonArrayTeam1 = jsonArray.getJSONObject(0).getJSONArray("players");
                            JSONObject jsonObject1Again = null;
                            JSONArray jsonArray1Final = new JSONArray();
                            for (int j = 0; j < jsonArrayTeam1.length(); j++) {
                                jsonObject1Again = jsonArrayTeam1.getJSONObject(j);
                                jsonObject1Again.put("teamName", teamName1);
                                jsonArray1Final.put(jsonObject1Again);
                            }

                            JSONArray jsonArrayTeam2 = jsonArray.getJSONObject(1).getJSONArray("players");
                            JSONObject jsonObject2Again = null;
                            for (int k = 0; k < jsonArrayTeam2.length(); k++) {
                                jsonObject2Again = jsonArrayTeam2.getJSONObject(k);
                                jsonObject2Again.put("teamName", teamName2);
                                jsonArray1Final.put(jsonObject2Again);
                            }

                            PlayerModel playerModel = null;
                            playerModelArrayList = new ArrayList<>();
                            for (int i = 0; i < jsonArray1Final.length(); i++) {
                                playerModel = new PlayerModel(jsonArray1Final.getJSONObject(i));
                                playerModelArrayList.add(playerModel);
                            }


                            adapter = new PlayesrListAdapter(mActivity, team1Name,team2Name,playerModelArrayList, new PlayesrListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(ArrayList<PlayerModel> integers,ArrayList<String>team1List,ArrayList<String>team2List) {
                                    count = integers.size();
                                    jsonArraySelectedItem= new JSONArray();
                                    JSONObject jsonObject1=null;

                                    if(!integers.isEmpty()){
                                        for(int j =0;j<integers.size();j++){
                                            jsonObject1=new JSONObject();
                                            try {
                                                jsonObject1.put("id",integers.get(j).getId());
                                                jsonObject1.put("Name",integers.get(j).getName());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            jsonArraySelectedItem.put(jsonObject1);
                                        }
                                    }
                                    team1_playerCount.setText(String.valueOf(team1List.size()));
                                    team2_playerCount.setText(String.valueOf(team2List.size()));
                                    tv_selectedPlayerCount.setText(String.valueOf(integers.size()));
                                    if (count > 0) {
                                        btn_submit.setVisibility(View.VISIBLE);
                                    } else {
                                        btn_submit.setVisibility(View.GONE);
                                    }
                                }
                            });
                            rv_player.setAdapter(adapter);

                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

    private void getJoinContest() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL+ Global.JOIN_FANTASY_MATCH_CONTEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.d(response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                               Toaster.customToast(jsonObject.getString("message"));
                               btn_submit.setText("Go to your score board");

                                JSONArray jsonArray= jsonObject.getJSONArray("data");

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                if(jsonObject.optJSONObject("errors").optString("error_text").equalsIgnoreCase("Already Joined this match")){
                                    btn_submit.setText("Go to your score board");
                                }else{
                                    btn_submit.setText("SUBMIT");
                                }

                                Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                btn_submit.setText("SUBMIT");
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
                        btn_submit.setText("SUBMIT");
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
                param.put("player_json", jsonArraySelectedItem.toString());
                param.put("match_id", matchId);
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private long getTimer(String timer) {
        String timerFinal = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy, HH:mm");
        formatter.setLenient(false);
        long milliseconds = 0;

        final CountDownTimer mCountDownTimer;
        final long startTime;
        Date endDate;
        try {
            endDate = formatter.parse(timer);
            assert endDate != null;
            milliseconds = endDate.getTime();
            Log.d("mili", milliseconds + "");

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return milliseconds;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}