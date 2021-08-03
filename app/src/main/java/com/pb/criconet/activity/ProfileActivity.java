package com.pb.criconet.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.models.UserData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity {
    private LinearLayout llFollowers;
    private LinearLayout llFolloweing;
    private LinearLayout llFolloweingRequest;
    private TextView tvNumFollowers;
    private TextView tvNumFollowing;
    private TextView tvNumFollowingRequest;
    private ImageView ivEditProfile;
    private SharedPreferences prefs;
    private AppCompatActivity mActivity;
    private CircleImageView profile_pic;
    private ImageView cover_img;
    private TextView txt_nav_name;
    private UserData userData = new UserData();
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mActivity= ProfileActivity.this;
        queue = Volley.newRequestQueue(ProfileActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v ->
        {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText("Profile");
        profile_pic=findViewById(R.id.profile_pic);
        cover_img=findViewById(R.id.cover_img);
        txt_nav_name=findViewById(R.id.txt_nav_name);

        ivEditProfile = findViewById(R.id.ivEditProfile);
        llFollowers = findViewById(R.id.llFollowers);
        llFolloweing = findViewById(R.id.llFolloweing);
        llFolloweingRequest = findViewById(R.id.llFolloweingRequest);
        tvNumFollowers = findViewById(R.id.tvNumFollowers);
        tvNumFollowing = findViewById(R.id.tvNumFollowing);
        tvNumFollowingRequest = findViewById(R.id.tvNumFollowingRequest);
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

        ivEditProfile.setOnClickListener(view -> {
            startActivity(new Intent(mActivity,EditProfile.class).putExtra("FROM","1"));
            finish();
        });


        if (SessionManager.get_image(prefs) != null && !SessionManager.get_image(prefs).isEmpty()) {
            Glide.with(mActivity).load(SessionManager.get_image(prefs)).dontAnimate().into(profile_pic);
            Glide.with(mActivity).load(SessionManager.get_cover(prefs)).dontAnimate().into(cover_img);
            txt_nav_name.setText(SessionManager.get_name(prefs));
        }

        navigationController.navigatoFollowers();


        llFollowers.setOnClickListener(view1 -> {
            navigationController.navigatoFollowers();
        });
        llFolloweing.setOnClickListener(view1 -> {
            navigationController.navigatoFollowing();
        });
        llFolloweingRequest.setOnClickListener(view1 -> {
            navigationController.navigatoFollowingRequest();
        });

        if (Global.isOnline(ProfileActivity.this)) {
            getUsersDetails();
        } else {
            Global.showDialog(ProfileActivity.this);
        }
    }
    public void getUsersDetails() {
        //progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_user_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Timber.v(response);

                        Log.d("UserDetailsResponse",response);
                        //progress.dismiss();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                JSONObject object = jsonObject.getJSONObject("user_data");
                                userData = UserData.fromJson(object);
                                tvNumFollowers.setText(userData.getCount_followers());
                                tvNumFollowing.setText(userData.getCount_following());
                                tvNumFollowingRequest.setText(userData.getCount_follow_request());

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(ProfileActivity.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(ProfileActivity.this, "Error in server");
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
                       // progress.dismiss();
                        Global.msgDialog(ProfileActivity.this, "Error from server");
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("user_profile_id", SessionManager.get_user_id(prefs));
//                param.put("s", "1");
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
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
