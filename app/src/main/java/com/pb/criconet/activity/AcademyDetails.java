package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.card.MaterialCardView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.pb.criconet.R;
import com.pb.criconet.Utills.ExoPlayerRecyclerView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.GridSpacingItemDecoration;
import com.pb.criconet.Utills.HorizontalSpaceItemDecoration;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.AcademyCoachListAdapter;
import com.pb.criconet.adapters.ImageGalleryAdapter;
import com.pb.criconet.adapters.MediaRecyclerAdapter;
import com.pb.criconet.adapters.TimeAdapterAcademy;
import com.pb.criconet.adapters.TimeAdaptercoachh;
import com.pb.criconet.adapters.VideoGalleryAdapter;
import com.pb.criconet.models.AcademyListModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class AcademyDetails extends AppCompatActivity {

    private CircleImageView image_profile;
    private RatingBar rating_bar_review, rating_bar_revieww;
    private TextView tvCoachName;
    private TextView tvCoachTitle;
    private TextView tvCoachExp;
    private TextView tvCoachSpecialization;
    private TextView tvPrice;
    private FrameLayout tvBookNow;
    private TextView tvAchievement_details, tv_booked_session;
    private TextView tvBio_details;
    private Context mContext;
    private String coachId, offerpersantage;
    private RelativeLayout rel_achievement;
    private int rating;
    private RelativeLayout rl_bio;
    private SharedPreferences prefs;
    private RelativeLayout rl_offer;
    private TextView tvoffer;
    private LinearLayout li_offer;
    private TextView tvOfferPrice, tvLanguage_details, tvwht_learn_details, tvskills_you_learn_details;
    private RoundedImageView img_banner;
    RelativeLayout lay_specilization, rl_session_available_or_not, rel_language, rl_certificate, rel_wht_learn, rel_skills_you_learn;
    String ADAYS = "", ADAYS_msg = "";
    ImageView img_edit;

    private SlideUp slideUp;
    private View dim, rootVieww;
    private View slideView;
    TextView tv_person_phone, tv_person_email, tv_person;
    ImageButton ib_facebook, ib_instagram, ib_twitter, ib_linked, ib_youyube;
    MediaController mediaController;
    RecyclerView rv_gallery, recycler_view,rv_coach;
    ExoPlayerRecyclerView rv_video;

    ArrayList<Integer> imageGalleryList = new ArrayList<>();
    ArrayList<String> videoGalleryList = new ArrayList<>();
    LinearLayout li_youtube, li_lined, li_twi, li_ins, li_fb;
    Button btn_done;
    String selectedTimeSlott = "", selectedTimeSlot = "";
    AcademyListModel academyListModel = null;
    MaterialCardView cv_buttom,cv_buttomm, md_video,cv_coach;
    TextView tv_academyName, tv_academyAddress, tv_price, tv_specilities;
    LinearLayout li_social;
    TimeAdapterAcademy timeAdapterAcademy=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_details);
        mContext = this;
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
        mTitle.setText(R.string.academy_profile);
        rootVieww = findViewById(R.id.root_view);
        slideView = findViewById(R.id.slideView);
        dim = findViewById(R.id.dim);
        btn_done = findViewById(R.id.btn_done);
        slideUp = new SlideUpBuilder(slideView)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                        dim.setAlpha(1 - (percent / 100));
                        if (percent < 100) {
                            // slideUp started showing

                        }
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                        }
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(rootVieww)
                .build();


        initializeView();
        getIntentValue();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initializeView() {

        image_profile = findViewById(R.id.image_profile);
        rating_bar_review = findViewById(R.id.rating_bar_review);
        rating_bar_revieww = findViewById(R.id.rating_bar_revieww);
        tvCoachName = findViewById(R.id.tvCoachName);
        tvCoachTitle = findViewById(R.id.tvCoachTitle);
        tvCoachExp = findViewById(R.id.tvCoachExp);
        tv_booked_session = findViewById(R.id.tv_booked_session);
        lay_specilization = findViewById(R.id.lay_specilization);
        tvCoachSpecialization = findViewById(R.id.tvCoachSpecialization);
        rel_achievement = findViewById(R.id.rel_achievement);
        rel_wht_learn = findViewById(R.id.rel_wht_learn);
        rel_skills_you_learn = findViewById(R.id.rel_skills_you_learn);
        tvwht_learn_details = findViewById(R.id.tvwht_learn_details);
        tvskills_you_learn_details = findViewById(R.id.tvskills_you_learn_details);

        tv_academyName = findViewById(R.id.tv_academyName);
        tv_academyAddress = findViewById(R.id.tv_academyAddress);
        tv_price = findViewById(R.id.tv_price);
        tv_specilities = findViewById(R.id.tv_specilities);

        tv_person_phone = findViewById(R.id.tv_person_phone);
        tv_person_email = findViewById(R.id.tv_person_email);
        tv_person = findViewById(R.id.tv_person);
        li_social = findViewById(R.id.li_social);
        li_youtube = findViewById(R.id.li_youtube);
        li_lined = findViewById(R.id.li_lined);
        li_twi = findViewById(R.id.li_twi);
        li_ins = findViewById(R.id.li_ins);
        li_fb = findViewById(R.id.li_fb);
        ib_facebook = findViewById(R.id.ib_facebook);
        ib_instagram = findViewById(R.id.ib_instagram);
        ib_twitter = findViewById(R.id.ib_twitter);
        ib_linked = findViewById(R.id.ib_linked);
        ib_youyube = findViewById(R.id.ib_youyube);
        //Creating MediaController

        recycler_view = findViewById(R.id.recycler_vieww);
        recycler_view.setLayoutManager(new GridLayoutManager(mContext, 3));
        recycler_view.addItemDecoration(new GridSpacingItemDecoration(3, 10, false));

        cv_coach = findViewById(R.id.cv_coach);
        rv_coach = findViewById(R.id.rv_coach);
        rv_coach.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        rv_coach.addItemDecoration(new HorizontalSpaceItemDecoration(10));

       // cv_buttomm = findViewById(R.id.cv_buttomm);

        cv_buttom = findViewById(R.id.cv_buttom);
        rv_gallery = findViewById(R.id.rv_gallery);
        rv_gallery.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        rv_gallery.addItemDecoration(new HorizontalSpaceItemDecoration(25));

        md_video = findViewById(R.id.md_video);
        rv_video = findViewById(R.id.exoPlayerRecyclerView);
        //rv_video.setLayoutManager(new LinearLayoutManager(this));
        rv_video.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        rv_video.addItemDecoration(new HorizontalSpaceItemDecoration(5));


        rl_bio = findViewById(R.id.rl_bio);
        tvPrice = findViewById(R.id.tvPrice);

        li_offer = findViewById(R.id.li_offer);
        rl_session_available_or_not = findViewById(R.id.rl_session_available_or_not);
        tvBookNow = findViewById(R.id.tvBookNow);
        tvAchievement_details = findViewById(R.id.tvAchievement_details);
        rel_language = findViewById(R.id.rel_language);
        tvLanguage_details = findViewById(R.id.tvLanguage_details);
        rl_certificate = findViewById(R.id.rl_certificate);
        tvBio_details = findViewById(R.id.tvBio_details);
        rl_offer = findViewById(R.id.rl_offer);
        tvoffer = findViewById(R.id.tvoffer);
        tvOfferPrice = findViewById(R.id.tvOfferPrice);
        img_banner = findViewById(R.id.img_banner);

//        if(SessionManager.getProfileType(prefs).equalsIgnoreCase("coach")){
//            tvBookNow.setVisibility(View.GONE);
//        }else{
//            tvBookNow.setVisibility(View.VISIBLE);
//        }
        tvBookNow.setOnClickListener(v -> {
            slideUp.show();

        });
        btn_done.setOnClickListener(v -> {
            if(selectedTimeSlot.equalsIgnoreCase("")){
                Toaster.customToast(getString(R.string.Please_select_time_first));
            }else{

                Intent intent = new Intent(mContext, AcademyBookChcekOutActvity.class);
                intent.putExtra("Certificate", academyListModel);
                intent.putExtra("Slot", selectedTimeSlot);
                intent.putExtra("SlotID", selectedTimeSlott);
                startActivity(intent);
                slideUp.hide();
            }

        });

    }

    private void getIntentValue() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            academyListModel = (AcademyListModel) extras.getSerializable("Certificate");

            tv_academyName.setText(academyListModel.getName());
            tv_academyAddress.setText(academyListModel.getAddress());
            tv_specilities.setText(academyListModel.getCat_title());
            tv_price.setText(getString(R.string.fees) + ": " + "\u20B9" +" "+academyListModel.getFee()+"/"+getString(R.string.month));

            if (academyListModel.getRating().isEmpty()) {
                rating_bar_revieww.setVisibility(View.GONE);
            } else {
                rating_bar_revieww.setVisibility(View.VISIBLE);
                rating_bar_revieww.setRating(Float.parseFloat(academyListModel.getRating()));
            }

            if (!academyListModel.getLogo().isEmpty()) {
                Glide.with(mContext).load(academyListModel.getLogo())
                        .into(image_profile);
            } else {
                Glide.with(mContext).load(R.drawable.placeholder_user)
                        .into(image_profile);
            }
            if (!academyListModel.getBanner_image().isEmpty()) {
                Glide.with(mContext).load(academyListModel.getBanner_image())
                        .into(img_banner);
            } else {
                Glide.with(mContext).load(R.drawable.bg2)
                        .into(img_banner);
            }

            //Toaster.customToast(academyListModel.getImageArryList().toString()+"");
            if (academyListModel.getImageArryList().toString().equals("[]")) {
                rv_gallery.setVisibility(View.GONE);
                cv_buttom.setVisibility(View.GONE);
                //rv_gallery.setAdapter(new ImageGalleryAdapter(mContext, academyListModel.getImageArryList()));
            } else {
                cv_buttom.setVisibility(View.VISIBLE);
                rv_gallery.setVisibility(View.VISIBLE);
                rv_gallery.setAdapter(new ImageGalleryAdapter(mContext, academyListModel.getImageArryList()));
            }
            if (academyListModel.getAcademyCoachesArrayList().isEmpty() || academyListModel.getAcademyCoachesArrayList() == null) {
                rv_coach.setVisibility(View.GONE);
                cv_coach.setVisibility(View.GONE);
                rv_coach.setAdapter(new AcademyCoachListAdapter(academyListModel.getAcademyCoachesArrayList(),mContext));
            } else {
                rv_coach.setVisibility(View.VISIBLE);
                cv_coach.setVisibility(View.VISIBLE);
                rv_coach.setAdapter(new AcademyCoachListAdapter(academyListModel.getAcademyCoachesArrayList(),mContext));
            }



            if (academyListModel.getVideoList().isEmpty() || academyListModel.getVideoList() == null) {
                md_video.setVisibility(View.GONE);
                rv_video.setVisibility(View.GONE);
                rv_video.setMediaObjects(academyListModel.getVideoList());
                rv_video.setAdapter(new MediaRecyclerAdapter(new ArrayList<>(), initGlide()));
                //rv_video.smoothScrollToPosition(1);

            } else {
                md_video.setVisibility(View.VISIBLE);
                rv_video.setVisibility(View.VISIBLE);
                rv_video.setMediaObjects(academyListModel.getVideoList());
                rv_video.setAdapter(new MediaRecyclerAdapter(academyListModel.getVideoList(), initGlide()));
                //rv_video.smoothScrollToPosition(0);

            }


            if (academyListModel.getRating().isEmpty()) {
                rating_bar_review.setVisibility(View.GONE);
            } else {
                rating_bar_review.setVisibility(View.VISIBLE);
                rating_bar_review.setRating(Float.parseFloat(academyListModel.getRating()));
            }
            tvCoachName.setText(Global.capitizeString(academyListModel.getName()));
            tvCoachExp.setText(academyListModel.getAddress());
            tvCoachSpecialization.setText(academyListModel.getCat_title());

            if(academyListModel.getAbout().isEmpty()){
                rl_bio.setVisibility(View.GONE);
            }else{
                rl_bio.setVisibility(View.VISIBLE);
                tvBio_details.setText(academyListModel.getAbout());
            }


            if(academyListModel.getLang().isEmpty()){
                rel_language.setVisibility(View.GONE);
            }else{
                rel_language.setVisibility(View.VISIBLE);
                tvLanguage_details.setText(academyListModel.getLang());
            }




            if (academyListModel.getAchievement().isEmpty()) {
                rel_achievement.setVisibility(View.GONE);
            } else {
                rel_achievement.setVisibility(View.VISIBLE);
                tvAchievement_details.setText(academyListModel.getAchievement());
            }


            tv_person.setText(academyListModel.getContact_person_name());
            tv_person_email.setText(academyListModel.getEmail());
            tv_person_phone.setText(academyListModel.getContact_person_phone());
            tvOfferPrice.setText(academyListModel.getTraining_type());
            tvPrice.setText(mContext.getString(R.string.fees) + ": " + "\u20B9" + academyListModel.getFee() + "/" + mContext.getString(R.string.month));


            if(academyListModel.getFacebook().isEmpty() && academyListModel.getInstagram().isEmpty() &&
                    academyListModel.getLinkedIn().isEmpty() && academyListModel.getTwitter().isEmpty() &&
                    academyListModel.getYoutube().isEmpty()){
                li_social.setVisibility(View.GONE);
            }else{
                li_social.setVisibility(View.VISIBLE);
            }

            if (academyListModel.getFacebook().isEmpty()) {
                li_fb.setVisibility(View.GONE);
            } else {
                li_fb.setVisibility(View.VISIBLE);
                ib_facebook.setOnClickListener(v -> {
                    sendFacebook(academyListModel.getFacebook());
                });
            }
            if (academyListModel.getInstagram().isEmpty()) {
                li_ins.setVisibility(View.GONE);
            } else {
                li_ins.setVisibility(View.VISIBLE);

                ib_instagram.setOnClickListener(v -> {
                    sendInstagram(academyListModel.getInstagram());
                });
            }

            if (academyListModel.getLinkedIn().isEmpty()) {
                li_lined.setVisibility(View.GONE);
            } else {
                li_lined.setVisibility(View.VISIBLE);
                ib_linked.setOnClickListener(v -> {
                    sendLinkend(academyListModel.getLinkedIn());
                });
            }

            if (academyListModel.getTwitter().isEmpty()) {
                li_twi.setVisibility(View.GONE);
            } else {
                li_twi.setVisibility(View.VISIBLE);
                ib_twitter.setOnClickListener(v -> {
                    sendTwitter(academyListModel.getTwitter());
                });
            }

            if (academyListModel.getYoutube().isEmpty()) {
                li_youtube.setVisibility(View.GONE);
            } else {
                li_youtube.setVisibility(View.VISIBLE);
                ib_youyube.setOnClickListener(v -> {
                    sendYoutube(academyListModel.getYoutube());
                });
            }

            timeAdapterAcademy = new TimeAdapterAcademy(mContext, academyListModel.getAcademySlots(), new TimeAdapterAcademy.timeSelectt() {
                @Override
                public void getSlotId(String slotId, String slotTime) {
                    //String[] namesArr = arrayListUser.toArray(new String[arrayListUser.size()]);
                    //String[] namesArrr = arrayListUser_slot.toArray(new String[arrayListUser_slot.size()]);
                    selectedTimeSlot=slotTime;
                    selectedTimeSlott=slotId;
                    // selectedTimeSlott = toCSV(namesArr);
                    // selectedTimeSlot = toCSVV(namesArrr);
                    //Toaster.customToast(selectedTimeSlot+"/"+selectedTimeSlott);
                }
            });
            recycler_view.setAdapter(timeAdapterAcademy);

//            recycler_view.setAdapter(new TimeAdapterAcademy(mContext, academyListModel.getAcademySlots(), new TimeAdapterAcademy.timeSelectt() {
//                @Override
//                public void getSlotId(String slotId, String slotTime) {
//                    //String[] namesArr = arrayListUser.toArray(new String[arrayListUser.size()]);
//                    //String[] namesArrr = arrayListUser_slot.toArray(new String[arrayListUser_slot.size()]);
//                    selectedTimeSlot=slotTime;
//                    selectedTimeSlott=slotId;
//                   // selectedTimeSlott = toCSV(namesArr);
//                   // selectedTimeSlot = toCSVV(namesArrr);
//                    //Toaster.customToast(selectedTimeSlot+"/"+selectedTimeSlott);
//                }
//            }));
        }
    }

    public static String toCSV(String[] array) {
        String result = "";
        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }

    public static String toCSVV(String[] array) {
        String result = "";
        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }

    private void sendFacebook(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            Timber.d("Application not intalled.");
            //Open url web page.
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    private void sendInstagram(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            Timber.d("Application not intalled.");
            //Open url web page.
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    private void sendTwitter(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            Timber.d("Application not intalled.");
            //Open url web page.
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    private void sendLinkend(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            Timber.d("Application not intalled.");
            //Open url web page.
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    private void sendYoutube(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            Timber.d("Application not intalled.");
            //Open url web page.
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
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

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions();

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (rv_video != null) {
            rv_video.onPausePlayer();
        }
    }

    @Override
    protected void onDestroy() {
        if (rv_video != null) {
            rv_video.releasePlayer();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (rv_video != null) {
            rv_video.OnResumePlayer();
            //rv_video.setAdapter(new MediaRecyclerAdapter(academyListModel.getVideoList(), initGlide()));
        }
        //timeAdapterAcademy.notifyDataSetChanged();
        super.onResume();
    }
}