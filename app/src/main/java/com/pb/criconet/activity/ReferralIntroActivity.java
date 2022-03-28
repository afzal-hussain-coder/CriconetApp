package com.pb.criconet.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;
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
    String statement3="";
    String URL_LINK = "";
    String image = "";
    Bitmap b;
    int total_points;
    ArrayList<RewardsModell>rewardsModells=null;
    TextView tv_referralNumber;
    CustomLoaderView loaderView;
    String generate_new_ref="";
    Uri imageUri;
    URL url;
    TextView tv_copy,tv_copied;
    ImageView iv_copy;
    ImageView iv_banner;
    String gif_url;
    String msg;

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
        iv_banner =findViewById(R.id.iv_banner);
        iv_copy = findViewById(R.id.iv_copy);
        tv_copy = findViewById(R.id.tv_copy);
        tv_copied = findViewById(R.id.tv_copied);
        rvRewards = findViewById(R.id.rvRewards);
        rvRewards.setLayoutManager(new GridLayoutManager(this, 4));

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnTouchListener((view, motionEvent) -> true);
        tv_referralNumber = findViewById(R.id.tv_referralNumber);


        fl_referFriend = findViewById(R.id.fl_referFriend);
        fl_referFriend.setOnClickListener(view -> {
            loaderView.showLoader();
            generate_new_ref="1";
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loaderView.hideLoader();
                    sendCode(msg,imageUri);
                }
            },20000);

        });

    }

    private void shareDeepLink(String deepLink,Bitmap bitmap) {

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

                            //Log.e("DynamicLink", "shortLink: " + shortLink + System.lineSeparator());
                            //Log.e("DynamicLink", "flowChartLink: " + flowchartLink + System.lineSeparator());


                            //Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.onlinecoaching);
                            //Log.d("Bitmap",bitmap+"");
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String path = MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), bitmap, "IMG_" + Calendar.getInstance().getTime(), null);
                            imageUri = Uri.parse(path);
                            msg = statement1 + "\n" + "\n" + "*" + statement2 + "*" + "\n" + "\n" + statement3 +"\n" + shortLink + "?code=" + ref_code;

                        } else {
                            Toast.makeText(mActivity, "Failed to share event.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void shareDeepLinkk(String deepLink,String code) {

        com.google.android.gms.tasks.Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(deepLink))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull @NotNull com.google.android.gms.tasks.Task<ShortDynamicLink> task) {

                        //Log.d("task",task.getException().getMessage());
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = Objects.requireNonNull(task.getResult()).getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                           // Log.e("DynamicLink", "shortLink: " + shortLink + System.lineSeparator());
                            //Log.e("DynamicLink", "flowChartLink: " + flowchartLink + System.lineSeparator());

                            try{
                                String msg = shortLink + "?code=" + code;
                                tv_copy.setText(msg);
                                iv_copy.setOnClickListener(view -> {
                                    if(tv_copy.getText().toString().trim().isEmpty()){
                                        Toaster.customToast("No Content for copy");
                                    }else{
                                        copyToClipBoard();
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mActivity, "Failed to share event.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void sendCode(String message, Uri bitmap){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bitmap);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ContentResolver contentResolver = getContentResolver ();
                if (imageUri != null) {
                    contentResolver.delete (imageUri,null ,null );
                }

            }
        },30000);
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

    private class DownloadTask extends AsyncTask<URL,Void,Bitmap>{
        protected void onPreExecute(){
           // mProgressDialog.show();
        }
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;
            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                return BitmapFactory.decodeStream(bufferedInputStream);
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        // When all async task done
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog
            //mProgressDialog.dismiss();
            if(result==null){
                //b=result;
                Toast.makeText(mActivity, "Error", Toast.LENGTH_SHORT).show();
                // mImageView.setImageBitmap(result);
            } else {
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

                shareDeepLink(dynamicLinkUri.toString(),result);
                // Notify user that an error occurred while downloading image
            }
        }
    }
    protected URL stringToURL(String  urll) {
        try {
            url = new URL(urll);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
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
                        if (jsonObject1.has("ref_code")) {
                            ref_code = jsonObject1.getString("ref_code");
                        }
                        if (jsonObject1.has("statement1")) {
                            statement1 = jsonObject1.getString("statement1");
                        }
                        if (jsonObject1.has("statement2")) {
                            statement2 = jsonObject1.getString("statement2");
                        }
                        if (jsonObject1.has("statement3")) {
                            statement3 = jsonObject1.getString("statement3");
                        }
                        if (jsonObject1.has("image1")) {
                            gif_url = jsonObject1.getString("image1");
                            Glide.with(mActivity)
                                    .load(gif_url)
                                    .into(iv_banner);
                        }

                        if(jsonObject1.has("total_register_points")){
                            if(jsonObject1.getString("total_register_points").equalsIgnoreCase("null")){
                                total_points = 0;
                            }else{
                                total_points = Integer.parseInt(jsonObject1.getString("total_register_points"));
                                tv_referralNumber.setText(String.valueOf(total_points));
                                seekBar.setThumb(getThumb(total_points));
                                seekBar.setProgress(total_points);
                            }

                        }
                        if(jsonObject1.has("total_registration")){
                            if(jsonObject1.getString("total_registration").equalsIgnoreCase("null")){
                                total_points = 0;
                            }else{
                                total_points = Integer.parseInt(jsonObject1.getString("total_registration"));
                                tv_referralNumber.setText(String.valueOf(total_points));
                            }

                        }


                        if(jsonObject1.has("user_rewards_points_table")){
                            JSONArray  jsonArray= jsonObject1.getJSONArray("user_rewards_points_table");

                            rewardsModells = new ArrayList<>();
                            for(int i=0;i<jsonArray.length();i++){
                                RewardsModell rewardsModell= new RewardsModell(jsonArray.getJSONObject(i));
                                rewardsModells.add(rewardsModell);
                            }
                            rewardsAdapter = new RewardsAdapter(mActivity,rewardsModells);
                            rvRewards.setAdapter(rewardsAdapter);
                        }
                        if (jsonObject1.has("downloaded_img_url")) {
                            image = jsonObject1.getString("downloaded_img_url");
                            new DownloadTask().execute(stringToURL(image));
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
                        Log.d("link",dynamicLinkUri.toString());

                        shareDeepLinkk(dynamicLinkUri.toString(),ref_code);

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

    private void copyToClipBoard()
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(mActivity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", tv_copy.getText());
        if (clipboard == null || clip == null) return;
        clipboard.setPrimaryClip(clip);
        if(clipboard.hasPrimaryClip()){
            tv_copied.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv_copied.setVisibility(View.GONE);
                }
            },1000);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(mActivity, MainActivity.class);
        startActivity(intent);
        finish();
    }
}