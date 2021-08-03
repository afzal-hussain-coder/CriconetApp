package com.pb.criconet.fontsview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class OSLTextView extends androidx.appcompat.widget.AppCompatTextView {
    public OSLTextView(Context context) {
        super(context);
        createFont();
    }

    public OSLTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public OSLTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans_Regular.ttf");
        setTypeface(font);
    }

}
