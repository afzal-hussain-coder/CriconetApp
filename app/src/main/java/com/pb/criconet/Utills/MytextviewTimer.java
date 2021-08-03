package com.pb.criconet.Utills;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Ajay on 10/5/2017.
 */
public class MytextviewTimer extends androidx.appcompat.widget.AppCompatTextView {

    public MytextviewTimer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MytextviewTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MytextviewTimer(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "digital_7.ttf");
//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"Montserrat_Thin.otf");
//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"MontserratAlternates_Light.otf");
//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"MontserratAlternates_ExtraLight.otf");
        setTypeface(typeface, 1);

    }
}
