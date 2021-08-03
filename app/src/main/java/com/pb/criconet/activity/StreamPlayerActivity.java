package com.pb.criconet.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.YoutubeConfig;

import timber.log.Timber;

public class StreamPlayerActivity extends AppCompatActivity {
    private static final String TAG = "YoutubePlayerActivity";
    YouTubePlayerView player_view;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText("Recorded Matches");

        player_view = (YouTubePlayerView) findViewById(R.id.player_view);

//        Bundle bundle = getIntent().getExtras();
//        getVideoKey(bundle.getString("url"));

        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Timber.d("onInitializationSuccess: ");
                Bundle bundle = getIntent().getExtras();
                youTubePlayer.loadVideo(getVideoKey(bundle.getString("url")));
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Timber.d("onInitializationFailure: ");
            }
        };

        player_view.initialize(YoutubeConfig.getApi_Key(), mOnInitializedListener);


    }

    private String getVideoKey(String url) {
        String key = "";
        if (url.contains("v=")) {
            key=url.substring(url.lastIndexOf("v=")+1);
        }else{
            key=url.substring(url.lastIndexOf("/")+1);
        }
        Timber.e(key);
        Log.e(TAG,key);
        return key;
    }
}
