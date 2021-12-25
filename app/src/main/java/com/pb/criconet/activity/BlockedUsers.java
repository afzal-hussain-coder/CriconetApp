package com.pb.criconet.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.models.NewUserModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class BlockedUsers extends AppCompatActivity {
    SharedPreferences prefs;
    ListView notification_list;
    EditText etxt_search;
    TextView notfound;
    ArrayList<NewUserModel> modelArrayList;
    Block_Adapter adapter;
    ProgressDialog progress;
    RequestQueue queue;

    // For pagination.
    int page = 1;
    private int visibleThreshold = 10;
    private int previousTotal = 0;
    private boolean loading = true;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        rootView = inflater.inflate(R.layout.blocked_users, container, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blocked_users);

        Toolbar toolbar = (Toolbar) BlockedUsers.this.findViewById(R.id.toolbar);
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
        mTitle.setText(R.string.blocked_users);

        prefs = PreferenceManager.getDefaultSharedPreferences(BlockedUsers.this);
        queue = Volley.newRequestQueue(BlockedUsers.this);
        progress = new ProgressDialog(BlockedUsers.this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        notification_list = (ListView) findViewById(R.id.notification_list);
        etxt_search = (EditText) findViewById(R.id.etxt_search);
        notfound = (TextView) findViewById(R.id.notfound);


//        notification_list.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (loading) {
//                    if (totalItemCount > previousTotal) {
//                        loading = false;
//                        previousTotal = totalItemCount;
//                        page++;
//                    }
//                }
//                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
//                    // I load the next page of gigs using a background task,
//                    // but you can call any function here.
//                    getFriends();
//                    loading = true;
//                }
//            }
//        });


        RefreshList();


    }

    public void RefreshList() {

        modelArrayList = new ArrayList<>();
        adapter = new Block_Adapter(BlockedUsers.this, modelArrayList);
        notification_list.setAdapter(adapter);
        if (Global.isOnline(BlockedUsers.this)) {
            getBlockUsers();
            System.out.println("xxxxxxxxxxxxxxxx getNotification xxxxxxxxxxxxxxx");
        } else {
            Global.showDialog(BlockedUsers.this);
        }
    }

    public void getBlockUsers() {

        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_blocked_users",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e("response:  %s", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                ArrayList<NewUserModel> object = NewUserModel.fromJson(jsonObject.getJSONArray("blocked_users"));
                                modelArrayList.addAll(object);
                                adapter.notifyDataSetChanged();
                                if (modelArrayList.size() == 0) {
                                    notfound.setVisibility(View.VISIBLE);
                                } else {
                                    notfound.setVisibility(View.GONE);
                                }

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(BlockedUsers.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(BlockedUsers.this, getResources().getString(R.string.error_server));
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
                        Global.msgDialog(BlockedUsers.this, getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
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

    public void UnBlockUser(final String user_id) {
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
                                RefreshList();
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(BlockedUsers.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(BlockedUsers.this, getResources().getString(R.string.error_server));
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
                        Global.msgDialog(BlockedUsers.this, getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//        "user_id:1735
//        s:1
//        recipient_id:1703
//        block_type:un - block "
                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", "1");
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



    public class Block_Adapter extends BaseAdapter {
        Activity context;
        ViewHolder holder = null;
        LayoutInflater inflater;
        ArrayList<NewUserModel> arrayList_image;

        Block_Adapter(Activity mcontext, ArrayList<NewUserModel> chatname_list1) {
            // TODO Auto-generated constructor stub
            context = mcontext;
            arrayList_image = chatname_list1;
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrayList_image.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.blocked_adapter_item, null);
                holder = new ViewHolder();
                holder.relative_dash = (RelativeLayout) convertView.findViewById(R.id.relative_dash);
                holder.user_image = (RoundedImageView) convertView.findViewById(R.id.user_image);
                holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
                //holder.user_address = (TextView) convertView.findViewById(R.id.user_address);
                holder.unblock = (Button) convertView.findViewById(R.id.unblock);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
                holder.relative_dash.startAnimation(scaleUp);
            }

            holder.user_name.setText(Global.capitizeString(arrayList_image.get(position).getName()));
//            holder.user_address.setText(arrayList_image.get(position).getEmailId());
            Glide.with(context).load(arrayList_image.get(position).getAvatar()).into(holder.user_image);

            holder.unblock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UnBlockUser(arrayList_image.get(position).getUser_id());
                }
            });

            return convertView;
        }

        private class ViewHolder {
            RoundedImageView user_image, user_status;
            TextView user_name, user_address;
            Button unblock;
            RelativeLayout relative_dash;
        }
    }


}
