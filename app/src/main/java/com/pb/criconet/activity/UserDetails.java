package com.pb.criconet.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.HomeAdapter;
import com.pb.criconet.models.NewPostModel;
import com.pb.criconet.models.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class UserDetails extends AppCompatActivity implements PostListeners {
    private View rootView;
    private SharedPreferences prefs;
    private ProgressDialog progress;
    private RequestQueue queue;
    private String user_id, friendStatus;
    private TextView mTitle;
    private RoundedImageView ep_user_image;

    private TextView tv_name, status, accept, reject;
    private RelativeLayout acc_rej;
    //    private UserModel user;
    private UserData userData = new UserData();
    private ImageView cover;

    private AAH_CustomRecyclerView post_list;
    public ArrayList<NewPostModel> modelArrayList;
    private HomeAdapter adapter;
    private String after_post_id = "0";
    LinearLayoutManager mLayoutManager;
    ArrayList<String> images;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private TextView notfound, tvNumFollowers, tvNumFollowing;
    String from_where = "", feed_id = "";
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);

        prefs = PreferenceManager.getDefaultSharedPreferences(UserDetails.this);
        queue = Volley.newRequestQueue(UserDetails.this);
        progress = new ProgressDialog(UserDetails.this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user_id = bundle.getString("user_id");
            from_where = bundle.getString("FROM");
            feed_id = bundle.getString("feed_id");
            //Toaster.customToast(user_id);
            friendStatus = bundle.getString("friendStatus");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from_where.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(UserDetails.this, FeedDetails.class);
                    intent.putExtra("feed_id", feed_id);
                    startActivity(intent);
                    finish();
                } else if (from_where.equalsIgnoreCase("3")) {

                    Intent intent = new Intent(UserDetails.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else if(from_where.equalsIgnoreCase("4")){

                    Intent intent = new Intent(UserDetails.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(UserDetails.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        if (!user_id.equals(SessionManager.get_user_id(prefs))) {
//            HasOptionsMenu(true);
        }
        post_list = (AAH_CustomRecyclerView) findViewById(R.id.post_list);
        ep_user_image = (RoundedImageView) findViewById(R.id.ep_user_image);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tvNumFollowers = (TextView) findViewById(R.id.tvNumFollowers);
        tvNumFollowing = (TextView) findViewById(R.id.tvNumFollowing);
        status = (TextView) findViewById(R.id.status);
        acc_rej = (RelativeLayout) findViewById(R.id.acc_rej);
        accept = (TextView) findViewById(R.id.accept);
        reject = (TextView) findViewById(R.id.reject);
        cover = (ImageView) findViewById(R.id.rlt);
        acc_rej.setVisibility(View.GONE);
        notfound = (TextView) findViewById(R.id.notfound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ep_user_image.setClipToOutline(true);
        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptFriendRequest(user_id);
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unFriend(user_id);
            }
        });

        if (Global.isOnline(UserDetails.this)) {

            ResetFeed();
            System.out.println("xxxxxxxxxxxxxxxx UsersDetails xxxxxxxxxxxxxxx");
        } else {
            Global.showDialog(UserDetails.this);
        }


        ep_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewDialog(userData.getAvatar());
            }
        });

//        post_list.addOnScrollListener(new RecycleViewPaginationScrollListener(mLayoutManager) {
//            @Override
//            protected void loadMoreItems() {
//                isLoading = true;
////                    page++;
//                after_post_id = modelArrayList.get(modelArrayList.size() - 1).getId();
//                getFeed();
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return isLastPage;
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//
//        });

    }

    private void ResetFeed() {
        after_post_id = "0";
        isLoading = false;
        isLastPage = false;
//        up_image.setVisibility(View.GONE);
//        link_layout.setVisibility(View.GONE);

        modelArrayList = new ArrayList<>();
        adapter = new HomeAdapter(UserDetails.this, modelArrayList, this);
        mLayoutManager = new LinearLayoutManager(UserDetails.this);
        post_list.setLayoutManager(mLayoutManager);
        post_list.setItemAnimator(new DefaultItemAnimator());

        post_list.setActivity(UserDetails.this); //todo before setAdapter
        //optional - to play only first visible video
        post_list.setPlayOnlyFirstVideo(true); // false by default
        //optional - by default we check if url ends with ".mp4". If your urls do not end with mp4, you can set this param to false and implement your own check to see if video points to url
//        post_list.setCheckForMp4(false); //true by default

        //optional - download videos to local storage (requires "android.permission.WRITE_EXTERNAL_STORAGE" in manifest or ask in runtime)
//        post_list.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo"); // (Environment.getExternalStorageDirectory() + "/Video") by default
//        post_list.setDownloadVideos(true); // false by default
        post_list.setVisiblePercent(90); // percentage of View that needs to be visible to start playing

        post_list.setAdapter(adapter);
        //call this functions when u want to start autoplay on loading async lists (eg firebase)
//        post_list.smoothScrollBy(0,1);
//        post_list.smoothScrollBy(0,-1);

        if (Global.isOnline(UserDetails.this)) {
            getUsersDetails(user_id);
            getFeed();
            System.out.println("xxxxxxxxxx getFeed " + after_post_id + "xxxxxxxxxx");
        } else {
            Global.showDialog(UserDetails.this);
        }
    }

    public void getUsersDetails(final String user_id) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_user_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Timber.v(response);

                        Log.d("UserDetailsResponse", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                JSONObject object = jsonObject.getJSONObject("user_data");
                                userData = UserData.fromJson(object);
                                setData(userData);

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(UserDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(UserDetails.this, "Error in server");
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
                        progress.dismiss();
                        Global.msgDialog(UserDetails.this, "Error from server");
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("user_profile_id", user_id);
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    private void setData(final UserData userData) {

        mTitle.setText(userData.getName());
        tv_name.setText(userData.getName());
        tvNumFollowers.setText(userData.getCount_followers());
        tvNumFollowing.setText(userData.getCount_following());
        Glide.with(UserDetails.this).load(userData.getAvatar()).into(ep_user_image);
        Glide.with(UserDetails.this).load(userData.getCover()).into(cover);

        if (userData.getUser_id().equalsIgnoreCase(SessionManager.get_user_id(prefs))) {
            status.setVisibility(View.GONE);
        }
        if (userData.getIs_following() == 1) {
            status.setText("UnFollow");
        } else if (userData.getIs_following() == 2) {
            status.setText("Requested");
        } else {
            status.setText("Follow");
        }
//        }

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userData.getIs_following() == 1) {
                    FollowUser();
                } else {
                    FollowUser();
                }
//                }
            }
        });


    }

    private void getFeed() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "home_posts", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        JSONArray array = jsonObject.getJSONArray("posts");
                        if (array.length() < 1) {
                            isLastPage = true;
                        }
                        modelArrayList.addAll(NewPostModel.fromJson(array));
                        isLoading = false;
                        adapter.notifyDataSetChanged();

                        if (after_post_id.equals("0")) {
                            post_list.smoothScrollBy(0, 1);
                            post_list.smoothScrollBy(0, -1);
                            if (modelArrayList.size() == 0) {
                                notfound.setVisibility(View.VISIBLE);
                            } else {
                                notfound.setVisibility(View.GONE);
                            }
                        }

                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(UserDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(UserDetails.this, "Error in server");
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
                Global.msgDialog(UserDetails.this, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("publisher_id", user_id);
                param.put("limit", "20");
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

    public void imageViewDialog(String url) {
        final Dialog dialog = new Dialog(UserDetails.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.profiledialog);
        dialog.setCancelable(true);
        PhotoView img = (PhotoView) dialog.findViewById(R.id.image_view);
        ImageView del = (ImageView) dialog.findViewById(R.id.del);
        Glide.with(UserDetails.this)
                .load(url)
//                .asBitmap()
                .into(img);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (!user_id.equalsIgnoreCase(SessionManager.get_user_id(prefs)))
            inflater.inflate(R.menu.block_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!user_id.equalsIgnoreCase(SessionManager.get_user_id(prefs))) {
            MenuItem block = menu.findItem(R.id.menu_block);
            MenuItem unblock = menu.findItem(R.id.menu_unblock);

            if (userData.getIs_blocked()) {
                block.setVisible(false);
                unblock.setVisible(true);
            } else {
                unblock.setVisible(false);
                block.setVisible(true);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_report) {

        }

        switch (item.getItemId()) {
            case R.id.menu_block:
                BlockUser();
                break;
            case R.id.menu_unblock:
                UnBlockUser();
                break;
            case R.id.menu_report:
                ReportDialog();
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);

    }

    public void BlockUser() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "block_user",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e("response:  %s", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                                //finish();
                                msgDialogg(UserDetails.this, jsonObject.optString("blocked"));
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(UserDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(UserDetails.this, "Error in server");
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
                        progress.dismiss();
                        Global.msgDialog(UserDetails.this, "Error from server");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("recipient_id", user_id);
                param.put("block_type", "block");

                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void UnBlockUser() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "block_user",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e("response:  %s", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                ResetFeed();
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(UserDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(UserDetails.this, "Error in server");
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
                        progress.dismiss();
                        Global.msgDialog(UserDetails.this, "Error from server");
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("recipient_id", user_id);
                param.put("block_type", "un-block");

                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void ReportDialog() {


        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_with_edittext);
        final EditText input = (EditText) dialog.findViewById(R.id.editxt);
        TextView ok = dialog.findViewById(R.id.ok);
//        TextView cancel = dialog.findViewById(R.id.cancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.validateLength(input.getText().toString(), 5)) {
                    input.setError("Enter your reason to report this post?");
                } else {
                    input.setError(null);
                    ReportUser(input.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    public void ReportUser(String msg) {
        final JSONObject json = new JSONObject();
        try {
            json.put("action", "reportuser");
            json.put("user_id", SessionManager.get_user_id(prefs));
            json.put("profile_id", user_id);
            json.put("message", msg);
            Log.e("delete feeds : ", " data :  " + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Global.URL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("delete response : ", "" + response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = response;
                            if (jsonObject.getString("status").equals("Success")) {
//                                message
                                Toast.makeText(UserDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                UserDetails.this.onBackPressed();
                            } else if (jsonObject.getString("status").equals("Fail")) {
                                Global.msgDialog(UserDetails.this, jsonObject.getString("msg"));
                            } else {
                                Global.msgDialog(UserDetails.this, "Error in server");
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
                Global.msgDialog(UserDetails.this, "Internet connection is slow");
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }

    public void sendFriendRequest(String user_id) {
        final JSONObject json = new JSONObject();
        try {
            json.put("action", "sendfriendrequest");
            json.put("user_id", SessionManager.get_user_id(prefs));
            json.put("friend_id", user_id);
            Timber.e(" data : %s", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Global.URL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.tag("FriendRequest res : ").v("%s", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.getString("status").equals("Success")) {
//                                Global.msgDialog(UserDetails.this, jsonObject.getString("Request Sent"));
                                Toast.makeText(UserDetails.this, "Request Sent", Toast.LENGTH_SHORT).show();
                                friendStatus = "1";
                                status.setText("Request Pending");
                                status.setVisibility(View.VISIBLE);
                                acc_rej.setVisibility(View.GONE);

                            } else if (jsonObject.getString("status").equals("Fail")) {
                                Global.msgDialog(UserDetails.this, jsonObject.getString("msg"));
                            } else {
                                Global.msgDialog(UserDetails.this, "Error in server");
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
                Global.msgDialog(UserDetails.this, "Error from server");
//                Global.msgDialog(UserDetails.this, "Internet connection is slow");
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }

    public void unFriend(String user_id) {
        final JSONObject json = new JSONObject();
        try {
            json.put("action", "unfriend");
            json.put("user_id", SessionManager.get_user_id(prefs));
            json.put("friend_id", user_id);
            Timber.e(" data : %s", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Global.URL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.tag("unFriend res : ").v("%s", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.getString("status").equals("Success")) {
                                Global.msgDialog(UserDetails.this, jsonObject.getString("msg"));
                                friendStatus = "0";
                                status.setText("+ Send Friend Request");
                                status.setVisibility(View.VISIBLE);
                                acc_rej.setVisibility(View.GONE);

                            } else if (jsonObject.getString("status").equals("Fail")) {
                                Global.msgDialog(UserDetails.this, jsonObject.getString("msg"));
                            } else {
                                Global.msgDialog(UserDetails.this, "Error in server");
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
                Global.msgDialog(UserDetails.this, "Error from server");
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }

    public void acceptFriendRequest(String user_id) {
        final JSONObject json = new JSONObject();
        try {
            json.put("action", "accecptFriendReq");
            json.put("user_id", SessionManager.get_user_id(prefs));
            json.put("friend_id", user_id);
            Timber.e(" data : %s", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Global.URL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.tag("acceptFriendReq res : ").v("%s", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.getString("status").equals("Success")) {
                                Global.msgDialog(UserDetails.this, jsonObject.getString("msg"));
                                friendStatus = "3";
                                status.setText("UnFriend");
                                status.setVisibility(View.VISIBLE);
                                acc_rej.setVisibility(View.GONE);

                            } else if (jsonObject.getString("status").equals("Fail")) {
                                Global.msgDialog(UserDetails.this, jsonObject.getString("msg"));
                            } else {
                                Global.msgDialog(UserDetails.this, "Error in server");
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
                Global.msgDialog(UserDetails.this, "Error from server");
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }

    public void FollowUser() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "follow_user", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        getUsersDetails(user_id);
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(UserDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(UserDetails.this, "Error in server");
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
                Global.msgDialog(UserDetails.this, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("recipient_id", user_id);
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

    public void DeleteFeed(final String id) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "delete_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(UserDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(UserDetails.this, "Error in server");
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
                Global.msgDialog(UserDetails.this, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                "user_id:1735
//                s:1
//                post_id:8754"
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
//                param.put("s", "1");
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

    private void likeFeed(final String id) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "like_on_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
//                        ResetFeed();
                        //msgDialogg();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(UserDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(UserDetails.this, "Error in server");
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
                Global.msgDialog(UserDetails.this, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
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

    public void msgDialogg(Activity ac, String msg) {
        try {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(ac);
            alertbox.setTitle(ac.getResources().getString(R.string.app_name));
            alertbox.setMessage(Html.fromHtml(msg));
            alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            ResetFeed();
                        }
                    });
            alertbox.show();
        } catch (Exception e) {
        }
    }

    private void dislikeFeed(final String id) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "unlike_on_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(UserDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(UserDetails.this, "Error in server");
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
                Global.msgDialog(UserDetails.this, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
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

    public void ReportFeed(final String id, final String message) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "report_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        Global.msgDialog(UserDetails.this, "Post reported Successfully.");
//                        Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(UserDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(UserDetails.this, "Error in server");
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
                Global.msgDialog(UserDetails.this, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("post_id", id);
                param.put("report_text", message);
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
    public void onLikeClickListener(NewPostModel post) {
        likeFeed(post.getId());
    }

    @Override
    public void onDislikeClickListener(NewPostModel post) {
        dislikeFeed(post.getId());
    }

    @Override
    public void onCommentClickListener(NewPostModel post) {
        Timber.e(post.toString());
        Intent intent = new Intent(UserDetails.this, FeedDetails.class);
        intent.putExtra("feed_id", post.getId());
        startActivity(intent);
    }

    @Override
    public void onShareClickListener(NewPostModel post) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, post.getDetails_url());
        shareIntent.setType("text/html");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }

    @Override
    public void onReportFeedListener(String id, String message) {
        ReportFeed(id, message);
    }

    @Override
    public void onDeleteFeedListener(String id) {
        DeleteFeed(id);
    }

    @Override
    public void onProfileClickListener(NewPostModel post) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (from_where.equalsIgnoreCase("1")) {
            Intent intent = new Intent(UserDetails.this, FeedDetails.class);
            intent.putExtra("feed_id", feed_id);
            startActivity(intent);
            finish();
        } else if (from_where.equalsIgnoreCase("3")) {
            Intent intent = new Intent(UserDetails.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if(from_where.equalsIgnoreCase("4")){

            Intent intent = new Intent(UserDetails.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(UserDetails.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
