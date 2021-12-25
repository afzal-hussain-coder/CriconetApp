package com.pb.criconet.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CCResource;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.DataModel;
import com.pb.criconet.Utills.DateDropDownView;
import com.pb.criconet.Utills.FilterBookingDropDownView;
import com.pb.criconet.Utills.FilterCoachSpecilitiesDropDownViewo;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.SortDropDownView;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.activity.MainActivity;
import com.pb.criconet.adapters.CoachListAdapter;
import com.pb.criconet.models.CoachList;
import com.pb.criconet.models.NewPostModel;
import com.pb.criconet.models.Specialization;
import com.pb.criconet.models.UserIdInput;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import timber.log.Timber;


public class CoachFragment extends Fragment {
    View rootView;
    private ProgressDialog progressDialog;
    private SharedPreferences prefs;
    private RequestQueue queue;
    private RecyclerView recyclerView;
    CustomLoaderView loaderView;
    SortDropDownView drop_sorting;
    String experience = "";
    String price = "";
    FilterCoachSpecilitiesDropDownViewo drop_filter;
    private ArrayList<Specialization> option = new ArrayList<>();
    String filterType = "";
    TextView tv_sorting;
    Specialization specialization;
    ImageView img_banner;
    String coupon_code = "";
    ;

    public static CoachFragment newInstance() {
        return new CoachFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.coach_fragment, container, false);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loaderView = CustomLoaderView.initialize(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);

        if (SessionManager.getProfileType(prefs).equalsIgnoreCase("coach")) {
            mTitle.setText(getResources().getString(R.string.VIEW_YOUR_PROFILE));
        } else {
            mTitle.setText(getResources().getString(R.string.BOOK_YOUR_COACH));
        }

        TextView tv_post = toolbar.findViewById(R.id.tv_post);
        tv_post.setVisibility(View.GONE);
        ImageView img_addpost = toolbar.findViewById(R.id.img_addpost);
        img_addpost.setVisibility(View.GONE);
        img_addpost.setVisibility(View.GONE);
        ImageView img_close = toolbar.findViewById(R.id.img_close);
        img_close.setVisibility(View.GONE);
        img_banner = rootView.findViewById(R.id.img_banner);

        recyclerView = rootView.findViewById(R.id.coachlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        tv_sorting = rootView.findViewById(R.id.tv_sorting);
        drop_sorting = rootView.findViewById(R.id.drop_sorting);

        drop_sorting.setClickListener(new SortDropDownView.onClickInterface() {
            @Override
            public void onClickAction() {

            }

            @Override
            public void onClickDone(String exp, String pri) {
                filterType = "";
                experience = exp;
                price = pri;
                tv_sorting.setText(getResources().getString(R.string.Experience) + experience + " , " + getResources().getString(R.string.price) + price);
                if (Global.isOnline(getActivity())) {
                    getCoachList();
                } else {
                    Global.showDialog(getActivity());
                }
            }

            @Override
            public void onDismiss() {

            }
        });


        drop_filter = rootView.findViewById(R.id.drop_filter);


        drop_filter.setClickListener(new FilterCoachSpecilitiesDropDownViewo.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(ArrayList<Integer> id, ArrayList<String> name) {
                experience = "";
                price = "";

                filterType = id.toString().replace("[", "").replace("]", "")
                        .replace(",", ",");
                String name_string = name.toString().replace("[", "").replace("]", "")
                        .replace(", ", ",");
                drop_filter.setText(name_string);

                Toaster.customToast(filterType + "/" + name_string);
                if (Global.isOnline(getActivity())) {
                    getCoachList();
                } else {
                    Global.showDialog(getActivity());
                }
                //tv_filter.setText("");
            }


            @Override
            public void onDismiss() {
            }
        });
        if (Global.isOnline(getActivity())) {
            getSpecialities();
        } else {
            Global.showDialog(getActivity());
        }
        if (Global.isOnline(getActivity())) {
            getOffer();
        } else {
            Global.showDialog(getActivity());
        }

        if (Global.isOnline(getActivity())) {
            getCoachList();
        } else {
            Global.showDialog(getActivity());
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.bottomNavigation.setSelectedIndex(1, true);
    }

    private void getCoachList() {
        loaderView.showLoader();
        //progressDialog = Global.getProgressDialog(getActivity(), CCResource.getString(getActivity(), R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_lists", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("Response", response);
                // Global.dismissDialog(progressDialog);
                loaderView.hideLoader();
                try{
                    Gson gson = new Gson();
                    CoachList modelArrayList = gson.fromJson(response, CoachList.class);

                    if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                        recyclerView.setAdapter(new CoachListAdapter(modelArrayList.getData(), getActivity()));
                    } else {
                        Toast.makeText(getActivity(), modelArrayList.getErrors().getErrorText(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("order_by", experience);
                param.put("sort_by", price);
                param.put("filter_cat", filterType);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getSpecialities() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + Global.GET_SPECIALITIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response_specialities", response);
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            specialization = new Specialization(jsonArray.getJSONObject(i));
                            option.add(specialization);
                        }

                        drop_filter.setOptionList(option);
                    } else {
                        Toaster.customToast(getString(R.string.socket_timeout));
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
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
            }
        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> param = new HashMap<String, String>();
//                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", SessionManager.get_session_id(prefs));
//                param.put("order_by", experience);
//                param.put("sort_by", price);
//                param.put("filter_cat", filterType);
//                Timber.e(param.toString());
//                return param;
//            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getOffer() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.getOffet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("offer_res", response);
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {


                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = null;


                        if (jsonObject1.has("coupon_code")) {
                            coupon_code = jsonObject1.getString("coupon_code");
                            SessionManager.save_coupon_code(prefs,coupon_code);
                        }
                        if (jsonObject1.has("offer_details")) {
                            jsonArray = jsonObject1.getJSONArray("offer_details");
                        }

                        if (jsonArray.length()==0) {
                            img_banner.setVisibility(View.GONE);
                        }else{
                            img_banner.setVisibility(View.VISIBLE);
                            JSONObject jsonObject2= jsonArray.getJSONObject(0);
                            if (!jsonObject2.getString("banner").isEmpty()) {
                                Glide.with(getActivity()).load(jsonObject2.getString("banner"))
                                        .into(img_banner);
                            } else {
                                Glide.with(getActivity()).load(R.drawable.bg_coach)
                                        .into(img_banner);
                            }
                        }


                    } else {
                        Toaster.customToast(getString(R.string.socket_timeout));
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
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
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

}
