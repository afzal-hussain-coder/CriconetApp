package com.pb.criconet.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
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
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.FilterCoachSpecilitiesDropDownViewo;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.GridSpacingItemDecoration;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.SortDropDownView;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.activity.AcademyBookChcekOutActvity;
import com.pb.criconet.activity.MainActivity;
import com.pb.criconet.adapters.AcademyListAdapter;
import com.pb.criconet.adapters.TimeAdapterAcademy;
import com.pb.criconet.models.AcademyListModel;
import com.pb.criconet.models.CoachList;
import com.pb.criconet.models.Specialization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public final class AcademyFragment extends Fragment {
    View rootView;
    private ProgressDialog progressDialog;
    private SharedPreferences prefs;
    private RequestQueue queue;
    private RecyclerView recyclerView,recycler_vieww;
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
    private SlideUp slideUp;
    private View dim, rootVieww;
    private View slideView;
    private ArrayList<AcademyListModel> academyListModels = new ArrayList<>();
    String selectedTimeSlott="",selectedTimeSlot="";
    AcademyListModel academyListModel2=null;

    TextView tv_academyName, tv_academyAddress, tv_price, tv_specilities;
    RatingBar rating_bar_review;
    Button btn_done;
    Context mContext;

    public static AcademyFragment newInstance() {
        return new AcademyFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.academy_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loaderView = CustomLoaderView.initialize(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);

//        if (SessionManager.getProfileType(prefs).equalsIgnoreCase("coach")) {
//            mTitle.setText(getResources().getString(R.string.VIEW_YOUR_PROFILE));
//        } else {
//            mTitle.setText(getResources().getString(R.string.BOOK_YOUR_COACH));
//        }
        mTitle.setText(getResources().getString(R.string.Book_academy));

        rootVieww = rootView.findViewById(R.id.root_view);
        slideView = rootView.findViewById(R.id.slideView);
        dim = rootView.findViewById(R.id.dim);
        btn_done = rootView.findViewById(R.id.btn_done);
        recycler_vieww = rootView.findViewById(R.id.recycler_view);
        recycler_vieww.setLayoutManager(new GridLayoutManager(mContext, 3));
        recycler_vieww.addItemDecoration(new GridSpacingItemDecoration(3, 10, false));
        tv_academyName = rootView.findViewById(R.id.tv_academyName);
        tv_academyAddress = rootView.findViewById(R.id.tv_academyAddress);
        tv_price = rootView.findViewById(R.id.tv_price);
        tv_specilities = rootView.findViewById(R.id.tv_specilities);
        rating_bar_review = rootView.findViewById(R.id.rating_bar_review);
        slideUp = new SlideUpBuilder(slideView)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                        dim.setAlpha(1 - (percent / 100));
                        if (percent < 100) {
                            // slideUp started showing

                        }
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                        }
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(rootVieww)
                .build();


        TextView tv_post = toolbar.findViewById(R.id.tv_post);
        tv_post.setVisibility(View.GONE);
        ImageView img_addpost = toolbar.findViewById(R.id.img_addpost);
        img_addpost.setVisibility(View.GONE);
        img_addpost.setVisibility(View.GONE);
        ImageView img_close = toolbar.findViewById(R.id.img_close);
        img_close.setVisibility(View.GONE);

        recyclerView = rootView.findViewById(R.id.coachlistt);
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
//        if (Global.isOnline(getActivity())) {
//            getSpecialities();
//        } else {
//            Global.showDialog(getActivity());
//        }
//        if (Global.isOnline(getActivity())) {
//            getOffer();
//        } else {
//            Global.showDialog(getActivity());
//        }

        if (Global.isOnline(getActivity())) {
            getCoachList();
        } else {
            Global.showDialog(getActivity());
        }

        btn_done.setOnClickListener(v -> {

            if(selectedTimeSlot.equalsIgnoreCase("")){
                Toaster.customToast(getString(R.string.Please_select_time_first));
            }else{

                Intent intent = new Intent(mContext, AcademyBookChcekOutActvity.class);
                intent.putExtra("Certificate", academyListModel2);
                intent.putExtra("Slot", selectedTimeSlot);
                intent.putExtra("SlotID", selectedTimeSlott);
                startActivity(intent);
                slideUp.hide();
            }

        });

    }
    @Override
    public void onResume() {
        super.onResume();
        MainActivity.bottomNavigation.setSelectedIndex(3, true);
    }

    private void getCoachList() {
        loaderView.showLoader();
        //progressDialog = Global.getProgressDialog(getActivity(), CCResource.getString(getActivity(), R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.ACADEMY_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.d(response);

                try {
                    loaderView.hideLoader();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        AcademyListModel academyListModel;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            academyListModel = new AcademyListModel(jsonArray.getJSONObject(i));
                            academyListModels.add(academyListModel);
                        }

                        recyclerView.setAdapter(new AcademyListAdapter(academyListModels, getActivity(), new AcademyListAdapter.itemclicklistner() {
                            @Override
                            public void book(AcademyListModel academyListModel1) {
                                academyListModel2 = academyListModel1;
                                /// slider should be open..
                                tv_academyAddress.setText(academyListModel1.getAddress());
                                tv_academyName.setText(academyListModel1.getName());
                                if(academyListModel1.getRating().isEmpty()){
                                    rating_bar_review.setVisibility(View.GONE);
                                }else{
                                    rating_bar_review.setVisibility(View.VISIBLE);
                                    rating_bar_review.setRating(Float.parseFloat(academyListModel1.getRating()));
                                }

                                tv_price.setText(getActivity().getString(R.string.fees)+": "+"\u20B9" +academyListModel1.getFee()+"/"+getActivity().getString(R.string.month));
                                tv_specilities.setText(academyListModel1.getCat_title());

                                recycler_vieww.setAdapter(new TimeAdapterAcademy(getActivity(), academyListModel1.getAcademySlots(), new TimeAdapterAcademy.timeSelectt() {
                                    @Override
                                    public void getSlotId(String slotId, String slotTime) {
                                        //String[] namesArr = arrayListUser.toArray(new String[arrayListUser.size()]);
                                        //String[] namesArrr = arrayListUser_slot.toArray(new String[arrayListUser_slot.size()]);
                                        selectedTimeSlot=slotTime;
                                        selectedTimeSlott=slotId;
                                        // selectedTimeSlott = toCSV(namesArr);
                                        // selectedTimeSlot = toCSVV(namesArrr);
                                        //Toaster.customToast(selectedTimeSlot+"/"+selectedTimeSlott);
                                    }
                                }));
                                slideUp.show();
                            }
                        }));
                    } else {
                        loaderView.hideLoader();
                        Toast.makeText(getActivity(), jsonObject.getString("api_text"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
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
//                param.put("order_by", experience);
//                param.put("sort_by", price);
//                param.put("filter_cat", filterType);
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
                Global.msgDialog(getActivity(),"Error in server");
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
                            SessionManager.save_coupon_code(prefs, coupon_code);
                        }
                        if (jsonObject1.has("offer_details")) {
                            jsonArray = jsonObject1.getJSONArray("offer_details");
                        }

//                        if (jsonArray.length()==0) {
//                            //img_banner.setVisibility(View.GONE);
//                        }else{
//                            //img_banner.setVisibility(View.VISIBLE);
//                            JSONObject jsonObject2= jsonArray.getJSONObject(0);
//                            if (!jsonObject2.getString("banner").isEmpty()) {
//                                Glide.with(getActivity()).load(jsonObject2.getString("banner"))
//                                        .into(img_banner);
//                            } else {
//                                Glide.with(getActivity()).load(R.drawable.bg_coach)
//                                        .into(img_banner);
//                            }
//                        }


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

    public static String toCSV(String[] array) {
        String result = "";
        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        } return result;
    }
    public static String toCSVV(String[] array) {
        String result = "";
        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        } return result;
    }

}
