package com.pb.criconet.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.google.gson.Gson;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.adapters.TimeAdapter;
import com.pb.criconet.event.SlotId;
import com.pb.criconet.models.BookCoach;
import com.pb.criconet.models.DateSlotes;
import com.pb.criconet.models.TimeSlot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class FragmentAvility extends Fragment implements TimeAdapter.timeSelect{
    private View rootView;
    private SharedPreferences prefs;
    private RequestQueue queue;
    private ProgressDialog progress;
    private CalendarView datePicker;
    private String year;
    private String month;
    private TextView btnCalender;
    private String dateGot="";
    private StringBuilder multiDate;
    private StringBuilder multiTime;
    private RecyclerView recyclerView;
    private TimeSlot modelArrayList;
    private Calendar[] days;
    List<EventDay> events;
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
        btnCalender =  rootView.findViewById(R.id.btnCalender);
        datePicker =  rootView.findViewById(R.id.calendarView);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);


        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        progress = new ProgressDialog(getActivity());
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);


        year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
        month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());


        // disable dates before today
        Calendar min = Calendar.getInstance();
        min.add(Calendar.DAY_OF_MONTH, -1);
        datePicker.setMinimumDate(min);

        datePicker.setOnDayClickListener(eventDay -> {
            dateGot=Global.getDateGot(eventDay.getCalendar().getTime().toString());
            if (Global.isOnline(getActivity())) {
                getDateSlote(dateGot);
            } else {
                Global.showDialog(getActivity());
            }
        });
        dateGot=Global.getDateGot(datePicker.getCurrentPageDate().getTime().toString());

        getDateSlote(dateGot);
        getCoachDete();

        datePicker.setOnForwardPageChangeListener(() -> {
            year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());
            getCoachDete();
            dateGot=Global.getDateGot(datePicker.getCurrentPageDate().getTime().toString());
            getDateSlote(dateGot);
        });

        datePicker.setOnPreviousPageChangeListener(() -> {
            year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());
            getCoachDete();
            dateGot=Global.getDateGot(datePicker.getCurrentPageDate().getTime().toString());
            getDateSlote(dateGot);
        });

        btnCalender.setOnClickListener(view -> {
            multiDate=new StringBuilder();
            multiTime=new StringBuilder();

            String prefix = "";
            for (Calendar calendar : datePicker.getSelectedDates()) {
                    multiDate.append(prefix);
                    prefix = ",";
                    multiDate.append(Global.getDateGot(calendar.getTime().toString()));
            }

            String prefix1 = "";
            for (TimeSlot.Datum  data : modelArrayList.getData()) {
                if(data.isActive()) {
                    multiTime.append(prefix1);
                    prefix1 = ",";
                    multiTime.append(data.getSlotId());
                }
            }
            if(checkValidation()){
                updateCoachAvailability();
            }

        });

    }

    private void getDateSlote(String dateGot) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_time_slot_data", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                 modelArrayList = gson.fromJson(response, TimeSlot.class);
                if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    recyclerView.setAdapter(new TimeAdapter(getActivity(), modelArrayList.getData(), FragmentAvility.this::getSlotId));
                } else {
                    Toast.makeText(getActivity(), modelArrayList.getErrors().getErrorText(), Toast.LENGTH_SHORT).show();
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
                param.put("date", dateGot);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void getSlotId(String sloteId) {
        EventBus.getDefault().post(new SlotId(sloteId,null));
    }



    private void updateCoachAvailability() {

        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "update_coach_availability_date", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                Gson gson = new Gson();
                BookCoach modelArrayList = gson.fromJson(response, BookCoach.class);
                if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), modelArrayList.getErrors().getErrorText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progress.dismiss();
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("date_slot", multiDate.toString());
                param.put("time_slot", multiTime.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getCoachDete() {
        //progressDialog = Global.getProgressDialog(mActivity, CCResource.getString(mActivity, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_coach_available_date", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Global.dismissDialog(progressDialog);
                Gson gson = new Gson();

                if(!response.contains("false")) {
                    DateSlotes modelArrayList = gson.fromJson(response, DateSlotes.class);

                    if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                        days = new Calendar[modelArrayList.getData().size()];
                        events = new ArrayList<>();
                        for (int i = 0; i < modelArrayList.getData().size(); i++) {

                            try {
                                days[i] = Global.convertStringToCalendar(modelArrayList.getData().get(i));
                                events.add(new EventDay(Global.convertStringToCalendar(modelArrayList.getData().get(i)), Global.getThreeDots(getActivity())));
                                datePicker.setEvents(events);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), modelArrayList.getErrors().getErrorText(), Toast.LENGTH_SHORT).show();
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
                param.put("coach_id", SessionManager.get_user_id(prefs));
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

    private boolean checkValidation(){

        if(multiDate.length()==0){
           Toast.makeText(getActivity(),getResources().getString(R.string.Please_select_date_first),Toast.LENGTH_SHORT).show();
            return false;
        }else if(multiTime.length()==0){
            Toast.makeText(getActivity(),getResources().getString(R.string.Please_select_time_first),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
