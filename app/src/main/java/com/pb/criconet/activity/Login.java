package com.pb.criconet.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.models.UserData;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


/**
 * Created by Pradeep on 9/6/2016.
 */
@SuppressLint("LogNotTimber")
public class Login extends AppCompatActivity {

    SharedPreferences prefs;
    LinearLayout btn_login;
    EditText edttxt_email, edttxt_password;
    TextInputLayout til_edttxt_email, til_edttxt_password;
    String email_String, password_String;
    RequestQueue queue;
    //ProgressWheel progress_wheel;
    Location mylocation;
    ProgressDialog progress_dialog;
    boolean isGPSEnabled = false;
    String personid, personemail, personName, personPhotoUrl, logintype = "";
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    //    ImageView fb_login, twitter_login, insta_login;
    SignInButton gmail_login;
    LoginButton fb_login;

    HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private FusedLocationProviderClient mFusedLocationClient;
    private CallbackManager callbackmanager;

    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    // Afzal code here....
    TextView forgot_password, click_signup, edit_text_email, edit_text_password;
    EditText edit_text_email_red, editText_password_red;
    TextView txt_error_usernamem, txt_error_password;
    RelativeLayout rel_username_bg_red, rel_password_bg_red, rel_login, rel_login_error;
    ImageButton ib_visible_p;
    CustomLoaderView loaderView;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    private TextView fb;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarGradient.setStatusBarGradiant(Login.this);
        setContentView(R.layout.activity_login_screen);
        activity=this;
        enableLoc();


        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView toolbars = findViewById(R.id.toolbartext);
        toolbars.setText(R.string.login);
        mFusedLocationClient = getFusedLocationProviderClient(Login.this);
        /*Facebook Login*/
        callbackmanager = CallbackManager.Factory.create();
        /*Gmail Login*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1072621342122-hrdbvme9dvs47eq072q7d492ruu01ahj.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        initializeView();

    }

    /*This method is used for enabled GPS*/
    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(Login.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(Login.this, REQUEST_LOCATION);

//                                finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }

    }

    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        getAddress(location.getLatitude(),location.getLongitude());
        // You can now create a LatLng Object for use with maps
    }

    /*private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(Login.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation =LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, new com.google.android.gms.location.LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    mylocation = location;
                                    if (mylocation != null) {
                                        Double latitude=mylocation.getLatitude();
                                        Double longitude=mylocation.getLongitude();

                                        getAddress(latitude,longitude);

                                    }
                                }
                            });
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(Login.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(Login.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }*/

    private void initializeView() {
        fb = findViewById(R.id.fb);
        fb_login = (LoginButton) findViewById(R.id.fb_login);
        gmail_login = (SignInButton) findViewById(R.id.gmail_login);
        setGooglePlusButtonText(gmail_login,getResources().getString(R.string.Continue_with_Google));

        click_signup = findViewById(R.id.txt_register);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        btn_login = findViewById(R.id.btn_login);
        queue = Volley.newRequestQueue(Login.this);
        //progress_wheel = (ProgressWheel) findViewById(R.id.progress_wheel);

        // Afzal code here..

        edit_text_email = findViewById(R.id.edit_text_email);
        edit_text_email_red = findViewById(R.id.edit_text_email_red);
        edit_text_password = findViewById(R.id.edit_text_password);
        editText_password_red = findViewById(R.id.editText_password_red);
        ib_visible_p = findViewById(R.id.ib_visible_p);
        ib_visible_p.setTag("InVisible");
        ib_visible_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ib_visible_p.getTag().equals("InVisible")) {
                    editText_password_red.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ib_visible_p.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_black_24dp));
                    ib_visible_p.setTag("Visible");
                } else {
                    editText_password_red.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ib_visible_p.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_black_24dp));
                    ib_visible_p.setTag("InVisible");
                }
            }
        });

        txt_error_usernamem = findViewById(R.id.txt_error_username);
        txt_error_password = findViewById(R.id.txt_error_password);

        rel_username_bg_red = findViewById(R.id.rel_username_bg_red);
        rel_password_bg_red = findViewById(R.id.rel_password_bg_red);
        rel_login = findViewById(R.id.rel_login);
        rel_login_error = findViewById(R.id.rel_login_error);

        edit_text_email.setOnClickListener(v -> {
            edit_text_email.setVisibility(View.GONE);
            rel_username_bg_red.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            edit_text_email_red.requestFocus();
            imm.showSoftInput(edit_text_email_red, InputMethodManager.SHOW_FORCED);
        });
        edit_text_password.setOnClickListener(v -> {
            edit_text_password.setVisibility(View.GONE);
            rel_password_bg_red.setVisibility(View.VISIBLE);

        });

        edit_text_email_red.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edit_text_password.setVisibility(View.GONE);
                    rel_password_bg_red.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        edit_text_email_red.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txt_error_usernamem.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if (check_for_word(s.toString())==true){
                    txt_error_usernamem.setVisibility(View.VISIBLE);
                    txt_error_usernamem.setText(getResources().getString(R.string.welcome));
                    txt_error_usernamem.setTextColor(getResources().getColor(R.color.green));
                }else{
                    txt_error_usernamem.setVisibility(View.VISIBLE);
                    txt_error_usernamem.setText(getResources().getString(R.string.error_register_message));
                    txt_error_usernamem.setTextColor(getResources().getColor(R.color.edit_text_color));
                }*/
            }
        });

        editText_password_red.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 6) {
                    txt_error_password.setVisibility(View.VISIBLE);
                    txt_error_password.setText(getResources().getString(R.string.password_too_short));
                    txt_error_password.setTextColor(getResources().getColor(R.color.red));
                } else {
                    txt_error_password.setVisibility(View.GONE);

                }

            }
        });

        click_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionManager.get_select_type(prefs).equals("M")) {
                    Intent intent = new Intent(Login.this, Signup.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Login.this, Signup.class);
                    startActivity(intent);
                }
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);/**/
                startActivity(intent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        edit_text_email_red.setText("");
                        editText_password_red.setText("");
                        edit_text_email_red.setClickable(true);
                        edit_text_email.setVisibility(View.VISIBLE);
                        rel_username_bg_red.setVisibility(View.GONE);
                        edit_text_password.setVisibility(View.VISIBLE);
                        rel_password_bg_red.setVisibility(View.GONE);

                        txt_error_password.setVisibility(View.GONE);
                    }
                }, 1000);


            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.printKeyHash(activity);
                email_String = edit_text_email_red.getText().toString().trim();
                password_String = editText_password_red.getText().toString().trim();
                if (!Global.validateName(email_String)) {
                    Toaster.customToast(getResources().getString(R.string.Username_and_email_id_cant_be_empty));
                    edit_text_email.setVisibility(View.GONE);
                    rel_username_bg_red.setVisibility(View.VISIBLE);
                    edit_text_email_red.requestFocus();
                    edit_text_email_red.setCursorVisible(true);
                } else if (!Global.validateName(password_String)) {
                    Toaster.customToast(getResources().getString(R.string.The_Password_Cant_be_empty));
                    edit_text_password.setVisibility(View.GONE);
                    rel_password_bg_red.setVisibility(View.VISIBLE);
                    editText_password_red.requestFocus();
                    editText_password_red.setCursorVisible(true);
                } else {
                    if (Global.isOnline(Login.this)) {
                        login_task(email_String, password_String);


//                        LocationManager locationManager = (LocationManager) Login.this.getSystemService(LOCATION_SERVICE);
//                        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//                        if (isGPSEnabled) {
                        //// loginWithFirebase(email_String, password_String);
//                        SessionManager.save_check_login(prefs, true);
//                            Intent intent = new Intent(Login.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();

//                        } else {
//                            gps_on();
//                        }
                    } else {
                        Toast.makeText(Login.this, R.string.no_internet, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

//        fb_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onFblogin();
//            }
//        });
//        twitter_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), Twitterpage.class);
//                startActivity(i);
//            }
//        });
//        insta_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                connectOrDisconnectUser();
//            }
//        });


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Login.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                SessionManager.save_devicetoken(prefs, newToken);
                Log.e("newToken", newToken);
            }
        });


        gmail_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        //fb_login.setReadPermissions(Arrays.asList("email", "public_profile"));
        fb_login.setPermissions(Arrays.asList("email", "public_profile"));
        // If you are using in a fragment, call loginButton.setFragment(this);
        callbackmanager = CallbackManager.Factory.create();
        fb_login.registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("FB Login Button:", "login Success");
                performFbLoginOrSignUp(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                System.out.println("Login details = ");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });
    }

    private void googleSignIn() {
//        Auth.GoogleSignInApi.signOut(mGoogleSignInClient);
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            boolean gps_enabled = false;
                            boolean network_enabled = false;

                            try {
                                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            } catch (Exception ex) {
                            }

                            try {
                                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                            } catch (Exception ex) {
                            }

                            if (!gps_enabled && !network_enabled) {
//                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                startActivity(intent);
                                new AlertDialog.Builder(Login.this)
                                        .setMessage(R.string.gps_network_not_enabled)
                                        .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                                finish();
                                            }
                                        })
                                        .show();
                            } else {
                                mFusedLocationClient.getLastLocation().addOnSuccessListener(Login.this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            // Logic to handle location object
                                            mylocation = location;
//                                        Toast.makeText(Login.this, "" + mylocation.getLatitude() + " - " + mylocation.getLongitude(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            mylocation = new Location("DummyLocation");
                                            mylocation.setLatitude(0.0);
                                            mylocation.setLongitude(0.0);
                                        }
                                    }
                                });
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            Global.showSettingsDialog(Login.this);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();*/
    }

    public void login_task(final String email_String, final String password_String) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "user_login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("login reponse", "" + response);
                        loaderView.hideLoader();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                edit_text_email_red.setText("");
                                editText_password_red.setText("");
                                txt_error_password.setVisibility(View.GONE);
                                SessionManager.save_user_id(prefs, jsonObject.optString("user_id"));
                                SessionManager.save_session_id(prefs, jsonObject.optString("session_id"));
                                SessionManager.save_name(prefs, email_String);
                                SessionManager.save_emailid(prefs, email_String);
                                SessionManager.save_password(prefs, password_String);
//                                SessionManager.save_session_id(prefs, jsonObject.optString("session_id"));
//                                SessionManager.save_image(prefs, jsonObject.optString("i"));
                                SessionManager.save_check_login(prefs, true);

                                JSONObject jsonData=jsonObject.optJSONObject("data");
                                UserData userData = UserData.fromJson(jsonObject.optJSONObject("data"));
                                SessionManager.save_user_id(prefs, userData.getUser_id());
                                SessionManager.save_name(prefs, userData.getUsername());
                                SessionManager.save_emailid(prefs, userData.getEmail());
                                SessionManager.save_sex(prefs, userData.getGender());
                                SessionManager.save_address(prefs, userData.getAddress());
                                SessionManager.save_image(prefs, userData.getAvatar());
                                SessionManager.save_cover(prefs, userData.getCover());
                                SessionManager.save_fname(prefs, userData.getFirst_name());
                                SessionManager.save_lname(prefs, userData.getLast_name());
                                SessionManager.save_profiletype(prefs, userData.getProfile_type());
                                SessionManager.save_mobile_verified(prefs, userData.getIs_mobile_verified());
                                //Toaster.customToast(SessionManager.get_mobile_verified(prefs));
                                if(jsonData.has("ambassadorProfile")){
                                   JSONObject ambassadorProfile = jsonData.getJSONObject("ambassadorProfile");

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
                                   //Toaster.customToast(ambassadorProfile.length()+"");
                                }
                                if(userData.getProfile_type().equalsIgnoreCase("coach")){

                                    if(jsonData.has("coachProfile")){
                                        JSONObject  jsonObject1= jsonData.getJSONObject("coachProfile");
                                        SessionManager.save_coach_id(prefs, jsonObject1.getString("coach_id"));
                                    }
                                    

                                }else{
                                    SessionManager.save_coach_id(prefs,"");
                                }

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {

                                Toaster.customToast(jsonObject.optJSONObject("errors").optString("error_text"));
                                //Global.msgDialog(Login.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Toaster.customToast(getResources().getString(R.string.socket_timeout));
                                //Global.msgDialog(Login.this, "Error in server");
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
//                Global.msgDialog(Login.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("username", email_String);
                param.put("password", password_String);
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

    public void signup_tasksocial(String url, final String name_string, final String email_String, final String logintype, final String personid, final String image) {
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loaderView.hideLoader();
                        Log.d("socialLogin",response);
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
                                SessionManager.save_user_id(prefs, jsonObject.optString("user_id"));
                                SessionManager.save_session_id(prefs, jsonObject.optString("session_id"));
                                SessionManager.save_check_login(prefs, true);
                                JSONObject jsonData=jsonObject.optJSONObject("data");
                                UserData userData = UserData.fromJson(jsonObject.optJSONObject("data"));
                                SessionManager.save_user_id(prefs, userData.getUser_id());
                                SessionManager.save_name(prefs, userData.getUsername());
                                SessionManager.save_emailid(prefs, userData.getEmail());
                                SessionManager.save_sex(prefs, userData.getGender());
                                SessionManager.save_address(prefs, userData.getAddress());
                                SessionManager.save_image(prefs, userData.getAvatar());
                                SessionManager.save_cover(prefs, userData.getCover());
                                SessionManager.save_profiletype(prefs, userData.getProfile_type());
                                SessionManager.save_mobile_verified(prefs, userData.getIs_mobile_verified());

                                if(userData.getProfile_type().equalsIgnoreCase("coach")){
                                    JSONObject  jsonObject1= jsonData.getJSONObject("coachProfile");
                                    SessionManager.save_coach_id(prefs, jsonObject1.getString("coach_id"));
                                }else{
                                    SessionManager.save_coach_id(prefs,"");
                                }
                                edit_text_email_red.setText("");
                                editText_password_red.setText("");

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Toaster.customToast(getResources().getString(R.string.message_internal_inconsistency));
                                //Global.msgDialog(Login.this, jsonObject.optJSONObject("errors").optString("error_text"));
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("provider", logintype);
                param.put("identifier", personid);
                param.put("first_name", name_string);
                param.put("email", email_String);
                param.put("device", "android");

                param.put("device_id", SessionManager.get_devicetoken(prefs));
                param.put("deviceToken", SessionManager.get_devicetoken(prefs));
                param.put("firebaseDeviceTocken", SessionManager.get_devicetoken(prefs));
                param.put("firebaseId", SessionManager.get_firebaseId(prefs));
                param.put("photoURL", image);

//                param.put("socialMediaProfile", location_string);
//                param.put("phone", phone_string);
//                param.put("address", location_string);

                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Signed in successfully, show authenticated UI.
                updateUI(account);
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                updateUI(null);
            }
        } else if (requestCode == REQUEST_LOCATION) {

            switch (resultCode) {
                case Activity.RESULT_OK:
                    //getAddress();
                    //startLocationUpdates();
                    break;
                case Activity.RESULT_CANCELED:
                    finish();
                    break;
            }
        } else {
            try {
                callbackmanager.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            String name = account.getDisplayName();
            String imageurl = "";
            try {
                Uri uri = account.getPhotoUrl();
                if (uri != null)
                    imageurl = new URL(account.getPhotoUrl().toString()).toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            String email = account.getEmail();
            String id = account.getId();
            String logintype = "Google";

            Log.e(TAG, "updateUI name: " + name);
            Log.e(TAG, "updateUI imageurl: " + imageurl);
            Log.e(TAG, "updateUI email: " + email);
            Log.e(TAG, "updateUI id: " + id);

            signup_tasksocial(Global.URL + "social_login", name, email, logintype, id, imageurl);

        } else {
            Toast.makeText(this, getResources().getString(R.string.SignIn_failed), Toast.LENGTH_SHORT).show();
        }
    }

    private void performFbLoginOrSignUp(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        // TODO Auto-generated method stub
                        if (response.getError() != null) {
                            // handle error
                            System.out.println("ERROR");
                        } else {
                            System.out.println("Success");
                            try {
                                String jsonresult = String.valueOf(json);
                                System.out.println("JSON Result" + jsonresult);
                                String personid = json.getString("id");
                                String personemail = json.optString("email");
                                String personName = json.getString("first_name");
                                String personPhotoUrl = "https://graph.facebook.com/" + personid + "/picture?type=large";
                                System.out.println("Login details = " + personid + " " + personemail + " " + personName+" "+personPhotoUrl);
                                String logintype = "Facebook";
                                fb.setText(R.string.facebook_logout);

                                signup_tasksocial(Global.URL + "social_login", personName, personemail, logintype, personid, personPhotoUrl);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void onFblogin() {
        callbackmanager = CallbackManager.Factory.create();
        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        // TODO Auto-generated method stub
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            try {
                                                String jsonresult = String.valueOf(json);

                                                System.out.println("JSON Result" + jsonresult);
                                                personid = json.optString("id");
                                                personemail = json.optString("email");
                                                personName = json.optString("first_name");
                                                System.out.println("Login details = " + personid + " " + personemail + " " + personName);
                                                personPhotoUrl = "https://graph.facebook.com/" + personid + "/picture?type=large";
                                                logintype = "FACEBOOK";

//                                                signup_tasksocial(Global.URL, personName, personemail, logintype, personid);
//                                                createAccount(personemail, personemail);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,last_name,link,email,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("Login details = ");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        error.printStackTrace();
                    }
                });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Global.hideSoftKeyboard(activity);
        /*if (rel_username_bg_red.getVisibility() == View.VISIBLE) {
            edit_text_email_red.setClickable(true);
            edit_text_email.setVisibility(View.VISIBLE);
            rel_username_bg_red.setVisibility(View.GONE);
        } else if (rel_password_bg_red.getVisibility() == View.VISIBLE) {
            edit_text_password.setVisibility(View.VISIBLE);
            rel_password_bg_red.setVisibility(View.GONE);
        } else {

        }*/

        txt_error_password.setVisibility(View.GONE);
        return true;
    }

    public void onClick(View view) {
        fb_login.performClick();
    }

    public void getAddress(Double latitude,Double Longitude) {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> listAddresses = null;
        try {
            listAddresses = geocoder.getFromLocation(latitude, Longitude, 1);
            String countryCode = listAddresses.get(0).getCountryCode();
            SessionManager.saveCountryCode(prefs,countryCode);
            //Toaster.customToast(countryCode);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                tv.setTextSize(14);
                tv.setTextColor(activity.getResources().getColor(R.color.bckground));
                Typeface face = Typeface.createFromAsset(getAssets(),
                        "fonts/OpenSans_Semibold.ttf");
                tv.setTypeface(face);
                return;
            }
        }
    }

}

