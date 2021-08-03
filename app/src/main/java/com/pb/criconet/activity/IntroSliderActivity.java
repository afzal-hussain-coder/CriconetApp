package com.pb.criconet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pb.criconet.R;
import com.pb.criconet.Utills.IndicatorLayout;
import com.pb.criconet.Utills.IntroSliderAdapter;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.fragments.Intro1Fragment;
import com.pb.criconet.fragments.Intro2Fragment;
import com.pb.criconet.fragments.Intro3Fragment;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

import static com.google.android.gms.common.util.CollectionUtils.listOf;


public class IntroSliderActivity extends AppCompatActivity {
    private final ArrayList fragmentList = new ArrayList();
    private HashMap _$_findViewCache;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 200;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000;
    TextView tv_getting;
    SharedPreferences prefs;
    boolean permission = false;
    int PERMISSION_ALL = 1;
    Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        }

        setContentView(R.layout.activity_intro_slider);
        mContext = this;
        ;
//        Fresco.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        setViewPager();

        registerListeners();

    }

    private void setViewPager() {
        IntroSliderAdapter adapter = new IntroSliderAdapter(this);
        ViewPager2 var4 = findViewById(R.id.vpIntroSlider);
        tv_getting = findViewById(R.id.tv_getting);
        var4.setAdapter(adapter);
        fragmentList.addAll(CollectionsKt.listOf(new Intro1Fragment(), new Intro2Fragment()));  // add into3 fragment if require , new Intro3Fragment()
        adapter.setFragmentList(fragmentList);
        ((IndicatorLayout) findViewById(R.id.indicatorLayout)).setIndicatorCount(adapter.getItemCount());
        ((IndicatorLayout) findViewById(R.id.indicatorLayout)).selectCurrentPosition(0);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == fragmentList.size()) {
                    currentPage = 0;
                }
                var4.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    private void registerListeners() {
      try{
          tv_getting.setOnClickListener(v -> {
              sendIntent();
          });
      }catch (Exception e){
          e.printStackTrace();
      }


    }




    void sendIntent() {

        if (SessionManager.get_check_login(prefs)) {
//                    if (SessionManager.get_check_agreement(prefs)) {
//                        Intent intent = new Intent(Splash.this, Verification.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
            Intent intent = new Intent(IntroSliderActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
//                    }
        } else {
//                    Intent intent = new Intent(Splash.this, Welcome.class);
            Intent intent = new Intent(IntroSliderActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

    }

}
