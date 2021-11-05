package com.pb.criconet.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pb.criconet.R;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.activity.Splash;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

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
           // Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            JSONObject json = null;//notification;

            json = new JSONObject(remoteMessage.getData());
            //Log.d("Json", json + "");
            messageBody = remoteMessage.getNotification().getBody();
            title = remoteMessage.getNotification().getTitle();
            icon = remoteMessage.getNotification().getIcon();
            sound = remoteMessage.getNotification().getSound();


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
                    //Toaster.customToast(notificationType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            sendNotification(sound, icon, title, messageBody, notificationType, booking_id);

        }
    }
    

    private void sendNotification(String sound,String icon,String title,String msg,String type,String bookingid) {


        try {

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            long notificatioId = System.currentTimeMillis();
            Intent intent=null;
            //Log.d("Type",type);
            if(type.equalsIgnoreCase("Coach_List")){
                intent = new Intent(this, Splash.class);
                intent.putExtra("type", "coachFreagment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            }else if(notificationType.equalsIgnoreCase("booking")){
                intent = new Intent(this, Splash.class);
                intent.putExtra("booking_id", booking_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            } else if(notificationType.equalsIgnoreCase("live_streaming")){
                intent = new Intent(this, Splash.class);
                intent.putExtra("type", "live_streaming");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            PendingIntent contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, 0);

            int currentapiVersion = Build.VERSION.SDK_INT;
            if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP){
                currentapiVersion = R.drawable.app_logo2;
            } else{
                currentapiVersion = R.drawable.app_logo2;
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(currentapiVersion)
                    .setContentTitle(this.getResources().getString(R.string.app_name))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .setContentIntent(contentIntent);
            mNotificationManager.notify((int) notificatioId, notificationBuilder.build());
        } catch (Exception  e) {
            e.printStackTrace();
        }

//        Intent intent;
//
//        if(notificationType.equalsIgnoreCase("booking")){
//            if (bookingid.equalsIgnoreCase("")) {
//                intent = new Intent(this, BookingActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            } else {
//                intent = new Intent(this, BookingDetailsActivity.class);
//                intent.putExtra("FROM", "2");
//                intent.putExtra("BookingID", bookingid);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//        } else if(notificationType.equalsIgnoreCase("Coach_List")){
//            intent = new Intent(this, MainActivity.class);
//            intent.putExtra("type", "coachFreagment");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        }
//        else{
//            intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        }// Here pass your activity where you want to redirect.
//
//        intent.putExtra("type", notificationType);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0  Request code , intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri;
//        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//
//        Uri soundd = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notification_sound);
//        Bitmap bm = BitmapFactory.decodeResource(this.getResources(),R.drawable.app_icon); //large drawable);
//        NotificationCompat.Builder notificationBuilder;
//
//        if (notificationType.equalsIgnoreCase("live_streaming"))
//            notificationBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.ls_channel_id));
//        else
//            notificationBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.channel_id));
//
//        notificationBuilder.setContentTitle(title)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
//                .setContentText(msg)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setVibrate(new long[]{1000, 1000})
//                .setSmallIcon(R.drawable.app_logo2)
//                .setLargeIcon(bm)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//             Create or update.
//            NotificationChannel nchannel;
//
//            if (notificationType.equalsIgnoreCase("live_streaming"))
//                nchannel = new NotificationChannel(getResources().getString(R.string.ls_channel_id), "Criconet", NotificationManager.IMPORTANCE_DEFAULT);
//            else
//                nchannel = new NotificationChannel(getResources().getString(R.string.channel_id), "Criconet", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(nchannel);
//        }
//
//        Random rand = new Random();
//        int as = rand.nextInt();
//        notificationManager.notify(as  ID of notification , notificationBuilder.build());
    }
}
