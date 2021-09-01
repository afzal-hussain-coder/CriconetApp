package com.pb.criconet.activity;

import android.drm.DrmStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.Utills.YoutubeConfig;

import javax.security.auth.login.LoginException;

import timber.log.Timber;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements  YouTubePlayer.OnInitializedListener{
    private static final String TAG = "YoutubePlayerActivity";
    YouTubePlayerView player_view;
    //YouTubePlayer.OnInitializedListener mOnInitializedListener;

    // New code here..
    //http://youtu.be/<VIDEO_ID>
    public String VIDEO_ID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText("Recorded Matches");
        Bundle bundle = getIntent().getExtras();
        VIDEO_ID=getVideoKey(bundle.getString("url"));
        player_view = (YouTubePlayerView) findViewById(R.id.player_view);
        player_view.initialize(YoutubeConfig.getApi_Key(), this);


//
//        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                Timber.d("onInitializationSuccess: ");
//                Bundle bundle = getIntent().getExtras();
//                youTubePlayer.loadVideo(getVideoKey(bundle.getString("url")));
//                //youTubePlayer.setFullscreen(true);
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                Timber.d("onInitializationFailure: ");
//            }
//        };
//
//        player_view.initialize(YoutubeConfig.getApi_Key(), mOnInitializedListener);

    }

    private String getVideoKey(String url) {
        String key = "";
        if (url.contains("v=")) {
            key=url.substring(url.lastIndexOf("v=")+2);
        }else{
            key=url.substring(url.lastIndexOf("/")+1);
        }
        Timber.e(key);
        return key;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
       /** add listeners to YouTubePlayer instance **/
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.setFullscreen(true);
        /** Start buffering **/
        if (!b) {
            youTubePlayer.cueVideo(VIDEO_ID);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toaster.customToast("Failed to Initialize!");
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
        }
    };
}
