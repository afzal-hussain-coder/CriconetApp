package com.pb.criconet.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pb.criconet.R;
import com.potyvideo.library.AndExoPlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import timber.log.Timber;

public class PlayRecordedVideoActivity extends AppCompatActivity {
    private AndExoPlayerView videoview;
    private String VideoURL = "", VideoURL2 = "";
    private int selected = 1;
    LinearLayout bottomPanel;
    Toolbar toolbar;
    String player_status="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_recorded_video);

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
        toolbarText.setText(getResources().getString(R.string.recorded_video).toUpperCase());

        videoview = (AndExoPlayerView) findViewById(R.id.VideoView);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            VideoURL = bundle.getString("data");
            videoview.setSource(VideoURL);

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoview.pausePlayer();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            videoview.pausePlayer();
            Objects.requireNonNull(getSupportActionBar()).hide();

        }else{
            Objects.requireNonNull(getSupportActionBar()).show();
        }
        super.onConfigurationChanged(newConfig);

    }
}