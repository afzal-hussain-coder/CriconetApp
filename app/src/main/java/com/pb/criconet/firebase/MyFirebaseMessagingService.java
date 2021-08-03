///**
// * Copyright 2016 Google Inc. All Rights Reserved.
// * <p>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p>
// * http://www.apache.org/licenses/LICENSE-2.0
// * <p>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.pb.criconet.firebase;
//
//import android.app.IntentService;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.media.Ringtone;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//import android.preference.PreferenceManager;
//import android.util.Log;
//
//import androidx.core.app.NotificationCompat;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//import com.pb.criconet.Utills.Toaster;
//import com.pb.criconet.activity.BookingActivity;
//import com.pb.criconet.activity.MainActivity;
//import com.pb.criconet.R;
//import com.pb.criconet.Utills.SessionManager;
//
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Map;
//import java.util.Random;
//
//import io.agora.rtc.IRtcEngineEventHandler;
//import io.agora.rtc.RtcEngine;
//
//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//
//
//    public static final String ACTION_1 = "action_1";
//    private static final String TAG = "MyFirebaseMsgService";
//    SharedPreferences prefs;
//    String content = "";
//    PendingIntent end_call_Intent;
//    PendingIntent accept_call_Intent;
//    String booking_id="",notificationType="",post_id ="",title="",messageBody="";
//
//
//    @Override
//    public void onNewToken(String s) {
//        super.onNewToken(s);
//        prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        SessionManager.save_devicetoken(prefs, s);
//    }
//
//    /**
//     * Called when message is received.
//     *
//     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
//     */
//    // [START receive_message]
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // TODO(developer): Handle FCM messages here.
//        // If the application is in the foreground handle both data and notification messages here.
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
//        prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        // TODO(developer): Handle FCM messages here.
//
//        Log.d("From: %s",remoteMessage.getFrom());
//        if (remoteMessage.getData().size() > 0) {
//            Log.d("Message data: %s", remoteMessage.getData().toString());
//
//        }
//        if (remoteMessage.getNotification() != null) {
//            Log.d("Notification Body %s", remoteMessage.getNotification().getBody());
//
//        }
//        try {
//            JSONObject object1 = new JSONObject(remoteMessage.getData());
//            if (object1.has("type")) {
//                readData(object1);
//            } else {
//                try {
//                    Map<String, String> params = remoteMessage.getData();
//                    object1 = new JSONObject(params.get("data"));
//                    readData(object1);
//                } catch (JSONException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            try {
//                Map<String, String> params = remoteMessage.getData();
//                JSONObject object1 = new JSONObject(params.get("data"));
//                readData(object1);
//            } catch (JSONException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }
//
//    private void readData(JSONObject object1) {
//        try {
//            if (object1.getString("type").equalsIgnoreCase("chat")) {
//                sendNotificationforchat(object1.getString("message"),
//                        object1.getString("type"), object1.getString("person_id"),
//                        object1.getString("person_name"), object1.getString("person_image"),
//                        object1.getString("person_firebaseid"), object1.getString("deviceToken"), "");
//            } else if (object1.getString("type").equalsIgnoreCase("GroupChat")) {
//                sendNotificationforchat(object1.getString("message"),
//                        object1.getString("type"), object1.getString("person_id"),
//                        object1.getString("person_name"), object1.getString("person_image"),
//                        object1.getString("person_firebaseid"), object1.getString("deviceToken"), object1.getString("sender_name"));
//            } else if (object1.getString("type").equalsIgnoreCase("videocall")) {
////                sendNotificationForVideoCall(object1.getString("message"), object1.getString("channel"), object1.getString("person_name"));
//            } else if (object1.getString("type").equalsIgnoreCase("audiocall")) {
////                sendNotificationForAudioCall(object1.getString("message"), object1.getString("channel"), object1.getString("person_name"), object1.getString("person_image"));
////            } else if (object1.getString("type").equalsIgnoreCase("live_streaming")) {
////                sendNotification(object1.getString("message"), object1.getString("type"), content);
//            } else {
//                content = object1.getString("message");
//                sendNotification(object1);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void sendNotificationforchat(String message, String type, String person_id, String person_name, String person_image, String person_firebaseid, String deviceToken, String sendername) {
//        int not_id;
//        Intent intent;
//        if (type.equalsIgnoreCase("GroupChat")) {
//        /*    intent = new Intent(this, Group_inner.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("stdid", "" + person_id);
//            intent.putExtra("name", "" + person_name);
//            intent.putExtra("image", "" + person_image);
//            intent.putExtra("firebaseid", "" + person_firebaseid);
//            intent.putExtra("deviceTokenList", "" + deviceToken);*/
//            message = sendername + " : " + message;
//
//            try {
//                not_id = Integer.parseInt(person_id);
//            } catch (Exception e) {
//                not_id = new Random().nextInt();
//            }
//        } else {
//      /*      intent = new Intent(this, Individual_inner.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            intent.putExtra("person_id", "" + person_id);
////            intent.putExtra("image", "" + person_image);
////            intent.putExtra("person_name", "" + person_name);
////            intent.putExtra("deviceToken", "" + deviceToken);
////            intent.putExtra("person_firebase_id", "" + person_firebaseid);
//            intent.putExtra("stdid", "" + person_id);
//            intent.putExtra("name", "" + person_name);
//            intent.putExtra("image", "" + person_image);
//            intent.putExtra("firebaseid", "" + person_firebaseid);
//            intent.putExtra("deviceToken", "" + deviceToken);*/
//
//            try {
//                not_id = Integer.parseInt(person_id);
//            } catch (Exception e) {
//                not_id = new Random().nextInt();
//            }
//        }
//
//      //  PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.channel_id))
////                .setSmallIcon(R.drawable.app_icon)
//                .setContentTitle(person_name)
////                .setStyle(new NotificationCompat.BigTextStyle().bigText(person_name))
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setVibrate(new long[]{1000, 1000});
//               // .setContentIntent(pendingIntent);
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            notificationBuilder.setSmallIcon(R.drawable.icon_transperent);
//            notificationBuilder.setColor(getResources().getColor(R.color.app_blue_grey));
//        } else {
//            notificationBuilder.setSmallIcon(R.drawable.app_icon);
//        }
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            /* Create or update. */
//            NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.channel_id), "Criconet", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
////        notificationManager.notify(as /* ID of notification */, notificationBuilder.build());
//        try {
//            notificationManager.notify(not_id /* ID of notification */, notificationBuilder.build());
//        } catch (Exception ei) {
//            ei.printStackTrace();
//            Random rand = new Random();
//            int as = rand.nextInt();
//            notificationManager.notify(as /* ID of notification */, notificationBuilder.build());
//        }
//
//        SaveCounter(person_id);
//
//    }
//
//
//    /**
//     * Create and show a simple notification containing the received FCM message.
//     *
//     * //@param messageBody FCM message body received.
//     */
//
//    private void sendNotification(JSONObject json) {
//
//
//        if (json.has("booking_id")) {
//            try {
//                booking_id = json.getString("booking_id");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (json.has("post_id")) {
//            try {
//                post_id = json.getString("post_id");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (json.has("type")) {
//            try {
//                notificationType = json.getString("type");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        Log.d(TAG, booking_id + "/" + notificationType + "/" + post_id);
//        Intent intent;
//        if (notificationType.equalsIgnoreCase("booking")) {
//            intent = new Intent(this, BookingActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        } else {
//            intent = new Intent(this, BookingActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        }
//        intent.putExtra("type", notificationType);
////        intent.putExtra("title", title);
////        intent.putExtra("desc", messageBody);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri notification = Uri.parse("android.resource://" + getPackageName() + R.raw.sounds);
//        Ringtone ringtone = RingtoneManager.getRingtone(getBaseContext(), notification);
//        Uri defaultSoundUri;
//        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder notificationBuilder;
//
//        if (notificationType.equalsIgnoreCase("live_streaming"))
//            notificationBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.ls_channel_id));
//        else
//            notificationBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.channel_id));
//
////                .setSmallIcon(R.drawable.app_icon)
//        notificationBuilder.setContentTitle(title)
//                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
//                    .setContentText(messageBody)
//                    .setAutoCancel(true)
//                    .setSound(notification)
//                    .setVibrate(new long[]{1000, 1000})
//                    .setContentIntent(pendingIntent);
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            notificationBuilder.setSmallIcon(R.drawable.app_icon);
//            notificationBuilder.setColor(getResources().getColor(R.color.app_blue_grey));
//        } else {
//            notificationBuilder.setSmallIcon(R.drawable.app_icon);
//        }
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            /* Create or update. */
//            NotificationChannel nchannel;
//            if (notificationType.equalsIgnoreCase("live_streaming"))
//                nchannel = new NotificationChannel(getResources().getString(R.string.ls_channel_id), "Criconet", NotificationManager.IMPORTANCE_DEFAULT);
//            else
//                nchannel = new NotificationChannel(getResources().getString(R.string.channel_id), "Criconet", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(nchannel);
//        }
//
//        Random rand = new Random();
//        int as = rand.nextInt();
//        notificationManager.notify(as /* ID of notification */, notificationBuilder.build());
//
//    }
//
//    public void SaveCounter(String person_id) {
//        SharedPreferences sharedPreferences = getSharedPreferences("registerData", Context.MODE_PRIVATE);
//        SharedPreferences.Editor myEditor = sharedPreferences.edit();
//        int count = sharedPreferences.getInt("person_id" + person_id, 0);
//        count++;
//        myEditor.putInt("person_id" + person_id, count);
//        myEditor.apply();//returns nothing,don't forgot to commit changes
//        //Log.d(" = " +String.valueOf(count));
//        SaveTotalCounter();
//    }
//
//    public void SaveTotalCounter() {
//        SharedPreferences sharedPreferences = getSharedPreferences("registerData", Context.MODE_PRIVATE);
//        SharedPreferences.Editor myEditor = sharedPreferences.edit();
////        save_TotalNotifications
//        int count = sharedPreferences.getInt("TotalNotifications", 0);
//        count++;
//        myEditor.putInt("TotalNotifications", count);
//        myEditor.apply();//returns nothing,don't forgot to commit changes
//        Log.e("Counter: ", "TotalNotifications = " + count);
////        ShortcutBadger.applyCount(getApplicationContext(), count);
//    }
//
//    public static class NotificationActionService extends IntentService {
//        private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
//            @Override
//            public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
//            }
//
//            @Override
//            public void onUserOffline(int uid, int reason) {
//            }
//
//            @Override
//            public void onUserMuteVideo(final int uid, final boolean muted) {
//            }
//        };
//        String LOG_TAG = "LOG_TAG";
//        String channel_nm = "";
//        private RtcEngine mRtcEngine;
//
//        public NotificationActionService() {
//            super(NotificationActionService.class.getSimpleName());
//        }
//
//        @Override
//        protected void onHandleIntent(Intent intent) {
//            String action = intent.getAction();
//            channel_nm = intent.getStringExtra("channel");
////            DebugUtils.log("Received notification action: " + action);
//            if (ACTION_1.equals(action)) {
//                // TODO: handle action 1.
//                // If you want to cancel the notification: NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
//                initializeAgoraEngine();
//            }
//        }
//
//        private void initializeAgoraEngine() {
//            try {
//                mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
//                mRtcEngine.joinChannel(null, channel_nm, "Extra Optional Data", 0);
//                mRtcEngine.leaveChannel();
//                RtcEngine.destroy();
//                mRtcEngine = null;
//            } catch (Exception e) {
//                Log.e(LOG_TAG, Log.getStackTraceString(e));
//                throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
//            }
//        }
//    }
//
//}
