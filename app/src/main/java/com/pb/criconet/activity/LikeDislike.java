package com.pb.criconet.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.models.NewUserModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class LikeDislike extends AppCompatActivity {
    SharedPreferences prefs;
    ListView friends_list;
    EditText etxt_search;
    TextView notfound;
    ArrayList<NewUserModel> arrayList_new, modelArrayList;
    Friends_Adapter adapter;
    ProgressDialog progress;
    RequestQueue queue;
    String Post_id, post_type;

    // For pagination.
    int page = 1;
    public static final String Type = "Type";
    public static final String PostId = "post_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LikeDislike.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);


        prefs = PreferenceManager.getDefaultSharedPreferences(LikeDislike.this);
        queue = Volley.newRequestQueue(LikeDislike.this);
        progress = new ProgressDialog(LikeDislike.this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        friends_list = (ListView) findViewById(R.id.friends_list);
        etxt_search = (EditText) findViewById(R.id.etxt_search);
        notfound = (TextView) findViewById(R.id.notfound);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if(bundle.getString(Type).equalsIgnoreCase("Like")){
                mTitle.setText("Liked");
            }else{
                mTitle.setText(bundle.getString(Type));
            }


            post_type = bundle.getString(Type);
            Post_id = bundle.getString(PostId);
        }

//        friends_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String search = etxt_search.getText().toString();
//                if (search.length() > 0) {
//                    Intent intent = new Intent(LikeDislike.this, UserDetails.class);
//                    intent.putExtra("user_id", arrayList_new.get(position).getUser_id());
////                    intent.putExtra("friendStatus", arrayList_new.get(position).getFriendStatus());
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(LikeDislike.this, UserDetails.class);
//                    intent.putExtra("user_id", modelArrayList.get(position).getUser_id());
////                    intent.putExtra("friendStatus", modelArrayList.get(position).getFriendStatus());
//                    startActivity(intent);
//                }
//            }
//        });

        RefreshList();

    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
    }

    public void RefreshList() {
        page = 1;

        modelArrayList = new ArrayList<>();
        adapter = new Friends_Adapter(LikeDislike.this, modelArrayList);
        friends_list.setAdapter(adapter);
        if (Global.isOnline(LikeDislike.this)) {
            getFriends();
            System.out.println("xxxxxxxxxxxxxxxx Search xxxxxxxxxxxxxxx");
        } else {
            Global.showDialog(LikeDislike.this);
        }
    }

    public void getFriends() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_post_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e("response:  %s", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                ArrayList<NewUserModel> object;
                                if (post_type.equalsIgnoreCase(LikeDislike.this.getString(R.string.likes)))
                                    object = NewUserModel.fromJson(jsonObject.getJSONArray("post_likes"));
                                else
                                    object = NewUserModel.fromJson(jsonObject.getJSONArray("post_wonders"));
                                modelArrayList.addAll(object);
                                adapter.notifyDataSetChanged();
                                if (modelArrayList.size() == 0) {
                                    notfound.setVisibility(View.VISIBLE);
                                } else {
                                    notfound.setVisibility(View.GONE);
                                }

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(LikeDislike.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(LikeDislike.this, "Error in server");
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
                        Global.msgDialog(LikeDislike.this, "Error from server");
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
                param.put("post_id", Post_id);
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    public static class Friends_Adapter extends BaseAdapter {
        Activity context;
        ViewHolder holder = null;
        LayoutInflater inflater;
        ArrayList<NewUserModel> arrayList_image;

        Friends_Adapter(Activity mcontext, ArrayList<NewUserModel> chatname_list1) {
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
                convertView = inflater.inflate(R.layout.friendslist_adapter_item, null);
                holder = new ViewHolder();
                holder.relative_dash = (RelativeLayout) convertView.findViewById(R.id.relative_dash);
                holder.user_image = (ImageView) convertView.findViewById(R.id.user_image);
                holder.user_status = (ImageView) convertView.findViewById(R.id.user_status);
                holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
                holder.user_address = (TextView) convertView.findViewById(R.id.user_address);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
                holder.relative_dash.startAnimation(scaleUp);
            }

            holder.user_name.setText(Global.capitizeString(arrayList_image.get(position).getName()));
            //holder.user_address.setText(arrayList_image.get(position).getAddress());
            Glide.with(context).load(arrayList_image.get(position).getAvatar()).into(holder.user_image);


            return convertView;
        }

        private class ViewHolder {
            ImageView user_image, user_status;
            TextView user_name, user_address;
            RelativeLayout relative_dash;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LikeDislike.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
