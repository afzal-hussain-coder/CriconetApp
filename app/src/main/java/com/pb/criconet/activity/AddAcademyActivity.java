package com.pb.criconet.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
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
import com.mukesh.OtpView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.DataModel;
import com.pb.criconet.Utills.DropDownView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.GridViewScrollable;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.GalleryAdapterAcademy;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class AddAcademyActivity extends AppCompatActivity {

    EditText edit_username_red_bg;
    EditText edit_email_red_bg;
    EditText edit_address;
    EditText edit_academy_fees;
    EditText edit_details_about_academy;
    EditText edit_acheivement;
    EditText edit_short_des;
    EditText edit_contact_person_name;
    EditText edit_contact_person_phone;
    EditText edit_review_of_the_academy;
    EditText edttxt_speciality;
    EditText edttxt_fb_profileLink;
    EditText edttxt_twitter_profile;
    EditText edttxt_linkind_profile;
    EditText edttxt_instagram_profile;
    EditText edttxt_youtube_profile;

    RatingBar rating_bar_academy;
    CheckBox cb_personal;
    CheckBox cb_group;
    TextView textView_language;
    TextView textView_select_training_session;
    TextView mTitle;
    private OtpView otpView;
    String phoneNumber;

    Context mContext;
    Activity mActivity;
    CustomLoaderView loaderView;
    private RequestQueue queue;
    private static final int CAMERA_REQUESTid = 2015;
    private static final int PICK_IMAGEid = 100;
    private SharedPreferences prefs;
    String is_contact_verify;
    GridViewScrollable gv_multiple_image;
    Button btn_select_multiple_image;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    private GalleryAdapterAcademy galleryAdapter;



    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_academy);
        mActivity = this;
        mContext = this;
        loaderView = CustomLoaderView.initialize(this);
//        StatusBarGradient.setStatusBarGradiant(Signup.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(getResources().getString(R.string.add_academy));

        intializeView();
    }

    private void intializeView() {
        edit_username_red_bg = findViewById(R.id.edit_username_red_bg);
        edit_email_red_bg = findViewById(R.id.edit_email_red_bg);
        edit_address = findViewById(R.id.edit_address);
        edit_academy_fees = findViewById(R.id.edit_academy_fees);
        edit_details_about_academy = findViewById(R.id.edit_details_about_academy);
        edit_acheivement = findViewById(R.id.edit_acheivement);
        edit_short_des = findViewById(R.id.edit_short_des);
        edit_contact_person_name = findViewById(R.id.edit_contact_person_name);
        edit_contact_person_phone = findViewById(R.id.edit_contact_person_phone);
        edit_review_of_the_academy = findViewById(R.id.edit_review_of_the_academy);
        edttxt_speciality = findViewById(R.id.edttxt_speciality);

        edttxt_fb_profileLink=findViewById(R.id.edttxt_fb_profileLink);
        edttxt_twitter_profile=findViewById(R.id.edttxt_twitter_profile);
        edttxt_linkind_profile=findViewById(R.id.edttxt_linkind_profile);
        edttxt_instagram_profile=findViewById(R.id.edttxt_instagram_profile);
        edttxt_youtube_profile=findViewById(R.id.edttxt_youtube_profile);


        rating_bar_academy=findViewById(R.id.rating_bar_academy);
        cb_personal=findViewById(R.id.cb_personal);
        cb_group=findViewById(R.id.cb_group);
        textView_language=findViewById(R.id.textView_language);
        textView_select_training_session=findViewById(R.id.textView_select_training_session);
        gv_multiple_image=findViewById(R.id.gv_multiple_image);
        btn_select_multiple_image=findViewById(R.id.btn_select_multiple_image);
        btn_select_multiple_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(mImageUri);
                    galleryAdapter = new GalleryAdapterAcademy(getApplicationContext(),mArrayUri);
                    gv_multiple_image.setAdapter(galleryAdapter);
                    gv_multiple_image.setVerticalSpacing(gv_multiple_image.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gv_multiple_image
                            .getLayoutParams();
                    mlp.setMargins(0, gv_multiple_image.getHorizontalSpacing(), 0, 0);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                            galleryAdapter = new GalleryAdapterAcademy(getApplicationContext(),mArrayUri);
                            gv_multiple_image.setAdapter(galleryAdapter);
                            gv_multiple_image.setVerticalSpacing(gv_multiple_image.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gv_multiple_image
                                    .getLayoutParams();
                            mlp.setMargins(0, gv_multiple_image.getHorizontalSpacing(), 0, 0);

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toaster.customToast("You haven't picked Image");
            }
        } catch (Exception e) {
            Toaster.customToast("Something went wrong");

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void getAddacademyResponse() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_CAMPUS_AMBASSADOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.tag("Ambassador response").e("%s", response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ;
                            if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {



                                if (jsonObject.has("data")) {
                                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");

                                    JSONObject ambassadorProfile = jsonObjectData.getJSONObject("ambassadorProfile");
                                    if(ambassadorProfile.length()>0){
                                        SessionManager.save_is_ambassador(prefs,"1");

                                    SessionManager.save_is_amb_name(prefs,ambassadorProfile.getString("name"));
                                    SessionManager.save_is_amb_fullname(prefs,ambassadorProfile.getString("full_name"));
                                    SessionManager.save_is_amb_email(prefs,ambassadorProfile.getString("email"));
                                    SessionManager.save_mobile(prefs,ambassadorProfile.getString("phone_number"));
                                    SessionManager.save_is_amb_college(prefs,ambassadorProfile.getString("school_college_name"));
                                    SessionManager.save_is_amb_highestQ(prefs,ambassadorProfile.getString("height_qualification"));
                                    SessionManager.save_is_ambs_have_you_org_event_flag(prefs,ambassadorProfile.getString("have_you_org_event_flag"));
                                    SessionManager.save_is_ambs_have_you_org_event_txt(prefs,ambassadorProfile.getString("have_you_org_event_txt"));
                                    SessionManager.save_is_ambs_innovative_thing(prefs,ambassadorProfile.getString("innovative_thing"));
                                    SessionManager.save_is_ambs_how_many_hrs_per_week(prefs,ambassadorProfile.getString("how_many_hrs_per_week"));
                                    SessionManager.save_is_ambs_passionate_thing(prefs,ambassadorProfile.getString("passionate_thing"));
                                    SessionManager.save_is_ambs_do_you_want_campus_ambassdor(prefs,ambassadorProfile.getString("do_you_want_campus_ambassdor"));
                                    SessionManager.save_is_ambs_thing_you_are_know_criconet(prefs,ambassadorProfile.getString("thing_you_are_know_criconet"));
                                    is_contact_verify = jsonObjectData.getString("is_mobile_verified");
                                    if (is_contact_verify.equalsIgnoreCase("0")) {
                                        EmailOtpDialog(phoneNumber);
                                    }else {
                                        congratsDialog();
                                    }
                                    }else{
                                        SessionManager.save_is_ambassador(prefs,"0");
                                    }
                                }

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Toaster.customToast(getResources().getString(R.string.socket_timeout));
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
                        Toaster.customToast(getResources().getString(R.string.socket_timeout));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
//                param.put("full_name", userName);
//                param.put("email", email);
//                param.put("phone_number", phoneNumber);
//                param.put("phone_code", phoneCode);
//                param.put("school_college_name", collage);
//                param.put("height_qualification", highestQualification);
//                param.put("have_you_org_event_txt", organized);
//                param.put("innovative_thing", innovation);
//                param.put("how_many_hrs_per_week", drop_time.getText().toString().trim());
//                param.put("passionate_thing", fiveThings);
//                param.put("do_you_want_campus_ambassdor", becomeAmbs);
//                param.put("thing_you_are_know_criconet", topThreeThings);
//                param.put("have_you_org_event_flag", organized_status);
//                if(From.equalsIgnoreCase("1")){
//                    param.put("edit_profile", "1");
//                }else{
//                    param.put("edit_profile", "0");
//                }
                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


    private void EmailOtpDialog(String mobile) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_email_verification);
        dialog.setCancelable(false);


        TextView tvOTPTime = dialog.findViewById(R.id.tv_otp_time);
        TextView btResend = dialog.findViewById(R.id.btn_resend);
        Button btContinue = dialog.findViewById(R.id.btn_continue);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        LinearLayout lay_otp_expire = dialog.findViewById(R.id.lay_otp_expire);
        CountryCodePicker ccp = dialog.findViewById(R.id.ccp);
        EditText edit_phone = dialog.findViewById(R.id.edit_phone);

        startTimer(tvOTPTime, btResend, lay_otp_expire);

        btResend.setOnClickListener(view -> {
            resendOTP(mobile);
            lay_otp_expire.setVisibility(View.VISIBLE);
            startTimer(tvOTPTime, btResend, lay_otp_expire);
        });
        otpView = dialog.findViewById(R.id.otp_view);
        btContinue.setOnClickListener(v -> {
            lay_otp_expire.setVisibility(View.VISIBLE);
            if (Objects.requireNonNull(otpView.getText()).toString().isEmpty()) {
                Toaster.customToast(getString(R.string.code_msg));
            } else if (otpView.getText().toString().length() != 4) {
                Toaster.customToast(getString(R.string.code_invalid));
            } else {
                sendVerifyOtp(otpView.getText().toString().trim(), dialog);
            }
        });

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();

    }

    private void resendOTP(String mobile) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.RESEND_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.tag("login response").e("%s", response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Toaster.customToast(getResources().getString(R.string.socket_timeout));
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
                        Toaster.customToast(getResources().getString(R.string.socket_timeout));
//                Global.msgDialog(Signup.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("phone_number", mobile);
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

    private void sendVerifyOtp(String otp, Dialog dialog) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.OTP_VERIFY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.tag("login response").e("%s", response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                                dialog.dismiss();
                                if (jsonObject.has("data")) {
                                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                   phoneNumber = jsonObjectData.getString("temp_mobile_no");
                                    SessionManager.save_name(prefs, jsonObjectData.getString("username"));
                                    SessionManager.save_emailid(prefs, jsonObjectData.getString("email"));
                                    SessionManager.save_mobile(prefs, jsonObjectData.getString("phone_number"));
                                    SessionManager.savePhoneCode(prefs, jsonObjectData.getString("phone_code"));
                                    SessionManager.save_mobile_verified(prefs, jsonObjectData.getString("is_mobile_verified"));
                                    JSONObject ambassadorProfile = jsonObjectData.getJSONObject("ambassadorProfile");

                                    if(ambassadorProfile.length()>0){
                                        SessionManager.save_is_ambassador(prefs,"1");
                                        SessionManager.save_is_amb_name(prefs,ambassadorProfile.getString("name"));
                                        SessionManager.save_is_amb_fullname(prefs,ambassadorProfile.getString("full_name"));
                                        SessionManager.save_is_amb_email(prefs,ambassadorProfile.getString("email"));
                                        SessionManager.save_mobile(prefs,ambassadorProfile.getString("phone_number"));
                                        SessionManager.save_is_amb_college(prefs,ambassadorProfile.getString("school_college_name"));
                                        SessionManager.save_is_amb_highestQ(prefs,ambassadorProfile.getString("height_qualification"));
                                        SessionManager.save_is_ambs_have_you_org_event_flag(prefs,ambassadorProfile.getString("have_you_org_event_flag"));
                                        SessionManager.save_is_ambs_have_you_org_event_txt(prefs,ambassadorProfile.getString("have_you_org_event_txt"));
                                        SessionManager.save_is_ambs_innovative_thing(prefs,ambassadorProfile.getString("innovative_thing"));
                                        SessionManager.save_is_ambs_how_many_hrs_per_week(prefs,ambassadorProfile.getString("how_many_hrs_per_week"));
                                        SessionManager.save_is_ambs_passionate_thing(prefs,ambassadorProfile.getString("passionate_thing"));
                                        SessionManager.save_is_ambs_do_you_want_campus_ambassdor(prefs,ambassadorProfile.getString("do_you_want_campus_ambassdor"));
                                        SessionManager.save_is_ambs_thing_you_are_know_criconet(prefs,ambassadorProfile.getString("thing_you_are_know_criconet"));
                                    }else{
                                        SessionManager.save_is_ambassador(prefs,"0");
                                    }
                                    congratsDialog();

                                }



                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Toaster.customToast(getResources().getString(R.string.socket_timeout));
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
                        Toaster.customToast(getResources().getString(R.string.socket_timeout));
//                Global.msgDialog(Signup.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("otp", otp);
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

    private void congratsDialog() {

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_congratulation);
        dialog.setCancelable(false);
        FrameLayout btContinue = dialog.findViewById(R.id.fl_done);

//        btContinue.setOnClickListener(v -> {
//            dialog.dismiss();
//            Intent intent = new Intent(mActivity, AmbassdorRewardsActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        });

        dialog.show();
    }
    private void startTimer(TextView tvOTPTime, TextView btResend, LinearLayout lay_otp_expire) {
        new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvOTPTime.setText("00 : " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                lay_otp_expire.setVisibility(View.GONE);
                btResend.setVisibility(View.VISIBLE);
            }

        }.start();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (From.equalsIgnoreCase("1")) {
//            Intent intent = new Intent(AddAcademyActivity.this, AmbassdorRewardsActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            Intent intent = new Intent(AddAcademyActivity.this, AmbassadoarProgrrameActivity.class);
//            intent.putExtra("From","2");
//            startActivity(intent);
//            finish();
//        }

    }
}