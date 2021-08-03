package com.pb.criconet.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.pb.criconet.R;
//import com.pb.criconet.Utills.StatusBarGradient;

//import com.afollestad.materialcamera.MaterialCamera;

public class Videos_View_Activity extends Activity {


    SharedPreferences prefs;
    RequestQueue queue;
    String path;
    ProgressDialog pDialog;
    VideoView videoview;
    String VideoURL = "";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_video_view);
//        StatusBarGradient.setStatusBarGradiant(Videos_View_Activity.this);

//        ((AppCompatActivity) Videos_View_Activity.this).getSupportActionBar().hide();
        videoview = (VideoView) findViewById(R.id.VideoView);
        path = getIntent().getStringExtra("path");
        System.out.println("id = " + path);

        VideoURL = path;

        pDialog = new ProgressDialog(Videos_View_Activity.this);
        pDialog.setTitle("Video Streaming");
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.show();

//        initializePlayer();

        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
            }
        });

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                getFragmentManager().popBackStack();
            }
        });

//        return rootview;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            videoview.pause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        initializePlayer();
    }

    private void initializePlayer() {

        try {
            MediaController mediacontroller = new MediaController(Videos_View_Activity.this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(VideoURL);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

//        Uri videoUri = getMedia(VIDEO_SAMPLE);
//        mVideoView.setVideoURI(videoUri);
//        mVideoView.start();

    }

    @Override
    protected void onStop() {
        super.onStop();

        releasePlayer();
    }

    private void releasePlayer() {
        videoview.stopPlayback();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//            questionVideo.setDimensions(displayHeight, displayWidth);
//            questionVideo.getHolder().setFixedSize(displayHeight, displayWidth);

        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

//            questionVideo.setDimensions(displayWidth, smallHeight);
//            questionVideo.getHolder().setFixedSize(displayWidth, smallHeight);

        }
    }


}
