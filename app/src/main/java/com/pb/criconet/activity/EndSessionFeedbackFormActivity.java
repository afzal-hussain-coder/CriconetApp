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
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.EndSessionFeedbackAdapter;
import com.pb.criconet.adapters.FeedbackAdapter;
import com.pb.criconet.models.CoachList;
import com.pb.criconet.models.FeedBackFormChildData;
import com.pb.criconet.models.FeedbackModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class EndSessionFeedbackFormActivity extends AppCompatActivity {

    private EditText edit_type_feedback;
    private CheckBox ch_terams;
    private LinearLayout li_edit;
    private boolean isTermsPolicyChecked;
    private FrameLayout fl_submit_feedback;
    private Activity mActivity;
    CustomLoaderView loaderView;
    private RequestQueue queue;
    private SharedPreferences prefs;
    private String bookingId;
    private FeedbackModel feedbackModel;
    private ArrayList<FeedBackFormChildData> feedbackModelList=new ArrayList<>();
    private RecyclerView recycler_feedback;
    private EndSessionFeedbackAdapter feedbackAdapter;
    private Context mContext;
    private TextView tvCancelFormQuets,tv_last_position,tv_dear_coach;
    private String feedbackOption;
    private String feedbackText;
    private int lastCheckedRB = -1;
    TextView tv_terms,tv_policy;
    int a1,a2,a3,a4,a5,a6,a7;
    String suggestions="",id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_session_feedback_form);
        mActivity = this;
        mContext = this;
        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        if(getIntent().getExtras()!=null){
            Bundle args = getIntent().getBundleExtra("Certificate");
            feedbackModelList = (ArrayList<FeedBackFormChildData>) args.getSerializable("ARRAYLIST");
            id = getIntent().getExtras().getString("id");

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
        mTitle.setText(getResources().getString(R.string.e_Coaching_Session_Feedback));

        initializeView();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (Global.isOnline(mActivity)) {
//
//        } else {
//            Global.showDialog(mActivity);
//        }
    }

    private void initializeView() {
        tvCancelFormQuets = findViewById(R.id.tvCancelFormQuets);
        recycler_feedback=findViewById(R.id.recycler_feedback);
        recycler_feedback.setLayoutManager(new LinearLayoutManager(this));
        recycler_feedback.setHasFixedSize(true);
        tv_dear_coach = findViewById(R.id.tv_dear_coach);

        //Toaster.customToast(SessionManager.getProfileType(prefs));
        if(SessionManager.getProfileType(prefs).equalsIgnoreCase("coach")){
            tv_dear_coach.setText(getResources().getString(R.string.Dear_Coach));
        }else{
            tv_dear_coach.setText(getResources().getString(R.string.Dear_Students));
        }
        recycler_feedback.setAdapter(new EndSessionFeedbackAdapter(feedbackModelList, mContext, new EndSessionFeedbackAdapter.radioClickListenerr() {
            @Override
            public void radioClickk(int q1, int v1) {
                //Toaster.customToast(q1.size()+"/"+v1.size());
                for(int i=1;i<=feedbackModelList.size();i++){

                    if(SessionManager.getProfileType(prefs).equalsIgnoreCase("coach")){
                        if(q1==1){
                            a5=v1;
                        }else if(q1==2){
                            a6=v1;
                        }else if(q1==3){
                            a7=v1;
                        }
                    }else{
                        if(q1==1){
                            a1=v1;
                        }else if(q1==2){
                            a2=v1;
                        }else if(q1==3){
                            a3=v1;
                        }else if(q1==4){
                            a4=v1;
                        }
                    }


                }
            }
        }));
        li_edit = findViewById(R.id.li_edit);
        tv_last_position = findViewById(R.id.tv_last_position);

        if(feedbackModelList.size()==0){

        }else{
            tv_last_position.setText(feedbackModelList.size()+1+"."+getResources().getString(R.string.Do_you_have_any_suggestions));
        }

        edit_type_feedback = findViewById(R.id.edit_type_feedback);

        fl_submit_feedback = findViewById(R.id.fl_submit_feedback);
        fl_submit_feedback.setOnClickListener(v -> {
            feedbackText = edit_type_feedback.getText().toString().trim();

            if(SessionManager.getProfileType(prefs).equalsIgnoreCase("coach")){
                if (a5==0||a6==0||a7==0){
                    Toaster.customToastDownn(getResources().getString(R.string.Select_Box));
                } else if(feedbackText.isEmpty()){
                    Toaster.customToastDownn(getResources().getString(R.string.Write_suggestions));
                }
                else{
                    if (Global.isOnline(mActivity)) {
                        submitEndSessionFeedback();
                    } else {
                        Global.showDialog(mActivity);
                    }
                }
            }else{
                if (a1==0 || a2==0 || a3==0 || a4==0){
                    Toaster.customToastDownn(getResources().getString(R.string.Select_Box));
                } else if(feedbackText.isEmpty()){
                    Toaster.customToastDownn(getResources().getString(R.string.Write_suggestions));
                }
                else{
                    if (Global.isOnline(mActivity)) {
                        submitEndSessionFeedback();
                    } else {
                        Global.showDialog(mActivity);
                    }
                }
            }

        });
    }

    private void submitEndSessionFeedback() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.SUBMIT_END_SESSION_FEEDBACK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loaderView.hideLoader();
                Log.d("SubmitFeedback",response);

                try {
                    JSONObject jsonObject= new JSONObject(response);
                    if(jsonObject.getString("api_status").equalsIgnoreCase("200")) {
                        String message = jsonObject.getString("message");
                        sessionEndDialog(message);
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
                 Global.msgDialog((Activity) mActivity, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("commentText",feedbackText);
                param.put("booking_id",id);
                param.put("Q1", String.valueOf(a1));
                param.put("Q2", String.valueOf(a2));
                param.put("Q3", String.valueOf(a3));
                param.put("Q4", String.valueOf(a4));
                param.put("Q5", String.valueOf(a5));
                param.put("Q6", String.valueOf(a6));
                param.put("Q7", String.valueOf(a7));
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void sessionEndDialog(String message) {
        final Dialog dialog = new Dialog(EndSessionFeedbackFormActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.end_session_feedback_dialog);
        dialog.setCancelable(false);
        TextView tv_msg = dialog.findViewById(R.id.tv_msg);
        tv_msg.setText(message);
        FrameLayout fl_book_another_session=dialog.findViewById(R.id.fl_book_another_session);
        FrameLayout fl_navigate_home_page=dialog.findViewById(R.id.fl_navigate_home_page);

        if(SessionManager.getProfileType(prefs).equalsIgnoreCase("coach")){
            fl_book_another_session.setVisibility(View.GONE);
            fl_navigate_home_page.setVisibility(View.VISIBLE);
        }else{
            fl_book_another_session.setVisibility(View.VISIBLE);
            fl_navigate_home_page.setVisibility(View.VISIBLE);
        }

        fl_navigate_home_page.setOnClickListener(v -> {
            startActivity(new Intent(EndSessionFeedbackFormActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        });
        fl_book_another_session.setOnClickListener(v -> {

            startActivity(new Intent(EndSessionFeedbackFormActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("type","coachFreagment"));
            finish();
        });


        dialog.show();
    }

/*    private boolean isValidate(){
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
    }*/
}