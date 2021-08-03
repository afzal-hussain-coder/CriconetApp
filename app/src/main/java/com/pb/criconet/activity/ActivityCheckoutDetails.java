package com.pb.criconet.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.gson.Gson;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.models.BookingHistory;
import com.pb.criconet.models.BookingPaymentsDetails;
import com.pb.criconet.models.CoachDetails;
import com.pb.criconet.models.OrderCreate;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class ActivityCheckoutDetails extends AppCompatActivity implements PaymentResultListener {

    private AppCompatActivity mActivity = ActivityCheckoutDetails.this;
    private static final String TAG = PaymentActivity.class.getSimpleName();
    private RequestQueue queue;
    private SharedPreferences prefs;
    private CoachDetails modelArrayList;
    private OrderCreate ordercreate;
    private CircleImageView ivProfileImage;
    private TextView tvCoachName;
    private TextView tvCoacTitle;
    private TextView tvPrice;
    private TextView tvOfferPrice;
    private TextView tvDate;
    private TextView tvtime;
    private TextView tvSubtotal;
    private TextView tvTax;
    private TextView tvTotalAmount,tvCoacExp,tvCouponDis;
    private FrameLayout btnPaynow;
    private FrameLayout fl_call;
    private FrameLayout fl_whatsapp,fl_removecoupon;
    private TextView tv_criconet_support_info;
    private LinearLayout li_support;
    EditText edit_text_apply_coupon;
    private FrameLayout btnApplayCoupon;
    CustomLoaderView loaderView;
    String coupon_text="";
    RelativeLayout rel_apply;
    String coupon_status="",coupon_id="",dateGott="",mslotId="",booking_id="",tax="",subtotal="",payableAmount="";
    String payAmount="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_details);
        mActivity = this;
        loaderView = CustomLoaderView.initialize(this);
        queue = Volley.newRequestQueue(ActivityCheckoutDetails.this);
        modelArrayList = (CoachDetails) getIntent().getSerializableExtra("key");
        ordercreate = (OrderCreate) getIntent().getSerializableExtra("key1");
        dateGott=getIntent().getStringExtra("booking_slot_date");
        mslotId=getIntent().getStringExtra("booking_slot_id");
        payableAmount=String.valueOf(ordercreate.getPaymentOption().getAmount());

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
        mTitle.setText("Checkout Details");
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);

        initializeView();
    }

    private void initializeView() {

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvCoachName = findViewById(R.id.tvCoachName);
        tvCoacTitle = findViewById(R.id.tvCoacTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvOfferPrice = findViewById(R.id.tvOfferPrice);
        tvDate = findViewById(R.id.tvDate);
        tvtime = findViewById(R.id.tvtime);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvCouponDis = findViewById(R.id.tvCouponDis);
        tvTax = findViewById(R.id.tvTax);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        li_support = findViewById(R.id.li_support);
        tvCoacExp = findViewById(R.id.tvCoacExp);
        tv_criconet_support_info = findViewById(R.id.tv_criconet_support_info);
        tv_criconet_support_info.setText(ordercreate.getCriconetSupport().getTxtmsg2());
        btnPaynow = findViewById(R.id.btnPaynow);
        rel_apply = findViewById(R.id.rel_apply);
        fl_removecoupon = findViewById(R.id.fl_removecoupon);
//
        if(SessionManager.getCouponCode(prefs).equalsIgnoreCase("1")){
            rel_apply.setVisibility(View.VISIBLE);
        }else{
            rel_apply.setVisibility(View.GONE);
        }

        edit_text_apply_coupon = findViewById(R.id.edit_text_apply_coupon);
        btnApplayCoupon = findViewById(R.id.btnApplayCoupon);
        btnApplayCoupon.setOnClickListener(v -> {
            coupon_text = edit_text_apply_coupon.getText().toString().trim();
            coupon_status="apply";
            if(coupon_text.equalsIgnoreCase("")){
                Toaster.customToast("Please Enter coupon code");
            }else{
                if (Global.isOnline(mActivity)) {
                    checkCouponCode();
                } else {
                    Global.showDialog(mActivity);
                }

                //Toaster.customToast("Hi"+coupon_text);
            }
        });
        fl_removecoupon.setOnClickListener(v -> {

            coupon_status="remove";
            if (Global.isOnline(mActivity)) {
                applyCoupon();
            } else {
                Global.showDialog(mActivity);
            }
        });

        btnPaynow.setOnClickListener(view -> {
           int a= ordercreate.getPaymentOption().getAmount();
           //a=0;

            if(a==0){
                Intent intent = new Intent(ActivityCheckoutDetails.this, BookingActivity.class);
                startActivity(intent);
                finish();
            }else{
                if(coupon_status.equalsIgnoreCase("apply")){
                    if (Global.isOnline(mActivity)) {
                        BookCoach();
                    } else {
                        Global.showDialog(mActivity);
                    }
                }else{
                    if (Global.isOnline(mActivity)) {
                        startPayment();
                    } else {
                        Global.showDialog(mActivity);
                    }
                }



            }

        });
        fl_call =findViewById(R.id.fl_call);
        fl_call.setOnClickListener(v -> {
                startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+ordercreate.getCriconetSupport().getContact_number())));
        });

        fl_whatsapp = findViewById(R.id.fl_whatsapp);
        fl_whatsapp.setOnClickListener(v -> {
            String number =ordercreate.getCriconetSupport().getContact_number();
            String url = "https://api.whatsapp.com/send?phone="+number;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        if(ordercreate.getCriconet_support().equalsIgnoreCase("1")){
        tv_criconet_support_info.setVisibility(View.VISIBLE);
            li_support.setVisibility(View.VISIBLE);
        }else{
            tv_criconet_support_info.setVisibility(View.GONE);
            li_support.setVisibility(View.GONE);
        }

        Checkout.preload(getApplicationContext());

        if (modelArrayList != null && ordercreate != null) {
            Glide.with(mActivity).load(modelArrayList.getData().getAvatar())
                    .into(ivProfileImage);
            tvCoachName.setText(Global.capitizeString(modelArrayList.getData().getName()));
            tvCoacTitle.setText(modelArrayList.getData().getProfileTitle());
            tvCoacExp.setText(modelArrayList.getData().getExps());
            tvPrice.setText("Price: " + "\u20B9" + modelArrayList.getData().getPrice().getCoachPrice());
            tvTax.setText("Taxes : " + "\u20B9" + modelArrayList.getData().getPrice().getTaxesAmount());
            tvTotalAmount.setText("Total payable amount: " + "\u20B9" + modelArrayList.getData().getPrice().getWithTaxesAmount());
            tvSubtotal.setText("Subtotal : " + "\u20B9" + modelArrayList.getData().getPrice().getPaymentPrice());
            tvDate.setText(Global.convertUTCDateToLocall(ordercreate.getPaymentOption().getSessionDate()));
            tvtime.setText(ordercreate.getPaymentOption().getSessionTime());
            if (modelArrayList.getData().getIsOffer().equalsIgnoreCase("Yes")) {
                tvOfferPrice.setVisibility(View.VISIBLE);
                tvOfferPrice.setText("Offer price: " + "\u20B9" + modelArrayList.getData().getPrice().getPaymentPrice());
            } else {
                tvOfferPrice.setVisibility(View.GONE);
            }
        }


    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", ordercreate.getPaymentOption().getName());
            options.put("description", ordercreate.getPaymentOption().getDescription());
            options.put("send_sms_hash", true);
            options.put("allow_rotation", false);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", modelArrayList.getData().getAvatar());
            options.put("currency", "INR");
            if(coupon_status.equalsIgnoreCase("apply")){
                options.put("amount", payableAmount.toString().trim());
            }else{
                options.put("amount", payableAmount);
            }

            JSONObject preFill = new JSONObject();
            preFill.put("email", ordercreate.getPaymentOption().getPrefill().getEmail());
            preFill.put("contact", ordercreate.getPaymentOption().getPrefill().getContact());
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Timber.d(razorpayPaymentID);
            if (Global.isOnline(mActivity)) {
                sendPaymentSuccess(razorpayPaymentID);
            } else {
                Global.showDialog(mActivity);
            }

        } catch (Exception e) {
            Timber.e(e, "Exception in onPaymentSuccess");
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            //Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    private void sendPaymentSuccess(String razorpayPaymentID) {

        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.PAYMENT_SUCCESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Payment Response", response);
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        BookingPaymentsDetails bookingPaymentsDetails = new BookingPaymentsDetails(jsonObject1);

                        Intent intent = new Intent(ActivityCheckoutDetails.this, BookingDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("Data", (Serializable) bookingPaymentsDetails);
                        intent.putExtra("FROM", "1");
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
                Global.msgDialog(mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", modelArrayList.getData().getCoachId());
                param.put("booking_id", String.valueOf(ordercreate.getBookingId()));
                param.put("order_id", ordercreate.getPaymentOption().getOrderId());
                param.put("razorpay_payment_id", razorpayPaymentID);
                param.put("razorpay_signature", "kjqdka");
                Log.d("Param", param.toString());
                return param;
            }

        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void applyCoupon() {

        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.coupan_applay, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Payment Response", response);
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                        if(jsonObject1.has("coupon_status")){
                            coupon_status = jsonObject1.getString("coupon_status");
                        }

                        if(jsonObject1.has("taxes_amount")){
                            tax =jsonObject1.getString("taxes_amount");
                            tvTax.setText("Taxes : " + "\u20B9" + jsonObject1.getString("taxes_amount"));
                        }
                        if(jsonObject1.has("payment_amount")){
                            payAmount=jsonObject1.getString("payment_amount");
                            tvTotalAmount.setText("Total payable amount: " + "\u20B9" + jsonObject1.getString("payment_amount"));
                        }
                        if(jsonObject1.has("payment_price")){
                            subtotal=jsonObject1.getString("payment_price");
                            tvSubtotal.setText("Subtotal : " + "\u20B9" + jsonObject1.getString("payment_price"));
                        }

                        if(jsonObject1.has("coupon_discount")){

                            if(jsonObject1.getString("coupon_discount").equalsIgnoreCase("")){
                                tvCouponDis.setVisibility(View.GONE);
                            }else{
                                tvCouponDis.setVisibility(View.VISIBLE);
                                tvCouponDis.setText("Coupon Discount : " + "\u20B9" + jsonObject1.getString("coupon_discount"));
                            }
                        }

                        if(coupon_status.equalsIgnoreCase("apply")){
                            rel_apply.setVisibility(View.GONE);
                            fl_removecoupon.setVisibility(View.VISIBLE);
                        }else{
                            rel_apply.setVisibility(View.VISIBLE);
                            fl_removecoupon.setVisibility(View.GONE);
                        }
                        //payableAmount =jsonObject1.getString("payment_amount");
                        payableAmount =jsonObject1.getString("total_payable_amount");



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
                Global.msgDialog(mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", modelArrayList.getData().getCoachId());
                param.put("coupon_code", coupon_text);
                param.put("coupon_id", coupon_id);
                param.put("order_id", ordercreate.getPaymentOption().getOrderId());
                param.put("coupon_status", coupon_status);
                Log.d("Param", param.toString());
                return param;
            }



        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void checkCouponCode() {

        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.check_coupon_code, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("check coupon Response", response);
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if(jsonObject1.has("id")){
                            coupon_id= jsonObject1.getString("id");
                            if (Global.isOnline(mActivity)) {
                                edit_text_apply_coupon.setText("");
                                applyCoupon();
                            } else {
                                Global.showDialog(mActivity);
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                Global.msgDialog(mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coupon_code", coupon_text);
                Log.d("Param", param.toString());
                return param;
            }



        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void BookCoach() {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "create_booking_order", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Booking Response", response);
                //loaderView.hideLoader();
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    Gson gson = new Gson();
                    ordercreate = gson.fromJson(response, OrderCreate.class);
                    if (ordercreate != null && jsonObject.getString("status").equalsIgnoreCase("200")) {


                        if (Global.isOnline(mActivity)) {
                            startPayment();
                        } else {
                            Global.showDialog(mActivity);
                        }
                    } else {
                        if (ordercreate == null){
                            Toaster.customToast(ordercreate.getErrors().getErrorText());
                        }else{

                        }

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //loaderView.hideLoader();
                Global.msgDialog(mActivity, "Error from server");
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
                param.put("coach_id", modelArrayList.getData().getCoachId());
                param.put("booking_slot_date", dateGott);
                param.put("booking_slot_id", mslotId);
                param.put("booking_amount",subtotal);
                param.put("payment_amount", payAmount);
                param.put("taxes_amount", tax);
                param.put("offer_id", modelArrayList.getData().getPrice().getOfferId());
                param.put("coupon_code", coupon_text);
                param.put("coupon_id",coupon_id);
                param.put("old_booking_id",String.valueOf(ordercreate.getBookingId()));
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


}