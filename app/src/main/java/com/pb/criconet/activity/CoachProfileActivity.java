package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.github.chrisbanes.photoview.PhotoView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.models.CoachLanguage;
import com.pb.criconet.models.CoachList;
import com.pb.criconet.models.Drawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CoachProfileActivity extends AppCompatActivity {

    private CircleImageView image_profile;
    private RatingBar rating_bar_review;
    private TextView tvCoachName;
    private TextView tvCoachTitle;
    private TextView tvCoachExp;
    private TextView tvCertificate;
    private TextView tvCoachSpecialization;
    private TextView tvPrice;
    private FrameLayout tvBookNow;
    private TextView tvAchievement_details,tv_booked_session;
    private TextView tvBio_details;
    private Context mContext;
    private String coachId,offerpersantage;
    private RelativeLayout rel_achievement;
    private int rating;
    private RelativeLayout rl_bio;
    private SharedPreferences prefs;
    private RelativeLayout rl_offer;
    private TextView tvoffer;
    private LinearLayout li_offer;
    private TextView tvOfferPrice,tvLanguage_details,tvwht_learn_details, tvskills_you_learn_details;
    private RoundedImageView img_banner,img_certificate;
    RelativeLayout lay_specilization,rl_session_available_or_not,rel_language,rl_certificate,rel_wht_learn, rel_skills_you_learn;
    String ADAYS="",ADAYS_msg="";
    ImageView img_edit;
    private RequestQueue queue;
    String firtsName="",lastName="",middleName="",exp_years="",exp_month="";
    ArrayList<String>coachLanguages = new ArrayList<>();
    private StringBuilder langStringBuilder;
    ArrayList<CoachList.certificate> object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coatch_profile);
        mContext =this;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);
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
        mTitle.setText(R.string.coach_profile);



        img_edit = toolbar.findViewById(R.id.img_edit);
        if (SessionManager.get_profiletype(prefs).equalsIgnoreCase("coach")) {
            img_edit.setVisibility(View.VISIBLE);
            img_edit.setOnClickListener(view -> {
                startActivity(new Intent(mContext,CoachRegistrationActivity.class).putExtra("FROM","1"));
                finish();
            });

            //text.add(new Drawer(getString(R.string.join_as_coach),false, R.drawable.ic_sports_handball_black_24dp));
        }else{
            img_edit.setVisibility(View.GONE);
        }

        initializeView();
        if (Global.isOnline(mContext)) {
            getUsersDetails();
        } else {
            Global.showDialog((Activity) mContext);
        }

    }

    private void initializeView() {
        image_profile = findViewById(R.id.image_profile);
        rating_bar_review = findViewById(R.id.rating_bar_review);
        tvCoachName = findViewById(R.id.tvCoachName);
        tvCoachTitle = findViewById(R.id.tvCoachTitle);
        tvCoachExp = findViewById(R.id.tvCoachExp);
        tvCertificate = findViewById(R.id.tvCertificate);
        tv_booked_session = findViewById(R.id.tv_booked_session);
        lay_specilization = findViewById(R.id.lay_specilization);
        tvCoachSpecialization= findViewById(R.id.tvCoachSpecialization);
        rel_achievement = findViewById(R.id.rel_achievement);
        rel_wht_learn = findViewById(R.id.rel_wht_learn);
        rel_skills_you_learn = findViewById(R.id.rel_skills_you_learn);
        tvwht_learn_details = findViewById(R.id.tvwht_learn_details);
        tvskills_you_learn_details = findViewById(R.id.tvskills_you_learn_details);
        rl_bio = findViewById(R.id.rl_bio);
        tvPrice=findViewById(R.id.tvPrice);

        li_offer = findViewById(R.id.li_offer);
        li_offer.setVisibility(View.GONE);
        rl_session_available_or_not = findViewById(R.id.rl_session_available_or_not);
        tvBookNow=findViewById(R.id.tvBookNow);
        tvAchievement_details=findViewById(R.id.tvAchievement_details);
        rel_language = findViewById(R.id.rel_language);
        tvLanguage_details = findViewById(R.id.tvLanguage_details);
        img_certificate = findViewById(R.id.img_certificate);
        rl_certificate = findViewById(R.id.rl_certificate);
        tvBio_details=findViewById(R.id.tvBio_details);
        rl_offer = findViewById(R.id.rl_offer);
        //rl_offer.setVisibility(View.GONE);
        tvoffer = findViewById(R.id.tvoffer);
        tvOfferPrice = findViewById(R.id.tvOfferPrice);
        img_banner = findViewById(R.id.img_banner);

        if(SessionManager.getProfileType(prefs).equalsIgnoreCase("coach")){
            tvBookNow.setVisibility(View.GONE);
        }else{
            tvBookNow.setVisibility(View.VISIBLE);
        }
        tvBookNow.setOnClickListener(v -> {
            Intent intent =new Intent(mContext,CoachDetailsActivity.class);
            intent.putExtra("coachId",coachId);
            intent.putExtra("rating",rating);
            startActivity(intent);
        });

    }

    public void getUsersDetails() {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_user_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("getUserDetails",response);
                        //loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                JSONObject object = jsonObject.getJSONObject("coach_data");
                                JSONObject jsonObject_personal_info=null;
                                JSONObject jsonObject_professional_info=null;
                                if(object.has("personal_info")){
                                    jsonObject_personal_info= object.getJSONObject("personal_info");
                                }
                                if(object.has("coach_info")){
                                    jsonObject_professional_info= object.getJSONObject("coach_info");
                                }
                                setData(jsonObject_personal_info,jsonObject_professional_info);
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog((Activity) mContext, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
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
                        //loaderView.hideLoader();
                        Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("user_profile_id", SessionManager.get_user_id(prefs));
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
    private void setData(JSONObject  data,JSONObject jsonObject_professional_info) {

        if(data.has("first_name")){
            firtsName = data.optString("first_name");
        }
        if(data.has("last_name")){
            lastName= data.optString("last_name");
        }
        if(data.has("mid_name")){
            middleName = data.optString("mid_name");
        }

        if(lastName.equalsIgnoreCase("")){
            tvCoachName.setText(firtsName+" "+middleName);
        }else if(middleName.equalsIgnoreCase("")){
            tvCoachName.setText(firtsName+" "+lastName);
        }else{
            tvCoachName.setText(firtsName+" "+middleName+" "+lastName);
        }


        if(data.has("avatar")){

            if (!data.optString("avatar").isEmpty()) {
                Glide.with(mContext).load(data.optString("avatar"))
                        .into(image_profile);
            } else {
                Glide.with(mContext).load(R.drawable.placeholder_user)
                        .into(image_profile);
            }
        }

        if (!data.optString("Cover").isEmpty()) {
            Glide.with(mContext).load(data.optString("Cover"))
                    .into(img_banner);
        } else {
            Glide.with(mContext).load(R.drawable.bg_coach)
                    .into(img_banner);
        }

        if (jsonObject_professional_info.has("profile_title")) {
            tvCoachTitle.setText(jsonObject_professional_info.optString("profile_title"));
        }
//        if (!jsonObject_professional_info.optString("achievementDetails").isEmpty()){
//            rel_achievement.setVisibility(View.VISIBLE);
//            tvAchievement_details.setText(jsonObject_professional_info.optString("achievementDetails"));
//        }else{
//            rel_achievement.setVisibility(View.GONE);
//        }
        if (!jsonObject_professional_info.optString("achievement").isEmpty()){
            rel_achievement.setVisibility(View.VISIBLE);
            tvAchievement_details.setText(jsonObject_professional_info.optString("achievement"));
        }else{
            rel_achievement.setVisibility(View.GONE);
        }

        if (!jsonObject_professional_info.optString("about_coach_profile").isEmpty()){
            rl_bio.setVisibility(View.VISIBLE);
            tvBio_details.setText(jsonObject_professional_info.optString("about_coach_profile"));
        }else{
            rl_bio.setVisibility(View.GONE);
        }

        if (!jsonObject_professional_info.optString("what_you_teach").isEmpty()) {
            rel_wht_learn.setVisibility(View.VISIBLE);
            tvwht_learn_details.setText(Html.fromHtml(jsonObject_professional_info.optString("what_you_teach")));
            //tvwht_learn_details.setText(extras.getString("what_you_teach"));
        } else {
            rel_wht_learn.setVisibility(View.GONE);
        }

        if (!jsonObject_professional_info.optString("skills_you_learn").isEmpty()) {
            rel_skills_you_learn.setVisibility(View.VISIBLE);
            tvskills_you_learn_details.setText(jsonObject_professional_info.optString("skills_you_learn"));
        } else {
            rel_skills_you_learn.setVisibility(View.GONE);
        }


//        if (extras.getInt("rating")==0){
//            rating_bar_review.setVisibility(View.GONE);
//        }else{
//            rating_bar_review.setVisibility(View.VISIBLE);
//            rating_bar_review.setRating(Float.parseFloat(String.valueOf(extras.getInt("rating"))));
//        }
        rating_bar_review.setVisibility(View.GONE);
        tvPrice.setText(getResources().getString(R.string.price)+"\u20B9" +jsonObject_professional_info.optString("charge_amount")+"/"+getResources().getString(R.string.session));



        if(jsonObject_professional_info.optString("cat_title").isEmpty()){
            lay_specilization.setVisibility(View.GONE);
        }else{
            lay_specilization.setVisibility(View.VISIBLE);
            tvCoachSpecialization.setText(jsonObject_professional_info.optString("cat_title"));
        }

        /*if(data.has("languages")){

            try {
                JSONArray jsonArray = data.getJSONArray("languages");
                JSONObject jsonObject=null;
                CoachLanguage coachLanguage=null;
                for(int i= 0;i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    coachLanguage= new CoachLanguage(jsonObject);
                    coachLanguages.add(coachLanguage.getName_eng());
                }

                langStringBuilder = new StringBuilder();
                String prefix = "";
                for (String item : coachLanguages) {
                    langStringBuilder.append(prefix);
                    prefix = ",";
                    langStringBuilder.append(item);
                }
                //Log.d("size",langStringBuilder.toString());

                tvLanguage_details.setText(langStringBuilder.toString());

                Log.d("size",coachLanguages.size()+"");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/


        if(data.has("user_lang")){
            tvLanguage_details.setText(data.optString("user_lang"));
        }

        if (jsonObject_professional_info.has("exp_years")) {
            exp_years=jsonObject_professional_info.optString("exp_years");

        }
        if (jsonObject_professional_info.has("exp_months")) {
            exp_month=jsonObject_professional_info.optString("exp_months");
        }

        if(exp_years.equalsIgnoreCase("1") &&exp_month.equalsIgnoreCase("1") ){
            tvCoachExp.setText(exp_years+"."+exp_month+" "+"Year Experience");
        }else if(exp_years.equalsIgnoreCase("") &&exp_month.equalsIgnoreCase("1")){
            tvCoachExp.setText(exp_month+" "+"Month Experience");
        }else if(exp_month.equalsIgnoreCase("")){
            if(exp_years.equalsIgnoreCase("1")){
                tvCoachExp.setText(exp_years+" "+"Year Experience");
            }else{
                tvCoachExp.setText(exp_years+" "+"Years Experience");
            }

        }

        else if(exp_years.equalsIgnoreCase("0")){
            if(exp_month.equalsIgnoreCase("1")){
                tvCoachExp.setText(exp_month+" "+"Month Experience");
            }else{
                tvCoachExp.setText(exp_month+" "+"Months Experience");
            }


        }
        else {
            tvCoachExp.setText(exp_years+"."+exp_month+" "+"Years Experience");
        }


        if(jsonObject_professional_info.has("certificate_url")){
            if((jsonObject_professional_info.optString("certificate_url").isEmpty())){
                rl_certificate.setVisibility(View.GONE);
            }else{
                rl_certificate.setVisibility(View.VISIBLE);
                tvCertificate.setText("Certificate: "+jsonObject_professional_info.optString("certificate_title") );
                Glide.with(mContext).load(jsonObject_professional_info.optString("certificate_url"))
                        .into(img_certificate);
                img_certificate.setOnClickListener(v -> {
                    imageViewDialog(jsonObject_professional_info.optString("certificate_url"));
                });

            }
                /*JSONArray jsonArray=jsonObject_professional_info.getJSONArray("certificate");
                JSONObject modelJson;
                object = new ArrayList<>();
                // Process each result in json array, decode and convert to CardModel object
                for (int i = 0; i < jsonArray.length(); i++) {

                        modelJson = jsonArray.getJSONObject(i);

                    CoachList.certificate model = new CoachList.certificate(modelJson);
                    if (model != null) {
                        object.add(model);
                    }
                }
                Log.d("link",object.size()+"");*/

        }

    }

    void imageViewDialog(String url) {

        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.profiledialog);
        dialog.setCancelable(true);
        PhotoView img = (PhotoView) dialog.findViewById(R.id.image_view);
        ImageView del = (ImageView) dialog.findViewById(R.id.del);
//        img.setScale(3);
        Glide.with(mContext).load(url)
                .into(img);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}