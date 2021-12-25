package com.pb.criconet.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


/**
 * Created by Pradeep on 9/6/2016.
 */
public class ForgotPassword extends AppCompatActivity {

    SharedPreferences prefs;
    TextView click_signup, forgot_password;
    TextInputLayout til_edttxt_email;
    Button lin_lay_recover;
    EditText edttxt_email;
    String email_String;
    RequestQueue queue;
    //    LinearLayout linear_fb, linear_gplus;
    ProgressDialog progress_dialog;
    boolean isGPSEnabled = false;
    // Afzal code here.

    EditText  edit_forgot_email_redd;
    RelativeLayout rel_email_bg_redd, rel_ok;
    LinearLayout lin_lay_recoverr;
    TextView txt_sucess,edit_email_forgott;
    MaterialCardView rel_message,rel_input;
    CustomLoaderView loaderView;
    private Activity activity;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        activity =this;
        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(ForgotPassword.this);
        queue = Volley.newRequestQueue(ForgotPassword.this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
        TextView toolbartext = (TextView) toolbar.findViewById(R.id.toolbartext);
        toolbartext.setText(R.string.forgot_password_hd);



        initializeView();


    }

    private void initializeView() {

        lin_lay_recoverr = (LinearLayout) findViewById(R.id.lin_lay_recover);
        edit_email_forgott = findViewById(R.id.edit_email_forgot);
        rel_email_bg_redd = findViewById(R.id.rel_email_bg_red);
        edit_forgot_email_redd = findViewById(R.id.edit_forgot_email_red);
        edit_forgot_email_redd.requestFocus();
        rel_message = findViewById(R.id.rel_message);
        rel_ok = findViewById(R.id.rel_ok);
        txt_sucess=findViewById(R.id.txt_sucess);
        rel_input=findViewById(R.id.rel_input);
        edit_email_forgott.setVisibility(View.GONE);
        rel_email_bg_redd.setVisibility(View.VISIBLE);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        rel_email_bg_redd.requestFocus();
        imm.showSoftInput(rel_email_bg_redd, InputMethodManager.SHOW_FORCED);

//        edit_email_forgott.setOnClickListener(v -> {
//            edit_email_forgott.setVisibility(View.GONE);
//            rel_email_bg_redd.setVisibility(View.VISIBLE);
//            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//            rel_email_bg_redd.requestFocus();
//            imm.showSoftInput(rel_email_bg_redd, InputMethodManager.SHOW_FORCED);
//        });


        lin_lay_recoverr.setOnClickListener(v -> {
            email_String = edit_forgot_email_redd.getText().toString().trim();


            if (email_String.length()<1) {
                Toaster.customToast(getResources().getString(R.string.Email_Cant_be_empty));
            } else if (!Global.ValidEmail(email_String)) {
                Toaster.customToast(getResources().getString(R.string.Invalid_email_format));
            } else {
                if (Global.isOnline(ForgotPassword.this)) {
                    forgot_task(email_String);
                } else {
                    Toaster.customToast(getResources().getString(R.string.no_internet));
                }
            }
        });

        rel_ok.setOnClickListener(v -> {
            rel_message.setVisibility(View.GONE);
            rel_input.setVisibility(View.VISIBLE);
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Global.hideSoftKeyboard(activity);
        return true;
    }

    public void forgot_task(final String email_String) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "forget_password",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e("%s", response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                rel_input.setVisibility(View.GONE);
                                rel_message.setVisibility(View.VISIBLE);
                                txt_sucess.setText(jsonObject.optString("message"));
                                edit_forgot_email_redd.setText("");

                                //Global.msgDialog(ForgotPassword.this, jsonObject.optString("message"));

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                rel_input.setVisibility(View.GONE);
                                rel_message.setVisibility(View.VISIBLE);
                                txt_sucess.setText(jsonObject.optString("message"));
                                //Global.msgDialog(ForgotPassword.this, jsonObject.optString("message"));
                            } else {
                                Toaster.customToast(getResources().getString(R.string.socket_timeout));
                                //Global.msgDialog(ForgotPassword.this, "Error in server");
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
//                Global.msgDialog(Login.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("email", email_String);
//                param.put("device_id", SessionManager.get_devicetoken(prefs));
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


}