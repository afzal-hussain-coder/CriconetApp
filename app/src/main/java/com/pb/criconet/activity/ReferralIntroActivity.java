package com.pb.criconet.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.RewardsAdapter;
import com.pb.criconet.models.FeedBackFormChildData;
import com.pb.criconet.models.OrderCreate;
import com.pb.criconet.models.RewardsModell;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class ReferralIntroActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private RequestQueue queue;
    Activity mActivity;
    RecyclerView rvRewards;
    RewardsAdapter rewardsAdapter;
    SeekBar seekBar;
    TextView tvProgress;
    View thumbView;
    FrameLayout fl_referFriend;
    DynamicLink dynamicLink;
    String ref_code = "";
    String statement1 = "";
    String statement2 = "";
    String URL_LINK = "";
    String image = "";
    Bitmap b;
    int total_points;
    ArrayList<RewardsModell>rewardsModells=null;
    TextView tv_referralNumber;
    CustomLoaderView loaderView;
    String generate_new_ref="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_intro);
        loaderView = CustomLoaderView.initialize(this);
        mActivity = this;
        thumbView = LayoutInflater.from(mActivity).inflate(R.layout.layout_seekbar_thumb, null, false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mActivity, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        queue = Volley.newRequestQueue(mActivity);
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(getResources().getString(R.string.referrar_rewards));
        if (Global.isOnline(mActivity)) {
            getReferralCode();
        } else {
            Global.showDialog(mActivity);
        }
        initializeView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeView() {
        rvRewards = findViewById(R.id.rvRewards);
        rvRewards.setLayoutManager(new GridLayoutManager(this, 4));

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnTouchListener((view, motionEvent) -> true);
        tv_referralNumber = findViewById(R.id.tv_referralNumber);


        fl_referFriend = findViewById(R.id.fl_referFriend);
        fl_referFriend.setOnClickListener(view -> {
            generate_new_ref="1";
            if (Global.isOnline(mActivity)) {
                getReferralCodee();
            } else {
                Global.showDialog(mActivity);
            }

        });

    }


    private void shareDeepLink(String deepLink) {

        com.google.android.gms.tasks.Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(deepLink))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = Objects.requireNonNull(task.getResult()).getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Log.e("DynamicLink", "shortLink: " + shortLink + System.lineSeparator());
                            Log.e("DynamicLink", "flowChartLink: " + flowchartLink + System.lineSeparator());


                            //Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.onlinecoaching);
                            Log.d("Bitmap",b+"");

                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/*");
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String path = MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), b, "IMG_" + Calendar.getInstance().getTime(), null);
                            Uri imageUri = Uri.parse(path);

                            String msg = statement1 + "\n" + "\n" + "*" + statement2 + "*" + "\n" + "\n" + "Download Criconet here - " + shortLink + "?code=" + ref_code;
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                            shareIntent.setType("image/*");
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(shareIntent, "send"));

                        } else {
                            Toast.makeText(mActivity, "Failed to share event.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public Drawable getThumb(int progress) {
        tvProgress = thumbView.findViewById(R.id.tvProgress);
        tvProgress.setText(progress + "");
        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
// Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
// Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
// Set the bitmap into ImageView
       b = result;
        }
    }


    private void getReferralCode() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_REFERRAL_CODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loaderView.hideLoader();
                Log.d("ReferralCodeResponse", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                        if(jsonObject1.has("total_points")){
                            if(jsonObject1.getString("total_points").equalsIgnoreCase("null")){
                                total_points = 0;
                            }else{
                                total_points = Integer.parseInt(jsonObject1.getString("total_points"));
                                tv_referralNumber.setText(String.valueOf(total_points));
                                seekBar.setThumb(getThumb(total_points));
                                seekBar.setProgress(total_points);
                            }

                        }

                        if(jsonObject1.has("rewards_points_table")){
                            JSONArray  jsonArray= jsonObject1.getJSONArray("rewards_points_table");

                            rewardsModells = new ArrayList<>();
                            for(int i=0;i<jsonArray.length();i++){
                                RewardsModell rewardsModell= new RewardsModell(jsonArray.getJSONObject(i));
                                rewardsModells.add(rewardsModell);
                            }
                            rewardsAdapter = new RewardsAdapter(mActivity,rewardsModells);
                            rvRewards.setAdapter(rewardsAdapter);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                // Global.msgDialog((Activity) mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("generate_new_ref", generate_new_ref);

                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getReferralCodee() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_REFERRAL_CODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loaderView.hideLoader();
                Log.d("ReferralCodeResponse", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                        if (jsonObject1.has("ref_code")) {
                            ref_code = jsonObject1.getString("ref_code");
                        }
                        if (jsonObject1.has("statement1")) {
                            statement1 = jsonObject1.getString("statement1");
                        }
                        if (jsonObject1.has("statement2")) {
                            statement2 = jsonObject1.getString("statement2");
                        }
                        if (jsonObject1.has("URL")) {
                            URL_LINK = jsonObject1.getString("URL");
                        }
                        if (jsonObject1.has("downloaded_img_url")) {
                            image = jsonObject1.getString("downloaded_img_url");
                            new DownloadImage().execute(image);
                        }
                        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                .setLink(Uri.parse("https://www.criconet.com/"))
                                .setDomainUriPrefix("https://criconet.page.link")
                                .setAndroidParameters(
                                        new DynamicLink.AndroidParameters.Builder("com.pb.criconet")
                                                .setFallbackUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.pb.criconet"))
                                                .setMinimumVersion(1)
                                                .build())
                                .buildDynamicLink();

                        Uri dynamicLinkUri = dynamicLink.getUri();

                        shareDeepLink(dynamicLinkUri.toString());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                // Global.msgDialog((Activity) mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("generate_new_ref", generate_new_ref);

                Timber.e(param.toString());
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

        Intent intent = new Intent(mActivity, MainActivity.class);
        startActivity(intent);
        finish();
    }
}