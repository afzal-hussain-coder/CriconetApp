package com.pb.criconet.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;


public class Settings extends AppCompatActivity {
    private SharedPreferences prefs;
    private RelativeLayout delete_acc, privacy_setting, deactivate_acc, feed, rate, recomend;
    private ProgressDialog progress;
    private RequestQueue queue;
    private RelativeLayout rl_record_video,about, privacy,user_aggrement, terms, agreement, help, rl_blockuser, rl_edit_profile, rl_chnage_password;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_user);
        mActivity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        queue = Volley.newRequestQueue(Settings.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(Settings.this);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(getResources().getString(R.string.setting).toUpperCase());

        progress = new ProgressDialog(Settings.this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        privacy_setting = (RelativeLayout) findViewById(R.id.privacy_setting);
        deactivate_acc = (RelativeLayout) findViewById(R.id.deactivate_acc);
        delete_acc = (RelativeLayout) findViewById(R.id.delete_acc);
        feed = (RelativeLayout) findViewById(R.id.feed);

        rl_blockuser = findViewById(R.id.rl_blockuser);
        rl_blockuser.setOnClickListener(v -> {startActivity(new Intent(mActivity, BlockedUsers.class));});
        rl_edit_profile = findViewById(R.id.rl_edit_profile);
        rl_edit_profile.setOnClickListener(v -> {
            startActivity(new Intent(mActivity, EditProfile.class).putExtra("FROM","2"));
            finish();
        });
        rl_chnage_password = findViewById(R.id.rl_chnage_password);
        rl_chnage_password.setOnClickListener(v -> {
            startActivity(new Intent(mActivity, ChangePassword.class));
            finish();
        });
        user_aggrement = findViewById(R.id.user_aggrement);

        /////////////////////////////////////////////////////////
        rate = (RelativeLayout) findViewById(R.id.rate);
        recomend = (RelativeLayout) findViewById(R.id.recomend);
        about = (RelativeLayout) findViewById(R.id.about);
        privacy = (RelativeLayout) findViewById(R.id.privacy);
        terms = (RelativeLayout) findViewById(R.id.terms);
        agreement = (RelativeLayout) findViewById(R.id.agreement);
        help = (RelativeLayout) findViewById(R.id.help);

//        privacy_setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().beginTransaction().replace(R.id.frame_container, new PivacySetting())
//                        .addToBackStack(null).commit();
//            }
//        });

        deactivate_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(Settings.this);
                alertbox.setTitle(Settings.this.getResources().getString(R.string.app_name));
                alertbox.setMessage(R.string.confirm_deactivate);
                alertbox.setPositiveButton(R.string.deactivate,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                deactivateAccount();
                            }
                        });
                alertbox.setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                alertbox.show();
            }
        });

        delete_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(Settings.this);
                alertbox.setTitle(Settings.this.getResources().getString(R.string.app_name));
                alertbox.setMessage(R.string.confirm_delete);
                alertbox.setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteAccount();
                            }
                        });
                alertbox.setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                alertbox.show();
            }
        });

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, "info@bookmebarber.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi, ");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + Settings.this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + Settings.this.getPackageName())));
                }
            }
        });
        recomend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                    String sAux = "\nI recommend this great App, download now and we can connect.\n\n";
                    sAux = sAux + "Android : https://play.google.com/store/apps/details?id=" + Settings.this.getPackageName();
//                    sAux = sAux + " \n\n IOS : https://itunes.apple.com/us/app/criconet/id1447634287?ls=1&mt=8";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Select one"));
                } catch (Exception e) {
                    //e.toString();
                }

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //"title": "About Us",
                        //"url": "https:\/\/www.criconet.com\/terms\/about-us?rst=app"

                String url ="https://www.criconet.com/terms/about-us?rst=app";
                startActivity(new Intent(mActivity, SettingWebView.class).putExtra("URL", url).putExtra("title","About Us"));
//                Intent intent = new Intent(Settings.this, About.class);
//                intent.putExtra("status", "about");
//                startActivity(intent);
//                finish();
            }
        });
//
//        "title": "User Agreement",
//                "url": "https:\/\/www.criconet.com\/user-agreement?rst=app"

        user_aggrement.setOnClickListener(v -> {
            String url ="https:\\/\\/www.criconet.com\\/user-agreement?rst=app";
            startActivity(new Intent(mActivity, SettingWebView.class).putExtra("URL", url).putExtra("title","User Agreement"));
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url ="https:\\/\\/www.criconet.com\\/terms\\/privacy-policy?rst=app";
                startActivity(new Intent(mActivity, SettingWebView.class).putExtra("URL", url).putExtra("title","Privacy Policy"));

//                Intent intent = new Intent(Settings.this, About.class);
//                intent.putExtra("status", "privacy_policy");
//                startActivity(intent);
//                finish();
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url ="https:\\/\\/www.criconet.com\\/terms\\/terms?rst=app";
                startActivity(new Intent(mActivity, SettingWebView.class).putExtra("URL", url).putExtra("title","Terms"));

//                Intent intent = new Intent(Settings.this, About.class);
//                intent.putExtra("status", "teams");
//                startActivity(intent);
//                finish();
            }
        });
        agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, About.class);
                intent.putExtra("status", "privacy_policy");
                startActivity(intent);
                finish();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, About.class);
                intent.putExtra("status", "help");
                startActivity(intent);
                finish();
//                Intent intent = new Intent(Settings.this, About.class);
//                startActivity(intent);
            }
        });


    }


    public void deleteAccount() {
        final JSONObject json = new JSONObject();
        try {
// action:profiledelete,userId:113
            json.put("action", "profiledelete");
            json.put("userId", SessionManager.get_user_id(prefs));
            Log.e("deleteGallery : ", " data :  " + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Global.URL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("deleteGallery res : ", "" + response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = response;
                            if (jsonObject.getString("status").equals("Success")) {
                                SessionManager.dataclear(prefs);
                                Intent intent = new Intent(Settings.this, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                Settings.this.finish();
                            } else if (jsonObject.getString("status").equals("Fail")) {
                                Global.msgDialog(Settings.this, jsonObject.getString("msg"));
                            } else {
                                Global.msgDialog(Settings.this, "Error in server");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progress.dismiss();
                Global.msgDialog(Settings.this, "Internet connection is slow");
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }

    public void deactivateAccount() {
        final JSONObject json = new JSONObject();
        try {
// // action:profiledeactivated,userId:113
            json.put("action", "profiledeactivated");
            json.put("userId", SessionManager.get_user_id(prefs));
            Log.e("deleteGallery : ", " data :  " + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Global.URL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("deleteGallery res : ", "" + response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = response;
                            if (jsonObject.getString("status").equals("Success")) {
                                SessionManager.dataclear(prefs);
                                Intent intent = new Intent(Settings.this, Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                Settings.this.finish();
                            } else if (jsonObject.getString("status").equals("Fail")) {
                                Global.msgDialog(Settings.this, jsonObject.getString("msg"));
                            } else {
                                Global.msgDialog(Settings.this, "Error in server");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progress.dismiss();
                Global.msgDialog(Settings.this, "Internet connection is slow");
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
