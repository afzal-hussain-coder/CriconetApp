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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.json.JSONException;
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
    private TextView tvTotalAmount, tvCoacExp, tvCouponDis;
    private FrameLayout btnPaynow, btnPayLater;
    private FrameLayout fl_call;
    private FrameLayout fl_whatsapp, fl_removecoupon;
    private TextView tv_criconet_support_info;
    private LinearLayout li_support;
    EditText edit_text_apply_coupon;
    private FrameLayout btnApplayCoupon;
    CustomLoaderView loaderView;
    String coupon_text = "";
    RelativeLayout rel_apply;
    String coupon_status = "", coupon_id = "", dateGott = "", mslotId = "", booking_id = "", tax = "", subtotal = "", payableAmount = "";
    String payAmount = "", laterPayeeName, laterPayeeAddress, laterPayeePin, laterPayAlternateMobileNo;
    private SlideUp slideUp;
    private View dim, rootView;
    private View slideView;
    EditText edit_fullName;
    EditText edit_enter_address;
    EditText edit_pinCode;
    EditText edit_alternateMobile;
    FrameLayout flSubmitDetails;
    TextView tvPricee, tvOfferPricee, tvTotalAmountt, tvCouponDiss, tvSubtotall, tvTaxx;
    LinearLayout li_offerr;
    String payLater_settings_status="",coupon_code_settings_status="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_details);
        mActivity = this;
        loaderView = CustomLoaderView.initialize(this);
        queue = Volley.newRequestQueue(ActivityCheckoutDetails.this);
        modelArrayList = (CoachDetails) getIntent().getSerializableExtra("key");
        ordercreate = (OrderCreate) getIntent().getSerializableExtra("key1");
        dateGott = getIntent().getStringExtra("booking_slot_date");
        mslotId = getIntent().getStringExtra("booking_slot_id");
        payableAmount = String.valueOf(ordercreate.getPaymentOption().getAmount());

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
        if (Global.isOnline(mActivity)) {
            checkAppSettings();
        } else {
            Global.showDialog(mActivity);
        }
    }

    private void initializeView() {

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvCoachName = findViewById(R.id.tvCoachName);
        tvCoacTitle = findViewById(R.id.tvCoacTitle);
        tvPrice = findViewById(R.id.tvPrice);

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
        btnPayLater = findViewById(R.id.btnPayLater);
        rel_apply = findViewById(R.id.rel_apply);
        fl_removecoupon = findViewById(R.id.fl_removecoupon);
        rootView = findViewById(R.id.root_view);
        slideView = findViewById(R.id.slideView);
        dim = findViewById(R.id.dim);
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
                .withSlideFromOtherView(rootView)
                .build();
        edit_fullName = findViewById(R.id.edit_fullName);
        edit_enter_address = findViewById(R.id.edit_enter_address);
        edit_pinCode = findViewById(R.id.edit_pinCode);
        edit_alternateMobile = findViewById(R.id.edit_alternateMobile);
        tvPricee = findViewById(R.id.tvPricee);
        li_offerr = findViewById(R.id.li_offerr);
        //tvPricee.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvOfferPricee = findViewById(R.id.tvOfferPricee);
        tvTotalAmountt = findViewById(R.id.tvTotalAmountt);
        flSubmitDetails = findViewById(R.id.flSubmitDetails);
        tvCouponDiss = findViewById(R.id.tvCouponDiss);
        tvSubtotall = findViewById(R.id.tvSubtotall);
        tvTaxx = findViewById(R.id.tvTaxx);

        flSubmitDetails.setOnClickListener(v -> {
            laterPayeeName = edit_fullName.getText().toString().trim();
            laterPayeeAddress = edit_enter_address.getText().toString().trim();
            laterPayeePin = edit_pinCode.getText().toString().trim();
            laterPayAlternateMobileNo = edit_alternateMobile.getText().toString().trim();

            if (laterPayeeName.isEmpty()) {
                Toaster.customToastUp("Please Enter Full Name");
            } else if (laterPayeeAddress.isEmpty()) {
                Toaster.customToastUp("Please Enter Address");
            } else if (!Global.isValidAddress(laterPayeeAddress)) {
                Toaster.customToastUp("Address must have min 5 character");
            } else if (laterPayeePin.isEmpty()) {
                Toaster.customToastUp("Please Enter Pin Code");
            } else if (!Global.isValidPincode(laterPayeePin)) {
                Toaster.customToastUp("Please Enter Valid Pin Code");
            } else if (laterPayAlternateMobileNo.isEmpty()) {
                Toaster.customToastUp("Please enter alternate number");
            } else if (!Global.isValidPhoneNumber(laterPayAlternateMobileNo)) {
                Toaster.customToastUp("Please enter valid alternate number");
            } else {

                if (coupon_status.equalsIgnoreCase("apply")) {
                    if (Global.isOnline(mActivity)) {
                        BookCoachPaylater();
                    } else {
                        Global.showDialog(mActivity);
                    }
                } else {
                    if (Global.isOnline(mActivity)) {
                        submitDetails();
                    } else {
                        Global.showDialog(mActivity);
                    }
                }


            }

        });
//
        if (SessionManager.getCouponCode(prefs).equalsIgnoreCase("1")) {
            rel_apply.setVisibility(View.VISIBLE);
        } else {
            rel_apply.setVisibility(View.GONE);
        }

        edit_text_apply_coupon = findViewById(R.id.edit_text_apply_coupon);
        btnApplayCoupon = findViewById(R.id.btnApplayCoupon);
        btnApplayCoupon.setOnClickListener(v -> {
            coupon_text = edit_text_apply_coupon.getText().toString().trim();
            coupon_status = "apply";
            if (coupon_text.equalsIgnoreCase("")) {
                Toaster.customToast("Please Enter coupon code");
            } else {
                if (Global.isOnline(mActivity)) {
                    checkCouponCode();
                } else {
                    Global.showDialog(mActivity);
                }

                //Toaster.customToast("Hi"+coupon_text);
            }
        });
        fl_removecoupon.setOnClickListener(v -> {

            coupon_status = "remove";
            if (Global.isOnline(mActivity)) {
                applyCoupon();
            } else {
                Global.showDialog(mActivity);
            }
        });

        btnPaynow.setOnClickListener(view -> {
            try {
                int a = ordercreate.getPaymentOption().getAmount();
                if (a == 0) {
                    Intent intent = new Intent(ActivityCheckoutDetails.this, BookingActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (coupon_status.equalsIgnoreCase("apply")) {
                        if (Global.isOnline(mActivity)) {
                            BookCoach();
                        } else {
                            Global.showDialog(mActivity);
                        }
                    } else {
                        if (Global.isOnline(mActivity)) {
                            startPayment();
                        } else {
                            Global.showDialog(mActivity);
                        }
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();

            }


        });

        btnPayLater.setOnClickListener(v -> {
            slideUp.show();
        });

        fl_call = findViewById(R.id.fl_call);
        fl_call.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + ordercreate.getCriconetSupport().getContact_number())));
        });

        fl_whatsapp = findViewById(R.id.fl_whatsapp);
        fl_whatsapp.setOnClickListener(v -> {
            String number = ordercreate.getCriconetSupport().getContact_number();
            String url = "https://api.whatsapp.com/send?phone=" + number;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        if (ordercreate.getCriconet_support().equalsIgnoreCase("1")) {
            tv_criconet_support_info.setVisibility(View.VISIBLE);
            li_support.setVisibility(View.VISIBLE);
        } else {
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
            if (modelArrayList.getData().getPrice().getOfferId().equalsIgnoreCase("0")) {
                tvOfferPrice.setVisibility(View.GONE);
            } else {
                tvOfferPrice.setVisibility(View.VISIBLE);
                tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvOfferPrice.setText("Offer price: " + "\u20B9" + modelArrayList.getData().getPrice().getPaymentPrice());
            }
            tvPricee.setText("\u20B9" + modelArrayList.getData().getPrice().getCoachPrice() + "/" + "Session");
            tvTotalAmountt.setText("Total payable amount: " + "\u20B9" + modelArrayList.getData().getPrice().getWithTaxesAmount());
            tvTaxx.setText("Taxes : " + "\u20B9" + modelArrayList.getData().getPrice().getTaxesAmount());
            tvSubtotall.setText("Subtotal : " + "\u20B9" + modelArrayList.getData().getPrice().getPaymentPrice());
            if (modelArrayList.getData().getPrice().getOfferId().equalsIgnoreCase("0")) {
                li_offerr.setVisibility(View.GONE);
            } else {
                tvPricee.setPaintFlags(tvPricee.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvOfferPricee.setText("\u20B9" + modelArrayList.getData().getPrice().getPaymentPrice() + "/" + "Session");
                li_offerr.setVisibility(View.VISIBLE);
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
            if (coupon_status.equalsIgnoreCase("apply")) {
                options.put("amount", payableAmount.toString().trim());
            } else {
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

                        if (jsonObject1.has("coupon_status")) {
                            coupon_status = jsonObject1.getString("coupon_status");
                        }

                        if (jsonObject1.has("taxes_amount")) {
                            tax = jsonObject1.getString("taxes_amount");
                            tvTax.setText("Taxes : " + "\u20B9" + jsonObject1.getString("taxes_amount"));
                            tvTaxx.setText("Taxes : " + "\u20B9" + jsonObject1.getString("taxes_amount"));
                        }
                        if (jsonObject1.has("payment_amount")) {
                            payAmount = jsonObject1.getString("payment_amount");
                            tvTotalAmount.setText("Total payable amount: " + "\u20B9" + jsonObject1.getString("payment_amount"));
                            tvTotalAmountt.setText("Total payable amount: " + "\u20B9" + jsonObject1.getString("payment_amount"));
                        }
                        if (jsonObject1.has("payment_price")) {
                            subtotal = jsonObject1.getString("payment_price");
                            tvSubtotal.setText("Subtotal : " + "\u20B9" + jsonObject1.getString("payment_price"));
                            tvSubtotall.setText("Subtotal : " + "\u20B9" + jsonObject1.getString("payment_price"));
                        }

                        if (jsonObject1.has("coupon_discount")) {

                            if (jsonObject1.getString("coupon_discount").equalsIgnoreCase("")) {
                                tvCouponDis.setVisibility(View.GONE);
                                tvCouponDiss.setVisibility(View.GONE);
                            } else {
                                tvCouponDis.setVisibility(View.VISIBLE);
                                tvCouponDiss.setVisibility(View.VISIBLE);
                                tvCouponDis.setText("Coupon Discount : " + "\u20B9" + jsonObject1.getString("coupon_discount"));
                                tvCouponDiss.setText("Coupon Discount : " + "\u20B9" + jsonObject1.getString("coupon_discount"));
                            }
                        }

                        if (coupon_status.equalsIgnoreCase("apply")) {
                            rel_apply.setVisibility(View.GONE);
                            fl_removecoupon.setVisibility(View.VISIBLE);
                        } else {
                            rel_apply.setVisibility(View.VISIBLE);
                            fl_removecoupon.setVisibility(View.GONE);
                        }
                        //payableAmount =jsonObject1.getString("payment_amount");
                        payableAmount = jsonObject1.getString("total_payable_amount");



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
                        if (jsonObject1.has("id")) {
                            coupon_id = jsonObject1.getString("id");
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
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Gson gson = new Gson();
                    ordercreate = gson.fromJson(response, OrderCreate.class);
                    if (ordercreate != null && jsonObject.getString("status").equalsIgnoreCase("200")) {


                        // Log.d("Payment",ordercreate.getPayment()+".....");
                         if(ordercreate.getPayment()==0){
                             Intent intent = new Intent(ActivityCheckoutDetails.this, BookingActivity.class);
                             startActivity(intent);
                             finish();
                         }else{
                             if (Global.isOnline(mActivity)) {
                                 startPayment();
                             } else {
                                 Global.showDialog(mActivity);
                             }
                         }

                    } else {
                        if (ordercreate == null) {
                            Toaster.customToast(ordercreate.getErrors().getErrorText());
                        } else {

                        }

                    }
                } catch (Exception e) {
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
                param.put("booking_amount", subtotal);
                param.put("payment_amount", payAmount);
                param.put("taxes_amount", tax);
                param.put("offer_id", modelArrayList.getData().getPrice().getOfferId());
                param.put("coupon_code", coupon_text);
                param.put("coupon_id", coupon_id);
                param.put("old_booking_id", String.valueOf(ordercreate.getBookingId()));
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

    private void BookCoachPaylater() {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "create_booking_order", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Booking Response", response);
                //loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Gson gson = new Gson();
                    ordercreate = gson.fromJson(response, OrderCreate.class);
                    if (ordercreate != null && jsonObject.getString("status").equalsIgnoreCase("200")) {


                        if (Global.isOnline(mActivity)) {
                            submitDetails();
                        } else {
                            Global.showDialog(mActivity);
                        }
                    } else {
                        if (ordercreate == null) {
                            Toaster.customToast(ordercreate.getErrors().getErrorText());
                        } else {

                        }

                    }
                } catch (Exception e) {
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
                param.put("booking_amount", subtotal);
                param.put("payment_amount", payAmount);
                param.put("taxes_amount", tax);
                param.put("offer_id", modelArrayList.getData().getPrice().getOfferId());
                param.put("coupon_code", coupon_text);
                param.put("coupon_id", coupon_id);
                param.put("old_booking_id", String.valueOf(ordercreate.getBookingId()));
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

    private void submitDetails() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "booking_pay_later", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loaderView.hideLoader();
                Log.d("PayLaterResponse", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        BookingPaymentsDetails bookingPaymentsDetails = new BookingPaymentsDetails(jsonObject1);

                        Intent intent = new Intent(ActivityCheckoutDetails.this, BookingDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("Data", (Serializable) bookingPaymentsDetails);
                        intent.putExtra("FROM", "1");
                        intent.putExtra("PAYLATER", "PAYLATER");
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
                Global.msgDialog((Activity) mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("booking_id", String.valueOf(ordercreate.getBookingId()));
                param.put("name", laterPayeeName);
                param.put("address", laterPayeeAddress);
                param.put("pincode", laterPayeePin);
                param.put("alternate_mobile_no", laterPayAlternateMobileNo);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
    private void checkAppSettings() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + Global.GET_APP_SETTINGS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AppSettingsResponse", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                        if(jsonObject1.has("paylater")) {
                            try {
                                payLater_settings_status = jsonObject1.getString("paylater");
                                if(payLater_settings_status.equalsIgnoreCase("0")){
                                    btnPayLater.setVisibility(View.GONE);
                                }else{
                                    btnPayLater.setVisibility(View.VISIBLE);
                                }

                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }
                        }
                        if(jsonObject1.has("coupon_code")) {
                            try {
                                coupon_code_settings_status = jsonObject1.getString("coupon_code");
                                if(coupon_code_settings_status.equalsIgnoreCase("0")){
                                    rel_apply.setVisibility(View.GONE);
                                }else{
                                    rel_apply.setVisibility(View.VISIBLE);
                                }

                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
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
                //Global.msgDialog((Activity) mActivity, "Error from server");
            }
        }) ;
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

}