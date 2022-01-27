package com.pb.criconet.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.pb.criconet.activity.Signup;
import com.pb.criconet.models.City;
import com.pb.criconet.models.Country;
import com.pb.criconet.models.Language;
import com.pb.criconet.models.States;
import com.pb.criconet.models.UserData;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class FragmentCoachEditProfile extends Fragment implements AdapterView.OnItemSelectedListener {
    View rootView;
    private static final int CAMERA_REQUESTid = 2015;
    private static final int PICK_IMAGEid = 100;
    private SharedPreferences prefs;
    private String  fname_String, mname_String, lname_String, gender_String, countryID="", stateID="",cityID="",address1,address2,pincode,mobile;
    private EditText etAddress2, edttxt_fname, edttxt_lname, etAddress, edttxt_birthday, edttxt_phone,etPincode;
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
    private String file_pathid = "", image_pathid = "",phoneNumber;
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
    ArrayList<String> language=null;
    String[] langArray = null;
    private OtpView otpView;
    private navigateListener listener;

            //{"Java", "C++", "Kotlin", "C", "Python", "Javascript","aa","jaja","jsk","shaH","JSKS","JSKA","JSA","JSKAS","HS"};

    public static FragmentCoachEditProfile newInstance() {
        return new FragmentCoachEditProfile();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (navigateListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.coach_edit_profile, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loaderView = CustomLoaderView.initialize(getActivity());
        queue = Volley.newRequestQueue(getActivity());


        edttxt_fname = rootView.findViewById(R.id.edttxt_fname);
        edttxt_Mname =rootView.findViewById(R.id.edttxt_Mname);
        edttxt_lname =rootView.findViewById(R.id.edttxt_lname);
        etAddress = rootView.findViewById(R.id.etAddress);
        etAddress2 = rootView.findViewById(R.id.etAddress2);
        edttxt_phone = rootView.findViewById(R.id.edttxt_phone);
        etPincode = rootView.findViewById(R.id.etPincode);
        textView_language = rootView.findViewById(R.id.textView_language);
        spn_language = rootView.findViewById(R.id.spn_language);
        spinnerCountry = rootView.findViewById(R.id.spinerCountry);
        spinnerState = rootView.findViewById(R.id.spinerState);
        spinnerCity =  rootView.findViewById(R.id.spinerCity);
        profile_image =  rootView.findViewById(R.id.profile_pic);
        middle = rootView.findViewById(R.id.middle);
        cover_img =rootView.findViewById(R.id.cover_img);
        imageView =rootView.findViewById(R.id.imageView);
        btn_login =rootView.findViewById(R.id.btn_login);

        rootViewPost = rootView.findViewById(R.id.root_vieww);
        slideView = rootView.findViewById(R.id.slideView);
        dim = rootView.findViewById(R.id.dim);
        tv_camera=rootView.findViewById(R.id.tv_camera);
        tvGallery=rootView.findViewById(R.id.tvGallery);
        tvCancel=rootView.findViewById(R.id.tvCancel);
        tv_choose=rootView.findViewById(R.id.tv_choose);
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


        if (Global.isOnline(getActivity())) {
            getCountry();
            getLanguage();
        } else {
            Global.showDialog(getActivity());
        }
        // getUsersDetails(SessionManager.get_user_id(prefs));

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

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // set title
                builder.setTitle(getActivity().getResources().getString(R.string.Select_Language_You_Speak));

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(langArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            Log.d("Index",i+"");
                            langList.add(i);
                            // Sort array list
                            //Collections.sort(langList);
                        } else {

                            if (langList.contains(i)) {
                                langList.remove((Integer) i);
                            }

                            //langList.remove(langArray);
                            // when checkbox unselected
                            // Remove position from langList

                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(langArray[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(",");
                            }
                        }
                        // set text on textView
                        textView_language.setText(stringBuilder.toString());

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedLanguage.length; j++) {
                            // remove all selection
                            selectedLanguage[j] = false;
                            // clear language list
                            langList.clear();
                            // clear text view value
                            textView_language.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
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

        if (!Global.validateLengthofCoachRegister(fname_String)) {
            Toaster.customToast(getResources().getString(R.string.Enter_First_Name));
        }
//        else if (!Global.validateLength(mname_String, 3)) {
//            Toaster.customToast(getResources().getString(R.string.Enter_Middle_Name_at_least_3_character));
//        }
        else if (!Global.validateLengthofCoachRegister(lname_String)) {
            Toaster.customToast(getResources().getString(R.string.Enter_Last_Name));
        }
        else if (langList.size()==0) {
            Toaster.customToast(getResources().getString(R.string.select_language));
        }else if (langList.size() >5) {
            Toaster.customToast(getResources().getString(R.string.you_can_only_select_five_item));
        }
        else if(countryID.equalsIgnoreCase("")){
            Toaster.customToast(getResources().getString(R.string.Select_Country));
        }
//        else if(stateID.equalsIgnoreCase("")){
            //Toaster.customToast(getResources().getString(R.string.Select_City));
//        }else if(cityID.equalsIgnoreCase("")){
           // Toaster.customToast(getResources().getString(R.string.Select_State));
//        }
        else if(!Global.validateLengthofCoachRegister(address1)){
            Toaster.customToast(getResources().getString(R.string.Enter_your_address));
        }
//        else if(!Global.validateLength(address2, 3)){
//            Toaster.customToast(getResources().getString(R.string.enter_landmark));
//        }
        else if(!Global.isValidPincode(pincode)){
            Toaster.customToast(getResources().getString(R.string.enter_pincode));
        }else if(!Global.isValidPhoneNumber(mobile)){
            Toaster.customToast(getResources().getString(R.string.Enter_your_phone_number));
        } else if(imagepath.equalsIgnoreCase("")){
            Toaster.customToast(getResources().getString(R.string.Upload_profile_picture));
        }
        else {
            if (Global.isOnline(getActivity())) {
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
            mCapturedImageURIid = getActivity().getContentResolver().insert(
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

                cursor = getActivity().getContentResolver().query(selectedImageid, filePathColumn, null, null, null);
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
                Cursor cursor = getActivity().managedQuery(mCapturedImageURIid, projection, null, null, null);
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
                        Glide.with(getActivity()).load(imagepath).into(profile_image);
                    } else {
                        Glide.with(getActivity()).load(imagepath).into(cover_img);
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
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void setData() {
        edttxt_fname.setText(Global.capitizeString(SessionManager.get_name(prefs)));
        edttxt_fname.setText(SessionManager.get_fname(prefs));
        edttxt_lname.setText(SessionManager.get_lname(prefs));
        etAddress.setText(SessionManager.get_address(prefs));
        edttxt_phone.setText(SessionManager.get_mobile(prefs));

        if(userData.getPincode().equalsIgnoreCase("null")){
            etPincode.setText("");
        }else{
            etPincode.setText(SessionManager.getpinCode(prefs));
        }

        spn_language.setSelection(Global.getIndex(spn_language, SessionManager.get_sex(prefs)));

        Glide.with(getActivity()).load(SessionManager.get_image(prefs)).into(profile_image);
        Glide.with(getActivity()).load(SessionManager.get_cover(prefs)).into(cover_img);
    }

    private void setEnable() {
        edttxt_fname.setEnabled(false);
        edttxt_fname.setEnabled(true);
        edttxt_lname.setEnabled(true);
        etAddress.setEnabled(true);
        edttxt_birthday.setEnabled(true);
        edttxt_phone.setEnabled(true);
        etPincode.setEnabled(true);
        spn_language.setEnabled(true);
        spinnerCountry.setEnabled(true);
        spinnerState.setEnabled(true);
        spinnerCity.setEnabled(true);
        spinnerCity.setEnabled(true);
        spinnerState.setEnabled(true);
        btn_login.setText("Click to Update Profile");

    }
    private void setDisable() {
        edttxt_fname.setEnabled(false);
        edttxt_fname.setEnabled(false);
        edttxt_lname.setEnabled(false);
        etAddress.setEnabled(false);
        edttxt_birthday.setEnabled(false);
        edttxt_phone.setEnabled(false);
        etPincode.setEnabled(false);
        spn_language.setEnabled(false);
        spinnerCountry.setEnabled(false);
        spinnerCity.setEnabled(false);
        spinnerState.setEnabled(false);
        btn_login.setText(getResources().getString(R.string.Click_to_Edit_Profile));
    }

    private void editprofile_task() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "update_user_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UpdateResponse",response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                Global.msgDialog(getActivity(), "Profile Saved Successfully");
                                // getUsersDetails(SessionManager.get_user_id(prefs));

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(getActivity(), jsonObject.optString("errors"));
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
                        loaderView.hideLoader();
                        Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("type", "profile_update");
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("username", edttxt_fname.getText().toString());
                param.put("gender", gender_String);
                param.put("first_name", edttxt_fname.getText().toString());
                param.put("last_name", edttxt_lname.getText().toString());
                //param.put("mid_name", "");
                param.put("country_id", countryID);
                param.put("state_id", stateID);
                param.put("city_id", cityID);
                param.put("address", etAddress.getText().toString().trim());
                //param.put("address2", "");
                param.put("pincode", etPincode.getText().toString().trim());
                //param.put("phone_code", "");
                param.put("phone_number", edttxt_phone.getText().toString());
                param.put("birthday", edttxt_birthday.getText().toString());
                System.out.println("data   :" + param);

                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

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
                            Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
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
    private void getCountry() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_countries", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponseCountry",response);
                Gson gson = new Gson();
                modelArrayList = gson.fromJson(response, Country.class);
                if(modelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    ArrayList<String> country = new ArrayList<>();
                    country.add("Country");
                    for (Country.Datum data : modelArrayList.getData()) {
                        country.add(data.getName());
                    }
                    ArrayAdapter aa = new ArrayAdapter(getActivity(), R.layout.custom_spinner, country);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCountry.setAdapter(aa);

                    for(int i=0;i<country.size();i++){
                        if(country.get(i).equalsIgnoreCase("India")){
                            spinnerCountry.setSelection(Global.getIndex(spinnerCountry, country.get(i)));
                            break;
                        }
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
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + "get_states"+"&country_id="+countryId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponseSatate",response);
                if(!response.isEmpty()) {
                    loaderView.hideLoader();
                    Gson gson = new Gson();
                    statemodelArrayList = gson.fromJson(response, States.class);
                    if(statemodelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                        ArrayList<String> state = new ArrayList<>();
                        state.add("States");
                        for (States.Datum data : statemodelArrayList.getData()) {
                            state.add(data.getName());
                        }
                        ArrayAdapter aa = new ArrayAdapter(getActivity(), R.layout.custom_spinner, state);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerState.setAdapter(aa);
                        //spinnerState.setSelection(Global.getIndex(spinnerState, SessionManager.getStates(prefs)));
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loaderView.hideLoader();
                error.printStackTrace();
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getCity(String stateId) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.GET, Global.URL + "get_cities"+"&state_id="+stateId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponseCity",response);
                loaderView.hideLoader();
                Gson gson = new Gson();
                citymodelArrayList = gson.fromJson(response, City.class);
                if(citymodelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    ArrayList<String> city = new ArrayList<>();
                    city.add("City");
                    for (City.Datum data : citymodelArrayList.getData()) {
                        city.add(data.getName());
                    }
                    ArrayAdapter aa = new ArrayAdapter(getActivity(), R.layout.custom_spinner, city);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCity.setAdapter(aa);
                    //spinnerCity.setSelection(Global.getIndex(spinnerCity, SessionManager.getCity(prefs)));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loaderView.hideLoader();
                error.printStackTrace();
                Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
            }
        }) ;
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void getLanguage() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_LANGUAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponseCountry",response);
                Gson gson = new Gson();
                languageModelArrayList = gson.fromJson(response, Language.class);
                if(languageModelArrayList.getApiStatus().equalsIgnoreCase("200")) {
                    language = new ArrayList<>();
                    language.add("Language");
                    for (Language.Datum datum : languageModelArrayList.getData()) {
                        language.add(datum.getName_eng());
                    }
                    language.remove(0);
                    langArray= language.toArray(new String[language.size()]);
                    selectedLanguage = new boolean[langArray.length];
                    ArrayAdapter aa = new ArrayAdapter(getActivity(), R.layout.custom_spinner, language);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_language.setAdapter(aa);
                    spn_language.setSelection(Global.getIndex(spn_language, SessionManager.getLanguage(prefs)));
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

        if(adapterView==spinnerCountry && i!=0) {
            getState(modelArrayList.getData().get(i-1).getId());
            countryID=modelArrayList.getData().get(i-1).getId();
        }else if(adapterView==spinnerState && i!=0){
            getCity(statemodelArrayList.getData().get(i-1).getId());
            stateID=statemodelArrayList.getData().get(i-1).getId();
        }else if(adapterView==spinnerCity && i!=0){
            cityID=citymodelArrayList.getData().get(i-1).getId();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /*public void getUsersDetails(final String user_id) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_user_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("getUserDetails",response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                JSONObject object = jsonObject.getJSONObject("user_data");

                                userData = UserData.fromJson(object);

                                SessionManager.save_user_id(prefs, userData.getUser_id());
                                SessionManager.save_name(prefs, userData.getUsername());
                                SessionManager.save_fname(prefs, userData.getFirst_name());
                                SessionManager.save_lname(prefs, userData.getLast_name());
                                SessionManager.save_emailid(prefs, userData.getEmail());
                                SessionManager.save_sex(prefs, userData.getGender());
                                SessionManager.save_address(prefs, userData.getAddress());
                                SessionManager.save_image(prefs, userData.getAvatar());
                                SessionManager.save_cover(prefs, userData.getCover());
                                SessionManager.save_dob(prefs, userData.getBirthday());
                                SessionManager.save_mobile(prefs, userData.getPhone_number());

                                SessionManager.savepinCode(prefs, userData.getPincode());
                                SessionManager.saveCountry(prefs, userData.getCountry_name());
                                SessionManager.saveStates(prefs, userData.getState_name());
                                SessionManager.saveCity(prefs, userData.getCity_name());
                                SessionManager.saveCityId(prefs, userData.getCity_id());
                                SessionManager.saveStateId(prefs, userData.getState_id());

                                //setData();
                                //setDisable();


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
                        loaderView.hideLoader();
                        Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", user_id);
                param.put("user_profile_id", user_id);
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

    }*/

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
                               // Global.msgDialog(getActivity(), "Profile saved successfully!");

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
                                    //SessionManager.save_mobile_verified(prefs, jsonObjectData.getString("is_mobile_verified"));
                                    SessionManager.save_profiletype(prefs, jsonObjectData.getString("profile_type"));

                                    EmailOtpDialog(phoneNumber);

                                }



                                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(getActivity(), jsonObject.optString("errors"));
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
                        loaderView.hideLoader();
                        Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
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
        Dialog dialog = new Dialog(getActivity());
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
                                    listener.callbackMetod("5");
                                    SessionManager.save_name(prefs, jsonObjectData.getString("username"));
                                    SessionManager.save_emailid(prefs, jsonObjectData.getString("email"));
                                    SessionManager.save_mobile(prefs, jsonObjectData.getString("phone_number"));
                                    SessionManager.savePhoneCode(prefs, jsonObjectData.getString("phone_code"));

                                    // Toaster.customToast(jsonObjectData.getString("is_mobile_verified"));
                                    SessionManager.save_mobile_verified(prefs, jsonObjectData.getString("is_mobile_verified"));
                                    JSONObject ambassadorProfile = jsonObjectData.getJSONObject("ambassadorProfile");

                                    if(ambassadorProfile.length()>0){
                                        SessionManager.save_is_ambassador(prefs,"1");
                                        SessionManager.save_is_amb_name(prefs,ambassadorProfile.getString("name"));
                                        SessionManager.save_is_amb_fullname(prefs,ambassadorProfile.getString("full_name"));
                                        SessionManager.save_is_amb_email(prefs,ambassadorProfile.getString("email"));
                                        SessionManager.save_mobile(prefs,ambassadorProfile.getString("phone_number"));
                                        SessionManager.save_is_amb_college(prefs,ambassadorProfile.getString("school_college_name"));
                                        SessionManager.save_is_amb_highestQ(prefs,ambassadorProfile.getString("height_qualification"));
                                        SessionManager.save_is_ambs_have_you_org_event_flag(prefs,ambassadorProfile.getString("have_you_org_event_flag"));
                                        SessionManager.save_is_ambs_have_you_org_event_txt(prefs,ambassadorProfile.getString("have_you_org_event_txt"));
                                        SessionManager.save_is_ambs_innovative_thing(prefs,ambassadorProfile.getString("innovative_thing"));
                                        SessionManager.save_is_ambs_how_many_hrs_per_week(prefs,ambassadorProfile.getString("how_many_hrs_per_week"));
                                        SessionManager.save_is_ambs_passionate_thing(prefs,ambassadorProfile.getString("passionate_thing"));
                                        SessionManager.save_is_ambs_do_you_want_campus_ambassdor(prefs,ambassadorProfile.getString("do_you_want_campus_ambassdor"));
                                        SessionManager.save_is_ambs_thing_you_are_know_criconet(prefs,ambassadorProfile.getString("thing_you_are_know_criconet"));
                                    }else{
                                        SessionManager.save_is_ambassador(prefs,"0");
                                    }

                                    //congratsDialog();

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
    public void setMyCustomListener(navigateListener listener) {
        this.listener = listener;
    }

    public interface navigateListener {
        void callbackMetod(String type);
    }

}
