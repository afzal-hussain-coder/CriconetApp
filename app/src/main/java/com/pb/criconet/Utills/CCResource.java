package com.pb.criconet.Utills;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class CCResource {

    public static String getString(Context context, @StringRes int id) {
        return context.getResources().getString(id);
    }

    public static String getString(Context context, @StringRes int id, Object... arguments) {
        return context.getResources().getString(id, arguments);
    }

    public static Drawable getDrawable(Context context, @DrawableRes int id) {
        return context.getResources().getDrawable(id);
    }

    public static int getColor(Context context, @ColorRes int id) {
        return context.getResources().getColor(id);
    }
}
