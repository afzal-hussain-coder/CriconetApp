package com.pb.criconet.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.mukesh.OtpView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.MultipartRequest;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.models.City;
import com.pb.criconet.models.CoachLanguage;
import com.pb.criconet.models.Country;
import com.pb.criconet.models.Language;
import com.pb.criconet.models.States;
import com.pb.criconet.models.UserData;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class CoachPersonalInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Context mContext;
    Activity mActivity;
    private static final int CAMERA_REQUESTid = 2015;
    private static final int PICK_IMAGEid = 100;
    private SharedPreferences prefs;
    private String fname_String, mname_String, lname_String, gender_String, countryID = "", stateID = "", cityID = "", address1, address2, pincode, mobile;
    private EditText etAddress2, edttxt_fname, edttxt_lname, etAddress, edttxt_birthday, edttxt_phone, etPincode,
            edttxt_fb_profileLink, edttxt_twitter_profile, edttxt_linkind_profile, edttxt_instagram_profile, edttxt_youtube_profile;
    private Spinner spn_language;
    private Spinner spinnerCountry;
    private Spinner spinnerState;
    private Spinner spinnerCity;
    private ImageView profile_image, cover_img, imageView;
    private RequestQueue queue;
    //    private ProgressWheel progress_wheel;
    private Button btn_login;
    private Cursor cursor;
    private int columnindex, i;
    private Uri URIid = null;
    private Uri selectedImageid, mCapturedImageURIid;
    private String file_pathid = "", image_pathid = "", phoneNumber;
    private String imagepath = "";
    private String img_type = "";

    private UserData userData;
    private ProgressDialog progress;
    private Country modelArrayList;
    private City citymodelArrayList;
    private States statemodelArrayList;
    private Language languageModelArrayList;
    private SlideUp slideUp;
    private View dim, rootViewPost;
    private View slideView;
    private TextView tv_camera;
    private TextView tvGallery;
    private TextView tvCancel;
    private TextView tv_choose;
    EditText edttxt_Mname;
    RelativeLayout middle;
    CustomLoaderView loaderView;

    TextView textView_language;
    boolean[] selectedLanguage;
    ArrayList<Integer> langList = new ArrayList<>();
    //ArrayList<String> language=null;
    String[] langArray = null;
    private OtpView otpView;
    private String from_where = "";
    String state_Name = "";
    String city_Name = "";
    String countryName = "";
    String twitter = "";
    String facebook = "";
    String linkedin = "";
    String instagram = "";
    String youtube = "";
    ArrayList<String> coachLanguages = new ArrayList<>();
    ArrayList<String> languageArray_new = new ArrayList<>();
    Dialog dialog;
    private StringBuilder langStringBuilder;
    ArrayList<Language.Datum> language = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_personal_info);
        mActivity = this;
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_select_language);
        dialog.setCancelable(false);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(getResources().getString(R.string.Personal_Information));

        prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        loaderView = CustomLoaderView.initialize(mActivity);
        queue = Volley.newRequestQueue(mActivity);
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

        edttxt_fname = findViewById(R.id.edttxt_fname);
        edttxt_Mname = findViewById(R.id.edttxt_Mname);
        edttxt_lname = findViewById(R.id.edttxt_lname);
        etAddress = findViewById(R.id.etAddress);
        etAddress2 = findViewById(R.id.etAddress2);
        edttxt_phone = findViewById(R.id.edttxt_phone);
        etPincode = findViewById(R.id.etPincode);
        edttxt_fb_profileLink = findViewById(R.id.edttxt_fb_profileLink);
        edttxt_twitter_profile = findViewById(R.id.edttxt_twitter_profile);
        edttxt_linkind_profile = findViewById(R.id.edttxt_linkind_profile);
        edttxt_instagram_profile = findViewById(R.id.edttxt_instagram_profile);
        edttxt_youtube_profile = findViewById(R.id.edttxt_youtube_profile);
        textView_language = findViewById(R.id.textView_language);
        spn_language = findViewById(R.id.spn_language);
        spinnerCountry = findViewById(R.id.spinerCountry);
        spinnerState = findViewById(R.id.spinerState);
        spinnerCity = findViewById(R.id.spinerCity);
        profile_image = findViewById(R.id.profile_pic);
        middle = findViewById(R.id.middle);
        cover_img = findViewById(R.id.cover_img);
        imageView = findViewById(R.id.imageView);
        btn_login = findViewById(R.id.btn_login);

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

        spinnerCountry.setOnItemSelectedListener(this);
        spinnerState.setOnItemSelectedListener(this);
        spinnerCity.setOnItemSelectedListener(this);


        if (Global.isOnline(mActivity)) {
            getCountry();
            getUsersDetails();
            getLanguage();
        } else {
            Global.showDialog(mActivity);
        }


        middle.setOnClickListener(v -> {
            img_type = "profile";
            selectImage();
        });

        cover_img.setOnClickListener(v -> {
            img_type = "cover";
            selectImage();
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(btn_login.getText().toString().trim().equalsIgnoreCase("Click to Edit Profile")){
//                    setEnable();
//                }else {
//
//                }
                checkMethod();
            }
        });

        textView_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

                // Initialize alert dialog
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                // set title
//                builder.setTitle(getActivity().getResources().getString(R.string.Select_Language_You_Speak));
//
//                // set dialog non cancelable
//                builder.setCancelable(false);
//
//
//                builder.setMultiChoiceItems(langArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
//                        // check condition
//                        if (b) {
//                            // when checkbox selected
//                            // Add position  in lang list
//                            Log.d("Index",i+"");
//                            langList.add(i);
//                            // Sort array list
//                            //Collections.sort(langList);
//                        } else {
//
//                            if (langList.contains(i)) {
//                                langList.remove((Integer) i);
//                            }
//
//                            //langList.remove(langArray);
//                            // when checkbox unselected
//                            // Remove position from langList
//
//                        }
//                    }
//                });
//
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // Initialize string builder
//                        StringBuilder stringBuilder = new StringBuilder();
//                        // use for loop
//                        for (int j = 0; j < langList.size(); j++) {
//                            // concat array value
//                            stringBuilder.append(langArray[langList.get(j)]);
//                            // check condition
//                            if (j != langList.size() - 1) {
//                                // When j value  not equal
//                                // to lang list size - 1
//                                // add comma
//                                stringBuilder.append(",");
//                            }
//                        }
//                        // set text on textView
//                        textView_language.setText(stringBuilder.toString());
//
//                    }
//                });
//
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // dismiss dialog
//                        dialogInterface.dismiss();
//                    }
//                });
//                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // use for loop
//                        for (int j = 0; j < selectedLanguage.length; j++) {
//                            // remove all selection
//                            selectedLanguage[j] = false;
//                            // clear language list
//                            langList.clear();
//                            // clear text view value
//                            textView_language.setText("");
//                        }
//                    }
//                });
//                // show dialog
//                builder.show();
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

    private void checkMethod() {
        fname_String = edttxt_fname.getText().toString().trim();
        mname_String = edttxt_Mname.getText().toString().trim();
        lname_String = edttxt_lname.getText().toString().trim();
        address1 = etAddress.getText().toString().trim();
        address2 = etAddress2.getText().toString().trim();
        gender_String = spn_language.getSelectedItem().toString();
        pincode = etPincode.getText().toString().trim();
        mobile = edttxt_phone.getText().toString().trim();
        facebook = edttxt_fb_profileLink.getText().toString().trim();
        twitter = edttxt_twitter_profile.getText().toString().trim();
        instagram = edttxt_instagram_profile.getText().toString().trim();
        linkedin = edttxt_linkind_profile.getText().toString().trim();
        youtube = edttxt_youtube_profile.getText().toString().trim();

        if (!Global.validateLengthofCoachRegister(fname_String)) {
            Toaster.customToast(getResources().getString(R.string.Enter_First_Name));
        }
//        else if (!Global.validateLength(mname_String, 3)) {
//            Toaster.customToast(getResources().getString(R.string.Enter_Middle_Name_at_least_3_character));
//        }
        else if (!Global.validateLengthofCoachRegister(lname_String)) {
            Toaster.customToast(getResources().getString(R.string.Enter_Last_Name));
        } else if (textView_language.getText().equals("")) {
            Toaster.customToast(getResources().getString(R.string.select_language));
        } else if (languageArray_new.size() > 5) {
            Toaster.customToast(getResources().getString(R.string.you_can_only_select_five_item));
        } else if (countryID.equalsIgnoreCase("") || spinnerCountry.getSelectedItem().equals("Country")) {
            Toaster.customToast(getResources().getString(R.string.Select_Country));
        }
//        else if(stateID.equalsIgnoreCase("")){
        //Toaster.customToast(getResources().getString(R.string.Select_City));
//        }else if(cityID.equalsIgnoreCase("")){
        // Toaster.customToast(getResources().getString(R.string.Select_State));
//        }
        else if (address1.isEmpty()) {
            Toaster.customToast(getResources().getString(R.string.Enter_your_address));
        }
//        else if(!Global.validateLengthofCoachRegisterr(address1)){
//            Toaster.customToast(getResources().getString(R.string.Enter_your_addresss));
//        }
//        else if(!Global.validateLength(address2, 3)){
//            Toaster.customToast(getResources().getString(R.string.enter_landmark));
//        }
        else if (pincode.isEmpty()) {
            Toaster.customToast(getResources().getString(R.string.enter_pincode));
        } else if (pincode.equalsIgnoreCase("111111") || pincode.equalsIgnoreCase("222222") ||
                pincode.equalsIgnoreCase("333333") || pincode.equalsIgnoreCase("444444") ||
                pincode.equalsIgnoreCase("555555") || pincode.equalsIgnoreCase("666666") ||
                pincode.equalsIgnoreCase("777777") || pincode.equalsIgnoreCase("888888") ||
                pincode.equalsIgnoreCase("999999") || pincode.equalsIgnoreCase("000000")) {
            Toaster.customToast(getResources().getString(R.string.enter_pincodee));
        } else if (!Global.isValidPincode(pincode)) {
            Toaster.customToast(getResources().getString(R.string.enter_pincodee));
        } else if (mobile.isEmpty()) {
            Toaster.customToast(getResources().getString(R.string.Enter_your_phone_number));
        } else if (!Global.isValidPhoneNumber(mobile)) {
            Toaster.customToast(getResources().getString(R.string.Enter_your_phone_numberr));
        } else if (imagepath.equalsIgnoreCase("")) {
            Toaster.customToast(getResources().getString(R.string.Upload_profile_picture));
        } else {
            if (Global.isOnline(mActivity)) {
                submitCoachPersonalInfo();
            } else {
                Toaster.customToast(getResources().getString(R.string.no_internet));
            }
        }
    }

    private void openCameraid() {

        try {
            String fileName = "profile.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURIid = mActivity.getContentResolver().insert(
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

                cursor = mActivity.getContentResolver().query(selectedImageid, filePathColumn, null, null, null);
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
                    if (img_type.equals("profile")) {
                        profile_image.setImageURI(Uri.parse(imagepath));
                    } else {
                        cover_img.setImageURI(Uri.parse(imagepath));
                    }
                    updateImageTask(imagepath);
//                    try {
//                        Glide.with(EditProfile.this).load(Uri.parse(imagepath)).into(profile_image);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    textViewidproof.setText(image_pathid);
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
//                    dialog_camera.dismiss();
//                    profile_image.setImageURI(Uri.parse(imagepath));
                    if (img_type.equals("profile")) {
                        Glide.with(mActivity).load(imagepath).into(profile_image);
                    } else {
                        Glide.with(mActivity).load(imagepath).into(cover_img);
                    }
//                    textViewidproof.setText(image_pathid);

                    System.out.println("cccccccc   " + imagepath);
                    updateImageTask(imagepath);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = mActivity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void updateImageTask(String path) {
        loaderView.showLoader();
        try {
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("user_id", new StringBody(SessionManager.get_user_id(prefs)));
            entity.addPart("s", new StringBody(SessionManager.get_session_id(prefs)));
            if (!(path.equals("") || path == null)) {
                File file = new File(path);
                FileBody fileBody = new FileBody(file);
                if (img_type.equals("profile")) {
                    entity.addPart("image_type", new StringBody("avatar"));
                    entity.addPart("image", fileBody);
                } else {
                    entity.addPart("image_type", new StringBody("cover"));
                    entity.addPart("image", fileBody);
                }
            }
            MultipartRequest req = new MultipartRequest(Global.URL + "update_profile_picture", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        loaderView.hideLoader();
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
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

    private void getLanguage() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_LANGUAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponseCountry", response);
                Gson gson = new Gson();
                languageModelArrayList = gson.fromJson(response, Language.class);
                if (languageModelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    language = new ArrayList<>();
                    for (Language.Datum datum : languageModelArrayList.getData()) {
                        language.add(datum);
                    }
                    languageSelectionDialog(language, new ArrayList<>());

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

                                if (object.has("personal_info")) {
                                    JSONObject jsonObject_personal_info = object.getJSONObject("personal_info");
                                    setData(jsonObject_personal_info);
                                }
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
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
                        //loaderView.hideLoader();
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
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

    private void getCountry() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_countries", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponseCountry", response);
                Gson gson = new Gson();
                modelArrayList = gson.fromJson(response, Country.class);
                if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    ArrayList<String> country = new ArrayList<>();
                    country.add("Country");
                    for (Country.Datum data : modelArrayList.getData()) {
                        country.add(data.getName());
                    }

                    //countryID="";
                    ArrayAdapter aa = new ArrayAdapter(mActivity, R.layout.custom_spinner, country);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCountry.setAdapter(aa);

                    for (int i = 0; i < country.size(); i++) {
                        if (country.get(i).equalsIgnoreCase("India")) {
                            countryName = country.get(i);
                            spinnerCountry.setSelection(Global.getIndex(spinnerCountry, countryName));
                            break;
                        }
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
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getState(String countryId) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + "get_states" + "&country_id=" + countryId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponseSatate", response);
                if (!response.isEmpty()) {
                    loaderView.hideLoader();
                    Gson gson = new Gson();
                    statemodelArrayList = gson.fromJson(response, States.class);
                    if (statemodelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                        ArrayList<String> state = new ArrayList<>();
                        state.add("States");
                        for (States.Datum data : statemodelArrayList.getData()) {
                            state.add(data.getName());
                        }
                        ArrayAdapter aa = new ArrayAdapter(mActivity, R.layout.custom_spinner, state);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerState.setAdapter(aa);
                        spinnerState.setSelection(Global.getIndex(spinnerState, state_Name));

                        if(spinnerState.getSelectedItem().equals("States")){
                            getCity(statemodelArrayList.getData().get(i).getId());
                        }
                        //getCity(statemodelArrayList.getData().get(i-1).getId());
                        //spinnerState.setSelection(Global.getIndex(spinnerState, SessionManager.getStates(prefs)));
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loaderView.hideLoader();
                error.printStackTrace();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getCity(String stateId) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + "get_cities" + "&state_id=" + stateId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponseCity", response);
                loaderView.hideLoader();
                Gson gson = new Gson();
                citymodelArrayList = gson.fromJson(response, City.class);
                if (citymodelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    ArrayList<String> city = new ArrayList<>();
                    city.add("City");
                    for (City.Datum data : citymodelArrayList.getData()) {
                        city.add(data.getName());
                    }
                    ArrayAdapter aa = new ArrayAdapter(mActivity, R.layout.custom_spinner, city);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCity.setAdapter(aa);
                    spinnerCity.setSelection(Global.getIndex(spinnerCity, city_Name));
                    //spinnerCity.setSelection(Global.getIndex(spinnerCity, SessionManager.getCity(prefs)));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loaderView.hideLoader();
                error.printStackTrace();
                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView == spinnerCountry && i != 0) {
            //countryID = "";
            //state_Name="";
            //city_Name="";
            //spinnerState.setSelection(Global.getIndex(spinnerState, state_Name));
            //spinnerCity.setSelection(Global.getIndex(spinnerCity, city_Name));
            getState(modelArrayList.getData().get(i - 1).getId());
            countryID = modelArrayList.getData().get(i - 1).getId();


        } else if (adapterView == spinnerState && i != 0) {
            getCity(statemodelArrayList.getData().get(i - 1).getId());
            stateID = statemodelArrayList.getData().get(i - 1).getId();
        } else if (adapterView == spinnerCity && i != 0) {
            cityID = citymodelArrayList.getData().get(i - 1).getId();
        } else {
            state_Name = "";
            city_Name = "";
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setData(JSONObject data) {
        if (data.has("languages")) {

            try {
                JSONArray jsonArray = data.getJSONArray("languages");
                JSONObject jsonObject = null;
                CoachLanguage coachLanguage = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    coachLanguage = new CoachLanguage(jsonObject);
                    coachLanguages.add(coachLanguage.getName_eng());
                }
                languageSelectionDialog(language, coachLanguages);

                langStringBuilder = new StringBuilder();
                String prefix = "";
                for (String item : coachLanguages) {
                    langStringBuilder.append(prefix);
                    prefix = ",";
                    langStringBuilder.append(item);
                }
                //Log.d("size",langStringBuilder.toString());

                textView_language.setText(langStringBuilder.toString());

                //Log.d("size",coachLanguages.size()+"");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (data.has("first_name")) {
            edttxt_fname.setText(data.optString("first_name"));
        }
        if (data.has("last_name")) {
            edttxt_lname.setText(data.optString("last_name"));
        }
        if (data.has("mid_name")) {
            try {
                edttxt_Mname.setText(data.getString("mid_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (data.has("avatar")) {
            imagepath = data.optString("avatar");
            Glide.with(mActivity).load(data.optString("avatar")).into(profile_image);
        }
        if (data.has("country_name")) {
            countryName = data.optString("country_name");

            if (countryName.isEmpty()) {

            } else {
                //Toaster.customToast(countryName);
                countryID = data.optString("country_id");
                getState(countryID);
                spinnerCountry.setSelection(Global.getIndex(spinnerCountry, countryName));
            }

        }
        if (data.has("state_name")) {
            state_Name = data.optString("state_name");
            if (state_Name.equalsIgnoreCase("")) {

            } else {
                stateID = data.optString("state_id");
                getCity(stateID);
                //Toaster.customToast(state_Name);

            }


        }
        if (data.has("city_name")) {
            city_Name = data.optString("city_name");
            if (city_Name.isEmpty()) {

            } else {
                // Toaster.customToast(city_Name);
                //spinnerCity.setSelection(Global.getIndex(spinnerCity, city_Name));
            }


        }
        if (data.has("pincode")) {
            etPincode.setText(data.optString("pincode"));
        }
        if (data.has("phone_number")) {
            edttxt_phone.setText(data.optString("phone_number"));
        }
        if (data.has("address")) {
            etAddress.setText(data.optString("address"));
        }
        if (data.has("address2")) {
            etAddress2.setText(data.optString("address2"));
        }

        if (data.has("facebook")) {
            edttxt_fb_profileLink.setText(data.optString("facebook"));
        }
        if (data.has("twitter")) {
            edttxt_twitter_profile.setText(data.optString("twitter"));
        }

        if (data.has("linkedin")) {
            edttxt_linkind_profile.setText(data.optString("linkedin"));
        }
        if (data.has("instagram")) {
            edttxt_instagram_profile.setText(data.optString("instagram"));
        }
        if (data.has("youtube")) {
            edttxt_youtube_profile.setText(data.optString("youtube"));
        }
//
//        "facebook": "https://www.facebook.com/criconetonline12/",
//                "twitter": "Afzal Hussain",
//                "linkedin": "Afzal Hussain",
//                "instagram": "hafzal446",
//                "youtube": "https://youtube.com/channel/UCEDb-adU-R4urGLkXlMileg",


    }

    public void submitCoachPersonalInfo() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.UPDATE_COACH_PERSONAL_INFO,
                new Response.Listener<String>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(String response) {
                        Timber.d(response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                // Global.msgDialog(mActivity, "Profile saved successfully!");

                                if (jsonObject.has("data")) {
                                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                    phoneNumber = jsonObjectData.getString("temp_mobile_no");
                                    SessionManager.save_name(prefs, jsonObjectData.getString("username"));
                                    SessionManager.save_emailid(prefs, jsonObjectData.getString("email"));
                                    SessionManager.savePhone(prefs, jsonObjectData.getString("phone_number"));
                                    SessionManager.savePhoneCode(prefs, jsonObjectData.getString("phone_code"));
                                    SessionManager.save_sex(prefs, jsonObjectData.getString("gender"));
                                    SessionManager.save_image(prefs, jsonObjectData.getString("avatar"));
                                    SessionManager.save_cover(prefs, jsonObjectData.getString("cover"));
                                    SessionManager.save_mobile_verified(prefs, jsonObjectData.getString("is_mobile_verified"));
                                    SessionManager.save_profiletype(prefs, jsonObjectData.getString("profile_type"));

                                    String is_contact_verify = jsonObjectData.getString("is_mobile_verified");
                                    if (is_contact_verify.equalsIgnoreCase("0")) {
                                        EmailOtpDialog(phoneNumber);
                                    } else {
                                        congratsDialog();
                                    }
                                }

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(mActivity, jsonObject.optString("errors"));
                            } else {
                                Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
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
                        loaderView.hideLoader();
                        Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("first_name", fname_String);
                param.put("last_name", lname_String);
                param.put("mid_name", mname_String);
                param.put("address", address1);
                param.put("address2", address2);
                param.put("country_id", countryID);
                param.put("state_id", stateID);
                param.put("city_id", cityID);
                param.put("pincode", pincode);
                param.put("phone_number", mobile);
                param.put("twitter", twitter);
                param.put("facebook", facebook);
                param.put("linkedin", linkedin);
                param.put("instagram", instagram);
                param.put("youtube", youtube);
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("language_id", textView_language.getText().toString().trim());


                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    private void EmailOtpDialog(String mobile) {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_email_verification);
        dialog.setCancelable(false);


        TextView tvOTPTime = dialog.findViewById(R.id.tv_otp_time);
        TextView btResend = dialog.findViewById(R.id.btn_resend);
        Button btContinue = dialog.findViewById(R.id.btn_continue);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        LinearLayout lay_otp_expire = dialog.findViewById(R.id.lay_otp_expire);
        CountryCodePicker ccp = dialog.findViewById(R.id.ccp);
        EditText edit_phone = dialog.findViewById(R.id.edit_phone);

        startTimer(tvOTPTime, btResend, lay_otp_expire);

        btResend.setOnClickListener(view -> {
            resendOTP(mobile);
            lay_otp_expire.setVisibility(View.VISIBLE);
            startTimer(tvOTPTime, btResend, lay_otp_expire);
        });
        otpView = dialog.findViewById(R.id.otp_view);
        btContinue.setOnClickListener(v -> {
            lay_otp_expire.setVisibility(View.VISIBLE);
            if (Objects.requireNonNull(otpView.getText()).toString().isEmpty()) {
                Toaster.customToast(getString(R.string.code_msg));
            } else if (otpView.getText().toString().length() != 4) {
                Toaster.customToast(getString(R.string.code_invalid));
            } else {
                sendVerifyOtp(otpView.getText().toString().trim(), dialog);
            }
        });

        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();

    }

    private void resendOTP(String mobile) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.RESEND_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.tag("login response").e("%s", response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Toaster.customToast(getResources().getString(R.string.socket_timeout));
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
                        loaderView.hideLoader();
                        Toaster.customToast(getResources().getString(R.string.socket_timeout));
//                Global.msgDialog(Signup.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("phone_number", mobile);
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void sendVerifyOtp(String otp, Dialog dialog) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.OTP_VERIFY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.tag("login response").e("%s", response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                                dialog.dismiss();
                                if (jsonObject.has("data")) {
                                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                    phoneNumber = jsonObjectData.getString("temp_mobile_no");
                                    SessionManager.save_name(prefs, jsonObjectData.getString("username"));
                                    SessionManager.save_emailid(prefs, jsonObjectData.getString("email"));
                                    SessionManager.save_mobile(prefs, jsonObjectData.getString("phone_number"));
                                    SessionManager.savePhoneCode(prefs, jsonObjectData.getString("phone_code"));
                                    SessionManager.save_sex(prefs, jsonObjectData.getString("gender"));
                                    SessionManager.save_image(prefs, jsonObjectData.getString("avatar"));
                                    SessionManager.save_cover(prefs, jsonObjectData.getString("cover"));
                                    SessionManager.save_profiletype(prefs, jsonObjectData.getString("profile_type"));
                                    // Toaster.customToast(jsonObjectData.getString("is_mobile_verified"));
                                    SessionManager.save_mobile_verified(prefs, jsonObjectData.getString("is_mobile_verified"));
//                                    JSONObject ambassadorProfile = jsonObjectData.getJSONObject("ambassadorProfile");
//
//                                    if(ambassadorProfile.length()>0){
//                                        SessionManager.save_is_ambassador(prefs,"1");
//                                        SessionManager.save_is_amb_name(prefs,ambassadorProfile.getString("name"));
//                                        SessionManager.save_is_amb_fullname(prefs,ambassadorProfile.getString("full_name"));
//                                        SessionManager.save_is_amb_email(prefs,ambassadorProfile.getString("email"));
//                                        SessionManager.save_mobile(prefs,ambassadorProfile.getString("phone_number"));
//                                        SessionManager.save_is_amb_college(prefs,ambassadorProfile.getString("school_college_name"));
//                                        SessionManager.save_is_amb_highestQ(prefs,ambassadorProfile.getString("height_qualification"));
//                                        SessionManager.save_is_ambs_have_you_org_event_flag(prefs,ambassadorProfile.getString("have_you_org_event_flag"));
//                                        SessionManager.save_is_ambs_have_you_org_event_txt(prefs,ambassadorProfile.getString("have_you_org_event_txt"));
//                                        SessionManager.save_is_ambs_innovative_thing(prefs,ambassadorProfile.getString("innovative_thing"));
//                                        SessionManager.save_is_ambs_how_many_hrs_per_week(prefs,ambassadorProfile.getString("how_many_hrs_per_week"));
//                                        SessionManager.save_is_ambs_passionate_thing(prefs,ambassadorProfile.getString("passionate_thing"));
//                                        SessionManager.save_is_ambs_do_you_want_campus_ambassdor(prefs,ambassadorProfile.getString("do_you_want_campus_ambassdor"));
//                                        SessionManager.save_is_ambs_thing_you_are_know_criconet(prefs,ambassadorProfile.getString("thing_you_are_know_criconet"));
//                                    }else{
//                                        SessionManager.save_is_ambassador(prefs,"0");
//                                    }

                                    congratsDialog();

                                }

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Toaster.customToast(getResources().getString(R.string.socket_timeout));
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
                        loaderView.hideLoader();
                        Toaster.customToast(getResources().getString(R.string.socket_timeout));
//                Global.msgDialog(Signup.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("otp", otp);
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void startTimer(TextView tvOTPTime, TextView btResend, LinearLayout lay_otp_expire) {
        new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvOTPTime.setText("00 : " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                lay_otp_expire.setVisibility(View.GONE);
                btResend.setVisibility(View.VISIBLE);
            }

        }.start();
    }

    private void congratsDialog() {

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_congratulation);
        dialog.setCancelable(false);
        TextView tv_cancel_confirmed_msg = dialog.findViewById(R.id.tv_cancel_confirmed_msg);
        tv_cancel_confirmed_msg.setText(getResources().getString(R.string.Information_saved_successfully_Please_proceed_to_continue_your_registration));
        FrameLayout btContinue = dialog.findViewById(R.id.fl_done);

        btContinue.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(mContext, CoachProfessionalInfoActivity.class);
            startActivity(intent);
            //finish();
        });

        dialog.show();
    }

    private void languageSelectionDialog(ArrayList<Language.Datum> languageArray, ArrayList<String> coachLanguages) {
        TextView btnOk = dialog.findViewById(R.id.btnOk);
        RecyclerView rv_language = dialog.findViewById(R.id.rv_language);
        rv_language.setHasFixedSize(true);
        rv_language.setLayoutManager(new LinearLayoutManager(mActivity));
        //Log.d("ReSize",coachLanguages.size()+"");
        selectCoachLanguageAdapter selectCoachLanguageAdapter = new selectCoachLanguageAdapter(languageArray, coachLanguages, mActivity, new selectCoachLanguageAdapter.languageSelectionListner() {
            @Override
            public void itemChcked(ArrayList<String> languageArrayy) {
                // Log.d("SizeSelected",languageArrayy.size()+"");
                languageArray_new = languageArrayy;
            }
        });
        rv_language.setAdapter(selectCoachLanguageAdapter);
        //TextView btnCancel = dialog.findViewById(R.id.btnCancel);
//        btnCancel.setOnClickListener(view -> {
//            dialog.dismiss();
//        });
        btnOk.setOnClickListener(view -> {


            langStringBuilder = new StringBuilder();
            String prefix = "";

            for (int i = 0; i < languageArray_new.size(); i++) {
                langStringBuilder.append(prefix);
                prefix = ",";
                langStringBuilder.append(languageArray_new.get(i));
            }

            //Log.d("size",langStringBuilder.toString());

            textView_language.setText(langStringBuilder.toString());
            dialog.dismiss();
        });

        //dialog.show();

    }

    public static class selectCoachLanguageAdapter extends RecyclerView.Adapter<selectCoachLanguageAdapter.MyViewHolder> {
        Context context;
        ArrayList<Language.Datum> languageArray;
        ArrayList<String> checkedArray;
        ArrayList<String> coachLanguages;
        selectCoachLanguageAdapter.languageSelectionListner languageSelectionListner;

        public selectCoachLanguageAdapter(ArrayList<Language.Datum> languageArray, ArrayList<String> coachLanguages, Context context, selectCoachLanguageAdapter.languageSelectionListner languageSelectionListner) {
            this.context = context;
            this.languageArray = languageArray;
            this.languageSelectionListner = languageSelectionListner;
            this.coachLanguages = coachLanguages;
            checkedArray = new ArrayList<>();
        }

        @Override
        public selectCoachLanguageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // inflate the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_coach_spinner_item, parent, false);
            selectCoachLanguageAdapter.MyViewHolder vh = new selectCoachLanguageAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(selectCoachLanguageAdapter.MyViewHolder holder, final int position) {
            Language.Datum coachLanguage = languageArray.get(position);

            holder.textView.setText(coachLanguage.getName_eng());
            holder.checkBox.setChecked(coachLanguage.isSelected());

            Log.d("coachSize", coachLanguages.size() + "");

            for (int j = 0; j < coachLanguages.size(); j++) {
                if (coachLanguages.get(j).equalsIgnoreCase(coachLanguage.getName_eng())) {
                    holder.checkBox.setChecked(true);
                    checkedArray.add(coachLanguages.get(j));
                    languageSelectionListner.itemChcked(checkedArray);
                    break;
                }


            }


            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox myCheckBox = (CheckBox) view;
                    Language.Datum coachLanguage = languageArray.get(position);

                    if (myCheckBox.isChecked()) {
                        coachLanguage.setSelected(true);
                        checkedArray.add(coachLanguage.getName_eng());
                    } else if (!myCheckBox.isChecked()) {
                        coachLanguage.setSelected(false);
                        checkedArray.remove(coachLanguage.getName_eng());
                    }
                    languageSelectionListner.itemChcked(checkedArray);
                }
            });


        }

        @Override
        public int getItemCount() {
            return languageArray.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            CheckBox checkBox;
            TextView textView;

            MyViewHolder(View itemView) {
                super(itemView);
                checkBox = itemView.findViewById(R.id.radio);
                textView = itemView.findViewById(R.id.textView);

            }
        }

        interface languageSelectionListner {
            void itemChcked(ArrayList<String> languageArray);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}