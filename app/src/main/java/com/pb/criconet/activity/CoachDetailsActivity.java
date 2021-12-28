package com.pb.criconet.activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mukesh.OtpView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.event.SlotId;
import com.pb.criconet.models.BookingPaymentsDetails;
import com.pb.criconet.models.CoachDetails;
import com.pb.criconet.models.DateSlotes;
import com.pb.criconet.models.OrderCreate;
import com.pb.criconet.models.TimeSlot;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class CoachDetailsActivity extends BaseActivity {

    List<EventDay> events = new ArrayList<>();
    private String year;
    private String month;
    private String dateGott = "";
    private AppCompatActivity mActivity = CoachDetailsActivity.this;
    private TextView tvCoachName;
    private TextView tvCoacTitle;
    private TextView tvPrice;
    private TextView tvOfferPrice;
    private CircleImageView ivProfileImage;
    private RequestQueue queue;
    private SharedPreferences prefs;
    private String coachId;
    private FrameLayout btnCalender;
    private Calendar[] days=new Calendar[0];;
    private String mslotId = "";
    private CalendarView datePicker;
    private CoachDetails modelArrayList;
    private RatingBar rating_bar_review;
    private TextView tvCoacExp;
    private TextView tvCoachSpecialization;
    private int rating;
    private OtpView otpView;
    private LinearLayout layout_enter_mobile_no, layout_otp;
    CustomLoaderView loaderView;
    private FrameLayout fl_call;
    private FrameLayout fl_whatsapp;
    private TextView tv_criconet_support_info;
    private LinearLayout li_support;
    private LinearLayout li_offer;
    Date previousDate;
    String coupon_code = "", percentage;
    BookingPaymentsDetails bookingPaymentsDetails;
    OrderCreate ordercreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_details);
        loaderView = CustomLoaderView.initialize(this);
        coachId = getIntent().getStringExtra("coachId");
        rating = getIntent().getIntExtra("rating", 0);
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
        mTitle.setText(R.string.available_date_session);
        prefs = PreferenceManager.getDefaultSharedPreferences(CoachDetailsActivity.this);
        queue = Volley.newRequestQueue(CoachDetailsActivity.this);


        tvCoachName = findViewById(R.id.tvCoachName);
        tvCoacTitle = findViewById(R.id.tvCoacTitle);
        tvPrice = findViewById(R.id.tvPrice);

        tvOfferPrice = findViewById(R.id.tvOfferPrice);
        li_offer = findViewById(R.id.li_offer);
        li_support = findViewById(R.id.li_support);
        tv_criconet_support_info = findViewById(R.id.tv_criconet_support_info);
        fl_call = findViewById(R.id.fl_call);
        fl_whatsapp = findViewById(R.id.fl_whatsapp);

        rating_bar_review = findViewById(R.id.rating_bar_review);
        if (rating == 0) {
            rating_bar_review.setVisibility(View.GONE);
        } else {
            rating_bar_review.setVisibility(View.VISIBLE);
            rating_bar_review.setRating(Float.parseFloat(String.valueOf(rating)));
        }
        tvCoacExp = findViewById(R.id.tvCoacExp);
        tvCoachSpecialization = findViewById(R.id.tvCoachSpecialization);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        btnCalender = findViewById(R.id.btnCalender);

        if (SessionManager.getProfileType(prefs).equalsIgnoreCase("coach")) {
            btnCalender.setVisibility(View.GONE);
        } else {
            btnCalender.setVisibility(View.VISIBLE);
        }

        datePicker = findViewById(R.id.calendarView);
        btnCalender.setOnClickListener(view -> {
            if (dateGott.equalsIgnoreCase("")) {
                Toaster.customToast(getResources().getString(R.string.Select_Slot_Available_Date));
            } else if (mslotId.equalsIgnoreCase("")) {
                Toaster.customToast(getResources().getString(R.string.Select_Time_Slot));
            } else {

                String is_contact_verify = SessionManager.get_mobile_verified(prefs);
                //Toaster.customToast(SessionManager.get_mobile_verified(prefs));
                //is_contact_verify="0";

                if (is_contact_verify.equalsIgnoreCase("0")) {
                    EmailOtpDialog(modelArrayList, ordercreate);
                }else{
                    if (Global.isOnline(mActivity)) {
                        BookCoach();
                    } else {
                        Global.showDialog(mActivity);
                    }
                }



            }
        });

        year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
        month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());


        //disable dates before today
        Calendar min = Calendar.getInstance();
        previousDate = min.getTime();
        min.add(Calendar.DAY_OF_MONTH, -1);
        datePicker.setMinimumDate(min);

//        datePicker.setOnDayClickListener(eventDay -> {
//            dateGot = Global.getDateGot(eventDay.getCalendar().getTime().toString());
//            if (Global.isOnline(mActivity)) {
//                getDateSlote(dateGot, coachId);
//            } else {
//                Global.showDialog(mActivity);
//            }
//
//        });

        if (Global.isOnline(mActivity)) {
            getCoachDetails();
            getCoachDete();
        } else {
            Global.showDialog(mActivity);
        }

        datePicker.setOnDayClickListener(eventDay -> {
            mslotId="";
            dateGott = Global.getDateGot(eventDay.getCalendar().getTime().toString());
            if(previousDate.compareTo(eventDay.getCalendar().getTime()) ==-1){
                if (Global.isOnline(mActivity)) {
                    getDateSlote(dateGott, coachId);
                } else {
                    Global.showDialog(mActivity);
                }
            }else if(Global.getDateGot(previousDate.toString()).equalsIgnoreCase(Global.getDateGot(eventDay.getCalendar().getTime().toString()))){
                if (Global.isOnline(mActivity)) {
                    getDateSlote(dateGott, coachId);
                } else {
                    Global.showDialog(mActivity);
                }
            }else{
                //dateGot="";
            }

        });

        datePicker.setOnForwardPageChangeListener(() -> {
            year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());
            getCoachDete();
        });

        datePicker.setOnPreviousPageChangeListener(() -> {
            year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());
            getCoachDete();
        });

        if (Global.isOnline(mActivity)) {
        getOffer();
    } else {
        Global.showDialog(mActivity);
    }

    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }


    private void getOffer() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.getOffet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("offer_res", response);
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {


                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = null;


                        if (jsonObject1.has("coupon_code")) {
                            coupon_code = jsonObject1.getString("coupon_code");
                            SessionManager.save_coupon_code(prefs, coupon_code);
                        }
                        if (jsonObject1.has("offer_details")) {
                            jsonArray = jsonObject1.getJSONArray("offer_details");
                        }
                        JSONObject  jsonObject2=new JSONObject();
                        for(int i=0;i<jsonArray.length();i++){
                            jsonObject2=jsonArray.getJSONObject(0);
                        }

                        if(jsonObject2.has("offer_percentage")){
                            percentage = jsonObject2.getString("offer_percentage");
                            Log.d("Perstage",percentage);
                        }

//                        if (jsonArray.length()==0) {
//                            img_banner.setVisibility(View.GONE);
//                        }else{
//                            img_banner.setVisibility(View.VISIBLE);
//                            JSONObject jsonObject2= jsonArray.getJSONObject(0);
//                            if (!jsonObject2.getString("banner").isEmpty()) {
//                                Glide.with(getActivity()).load(jsonObject2.getString("banner"))
//                                        .into(img_banner);
//                            } else {
//                                Glide.with(getActivity()).load(R.drawable.bg_coach)
//                                        .into(img_banner);
//                            }
//                        }


                    } else {
                        Toaster.customToast(getString(R.string.socket_timeout));
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
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getCoachDetails() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_details", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                loaderView.hideLoader();
                Gson gson = new Gson();
                modelArrayList = gson.fromJson(response, CoachDetails.class);

                if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    Glide.with(mActivity).load(modelArrayList.getData().getAvatar())
                            .into(ivProfileImage);
                    tvCoachName.setText(Global.capitalizeFirstLatterOfString(modelArrayList.getData().getName()));
                    tvCoacTitle.setText(modelArrayList.getData().getProfileTitle());
                    tvPrice.setText("\u20B9" + modelArrayList.getData().getChargeAmount() + "/" + getResources().getString(R.string.session));
                    tvCoacExp.setText(modelArrayList.getData().getExps());
                    tvCoachSpecialization.setText(modelArrayList.getData().getCatTitle());

                    if (modelArrayList.getData().getPrice().getOfferId().equalsIgnoreCase("0")) {
                        li_offer.setVisibility(View.GONE);
                    } else {
                        li_offer.setVisibility(View.VISIBLE);
                        tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        tvOfferPrice.setText("\u20B9" + modelArrayList.getData().getPrice().getPaymentPrice() + "/" + getResources().getString(R.string.session));

                    }

//                    tv_criconet_support_info.setText(ordercreate.getCriconetSupport().getTxtmsg2());
//                    fl_call.setOnClickListener(v -> {
//                        startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+ordercreate.getCriconetSupport().getContact_number())));
//                    });
//
//
//                    fl_whatsapp.setOnClickListener(v -> {
//                        String number =ordercreate.getCriconetSupport().getContact_number();
//                        String url = "https://api.whatsapp.com/send?phone="+number;
//                        Intent i = new Intent(Intent.ACTION_VIEW);
//                        i.setData(Uri.parse(url));
//                        startActivity(i);
//                    });
//
//                    if(ordercreate.getCriconet_support().equalsIgnoreCase("0")){
//                        tv_criconet_support_info.setVisibility(View.VISIBLE);
//                        li_support.setVisibility(View.VISIBLE);
//                    }else{
//                        tv_criconet_support_info.setVisibility(View.GONE);
//                        li_support.setVisibility(View.GONE);
//                    }


                } else {
                    Toaster.customToast(modelArrayList.getErrors().getErrorText());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", coachId);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getCoachDete() {
        //progressDialog = Global.getProgressDialog(mActivity, CCResource.getString(mActivity, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_available_date", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Global.dismissDialog(progressDialog);
                Gson gson = new Gson();

                if (!response.contains("false")) {
                    DateSlotes modelArrayList = gson.fromJson(response, DateSlotes.class);

                    if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {

                        days = new Calendar[modelArrayList.getData().size()];
//                        List<Calendar>calendars =new ArrayList<>(modelArrayList.getData().size());
//                        Toaster.customToast(calendars.size()+"");
//                        datePicker.setDisabledDays(calendars);
                        for (int i = 0; i < modelArrayList.getData().size(); i++) {

                            try {
                                days[i] = Global.convertStringToCalendar(modelArrayList.getData().get(i));
                                events.add(new EventDay(Global.convertStringToCalendar(modelArrayList.getData().get(i)), Global.getThreeDots(mActivity)));
                                datePicker.setEvents(events);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                        if (modelArrayList.getData().size() == 0) {
                            btnCalender.setVisibility(View.GONE);
                        } else {
                            btnCalender.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Toaster.customToast(modelArrayList.getErrors().getErrorText());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //    Global.dismissDialog(progressDialog);
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", coachId);
                param.put("month", month);
                param.put("year", year);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getDateSlote(String dateGot, String coachId) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_date_slot", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("Time respose", response);
                try{
                    Gson gson = new Gson();
                    TimeSlot modelArrayList = gson.fromJson(response, TimeSlot.class);
                    if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                        navigationController.showTimePreode(modelArrayList);
                    } else {
                        dateGott="";
                        Toaster.customToast(modelArrayList.getErrors().getErrorText());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", coachId);
                param.put("date", dateGot);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void BookCoach() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "create_booking_order", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Booking Response", response);
                loaderView.hideLoader();
                try{
                    Gson gson = new Gson();
                    ordercreate = gson.fromJson(response, OrderCreate.class);

                    if (ordercreate != null && ordercreate.getStatus() == 200) {
                        //JSONObject jsonObject1 = ordercreate.getJSONObject("data");

                        SessionManager.save_mobile_verified(prefs,ordercreate.getPaymentOption().getPrefill().getIs_conatct_verified());


                            if (ordercreate.getPayment() == 0) {
                                //BookingPaymentsDetails bookingPaymentsDetails = new BookingPaymentsDetails(jsonObject1);

                                if (Global.isOnline(mActivity)) {
                                    sendPaymentSuccess();
                                } else {
                                    Global.showDialog(mActivity);
                                }

                            }else{

                                Intent intent = new Intent(mActivity, ActivityCheckoutDetails.class);
                                intent.putExtra("key", (Serializable) modelArrayList);
                                intent.putExtra("key1", (Serializable) ordercreate);
                                intent.putExtra("booking_slot_date", dateGott);
                                intent.putExtra("booking_slot_id",mslotId);
                                startActivity(intent);
                            }


                            //finish();


                    } else {
                        Toaster.customToast(ordercreate.getErrors().getErrorText());
//                        if (ordercreate == null){
//                            Toaster.customToast(ordercreate.getErrors().getErrorText());
//                        }else{
//
//                        }

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return (headers != null || headers.isEmpty()) ? headers : super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() {
//                param.put("booking_date", getDateTime());
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", coachId);
                param.put("booking_slot_date", dateGott);
                param.put("booking_slot_id", mslotId);
                param.put("booking_amount", modelArrayList.getData().getPrice().getPaymentPrice());
                param.put("payment_amount", modelArrayList.getData().getPrice().getPayment_amount());
                param.put("taxes_amount", modelArrayList.getData().getPrice().getTaxesAmount());
                param.put("offer_id", modelArrayList.getData().getPrice().getOfferId());
                param.put("cuurency_code", "INR");
                Timber.e(param.toString());
                return param;
            }

        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SlotId event) {
        this.mslotId = event.slotId;
        btnCalender.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void EmailOtpDialog(CoachDetails modelArrayList, OrderCreate ordercreate) {
        final Dialog dialog = new Dialog(CoachDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_email_verification_checkout);
        dialog.setCancelable(false);

        TextView tvOTPTime = dialog.findViewById(R.id.tv_otp_time);
        TextView btResend = dialog.findViewById(R.id.btn_resend);
        Button btContinue = dialog.findViewById(R.id.btn_continue);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        layout_enter_mobile_no = dialog.findViewById(R.id.layout_enter_mobile_no);
        layout_otp = dialog.findViewById(R.id.layout_otp);
        LinearLayout lay_otp_expire = dialog.findViewById(R.id.lay_otp_expire);
        CountryCodePicker ccp = dialog.findViewById(R.id.ccp);
        EditText edit_phone = dialog.findViewById(R.id.edit_phone);
        Button btn_send = dialog.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(v -> {
            if (edit_phone.getText().toString().trim().isEmpty()) {
                Toaster.customToast(getResources().getString(R.string.Please_enter_mobile_no));
            } else {
                /*String phoneCode = ccp.getSelectedCountryCode();*/
                String phoneCode ="91";
                String phoneNumber = edit_phone.getText().toString().trim();
                updateMobileNo(phoneNumber, phoneCode, layout_otp, layout_enter_mobile_no);
            }

        });

//        layout_enter_mobile_no.setVisibility(View.GONE);
//        layout_otp.setVisibility(View.VISIBLE);
        startTimer(tvOTPTime, btResend, lay_otp_expire);

        btResend.setOnClickListener(view -> {
            String phoneCode = ccp.getSelectedCountryCode();
            String phoneNumber = edit_phone.getText().toString().trim();
            lay_otp_expire.setVisibility(View.VISIBLE);
            startTimer(tvOTPTime, btResend, lay_otp_expire);
            resendOTP(phoneNumber);
        });
        otpView = dialog.findViewById(R.id.otp_view);
        /*otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String s) {

            }
        });
        otpView.setOtpCompletionListener(s -> {
        });*/

        btContinue.setOnClickListener(v -> {
            lay_otp_expire.setVisibility(View.VISIBLE);
            if (Objects.requireNonNull(otpView.getText()).toString().isEmpty()) {
                Toaster.customToast(getString(R.string.code_msg));
            } else if (otpView.getText().toString().length() != 4) {
                Toaster.customToast(getString(R.string.code_invalid));
            } else {
                sendVerifyOtp(otpView.getText().toString().trim(), dialog, modelArrayList, ordercreate);
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

    private void sendVerifyOtp(String otp, Dialog dialog, CoachDetails modelArrayList, OrderCreate ordercreate) {
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

                                if (Global.isOnline(mActivity)) {
                                    BookCoach();
                                } else {
                                    Global.showDialog(mActivity);
                                }

                                //finish();
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                otpView.setText("");
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

    private void updateMobileNo(String mobile, String phone_code, LinearLayout lin_opt, LinearLayout lin_mobile) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.VERIFY_MOBILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.tag("login response").e("%s", response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                                lin_mobile.setVisibility(View.GONE);
                                lin_opt.setVisibility(View.VISIBLE);
                                if (jsonObject.has("data")) {
                                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                    //phoneNumber = jsonObjectData.getString("temp_mobile_no");
                                    SessionManager.save_name(prefs, jsonObjectData.getString("username"));
                                    SessionManager.save_emailid(prefs, jsonObjectData.getString("email"));
                                    SessionManager.savePhone(prefs, jsonObjectData.getString("phone_number"));
                                    SessionManager.savePhoneCode(prefs, jsonObjectData.getString("phone_code"));
                                    SessionManager.save_mobile_verified(prefs, jsonObjectData.getString("is_mobile_verified"));
                                    //congratsDialog();

                                }
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                lin_mobile.setVisibility(View.VISIBLE);
                                lin_opt.setVisibility(View.GONE);
                                Toaster.customToastUp(jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Toaster.customToastUp(getResources().getString(R.string.socket_timeout));
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
                param.put("phone_code", phone_code);
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

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void sendPaymentSuccess() {

        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_BOOKING_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Booking Details", response);
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        BookingPaymentsDetails bookingPaymentsDetails = new BookingPaymentsDetails(jsonObject1);

                        Intent intent = new Intent(CoachDetailsActivity.this, BookingDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("Data", (Serializable) bookingPaymentsDetails);
                        intent.putExtra("FROM", "1");
                        intent.putExtra("PAYLATER", "PAID");
                        startActivity(intent);
                        finish();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Toaster.customToast(getResources().getString(R.string.socket_timeout));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("booking_id", String.valueOf(ordercreate.getBookingId()));
                Log.d("Param", param.toString());
                return param;
            }

        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

}

