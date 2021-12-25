package com.pb.criconet.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.pb.criconet.R;
import com.pb.criconet.Utills.CCResource;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.MultipartRequest;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.activity.EditProfile;
import com.pb.criconet.adapters.BookingHistoryAdapter;
import com.pb.criconet.models.BookingHistory;
import com.pb.criconet.models.City;
import com.pb.criconet.models.Country;
import com.pb.criconet.models.States;
import com.pb.criconet.models.UserData;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FragmentEditProfile extends Fragment implements AdapterView.OnItemSelectedListener {
    View rootView;
    private static final int CAMERA_REQUESTid = 2015;
    private static final int PICK_IMAGEid = 100;
    private SharedPreferences prefs;
    private String email_String, name_String, fname_String, lname_String, gender_String, countryID="", stateID="",cityID="";
    private EditText edttxt_email, edttxt_name, edttxt_fname, edttxt_lname, etAddress, edttxt_birthday, edttxt_phone,etPincode;
    private Spinner spn_gender;
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
    private String file_pathid = "", image_pathid = "";
    private String imagepath = "";
    private String img_type = "";

    private UserData userData;
    private ProgressDialog progress;
    private Country modelArrayList;
    private City citymodelArrayList;
    private States statemodelArrayList;
    private SlideUp slideUp;
    private View dim, rootViewPost;
    private View slideView;
    private TextView tv_camera;
    private TextView tvGallery;
    private TextView tvCancel;
    private TextView tv_choose;
    RelativeLayout middle;
    CustomLoaderView loaderView;

    public static FragmentEditProfile newInstance() {
        return new FragmentEditProfile();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.edit_profile, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loaderView = CustomLoaderView.initialize(getActivity());

        queue = Volley.newRequestQueue(getActivity());
        progress = new ProgressDialog(getActivity());
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        edttxt_name = rootView.findViewById(R.id.edttxt_name);
        edttxt_fname =rootView.findViewById(R.id.edttxt_fname);
        edttxt_lname =rootView.findViewById(R.id.edttxt_lname);
        edttxt_email =rootView.findViewById(R.id.edttxt_email);
        etAddress = rootView.findViewById(R.id.etAddress);
        edttxt_birthday = rootView.findViewById(R.id.edttxt_birthday);
        edttxt_phone = rootView.findViewById(R.id.edttxt_phone);
        etPincode = rootView.findViewById(R.id.etPincode);
        spn_gender = rootView.findViewById(R.id.spn_gender);
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
                .withSlideFromOtherView(rootView)
                .build();
        tvCancel.setOnClickListener(v -> {
            slideUp.hide();
        });

        spinnerCountry.setOnItemSelectedListener(this);
        spinnerState.setOnItemSelectedListener(this);
        spinnerCity.setOnItemSelectedListener(this);

        getCountry();
        getUsersDetails(SessionManager.get_user_id(prefs));

        middle.setOnClickListener(v -> {
            img_type = "profile";
            selectImage();
        });
        cover_img.setOnClickListener(v -> {
            img_type = "cover";
            selectImage();
        });
        edttxt_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });



//        galary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment = new PhotosVideos();
//                Bundle bundle = new Bundle();
//                bundle.putString("type", "photoo.jpg.jpg");
//                bundle.putString("user_id", SessionManager.get_user_id(prefs));
//                fragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
//            }
//        });
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Fragment fragment = new PhotosVideos();
//                Bundle bundle = new Bundle();
//                bundle.putString("type", "video");
//                bundle.putString("user_id", SessionManager.get_user_id(prefs));
//                fragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
//            }
//        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_login.getText().toString().trim().equalsIgnoreCase("Click to Edit Profile")){
                    setEnable();
                }else {
                    checkMethod();
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
    private void checkMethod() {
        email_String = edttxt_email.getText().toString().trim();
        name_String = edttxt_name.getText().toString().trim();
        fname_String = edttxt_fname.getText().toString().trim();
        lname_String = edttxt_lname.getText().toString().trim();
//        address_String = edttxt_address.getText().toString().trim();
        gender_String = spn_gender.getSelectedItem().toString();

        if (!Global.validateLength(name_String, 3)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.Enter_Name), Toast.LENGTH_SHORT).show();
        } else if (!Global.validateLength(fname_String, 3)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.Enter_First_Name_at_least_3_character), Toast.LENGTH_SHORT).show();
        } else if (!Global.validateLength(lname_String, 3)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.Enter_Last_Name_at_least_3_character), Toast.LENGTH_SHORT).show();
        } else if (!Global.ValidEmail(email_String)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.Enter_EmailId), Toast.LENGTH_SHORT).show();
        } else if (spn_gender.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), getResources().getString(R.string.Select_Gender), Toast.LENGTH_SHORT).show();
        } else {
            if (Global.isOnline(getActivity())) {
                editprofile_task();
            } else {
                Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void getUsersDetails(final String user_id) {
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

                                setData();
                                setDisable();


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

    }

    private void setData() {
        edttxt_name.setText(Global.capitizeString(SessionManager.get_name(prefs)));
        edttxt_fname.setText(SessionManager.get_fname(prefs));
        edttxt_lname.setText(SessionManager.get_lname(prefs));
        edttxt_email.setText(SessionManager.get_emailid(prefs));
        etAddress.setText(SessionManager.get_address(prefs));

        if(SessionManager.get_dob(prefs).equalsIgnoreCase("0000-00-00")){

        }else{
            edttxt_birthday.setText(SessionManager.get_dob(prefs));
        }


        edttxt_phone.setText(SessionManager.get_mobile(prefs));

        if(userData.getPincode().equalsIgnoreCase("null")){
            etPincode.setText("");
        }else{
            etPincode.setText(SessionManager.getpinCode(prefs));
        }

        spn_gender.setSelection(Global.getIndex(spn_gender, SessionManager.get_sex(prefs)));

        Glide.with(getActivity()).load(SessionManager.get_image(prefs)).into(profile_image);
        Glide.with(getActivity()).load(SessionManager.get_cover(prefs)).into(cover_img);
    }

    private void setEnable() {
        edttxt_name.setEnabled(false);
        edttxt_fname.setEnabled(true);
        edttxt_lname.setEnabled(true);
        edttxt_email.setEnabled(false);
        etAddress.setEnabled(true);
        edttxt_birthday.setEnabled(true);
        edttxt_phone.setEnabled(true);
        etPincode.setEnabled(true);
        spn_gender.setEnabled(true);
        spinnerCountry.setEnabled(true);
        spinnerState.setEnabled(true);
        spinnerCity.setEnabled(true);
        spinnerCity.setEnabled(true);
        spinnerState.setEnabled(true);
        btn_login.setText("Click to Update Profile");

    }
    private void setDisable() {
        edttxt_name.setEnabled(false);
        edttxt_fname.setEnabled(false);
        edttxt_lname.setEnabled(false);
        edttxt_email.setEnabled(false);
        etAddress.setEnabled(false);
        edttxt_birthday.setEnabled(false);
        edttxt_phone.setEnabled(false);
        etPincode.setEnabled(false);
        spn_gender.setEnabled(false);
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
                                getUsersDetails(SessionManager.get_user_id(prefs));

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
                param.put("username", edttxt_name.getText().toString());
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
                            getUsersDetails(SessionManager.get_user_id(prefs));

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

    public void datePicker() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        txtMonth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        Calendar c = Calendar.getInstance();
                        Date date = new Date();
                        c.setTime(date);
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        date = c.getTime();

                        String outputPattern = "dd-MM-yyyy";
                        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                        edttxt_birthday.setText(outputFormat.format(date));
                    }
                }, mYear, mMonth + 1, mDay);
//        ((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
//        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.setTitle("");
        datePickerDialog.show();
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
                    spinnerCountry.setSelection(Global.getIndex(spinnerCountry, SessionManager.getCountry(prefs)));


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
                        spinnerState.setSelection(Global.getIndex(spinnerState, SessionManager.getStates(prefs)));
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
                    spinnerCity.setSelection(Global.getIndex(spinnerCity, SessionManager.getCity(prefs)));
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
}
