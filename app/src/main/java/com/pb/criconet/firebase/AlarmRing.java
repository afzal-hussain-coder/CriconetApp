package com.pb.criconet.firebase;

/**
 * Created by Manish Yadav on 6/22/2017.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;

import com.pb.criconet.R;

public class AlarmRing extends Service {
    static Ringtone r;
    static Vibrator vibrator;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //activating alarm sound

        Uri notification = Uri.parse("android.resource://" + getPackageName() + R.raw.notification_sound);
        r = RingtoneManager.getRingtone(getBaseContext(), notification);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //playing sound alarm
        r.play();

        long[] vibrationCycle = {0, 1000, 1000};
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(vibrationCycle, 1);
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        r.stop();
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

}
