package com.pb.criconet.Utills;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Ajay on 10/5/2017.
 */
public class MytextviewBold extends androidx.appcompat.widget.AppCompatTextView {

    public MytextviewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MytextviewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MytextviewBold(Context context) {
        super(context);
        init();
    }

    public void init() {
//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"Montserrat_Light.otf");
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans_Semibold.ttf");
        setTypeface(typeface);

    }
}
