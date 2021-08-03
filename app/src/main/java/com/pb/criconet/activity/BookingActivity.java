package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.pb.criconet.Utills.CCResource;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.DataModel;
import com.pb.criconet.Utills.DateDropDownView;
import com.pb.criconet.Utills.DropDownView;
import com.pb.criconet.Utills.FilterBookingDropDownView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.BookingHistoryAdapter;
import com.pb.criconet.models.BookingHistory;
import com.pb.criconet.models.BookingPaymentsDetails;
import com.pb.criconet.models.CoachAccept;
import com.pb.criconet.models.ConstantApp;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class BookingActivity extends AppCompatActivity implements BookingHistoryAdapter.clickCallback {
    private RecyclerView coachlist;
    private RequestQueue queue;
    private SharedPreferences prefs;
    private ProgressDialog progressDialog;
    private Context mContext;
    private Activity mActivity;
    private boolean loading = true;
    private SlideUp slideUp;
    private View dim, rootView;
    private View slideView;
    private CircleImageView ivProfileImage;
    private TextView tvCoachName;
    private TextView tvSessionDetails;
    private TextView tvSessionDateTime;
    private TextView tvBookingDate;
    private TextView tvSessionAmount;
    private TextView tvBookingStataus;
    private FrameLayout fl_cancel_booking;
    private FrameLayout fl_join_session;
    private FrameLayout fl_cancelled;
    private String bookingId;
    CustomLoaderView loaderView;
    FilterBookingDropDownView drop_filter_booking;
    DateDropDownView drop_date_from;
    DateDropDownView drop_date_to;
    TextView tv_from_date,tvInfo;
    TextView tv_to_date,notfound;
    FrameLayout fl_search;
    private ArrayList<DataModel> option = new ArrayList<>();
    private String filterType="" ;
    private String from_date="";
    private String to_date="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mContext = this;
        mActivity =this;
        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BookingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText("Booking History");

        initializeView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Global.isOnline(this)) {
            getBookingHistory();
        } else {
            Global.showDialog(this);
        }
    }

    private void initializeView() {
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
        notfound = findViewById(R.id.notfound);
        coachlist=findViewById(R.id.coachlist);
        coachlist.setHasFixedSize(true);
        coachlist.setLayoutManager(new LinearLayoutManager(this));
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvCoachName =findViewById(R.id.tvCoachName);
        tvSessionDetails =findViewById(R.id.tvSessionDetails);
        tvSessionDateTime =findViewById(R.id.tvSessionDateTime);
        tvBookingDate=findViewById(R.id.tvBookingDate);
        tvSessionAmount=findViewById(R.id.tvSessionAmount);
        tvBookingStataus=findViewById(R.id.tvBookingStataus);
        fl_cancel_booking = findViewById(R.id.fl_cancel_booking);
        fl_join_session = findViewById(R.id.fl_join_session);
        fl_cancelled = findViewById(R.id.fl_cancelled);
        tvInfo = findViewById(R.id.tvInfo);
        drop_filter_booking = findViewById(R.id.drop_filter_booking);
        option.add(new DataModel("All Booking"));
        option.add(new DataModel("Cancelled booking"));
        option.add(new DataModel("Confirmed booking"));
        drop_filter_booking.setOptionList(option);
        filterType = option.get(0).getName();
        drop_filter_booking.setText(filterType);

        drop_filter_booking.setClickListener(new FilterBookingDropDownView.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {

                if(name.equalsIgnoreCase("Cancelled booking")){
                    filterType= "cancelled";
                }else if(name.equalsIgnoreCase("Confirmed booking")){
                    filterType= "confirmed";
                }else{
                    filterType = name;
                }
            }


            @Override
            public void onDismiss() {
            }
        });

        drop_date_from = findViewById(R.id.drop_date_from);
        drop_date_to = findViewById(R.id.drop_date_to);
        tv_to_date = findViewById(R.id.tv_to_date);
        tv_from_date = findViewById(R.id.tv_from_date);
        drop_date_from.setClickListener(new DateDropDownView.onClickInterface() {
            @Override
            public void onClickAction() {

            }

            @Override
            public void onClickDone(String name) {
                from_date = name;
                tv_from_date.setText(name);
            }

            @Override
            public void onDismiss() {

            }
        });
        drop_date_to.setClickListener(new DateDropDownView.onClickInterface() {
            @Override
            public void onClickAction() {

            }

            @Override
            public void onClickDone(String name) {
                to_date= name;
                tv_to_date.setText(name);
            }

            @Override
            public void onDismiss() {

            }
        });

        fl_search = findViewById(R.id.fl_search);
        fl_search.setOnClickListener(v -> {
            getBookingHistory();
//            if (isValidateSearch()){
//                getBookingHistory();
//            }
        });


    }

    private void getBookingHistory() {
        loaderView.showLoader();
//        progressDialog = Global.getProgressDialog(this, CCResource.getString(this, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_booking_history", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Boking Response",response);
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                Gson gson = new Gson();
                BookingHistory modelArrayList = gson.fromJson(response, BookingHistory.class);

                if(modelArrayList.getData().isEmpty()){
                    notfound.setVisibility(View.VISIBLE);
                    coachlist.setVisibility(View.GONE);
                }else{
                    notfound.setVisibility(View.GONE);
                    coachlist.setVisibility(View.VISIBLE);
                    coachlist.setAdapter(new BookingHistoryAdapter(mContext,modelArrayList.getData(), BookingActivity.this));
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                Global.msgDialog((Activity) mContext, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("bstatus", filterType);
                param.put("from_date", from_date);
                param.put("to_date", to_date);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void buttonClick(long timeDuration,String action,String channel_id,String booking_id,String userId,String coachId,String coachName) {
        if(action.equalsIgnoreCase("join")){
            Intent intent=new Intent(this, CallActivity.class);
            intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,channel_id);
            intent.putExtra("UserId",userId);
            intent.putExtra("CoachId",coachId);
            intent.putExtra("booking_id",booking_id);
            intent.putExtra("timeDuration",timeDuration);
            intent.putExtra("Name",coachName);
            startActivity(intent);
        }else {
            validateAction(booking_id,action);
        }
    }

    @Override
    public void viewDetails(BookingHistory.Datum data) {
        bookingId = data.getId();
        if (!data.getAvatar().isEmpty()){
            Glide.with(mActivity).load(data.getAvatar())
                    .into(ivProfileImage);
        }else{
            Glide.with(mActivity).load(R.drawable.placeholder_user)
                    .into(ivProfileImage);
        }
        tvCoachName.setText(Global.capitalizeFirstLatterOfString(data.getName()));

        if(data.getDevice_message()==null || data.getDevice_message().isEmpty()){
            tvInfo.setVisibility(View.GONE);
        }else{
            tvInfo.setVisibility(View.VISIBLE);
            tvInfo.setText(Html.fromHtml(data.getDevice_message()));
        }

        tvSessionDetails.setText("Booking ID: "+" "+data.getBookingId());
        tvSessionDateTime.setText(Global.convertUTCDateToLocalDate(data.getBookingSlotDate())+" , "+data.getBookingSlotTxt());
        tvBookingDate.setText(Global.convertUTCDateToLocalDatee(data.getBooking_date()));
        tvSessionAmount.setText("\u20B9"+data.getPayment_amount());
        tvBookingStataus.setText("Your booking has been "+data.getBtn1().toLowerCase()+"!");

        if((data.getBtn1().equalsIgnoreCase("Confirmed") &&data.getBtn2().equalsIgnoreCase(""))){

            if(Global.getCurrentDate().compareTo(Global.convertUTCDateToLocalDate(data.getBookingSlotDate()))>=0 && Global.getCurrentDateAndTime().compareTo(data.getOnlineSessionStartTime())>0 ){

                fl_cancel_booking.setVisibility(View.GONE);
                fl_cancelled.setVisibility(View.GONE);
                fl_join_session.setVisibility(View.GONE);
            }else{

                if(data.getCancel_enable().equalsIgnoreCase("1")){
                    fl_cancel_booking.setVisibility(View.VISIBLE);
                }else{
                    fl_cancel_booking.setVisibility(View.GONE);
                }

                fl_cancelled.setVisibility(View.GONE);
                fl_join_session.setVisibility(View.GONE);
            }

        }else if(data.getBtn1().equalsIgnoreCase("Cancelled")){
            fl_cancel_booking.setVisibility(View.GONE);
            fl_cancelled.setVisibility(View.VISIBLE);
            fl_join_session.setVisibility(View.GONE);
        }else if(data.getBtn2().equalsIgnoreCase("Join") && data.getBtn1().equalsIgnoreCase("Confirmed")){
            fl_cancel_booking.setVisibility(View.GONE);
            fl_cancelled.setVisibility(View.GONE);
            if(data.getChanel_id().equalsIgnoreCase("")){
                fl_join_session.setVisibility(View.GONE);
            }else{
                fl_join_session.setVisibility(View.VISIBLE);
            }

        }

        fl_cancel_booking.setOnClickListener(v -> {
            slideUp.hide();
            cancelAlertDialog();
        });
        fl_join_session.setOnClickListener(v -> {
            Intent intent=new Intent(this, CallActivity.class);
            intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,data.getChanel_id());
            intent.putExtra("UserId",data.getUserId());
            intent.putExtra("CoachId",data.getCoachUserId());
            intent.putExtra("booking_id",data.getBookingId());
            intent.putExtra("timeDuration",data.getDuration_in_milisecond());
            intent.putExtra("Name",data.getName());
            startActivity(intent);
            slideUp.hide();
        });

        slideUp.show();
    }

    private boolean isValidateSearch(){
        boolean isOk = true;
        if(from_date.equalsIgnoreCase("")){
            Toaster.customToast("Select From date!");
            isOk =false;
        }else if(to_date.equalsIgnoreCase("")){
            Toaster.customToast("Select To date!");
            isOk =false;
        }else if(filterType.equalsIgnoreCase("")){
            Toaster.customToast("Select Booking Status!");
            isOk =false;
        }

        return isOk;
    }

    private void validateAction(String booking_id,String action) {
        loaderView.showLoader();
//        progressDialog = Global.getProgressDialog(this, CCResource.getString(this, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "booking_action", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                Gson gson = new Gson();
                CoachAccept modelArrayList = gson.fromJson(response, CoachAccept.class);
                Toaster.customToast(modelArrayList.getData().getMessage() );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                Global.msgDialog((Activity) mContext, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("action", action);
                param.put("booking_id", booking_id);
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

    private void cancelAlertDialog() {
        final Dialog dialog = new Dialog(BookingActivity.this);
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
            startActivity(new Intent(mContext,CancellationFeedbackFormActivity.class).putExtra("BookingId",bookingId));
        });
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BookingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}