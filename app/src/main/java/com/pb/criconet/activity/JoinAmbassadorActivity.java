package com.pb.criconet.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class JoinAmbassadorActivity extends AppCompatActivity {

    EditText edit_username_red_bg;
    EditText edit_email_red_bg;
    EditText edit_username_phone;
    EditText edit_password_red_bg;
    EditText edit_school_red_bg;
    EditText edit_higestQualification_red_bg;
    EditText edit_whywantbecomeambassodoar_red_bg;
    EditText edit_knowaboutcriconet_red_bg;
    EditText edit_eventBefore_red_bg;
    EditText edit_mentiontwothings_red_bg;
    EditText edit_passionate_red_bg;


    ImageButton ib_visible_p;
    DropDownView drop_gender;
    DropDownView drop_time;
    RadioButton rb_yes;
    RadioButton rb_no;
    LinearLayout lin_submit;
    LinearLayout li_organized;
    TextView mTitle;

    private ArrayList<DataModel> option = new ArrayList<>();
    private ArrayList<DataModel> option_type = new ArrayList<>();
    String time_interval="";
    String organized_status="";
    String userName, email, password, confirmPassword, gender, phoneCode, phoneNumber,collage,highestQualification,becomeAmbs,topThreeThings,organized,innovation,spendTime,fiveThings;

    SharedPreferences prefs;
    CustomLoaderView loaderView;
    RequestQueue queue;
    Activity activity;
    private OtpView otpView;
    String is_contact_verify="";
    String From="";
    String event_flag="";
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_ambassador);
        activity = this;
        loaderView = CustomLoaderView.initialize(this);
//        StatusBarGradient.setStatusBarGradiant(Signup.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        queue = Volley.newRequestQueue(activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(getResources().getString(R.string.ambassador_form));

        intializeView();
    }

    private void intializeView() {
        edit_username_red_bg = findViewById(R.id.edit_username_red_bg);
        edit_email_red_bg = findViewById(R.id.edit_email_red_bg);
        edit_username_phone = findViewById(R.id.edit_username_phone);
        edit_password_red_bg = findViewById(R.id.edit_password_red_bg);
        edit_school_red_bg = findViewById(R.id.edit_school_red_bg);
        edit_higestQualification_red_bg = findViewById(R.id.edit_higestQualification_red_bg);
        edit_whywantbecomeambassodoar_red_bg = findViewById(R.id.edit_whywantbecomeambassodoar_red_bg);
        edit_knowaboutcriconet_red_bg = findViewById(R.id.edit_knowaboutcriconet_red_bg);
        edit_eventBefore_red_bg = findViewById(R.id.edit_eventBefore_red_bg);
        edit_mentiontwothings_red_bg = findViewById(R.id.edit_mentiontwothings_red_bg);
        edit_passionate_red_bg = findViewById(R.id.edit_passionate_red_bg);

        li_organized = findViewById(R.id.li_organized);
        ib_visible_p = findViewById(R.id.ib_visible_p);
        ib_visible_p.setTag("InVisible");
        ib_visible_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ib_visible_p.getTag().equals("InVisible")) {
                    edit_password_red_bg.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ib_visible_p.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_black_24dp));
                    ib_visible_p.setTag("Visible");
                } else {
                    edit_password_red_bg.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ib_visible_p.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_black_24dp));
                    ib_visible_p.setTag("InVisible");
                }
            }
        });
        drop_gender = findViewById(R.id.drop_gender);
        option.add(new DataModel("Male"));
        option.add(new DataModel("Female"));
        option.add(new DataModel("Others"));
        drop_gender.setOptionList(option);

        drop_gender.setClickListener(new DropDownView.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {
                gender = name;
            }


            @Override
            public void onDismiss() {
            }
        });

        drop_time = findViewById(R.id.drop_time);
        option_type.add(new DataModel(getResources().getString(R.string.two_hr)));
        option_type.add(new DataModel(getResources().getString(R.string.four_hr)));
        option_type.add(new DataModel(getResources().getString(R.string.six_hr)));
        option_type.add(new DataModel(getResources().getString(R.string.eight_hr)));
        option_type.add(new DataModel(getResources().getString(R.string.ten_hr)));
        option_type.add(new DataModel(getResources().getString(R.string.twevle_hr)));
        option_type.add(new DataModel(getResources().getString(R.string.forteen_hr)));
        option_type.add(new DataModel(getResources().getString(R.string.sixteen_hr)));
        option_type.add(new DataModel(getResources().getString(R.string.eighteen_hr)));
        option_type.add(new DataModel(getResources().getString(R.string.twenty_hr)));
        option_type.add(new DataModel(getResources().getString(R.string.twentyTWO_hr)));
        option_type.add(new DataModel(getResources().getString(R.string.twentyFour_hr)));

        drop_time.setOptionList(option_type);

        drop_time.setClickListener(new DropDownView.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {
                time_interval = name;
            }


            @Override
            public void onDismiss() {
            }
        });
        rb_yes = findViewById(R.id.rb_yes);
        rb_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 if(b==true){
                     organized_status="yes";
                     li_organized.setVisibility(View.VISIBLE);
                     if(rb_no.isChecked()){
                         rb_no.setChecked(false);
                     }

                 }else{

                 }
            }
        });
        rb_no = findViewById(R.id.rb_no);
        rb_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    organized_status="no";
                    li_organized.setVisibility(View.GONE);
                    if(rb_yes.isChecked()){
                        rb_yes.setChecked(false);
                    }
                }else{

                }
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            From = extras.getString("From");
            if(From.equalsIgnoreCase("1")){
                edit_email_red_bg.setText(SessionManager.get_is_amb_email(prefs));
                edit_username_red_bg.setText(SessionManager.get_is_amb_name(prefs));
                edit_username_phone.setText(SessionManager.get_mobile(prefs));
                edit_school_red_bg.setText(SessionManager.get_is_amb_college(prefs));
                edit_higestQualification_red_bg.setText(SessionManager.get_is_amb_highestQ(prefs));
                edit_whywantbecomeambassodoar_red_bg.setText(SessionManager.get_is_ambs_do_you_want_campus_ambassdor(prefs));
                edit_knowaboutcriconet_red_bg.setText(SessionManager.get_is_ambs_thing_you_are_know_criconet(prefs));
                edit_mentiontwothings_red_bg.setText(SessionManager.get_is_ambs_innovative_thing(prefs));
                edit_passionate_red_bg.setText(SessionManager.get_is_ambs_passionate_thing(prefs));
                event_flag = SessionManager.get_is_ambs_have_you_org_event_flag(prefs);

                if(event_flag.equalsIgnoreCase("yes")){
                    rb_yes.setChecked(true);
                    edit_eventBefore_red_bg.setText(SessionManager.get_is_ambs_have_you_org_event_txt(prefs));
                }else{
                    rb_no.setChecked(true);
                    edit_eventBefore_red_bg.setText("");
                }
                drop_time.setText(SessionManager.get_is_ambs_how_many_hrs_per_week(prefs));

            }else{
                edit_username_red_bg.setText(SessionManager.get_name(prefs));
                edit_email_red_bg.setText(SessionManager.get_emailid(prefs));
                edit_username_phone.setText(SessionManager.getPhoneNumber(prefs));
                edit_school_red_bg.setText("");
                edit_higestQualification_red_bg.setText("");
                edit_whywantbecomeambassodoar_red_bg.setText("");
                edit_knowaboutcriconet_red_bg.setText("");
                edit_eventBefore_red_bg.setText("");
                edit_mentiontwothings_red_bg.setText("");
                edit_passionate_red_bg.setText("");
            }


        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(From.equalsIgnoreCase("1")){
                    Intent intent = new Intent(JoinAmbassadorActivity.this, AmbassdorRewardsActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(JoinAmbassadorActivity.this, AmbassadoarProgrrameActivity.class);
                    intent.putExtra("From","2");
                    startActivity(intent);
                    finish();
                }

            }
        });

        lin_submit = findViewById(R.id.lin_submit);
        lin_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = edit_username_red_bg.getText().toString().trim();
                email = edit_email_red_bg.getText().toString().trim();
                phoneNumber = edit_username_phone.getText().toString().trim();
                phoneCode = "91";
                password = edit_password_red_bg.getText().toString().trim();
                collage= edit_school_red_bg.getText().toString().trim();
                highestQualification= edit_higestQualification_red_bg.getText().toString().trim();
                becomeAmbs = edit_whywantbecomeambassodoar_red_bg.getText().toString().trim();
                topThreeThings = edit_knowaboutcriconet_red_bg.getText().toString().trim();
                organized = edit_eventBefore_red_bg.getText().toString().trim();
                innovation = edit_mentiontwothings_red_bg.getText().toString().trim();
                fiveThings= edit_passionate_red_bg.getText().toString().trim();


                if (userName.isEmpty()) {
                    Toaster.customToast(getResources().getString(R.string.full_empty_validation));
                } else if (!userName.matches("[a-zA-Z0-9.? ]*")) {
                    Toaster.customToast(getResources().getString(R.string.Special_character_not_allowed));
                } else if (userName.length() < 3) {
                    Toaster.customToast(getResources().getString(R.string.name_limit_validation));
                }
                else if (!Global.validateName(email)) {
                    Toaster.customToast(getResources().getString(R.string.email_empty_validation));
                } else if (!Global.isValidEmail(email)) {
                    Toaster.customToast(getResources().getString(R.string.email_invalid_validation));
                } else if (phoneNumber.isEmpty()) {
                   Toaster.customToast(getResources().getString(R.string.phone_error));
                } else if (!phoneNumber.matches("[0-9.? ]*")) {
                    Toaster.customToast(getResources().getString(R.string.Special_character_not_allowed));
                } else if (!Global.isValidPhoneNumber(phoneNumber)) {
                    Toaster.customToast(getResources().getString(R.string.error_invalid_phone));
                }
                //else if (password.isEmpty()) {
//                    Toaster.customToast(getResources().getString(R.string.password_cannot_empty));
//                } else if (password.length() < 6) {
//                    Toaster.customToast(getResources().getString(R.string.password_too_short));
//                }
//
//                else if (drop_gender.getText().toString().trim().isEmpty()) {
//                    Toaster.customToast("Select Gender");
//                }
                else if (collage.isEmpty()) {
                    Toaster.customToast(getResources().getString(R.string.Please_enter_school_name));
                }
                else if (!collage.matches("[a-zA-Z0-9.? ]*")) {
                    Toaster.customToast(getResources().getString(R.string.Special_character_not_allowed));
                }else if (highestQualification.isEmpty()) {
                    Toaster.customToast(getResources().getString(R.string.Please_enter_highestQualification));
                }else if (becomeAmbs.isEmpty()) {
                    Toaster.customToast(getResources().getString(R.string.Please_enter_description));
                }else if (topThreeThings.isEmpty()) {
                    Toaster.customToast(getResources().getString(R.string.Please_enter_description));
                }else if (rb_yes.isChecked() && organized.isEmpty()) {
                        Toaster.customToast(getResources().getString(R.string.Please_enter_description));

                }
                else if (innovation.isEmpty()) {
                    Toaster.customToast(getResources().getString(R.string.Please_enter_description));
                }else if (drop_time.getText().toString().trim().isEmpty()) {
                    Toaster.customToast(getResources().getString(R.string.Please_select_time));
                }else if (fiveThings.isEmpty()) {
                    Toaster.customToast(getResources().getString(R.string.Please_enter_description));
                }
                else {
                    if (Global.isOnline(activity)) {
                        getAmbassadorForm();
                    } else {
                        Toaster.customToast(getResources().getString(R.string.no_internet));
                    }


                }
            }
        });


    }

    public void getAmbassadorForm() {
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
                                //is_contact_verify="0";



//                                SessionManager.save_user_id(prefs, jsonObject.getString("user_id"));
//                                SessionManager.save_session_id(prefs, jsonObject.getString("session_id"));
//
                             //  if (jsonObject.has("data")) {
//                                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
//                                    is_contact_verify = jsonObjectData.getString("is_mobile_verified");
//                                    SessionManager.save_name(prefs, jsonObjectData.getString("username"));
//                                    SessionManager.save_emailid(prefs, jsonObjectData.getString("email"));
//                                    SessionManager.savePhone(prefs, jsonObjectData.getString("phone_number"));
//                                    SessionManager.savePhoneCode(prefs, jsonObjectData.getString("phone_code"));
//                                    SessionManager.save_sex(prefs, jsonObjectData.getString("gender"));
//                                    SessionManager.save_image(prefs, jsonObjectData.getString("avatar"));
//                                    SessionManager.save_cover(prefs, jsonObjectData.getString("cover"));
//                                    SessionManager.save_mobile_verified(prefs, jsonObjectData.getString("is_mobile_verified"));
//
//
//                                    if (jsonObjectData.getString("profile_type") == null) {
//
//                                        SessionManager.save_profiletype(prefs, jsonObjectData.getString("profile_type"));
//                                    } else {
//                                        SessionManager.save_profiletype(prefs, jsonObjectData.getString("profile_type"));
//                                    }
//
//                                    SessionManager.save_check_login(prefs, true);
//
//                                    edit_username_red_bg.setText("");
//                                    edit_email_red_bg.setText("");
//                                    edit_password_red_bg.setText("");
//                                    edit_username_phone.setText("");
//                                    EmailOtpDialog(phoneNumber);
//                                }


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
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("full_name", userName);
                param.put("email", email);
                param.put("phone_number", phoneNumber);
                param.put("phone_code", phoneCode);
                param.put("school_college_name", collage);
                param.put("height_qualification", highestQualification);
                param.put("have_you_org_event_txt", organized);
                param.put("innovative_thing", innovation);
                param.put("how_many_hrs_per_week", drop_time.getText().toString().trim());
                param.put("passionate_thing", fiveThings);
                param.put("do_you_want_campus_ambassdor", becomeAmbs);
                param.put("thing_you_are_know_criconet", topThreeThings);
                param.put("have_you_org_event_flag", organized_status);
                if(From.equalsIgnoreCase("1")){
                    param.put("edit_profile", "1");
                }else{
                    param.put("edit_profile ", "0");
                }
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
        Dialog dialog = new Dialog(activity);
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

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_congratulation);
        dialog.setCancelable(false);
        FrameLayout btContinue = dialog.findViewById(R.id.fl_done);

        btContinue.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(activity, AmbassdorRewardsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

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
        if (From.equalsIgnoreCase("1")) {
            Intent intent = new Intent(JoinAmbassadorActivity.this, AmbassdorRewardsActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(JoinAmbassadorActivity.this, AmbassadoarProgrrameActivity.class);
            intent.putExtra("From","2");
            startActivity(intent);
            finish();
        }

    }
}