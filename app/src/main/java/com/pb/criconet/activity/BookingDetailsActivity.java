package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.BookingHistoryAdapter;
import com.pb.criconet.models.BookingHistory;
import com.pb.criconet.models.BookingPaymentsDetails;
import com.pb.criconet.models.CoachDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class BookingDetailsActivity extends AppCompatActivity {
    private CircleImageView ivProfileImagee;
    private TextView tvCoachNamee;
    private TextView tvSessionDetails;
    private TextView tvSessionDateTime;
    private TextView tvBookingDate;
    private TextView tvSessionAmount;
    private TextView tvBookingStataus;
    FrameLayout fl_book_another_session;
    FrameLayout fl_navigate_home_page;
    FrameLayout fl_cancel_booking;
    JSONObject jsonObject;
    Activity mActivity;
    String bookingId, fromWhere = "";
    private RequestQueue queue;
    CustomLoaderView loaderView;
    BookingPaymentsDetails bookingPaymentsDetails;
    private SharedPreferences prefs;
    String paymentMethod = "";
    LinearLayout li_paid, li_paylater;
    TextView tvSessionAmount_paylater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details2);
        mActivity = this;
        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

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
        mTitle.setText(getResources().getString(R.string.Booking_Details));

        ivProfileImagee = findViewById(R.id.ivProfileImagee);
        tvCoachNamee = findViewById(R.id.tvCoachNamee);
        tvSessionDetails = findViewById(R.id.tvSessionDetails);
        tvSessionDateTime = findViewById(R.id.tvSessionDateTime);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvSessionAmount = findViewById(R.id.tvSessionAmount);
        tvBookingStataus = findViewById(R.id.tvBookingStataus);
        li_paid = findViewById(R.id.li_paid);
        li_paylater = findViewById(R.id.li_paylater);
        tvSessionAmount_paylater = findViewById(R.id.tvSessionAmount_paylater);
        fl_book_another_session = findViewById(R.id.fl_book_another_session);
        fl_book_another_session.setOnClickListener(v -> {
            startActivity(new Intent(BookingDetailsActivity.this, MainActivity.class).putExtra("type", "coachFreagment").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        });
        fl_navigate_home_page = findViewById(R.id.fl_navigate_home_page);
        fl_navigate_home_page.setOnClickListener(v -> {
            startActivity(new Intent(BookingDetailsActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        });
        fl_cancel_booking = findViewById(R.id.fl_cancel_booking);
        fl_cancel_booking.setOnClickListener(v -> {
            cancelAlertDialog();
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromWhere = bundle.getString("FROM");

            if (fromWhere.equalsIgnoreCase("1")) {
                paymentMethod = bundle.getString("PAYLATER");
                bookingPaymentsDetails = (BookingPaymentsDetails) getIntent().getSerializableExtra("Data");
                bookingDetails(bookingPaymentsDetails);
            } else {
                bookingId = bundle.getString("BookingID");
                if (Global.isOnline(this)) {
                    getBookingDetails();
                } else {
                    Global.showDialog(this);
                }
            }
        }


    }

    private void bookingDetails(BookingPaymentsDetails bookingPaymentsDetails) {

        bookingId = bookingPaymentsDetails.getId();
        try {
            if (!bookingPaymentsDetails.getAvatar().isEmpty()) {
                Glide.with(mActivity).load(bookingPaymentsDetails.getAvatar())
                        .into(ivProfileImagee);
            } else {
                Glide.with(mActivity).load(R.drawable.placeholder_user)
                        .into(ivProfileImagee);
            }
            tvCoachNamee.setText(bookingPaymentsDetails.getCoachName());
            tvSessionDetails.setText(getResources().getString(R.string.bookingId) + " " + bookingPaymentsDetails.getBooking_id());
            tvSessionDateTime.setText(Global.convertUTCDateToLocalDate(bookingPaymentsDetails.getOnlineSessionStartTime()) + " , " + bookingPaymentsDetails.getBookingSlotTxt());
            tvBookingDate.setText(Global.getDate(Long.parseLong(bookingPaymentsDetails.getBookingTime())));

            if (paymentMethod.equalsIgnoreCase("PAYLATER")) {
                li_paid.setVisibility(View.GONE);
                li_paylater.setVisibility(View.VISIBLE);
                tvSessionAmount_paylater.setText("\u20B9" + " " + bookingPaymentsDetails.getPaymentAmount());
            } else if (paymentMethod.equalsIgnoreCase("PAID")) {
                li_paid.setVisibility(View.VISIBLE);
                li_paylater.setVisibility(View.GONE);
                tvSessionAmount.setText("\u20B9" + " " + bookingPaymentsDetails.getPaymentAmount());
            } else {
                li_paid.setVisibility(View.VISIBLE);
                li_paylater.setVisibility(View.GONE);
                tvSessionAmount.setText("\u20B9" + " " + bookingPaymentsDetails.getPaymentAmount());
            }


            if (bookingPaymentsDetails.getBookingStatus().equalsIgnoreCase("1")) {
                tvBookingStataus.setText(getString(R.string.booking_confirmed));
            } else {
                tvBookingStataus.setText(getResources().getString(R.string.Booking_in_processing));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void cancelAlertDialog() {
        final Dialog dialog = new Dialog(BookingDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_cancel_booking);
        dialog.setCancelable(false);
        FrameLayout fl_no = dialog.findViewById(R.id.fl_no);
        fl_no.setOnClickListener(v -> {
            dialog.dismiss();
        });
        FrameLayout fl_yes = dialog.findViewById(R.id.fl_yes);
        fl_yes.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(BookingDetailsActivity.this, CancellationFeedbackFormActivity.class).putExtra("BookingId", bookingId));
        });
        dialog.show();
    }

    private void getBookingDetails() {
        loaderView.showLoader();
//        progressDialog = Global.getProgressDialog(this, CCResource.getString(this, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_booking_details", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BokingDetails Response", response);
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        bookingPaymentsDetails = new BookingPaymentsDetails(jsonObject1);
                        bookingDetails(bookingPaymentsDetails);
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Toaster.customToast(getResources().getString(R.string.socket_timeout));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //Global.dismissDialog(progressDialog);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                Global.msgDialog((Activity) mActivity, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("booking_id", bookingId);
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