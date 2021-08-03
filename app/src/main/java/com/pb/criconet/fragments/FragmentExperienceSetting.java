package com.pb.criconet.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.gson.Gson;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CCResource;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.adapters.ButtonAdapter;
import com.pb.criconet.models.BookCoach;
import com.pb.criconet.models.City;
import com.pb.criconet.models.DataModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class FragmentExperienceSetting extends Fragment implements ButtonAdapter.ItemListener, AdapterView.OnItemSelectedListener{
    private View rootView;
    private RequestQueue queue;
    private ProgressDialog progress;
    private SharedPreferences prefs;
    private RecyclerView recyclerView;
    private DataModel modelArrayList;
    private Spinner spinerYear;
    private Spinner spinerMonth;
    private Spinner spinerCurency;
    private String mcuurency="";
    private String expMonth="";
    private String expYear="";
    private EditText etAnyOtherInformation;
    private EditText etProfileTitle;
    private EditText etAmount;
    private Button btnSave;
    private List<DataModel.Datum> mValue;
    private StringBuilder categoryId;

    private String []curency={"₹ INR", "$ USD","€ EUR"};
    private String [] year={"Select Year", "0 year","1 year","2 year","3 year","4 year","5 year","6 year","7 year","8 year","9 year",
    "10 year","11 year","12 year","13 year","14 year","15 year","16 year","17 year","18 year","19 year","20 year","21 year","22 year",
    "23 year","24 year","25 year","26 year","27 year","28 year","29 year"};
    private String [] month={"Select Month", "0 month","1 month","2 month","3 month","4 month","5 month","6 month","7 month","8 month",
    "9 month","10 month","11 month"};
    public static FragmentExperienceSetting newInstance() {
        return new FragmentExperienceSetting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_experience_setting, container, false);



        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        progress = new ProgressDialog(getActivity());
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        btnSave=rootView.findViewById(R.id.btnSave);
        etAmount=rootView.findViewById(R.id.etAmount);
        etProfileTitle=rootView.findViewById(R.id.etProfileTitle);
        etAnyOtherInformation=rootView.findViewById(R.id.etAnyOtherInformation);

        recyclerView=rootView.findViewById(R.id.recylerButton);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        spinerCurency=rootView.findViewById(R.id.spinerCurency);
        spinerMonth=rootView.findViewById(R.id.spinerMonth);
        spinerYear=rootView.findViewById(R.id.spinerYear);

        spinerCurency.setOnItemSelectedListener(this);
        spinerMonth.setOnItemSelectedListener(this);
        spinerYear.setOnItemSelectedListener(this);


        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, curency);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerCurency.setAdapter(aa);

        ArrayAdapter bb = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, year);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerYear.setAdapter(bb);

        ArrayAdapter cc = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, month);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerMonth.setAdapter(cc);

        btnSave.setOnClickListener(view -> {
            if(checkValidation()) {
                updateCoachExp();
            }

        });

        if (Global.isOnline(getActivity())) {
            getSpecialities();
        } else {
            Global.showDialog(getActivity());
        }
    }
    private boolean checkValidation(){

        if (categoryId == null) {
            Toast.makeText(getActivity(),"Please choose specialities",Toast.LENGTH_SHORT).show();
            return false;
        }else if(etProfileTitle.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(getActivity(),"Please enter profile title",Toast.LENGTH_SHORT).show();
            return false;
        }else if(expYear.equalsIgnoreCase("")){
            Toast.makeText(getActivity(),"Please select year",Toast.LENGTH_SHORT).show();
            return false;
        }else if(expMonth.equalsIgnoreCase("")){
            Toast.makeText(getActivity(),"Please select month",Toast.LENGTH_SHORT).show();
            return false;
        }else if(mcuurency.equalsIgnoreCase("")){
            Toast.makeText(getActivity(),"Please select currency",Toast.LENGTH_SHORT).show();
            return  false;
        }else if(etAmount.getText().toString().trim().equalsIgnoreCase("")){
            Toast.makeText(getActivity(),"Please enter amount",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private void getSpecialities() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + "get_specialities_cat", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                modelArrayList = gson.fromJson(response, DataModel.class);
                if(modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    recyclerView.setAdapter(new ButtonAdapter(getActivity(),modelArrayList.getData(),FragmentExperienceSetting.this));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(getActivity(), "Error from server");
            }
        }) ;
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void onItemClick(List<DataModel.Datum> items) {
     //mValue=item;

        categoryId = new StringBuilder();
        String prefix = "";
        for (DataModel.Datum item : modelArrayList.getData()) {
            if (item.isCheck()) {
                categoryId.append(prefix);
                prefix = ",";
                categoryId.append(item.getId());

            }
        }
    }

    private void updateCoachExp() {

        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "update_coach_data", new Response.Listener<String>() {
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
                Global.msgDialog(getActivity(), "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("profile_title", etProfileTitle.getText().toString().trim());
                param.put("exp_years", expYear);
                param.put("exp_months", expMonth);
                param.put("about_coach_profile", etAnyOtherInformation.getText().toString().trim());
                param.put("cuurency_code", mcuurency);
                param.put("charge_amount", etAmount.getText().toString().trim());
                param.put("coach_category_id", etAmount.getText().toString().trim());
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView==spinerCurency && i!=0) {
            mcuurency=adapterView.getSelectedItem().toString();
        }else if(adapterView==spinerYear && i!=0){
            expYear=adapterView.getSelectedItem().toString();
        }else if(adapterView==spinerMonth && i!=0){
            expMonth=adapterView.getSelectedItem().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
