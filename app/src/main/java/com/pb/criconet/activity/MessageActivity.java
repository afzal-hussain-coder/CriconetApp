package com.pb.criconet.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.pb.criconet.AGApplication;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.MessageAdapter;
import com.pb.criconet.chatModel.MessageListBean;
import com.pb.criconet.chatModel.User;
import com.pb.criconet.models.ConstantApp;
import com.pb.criconet.models.MessageBean;
import com.pb.criconet.rtm.ChatManager;
import com.pb.criconet.utils.ColorAdapter;
import com.pb.criconet.utils.EqualSpacingItemDecoration;
import com.pb.criconet.utils.ImageUtil;
import com.pb.criconet.utils.MessageUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vanniktech.emoji.EmojiPopup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmMessageType;
import io.agora.rtm.RtmStatusCode;

public class MessageActivity extends AppCompatActivity {

    private final String TAG = MessageActivity.class.getSimpleName();

    private TextView mTitleTextView;
    private EditText mMsgEditText;
    private RecyclerView mRecyclerView;
    private List<MessageBean> mMessageBeanList = new ArrayList<>();
    private MessageAdapter mMessageAdapter;

    private boolean mIsPeerToPeerMode = true;
    private User user;
    private String mPeerId = "";
    private String mChannelName = "";
    private int mChannelMemberCount = 1,change_color;
    private String targetName = "";

    private ChatManager mChatManager;
    private RtmClient mRtmClient;
    private RtmClientListener mClientListener;
    private RtmChannel mRtmChannel;


    private MaterialToolbar materialToolbar;
    private Context mContext;
    private ImageView mBigImage, img_video_call, img_back, img_emoji, img_attached_file;
    private RecyclerView recyclerView_chat,recyclerView_color;
    private MaterialCardView lin_color;
    private List<Integer> colors;
    private GridLayoutManager gridLayoutManager;
    private FrameLayout img_send_message,img_change_color;
    EmojiPopup popup;
    private String mUserId = "";
    String channelName;
    private SharedPreferences prefs;

    //WebView web_chat;
    String webUrl="";
    String coach_id="";

    private final static int FCR = 1;
    WebView webView;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;

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

    //RtmRequestId requestId;
    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mContext = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        materialToolbar = findViewById(R.id.appbar);
        setSupportActionBar(materialToolbar);
        channelName = getIntent().getStringExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME);
        mUserId = getIntent().getStringExtra("UserId");
        coach_id = getIntent().getStringExtra("CoachId");
        webUrl= Global.URL_CHAT+"/"+"messages"+"/"+coach_id+"?"+"user_id="+mUserId+"&"+"s="+SessionManager.get_session_id(prefs);

        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MessageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }

        webView = (WebView) findViewById(R.id.web_chat);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        assert webView != null;

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.setWebViewClient(new Callback());
        webView.loadUrl(webUrl);
        webView.setWebChromeClient(new WebChromeClient() {

            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MessageActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
            }

            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MessageActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FCR);
            }

            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MessageActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), MessageActivity.FCR);
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
                if (takePictureIntent.resolveActivity(MessageActivity.this.getPackageManager()) != null) {

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
        img_video_call = materialToolbar.findViewById(R.id.img_video_call);
//
        img_video_call.setOnClickListener(v -> {
            sendVideoChat();
        });

        img_back = materialToolbar.findViewById(R.id.img_back);
        img_back.setOnClickListener(v -> {
            finish();
        });

        //init();
    }

    // Create an image file
    private File createImageFile() throws IOException {

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {

            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:

                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }

                    return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public class Callback extends WebViewClient {
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
        }
    }
//
//    @SuppressLint("SetJavaScriptEnabled")
//    private void init() {
//
//        web_chat = findViewById(R.id.web_chat);
//        web_chat.getSettings().setJavaScriptEnabled(true);
//        web_chat .getSettings().setDomStorageEnabled(true);
//        web_chat.setWebChromeClient(new WebChromeClient());
//        web_chat.setWebViewClient(new WebViewClient());
//        web_chat.setWebChromeClient(new WebChromeClient());
//        web_chat.setWebViewClient(new WebViewClient());
//        web_chat.clearCache(true);
//        web_chat.clearHistory();
//        web_chat.getSettings().setJavaScriptEnabled(true);
//        web_chat.getSettings().setSupportZoom(false);
//        //web_chat.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        web_chat.getSettings().setAllowContentAccess(true);
//        //web_chat.getSettings().setAllowFileAccess(true);
//        //web_chat.getSettings().setAllowFileAccessFromFileURLs(true);
//        web_chat.loadUrl(webUrl);
//        //web_chat.requestFocus();
//        //Log.d("URL",webUrl);
//
////        mChatManager = AGApplication.getChatManager();
////        mRtmClient = mChatManager.getRtmClient();
////        mClientListener = new MyRtmClientListener();
////        mChatManager.registerListener(mClientListener);
//
//        //getExtras();
//
////        mTitleTextView = findViewById(R.id.message_title);
////        mMsgEditText = findViewById(R.id.message_edittiext);
////
////        mBigImage = findViewById(R.id.big_image);
//
//        //if (mIsPeerToPeerMode) {
//           // mPeerId = targetName;
//            //mTitleTextView.setText(mPeerId);
//
//            // load history chat records
////            MessageListBean messageListBean = MessageUtil.getExistMessageListBean(mPeerId);
////            if (messageListBean != null) {
////                mMessageBeanList.addAll(messageListBean.getMessageBeanList());
////            }
////
////            // load offline messages since last chat with this peer.
////            // Then clear cached offline messages from message pool
////            // since they are already consumed.
////            MessageListBean offlineMessageBean = new MessageListBean(mPeerId, mChatManager);
////            mMessageBeanList.addAll(offlineMessageBean.getMessageBeanList());
////            mChatManager.removeAllOfflineMessages(mPeerId);
//        //} else {
//            //mChannelName = targetName;
////            mChannelMemberCount = 1;
////            mTitleTextView.setText(channelName + "(" + mChannelMemberCount + ")");
////            createAndJoinChannel();
////       // }
////
////        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
////        layoutManager.setOrientation(RecyclerView.VERTICAL);
////        mMessageAdapter = new MessageAdapter(this, mMessageBeanList, message -> {
////            if (message.getMessage().getMessageType() == RtmMessageType.IMAGE) {
////                if (!TextUtils.isEmpty(message.getCacheFile())) {
////                    Glide.with(this).load(message.getCacheFile()).into(mBigImage);
////                    mBigImage.setVisibility(View.VISIBLE);
////                } else {
////                    ImageUtil.cacheImage(this, mRtmClient, (RtmImageMessage) message.getMessage(), new ResultCallback<String>() {
////                        @Override
////                        public void onSuccess(String file) {
////                            message.setCacheFile(file);
////                            runOnUiThread(() -> {
////                                Glide.with(MessageActivity.this).load(file).into(mBigImage);
////                                mBigImage.setVisibility(View.VISIBLE);
////                            });
////                        }
////
////                        @Override
////                        public void onFailure(ErrorInfo errorInfo) {
////
////                        }
////                    });
////                }
////            }
////        });
////        mRecyclerView = findViewById(R.id.message_list);
////        mRecyclerView.setLayoutManager(layoutManager);
////        mRecyclerView.setAdapter(mMessageAdapter);
////
////        mBigImage.setOnClickListener(v -> {
////            mBigImage.setVisibility(View.GONE);
////        });
//
//        img_video_call = materialToolbar.findViewById(R.id.img_video_call);
//
//        img_video_call.setOnClickListener(v -> {
//            sendVideoChat();
//        });
//
//        img_back = materialToolbar.findViewById(R.id.img_back);
//        img_back.setOnClickListener(v -> {
//            finish();
//        });
//
////        img_attached_file = findViewById(R.id.img_attached_file);
////
////        img_attached_file.setOnClickListener(v -> {
////            CropImage.activity().start(this);
////        });
////        img_change_color = findViewById(R.id.img_change_color);
////        img_send_message=findViewById(R.id.img_send_message);
////
////        img_send_message.setOnClickListener(v -> {
////        sendTextMessage();
////        });
////
////        img_change_color.setOnClickListener(v -> {
////
////            if (lin_color.getVisibility()==View.VISIBLE){
////                lin_color.setVisibility(View.GONE);
////            }else {
////                lin_color.setVisibility(View.VISIBLE);
////            }
////        });
////        lin_color=findViewById(R.id.lin_color);
////        recyclerView_color=findViewById(R.id.recyclerView_color);
////        gridLayoutManager = new GridLayoutManager(mContext, 5);
////        recyclerView_color.addItemDecoration(new EqualSpacingItemDecoration(4, EqualSpacingItemDecoration.HORIZONTAL));
////        recyclerView_color.setLayoutManager(gridLayoutManager);
////        recyclerView_color.setAdapter(new ColorAdapter(mContext, colors, new ColorAdapter.ColorChooserInterface() {
////            @Override
////            public void setColor(int color) {
////                change_color=color;
////                lin_color.setVisibility(View.GONE);
////                materialToolbar.setBackgroundColor(color);
////                ((GradientDrawable)img_send_message.getBackground()).setColor(color);
////                ((GradientDrawable)img_change_color.getBackground()).setColor(color);
////            }
////        }));
////
////        popup = EmojiPopup.Builder
////                .fromRootView(findViewById(R.id.rootView)).build(mMsgEditText);
////        img_emoji=findViewById(R.id.img_emoji);
////        img_emoji.setOnClickListener(v -> {
////            popup.toggle();
////        });
//    }



    private void sendVideoChat(){
      finish();
//            Intent intent = new Intent(this, VideoChatViewActivity.class);
//            intent.putExtra("User", user);
//            intent.putExtra("Channel", channelName);
//            intent.putExtra(MessageUtil.INTENT_EXTRA_IS_PEER_MODE, mIsPeerToPeerMode);
//            intent.putExtra("Actual Target", targetName);
//            startActivity(intent);
        }
}
