package com.pb.criconet.propeller;

import android.net.Uri;
import android.widget.Toast;

import com.pb.criconet.R;
import com.pb.criconet.Utills.Toaster;

import java.io.File;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.VirtualBackgroundSource;

public class Constant {

    public static final String MEDIA_SDK_VERSION;

    static {
        String sdk = "undefined";
        try {
            sdk = RtcEngine.getSdkVersion();
        } catch (Throwable e) {
        }
        MEDIA_SDK_VERSION = sdk;
    }

    public static final String MIX_FILE_PATH = "/assets/qt.mp3"; // in assets folder

    public static boolean SHOW_VIDEO_INFO = true;

    public static boolean DEBUG_INFO_ENABLED = true; // Show debug/log info on screen

    public static boolean BEAUTY_EFFECT_ENABLED = true; // Built-in face beautification

    public static final int BEAUTY_EFFECT_DEFAULT_CONTRAST = 1;
    public static final float BEAUTY_EFFECT_DEFAULT_LIGHTNESS = .7f;
    public static final float BEAUTY_EFFECT_DEFAULT_SMOOTHNESS = .5f;
    public static final float BEAUTY_EFFECT_DEFAULT_REDNESS = .1f;

    public static final BeautyOptions BEAUTY_OPTIONS = new BeautyOptions(BEAUTY_EFFECT_DEFAULT_CONTRAST, BEAUTY_EFFECT_DEFAULT_LIGHTNESS, BEAUTY_EFFECT_DEFAULT_SMOOTHNESS, BEAUTY_EFFECT_DEFAULT_REDNESS);

    public static final float BEAUTY_EFFECT_MAX_LIGHTNESS = 1.0f;
    public static final float BEAUTY_EFFECT_MAX_SMOOTHNESS = 1.0f;
    public static final float BEAUTY_EFFECT_MAX_REDNESS = 1.0f;

    public static String getURLForResource (int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://"+ R.class.getPackage().getName()+"/" +resourceId).toString();
    }

    public static final int BACKGROUND_COLOR = 1;
    public static final int BACKGROUND_IMG = 2;
    public int backgroundSourceType;
    public static int color=0;
    public static String source = "https://www.criconet.com/themes/wowonder/img/vbackgroundsession.jpg";

//    static Uri fileUri = Uri.parse("android.resource://com.pb.criconet/" + R.drawable.vb);
//        public static File file= new File(fileUri.getPath());

    //public static final VirtualBackgroundSource VIRTUAL_BACKGROUND_SOURCE=new VirtualBackgroundSource(BACKGROUND_IMG,0,file.getAbsolutePath());
}
