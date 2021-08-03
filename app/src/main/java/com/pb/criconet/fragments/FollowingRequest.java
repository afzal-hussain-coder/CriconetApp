package com.pb.criconet.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.pb.criconet.models.UserData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class FollowingRequest extends Fragment {
    SharedPreferences prefs;
    ListView friends_list;
    EditText etxt_search;
    TextView notfound;
    ArrayList<NewUserModel> arrayList_new, modelArrayList;
    Friends_Adapter adapter;
    ProgressDialog progress;
    RequestQueue queue;
    // For pagination.
    int page = 1;
    private View rootView;
    private Activity mContext;
    private int visibleThreshold = 10;
    private int previousTotal = 0;
    private boolean loading = true;
    private UserData userData = new UserData();


    public static FollowingRequest newInstance() {
        return new FollowingRequest();
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.friends, container, false);


        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        queue = Volley.newRequestQueue(mContext);
        progress = new ProgressDialog(mContext);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        friends_list = rootView.findViewById(R.id.friends_list);
        etxt_search = rootView.findViewById(R.id.etxt_search);
        notfound = rootView.findViewById(R.id.notfound);

        if (Global.isOnline(mContext)) {
            getUsersDetails();
            //System.out.println("xxxxxxxxxxxxxxxx Search xxxxxxxxxxxxxxx");
        } else {
            Global.showDialog(mContext);
        }
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
//        etxt_search.setText("");
//        page = 1;
//        visibleThreshold = 10;
//        previousTotal = 0;
//        loading = true;
    }

    public void getUsersDetails() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_user_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Timber.v(response);

                        Log.d("UserDetailsResponse",response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                                JSONObject object = jsonObject.getJSONObject("user_data");
                                userData = UserData.fromJson(object);
                                adapter = new Friends_Adapter(mContext, userData.getFollow_request_users());
                                friends_list.setAdapter(adapter);
                                if (userData.getFollow_request_users().size() == 0) {
                                    notfound.setVisibility(View.VISIBLE);
                                    friends_list.setVisibility(View.GONE);
                                } else {
                                    notfound.setVisibility(View.GONE);
                                    friends_list.setVisibility(View.VISIBLE);
                                }

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(mContext, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(mContext, "Error in server");
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
                        Global.msgDialog(mContext, "Error from server");
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("user_profile_id", SessionManager.get_user_id(prefs));
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

//    public void RefreshList() {
//        page = 1;
//        visibleThreshold = 10;
//        previousTotal = 0;
//        loading = true;
//
//        modelArrayList = new ArrayList<>();
//        adapter = new Friends_Adapter(mContext, modelArrayList);
//        friends_list.setAdapter(adapter);
//        if (Global.isOnline(mContext)) {
//            getFriends();
//            System.out.println("xxxxxxxxxxxxxxxx Search xxxxxxxxxxxxxxx");
//        } else {
//            Global.showDialog(mContext);
//        }
//    }

//    public void getFriends() {
//        progress.show();
//        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_follower_user_req",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Timber.e("response:  %s", response);
//                        progress.dismiss();
//                        try {
//                            JSONObject jsonObject = new JSONObject(response.toString());
//                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
//                                ArrayList<NewUserModel> object = NewUserModel.fromJson(jsonObject.getJSONArray("data"));
//                                modelArrayList.addAll(object);
//                                adapter.notifyDataSetChanged();
//                                if (modelArrayList.size() == 0) {
//                                    notfound.setVisibility(View.VISIBLE);
//                                    notfound.setText(" No Pending Request ");
//                                } else {
//                                    notfound.setVisibility(View.GONE);
//                                }
//
//                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
//                                Global.msgDialog(mContext, jsonObject.optJSONObject("errors").optString("error_text"));
//                            } else {
//                                Global.msgDialog(mContext, "Error in server");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        progress.dismiss();
//                        Global.msgDialog(mContext, "Error from server");
////                Global.msgDialog(EditProfile.this, "Internet connection is slow");
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> param = new HashMap<String, String>();
//                param.put("user_id", SessionManager.get_user_id(prefs));
////                param.put("s", "1");
//                param.put("s", SessionManager.get_session_id(prefs));
//                System.out.println("data   :" + param);
//                return param;
//            }
//        };
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//
//    }

    public void acceptDecline(final String recipient_id, final String request) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "accept_decline_request",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e("response:  %s", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                getUsersDetails();
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(mContext, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(mContext, "Error in server");
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
                        Global.msgDialog(mContext, "Error from server");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                "user_id:1735
//                s:1
//                recipient_id:1703
//                request:accept / decline"
                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("recipient_id", recipient_id);
                param.put("request", request);
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    public class Friends_Adapter extends BaseAdapter {
        Activity context;
        ViewHolder holder = null;
        LayoutInflater inflater;
        ArrayList<NewUserModel> arrayList_image;

        Friends_Adapter(Activity mcontext, ArrayList<NewUserModel> chatname_list1) {
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
                convertView = inflater.inflate(R.layout.friendsrequest_adapter_item, null);
                holder = new ViewHolder();
                holder.relative_dash = (RelativeLayout) convertView.findViewById(R.id.relative_dash);
                holder.user_image = (ImageView) convertView.findViewById(R.id.user_image);
                holder.req_decline = (ImageView) convertView.findViewById(R.id.req_decline);
                holder.req_accept = (ImageView) convertView.findViewById(R.id.req_accept);
                holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
                holder.user_address = (TextView) convertView.findViewById(R.id.user_address);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
                holder.relative_dash.startAnimation(scaleUp);
            }

            holder.user_name.setText(arrayList_image.get(position).getName());
//            holder.user_address.setText(arrayList_image.get(position).getAddress());
            Glide.with(context).load(arrayList_image.get(position).getAvatar()).into(holder.user_image);

            holder.req_decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptDecline(arrayList_image.get(position).getUser_id(), "decline");
                }
            });
            holder.req_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptDecline(arrayList_image.get(position).getUser_id(), "accept");
                }
            });


            return convertView;
        }

        private class ViewHolder {
            ImageView user_image, req_decline, req_accept;
            TextView user_name, user_address;
            RelativeLayout relative_dash;
        }
    }

}
