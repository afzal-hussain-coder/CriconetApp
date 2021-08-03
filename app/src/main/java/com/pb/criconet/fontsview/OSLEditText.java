package com.pb.criconet.fontsview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class OSLEditText extends androidx.appcompat.widget.AppCompatEditText {
    public OSLEditText(Context context) {
        super(context);
        createFont();
    }

    public OSLEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public OSLEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans_Regular.ttf");
        setTypeface(font);
    }

}
