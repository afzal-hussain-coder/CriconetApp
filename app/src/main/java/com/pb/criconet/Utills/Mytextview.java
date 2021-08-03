package com.pb.criconet.Utills;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Ajay on 10/5/2017.
 */
public class Mytextview extends androidx.appcompat.widget.AppCompatTextView {

    public Mytextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Mytextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Mytextview(Context context) {
        super(context);
        init();
    }

    public void init() {
//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"Montserrat_Thin.otf");
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans_Regular.ttf");
        setTypeface(font);

    }
}
