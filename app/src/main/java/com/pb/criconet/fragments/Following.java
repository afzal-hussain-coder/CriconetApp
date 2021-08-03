package com.pb.criconet.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.activity.UserDetails;
import com.pb.criconet.models.NewUserModel;
import com.pb.criconet.models.UserData;
import com.pb.criconet.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class Following extends Fragment {
    SharedPreferences prefs;
    ListView friends_list;
    EditText etxt_search;
    TextView notfound;
    ArrayList<NewUserModel> arrayList_new, modelArrayList;
    Friends_Adapter adapter;
    ProgressDialog progress;
    private UserData userData = new UserData();
    RequestQueue queue;

    // For pagination.
    int page = 1;
    private int visibleThreshold = 10;
    private int previousTotal = 0;
    private boolean loading = true;
    private View rootView;
    private Activity mContext;

    public static Following newInstance() {
        return new Following();
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

//        etxt_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Timber.e(String.valueOf(s));
//                if (s.length() > 0) {
////                    searchFriends(s.toString());
//                    arrayList_new = new ArrayList<NewUserModel>();
//                    for (int i = 0; i < modelArrayList.size(); i++) {
//                        if (modelArrayList.get(i).getName().toLowerCase().contains(s.toString().toLowerCase())) {
//                            arrayList_new.add(modelArrayList.get(i));
//                        }
//                    }
//                    adapter = new Friends_Adapter(mContext, arrayList_new);
//                    friends_list.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                    if (arrayList_new.size() == 0) {
//                        notfound.setVisibility(View.VISIBLE);
//                    } else {
//                        notfound.setVisibility(View.GONE);
//                    }
//                } else {
//                    adapter = new Friends_Adapter(mContext, modelArrayList);
//                    friends_list.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                    if (modelArrayList.size() == 0) {
//                        notfound.setVisibility(View.VISIBLE);
//                    } else {
//                        notfound.setVisibility(View.GONE);
//                    }
////                    RefreshList();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                try {
////                    if (arrayList_new.isEmpty()) {
////
////                    }
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//            }
//        });


//        friends_list.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        friends_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String search = etxt_search.getText().toString();

                Intent intent = new Intent(mContext, UserDetails.class);
                intent.putExtra("user_id", userData.getFollowing_users().get(position).getUser_id());
                intent.putExtra("FROM", "4");
                startActivity(intent);
                getActivity().finish();
            }
        });

        if (Global.isOnline(mContext)) {
            getUsersDetails();
            //System.out.println("xxxxxxxxxxxxxxxx Search xxxxxxxxxxxxxxx");
        } else {
            Global.showDialog(mContext);
        }
     return  rootView;
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
                                adapter = new Friends_Adapter(mContext, userData.getFollowing_users());
                                friends_list.setAdapter(adapter);

                                if (userData.getFollowing_users().size() == 0) {
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

    @Override
    public void onResume() {
        super.onResume();
//        etxt_search.setText("");
//        page = 1;
//        visibleThreshold = 10;
//        previousTotal = 0;
//        loading = true;
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
//        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_following_user",
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

//    public void searchFriends(String searchkey) {
//        final JSONObject json = new JSONObject();
//        try {
////    "action:searchfriends,user_id:113,searchkey:Rama"
//            json.put("action", "friend");
//            json.put("user_id", SessionManager.get_user_id(prefs));
//            json.put("loginId", SessionManager.get_user_id(prefs));
//            json.put("searchkey", searchkey);
//            Timber.e(" data : %s", json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        progress.show();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST, Global.URL, json,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Timber.e("  %s", response);
////{"status":"Success","userList":[{"id":"122","firstName":"Ramandeep Singh","emailId":"ramandeep.singh@evirtualservices.com",
//// "firebaseId":"12345","image":"http:\/\/demo.evirtualservices.com\/criconet\/img\/profile\/1539592879profile.jpeg","address":"Noida",
//// "employment":"EVS","studied":"KIIT","school":"SSMS","friendststus":"2"},{"id":"125","firstName":"Ramandeep Singh","emailId":"raman@gmail.com","firebaseId":"TqzdYfJFjZamSJRoMQnD3Q9to4i2","image":"http:\/\/demo.evirtualservices.com\/criconet\/img\/profile\/1540383250profile.jpeg","address":"Noida","employment":"EVS","studied":"KIIT","school":"SSMs","friendststus":"3"}],"page":"1","pagelimit":"20","totalPage":1}
//                        progress.dismiss();
//                        try {
//                            JSONObject jsonObject = response;
//                            if (jsonObject.getString("status").equals("Success")) {
//                                JSONArray array = jsonObject.getJSONArray("userList");
//                                arrayList_new = new ArrayList<NewUserModel>();
//                                for (int i = 0; i < array.length(); i++) {
//                                    UserModel model = new UserModel();
//                                    JSONObject obj = array.getJSONObject(i);
//                                    model.setId(obj.getString("id"));
//                                    model.setFirstName(obj.getString("firstName"));
//                                    model.setEmailId(obj.getString("emailId"));
//                                    model.setImage(obj.getString("image"));
//                                    model.setAddress(obj.getString("address"));
//                                    model.setFriendId(obj.getString("friendId"));
//                                    model.setFirebaseId(obj.getString("firebaseId"));
//                                    model.setEmployment(obj.getString("employment"));
//                                    model.setStudied(obj.getString("studied"));
//                                    model.setSchool(obj.getString("school"));
//                                    model.setFriendStatus(obj.getString("friendStatus"));
////                                    arrayList_new.add(model);
//                                }
//                                adapter = new Friends_Adapter(mContext, arrayList_new);
//                                friends_list.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
//                                if (arrayList_new.size() == 0) {
//                                    notfound.setVisibility(View.VISIBLE);
//                                } else {
//                                    notfound.setVisibility(View.GONE);
//                                }
//
//                            } else if (jsonObject.getString("status").equals("Fail")) {
//                                Global.msgDialog(mContext, jsonObject.getString("msg"));
//                            } else {
//                                Global.msgDialog(mContext, "Error in server");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                progress.dismiss();
//                Global.msgDialog(mContext, "Internet connection is slow");
//            }
//        });
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        queue.add(jsonObjectRequest);
//    }

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

            holder.user_name.setText(arrayList_image.get(position).getName());
//            holder.user_address.setText(arrayList_image.get(position).getAddress());
            Glide.with(context).load(arrayList_image.get(position).getAvatar()).into(holder.user_image);

            holder.user_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(Following.this, Individual_inner.class);
//                    intent.putExtra("stdid", arrayList_image.get(position).getUser_id());
//                    intent.putExtra("name", arrayList_image.get(position).getName());
//                    intent.putExtra("image", arrayList_image.get(position).getAvatar());
//                    intent.putExtra("firebaseid", arrayList_image.get(position).getFirebaseId());
//                    intent.putExtra("deviceToken", arrayList_image.get(position).getDeviceToken());
//                    startActivity(intent);
                }
            });

            return convertView;
        }

        private class ViewHolder {
            ImageView user_image, user_status;
            TextView user_name, user_address;
            RelativeLayout relative_dash;
        }
    }

}
