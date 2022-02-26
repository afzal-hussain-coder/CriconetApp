package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.FilterCoachCloseTimeDropDownView;
import com.pb.criconet.Utills.FilterCoachSelectionDropDownView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.GridSpacingItemDecoration;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.TimeAdapter;
import com.pb.criconet.event.SlotId;
import com.pb.criconet.fragments.FragmentAvility;
import com.pb.criconet.fragments.TimePreodee;
import com.pb.criconet.models.BookCoach;
import com.pb.criconet.models.DateSlotes;
import com.pb.criconet.models.TimeSlot;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SNIHostName;

import timber.log.Timber;

public class CoachSloatAvailabilityActivity extends BaseActivity implements TimePreodee.selectedSlot {
    private SharedPreferences prefs;
    private RequestQueue queue;
    private ProgressDialog progress;
    private CalendarView datePicker;
    private String dateGott = "";
    private String year;
    private String month;
    private String day;
    private String dateGot="";
    private StringBuilder multiDate;
    private StringBuilder multiTime;
    private RecyclerView recyclerView;
    private TimeSlot modelArrayList;
    private Calendar[] days;
    List<EventDay> events;
    Context mContext;
    Activity mActivity;
    Button btn_login;
    private FilterCoachCloseTimeDropDownView spinerCurency;
    private ArrayList<com.pb.criconet.Utills.DataModel> option_currency = new ArrayList<>();
    String selectedCloseLimit="";
    String selectedTimeSlot="";
    Typeface typeface;
    CustomLoaderView loaderView;
    String booking_close_time="";
    Date previousDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_sloat_availability);
        mContext = this;
        mActivity= this;
        loaderView = CustomLoaderView.initialize(this);
        typeface = ResourcesCompat.getFont(mContext, R.font.opensans_semibold);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
//            if(from_where.equalsIgnoreCase("2")){
//
//                Intent intent = new Intent(mActivity, Settings.class);
//                startActivity(intent);
//                finish();
//            }else if(from_where.equalsIgnoreCase("3")){
//                Intent intent = new Intent(mActivity, MainActivity.class);
//                startActivity(intent);
//
//            }
//            else{
//
//                Intent intent = new Intent(mActivity, ProfileActivity.class);
//                startActivity(intent);
//                finish();
//            }
        });

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(getResources().getString(R.string.Available_Date_Session));

        btn_login =  findViewById(R.id.btn_login);
        datePicker =  findViewById(R.id.calendarView);
        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        queue = Volley.newRequestQueue(mActivity);
        spinerCurency = findViewById(R.id.spinerCurency);
        option_currency.add(new com.pb.criconet.Utills.DataModel("Select booking session close time"));
        option_currency.add(new com.pb.criconet.Utills.DataModel("Booking session should be closed before 4 hours"));
        option_currency.add(new com.pb.criconet.Utills.DataModel("Booking session should be closed before 6 hours"));
        option_currency.add(new com.pb.criconet.Utills.DataModel("Booking session should be closed before 8 hours"));
        spinerCurency.setOptionList(option_currency);
//    selectedCloseLimit = option_currency.get(0).getName();
        spinerCurency.setText(option_currency.get(0).getName());
        spinerCurency.setTypeface(typeface);
        spinerCurency.setClickListener(new FilterCoachCloseTimeDropDownView.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {
                selectedCloseLimit = name;

                if(selectedCloseLimit.contains("4")){
                    booking_close_time="4";
                }else if(selectedCloseLimit.contains("6")){
                    booking_close_time="6";
                }else{
                    booking_close_time="8";
                }


                //Toaster.customToast(selectedCloseLimit);
            }


            @Override
            public void onDismiss() {
            }
        });


        year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
        month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());

        printDatesInMonth(Integer.parseInt(year),Integer.parseInt(month));
        // disable dates before today
        Calendar min = Calendar.getInstance();
        previousDate = min.getTime();
        min.add(Calendar.DAY_OF_MONTH, -1);
        datePicker.setMinimumDate(min);
        //int daysInMonth = min.getActualMaximum(Calendar.DAY_OF_MONTH);


        datePicker.setOnDayClickListener(eventDay -> {
            dateGot=Global.getDateGotCoach(eventDay.getCalendar().getTime().toString());

            if(Global.getDateGot(eventDay.getCalendar().getTime().toString()).equals(Global.getDateGot(previousDate.toString()))||eventDay.getCalendar().getTime().compareTo(previousDate)>0){
                if (Global.isOnline(mActivity)) {
                    getDateSlote(dateGot);
                } else {
                    Global.showDialog(mActivity);
                }
            }
            else{
                Toaster.customToast("Select enabled date");
            }


        });
        //dateGot=Global.getDateGot(datePicker.getCurrentPageDate().getTime().toString());

        //getDateSlote(dateGot);
       // getCoachDete();

        datePicker.setOnForwardPageChangeListener(() -> {
            year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());
           // getCoachDete();
            printDatesInMonth(Integer.parseInt(year),Integer.parseInt(month));
            dateGot=Global.getDateGotCoach(datePicker.getCurrentPageDate().getTime().toString());
            //getDateSlote(dateGot);
        });

        datePicker.setOnPreviousPageChangeListener(() -> {
            year = Global.getYear(datePicker.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(datePicker.getCurrentPageDate().getTime().toString());
            //getCoachDete();
            printDatesInMonth(Integer.parseInt(year),Integer.parseInt(month));
            dateGot=Global.getDateGotCoach(datePicker.getCurrentPageDate().getTime().toString());
            //getDateSlote(dateGot);
        });

        btn_login.setOnClickListener(view -> {
            multiDate=new StringBuilder();
            multiTime=new StringBuilder();

//            Calendar calendar =datePicker.getSelectedDates();
//
//            Global.getDateGot(calendar.getTime().toString());
//
//            String prefix = "";
//            for (Calendar calendar : datePicker.getSelectedDates()) {
//                multiDate.append(prefix);
//                prefix = ",";
//                multiDate.append(Global.getDateGot(calendar.getTime().toString()));
//            }
//
//            String prefix1 = "";
//            for (TimeSlot.Datum  data : modelArrayList.getData()) {
//                if(data.isActive()) {
//                    multiTime.append(prefix1);
//                    prefix1 = ",";
//                    multiTime.append(data.getSlotId());
//                }
//            }
            if(checkValidation()){
                updateCoachAvailability();
            }

        });

    }

    public void printDatesInMonth(int year, int month) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month - 1, 0);
        events = new ArrayList<>();
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < daysInMonth; i++) {
            System.out.println(fmt.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, 1);
            events.add(new EventDay(Global.convertStringToCalendar(fmt.format(cal.getTime())), Global.getThreeDots(mActivity)));
            datePicker.setEvents(events);
        }
    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    private void getDateSlote(String dateGot) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_time_slot_data", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("slotResponse",response);
                Gson gson = new Gson();
                modelArrayList = gson.fromJson(response, TimeSlot.class);
                if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    navigationController.showTimePreodee(modelArrayList);
                } else {
                    dateGott="";
                    Toaster.customToast(modelArrayList.getErrors().getErrorText());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
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
                Log.d("SlotSubmitResponse",response);

                try {
                    JSONObject jsonObject= new JSONObject(response);
                    JSONObject data=null;
                    if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {
                        data=jsonObject.getJSONObject("data");
                        Toaster.customToast(data.getString("message"));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(mActivity, CoachProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },2000);

                    } else {
                        Toaster.customToast(data.getString("error"));
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
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("date_slot", dateGot);
                param.put("time_slot", selectedTimeSlot);
                param.put("booking_close_time", booking_close_time);

                Log.e("Param",param.toString());
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
                Log.d("CoachdateAvailable",response);
                Gson gson = new Gson();

                if(!response.contains("false")) {
                    DateSlotes modelArrayList = gson.fromJson(response, DateSlotes.class);

                    if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                        days = new Calendar[modelArrayList.getData().size()];
                        events = new ArrayList<>();


                        for (int i = 0; i < modelArrayList.getData().size(); i++) {

                            try {
                               // days[i] = Global.convertStringToCalendar(modelArrayList.getData().get(i));
                                events.add(new EventDay(Global.convertStringToCalendar(modelArrayList.getData().get(i)), Global.getThreeDots(mActivity)));
                                datePicker.setEvents(events);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        // Toast.makeText(mActivity, modelArrayList.getErrors().getErrorText(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
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

    private boolean checkValidation(){

        if(dateGot.equalsIgnoreCase("")){
            Toaster.customToast(getResources().getString(R.string.Please_select_date_first));
            return false;
        }else if(selectedCloseLimit.equalsIgnoreCase("")){
            Toaster.customToast(getResources().getString(R.string.Booking_Session_Close_Timee));
            return false;
        }
        return true;
    }

    @Override
    public void getSelectedSlot(ArrayList<String> arrayListUser) {

        String[] namesArr = arrayListUser.toArray(new String[arrayListUser.size()]);

        selectedTimeSlot =toCSV(namesArr);

        //Toaster.customToast(selectedTimeSlot);

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


//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    protected void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }
    
}