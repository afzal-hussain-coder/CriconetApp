package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.FilterCoachSelectionDropDownView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.MultipartRequest;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.ButtonAdapter;
import com.pb.criconet.fragments.FragmentExperienceSetting;
import com.pb.criconet.models.DataModel;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class CoachProfessionalInfoActivity extends AppCompatActivity implements ButtonAdapter.ItemListener{
    Context mContext;
    Activity mActivity;
    private View rootView;
    private RequestQueue queue;
    private ProgressDialog progress;
    private SharedPreferences prefs;
    private RecyclerView recyclerView;
    private DataModel modelArrayList;
    private FilterCoachSelectionDropDownView spinerYear;
    private FilterCoachSelectionDropDownView spinerMonth;
    private FilterCoachSelectionDropDownView spinerCurency;
    private String mcuurency = "";
    private String expMonth = "";
    private String expYear = "";
    private EditText etAnyOtherInformation;
    private EditText etProfileTitle;
    private EditText etAchievement;
    private EditText etwhat_you_teach;
    private EditText etSkills_Student_Learns;
    private EditText etcertificate_title;
    private ImageView iv_upload_certificate;
    private FrameLayout fl_click_to_upload_certificate;
    private LinearLayout li_by_signingup;
    private TextView cv_tearms;
    private TextView tvTearms;
    private TextView tvPrivacy;
    private EditText etAmount;
    private Button btn_save_continioue;
    private List<DataModel.Datum> mValue;
    private StringBuilder categoryId;


    private String profileTitle = "", achievement = "", whatYouTeach = "", skillsStudentLearns = "", otherInformation = "", certificateTitle = "", amount = "";
    private ArrayList<com.pb.criconet.Utills.DataModel> option = new ArrayList<>();
    private ArrayList<com.pb.criconet.Utills.DataModel> option_month = new ArrayList<>();
    private ArrayList<com.pb.criconet.Utills.DataModel> option_currency = new ArrayList<>();
    String selectedYear = "", selectedMonth = "", selectedCurency;
    Typeface typeface;
    CustomLoaderView loaderView;
    private static final int CAMERA_REQUESTid = 2015;
    private static final int PICK_IMAGEid = 100;
    private Cursor cursor;
    private int columnindex, i;
    private Uri URIid = null;
    private Uri selectedImageid, mCapturedImageURIid;
    private String file_pathid = "";
    private String imagepath = "";
    private String img_type = "";
    private SlideUp slideUp;
    private View dim, rootViewPost;
    private View slideView;
    private TextView tv_camera;
    private TextView tvGallery;
    private TextView tvCancel;
    private TextView tv_choose;
    private ArrayList<String> catIdList;
    private String isTeramsChecked="";
    private TextView tv_click_uploadCertificate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_professional_info);
        mActivity=this;
        mContext = this;
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
        mTitle.setText(getResources().getString(R.string.Professional_Qualifications));

        typeface = ResourcesCompat.getFont(mContext, R.font.opensans_semibold);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        loaderView = CustomLoaderView.initialize(mContext);
        queue = Volley.newRequestQueue(mContext);

        btn_save_continioue = findViewById(R.id.btn_save_continioue);
        etAmount = findViewById(R.id.etAmount);
        etProfileTitle = findViewById(R.id.etProfileTitle);
        etAchievement = findViewById(R.id.etAchievement);
        etwhat_you_teach = findViewById(R.id.etwhat_you_teach);
        etSkills_Student_Learns = findViewById(R.id.etSkills_Student_Learns);
        etAnyOtherInformation = findViewById(R.id.etAnyOtherInformation);
        etcertificate_title = findViewById(R.id.etcertificate_title);
        iv_upload_certificate = findViewById(R.id.iv_upload_certificate);
        fl_click_to_upload_certificate = findViewById(R.id.fl_click_to_upload_certificate);
        tv_click_uploadCertificate = findViewById(R.id.tv_click_uploadCertificate);
        li_by_signingup = findViewById(R.id.li_by_signingup);
        cv_tearms = findViewById(R.id.cv_tearms);
        cv_tearms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    isTeramsChecked="1";
                } else {
                    isTeramsChecked="";
                }
            }
        });
        tvTearms = findViewById(R.id.tvTearms);
        tvTearms.setOnClickListener(view -> {
            String url = "https:\\/\\/www.criconet.com\\/terms\\/terms?rst=app";
            startActivity(new Intent(mContext, WebViewCoachRegisterTermsActivity.class).putExtra("URL", url).putExtra("title", "Terms of Use"));
            //finish();
        });
        tvPrivacy = findViewById(R.id.tvPrivacy);
        tvPrivacy.setOnClickListener(view -> {
            String url ="https:\\/\\/www.criconet.com\\/terms\\/privacy-policy?rst=app";
            startActivity(new Intent(mContext, CoachRegisterPrivacyWebView.class).putExtra("URL", url).putExtra("title", "Privacy Policy"));
            //finish();
        });

        recyclerView = findViewById(R.id.recylerButton);
        GridLayoutManager manager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        spinerCurency = findViewById(R.id.spinerCurency);
        option_currency.add(new com.pb.criconet.Utills.DataModel("₹ INR"));
//        option_currency.add(new com.pb.criconet.Utills.DataModel("$ USD"));
//        option_currency.add(new com.pb.criconet.Utills.DataModel("€ EUR"));
        spinerCurency.setOptionList(option_currency);
        selectedCurency = option_currency.get(0).getName();
        spinerCurency.setText(selectedCurency);
        spinerCurency.setTypeface(typeface);
        spinerCurency.setClickListener(new FilterCoachSelectionDropDownView.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {
                selectedCurency = name;
            }


            @Override
            public void onDismiss() {
            }
        });

        spinerMonth = findViewById(R.id.spinerMonth);
        option_month.add(new com.pb.criconet.Utills.DataModel("Select Month"));
        option_month.add(new com.pb.criconet.Utills.DataModel("0 month"));
        option_month.add(new com.pb.criconet.Utills.DataModel("1 month"));
        option_month.add(new com.pb.criconet.Utills.DataModel("2 months"));
        option_month.add(new com.pb.criconet.Utills.DataModel("3 months"));
        option_month.add(new com.pb.criconet.Utills.DataModel("4 months"));
        option_month.add(new com.pb.criconet.Utills.DataModel("5 months"));
        option_month.add(new com.pb.criconet.Utills.DataModel("6 months"));
        option_month.add(new com.pb.criconet.Utills.DataModel("7 months"));
        option_month.add(new com.pb.criconet.Utills.DataModel("8 months"));
        option_month.add(new com.pb.criconet.Utills.DataModel("9 months"));
        option_month.add(new com.pb.criconet.Utills.DataModel("10 months"));
        option_month.add(new com.pb.criconet.Utills.DataModel("11 months"));
        spinerMonth.setOptionList(option_month);
        selectedMonth = option_month.get(0).getName();
        spinerMonth.setText(selectedMonth);
        spinerMonth.setTypeface(typeface);
        spinerMonth.setClickListener(new FilterCoachSelectionDropDownView.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {
                if(name.contains("month")){
                    selectedMonth = name.replace("month","").trim();
                }if(name.contains("months")){
                    selectedMonth = name.replace("months","").trim();
                }
            }

            @Override
            public void onDismiss() {
            }
        });

        spinerYear = findViewById(R.id.spinerYear);
        option.add(new com.pb.criconet.Utills.DataModel("Select Year"));
        option.add(new com.pb.criconet.Utills.DataModel("0 year"));
        option.add(new com.pb.criconet.Utills.DataModel("1 year"));
        option.add(new com.pb.criconet.Utills.DataModel("2 years"));
        option.add(new com.pb.criconet.Utills.DataModel("3 years"));
        option.add(new com.pb.criconet.Utills.DataModel("4 years"));
        option.add(new com.pb.criconet.Utills.DataModel("5 years"));
        option.add(new com.pb.criconet.Utills.DataModel("6 years"));
        option.add(new com.pb.criconet.Utills.DataModel("7 years"));
        option.add(new com.pb.criconet.Utills.DataModel("8 years"));
        option.add(new com.pb.criconet.Utills.DataModel("9 years"));
        option.add(new com.pb.criconet.Utills.DataModel("10 years"));
        option.add(new com.pb.criconet.Utills.DataModel("11 years"));
        option.add(new com.pb.criconet.Utills.DataModel("12 years"));
        option.add(new com.pb.criconet.Utills.DataModel("13 years"));
        option.add(new com.pb.criconet.Utills.DataModel("14 years"));
        option.add(new com.pb.criconet.Utills.DataModel("15 years"));
        option.add(new com.pb.criconet.Utills.DataModel("16 years"));
        option.add(new com.pb.criconet.Utills.DataModel("17 years"));
        option.add(new com.pb.criconet.Utills.DataModel("18 years"));
        option.add(new com.pb.criconet.Utills.DataModel("19 years"));
        option.add(new com.pb.criconet.Utills.DataModel("20 years"));
        option.add(new com.pb.criconet.Utills.DataModel("21 years"));
        option.add(new com.pb.criconet.Utills.DataModel("22 years"));
        option.add(new com.pb.criconet.Utills.DataModel("23 years"));
        option.add(new com.pb.criconet.Utills.DataModel("24 years"));
        option.add(new com.pb.criconet.Utills.DataModel("25 years"));
        option.add(new com.pb.criconet.Utills.DataModel("26 years"));
        option.add(new com.pb.criconet.Utills.DataModel("27 years"));
        option.add(new com.pb.criconet.Utills.DataModel("28 years"));
        option.add(new com.pb.criconet.Utills.DataModel("29 years"));
        spinerYear.setOptionList(option);
        selectedYear = option.get(0).getName();
        spinerYear.setText(selectedYear);
        spinerYear.setTypeface(typeface);

        spinerYear.setClickListener(new FilterCoachSelectionDropDownView.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {
                if(name.contains("year")){
                    selectedYear = name.replace("year","").trim();
                }if(name.contains("years")){
                    selectedYear = name.replace("years","").trim();
                }
            }


            @Override
            public void onDismiss() {
            }
        });

        rootViewPost = findViewById(R.id.root_vieww);
        slideView = findViewById(R.id.slideView);
        dim = findViewById(R.id.dim);
        tv_camera = findViewById(R.id.tv_camera);
        tvGallery = findViewById(R.id.tvGallery);
        tvCancel = findViewById(R.id.tvCancel);
        tv_choose = findViewById(R.id.tv_choose);
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
                .withSlideFromOtherView(slideView)
                .build();
        tvCancel.setOnClickListener(v -> {
            slideUp.hide();
        });
        fl_click_to_upload_certificate.setOnClickListener(v -> {
            if(tv_click_uploadCertificate.getText().toString().trim().equalsIgnoreCase(mContext.getResources().getString(R.string.remove_certificate))){
                if (Global.isOnline(mContext)) {
                    removeCertificate();
                } else {
                    Global.showDialog(mActivity);
                }
            }else{
                selectImage();
            }

        });

        if (Global.isOnline(mContext)) {
            getSpecialities();
        } else {
            Global.showDialog(mActivity);
        }

        btn_save_continioue.setOnClickListener(view -> {
            if (checkValidation()) {
                if (Global.isOnline(mContext)) {
                    updateCoachExp(imagepath);
                } else {
                    Global.showDialog(mActivity);
                }
            }

        });

    }



    private void selectImage() {
        slideUp.show();
        tv_choose.setText(R.string.add_photo);
        tv_camera.setOnClickListener(v -> {
            slideUp.hide();
            openCameraid();
        });
        tvGallery.setOnClickListener(v -> {
            slideUp.hide();
            openGalleryid();
        });
        tvCancel.setOnClickListener(v -> {
            slideUp.hide();
        });
    }

    private void openCameraid() {

        try {
            String fileName = "profile.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURIid = mContext.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURIid);
            startActivityForResult(intent, CAMERA_REQUESTid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGalleryid() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGEid);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGEid) {
            try {
                selectedImageid = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                cursor = mContext.getContentResolver().query(selectedImageid, filePathColumn, null, null, null);
                cursor.moveToFirst();
                columnindex = cursor.getColumnIndex(filePathColumn[0]);
                file_pathid = cursor.getString(columnindex);
                // Log.e("Attachment Path:", attachmentFile);
                URIid = Uri.parse("file://" + file_pathid);
                imagepath = file_pathid;

                cursor.close();

                if (resultCode == 0) {
//                    dialog_camera.dismiss();
                } else {
//                    dialog_camera.dismiss();
                    System.out.println("cccccccc   " + imagepath);
                    iv_upload_certificate.setImageURI(Uri.parse(imagepath));
                    //updateImageTask(imagepath);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == CAMERA_REQUESTid) {
            try {
                String[] projection = {MediaStore.Images.Media.DATA};
                @SuppressWarnings("deprecation")
                Cursor cursor = mActivity.managedQuery(mCapturedImageURIid, projection, null, null, null);
                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String capturedImageFilePath = cursor.getString(column_index_data);
                imagepath = capturedImageFilePath;

                if (resultCode == 0) {
//                    dialog_camera.dismiss();
                } else {
                    System.out.println("cccccccc   " + imagepath);
                    iv_upload_certificate.setImageURI(Uri.parse(imagepath));

                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private boolean checkValidation() {
        profileTitle = etProfileTitle.getText().toString().trim();
        achievement = etAchievement.getText().toString().trim();
        whatYouTeach = etwhat_you_teach.getText().toString().trim();
        skillsStudentLearns = etSkills_Student_Learns.getText().toString().trim();
        otherInformation = etAnyOtherInformation.getText().toString().trim();
        certificateTitle = etcertificate_title.getText().toString().trim();
        amount = etAmount.getText().toString().trim();

        //Log.d("CtId",categoryId+"");

        if (categoryId == null) {
            Toaster.customToast(mContext.getResources().getString(R.string.Please_choose_your_specialities));
            return false;
        } else if (!Global.validateLengthofCoachRegister(etProfileTitle.getText().toString())) {
            Toaster.customToast(mContext.getResources().getString(R.string.Fill_your_profile_title));
            return false;
        } else if (selectedYear.equalsIgnoreCase("Select Year") || selectedMonth.equalsIgnoreCase("Select Month")) {
            Toaster.customToast(mContext.getResources().getString(R.string.Select_year_and_month));
            return false;
        }else if(selectedYear.equalsIgnoreCase("0") && selectedMonth.equalsIgnoreCase("0")){
            Toaster.customToast(mContext.getResources().getString(R.string.Select_month));
            return false;
        }
        /*else if(selectedMonth.equalsIgnoreCase("0")){
            Toaster.customToast(mContext.getResources().getString(R.string.Select_month));
            return false;
        }*/
//        else if(mcuurency.equalsIgnoreCase("")){
//            Toast.makeText(mContext,"Please select currency",Toast.LENGTH_SHORT).show();
//            return  false;
//        }
        else if(amount.isEmpty()){
            Toaster.customToast(mContext.getResources().getString(R.string.Enter_Amount_session));
            return false;
        }
        else if (amount.equalsIgnoreCase("0") || Float.parseFloat(amount)<1.0000) {
            Toaster.customToast(mContext.getResources().getString(R.string.Enter_Amount_sessionn));
            return false;
        }
        else if(isTeramsChecked.equalsIgnoreCase("")){
            Toaster.customToast(mContext.getResources().getString(R.string.Please_check_tearms));
            return false;
        }

        return true;
    }
    private void getSpecialities() {
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + Global.GET_SPECIALITIES_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                modelArrayList = gson.fromJson(response, DataModel.class);
                if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    recyclerView.setAdapter(new ButtonAdapter(mContext, modelArrayList.getData(), CoachProfessionalInfoActivity.this));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Global.msgDialog(mActivity, "Error from server");
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void updateCoachExp(String path) {
        loaderView.showLoader();
        try {
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("user_id", new StringBody(SessionManager.get_user_id(prefs)));
            entity.addPart("s", new StringBody(SessionManager.get_session_id(prefs)));
            entity.addPart("profile_title", new StringBody(profileTitle));
            entity.addPart("coach_category_id", new StringBody(String.valueOf(categoryId)));
            entity.addPart("exp_years", new StringBody(selectedYear.trim()));
            entity.addPart("exp_months", new StringBody(selectedMonth.trim()));
            entity.addPart("about_coach_profile", new StringBody(otherInformation));
            entity.addPart("cuurency_code", new StringBody(selectedCurency));
            entity.addPart("charge_amount", new StringBody(amount));
            entity.addPart("certificate_title", new StringBody(certificateTitle));
            entity.addPart("what_you_teach", new StringBody(whatYouTeach));
            entity.addPart("skills_you_learn", new StringBody(skillsStudentLearns));
            entity.addPart("achievement", new StringBody(achievement));
            entity.addPart("is_agree", new StringBody(isTeramsChecked));
            if (!(path.equals("") || path == null)) {
                File file = new File(path);
                FileBody fileBody = new FileBody(file);
                entity.addPart("certificate_path", fileBody);
            }
            MultipartRequest req = new MultipartRequest(Global.URL + Global.ADD_COACH_QUALIFICATION, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Timber.d(response);
                        loaderView.hideLoader();
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                           //SessionManager.save_profileType(prefs,"coach");
                            if (jsonObject.has("data")) {
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                SessionManager.save_profiletype(prefs, jsonObjectData.getString("profile_type"));
                            }

                            Toaster.customToast(jsonObject.optString("msg"));

                            if(imagepath.isEmpty()){
                                tv_click_uploadCertificate.setText(mContext.getResources().getString(R.string.upload_certificate));
                            }else{
                                tv_click_uploadCertificate.setText(mContext.getResources().getString(R.string.remove_certificate));
                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(mContext, CoachSloatAvailabilityActivity.class);
                                    startActivity(intent);
                                }
                            },2000);

                            //getUsersDetails(SessionManager.get_user_id(prefs));

                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loaderView.hideLoader();
                    error.printStackTrace();
                }
            }, entity);
            Log.d("request",entity.toString());

            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);

            queue.add(req);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onItemClick(List<DataModel.Datum> items) {
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

    private void removeCertificate() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.REMOVE_CERTIFICATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponseCountry",response);
                try {
                    Timber.d(response);
                    loaderView.hideLoader();
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {

                        Toaster.customToast(jsonObject.optString("msg"));
                        tv_click_uploadCertificate.setText(mContext.getResources().getString(R.string.upload_certificate));
                        iv_upload_certificate.setImageURI(null);
                        //getUsersDetails(SessionManager.get_user_id(prefs));

                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}