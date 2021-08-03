package com.pb.criconet.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class ChangePassword extends AppCompatActivity {

    private SharedPreferences prefs;
    private RequestQueue queue;
    private String TAG = "ChangePassword";
    CustomLoaderView loaderView;

    private EditText edit_current_password_red_bg, edit_new_password_red_bg, edit_repeat_password_red_bg;
    private RelativeLayout rel_current_password_bg_red, rel_new_password_bg_red, rel_repeat_password_bg_red, rel_save;
    private TextView txt_user_profile_name, edttxt_current_password, edttxt_new_password, edttxt_repeat_password;
    private String current_password, new_password, repeat_password, user_firts_name = "", user_last_name = "";
    ImageButton ib_visible_cp, ib_visible_np, ib_visible_rp;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        activity =this;
        loaderView = CustomLoaderView.initialize(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChangePassword.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });
        TextView toolbartext = (TextView) toolbar.findViewById(R.id.toolbartext);
        toolbartext.setText(R.string.change_password);

        queue = Volley.newRequestQueue(ChangePassword.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(ChangePassword.this);
        user_firts_name = SessionManager.get_fname(prefs);
        user_last_name = SessionManager.get_lname(prefs);

        initializeView();
    }

    private void initializeView() {
        edttxt_current_password = findViewById(R.id.edttxt_current_password);
        edit_current_password_red_bg = findViewById(R.id.edit_current_password_red_bg);
        edttxt_new_password = findViewById(R.id.edttxt_new_password);
        edit_new_password_red_bg = findViewById(R.id.edit_new_password_red_bg);
        edttxt_repeat_password = findViewById(R.id.edttxt_repeat_password);
        edit_repeat_password_red_bg = findViewById(R.id.edit_repeat_password_red_bg);
        rel_current_password_bg_red = findViewById(R.id.rel_current_password_bg_red);
        rel_new_password_bg_red = findViewById(R.id.rel_new_password_bg_red);
        rel_repeat_password_bg_red = findViewById(R.id.rel_repeat_password_bg_red);

        ib_visible_cp = findViewById(R.id.ib_visible_cp);
        ib_visible_cp.setTag("InVisible");
        ib_visible_cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ib_visible_cp.getTag().equals("InVisible")) {
                    edit_current_password_red_bg.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ib_visible_cp.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_black_24dp));
                    ib_visible_cp.setTag("Visible");
                } else {
                    edit_current_password_red_bg.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ib_visible_cp.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_black_24dp));
                    ib_visible_cp.setTag("InVisible");
                }
            }
        });
        ib_visible_np = findViewById(R.id.ib_visible_np);
        ib_visible_np.setTag("InVisible");
        ib_visible_np.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ib_visible_np.getTag().equals("InVisible")) {
                    edit_new_password_red_bg.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ib_visible_np.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_black_24dp));
                    ib_visible_np.setTag("Visible");
                } else {
                    edit_new_password_red_bg.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ib_visible_np.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_black_24dp));
                    ib_visible_np.setTag("InVisible");
                }
            }
        });
        ib_visible_rp = findViewById(R.id.ib_visible_rp);
        ib_visible_rp.setTag("InVisible");
        ib_visible_rp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ib_visible_rp.getTag().equals("InVisible")) {
                    edit_repeat_password_red_bg.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ib_visible_rp.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_black_24dp));
                    ib_visible_rp.setTag("Visible");
                } else {
                    edit_repeat_password_red_bg.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ib_visible_rp.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_black_24dp));
                    ib_visible_rp.setTag("InVisible");
                }
            }
        });


        edttxt_current_password.setOnClickListener(v -> {
            edttxt_current_password.setVisibility(View.GONE);
            rel_current_password_bg_red.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            edit_current_password_red_bg.requestFocus();
            imm.showSoftInput(edit_current_password_red_bg, InputMethodManager.SHOW_FORCED);
        });
        edttxt_new_password.setOnClickListener(v -> {
            edttxt_new_password.setVisibility(View.GONE);
            rel_new_password_bg_red.setVisibility(View.VISIBLE);
            edit_new_password_red_bg.requestFocus();
        });

        edttxt_repeat_password.setOnClickListener(v -> {
            edttxt_repeat_password.setVisibility(View.GONE);
            rel_repeat_password_bg_red.setVisibility(View.VISIBLE);
            edit_repeat_password_red_bg.requestFocus();
        });

        edit_current_password_red_bg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edttxt_new_password.setVisibility(View.GONE);
                    rel_new_password_bg_red.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        edit_new_password_red_bg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edttxt_repeat_password.setVisibility(View.GONE);
                    rel_repeat_password_bg_red.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        txt_user_profile_name = findViewById(R.id.txt_user_profile_name);
        if (user_firts_name.isEmpty() || user_last_name.isEmpty()) {

        } else {
            txt_user_profile_name.setText("( " + capitizeString(user_firts_name) + " " + capitizeString(user_last_name) + " )");
        }

        rel_save = findViewById(R.id.rel_save);

        rel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMethod();
            }
        });
    }

    private String capitizeString(String name) {
        String captilizedString = "";
        if (!name.trim().equals("")) {
            captilizedString = name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return captilizedString;
    }

    public void changeMethod() {
        current_password = edit_current_password_red_bg.getText().toString().trim();
        new_password = edit_new_password_red_bg.getText().toString().trim();
        repeat_password = edit_repeat_password_red_bg.getText().toString().trim();
        if (current_password.length() < 1) {
            Toaster.customToast("Enter Current Password");
        } else if (current_password.length() < 6) {
            Toaster.customToast(getResources().getString(R.string.password_too_short));
        } else if (new_password.length() < 1) {
            Toaster.customToast("Enter New Password");
        } else if (new_password.length() < 6) {
            Toaster.customToast(getResources().getString(R.string.password_too_short));
        } else if (repeat_password.length() < 1) {
            Toaster.customToast("Enter Repeat Password");
        } else if (repeat_password.length() < 6) {
            Toaster.customToast(getResources().getString(R.string.password_too_short));
        } else if (!new_password.equals(repeat_password)) {
            Toaster.customToast("Repeat password must be same as new password");
        } else {
            if (Global.isOnline(ChangePassword.this)) {
                changePassword();
            } else {
                Global.showDialog(ChangePassword.this);
            }
        }


    }

    private void changePassword() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "update_user_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e(response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                SessionManager.save_password(prefs, edit_new_password_red_bg.getText().toString().trim());
                                Global.msgDialog(ChangePassword.this, "Password Changed Successfully");
                                edit_current_password_red_bg.setText("");
                                edit_new_password_red_bg.setText("");
                                edit_repeat_password_red_bg.setText("");
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Toaster.customToast(jsonObject.optString("message"));
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
//                Global.msgDialog(ChangePassword.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                type:  change_password
//                current_password:
//                new_password:"

                param.put("type", "change_password");
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("current_password", current_password);
                param.put("new_password", new_password);
                param.put("s", SessionManager.get_session_id(prefs));

                Timber.e("%s", param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);


    }


    public void FireBasechangePassword() {
        loaderView.showLoader();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(SessionManager.get_emailid(prefs), edit_current_password_red_bg.getText().toString().trim());
// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loaderView.hideLoader();
                            user.updatePassword(edit_new_password_red_bg.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Password updated Fire Base");
                                        Global.msgDialog(ChangePassword.this, "Password Changed Sucessfully");
                                        edit_current_password_red_bg.setText("");
                                        edit_new_password_red_bg.setText("");
                                        edit_repeat_password_red_bg.setText("");

                                    } else {
                                        Global.msgDialog(ChangePassword.this, "Please check your password");
                                        Log.d(TAG, "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            loaderView.hideLoader();
                            Global.msgDialog(ChangePassword.this, "Change Password Failed");
                            Log.d(TAG, "Error auth failed");
                        }
                    }
                });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Global.hideSoftKeyboard(activity);
        /*edttxt_current_password.setVisibility(View.VISIBLE);
        rel_current_password_bg_red.setVisibility(View.GONE);

        edttxt_new_password.setVisibility(View.VISIBLE);
        rel_new_password_bg_red.setVisibility(View.GONE);

        edttxt_repeat_password.setVisibility(View.VISIBLE);
        rel_repeat_password_bg_red.setVisibility(View.GONE);*/
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChangePassword.this, Settings.class);
        startActivity(intent);
        finish();
    }
}
