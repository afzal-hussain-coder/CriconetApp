package com.pb.criconet.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.github.chrisbanes.photoview.PhotoView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.models.CoachLanguage;
import com.pb.criconet.models.CoachList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class visitCoachProfileActivity extends AppCompatActivity {

    private CircleImageView image_profile;
    private RatingBar rating_bar_review;
    private TextView tvCoachName;
    private TextView tvCoachTitle;
    private TextView tvCoachExp;
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
    private TextView tvOfferPrice,tvLanguage_details;
    private RoundedImageView img_banner,img_certificate;
    RelativeLayout lay_specilization,rl_session_available_or_not,rel_language,rl_certificate;
    String ADAYS="",ADAYS_msg="";
    ImageView img_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coatch_profile);
        mContext =this;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
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
        img_edit = toolbar.findViewById(R.id.img_edit);
        img_edit.setVisibility(View.GONE);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(R.string.coach_profile);

        initializeView();
        getIntentValue();
    }

    private void initializeView() {

        image_profile = findViewById(R.id.image_profile);
        rating_bar_review = findViewById(R.id.rating_bar_review);
        tvCoachName = findViewById(R.id.tvCoachName);
        tvCoachTitle = findViewById(R.id.tvCoachTitle);
        tvCoachExp = findViewById(R.id.tvCoachExp);
        tv_booked_session = findViewById(R.id.tv_booked_session);
        lay_specilization = findViewById(R.id.lay_specilization);
        tvCoachSpecialization= findViewById(R.id.tvCoachSpecialization);
        rel_achievement = findViewById(R.id.rel_achievement);
        rl_bio = findViewById(R.id.rl_bio);
        tvPrice=findViewById(R.id.tvPrice);

        li_offer = findViewById(R.id.li_offer);
        rl_session_available_or_not = findViewById(R.id.rl_session_available_or_not);
        tvBookNow=findViewById(R.id.tvBookNow);
        tvAchievement_details=findViewById(R.id.tvAchievement_details);
        rel_language = findViewById(R.id.rel_language);
        tvLanguage_details = findViewById(R.id.tvLanguage_details);
        img_certificate = findViewById(R.id.img_certificate);
        rl_certificate = findViewById(R.id.rl_certificate);
        tvBio_details=findViewById(R.id.tvBio_details);
        rl_offer = findViewById(R.id.rl_offer);
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

    private void getIntentValue(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            coachId=extras.getString("coachId");
            rating=extras.getInt("rating");
            if (!extras.getString("image").isEmpty()) {
                Glide.with(mContext).load(extras.getString("image"))
                        .into(image_profile);
            } else {
                Glide.with(mContext).load(R.drawable.placeholder_user)
                        .into(image_profile);
            }

            if((getIntent().getBundleExtra("Certificate") ==null)){
                rl_certificate.setVisibility(View.GONE);
            }else{
                Bundle args = getIntent().getBundleExtra("Certificate");
                ArrayList<CoachList.certificate> object = (ArrayList<CoachList.certificate>) args.getSerializable("ARRAYLIST");
                rl_certificate.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(object.get(0).getCertificate_url())
                        .into(img_certificate);
                img_certificate.setOnClickListener(v -> {
                    imageViewDialog(object.get(0).getCertificate_url());
                });

            }
            if (!extras.getString("Cover").isEmpty()) {
                Glide.with(mContext).load(extras.getString("Cover"))
                        .into(img_banner);
            } else {
                Glide.with(mContext).load(R.drawable.bg_coach)
                        .into(img_banner);
            }

            if(extras.getString("exp").equals("0")){
                tvCoachExp.setVisibility(View.GONE);
            }else{
                tvCoachExp.setVisibility(View.VISIBLE);
                tvCoachExp.setText(extras.getString("exp"));
            }
            offerpersantage = extras.getString("Offer");
            ADAYS = extras.getString("ADAYS");
            ADAYS_msg = extras.getString("ADAYS_msg");

            if(ADAYS.equalsIgnoreCase("0")){
                rl_session_available_or_not.setVisibility(View.VISIBLE);
                tv_booked_session.setText(ADAYS_msg);
                rl_offer.setVisibility(View.GONE);
            }else{
                rl_session_available_or_not.setVisibility(View.GONE);
                if(offerpersantage.equalsIgnoreCase("0")){
                    rl_offer.setVisibility(View.GONE);
                }else{
                    tvoffer.setText(offerpersantage+"% "+"OFF");
                    rl_offer.setVisibility(View.VISIBLE);

                }
            }


            if (extras.getString("OfferID").equalsIgnoreCase("0")) {
                li_offer.setVisibility(View.GONE);
            } else {
                tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvOfferPrice.setText("\u20B9" + extras.getString("OfferPrice") + "/" + getResources().getString(R.string.session));
                li_offer.setVisibility(View.VISIBLE);
            }

            if (extras.getString("Language")==null) {
                rel_language.setVisibility(View.GONE);
            } else {
                tvLanguage_details.setText(extras.getString("Language"));
                rel_language.setVisibility(View.VISIBLE);
            }


            if (extras.getInt("rating")==0){
                rating_bar_review.setVisibility(View.GONE);
            }else{
                rating_bar_review.setVisibility(View.VISIBLE);
                rating_bar_review.setRating(Float.parseFloat(String.valueOf(extras.getInt("rating"))));
            }

            tvCoachName.setText(Global.capitizeString(extras.getString("name")));
            tvCoachTitle.setText(extras.getString("coachTitle"));

            if(extras.getString("specialization")==null||extras.getString("specialization").equalsIgnoreCase("")){
                lay_specilization.setVisibility(View.GONE);
            }else{
                lay_specilization.setVisibility(View.VISIBLE);
                tvCoachSpecialization.setText(extras.getString("specialization"));
            }



            if (!extras.getString("achievementDetails").isEmpty()){
                rel_achievement.setVisibility(View.VISIBLE);
                tvAchievement_details.setText(extras.getString("achievementDetails"));
            }else{
                rel_achievement.setVisibility(View.GONE);
            }

            if (!extras.getString("des").isEmpty()){
                rl_bio.setVisibility(View.VISIBLE);
                tvBio_details.setText(extras.getString("des"));
            }else{
                rl_bio.setVisibility(View.GONE);
            }


            tvPrice.setText(getResources().getString(R.string.price)+"\u20B9" +extras.getString("price")+"/"+getResources().getString(R.string.session));

            // and get whatever type user account id is
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