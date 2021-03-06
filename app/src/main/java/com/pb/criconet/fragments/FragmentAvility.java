package com.pb.criconet.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.FilterCoachCloseTimeDropDownView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.GridSpacingItemDecoration;
import com.pb.criconet.Utills.NavigationController;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.activity.CoachProfileActivity;
import com.pb.criconet.adapters.CoachButtonAdapter;
import com.pb.criconet.adapters.TimeAdapter;
import com.pb.criconet.adapters.TimeAdaptercoachh;
import com.pb.criconet.event.SlotId;
import com.pb.criconet.models.BookCoach;
import com.pb.criconet.models.DateSlotes;
import com.pb.criconet.models.Datum;
import com.pb.criconet.models.TimeSlot;
import com.pb.criconet.models.updatedTimeSlot;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;


public class FragmentAvility extends Fragment {
    private SharedPreferences prefs;
    private RequestQueue queue;
    private ProgressDialog progress;
    private CalendarView datePicker;
    private String dateGott = "";
    private String year;
    private String month;
    private String currentyear;
    private String currentmonth;
    private String day;
    private String dateGot = "";
    private StringBuilder multiDate;
    private StringBuilder multiTime;
    private RecyclerView recyclerView;
    private Calendar[] days;
    List<EventDay> events;
    Button btn_login;
    private FilterCoachCloseTimeDropDownView spinerCurency;
    private ArrayList<com.pb.criconet.Utills.DataModel> option_currency = new ArrayList<>();
    String selectedCloseLimit = "";
    String selectedTimeSlot = "", selectedTimeSlott = "",selectedTimeSlotUpdated="";
    Typeface typeface;
    CustomLoaderView loaderView;
    String booking_close_time = "";
    View rootView;
    Context mContext;
    private SlideUp slideUp;
    private View dim, rootVieww;
    private View slideView;

    private RecyclerView recyclerView_slot;
    private Button btn_done;

    private TimeSlot modelArrayList;
    ArrayList<String> arrayListselectedSlot = new ArrayList<>();
    TimePreodee.selectedSlot selectedSlot;
    String closeTime = "";
    List<String> updatedDateList = new ArrayList<>();
    ArrayList<updatedTimeSlot> updatedSlotList;
    JSONArray updateJsonArray = null;
    Calendar cal;
    Date previousDate;
    JSONArray jsonArray = null;
    JSONObject jsonObject = null;
    JSONArray jsonArrayUp = new JSONArray();
    RelativeLayout rlSlot;

    public static FragmentAvility newInstance() {
        return new FragmentAvility();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_availability, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        updatedDateList = new ArrayList<>();
        //jsonArray = new JSONArray();

        //navigationController = new NavigationController(Objects.requireNonNull(getActivity()));
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        loaderView = CustomLoaderView.initialize(getActivity());
        typeface = ResourcesCompat.getFont(getActivity(), R.font.opensans_semibold);


        btn_login = rootView.findViewById(R.id.btn_login);
        datePicker = rootView.findViewById(R.id.calendarView);


        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        spinerCurency = rootView.findViewById(R.id.spinerCurency);
        if (Global.isOnline(mContext)) {
            getSessionCloseTime();
        } else {
            Global.showDialog(getActivity());
        }
        //option_currency.add(new com.pb.criconet.Utills.DataModel("Select booking session close time"));

        //selectedCloseLimit = option_currency.get(0).getName();
        //spinerCurency.setText(selectedCloseLimit);
        spinerCurency.setTypeface(typeface);
        spinerCurency.setClickListener(new FilterCoachCloseTimeDropDownView.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name, String id) {
                selectedCloseLimit = name;
                booking_close_time = id;
                //Toaster.customToast(selectedCloseLimit);
            }

            @Override
            public void onDismiss() {
            }
        });

        if (Global.isOnline(mContext)) {
            getUsersDetails();
        } else {
            Global.showDialog(getActivity());
        }

        // disable dates before today
        Calendar min = Calendar.getInstance();
        previousDate = min.getTime();
        min.add(Calendar.DAY_OF_MONTH, -1);
        datePicker.setMinimumDate(min);

        year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
        month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());
        day = Global.getDay(datePicker.getCurrentPageDate().getTime().toString());
        //dateGot = Global.getDateGotCoach(datePicker.getCurrentPageDate().getTime().toString());
        printDatesInMonth(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        //rlSlot = rootView.findViewById(R.id.rlSlot);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        btn_done = rootView.findViewById(R.id.btn_done);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 20, false));

        rootVieww = rootView.findViewById(R.id.root_view);
        slideView = rootView.findViewById(R.id.slideView);
        dim = rootView.findViewById(R.id.dim);
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

//                            if (Global.isOnline(mContext)) {
//                                getBookingHistory();
//                            } else {
//                                Global.showDialog(mActivity);
//                            }

                        }
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(rootVieww)
                .build();


        datePicker.setOnDayClickListener(eventDay -> {
            dateGot = Global.getDateGotCoach(eventDay.getCalendar().getTime().toString());

            JSONObject jsonObject = null;
            updatedTimeSlot timeSlot = null;

            updatedSlotList = new ArrayList<>();
            updatedSlotList.clear();
            for (int i = 0; i < updateJsonArray.length(); i++) {
                try {
                    jsonObject = updateJsonArray.getJSONObject(i);
                    if (jsonObject.has("available_date")) {
                        String avl_date = jsonObject.getString("available_date");
                        // updatedDateList.add(avl_date);
                        //Log.d("Size",dateGot+"/"+Global.getDateGotCoachh(avl_date));
                        if (dateGot.equalsIgnoreCase(Global.getDateGotCoachh(avl_date))) {
                            if (jsonObject.has("slot_id")) {
                                JSONArray jsonArray1 = jsonObject.getJSONArray("slot_id");
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                    timeSlot = new updatedTimeSlot(jsonObject1);
                                    updatedSlotList.add(timeSlot);
                                }
                            }
                            Log.d("Size", updatedSlotList.size() + "");
                            break;

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            if (Global.getDateGot(eventDay.getCalendar().getTime().toString()).equals(Global.getDateGot(previousDate.toString())) || eventDay.getCalendar().getTime().compareTo(previousDate) > 0) {
                if (Global.isOnline(getActivity())) {
                    getDateSlote(dateGot, updatedSlotList);
                } else {
                    Global.showDialog(getActivity());
                }
            } else {
                Toaster.customToast("Select enabled date");
            }

        });


        datePicker.setOnForwardPageChangeListener(() -> {
            year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());
            day = Global.getDay(datePicker.getCurrentPageDate().getTime().toString());
            // getCoachDete();
            printDatesInMonth(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
            dateGot = Global.getDateGotCoach(datePicker.getCurrentPageDate().getTime().toString());
            //getDateSlote(dateGot);
        });

        datePicker.setOnPreviousPageChangeListener(() -> {
            year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());
            day = Global.getDay(datePicker.getCurrentPageDate().getTime().toString());
            //getCoachDete();
            printDatesInMonth(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
            dateGot = Global.getDateGotCoach(datePicker.getCurrentPageDate().getTime().toString());
            //getDateSlote(dateGot);
        });

        btn_done.setOnClickListener(view -> {
            selectedTimeSlot = selectedTimeSlott;

            if (Global.isOnline(mContext)) {
                slideUp.hide();
                updateCoachAvailability();
            } else {
                Global.showDialog(getActivity());
            }


          /*  if (checkValidation()) {
                slideUp.hide();
                updateCoachAvailability();
            }*/
        });
        btn_login.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), CoachProfileActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();

        });

    }

    public void printDatesInMonth(int year, int month, int day) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month, day);
        events = new ArrayList<>();
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i <= daysInMonth; i++) {
            System.out.println(fmt.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, -1);

            for (int j = 0; j < updatedDateList.size(); j++) {
                if (Global.getDateGot(cal.getTime().toString()).equals(updatedDateList.get(j))) {
                    events.add(new EventDay(Global.convertStringToCalendar(fmt.format(cal.getTime())), Global.getThreeDotss(getActivity())));
                    datePicker.setEvents(events);
                }
            }

            events.add(new EventDay(Global.convertStringToCalendar(fmt.format(cal.getTime())), Global.getThreeDots(getActivity())));
            datePicker.setEvents(events);
        }
    }

    private void getDateSlote(String dateGot, ArrayList<updatedTimeSlot> updatedSlotList) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_time_slot_data", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("slotResponse", response);
                Gson gson = new Gson();
                modelArrayList = gson.fromJson(response, TimeSlot.class);
                if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    recyclerView.setAdapter(new TimeAdaptercoachh(getActivity(), modelArrayList.getData(), updatedSlotList, new TimeAdaptercoachh.timeSelectt() {
                        @Override
                        public void getSlotId(ArrayList<String> arrayListUser) {
                            String[] namesArr = arrayListUser.toArray(new String[arrayListUser.size()]);
                            selectedTimeSlott = toCSV(namesArr);
//                            try {
//                                jsonObject = new JSONObject();
//                                if (!selectedTimeSlott.isEmpty()) {
//                                    jsonObject.put(dateGot, selectedTimeSlott);
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }
                    }));
                    // rlSlot.setVisibility(View.VISIBLE);
                    slideUp.show();
                } else {
                    dateGott = "";
                    Toaster.customToast(modelArrayList.getErrors().getErrorText());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                //param.put("date", dateGot);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void updateCoachAvailability() {

        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "update_coach_availability_date", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loaderView.hideLoader();
                Log.d("SlotSubmitResponse", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = null;
                    if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {
                        data = jsonObject.getJSONObject("data");
                        Toaster.customToast(data.getString("message"));
                        refreshData();
                        year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
                        month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());
                        day = Global.getDay(datePicker.getCurrentPageDate().getTime().toString());

                        if (Global.isOnline(mContext)) {
                            getUsersDetails();
                        } else {
                            Global.showDialog(getActivity());
                        }
                        printDatesInMonth(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

                       /* new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(), CoachProfileActivity.class);
                                startActivity(intent);
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        },2000);*/

                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
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
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
//                if(selectedTimeSlot.equalsIgnoreCase("")){
//                    param.put("date_slot", "");
//                    param.put("time_slot", "");
//                }else{
//                    param.put("date_slot", dateGot);
//                    param.put("time_slot", selectedTimeSlot);
//                }
                param.put("date_slot", dateGot);
                param.put("time_slot", selectedTimeSlot);
                //param.put("date_time_slot",jsonArray.toString());
                param.put("booking_close_time", booking_close_time);
                Log.e("Param", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getCoachDete() {
        //progressDialog = Global.getProgressDialog(getActivity(), CCResource.getString(getActivity(), R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_available_date", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Global.dismissDialog(progressDialog);
                Log.d("CoachdateAvailable", response);
                Gson gson = new Gson();

                if (!response.contains("false")) {
                    DateSlotes modelArrayList = gson.fromJson(response, DateSlotes.class);

                    if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                        days = new Calendar[modelArrayList.getData().size()];
                        events = new ArrayList<>();

                        for (int i = 0; i < modelArrayList.getData().size(); i++) {

                            try {
                                // days[i] = Global.convertStringToCalendar(modelArrayList.getData().get(i));
                                events.add(new EventDay(Global.convertStringToCalendar(modelArrayList.getData().get(i)), Global.getThreeDots(getActivity())));
                                datePicker.setEvents(events);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        // Toast.makeText(getActivity(), modelArrayList.getErrors().getErrorText(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("coach_id", "1209");
                param.put("month", month);
                param.put("year", year);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private boolean checkValidation() {

//        if (dateGot.equalsIgnoreCase("")) {
//            Toaster.customToast(getResources().getString(R.string.Please_select_date_first));
//            return false;
//        }
//        else
            if (selectedTimeSlot.equalsIgnoreCase("") && selectedTimeSlotUpdated.equalsIgnoreCase("")) {
            Toaster.customToast(getResources().getString(R.string.Please_select_time_first));
            return false;
        }
        return true;
    }

    public static String toCSV(String[] array) {
        String result = "";
        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }

    public void getUsersDetails() {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_user_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("getUserDetails", response);
                        //loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                JSONObject object = jsonObject.getJSONObject("coach_data");

                                if (object.has("coach_available_date")) {
                                    JSONArray jsonObject_personal_info = object.getJSONArray("coach_available_date");
                                    setData(jsonObject_personal_info);
                                }

                                if (object.has("coach_booking_close_time")) {

                                    booking_close_time = object.getString("coach_booking_close_time");
                                    selectedCloseLimit = "Booking session should be closed before " + booking_close_time + " hours";
                                    spinerCurency.setText(selectedCloseLimit);
                                    //Toaster.customToast(closeTime);
                                }

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
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
                        // loaderView.hideLoader();
                        Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("user_profile_id", SessionManager.get_user_id(prefs));
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

    private void setData(JSONArray data) {

        if (data != null) {
            updateJsonArray = data;
            try {
                JSONObject jsonObject = null;
                String avl_date = "";
                JSONObject jsonObjectUp = null;

                ArrayList<String> toArray = new ArrayList<>();
                for (int i = 0; i < data.length(); i++) {
                    jsonObject = data.getJSONObject(i);

                    if (jsonObject.has("available_date")) {
                        avl_date = jsonObject.getString("available_date");
                        updatedDateList.add(avl_date);
                        JSONArray jsonArray = jsonObject.getJSONArray("slot_id");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            updatedTimeSlot timeSlot = new updatedTimeSlot(jsonObject1);
                            toArray.add(timeSlot.getSlotId());
                            //...new code should be here...
                        }

                        String[] newName = toArray.toArray(new String[toArray.size()]);
                       selectedTimeSlotUpdated = toCSV(newName);
//                        jsonObjectUp = new JSONObject();
//                        jsonObjectUp.put(Global.getDateGotCoachh(avl_date), selectedTimeSlot);
//                        jsonArrayUp.put(jsonObjectUp);
//                        updatedDateList.add(avl_date);
                    }
                }

                // Log.d("newS",jsonArrayUp.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }


            year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());
            day = Global.getDay(datePicker.getCurrentPageDate().getTime().toString());
            printDatesInMonth(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

        }

    }

    private void getSessionCloseTime() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + Global.SESSION_CLOSE_TIME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SessionCloseTime", response);
                try {


                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                        JSONArray jsonObject1 = jsonObject.getJSONArray("data");
                        if(option_currency.size()>0){
                            option_currency.clear();
                        }
                        for (int i = 0; i < jsonObject1.length(); i++) {
                            JSONObject jsonObject2 = jsonObject1.getJSONObject(i);
                            option_currency.add(new com.pb.criconet.Utills.DataModel(jsonObject2.getString("msg"), jsonObject2.getString("id")));
                            spinerCurency.setOptionList(option_currency);
                        }


//                        option_currency.add(new com.pb.criconet.Utills.DataModel("Booking session should be closed before 4 hours"));
//                        option_currency.add(new com.pb.criconet.Utills.DataModel("Booking session should be closed before 6 hours"));
//                        option_currency.add(new com.pb.criconet.Utills.DataModel("Booking session should be closed before 8 hours"));
//                        spinerCurency.setOptionList(option_currency);

                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Toaster.customToast(getResources().getString(R.string.socket_timeout));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //Global.msgDialog((Activity) mActivity, "Error from server");
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void refreshData(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ft.detach(this).commitNow();
            ft.attach(this).commitNow();
        } else {
            ft.detach(this).attach(this).commit();
        }

    }

}
