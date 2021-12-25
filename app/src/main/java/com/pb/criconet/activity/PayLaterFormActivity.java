package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mancj.slideup.SlideUp;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.models.BookingPaymentsDetails;
import com.pb.criconet.models.OrderCreate;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class PayLaterFormActivity extends AppCompatActivity {
    private RequestQueue queue;
    private SharedPreferences prefs;
    private Context mContext;
    private Activity mActivity;
    CustomLoaderView loaderView;
    String payAmount = "", laterPayeeName, laterPayeeAddress, laterPayeePin, laterPayAlternateMobileNo;
    EditText edit_fullName;
    EditText edit_enter_address;
    EditText edit_pinCode;
    EditText edit_alternateMobile;
    FrameLayout flSubmitDetails;
    TextView tvPricee, tvOfferPricee, tvTotalAmountt, tvCouponDiss, tvSubtotall, tvTaxx;
    LinearLayout li_offerr;
    CheckBox ch_terams, ch_terams_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_later_form);
        mContext = this;
        mActivity = this;
        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(getResources().getString(R.string.Pay_Later_Form));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        inItView();
    }

    private void inItView() {

        edit_fullName = findViewById(R.id.edit_fullName);
        edit_enter_address = findViewById(R.id.edit_enter_address);
        edit_pinCode = findViewById(R.id.edit_pinCode);
        edit_alternateMobile = findViewById(R.id.edit_alternateMobile);
        tvPricee = findViewById(R.id.tvPricee);
        li_offerr = findViewById(R.id.li_offerr);
        tvPricee.setPaintFlags(tvPricee.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvOfferPricee = findViewById(R.id.tvOfferPricee);
        tvTotalAmountt = findViewById(R.id.tvTotalAmountt);
        flSubmitDetails = findViewById(R.id.flSubmitDetails);
        tvCouponDiss = findViewById(R.id.tvCouponDiss);
        tvSubtotall = findViewById(R.id.tvSubtotall);
        tvTaxx = findViewById(R.id.tvTaxx);
        ch_terams = findViewById(R.id.ch_terams);
        ch_terams_no = findViewById(R.id.ch_terams_no);

        tvTaxx.setText(getResources().getString(R.string.texex) + "\u20B9" + "4.50");
        tvSubtotall.setText(getResources().getString(R.string.subtotal) + "\u20B9" + "25.00");
        tvOfferPricee.setText("\u20B9" + "25.00" + "/" + getResources().getString(R.string.session));
        tvTotalAmountt.setText(getResources().getString(R.string.total_payable_amount) + "\u20B9" + "29.50");

        flSubmitDetails.setOnClickListener(v -> {
            laterPayeeName = edit_fullName.getText().toString().trim();
            laterPayeeAddress = edit_enter_address.getText().toString().trim();
            laterPayeePin = edit_pinCode.getText().toString().trim();
            laterPayAlternateMobileNo = edit_alternateMobile.getText().toString().trim();

            if (laterPayeeName.isEmpty()) {
                Toaster.customToastUp(getResources().getString(R.string.Please_Enter_Full_Name));
            } else if (laterPayeeAddress.isEmpty()) {
                Toaster.customToastUp(getResources().getString(R.string.Please_Enter_Address));
            } else if (laterPayeePin.isEmpty()) {
                Toaster.customToastUp(getResources().getString(R.string.Please_Enter_Pin_Code));
            } else if (!Global.isValidPincode(laterPayeePin)) {
                Toaster.customToastUp(getResources().getString(R.string.Please_Enter_Valid_Pin_Code));
            } else if (laterPayAlternateMobileNo.isEmpty()) {
                Toaster.customToastUp(getResources().getString(R.string.Please_enter_alternate_number));
            } else if (!Global.isValidPhoneNumber(laterPayAlternateMobileNo)) {
                Toaster.customToastUp(getResources().getString(R.string.Please_enter_valid_alternate_number));
            } else {

//                if (coupon_status.equalsIgnoreCase("apply")) {
//                    if (Global.isOnline(mActivity)) {
//                        BookCoachPaylater();
//                    } else {
//                        Global.showDialog(mActivity);
//                    }
//                } else {
//                    if (Global.isOnline(mActivity)) {
//                        submitDetails();
//                    } else {
//                        Global.showDialog(mActivity);
//                    }
//                }


            }

        });
    }

//    tvPricee.setText("\u20B9" + modelArrayList.getData().getPrice().getCoachPrice() + "/" + "Session");
//            tvTotalAmountt.setText("Total payable amount: " + "\u20B9" + modelArrayList.getData().getPrice().getWithTaxesAmount());
//            tvTaxx.setText("Taxes : " + "\u20B9" + modelArrayList.getData().getPrice().getTaxesAmount());
//            tvSubtotall.setText("Subtotal : " + "\u20B9" + modelArrayList.getData().getPrice().getPaymentPrice());
////            if (modelArrayList.getData().getIsOffer().equalsIgnoreCase("Yes")) {
//        tvOfferPricee.setText("\u20B9" + modelArrayList.getData().getPrice().getPaymentPrice() + "/" + "Session");
//        li_offerr.setVisibility(View.VISIBLE);
//    } else {
//        li_offerr.setVisibility(View.GONE);
//    }


//
//    private void BookCoachPaylater() {
//        //loaderView.showLoader();
//        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "create_booking_order", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("Booking Response", response);
//                //loaderView.hideLoader();
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    Gson gson = new Gson();
//                    ordercreate = gson.fromJson(response, OrderCreate.class);
//                    if (ordercreate != null && jsonObject.getString("status").equalsIgnoreCase("200")) {
//
//
//                        if (Global.isOnline(mActivity)) {
//                            submitDetails();
//                        } else {
//                            Global.showDialog(mActivity);
//                        }
//                    } else {
//                        if (ordercreate == null) {
//                            Toaster.customToast(ordercreate.getErrors().getErrorText());
//                        } else {
//
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                //loaderView.hideLoader();
//                Global.msgDialog(mActivity, "Error from server");
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Accept", "application/json");
//                headers.put("Content-Type", "application/x-www-form-urlencoded");
//                return (headers != null || headers.isEmpty()) ? headers : super.getHeaders();
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
////                param.put("booking_date", getDateTime());
//                Map<String, String> param = new HashMap<String, String>();
//                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", SessionManager.get_session_id(prefs));
//                param.put("coach_id", modelArrayList.getData().getCoachId());
//                param.put("booking_slot_date", dateGott);
//                param.put("booking_slot_id", mslotId);
//                param.put("booking_amount", subtotal);
//                param.put("payment_amount", payAmount);
//                param.put("taxes_amount", tax);
//                param.put("offer_id", modelArrayList.getData().getPrice().getOfferId());
//                param.put("coupon_code", coupon_text);
//                param.put("coupon_id", coupon_id);
//                param.put("old_booking_id", String.valueOf(ordercreate.getBookingId()));
//                param.put("cuurency_code", "INR");
//                Timber.e(param.toString());
//                return param;
//            }
//
//        };
//
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//    }
//
//    private void submitDetails() {
//        loaderView.showLoader();
//        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "booking_pay_later", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                loaderView.hideLoader();
//                Log.d("PayLaterResponse",response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
//                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                        BookingPaymentsDetails bookingPaymentsDetails = new BookingPaymentsDetails(jsonObject1);
//
//                        Intent intent = new Intent(ActivityCheckoutDetails.this, BookingDetailsActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        intent.putExtra("Data", (Serializable) bookingPaymentsDetails);
//                        intent.putExtra("FROM", "1");
//                        intent.putExtra("PAYLATER", "PAYLATER");
//                        startActivity(intent);
//                        finish();
//
//                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
//                        Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
//                    } else {
//                        Toaster.customToast(getResources().getString(R.string.socket_timeout));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                loaderView.hideLoader();
//                Global.msgDialog((Activity) mActivity, "Error from server");
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> param = new HashMap<String, String>();
//                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", SessionManager.get_session_id(prefs));
//                param.put("booking_id",String.valueOf(ordercreate.getBookingId()) );
//                param.put("name", laterPayeeName);
//                param.put("address", laterPayeeAddress);
//                param.put("pincode", laterPayeePin);
//                param.put("alternate_mobile_no", laterPayAlternateMobileNo);
//                Timber.e(param.toString());
//                return param;
//            }
//        };
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//    }
}