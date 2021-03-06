package com.pb.criconet.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import com.pb.criconet.R;
import com.pb.criconet.Utills.SessionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends Activity {
    SharedPreferences prefs;
    boolean permission = false;
    int PERMISSION_ALL = 1;
    Context mContext;
    String type="",booking_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
//        Fresco.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(Splash.this);
        //calculateHashKey("com.pb.criconet");
        //Log.d("DeviceToken",SessionManager.get_devicetoken(prefs));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if(bundle.containsKey("type")){
                type =bundle.getString("type");
            }
            if(bundle.containsKey("booking_id")){
                booking_id =bundle.getString("booking_id");
            }

          //Log.d("SpashNo",type+"/"+booking_id);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // only for gingerbread and newer versions
            if (!permission) {
                if (checkAndRequestPermissions()) {
                    // carry on the normal flow, as the case of  permissions  granted.
                    sendIntent();
                    permission = true;
                }
            }
        } else {
            sendIntent();
        }

    }

    private boolean checkAndRequestPermissions() {
        int access_network_state = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int readSDPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeSDPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int audioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int modify_audio_settings = ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS);
        int vibrate = ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE);
        int phoneCallPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);



        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (writeSDPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (readSDPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (vibrate != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.VIBRATE);
        }
        if (phoneCallPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }


        if (audioPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }

        if (access_network_state != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if (modify_audio_settings != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), PERMISSION_ALL);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            sendIntent();
        } else {
            finish();
        }
    }

    void sendIntent() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
               // Log.d("Status",SessionManager.get_check_login(prefs)+"");
                if (SessionManager.get_check_login(prefs)) {
//                    if (SessionManager.get_check_agreement(prefs)) {
//                        Intent intent = new Intent(Splash.this, Verification.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
                    if(type.equalsIgnoreCase("Coach_List")){
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        intent.putExtra("type", "Coach_List");
                        startActivity(intent);
                        finish();
                    }else if(type.equalsIgnoreCase("live_streaming")){
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        intent.putExtra("type", "live_streaming");
                        startActivity(intent);
                        finish();
                    } else if(type.equalsIgnoreCase("Booking")){
                        Intent intent = new Intent(Splash.this, BookingActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        /*if(type.equalsIgnoreCase("Booking")){
                            Intent intent = new Intent(Splash.this, BookingActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            if (booking_id.isEmpty()) {
//                                Intent intent = new Intent(Splash.this, BookingActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            } else {
//                                Intent intent = new Intent(Splash.this, BookingDetailsActivity.class);
//                                intent.putExtra("FROM", "2");
//                                intent.putExtra("BookingID", booking_id);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            }
                        }*/

                    }
                } else {

                    if(!SessionManager.get_check_login(prefs)){
                        Intent intent = new Intent(Splash.this, IntroSliderActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(Splash.this, Login.class);
                        startActivity(intent);
                        finish();
                    }

//                    Intent intent = new Intent(Splash.this, Welcome.class);

                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1000);
    }

    /*private void calculateHashKey(String yourPackageName) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    yourPackageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }*/

}