package com.pb.criconet.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
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

    WebView web_chat;
    String webUrl="";
    String coach_id="";

    //RtmRequestId requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mContext = this;
        materialToolbar = findViewById(R.id.appbar);
        setSupportActionBar(materialToolbar);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String[] colorsTxt = getApplicationContext().getResources().getStringArray(R.array.color_list);
//        colors = new ArrayList<Integer>();
//        for (int i = 0; i < colorsTxt.length; i++) {
//            int newColor = Color.parseColor(colorsTxt[i]);
//            colors.add(newColor);
//        }
        channelName = getIntent().getStringExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME);
        mUserId = getIntent().getStringExtra("UserId");
        coach_id = getIntent().getStringExtra("CoachId");
        //Toaster.customToastUp(mUserId+"/"+coach_id);
        //webUrl= Global.URL_CHAT+"/"+"messages"+"/"+coach_id+"?"+"user_id="+mUserId+"&"+"s="+SessionManager.get_session_id(prefs);
        init();

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {

        web_chat = findViewById(R.id.web_chat);
        web_chat.getSettings().setJavaScriptEnabled(true);
        web_chat .getSettings().setDomStorageEnabled(true);
        web_chat.setWebViewClient(new MyWebViewClient());
        Log.d("URL",webUrl);
       // "https://stage.criconetonline.com///messages/1209?user_id=1735&s=7295057200de972c10b71ac8197442b0c5da7dedb0b8893d2d18d0dcff64e25d5be3c527278297153b220b436e5f3d917a1e649a0dc0281c"
        web_chat.loadUrl(webUrl);
        web_chat.requestFocus();
        //openURL();

//        mChatManager = AGApplication.getChatManager();
//        mRtmClient = mChatManager.getRtmClient();
//        mClientListener = new MyRtmClientListener();
//        mChatManager.registerListener(mClientListener);

        //getExtras();

//        mTitleTextView = findViewById(R.id.message_title);
//        mMsgEditText = findViewById(R.id.message_edittiext);
//
//        mBigImage = findViewById(R.id.big_image);

        //if (mIsPeerToPeerMode) {
           // mPeerId = targetName;
            //mTitleTextView.setText(mPeerId);

            // load history chat records
//            MessageListBean messageListBean = MessageUtil.getExistMessageListBean(mPeerId);
//            if (messageListBean != null) {
//                mMessageBeanList.addAll(messageListBean.getMessageBeanList());
//            }
//
//            // load offline messages since last chat with this peer.
//            // Then clear cached offline messages from message pool
//            // since they are already consumed.
//            MessageListBean offlineMessageBean = new MessageListBean(mPeerId, mChatManager);
//            mMessageBeanList.addAll(offlineMessageBean.getMessageBeanList());
//            mChatManager.removeAllOfflineMessages(mPeerId);
        //} else {
            //mChannelName = targetName;
//            mChannelMemberCount = 1;
//            mTitleTextView.setText(channelName + "(" + mChannelMemberCount + ")");
//            createAndJoinChannel();
//       // }
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(RecyclerView.VERTICAL);
//        mMessageAdapter = new MessageAdapter(this, mMessageBeanList, message -> {
//            if (message.getMessage().getMessageType() == RtmMessageType.IMAGE) {
//                if (!TextUtils.isEmpty(message.getCacheFile())) {
//                    Glide.with(this).load(message.getCacheFile()).into(mBigImage);
//                    mBigImage.setVisibility(View.VISIBLE);
//                } else {
//                    ImageUtil.cacheImage(this, mRtmClient, (RtmImageMessage) message.getMessage(), new ResultCallback<String>() {
//                        @Override
//                        public void onSuccess(String file) {
//                            message.setCacheFile(file);
//                            runOnUiThread(() -> {
//                                Glide.with(MessageActivity.this).load(file).into(mBigImage);
//                                mBigImage.setVisibility(View.VISIBLE);
//                            });
//                        }
//
//                        @Override
//                        public void onFailure(ErrorInfo errorInfo) {
//
//                        }
//                    });
//                }
//            }
//        });
//        mRecyclerView = findViewById(R.id.message_list);
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setAdapter(mMessageAdapter);
//
//        mBigImage.setOnClickListener(v -> {
//            mBigImage.setVisibility(View.GONE);
//        });

        img_video_call = materialToolbar.findViewById(R.id.img_video_call);

        img_video_call.setOnClickListener(v -> {
            sendVideoChat();
        });

        img_back = materialToolbar.findViewById(R.id.img_back);
        img_back.setOnClickListener(v -> {
            finish();
        });

//        img_attached_file = findViewById(R.id.img_attached_file);
//
//        img_attached_file.setOnClickListener(v -> {
//            CropImage.activity().start(this);
//        });
//        img_change_color = findViewById(R.id.img_change_color);
//        img_send_message=findViewById(R.id.img_send_message);
//
//        img_send_message.setOnClickListener(v -> {
//        sendTextMessage();
//        });
//
//        img_change_color.setOnClickListener(v -> {
//
//            if (lin_color.getVisibility()==View.VISIBLE){
//                lin_color.setVisibility(View.GONE);
//            }else {
//                lin_color.setVisibility(View.VISIBLE);
//            }
//        });
//        lin_color=findViewById(R.id.lin_color);
//        recyclerView_color=findViewById(R.id.recyclerView_color);
//        gridLayoutManager = new GridLayoutManager(mContext, 5);
//        recyclerView_color.addItemDecoration(new EqualSpacingItemDecoration(4, EqualSpacingItemDecoration.HORIZONTAL));
//        recyclerView_color.setLayoutManager(gridLayoutManager);
//        recyclerView_color.setAdapter(new ColorAdapter(mContext, colors, new ColorAdapter.ColorChooserInterface() {
//            @Override
//            public void setColor(int color) {
//                change_color=color;
//                lin_color.setVisibility(View.GONE);
//                materialToolbar.setBackgroundColor(color);
//                ((GradientDrawable)img_send_message.getBackground()).setColor(color);
//                ((GradientDrawable)img_change_color.getBackground()).setColor(color);
//            }
//        }));
//
//        popup = EmojiPopup.Builder
//                .fromRootView(findViewById(R.id.rootView)).build(mMsgEditText);
//        img_emoji=findViewById(R.id.img_emoji);
//        img_emoji.setOnClickListener(v -> {
//            popup.toggle();
//        });
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    /** Opens the URL in a browser */
    private void openURL() {
        web_chat.loadUrl("https://stage.criconetonline.com///messages/1209?user_id=1735&s=7295057200de972c10b71ac8197442b0c5da7dedb0b8893d2d18d0dcff64e25d5be3c527278297153b220b436e5f3d917a1e649a0dc0281c");
        web_chat.requestFocus();
    }

//    private void sendTextMessage() {
//        String msg = mMsgEditText.getText().toString();
//        if (!msg.equals("")) {
//            RtmMessage message = mRtmClient.createMessage();
//            message.setText(msg);
//
//            MessageBean messageBean = new MessageBean(mUserId, message, true);
//            mMessageBeanList.add(messageBean);
//            mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
//            mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
//
//            if (mIsPeerToPeerMode) {
//                sendPeerMessage(message);
//            } else {
//                sendChannelMessage(message);
//            }
//        }
//        mMsgEditText.setText("");
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



    private void getExtras() {
//        Intent intent = getIntent();
//        mIsPeerToPeerMode = intent.getBooleanExtra(MessageUtil.INTENT_EXTRA_IS_PEER_MODE, true);
//        user = intent.getParcelableExtra(MessageUtil.INTENT_EXTRA_USER_ID);
//        targetName = intent.getStringExtra(MessageUtil.INTENT_EXTRA_TARGET_NAME);
//        mUserId=user.getFireDisplayName();
    }

    /**
     * API CALL: create and join channel
     */
//    private void createAndJoinChannel() {
//        // step 1: create a channel instance
//        mRtmChannel = mRtmClient.createChannel(channelName, new MyChannelListener());
//        if (mRtmChannel == null) {
//            //showToast(getString(R.string.join_channel_failed));
//            finish();
//            return;
//        }
//
//        Log.e("channel", mRtmChannel + "");
//
//        // step 2: join the channel
//        mRtmChannel.join(new ResultCallback<Void>() {
//            @Override
//            public void onSuccess(Void responseInfo) {
//                Log.i(TAG, "join channel success");
//                getChannelMemberList();
//            }
//
//            @Override
//            public void onFailure(ErrorInfo errorInfo) {
//                Log.e(TAG, "join channel failed");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showToast(getString(R.string.join_channel_failed));
//                        //finish();
//                    }
//                });
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mIsPeerToPeerMode) {
//            MessageUtil.addMessageListBeanList(new MessageListBean(mPeerId, mMessageBeanList));
//        } else {
//            leaveAndReleaseChannel();
//        }
//        mChatManager.unregisterListener(mClientListener);
//    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//
//                final String file = resultUri.getPath();
//                Toaster.customToast(file);
//
//
//                ImageUtil.uploadImage(this, mRtmClient, file, new ResultCallback<RtmImageMessage>() {
//                    @Override
//                    public void onSuccess(final RtmImageMessage rtmImageMessage) {
//                        runOnUiThread(() -> {
//                            MessageBean messageBean = new MessageBean(mUserId, rtmImageMessage, true);
//                            messageBean.setCacheFile(file);
//                            mMessageBeanList.add(messageBean);
//                            mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
//                            mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
//                            //sendChannelMessage(rtmImageMessage);
//                           // sendPeerMessage(rtmImageMessage);
////                            if (mIsPeerToPeerMode) {
////
////                            } else {
////
////                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(ErrorInfo errorInfo) {
//                        Toaster.customToast(errorInfo.toString()+"Failed Image Load");
//                        Log.d("error",errorInfo.toString());
//
//                    }
//                });
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                result.getError().printStackTrace();
//            }
//        }
//    }
//
//
//    /**
//     * API CALL: send message to peer
//     */
//    private void sendPeerMessage(final RtmMessage message) {
//        mRtmClient.sendMessageToPeer(mChannelName, message, mChatManager.getSendMessageOptions(), new ResultCallback<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                // do nothing
//            }
//
//            @Override
//            public void onFailure(ErrorInfo errorInfo) {
//                // refer to RtmStatusCode.PeerMessageState for the message state
//                final int errorCode = errorInfo.getErrorCode();
//                runOnUiThread(() -> {
//                    switch (errorCode) {
//                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_TIMEOUT:
//                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_FAILURE:
//                            showToast(getString(R.string.send_msg_failed));
//                            break;
//                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_PEER_UNREACHABLE:
//                            showToast(getString(R.string.peer_offline));
//                            break;
//                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_CACHED_BY_SERVER:
//                            showToast(getString(R.string.message_cached));
//                            break;
//                    }
//                });
//            }
//        });
//    }
//
//
//    /**
//     * API CALL: get channel member list
//     */
//    private void getChannelMemberList() {
//        mRtmChannel.getMembers(new ResultCallback<List<RtmChannelMember>>() {
//            @Override
//            public void onSuccess(final List<RtmChannelMember> responseInfo) {
//                runOnUiThread(() -> {
//                    mChannelMemberCount = responseInfo.size();
//                    refreshChannelTitle();
//                });
//            }
//
//            @Override
//            public void onFailure(ErrorInfo errorInfo) {
//                Log.e(TAG, "failed to get channel members, err: " + errorInfo.getErrorCode());
//            }
//        });
//    }
//
//    /**
//     * API CALL: send message to a channel
//     */
//    private void sendChannelMessage(RtmMessage message) {
//        mRtmChannel.sendMessage(message, new ResultCallback<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//            }
//
//            @Override
//            public void onFailure(ErrorInfo errorInfo) {
//                // refer to RtmStatusCode.ChannelMessageState for the message state
//                final int errorCode = errorInfo.getErrorCode();
//                runOnUiThread(() -> {
//                    switch (errorCode) {
//                        case RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_TIMEOUT:
//                        case RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_FAILURE:
//                            showToast(getString(R.string.send_msg_failed));
//                            break;
//                    }
//                });
//            }
//        });
//    }
//
//    /**
//     * API CALL: leave and release channel
//     */
//    private void leaveAndReleaseChannel() {
//        if (mRtmChannel != null) {
//            mRtmChannel.leave(null);
//            mRtmChannel.release();
//            mRtmChannel = null;
//        }
//    }
//
//    /**
//     * API CALLBACK: rtm event listener
//     */
//    class MyRtmClientListener implements RtmClientListener {
//
//        @Override
//        public void onConnectionStateChanged(final int state, int reason) {
//            runOnUiThread(() -> {
//                switch (state) {
//                    case RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING:
//                        showToast(getString(R.string.reconnecting));
//                        break;
//                    case RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED:
//                        showToast(getString(R.string.account_offline));
//                        setResult(MessageUtil.ACTIVITY_RESULT_CONN_ABORTED);
//                        finish();
//                        break;
//                }
//            });
//        }
//
//        @Override
//        public void onMessageReceived(final RtmMessage message, final String peerId) {
//            runOnUiThread(() -> {
//                if (peerId.equals(mPeerId)) {
//                    MessageBean messageBean = new MessageBean(peerId, message, false);
//                    messageBean.setBackground(getMessageColor(peerId));
//                    mMessageBeanList.add(messageBean);
//                    mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
//                    mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
//                } else {
//                    MessageUtil.addMessageBean(peerId, message);
//                }
//            });
//        }
//
//        @Override
//        public void onImageMessageReceivedFromPeer(final RtmImageMessage rtmImageMessage, final String peerId) {
//            runOnUiThread(() -> {
//                if (peerId.equals(mPeerId)) {
//                    MessageBean messageBean = new MessageBean(peerId, rtmImageMessage, false);
//                    messageBean.setBackground(getMessageColor(peerId));
//                    mMessageBeanList.add(messageBean);
//                    mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
//                    mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
//                } else {
//                    MessageUtil.addMessageBean(peerId, rtmImageMessage);
//                }
//            });
//        }
//
//        @Override
//        public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {
//
//        }
//
//        @Override
//        public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {
//
//        }
//
//        @Override
//        public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {
//
//        }
//
//        @Override
//        public void onTokenExpired() {
//
//        }
//
//        @Override
//        public void onPeersOnlineStatusChanged(Map<String, Integer> map) {
//
//        }
//    }

    /**
     * API CALLBACK: rtm channel event listener
     */
//    class MyChannelListener implements RtmChannelListener {
//        @Override
//        public void onMemberCountUpdated(int i) {
//
//        }
//
//        @Override
//        public void onAttributesUpdated(List<RtmChannelAttribute> list) {
//
//        }
//
//        @Override
//        public void onMessageReceived(final RtmMessage message, final RtmChannelMember fromMember) {
//            runOnUiThread(() -> {
//                String account = fromMember.getUserId();
//                Log.i(TAG, "onMessageReceived account = " + account + " msg = " + message);
//                MessageBean messageBean = new MessageBean(account, message, false);
//                messageBean.setBackground(getMessageColor(account));
//                mMessageBeanList.add(messageBean);
//                mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
//                mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
//            });
//        }
//
//        @Override
//        public void onImageMessageReceived(final RtmImageMessage rtmImageMessage, final RtmChannelMember rtmChannelMember) {
//            runOnUiThread(() -> {
//                String account = rtmChannelMember.getUserId();
//                Log.i(TAG, "onMessageReceived account = " + account + " msg = " + rtmImageMessage);
//                MessageBean messageBean = new MessageBean(account, rtmImageMessage, false);
//                messageBean.setBackground(getMessageColor(account));
//                mMessageBeanList.add(messageBean);
//                mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
//                mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
//            });
//        }
//
//        @Override
//        public void onFileMessageReceived(RtmFileMessage rtmFileMessage, RtmChannelMember rtmChannelMember) {
//            runOnUiThread(() -> {
//                String account = rtmChannelMember.getUserId();
//                Log.i(TAG, "onMessageReceived account = " + account + " msg = " + rtmFileMessage);
//                MessageBean messageBean = new MessageBean(account, rtmFileMessage, false);
//                messageBean.setBackground(getMessageColor(account));
//                mMessageBeanList.add(messageBean);
//                mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
//                mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
//            });
//        }
//
//        @Override
//        public void onMemberJoined(RtmChannelMember member) {
//            runOnUiThread(() -> {
//                mChannelMemberCount++;
//                refreshChannelTitle();
//            });
//        }
//
//        @Override
//        public void onMemberLeft(RtmChannelMember member) {
//            runOnUiThread(() -> {
//                mChannelMemberCount--;
//                refreshChannelTitle();
//            });
//        }
//    }
//
//    private int getMessageColor(String account) {
//        for (int i = 0; i < mMessageBeanList.size(); i++) {
//            if (account.equals(mMessageBeanList.get(i).getAccount())) {
//                return mMessageBeanList.get(i).getBackground();
//            }
//        }
//        return MessageUtil.COLOR_ARRAY[MessageUtil.RANDOM.nextInt(MessageUtil.COLOR_ARRAY.length)];
//    }
//
//    private void refreshChannelTitle() {
//        //String titleFormat = getString(R.string.channel_title);
//        String title = String.format(mChannelName, mChannelMemberCount);
//        mTitleTextView.setText(title);
//    }
//
//    private void showToast(final String text) {
//        runOnUiThread(() -> Toast.makeText(MessageActivity.this, text, Toast.LENGTH_SHORT).show());
//    }
}
