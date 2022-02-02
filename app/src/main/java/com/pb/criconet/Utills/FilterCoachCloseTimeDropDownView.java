package com.pb.criconet.Utills;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import com.pb.criconet.R;
import com.pb.criconet.fontsview.OSLTextView;

import java.util.ArrayList;

public class FilterCoachCloseTimeDropDownView extends OSLTextView implements View.OnClickListener, PopupWindow.OnDismissListener {

    private ArrayList<DataModel> optionList = new ArrayList<>();
    private FilterCoachCloseTimeDropDownView.onClickInterface onClickInterface;

    public FilterCoachCloseTimeDropDownView(Context context) {
        super(context);
        initView();
    }

    public FilterCoachCloseTimeDropDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FilterCoachCloseTimeDropDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        this.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private PopupWindow popupWindowSort(Context context) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow();
        popupWindow.setWidth(this.getWidth());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(context.getDrawable(R.drawable.booking_fileter_bgg));
        popupWindow.setElevation(10);

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, options);
        FilterCoachCloseTimeDropDownView.CustomAdapter adapter = new FilterCoachCloseTimeDropDownView.CustomAdapter(optionList,context);
        // the drop down list is a list view
        ListView listViewSort = new ListView(context);

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);

        // set on item selected
        listViewSort.setOnItemClickListener((parent, view, position, id) -> {
            DataModel model = optionList.get(position);
            this.setText(model.getName());
            Typeface typeface = ResourcesCompat.getFont(context, R.font.opensans_semibold);
            this.setTypeface(typeface);
            if (onClickInterface != null)
                onClickInterface.onClickDone(model.getName());
            popupWindow.dismiss();
        });

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        if(optionList.size()>10)
            popupWindow.setHeight(480);
        else
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(this);

        // set the ListView as popup content
        popupWindow.setContentView(listViewSort);

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(getRootView().getWindowToken(), 0);
        getRootView().clearFocus();


        return popupWindow;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v == this) {
            PopupWindow window = popupWindowSort(v.getContext());
            window.showAsDropDown(v, 0, 0);
            if (onClickInterface != null)
                onClickInterface.onClickAction();
        }
    }

    public void setOptionList(ArrayList<DataModel> list){
        this.optionList = list;
    }

    public void setClickListener(FilterCoachCloseTimeDropDownView.onClickInterface listener) {
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

    public class CustomAdapter extends ArrayAdapter<DataModel> implements OnClickListener {

        private ArrayList<DataModel> dataSet;
        Context mContext;

        // View lookup cache
        private class ViewHolder {
            int id;
            TextView txtName;
        }

        CustomAdapter(ArrayList<DataModel> data, Context context) {
            super(context, R.layout.coach_close_time_spinner_item, data);
            this.dataSet = data;
            this.mContext = context;
        }

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            DataModel object = getItem(position);

        }

        @Override
        public int getCount() {
            return dataSet.size();
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            // Get the data item for this position
            DataModel dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            FilterCoachCloseTimeDropDownView.CustomAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new FilterCoachCloseTimeDropDownView.CustomAdapter.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.coach_close_time_spinner_item, parent, false);
                viewHolder.txtName = convertView.findViewById(R.id.text_filter);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (FilterCoachCloseTimeDropDownView.CustomAdapter.ViewHolder) convertView.getTag();
            }

            assert dataModel != null;
            viewHolder.txtName.setText(dataModel.getName());
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.opensans_semibold);
            viewHolder.txtName.setTypeface(typeface);
            // Return the completed view to render on screen
            return convertView;
        }


    }
}

