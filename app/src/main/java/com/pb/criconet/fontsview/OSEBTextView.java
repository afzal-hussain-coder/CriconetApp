package com.pb.criconet.fontsview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OSEBTextView extends androidx.appcompat.widget.AppCompatTextView {
    public OSEBTextView(@NonNull Context context) {
        super(context);
        createFont();
    }

    public OSEBTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public OSEBTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans_Semibold.ttf");
        setTypeface(font);
    }
}
