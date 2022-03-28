package com.pb.criconet.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.loadingview.LoadingView;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.layout.GridVideoViewContainer;
import com.pb.criconet.layout.InChannelMessageListAdapter;
import com.pb.criconet.layout.SmallVideoViewAdapter;
import com.pb.criconet.layout.SmallVideoViewDecoration;
import com.pb.criconet.logger.LoggerRecyclerView;
import com.pb.criconet.models.AGEventHandler;
import com.pb.criconet.models.ConstantApp;
import com.pb.criconet.models.DuringCallEventHandler;
import com.pb.criconet.models.FeedBackFormChildData;
import com.pb.criconet.models.Message;
import com.pb.criconet.propeller.Constant;
import com.pb.criconet.propeller.UserStatusData;
import com.pb.criconet.propeller.ui.RecyclerItemClickListener;
import com.pb.criconet.propeller.ui.RtlLinearLayoutManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.ss.ScreenSharingClient;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import timber.log.Timber;

public class CallActivity extends BaseActivity implements DuringCallEventHandler {
    private final String TAG = CallActivity.class.getSimpleName();
    public static final int LAYOUT_TYPE_DEFAULT = 0;
    public static final int LAYOUT_TYPE_SMALL = 1;
    private final HashMap<Integer, SurfaceView> mUidsList = new HashMap<>(); // uid = 0 || uid == EngineConfig.mUid
    public int mLayoutType = LAYOUT_TYPE_DEFAULT;
    private GridVideoViewContainer mGridVideoViewContainer;
    private RelativeLayout mSmallVideoViewDock;
    private volatile boolean mVideoMuted = false;
    private volatile boolean mAudioMuted = false;
    private volatile boolean mMixingAudio = false;
    private volatile int mAudioRouting = Constants.AUDIO_ROUTE_DEFAULT;
    private volatile boolean mFullScreen = false;
    private boolean mIsLandscape = false;
    private InChannelMessageListAdapter mMsgAdapter;
    private ArrayList<Message> mMsgList;
    private SmallVideoViewAdapter mSmallVideoViewAdapter;
    private final Handler mUIHandler = new Handler();
    String channelName, accessToken;
    private SharedPreferences prefs;
    com.pb.criconet.chatModel.User user;
    String image_profile;
    private LoggerRecyclerView mLogView;
    String userId = "", coachId = "", coachName = "";
    TextView log_text, log_texth_other;
    LinearLayout lin_log;
    String bookingId = "", joinType = "";
    long timeDuration;
    private RequestQueue queue;
    Activity mActivity;
    TextView tv_timeDuration;
    ArrayList<FeedBackFormChildData> feedBackFormChildData = null;

    //WebView web_chat;
    private SlideUp slideUp;
    private View dim, rootView;
    private View slideView;
    String webUrl = "", FROM = "";
    String coach_id = "";

    private final static int FCR = 1;
    WebView webView;
    LoadingView progressBar;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    WebSettings webSettings;
    TextView tv_count;
    RelativeLayout rl_count;
    RelativeLayout rl_share_screen;
    TextView tv_doubleClick;
    LinearLayout li_tap;
    boolean mSS;
    int shareId=0;


    // End of ScreenSharing
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;

            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {

                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {

            if (requestCode == FCR) {
                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }



    //...share.
    private VideoEncoderConfiguration mVEC;
    private ScreenSharingClient mSSClient;
    private final ScreenSharingClient.IStateListener mListener = new ScreenSharingClient.IStateListener() {
        @Override
        public void onError(int error) {
            Log.e("LOG_TAG", "Screen share service error happened: " + error);
        }

        @Override
        public void onTokenWillExpire() {
            Log.d("LOG_TAG", "Screen share service token will expire");
            mSSClient.renewToken(accessToken); // Replace the token with your valid token
        }
    };

//    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
//
//        @Override
//        public void onUserOffline(int uid, int reason) {
//            Log.d(LOG_TAG, "onUserOffline: " + uid + " reason: " + reason);
//        }
//
//        @Override
//        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
//            Log.d(LOG_TAG, "onJoinChannelSuccess: " + channel + " " + elapsed);
//        }
//
//        @Override
//        public void onUserJoined(final int uid, int elapsed) {
//            Log.d(LOG_TAG, "onUserJoined: " + (uid&0xFFFFFFL));
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if(uid == com.pb.criconet.sharedscreen.Constant.SCREEN_SHARE_UID) {
//                        setupRemoteView(uid);
//
//                    }
//                }
//            });
//        }
//    };

    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        mActivity = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);
        user = new com.pb.criconet.chatModel.User(SessionManager.get_user_id(prefs));
        userId = getIntent().getStringExtra("UserId");

        Log.d("USERID",userId+"/"+SessionManager.get_user_id(prefs));
        coachId = getIntent().getStringExtra("CoachId");
        coachName = getIntent().getStringExtra("Name");
        FROM = getIntent().getStringExtra("FROM");
        //bookingId =getIntent().getStringExtra("booking_id");
        bookingId = getIntent().getStringExtra("id");
        timeDuration = getIntent().getLongExtra("timeDuration", 0);
        image_profile = SessionManager.get_image(prefs);
        log_text = findViewById(R.id.log_text);
        log_texth_other = findViewById(R.id.log_texth_other);
        lin_log = findViewById(R.id.lin_log);
        tv_timeDuration = findViewById(R.id.tv_timeDuration);
        rl_share_screen = findViewById(R.id.rl_share_screen);
        ///user ........profiletype
        ///coach
        li_tap = findViewById(R.id.li_tap);
        tv_doubleClick = (TextView) findViewById(R.id.tv_doubleClick );
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(100); //You can manage the blinking time with this parameter
        anim.setStartOffset(50);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tv_doubleClick.startAnimation(anim);

        rl_count = findViewById(R.id.rl_count);
        tv_count = findViewById(R.id.tv_count);
        webView = findViewById(R.id.web_chat);
        progressBar = findViewById(R.id.loadingVieww);
        webView.setWebViewClient(new Callback());
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        assert webView != null;

        webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        rootView = findViewById(R.id.root_view);
        slideView = findViewById(R.id.slideView);
        dim = findViewById(R.id.dim);
        slideUp = new SlideUpBuilder(slideView)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                        dim.setAlpha(1 - (percent / 100));
                        if (percent < 100) {
                            // slideUp started showing
                            //Toaster.customToast("show");
                            loadWebView();
                        }
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                            //Toaster.customToast("gone");
                            if (Build.VERSION.SDK_INT < 18) {
                                webView.clearView();
                            } else {
                                webView.loadUrl("about:blank");
                            }
                        }
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(rootView)
                .build();

        //........End Web Chat...

        //user.setFireDisplayName(image_link);

        mSSClient = ScreenSharingClient.getInstance();
        mSSClient.setListener(mListener);

        mVEC = new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_840x480,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_24,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_ADAPTIVE);

        /*rl_share_screen.setOnClickListener(view -> {
            shareType ="Share";


            mSSClient.start(getApplicationContext(), getResources().getString(R.string.agora_app_id), accessToken,
                    channelName, com.pb.criconet.sharedscreen.Constant.SCREEN_SHARE_UID, mVEC);

//            startActivity(new Intent(mActivity, CallActivity.class)
//                    .putExtra("UserId", userId)
//                    .putExtra("CoachId", coachId)
//                    .putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, channelName)
//                    .putExtra("Name", coachName)
//                    .putExtra("booking_id", bookingId)
//                    .putExtra("timeDuration", timeDuration)
//                    .putExtra("AccessToken", accessToken)
//                    .putExtra("FROM", "1"));
//           finish();
            //doLeaveChannel();
        });*/

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ab.setCustomView(R.layout.ard_agora_actionbar_with_title);
        }

        if (Global.isOnline(mActivity)) {
            getSessionFeedbackForm();
        } else {
            Global.showDialog(mActivity);
        }

        callChatApi();

    }

    public void onScreenSharingClicked(View view) {
        TextView tv_share = findViewById(R.id.tv_share);
        RelativeLayout button = (RelativeLayout) view;
        boolean selected = button.isSelected();
        button.setSelected(!selected);

        if (button.isSelected()) {
            shareId=1;
            mSSClient.start(getApplicationContext(), getResources().getString(R.string.agora_app_id), accessToken,
                    channelName, com.pb.criconet.sharedscreen.Constant.SCREEN_SHARE_UID, mVEC);
              rtcEngine().enableAudio();
              rtcEngine().enableLocalAudio(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv_share.setText(getResources().getString(R.string.label_stop_sharing_your_screen));
                }
            },3000);
            mSS=true;
        } else {
            mSS=false;
            shareId=2;
            mSSClient.stop(getApplicationContext());
            tv_share.setText(getResources().getString(R.string.label_start_sharing_your_screen));
        }
    }

    @Override
    protected void initUIandEvent() {
        addEventHandler(this);
        channelName = getIntent().getStringExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME);

        try {
            //Log.d("Channel",channelName);
            accessToken = generateToken(getResources().getString(R.string.agora_app_id), getResources().getString(R.string.app_certificate), channelName, 0, 3600);
            SessionManager.save_accessToken(prefs, accessToken);
            SessionManager.save_chanelId(prefs, channelName);
            Log.d("AccessToken", accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Toaster.customToast(channelName);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            TextView channelNameView = ((TextView) findViewById(R.id.ovc_page_title));
            channelNameView.setText(channelName);
        }

        // programmatically layout ui below of status bar/action bar
        LinearLayout eopsContainer = findViewById(R.id.extra_ops_container);
        RelativeLayout.MarginLayoutParams eofmp = (RelativeLayout.MarginLayoutParams) eopsContainer.getLayoutParams();
        eofmp.topMargin = getStatusBarHeight() + getActionBarHeight() + getResources().getDimensionPixelOffset(R.dimen.activity_vertical_margin) / 2; // status bar + action bar + divider

        final String encryptionKey = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY);
        final String encryptionMode = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE);

        doConfigEngine(encryptionKey, encryptionMode);

        mGridVideoViewContainer = (GridVideoViewContainer) findViewById(R.id.grid_video_view_container);
        mGridVideoViewContainer.setItemEventHandler(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onBigVideoViewClicked(view, position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemDoubleClick(View view, int position) {
                onBigVideoViewDoubleClicked(view, position);
            }
        });

        SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
        rtcEngine().enableVideo();
        preview(true, surfaceV, 0);
        surfaceV.setZOrderOnTop(false);
        surfaceV.setZOrderMediaOverlay(false);
        mUidsList.put(0, surfaceV); // get first surface view

        mGridVideoViewContainer.initViewContainer(this, 0, mUidsList, mIsLandscape); // first is now full view

        try {

            if (Global.isOnline(mActivity)) {
                joinChannel(accessToken, channelName, Integer.parseInt(SessionManager.get_user_id(prefs)));
            } else {
                Global.showDialog(mActivity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        optional();
    }


    private void onBigVideoViewClicked(View view, int position) {
        //log.debug("onItemClick " + view + " " + position + " " + mLayoutType);
        if(li_tap.getVisibility()== View.VISIBLE){
            li_tap.setVisibility(View.GONE);
        }
        toggleFullscreen();
    }

    private void onBigVideoViewDoubleClicked(View view, int position) {
        // log.debug("onItemDoubleClick " + view + " " + position + " " + mLayoutType);
        if(li_tap.getVisibility()== View.VISIBLE){
            li_tap.setVisibility(View.GONE);
        }

        if (mUidsList.size() < 2) {
            return;
        }

        try{
            UserStatusData user = mGridVideoViewContainer.getItem(position);
            int uid = (user.mUid == 0) ? config().mUid : user.mUid;

            if (mLayoutType == LAYOUT_TYPE_DEFAULT && mUidsList.size() != 1) {
                switchToSmallVideoView(uid);
            } else {
                switchToDefaultVideoView();
            }
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    private void onSmallVideoViewDoubleClicked(View view, int position) {
        //log.debug("onItemDoubleClick small " + view + " " + position + " " + mLayoutType);
        switchToDefaultVideoView();
    }

    private void optional() {
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    private void optionalDestroy() {
    }

    private int getVideoEncResolutionIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int videoEncResolutionIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_RESOLUTION, ConstantApp.DEFAULT_VIDEO_ENC_RESOLUTION_IDX);
        if (videoEncResolutionIndex > ConstantApp.VIDEO_DIMENSIONS.length - 1) {
            videoEncResolutionIndex = ConstantApp.DEFAULT_VIDEO_ENC_RESOLUTION_IDX;

            // save the new value
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_RESOLUTION, videoEncResolutionIndex);
            editor.apply();
        }
        return videoEncResolutionIndex;
    }

    private int getVideoEncFpsIndex() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int videoEncFpsIndex = pref.getInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_FPS, ConstantApp.DEFAULT_VIDEO_ENC_FPS_IDX);
        if (videoEncFpsIndex > ConstantApp.VIDEO_FPS.length - 1) {
            videoEncFpsIndex = ConstantApp.DEFAULT_VIDEO_ENC_FPS_IDX;

            // save the new value
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(ConstantApp.PrefManager.PREF_PROPERTY_VIDEO_ENC_FPS, videoEncFpsIndex);
            editor.apply();
        }
        return videoEncFpsIndex;
    }

    private void doConfigEngine(String encryptionKey, String encryptionMode) {
        VideoEncoderConfiguration.VideoDimensions videoDimension = ConstantApp.VIDEO_DIMENSIONS[getVideoEncResolutionIndex()];
        VideoEncoderConfiguration.FRAME_RATE videoFps = ConstantApp.VIDEO_FPS[getVideoEncFpsIndex()];
        configEngine(videoDimension, videoFps, encryptionKey, encryptionMode);
    }


    @Override
    protected void deInitUIandEvent() {
        optionalDestroy();
        doLeaveChannel();
        removeEventHandler(this);
        mUidsList.clear();
    }

    private void doLeaveChannel() {
        leaveChannel(config().mChannel, SessionManager.get_user_name(prefs));
        preview(false, null, 0);
    }

    @Override
    public void onUserJoined(int uid) {

        Log.d("UID",uid+"");
        joinType = "join";

        if (Global.isOnline(mActivity)) {
            getSessionLog();
        } else {
            Global.showDialog(mActivity);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                li_tap.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        li_tap.setVisibility(View.GONE);
                    }
                },2000);

                tv_timeDuration.setVisibility(View.VISIBLE);
                new CountDownTimer(timeDuration, 1000) {

                    @SuppressLint("SetTextI18n")
                    public void onTick(long millisUntilFinished) {
                        tv_timeDuration.setText(getResources().getString(R.string.Remaining_time) + Global.convertSecondsTomSs(millisUntilFinished / 1000));
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        sessionEndDialog();
                        tv_timeDuration.setVisibility(View.GONE);
                    }

                }.start();

                lin_log.setVisibility(View.VISIBLE);
                if (String.valueOf(uid).equalsIgnoreCase(SessionManager.get_user_id(prefs))) {
                    log_texth_other.setVisibility(View.GONE);

                }else if(uid==com.pb.criconet.sharedscreen.Constant.SCREEN_SHARE_UID){
                    log_texth_other.setVisibility(View.VISIBLE);
                    log_texth_other.setText(getResources().getString(R.string.Screen_sharing_has_started));

                }
                else{

                    log_texth_other.setVisibility(View.VISIBLE);
                    log_texth_other.setText(Global.capitizeString(coachName) + " " + getResources().getString(R.string.joined));
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lin_log.setVisibility(View.GONE);
                    }
                }, 2000);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(uid==2){
                            if(uid==2 && shareId==1){
                            }
                            else{
                                shareId=0;
                                rl_share_screen.setVisibility(View.GONE);
                            }
                        }else{
                            rl_share_screen.setVisibility(View.VISIBLE);
                        }


                    }
                });


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (rtcEngine() != null) {
            doLeaveChannel();
            RtcEngine.destroy();
        }

        if (mSS) {
            mSSClient.stop(getApplicationContext());
        }
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        //log.debug("onFirstRemoteVideoDecoded " + (uid & 0xFFFFFFFFL) + " " + width + " " + height + " " + elapsed);

        doRenderRemoteUi(uid);
    }

    @Override
    protected void onUserLeaveHint() {

    }

    @Override
    public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
        //ScreenShare..
//        rl_share_screen.setVisibility(View.VISIBLE);
//        rl_share_screen.setEnabled(true);
//        isSharing=true;
        //End ScreenShare
        //Toaster.customToast("onJoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed);
        //logge.debug("onJoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed);
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        //log.debug("onUserOffline " + (uid & 0xFFFFFFFFL) + " " + reason);
        Log.d("UID",uid+"");
        joinType = "leave";
        if (Global.isOnline(mActivity)) {
            getSessionLog();
        } else {
            Global.showDialog(mActivity);
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (String.valueOf(uid).equalsIgnoreCase(SessionManager.get_user_id(prefs))) {
                    log_text.setVisibility(View.GONE);
                    log_texth_other.setVisibility(View.GONE);


                }else if(uid==com.pb.criconet.sharedscreen.Constant.SCREEN_SHARE_UID){
                    log_texth_other.setVisibility(View.VISIBLE);
                    log_texth_other.setText(getResources().getString(R.string.Screen_sharing_has_stopped));

                }
                else{
                    log_texth_other.setVisibility(View.VISIBLE);
                    log_texth_other.setText(Global.capitizeString(coachName) + " " + getResources().getString(R.string.left_successfully));
                }

                lin_log.setVisibility(View.VISIBLE);
                li_tap.setVisibility(View.GONE);



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lin_log.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        });


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(uid==2){
                    if(uid==2 && shareId==2){
                    }
                    else{
                        shareId=0;
                        rl_share_screen.setVisibility(View.VISIBLE);
                    }
                }else{
                    rl_share_screen.setVisibility(View.GONE);
                }

            }
        });




        doRemoveRemoteUi(uid);
    }

    @Override
    public void onExtraCallback(final int type, final Object... data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                doHandleExtraCallback(type, data);
            }
        });
    }

    private void doHandleExtraCallback(int type, Object... data) {
        int peerUid;
        boolean muted;

        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> status = new HashMap<>();
                    status.put(peerUid, muted ? UserStatusData.AUDIO_MUTED : UserStatusData.DEFAULT_STATUS);
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, status, null);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                doHideTargetView(peerUid, muted);

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_STATS:
                IRtcEngineEventHandler.RemoteVideoStats stats = (IRtcEngineEventHandler.RemoteVideoStats) data[0];

                if (Constant.SHOW_VIDEO_INFO) {
                    if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                        //mGridVideoViewContainer.addVideoInfo(stats.uid, new VideoInfoData(stats.width, stats.height, stats.delay, stats.rendererOutputFrameRate, stats.receivedBitrate));
                        int uid = config().mUid;
                        int profileIndex = getVideoEncResolutionIndex();
                        String resolution = getResources().getStringArray(R.array.string_array_resolutions)[profileIndex];
                        String fps = getResources().getStringArray(R.array.string_array_frame_rate)[profileIndex];

                        String[] rwh = resolution.split("x");
                        int width = Integer.valueOf(rwh[0]);
                        int height = Integer.valueOf(rwh[1]);

//                        mGridVideoViewContainer.addVideoInfo(uid, new VideoInfoData(width > height ? width : height,
//                                width > height ? height : width,
//                                0, Integer.valueOf(fps), Integer.valueOf(0)));
                    }
                } else {
                    mGridVideoViewContainer.cleanVideoInfo();
                }

                break;


            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS:
                IRtcEngineEventHandler.AudioVolumeInfo[] infos = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];

                if (infos.length == 1 && infos[0].uid == 0) { // local guy, ignore it
                    break;
                }

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> volume = new HashMap<>();

                    for (IRtcEngineEventHandler.AudioVolumeInfo each : infos) {
                        peerUid = each.uid;
                        int peerVolume = each.volume;

                        if (peerUid == 0) {
                            continue;
                        }
                        volume.put(peerUid, peerVolume);
                    }
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, null, volume);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_APP_ERROR:
                int subType = (int) data[0];
                Toaster.customToast(subType + "");

                if (subType == ConstantApp.AppError.NO_CONNECTION_ERROR) {
                    String msg = getString(R.string.msg_connection_error);
                    //notifyMessageChanged(new Message(new User(0, null), msg));
                    Toaster.customToast(subType + "");
                    //showLongToast(msg);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_DATA_CHANNEL_MSG:

                peerUid = (Integer) data[0];
                final byte[] content = (byte[]) data[1];
                //notifyMessageChanged(new Message(new User(peerUid, String.valueOf(peerUid)), new String(content)));

                break;

            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR: {
                int error = (int) data[0];
                String description = (String) data[1];

                //notifyMessageChanged(new Message(new User(0, null), error + " " + description));

                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED:
                notifyHeadsetPlugged((int) data[0]);

                break;

        }
    }

    private void requestRemoteStreamType(final int currentHostCount) {
        // log.debug("requestRemoteStreamType " + currentHostCount);
    }

    private void doRenderRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                if (mUidsList.containsKey(uid)) {
                    return;
                }

                /*
                  Creates the video renderer view.
                  CreateRendererView returns the SurfaceView type. The operation and layout of the
                  view are managed by the app, and the Agora SDK renders the view provided by the
                  app. The video display view must be created using this method instead of
                  directly calling SurfaceView.
                 */
                SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                mUidsList.put(uid, surfaceV);

                boolean useDefaultLayout = mLayoutType == LAYOUT_TYPE_DEFAULT;

                surfaceV.setZOrderOnTop(true);
                surfaceV.setZOrderMediaOverlay(true);

                /*
                  Initializes the video view of a remote user.
                  This method initializes the video view of a remote stream on the local device. It affects only the video view that the local user sees.
                  Call this method to bind the remote video stream to a video view and to set the rendering and mirror modes of the video view.
                 */
                rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));

                if (useDefaultLayout) {
                    //log.debug("doRenderRemoteUi LAYOUT_TYPE_DEFAULT " + (uid & 0xFFFFFFFFL));
                    switchToDefaultVideoView();
                } else {
                    int bigBgUid = mSmallVideoViewAdapter == null ? uid : mSmallVideoViewAdapter.getExceptedUid();
                    //log.debug("doRenderRemoteUi LAYOUT_TYPE_SMALL " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL));
                    switchToSmallVideoView(bigBgUid);
                }
            }
        });
    }

    private void doRemoveRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                Object target = mUidsList.remove(uid);
                if (target == null) {
                    return;
                }

                int bigBgUid = -1;
                if (mSmallVideoViewAdapter != null) {
                    bigBgUid = mSmallVideoViewAdapter.getExceptedUid();
                }

                //log.debug("doRemoveRemoteUi " + (uid & 0xFFFFFFFFL) + " " + (bigBgUid & 0xFFFFFFFFL) + " " + mLayoutType);

                if (mLayoutType == LAYOUT_TYPE_DEFAULT || uid == bigBgUid) {
                    switchToDefaultVideoView();
                } else {
                    switchToSmallVideoView(bigBgUid);
                }

                //notifyMessageChanged(new Message(new User(0, null), "user " + (uid & 0xFFFFFFFFL) + " left"));
            }
        });
    }

    private void switchToDefaultVideoView() {
        if (mSmallVideoViewDock != null) {
            mSmallVideoViewDock.setVisibility(View.GONE);
        }
        mGridVideoViewContainer.initViewContainer(this, config().mUid, mUidsList, mIsLandscape);

        mLayoutType = LAYOUT_TYPE_DEFAULT;
        boolean setRemoteUserPriorityFlag = false;
        int sizeLimit = mUidsList.size();
        if (sizeLimit > ConstantApp.MAX_PEER_COUNT + 1) {
            sizeLimit = ConstantApp.MAX_PEER_COUNT + 1;
        }
        for (int i = 0; i < sizeLimit; i++) {
            int uid = mGridVideoViewContainer.getItem(i).mUid;
            if (config().mUid != uid) {
                if (!setRemoteUserPriorityFlag) {
                    setRemoteUserPriorityFlag = true;
                    rtcEngine().setRemoteUserPriority(uid, Constants.USER_PRIORITY_HIGH);
                    // log.debug("setRemoteUserPriority USER_PRIORITY_HIGH " + mUidsList.size() + " " + (uid & 0xFFFFFFFFL));
                } else {
                    rtcEngine().setRemoteUserPriority(uid, Constants.USER_PRIORITY_NORMAL);
                    //log.debug("setRemoteUserPriority USER_PRIORITY_NORANL " + mUidsList.size() + " " + (uid & 0xFFFFFFFFL));
                }
            }
        }
    }

    private void switchToSmallVideoView(int bigBgUid) {
        HashMap<Integer, SurfaceView> slice = new HashMap<>(1);
        slice.put(bigBgUid, mUidsList.get(bigBgUid));
        Iterator<SurfaceView> iterator = mUidsList.values().iterator();
        while (iterator.hasNext()) {
            SurfaceView s = iterator.next();
            s.setZOrderOnTop(true);
            s.setZOrderMediaOverlay(true);
        }

        mUidsList.get(bigBgUid).setZOrderOnTop(false);
        mUidsList.get(bigBgUid).setZOrderMediaOverlay(false);

        mGridVideoViewContainer.initViewContainer(this, bigBgUid, slice, mIsLandscape);

        bindToSmallVideoView(bigBgUid);

        mLayoutType = LAYOUT_TYPE_SMALL;

        requestRemoteStreamType(mUidsList.size());
    }

    private void bindToSmallVideoView(int exceptUid) {
        if (mSmallVideoViewDock == null) {
            ViewStub stub = (ViewStub) findViewById(R.id.small_video_view_dock);
            mSmallVideoViewDock = (RelativeLayout) stub.inflate();
        }

        boolean twoWayVideoCall = mUidsList.size() == 2;

        RecyclerView recycler = (RecyclerView) findViewById(R.id.small_video_view_container);

        boolean create = false;

        if (mSmallVideoViewAdapter == null) {
            create = true;
            mSmallVideoViewAdapter = new SmallVideoViewAdapter(this, config().mUid, exceptUid, mUidsList);
            mSmallVideoViewAdapter.setHasStableIds(true);
        }
        recycler.setHasFixedSize(true);

        //log.debug("bindToSmallVideoView " + twoWayVideoCall + " " + (exceptUid & 0xFFFFFFFFL));

        if (twoWayVideoCall) {
            recycler.setLayoutManager(new RtlLinearLayoutManager(getApplicationContext(), RtlLinearLayoutManager.HORIZONTAL, false));
        } else {
            recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        }
        recycler.addItemDecoration(new SmallVideoViewDecoration());
        recycler.setAdapter(mSmallVideoViewAdapter);
        recycler.addOnItemTouchListener(new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemDoubleClick(View view, int position) {
                onSmallVideoViewDoubleClicked(view, position);
            }
        }));

        recycler.setDrawingCacheEnabled(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        if (!create) {
            mSmallVideoViewAdapter.setLocalUid(config().mUid);
            mSmallVideoViewAdapter.notifyUiChanged(mUidsList, exceptUid, null, null);
        }
        for (Integer tempUid : mUidsList.keySet()) {
            if (config().mUid != tempUid) {
                if (tempUid == exceptUid) {
                    rtcEngine().setRemoteUserPriority(tempUid, Constants.USER_PRIORITY_HIGH);
                    //log.debug("setRemoteUserPriority USER_PRIORITY_HIGH " + mUidsList.size() + " " + (tempUid & 0xFFFFFFFFL));
                } else {
                    rtcEngine().setRemoteUserPriority(tempUid, Constants.USER_PRIORITY_HIGH);
                    //log.debug("setRemoteUserPriority USER_PRIORITY_NORANL " + mUidsList.size() + " " + (tempUid & 0xFFFFFFFFL));
                }
            }
        }
        recycler.setVisibility(View.VISIBLE);
        mSmallVideoViewDock.setVisibility(View.VISIBLE);
    }

    public void notifyHeadsetPlugged(final int routing) {
        // log.info("notifyHeadsetPlugged " + routing + " " + mVideoMuted);

        mAudioRouting = routing;

        ImageView iv = (ImageView) findViewById(R.id.switch_speaker_id);
        if (mAudioRouting == Constants.AUDIO_ROUTE_SPEAKERPHONE) {
            iv.setImageResource(R.drawable.ic_volume_up_black_24dp);
        } else {
            iv.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
    }

    /*All Container Click initialize here*/
    public void onSwitchCameraClicked(View view) {
        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.switchCamera();
    }

    public void onMixingAudioClicked(View view) {
        //log.info("onMixingAudioClicked " + view + " " + mUidsList.size() + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted + " mixing_audio: " + mMixingAudio);

        if (mUidsList.size() == 0) {
            return;
        }

        mMixingAudio = !mMixingAudio;

        RtcEngine rtcEngine = rtcEngine();
        if (mMixingAudio) {
            rtcEngine.startAudioMixing(Constant.MIX_FILE_PATH, false, false, -1);
        } else {
            rtcEngine.stopAudioMixing();
        }

        ImageView iv = (ImageView) view;
        iv.setImageResource(mMixingAudio ? R.drawable.btn_audio_mixing : R.drawable.btn_audio_mixing_off);
    }

    public void onVoiceMuteClicked(View view) {
        // log.info("onVoiceMuteClicked " + view + " " + mUidsList.size() + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted);
        if (mUidsList.size() == 0) {
            return;
        }

        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.muteLocalAudioStream(mAudioMuted = !mAudioMuted);

        ImageView iv = (ImageView) view;

        iv.setImageResource(mAudioMuted ? R.drawable.ic_mic_off_black_24dp : R.drawable.ic_mic_black_24dp);
    }

    public void onChatClicked(View view) {
        slideUp.show();
        loadWebView();
    }

    public void onFilterClicked(View view) {
        Constant.BEAUTY_EFFECT_ENABLED = !Constant.BEAUTY_EFFECT_ENABLED;

        if (Constant.BEAUTY_EFFECT_ENABLED) {

            setBeautyEffectParameters(Constant.BEAUTY_EFFECT_DEFAULT_LIGHTNESS, Constant.BEAUTY_EFFECT_DEFAULT_SMOOTHNESS, Constant.BEAUTY_EFFECT_DEFAULT_REDNESS);
            enablePreProcessor();
        } else {
            disablePreProcessor();
        }

        ImageView iv = (ImageView) view;

        iv.setImageResource(Constant.BEAUTY_EFFECT_ENABLED ? R.drawable.ic_auto_awesome_black_24dp : R.drawable.ic_auto_awesome_gray_24dp);
    }

    public void onSwitchSpeakerClicked(View view) {
        RtcEngine rtcEngine = rtcEngine();
        /*
          Enables/Disables the audio playback route to the speakerphone.
          This method sets whether the audio is routed to the speakerphone or earpiece.
          After calling this method, the SDK returns the onAudioRouteChanged callback
          to indicate the changes.
         */
        rtcEngine.setEnableSpeakerphone(mAudioRouting != Constants.AUDIO_ROUTE_SPEAKERPHONE);
    }

    public void onHangupClicked(View view) {
        sessionCancelAlertDialog();
    }


    private void showOrHideStatusBar(boolean hide) {
        // May fail on some kinds of devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            View decorView = getWindow().getDecorView();
            int uiOptions = decorView.getSystemUiVisibility();

            if (hide) {
                uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            } else {
                uiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
            }

            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void toggleFullscreen() {
        mFullScreen = !mFullScreen;

        showOrHideCtrlViews(mFullScreen);

        mUIHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showOrHideStatusBar(mFullScreen);
            }
        }, 200); // action bar fade duration
    }

    private void showOrHideCtrlViews(boolean hide) {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            if (hide) {
                ab.hide();
            } else {
                ab.show();
            }
        }

        findViewById(R.id.extra_ops_container).setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
        findViewById(R.id.bottom_action_container).setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
        findViewById(R.id.small_video_view_dock).setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
        //findViewById(R.id.rl_share_screen).setVisibility(hide ? View.INVISIBLE : View.VISIBLE);

    }

    public void onClickHideIME(View view) {
        closeIME(findViewById(R.id.msg_content));
        findViewById(R.id.msg_input_container).setVisibility(View.GONE);
        findViewById(R.id.bottom_action_container).setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_call, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_options:
                //showCallOptions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*END All Container Click initialize here*/

    private void doHideTargetView(int targetUid, boolean hide) {
        HashMap<Integer, Integer> status = new HashMap<>();
        status.put(targetUid, hide ? UserStatusData.VIDEO_MUTED : UserStatusData.DEFAULT_STATUS);
        if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
            mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
        } else if (mLayoutType == LAYOUT_TYPE_SMALL) {
            UserStatusData bigBgUser = mGridVideoViewContainer.getItem(0);
            if (bigBgUser.mUid == targetUid) { // big background is target view
                mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
            } else { // find target view in small video view list
                //log.warn("SmallVideoViewAdapter call notifyUiChanged " + mUidsList + " " + (bigBgUser.mUid & 0xFFFFFFFFL) + " target: " + (targetUid & 0xFFFFFFFFL) + "==" + targetUid + " " + status);
                mSmallVideoViewAdapter.notifyUiChanged(mUidsList, bigBgUser.mUid, status, null);
            }
        }
    }

    public void onVideoMuteClicked(View view) {
        // log.info("onVoiceChatClicked " + view + " " + mUidsList.size() + " video_status: " + mVideoMuted + " audio_status: " + mAudioMuted);
        if (mUidsList.size() == 0) {
            return;
        }

        SurfaceView surfaceV = getLocalView();
        ViewParent parent;
        if (surfaceV == null || (parent = surfaceV.getParent()) == null) {
            // log.warn("onVoiceChatClicked " + view + " " + surfaceV);
            return;
        }

        RtcEngine rtcEngine = rtcEngine();
        mVideoMuted = !mVideoMuted;

        if (mVideoMuted) {
            rtcEngine.disableVideo();
        } else {
            rtcEngine.enableVideo();
        }

        ImageView iv = (ImageView) view;

        iv.setImageResource(mVideoMuted ? R.drawable.ic_videocam_off_black_24dp : R.drawable.ic_videocam_black_24dp);

        hideLocalView(mVideoMuted);
    }

    private void hideLocalView(boolean hide) {
        int uid = config().mUid;
        doHideTargetView(uid, hide);
    }

    private SurfaceView getLocalView() {
        for (HashMap.Entry<Integer, SurfaceView> entry : mUidsList.entrySet()) {
            if (entry.getKey() == 0 || entry.getKey() == config().mUid) {
                return entry.getValue();
            }
        }

        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mIsLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
            switchToDefaultVideoView();
        } else if (mSmallVideoViewAdapter != null) {
            switchToSmallVideoView(mSmallVideoViewAdapter.getExceptedUid());
        }
    }

    private void sessionCancelAlertDialog() {
        final Dialog dialog = new Dialog(CallActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_alert_cancel_session);
        dialog.setCancelable(false);
        FrameLayout fl_no = dialog.findViewById(R.id.fl_no);
        fl_no.setOnClickListener(v -> {
            dialog.dismiss();
        });
        FrameLayout fl_yes = dialog.findViewById(R.id.fl_yes);
        fl_yes.setOnClickListener(v -> {
            joinType = "leave";
            if (Global.isOnline(mActivity)) {
                getSessionLog();
            } else {
                Global.showDialog(mActivity);
            }
            finish();
        });

        dialog.show();
    }

    private void sessionEndDialog() {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.session_end);
        dialog.setCancelable(false);
        dialog.show();
        FrameLayout fl_ok = dialog.findViewById(R.id.fl_ok);
        fl_ok.setOnClickListener(v -> {

            dialog.dismiss();
            // will show..after testing...............................................................
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST", (Serializable) feedBackFormChildData);
            startActivity(new Intent(CallActivity.this, EndSessionFeedbackFormActivity.class)
                    .putExtra("Certificate", args).putExtra("id", bookingId));


            finish();
        });


    }

    private void getSessionLog() {
//        progressDialog = Global.getProgressDialog(this, CCResource.getString(this, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "update_activity_session", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.d("SessionResponse",response);

                //Global.dismissDialog(progressDialog);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //Global.dismissDialog(progressDialog);
                // Global.msgDialog((Activity) mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("booking_id", bookingId);
                param.put("activity", joinType);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void callChatApi() {

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                //Initiate your API here
                if (Global.isOnline(mActivity)) {
                    getChatNotification();
                } else {
                    Global.showDialog(mActivity);
                }
                handler.postDelayed(this, 2000);
            }
        };

        handler.postDelayed(r, 2000);
    }

    private void getSessionFeedbackForm() {
//        progressDialog = Global.getProgressDialog(this, CCResource.getString(this, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_SESSION_FEEDBACKFORM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Log.d("SessionFeedback",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        FeedBackFormChildData feedBackFormChildDataa = null;
                        feedBackFormChildData = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            feedBackFormChildDataa = new FeedBackFormChildData(jsonArray.getJSONObject(i));
                            feedBackFormChildData.add(feedBackFormChildDataa);
                        }

                    }
                    //Toaster.customToast(feedBackFormChildData.size()+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Global.dismissDialog(progressDialog);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //Global.dismissDialog(progressDialog);
                // Global.msgDialog((Activity) mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void onBackPressed() {
        sessionCancelAlertDialog();
    }

    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt"})
    public void loadWebView() {
        if (SessionManager.getProfileType(prefs).equalsIgnoreCase("Coach")) {
            webUrl = Global.URL_CHAT + "/" + "messages" + "/" + userId + "?" + "user_id=" + SessionManager.get_user_id(prefs) + "&" + "s=" + SessionManager.get_session_id(prefs);
        } else {
            webUrl = Global.URL_CHAT + "/" + "messages" + "/" + coachId + "?" + "user_id=" + SessionManager.get_user_id(prefs) + "&" + "s=" + SessionManager.get_session_id(prefs);
        }

        Log.d("WevURL", webUrl);
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(CallActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.loadUrl(webUrl);
        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
//                File file = new File(url);
//                openFile(file);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                CallActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
            }

            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                CallActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FCR);
            }

            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                CallActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), CallActivity.FCR);
            }

            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {

                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }

                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(CallActivity.this.getPackageManager()) != null) {

                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch (IOException ex) {
                        Log.e(TAG, "Image file creation failed", ex);
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray;

                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);

                return true;
            }
        });

    }

    private File createImageFile() throws IOException {

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    public class Callback extends WebViewClient {
        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            webview.setVisibility(webView.INVISIBLE);
            progressBar.start();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.setVisibility(webView.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.stop();
                }
            }, 2000);

            super.onPageFinished(view, url);

        }

        // ProgressBar will disappear once page is loaded

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Failed_loading_app), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void getChatNotification() {
//        progressDialog = Global.getProgressDialog(this, CCResource.getString(this, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_CHAT_NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ChatNotification", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {
                        JSONObject jsonArray = jsonObject.getJSONObject("data");
                        if (jsonArray.getString("message_counter").equalsIgnoreCase("0")) {
                            rl_count.setVisibility(View.GONE);
                        } else {
                            rl_count.setVisibility(View.VISIBLE);
                            tv_count.setText(jsonArray.getString("message_counter"));
                        }


                    }
                    //Toaster.customToast(feedBackFormChildData.size()+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //Global.dismissDialog(progressDialog);
                // Global.msgDialog((Activity) mActivity, "Error from server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

}
