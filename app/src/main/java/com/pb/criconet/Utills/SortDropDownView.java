package com.pb.criconet.Utills;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.pb.criconet.R;
import com.pb.criconet.fontsview.OSLTextView;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class SortDropDownView extends OSLTextView implements View.OnClickListener, PopupWindow.OnDismissListener{
    private onClickInterface onClickInterface;
    final Calendar myCalendar = Calendar.getInstance();
    SeekBar seek_bar_short_by_experience;
    int progresExp=0;
    SeekBar seek_bar_short_by_price;
    int progressPrice=0;
    public SortDropDownView(Context context) {
        super(context);
        initView();
    }

    public SortDropDownView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SortDropDownView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView() {
        this.setOnClickListener(this);
    }


    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private PopupWindow popupWindowSort(Context context,View view) {
        PopupWindow popupWindow = new PopupWindow();
        popupWindow.setWidth(this.getWidth());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(context.getDrawable(R.drawable.booking_fileter_bg));
        popupWindow.setElevation(10);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);


        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.sort_popup, null);
        TextView tv_short_by_experience = popupView.findViewById(R.id.tv_short_by_experience);
        seek_bar_short_by_experience= popupView.findViewById(R.id.seek_bar_short_by_experience);
        TextView tv_short_by_price = popupView.findViewById(R.id.tv_short_by_price);
        seek_bar_short_by_price= popupView.findViewById(R.id.seek_bar_short_by_price);
        FrameLayout fl_short = popupView.findViewById(R.id.fl_short);

        seek_bar_short_by_experience.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progresExp = progress;
                tv_short_by_experience.setText(String.valueOf(progresExp));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek_bar_short_by_price.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressPrice = progress;
                tv_short_by_price.setText(String.valueOf(progressPrice));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        fl_short.setOnClickListener(v -> {
            String experience = String.valueOf(seek_bar_short_by_experience.getProgress());
            String price = String.valueOf(seek_bar_short_by_price.getProgress());
            onClickInterface.onClickDone(experience,price);
            popupWindow.dismiss();
        });

        popupWindow.setOnDismissListener(this);
        popupWindow.setFocusable(true);
        popupWindow.setContentView(popupView);
        return popupWindow;
    }


    @Override
    public void onClick(View v) {
        if (v == this) {
            PopupWindow window = popupWindowSort(v.getContext(),v.getRootView());
            window.showAsDropDown(v, 0, 0);
            if (onClickInterface != null){
                onClickInterface.onClickAction();
                seek_bar_short_by_experience.setProgress(progresExp);
                seek_bar_short_by_price.setProgress(progressPrice);
            }
        }

    }

    public void setClickListener(onClickInterface listener) {
        this.onClickInterface = listener;
    }

    @Override
    public void onDismiss() {
        if (onClickInterface != null)
            onClickInterface.onDismiss();
    }

    public interface onClickInterface {
        void onClickAction();

        void onClickDone(String experience,String price);

        void onDismiss();
    }
}
