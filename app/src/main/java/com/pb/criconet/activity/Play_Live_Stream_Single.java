package com.pb.criconet.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pb.criconet.R;
import com.pb.criconet.Utills.Toaster;
import com.potyvideo.library.AndExoPlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import timber.log.Timber;


public class Play_Live_Stream_Single extends AppCompatActivity {

    private String stream_name;
    private AndExoPlayerView videoview;
    private AndExoPlayerView videoview2;
    private String VideoURL = "", VideoURL2 = "";
    private int selected = 1;
    private TextView cam_1, cam_2, cam_both, desc;
    LinearLayout bottomPanel;
    Toolbar toolbar;
    String player_status="";
    FrameLayout btnShare;
    boolean installed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_live_view4);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        TextView toolbarText = (TextView) toolbar.findViewById(R.id.toolbartext);


        videoview = (AndExoPlayerView) findViewById(R.id.VideoView);

        videoview2 = (AndExoPlayerView) findViewById(R.id.VideoView2);
        cam_1 = (TextView) findViewById(R.id.cam_1);
        cam_2 = (TextView) findViewById(R.id.cam_2);
        cam_both = (TextView) findViewById(R.id.cam_both);
        desc = (TextView) findViewById(R.id.desc);
        bottomPanel = (LinearLayout) findViewById(R.id.bottomPanel);
        btnShare = findViewById(R.id.btnShare);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                JSONObject object = new JSONObject(bundle.getString("data"));
                Timber.e("Data : %s", object);
                VideoURL = object.getString("first_player_link");
                VideoURL2 = object.getString("second_player_link");
                player_status = object.optString("second_player_status");
                if(player_status.equalsIgnoreCase("0")){
                    cam_1.setVisibility(View.VISIBLE);
                    cam_2.setVisibility(View.GONE);
                    cam_both.setVisibility(View.GONE);
                }else {
                    cam_1.setVisibility(View.VISIBLE);
                    cam_2.setVisibility(View.VISIBLE);
                    cam_both.setVisibility(View.VISIBLE);
                }


//                VideoURL = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
//                VideoURL2 = "https://content.jwplatform.com/manifests/yp34SRmf.m3u8";
//                VideoURL2 = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";

                toolbarText.setText(object.optString("title"));
                if (object.optString("is_power").equalsIgnoreCase("1")){
                    desc.setText(object.optString("power_msg"));
                }
                else{
                    desc.setText(object.optString("desc"));
                }

//                name1.setText(object.optString("first_player"));
//                name2.setText(object.optString("second_player"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        cam_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchSelected(1);
            }
        });
        cam_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchSelected(2);
            }
        });
        cam_both.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchSelected(3);
            }
        });


        btnShare.setOnClickListener(v -> {
            Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
            String app_url = "http://play.google.com/store/apps/details?id=" + Play_Live_Stream_Single.this.getPackageName();
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });



        SwitchSelected(selected);
//        videoview.requestFocus();
//        videoview.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoview.pausePlayer();
        videoview2.pausePlayer();
    }

    void SwitchSelected(int i) {
        switch (i) {
            case 1:
                videoview.setSource(VideoURL);
                videoview2.pausePlayer();
                videoview2.setVisibility(View.GONE);
                cam_1.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_2.setTextColor(getResources().getColor(R.color.bckground));
                cam_both.setTextColor(getResources().getColor(R.color.bckground));
                cam_1.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
                cam_2.setBackground(getResources().getDrawable(R.drawable.round_border_red));
                cam_both.setBackground(getResources().getDrawable(R.drawable.round_border_red));
                selected = 1;
                break;
            case 2:
                videoview.setSource(VideoURL2);

                videoview2.pausePlayer();
                videoview2.setVisibility(View.GONE);
                cam_1.setTextColor(getResources().getColor(R.color.bckground));
                cam_2.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_both.setTextColor(getResources().getColor(R.color.bckground));
                cam_1.setBackground(getResources().getDrawable(R.drawable.round_border_red));
                cam_2.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
                cam_both.setBackground(getResources().getDrawable(R.drawable.round_border_red));
                selected = 2;
                break;
            case 3:
                videoview.setSource(VideoURL);

                videoview2.setVisibility(View.VISIBLE);
                videoview2.setSource(VideoURL2);
                cam_1.setTextColor(getResources().getColor(R.color.bckground));
                cam_2.setTextColor(getResources().getColor(R.color.bckground));
                cam_both.setTextColor(getResources().getColor(R.color.light_text_color));
                cam_1.setBackground(getResources().getDrawable(R.drawable.round_border_red));
                cam_2.setBackground(getResources().getDrawable(R.drawable.round_border_red));
                cam_both.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
                selected = 3;
                break;
            default:
                videoview.setSource(VideoURL);

                videoview2.pausePlayer();
                videoview2.setVisibility(View.GONE);

                selected = 1;
                break;
        }


    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (selected == 3)
                videoview2.pausePlayer();
            getSupportActionBar().hide();
            bottomPanel.setVisibility(View.GONE);
            btnShare.setVisibility(View.GONE);
            desc.setVisibility(View.GONE);
        } else {
            getSupportActionBar().show();
            btnShare.setVisibility(View.VISIBLE);
            bottomPanel.setVisibility(View.VISIBLE);
            desc.setVisibility(View.VISIBLE);
        }
        super.onConfigurationChanged(newConfig);

    }

}
