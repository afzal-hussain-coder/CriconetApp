package com.pb.criconet.Utills;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.pb.criconet.R;
import com.pb.criconet.fontsview.OSLTextView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateDropDownView extends OSLTextView implements View.OnClickListener, PopupWindow.OnDismissListener{
    private onClickInterface onClickInterface;
    final Calendar myCalendar = Calendar.getInstance();
    public DateDropDownView(Context context) {
        super(context);
        initView();
    }

    public DateDropDownView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DateDropDownView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
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
        View popupView = inflater.inflate(R.layout.popup, null);
        CalendarView datePicker = popupView.findViewById(R.id.calendarView);
        datePicker.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NotNull EventDay eventDay) {
                String dateGot = Global.getDateGott(eventDay.getCalendar().getTime().toString());
                if (onClickInterface != null)
                    onClickInterface.onClickDone(dateGot);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popupWindow.dismiss();
                    }
                },1000);

            }
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
            if (onClickInterface != null)
                onClickInterface.onClickAction();
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

        void onClickDone(String name);

        void onDismiss();
    }
}
