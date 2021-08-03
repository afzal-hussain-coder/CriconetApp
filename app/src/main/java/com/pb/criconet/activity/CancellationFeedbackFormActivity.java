package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.FeedbackAdapter;
import com.pb.criconet.models.FeedbackModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class CancellationFeedbackFormActivity extends AppCompatActivity {

    private EditText edit_type_feedback;
    private CheckBox ch_terams;
    private LinearLayout li_edit;
    private boolean isTermsPolicyChecked;
    private FrameLayout fl_submit_feedback;
    private Activity activity;
    CustomLoaderView loaderView;
    private RequestQueue queue;
    private SharedPreferences prefs;
    private String bookingId;
    private FeedbackModel feedbackModel;
    private ArrayList<FeedbackModel> feedbackModelList=new ArrayList<>();
    private RecyclerView recycler_feedback;
    private FeedbackAdapter feedbackAdapter;
    private Context mContext;
    private TextView tvCancelFormQuets;
    private String feedbackOption;
    private String feedbackText;
    private int lastCheckedRB = -1;
    TextView tv_terms,tv_policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelltion_feedback_form);
        activity = this;
        mContext = this;
        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        if(getIntent().getExtras()!=null){
            bookingId = getIntent().getExtras().getString("BookingId");
        }

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
        mTitle.setText("Cancellation Feedback");

        initializeView();
        getCancelBookingFeedbackForm();
    }

    private void initializeView() {
        tvCancelFormQuets = findViewById(R.id.tvCancelFormQuets);
        recycler_feedback=findViewById(R.id.recycler_feedback);
        recycler_feedback.setHasFixedSize(true);
        recycler_feedback.setLayoutManager(new LinearLayoutManager(this));
        li_edit = findViewById(R.id.li_edit);
        edit_type_feedback = findViewById(R.id.edit_type_feedback);
        ch_terams = findViewById(R.id.ch_terams);
        ch_terams.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isTermsPolicyChecked = isChecked;
        });
        tv_terms = findViewById(R.id.tv_terms);
        tv_terms.setOnClickListener(v -> {
            startActivity(new Intent(activity, CancelletionWebView.class).putExtra("URL","https://www.criconet.com/terms/terms?rst=app").putExtra("title", "Terms"));
            finish();
        });
        tv_policy= findViewById(R.id.tv_policy);
        tv_policy.setOnClickListener(v -> {

            startActivity(new Intent(activity, CancelletionWebView.class).putExtra("URL","https://www.criconet.com/terms/terms?rst=app").putExtra("title", "Terms"));
            finish();
        });


        fl_submit_feedback = findViewById(R.id.fl_submit_feedback);
        fl_submit_feedback.setOnClickListener(v -> {
            feedbackText = edit_type_feedback.getText().toString().trim();
            if (isValidate()){
                getBookingCancelled();
            }
        });
    }
    private void getCancelBookingFeedbackForm() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_FEEDBACK_FORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loaderView.hideLoader();
                Log.d("Feedback Form Response",response);
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                        String message = jsonObject.getString("message");
                        tvCancelFormQuets.setText(message);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i= 0;i<jsonArray.length();i++){
                            feedbackModel = new FeedbackModel(jsonArray.getJSONObject(i));
                            feedbackModelList.add(feedbackModel);
                        }

                        feedbackAdapter = new FeedbackAdapter(feedbackModelList, mContext, new FeedbackAdapter.radioClickListener() {
                            @Override
                            public void radioClick(String message, int pos) {
                                feedbackOption = message;
                                lastCheckedRB =  pos;
                                if (pos==feedbackModelList.size()-1){
                                    li_edit.setVisibility(View.VISIBLE);
                                    Global.showKeyboard(activity);
                                }else{
                                    li_edit.setVisibility(View.GONE);
                                }
                            }
                        });
                        recycler_feedback.setAdapter(feedbackAdapter);

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
                Toaster.customToast(getString(R.string.socket_timeout));
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
    private void getBookingCancelled() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.CANCEL_BOOKING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loaderView.hideLoader();
                Log.d("Boking Response",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                        cancelAlertDialog(jsonObject.getString("message"));
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
                Toaster.customToast(getString(R.string.socket_timeout));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("question_id", feedbackOption);
                param.put("feedbabk_teext", feedbackText);
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

    private boolean isValidate(){
        boolean isOk = true;
        if(lastCheckedRB==-1){
            Toaster.customToastDown("Please choose option!");
            isOk = false;
        }else if(lastCheckedRB == feedbackModelList.size()-1){
            if (edit_type_feedback.getText().toString().trim().isEmpty()) {
                Toaster.customToastDown("Please type your feedback!");
                isOk = false;
            }else if(!isTermsPolicyChecked){
                Toaster.customToastDown("Please check to accept the Cancellation Policy");
                isOk = false;
            }
        }else if(!isTermsPolicyChecked){
             Toaster.customToastDown("Please check to accept the cancellation policy");
             isOk = false;
        }
        return isOk;
    }

    private void cancelAlertDialog(String message) {
        final Dialog dialog = new Dialog(CancellationFeedbackFormActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_cancel_confirmation);
        dialog.setCancelable(false);
        TextView tv_cancel_confirmed_msg = dialog.findViewById(R.id.tv_cancel_confirmed_msg);
        tv_cancel_confirmed_msg.setText(message);
        FrameLayout fl_no = dialog.findViewById(R.id.fl_done);
        fl_no.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(CancellationFeedbackFormActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }


}