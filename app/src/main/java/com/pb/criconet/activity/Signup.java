package com.pb.criconet.activity;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.DataModel;
import com.pb.criconet.Utills.DropDownView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.MultipartRequest;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.activity.Login;
import com.pb.criconet.activity.MainActivity;
import com.pb.criconet.models.FBUser;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

/**
 * Created by Pradeep on 9/6/2016.
 */
public class Signup extends AppCompatActivity {

    private static final int CAMERA_REQUESTid = 2015;
    private static final int PICK_IMAGEid = 100;
    SharedPreferences prefs;
    LinearLayout click_signin;
    Button btn_login;
    TextView terms;
    EditText edttxt_email, edttxt_fname, edttxt_address;
    TextInputLayout til_edttxt_email, til_edttxt_password, til_edttxt_repassword, til_edttxt_fname, til_edttxt_lname, til_edttxt_address, til_edttxt_phone;
    String email_String, password_String, repassword_String, fname_String, lname_String, address_String, phone_String;
    RequestQueue queue;
    //    ProgressWheel progress_wheel;
    Spinner spn_gender;
    ProgressDialog progress;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    String imagepath = "";
    ImageView profile_image;
    //    CallbackManager callbackManager;
    Cursor cursor;
    int columnindex, i;
    Uri URIid = null;
    Uri selectedImageid, mCapturedImageURIid;
    String file_pathid = "", image_pathid = "";
    DropDownView drop_gender, drop_type;
    private ArrayList<DataModel> option = new ArrayList<>();
    private ArrayList<DataModel> option_type = new ArrayList<>();

    // Afzal code here...
    TextView txt_login, textterms, edt_username, edtEmailId, edttxt_password, edit_confirm_password;
    EditText edit_username_red_bg, edit_email_red_bg, edit_username_phone, edit_password_red_bg, edit_confirm_password_red;
    CountryCodePicker ccp;
    RelativeLayout rel_user_bg_red, rel_email_bg_red, rel_password_bg_red, rel_confirm_password_bg_red;
    LinearLayout lin_register;
    String userName, email, password, confirmPassword, gender, profileType = "", phoneCode, phoneNumber;
    CustomLoaderView loaderView;
    ImageButton ib_visible_p, ib_visible_cp;
    MaterialCardView rel_message, signup_card;
    private OtpView otpView;
    private Activity activity;
    private String mobile_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        activity = this;
        loaderView = CustomLoaderView.initialize(this);
//        StatusBarGradient.setStatusBarGradiant(Signup.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(Signup.this);
        queue = Volley.newRequestQueue(Signup.this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        progress = new ProgressDialog(Signup.this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView toolbartext = (TextView) toolbar.findViewById(R.id.toolbartext);
        toolbartext.setText(R.string.register);


//        callbackManager = CallbackManager.Factory.create();

        /*profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });*/

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Signup.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                SessionManager.save_devicetoken(prefs, newToken);
                Log.e("newToken", newToken);
            }
        });

        initializeView();

    }

    private void initializeView() {

        rel_message = findViewById(R.id.rel_message);
        signup_card = findViewById(R.id.signup_card);
        txt_login = findViewById(R.id.txt_login);
        edt_username = findViewById(R.id.edt_username);
        edit_username_red_bg = findViewById(R.id.edit_username_red_bg);
        edtEmailId = findViewById(R.id.edtEmailId);
        edit_email_red_bg = findViewById(R.id.edit_email_red_bg);
        edit_username_phone = findViewById(R.id.edit_username_phone);
        edit_username_phone.setLongClickable(false);
        ccp = findViewById(R.id.ccp);
        edttxt_password = findViewById(R.id.edttxt_password);
        edit_password_red_bg = findViewById(R.id.edit_password_red_bg);
//        edit_confirm_password = findViewById(R.id.edit_confirm_password);
//        edit_confirm_password_red = findViewById(R.id.edit_confirm_password_red);

        rel_user_bg_red = findViewById(R.id.rel_user_bg_red);
        rel_email_bg_red = findViewById(R.id.rel_email_bg_red);
        rel_password_bg_red = findViewById(R.id.rel_password_bg_red);
        rel_confirm_password_bg_red = findViewById(R.id.rel_confirm_password_bg_red);

        ib_visible_p = findViewById(R.id.ib_visible_p);
        ib_visible_p.setTag("InVisible");
        ib_visible_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ib_visible_p.getTag().equals("InVisible")) {
                    edit_password_red_bg.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ib_visible_p.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_black_24dp));
                    ib_visible_p.setTag("Visible");
                } else {
                    edit_password_red_bg.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ib_visible_p.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_black_24dp));
                    ib_visible_p.setTag("InVisible");
                }
            }
        });
//        ib_visible_cp = findViewById(R.id.ib_visible_cp);
//        ib_visible_cp.setTag("InVisible");
//        ib_visible_cp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ib_visible_cp.getTag().equals("InVisible")) {
//                    edit_confirm_password_red.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    ib_visible_cp.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_black_24dp));
//                    ib_visible_cp.setTag("Visible");
//                } else {
//                    edit_confirm_password_red.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    ib_visible_cp.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_black_24dp));
//                    ib_visible_cp.setTag("InVisible");
//                }
//            }
//        });

        textterms = findViewById(R.id.terms);
        textterms.setOnClickListener(v -> {

            String url ="https:\\/\\/www.criconet.com\\/terms\\/terms?rst=app";
            startActivity(new Intent(activity, WebViewSignUpTermsActivity.class).putExtra("URL", url).putExtra("title","Terms"));
             finish();
            //startActivity(new Intent(this, TermsCondition.class));
        });


        drop_gender = findViewById(R.id.drop_gender);
        option.add(new DataModel("Male"));
        option.add(new DataModel("Female"));
        option.add(new DataModel("Others"));
        drop_gender.setOptionList(option);

        drop_gender.setClickListener(new DropDownView.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {
                gender = name;
            }


            @Override
            public void onDismiss() {
            }
        });

        drop_type = findViewById(R.id.drop_user_type);
        option_type.add(new DataModel("User"));
        option_type.add(new DataModel("Coach"));
        drop_type.setOptionList(option_type);

        drop_type.setClickListener(new DropDownView.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {
                profileType = name;
            }


            @Override
            public void onDismiss() {
            }
        });


        lin_register = findViewById(R.id.lin_register);


        edt_username.setOnClickListener(v -> {
            edt_username.setVisibility(View.GONE);
            rel_user_bg_red.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            edit_username_red_bg.requestFocus();
            imm.showSoftInput(edit_username_red_bg, InputMethodManager.SHOW_FORCED);
        });
        edtEmailId.setOnClickListener(v -> {
            edtEmailId.setVisibility(View.GONE);
            rel_email_bg_red.setVisibility(View.VISIBLE);
            edit_email_red_bg.requestFocus();
        });

        edttxt_password.setOnClickListener(v -> {
            edttxt_password.setVisibility(View.GONE);
            rel_password_bg_red.setVisibility(View.VISIBLE);
            edit_password_red_bg.requestFocus();
        });
//        edit_confirm_password.setOnClickListener(v -> {
//            edit_confirm_password.setVisibility(View.GONE);
//            rel_confirm_password_bg_red.setVisibility(View.VISIBLE);
//            edit_confirm_password_red.requestFocus();
//        });
        edit_username_red_bg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edtEmailId.setVisibility(View.GONE);
                    rel_email_bg_red.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        edit_username_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edttxt_password.setVisibility(View.GONE);
                    rel_password_bg_red.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

//        edit_password_red_bg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                if (actionId == EditorInfo.IME_ACTION_NEXT) {
//                    edit_confirm_password.setVisibility(View.GONE);
//                    rel_confirm_password_bg_red.setVisibility(View.VISIBLE);
//                }
//                return false;
//            }
//        });


        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        lin_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = edit_username_red_bg.getText().toString().trim();
                email = edit_email_red_bg.getText().toString().trim();
                phoneNumber = edit_username_phone.getText().toString().trim();
                /*phoneCode = ccp.getSelectedCountryCode();*/
                phoneCode = "91";

                password = edit_password_red_bg.getText().toString().trim();
                //confirmPassword = edit_confirm_password_red.getText().toString().trim();

//                address_String = edttxt_address.getText().toString().trim();
                if (userName.isEmpty()) {
                    Toaster.customToast(getResources().getString(R.string.name_empty_validation));
                }else if(!userName.matches("[a-zA-Z0-9.? ]*")){
                    Toaster.customToast("Special character not allowed");
                }
                else if (userName.length() < 3) {
                    Toaster.customToast(getResources().getString(R.string.name_limit_validation));
                } else if (!Global.validateName(email)) {
                    Toaster.customToast(getResources().getString(R.string.email_empty_validation));
                } else if (!Global.isValidEmail(email)) {
                    Toaster.customToast(getResources().getString(R.string.email_invalid_validation));
                } else if (phoneNumber.isEmpty()) {
                    Toaster.customToast(getResources().getString(R.string.phone_error));
                }else if(!phoneNumber.matches("[0-9.? ]*")){
                    Toaster.customToast("Special character not allowed");
                }
                else if (!Global.isValidPhoneNumber(phoneNumber)) {
                    Toaster.customToast(getResources().getString(R.string.error_invalid_phone));
                } else if (password.isEmpty()) {
                    Toaster.customToast(getResources().getString(R.string.password_cannot_empty));
                } else if (password.length() < 6) {
                    Toaster.customToast(getResources().getString(R.string.password_too_short));
                }
//                else if (confirmPassword.isEmpty()) {
//                    Toaster.customToast(getResources().getString(R.string.conpassword_cannot_empty));
//                } else if (!password.equals(confirmPassword)) {
//                    Toaster.customToast(getResources().getString(R.string.confirm_password_mismatch));
//                }
                else if (drop_gender.getText().toString().trim().isEmpty()) {
                    Toaster.customToast("Select Gender");
                } else {
                    if (Global.isOnline(Signup.this)) {
                        signup_task();
                    } else {
                        Toaster.customToast(getResources().getString(R.string.no_internet));
                    }
//                    if (validatee()) {
//
//                    }

                }
            }
        });
    }

    private void createAccount(final String email, final String password) {
        progress.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            progress.dismiss();
                            Log.d("task data", task.getException().toString());
                            Toast.makeText(Signup.this, "Sign Up Failed : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        SessionManager.save_firebaseId(prefs, user.getUid());
        writeNewUser(user.getUid(), fname_String, user.getEmail());
    }

    private void writeNewUser(String userId, String sname, String semail) {
        FBUser FBUser = new FBUser(sname, semail, userId);
        mDatabase.child("users").child(userId).setValue(FBUser);

        signup_task(Global.URL, imagepath);
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Signup.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    openCameraid();
                } else if (items[item].equals("Gallery")) {
                    openGalleryid();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCameraid() {
        try {
            String fileName = "profile.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURIid = getContentResolver().insert(
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

                cursor = getContentResolver().query(selectedImageid, filePathColumn, null, null, null);
                cursor.moveToFirst();
                columnindex = cursor.getColumnIndex(filePathColumn[0]);
                file_pathid = cursor.getString(columnindex);
                // Log.e("Attachment Path:", attachmentFile);
                URIid = Uri.parse("file://" + file_pathid);
                imagepath = file_pathid;

                cursor.close();
                Log.e("TAG", "1234789" + imagepath);

                if (resultCode == 0) {
//                    dialog_camera.dismiss();
                } else {
//                    dialog_camera.dismiss();
                    System.out.println("cccccccc   " + imagepath);
                    profile_image.setImageURI(Uri.parse(imagepath));
//                    try {
//                        Glide.with(Signup.this).load(Uri.parse(imagepath)).into(profile_image);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    textViewidproof.setText(image_pathid);
                }

            } catch (Exception e) {

            }

        } else if (requestCode == CAMERA_REQUESTid) {
            try {
                String[] projection = {MediaStore.Images.Media.DATA};
                @SuppressWarnings("deprecation")
                Cursor cursor = managedQuery(mCapturedImageURIid, projection, null, null, null);
                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String capturedImageFilePath = cursor.getString(column_index_data);
                imagepath = capturedImageFilePath;

                if (resultCode == 0) {
//                    dialog_camera.dismiss();
                } else {
//                    dialog_camera.dismiss();
//                    profile_image.setImageURI(Uri.parse(imagepath));
                    Glide.with(Signup.this).load(imagepath).into(profile_image);
//                    textViewidproof.setText(image_pathid);
                    System.out.println("cccccccc   " + imagepath);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
//        try {
//            callbackmanager.onActivityResult(requestCode, resultCode, data);
//        } catch (Exception e) {
////            e.printStackTrace();
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void signup_task(String url, String path) {
        try {
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("action", new StringBody("registration"));
            entity.addPart("firstName", new StringBody(fname_String));
            entity.addPart("emailId", new StringBody(email_String));
            entity.addPart("password", new StringBody(password_String));
            entity.addPart("address", new StringBody(address_String));
            //entity.addPart("phone", new StringBody(address_String));
            if (spn_gender.getSelectedItemPosition() == 0) {
                entity.addPart("gender", new StringBody(""));
            } else {
                entity.addPart("gender", new StringBody(spn_gender.getSelectedItem().toString()));
            }
            entity.addPart("deviceToken", new StringBody(SessionManager.get_devicetoken(prefs)));
            entity.addPart("firebaseDeviceTocken", new StringBody(SessionManager.get_devicetoken(prefs)));
            entity.addPart("firebaseId", new StringBody(SessionManager.get_firebaseId(prefs)));
            if (!(path.equals("") || path == null)) {
                File file = new File(path);
                FileBody fileBody = new FileBody(file);
                entity.addPart("profile", fileBody);
            }
            Log.e("updateimage: ", entity.toString());
            progress.show();
            MultipartRequest req = new MultipartRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        progress.dismiss();
                        System.out.println(response);
                        JSONObject jsonObject2, jsonObject = new JSONObject(response);

                        if (jsonObject.getString("status").equals("Success")) {
                            jsonObject2 = jsonObject.getJSONObject("response");
                            SessionManager.save_user_id(prefs, jsonObject2.getString("id"));
                            SessionManager.save_fname(prefs, jsonObject2.getString("firstName"));
                            SessionManager.save_emailid(prefs, jsonObject2.getString("emailId"));
                            SessionManager.save_sex(prefs, jsonObject2.getString("gender"));
                            SessionManager.save_password(prefs, jsonObject2.getString("password2"));
                            SessionManager.save_address(prefs, jsonObject2.getString("address"));
                            SessionManager.save_mobile_verified(prefs, jsonObject2.getString("is_mobile_verified"));

                            if(jsonObject2.getString("profile_type")==null){
                                SessionManager.save_profiletype(prefs, "");
                            }else{
                                SessionManager.save_profiletype(prefs, jsonObject2.getString("profile_type"));
                            }

                            SessionManager.save_check_login(prefs, true);
                            SessionManager.save_image(prefs, jsonObject2.getString("image"));
                            congratsDialog();
//                            finish();


                        } else if (jsonObject.getString("status").equalsIgnoreCase("Fail")) {
                            Global.msgDialog(Signup.this, jsonObject.getString("msg"));
                        } else {
                            Global.msgDialog(Signup.this, "Error in server");
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    error.printStackTrace();
                    Global.msgDialog(Signup.this, "Error from Server");
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

    public void signup_task() {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "user_registration",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.tag("login response").e("%s", response);
// {"api_status": "200","api_text": "success","api_version": "1.4.4","message": "Successfully joined, Please wait..","success_type": "registered",
// "session_id": "c4ca4238a0b923820dcc509a6f75849b","cookie": "8ce3a486d73df920ee550f5a1a1abce332f9502ab55ffdc6dff36b892fe1133bc7c18a8621161794c344336196d5ec19bd54fd14befdde87",
// "user_id": "4834"}
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ;
                            if (jsonObject.getString("api_text").equalsIgnoreCase("success")) {
                                SessionManager.save_user_id(prefs, jsonObject.getString("user_id"));
                                SessionManager.save_session_id(prefs, jsonObject.getString("session_id"));

                                if(jsonObject.has("data")){
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


                                    if(jsonObjectData.getString("profile_type")==null){

                                        SessionManager.save_profiletype(prefs, jsonObjectData.getString("profile_type"));
                                    }else{
                                        SessionManager.save_profiletype(prefs, jsonObjectData.getString("profile_type"));
                                    }

                                SessionManager.save_check_login(prefs, true);

                                edit_username_red_bg.setText("");
                                edit_email_red_bg.setText("");
                                edit_password_red_bg.setText("");
                                edit_username_phone.setText("");
                                EmailOtpDialog(phoneNumber);
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
                param.put("username", userName);
                param.put("email", email);
                param.put("phone_number", phoneNumber);
                param.put("phone_code", phoneCode);
                if (profileType.equalsIgnoreCase("")) {
                    param.put("profile_type", "");
                } else {
                    param.put("profile_type", profileType.toLowerCase());
                }

                param.put("password", password);
                param.put("confirm_password", "");
                param.put("gender", gender.toLowerCase());
                param.put("device", "android");
                param.put("device_id", SessionManager.get_devicetoken(prefs));
                param.put("s", "1");
                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void gps_on() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Signup.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Turn On Location Services ");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        startActivityForResult(intent, 14);
                    }
                });
        alertDialog.setNegativeButton("Not Now",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Global.hideSoftKeyboard(activity);
        /*if (rel_user_bg_red.getVisibility() == View.VISIBLE) {
            edt_username.setVisibility(View.VISIBLE);
            rel_user_bg_red.setVisibility(View.GONE);
        } else if (rel_email_bg_red.getVisibility() == View.VISIBLE) {
            edtEmailId.setVisibility(View.VISIBLE);
            rel_email_bg_red.setVisibility(View.GONE);
        } else if (rel_password_bg_red.getVisibility() == View.VISIBLE) {
            rel_password_bg_red.setVisibility(View.GONE);
            edttxt_password.setVisibility(View.VISIBLE);
        } else if (rel_confirm_password_bg_red.getVisibility() == View.VISIBLE) {
            rel_confirm_password_bg_red.setVisibility(View.GONE);
            edit_confirm_password.setVisibility(View.VISIBLE);
        } else {
            rel_user_bg_red.setVisibility(View.GONE);
            rel_email_bg_red.setVisibility(View.GONE);
            rel_password_bg_red.setVisibility(View.GONE);
            rel_confirm_password_bg_red.setVisibility(View.GONE);
        }*/
        return true;
    }

    private boolean validatee() {
        boolean isOk = true;

        if (drop_type.getText().toString().isEmpty()) {
            Toaster.customToast("Select Profile Type!");
            isOk = false;
        }

        return isOk;
    }

    private void congratsDialog() {
        signup_card.setVisibility(View.GONE);
        rel_message.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Signup.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }, 2000);
        /*final Dialog dialog = new Dialog(Signup.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_congratulation);
        dialog.setCancelable(false);
        Button btContinue = dialog.findViewById(R.id.btn_continue);

        btContinue.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(Signup.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        dialog.show();*/
    }

    private void EmailOtpDialog(String mobile) {
        Dialog dialog = new Dialog(Signup.this);
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

                                congratsDialog();

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
}