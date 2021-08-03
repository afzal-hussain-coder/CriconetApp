package com.pb.criconet.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pb.criconet.R;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.activity.BookingActivity;
import com.pb.criconet.activity.BookingDetails;
import com.pb.criconet.activity.BookingDetailsActivity;
import com.pb.criconet.activity.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Map;
import java.util.Random;

public class ServerNotificationService extends FirebaseMessagingService {
    public static final String TAG = ServerNotificationService.class.getSimpleName();
    SharedPreferences prefs;
    String booking_id = "", notificationType = "", post_id = "", title = "", messageBody = "",
    icon="",sound="";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SessionManager.save_devicetoken(prefs, s);
    }

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());


            JSONObject json = null;//notification;

            json = new JSONObject(remoteMessage.getData());
            messageBody =remoteMessage.getNotification().getBody();
            title =remoteMessage.getNotification().getTitle();
            icon =remoteMessage.getNotification().getIcon();
            sound =remoteMessage.getNotification().getSound();


            if (json.has("booking_id")) {
                try {
                    booking_id = json.getString("booking_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (json.has("post_id")) {
                try {
                    post_id = json.getString("post_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (json.has("type")) {
                try {
                    notificationType = json.getString("type");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, title+"/"+"?"+messageBody+booking_id + "/" + notificationType + "/" + post_id);

            sendNotification(sound,icon,title,messageBody,notificationType,booking_id);





        }
    }

    private void sendNotification(String sound,String icon,String title,String msg,String type,String bookingid) {
        Intent intent;
        if(type.equalsIgnoreCase("booking")){
            if (bookingid.equalsIgnoreCase("")) {
                intent = new Intent(this, BookingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                intent = new Intent(this, BookingDetailsActivity.class);
                intent.putExtra("FROM", "2");
                intent.putExtra("BookingID", bookingid);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        }else{
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }// Here pass your activity where you want to redirect.

        intent.putExtra("type", notificationType);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri;
        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        Uri soundd = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notification_sound);
        Bitmap bm = BitmapFactory.decodeResource(this.getResources(),R.drawable.app_icon); //large drawable);
        NotificationCompat.Builder notificationBuilder;

        if (notificationType.equalsIgnoreCase("live_streaming"))
            notificationBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.ls_channel_id));
        else
            notificationBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.channel_id));

        notificationBuilder.setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{1000, 1000})
                .setSmallIcon(R.drawable.app_logo2)
                .setLargeIcon(bm)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            NotificationChannel nchannel;

            if (notificationType.equalsIgnoreCase("live_streaming"))
                nchannel = new NotificationChannel(getResources().getString(R.string.ls_channel_id), "Criconet", NotificationManager.IMPORTANCE_DEFAULT);
            else
                nchannel = new NotificationChannel(getResources().getString(R.string.channel_id), "Criconet", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(nchannel);
        }

        Random rand = new Random();
        int as = rand.nextInt();
        notificationManager.notify(as /* ID of notification */, notificationBuilder.build());
    }
}
