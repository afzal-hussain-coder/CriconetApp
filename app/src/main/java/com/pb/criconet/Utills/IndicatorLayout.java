package com.pb.criconet.Utills;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.pb.criconet.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import kotlin.jvm.internal.Intrinsics;

public class IndicatorLayout extends LinearLayout {
    private int indicatorCount;
    private int selectedPosition;
    private HashMap _$_findViewCache;

    private void initIndicators(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorLayout, defStyleAttr, 0);

        try {
            this.indicatorCount = typedArray.getInt(R.styleable.IndicatorLayout_indicatorCount, 0);
        } finally {
            typedArray.recycle();
        }

        this.updateIndicators();
    }

    private int px(float dpValue) {
        Context var10001 = this.getContext();
        Resources var2 = var10001.getResources();
        return (int)(dpValue * var2.getDisplayMetrics().density);
    }

    private void updateIndicators() {
        this.removeAllViews();
        int var1 = 0;

        for(int var2 = indicatorCount; var1 < var2; ++var1) {
            View indicator = new View(getContext());
            LayoutParams layoutParams = new LayoutParams(px(10.0F), px(10.0F));
            layoutParams.setMargins(px(3.0F), px(3.0F), px(3.0F), px(3.0F));
            indicator.setLayoutParams(layoutParams);
            indicator.setBackgroundResource(R.drawable.indicator_unselector);
            addView(indicator);
        }

    }

    public void setIndicatorCount(int count) {
        indicatorCount = count;
        updateIndicators();
    }

    public void selectCurrentPosition(int position) {


        if (position >= 0 && position <= indicatorCount) {
            selectedPosition = position;
            int index = 0;

            for(int var3 = indicatorCount; index < var3; ++index) {
                View indicator = getChildAt(index);
                if (index == selectedPosition) {
                    indicator.setBackgroundResource(R.drawable.indicator_selector);
                } else {
                    indicator.setBackgroundResource(R.drawable.indicator_unselector);
                }
            }
        }

    }

    public IndicatorLayout(Context context) {
        super(context);
    }

    public IndicatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initIndicators(context, attrs, 0);
    }

    public IndicatorLayout(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initIndicators(context, attrs, defStyleAttr);
    }

    public View _$_findCachedViewById(int var1) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }

        View var2 = (View)this._$_findViewCache.get(var1);
        if (var2 == null) {
            var2 = this.findViewById(var1);
            this._$_findViewCache.put(var1, var2);
        }

        return var2;
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }

    }
}
