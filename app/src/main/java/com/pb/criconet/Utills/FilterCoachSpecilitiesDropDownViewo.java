package com.pb.criconet.Utills;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.adapters.BookingHistoryAdapter;
import com.pb.criconet.fontsview.OSLTextView;
import com.pb.criconet.models.BookingHistory;
import com.pb.criconet.models.Specialization;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FilterCoachSpecilitiesDropDownViewo extends OSLTextView implements View.OnClickListener, PopupWindow.OnDismissListener {

    private ArrayList<Specialization> optionList = new ArrayList<>();
    private ArrayList<Integer> idlist = new ArrayList<>();
    private ArrayList<String> nameList = new ArrayList<>();
    private FilterCoachSpecilitiesDropDownViewo.onClickInterface onClickInterface;
    FrameLayout fl_done;
    PopupWindow popupWindow;

    public FilterCoachSpecilitiesDropDownViewo(Context context) {
        super(context);
        initView();
    }

    public FilterCoachSpecilitiesDropDownViewo(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FilterCoachSpecilitiesDropDownViewo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        this.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private PopupWindow popupWindowSort(Context context) {

        // initialize a pop up window type
        popupWindow = new PopupWindow();
        popupWindow.setWidth(this.getWidth());
        popupWindow.setOutsideTouchable(true);
        RelativeLayout viewGroup = (RelativeLayout) findViewById(R.id.viewgroup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.coach_filter_layout, viewGroup);
        popupWindow.setContentView(layout);
        popupWindow.setElevation(10);
        View myPoppyView = popupWindow.getContentView();

        RecyclerView reclerFilterCoach = myPoppyView.findViewById(R.id.reclerFilterCoach);
        reclerFilterCoach.setHasFixedSize(true);
        reclerFilterCoach.setLayoutManager(new LinearLayoutManager(context));
        fl_done = myPoppyView.findViewById(R.id.fl_done);


        CoachFilterAdapter coachFilterAdapter = new CoachFilterAdapter(context, optionList, idlist, new clickCallback() {
            @Override
            public void buttonClick(ArrayList<Integer> id, ArrayList<String> name) {
                if (onClickInterface != null) {
                    fl_done.setVisibility(VISIBLE);
                    fl_done.setOnClickListener(v -> {
                        onClickInterface.onClickDone(id, name);
                        popupWindow.dismiss();
                    });
//                    if(id.size()>0){
//                        fl_done.setVisibility(VISIBLE);
//                        fl_done.setOnClickListener(v -> {
//                            onClickInterface.onClickDone(id,name);
//                            popupWindow.dismiss();
//                        });
//                    }else{
//                        fl_done.setVisibility(GONE);
//                    }


                    //popupWindow.dismiss();
                }
            }
        });
        reclerFilterCoach.setAdapter(coachFilterAdapter);
        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        if (optionList.size() > 10)
            popupWindow.setHeight(480);
        else
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(this);

        // set the ListView as popup content
        //popupWindow.setContentView(listViewSort);

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

    public void setOptionList(ArrayList<Specialization> list) {
        this.optionList = list;
    }

    public void setClickListener(FilterCoachSpecilitiesDropDownViewo.onClickInterface listener) {
        this.onClickInterface = listener;
    }

    @Override
    public void onDismiss() {
        if (onClickInterface != null)
            onClickInterface.onDismiss();
    }

    public interface onClickInterface {
        void onClickAction();

        void onClickDone(ArrayList<Integer> id, ArrayList<String> name);

        void onDismiss();
    }

//    public class CustomAdapter extends ArrayAdapter<Specialization> implements OnClickListener {
//
//        private ArrayList<Specialization> dataSet;
//        Context mContext;
//
//        // View lookup cache
//        private class ViewHolder {
//            CheckBox cb;
//            TextView txtName;
//        }
//
//        CustomAdapter(ArrayList<Specialization> data, Context context) {
//            super(context, R.layout.filter_coach_item, data);
//            this.dataSet = data;
//            this.mContext = context;
//        }
//
//        @Override
//        public void onClick(View v) {
//            int position = (Integer) v.getTag();
//            Specialization object = getItem(position);
//
//        }
//
//        @Override
//        public int getCount() {
//            return dataSet.size();
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//            // Get the data item for this position
//            Specialization dataModel = getItem(position);
//            // Check if an existing view is being reused, otherwise inflate the view
//            FilterCoachSpecilitiesDropDownViewo.CustomAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
//            if (convertView == null) {
//                viewHolder = new FilterCoachSpecilitiesDropDownViewo.CustomAdapter.ViewHolder();
//                LayoutInflater inflater = LayoutInflater.from(getContext());
//                convertView = inflater.inflate(R.layout.filter_coach_item, parent, false);
//                viewHolder.txtName = convertView.findViewById(R.id.text);
//                viewHolder.cb = convertView.findViewById(R.id.cb);
//
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (FilterCoachSpecilitiesDropDownViewo.CustomAdapter.ViewHolder) convertView.getTag();
//            }
//
//            assert dataModel != null;
//            viewHolder.txtName.setText(dataModel.getTitle());
//            viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    if (isChecked) {
//                        if (onClickInterface != null) {
//                            onClickInterface.onClickDone(dataModel.getTitle(), dataModel.getId());
//                            popupWindow.dismiss();
//                        }
//                    }
//
//                }
//            });
//            // Return the completed view to render on screen
//            return convertView;
//        }
//
//
//    }


    public class CoachFilterAdapter extends RecyclerView.Adapter<CoachFilterAdapter.MyViewHolder> implements OnClickListener {

        private ArrayList<Specialization> dataSet;
        private ArrayList<Integer> id;
        Context mContext;
        private clickCallback callback;

        public CoachFilterAdapter(Context context, ArrayList<Specialization> dataSet, ArrayList<Integer> id, clickCallback callback) {
            this.mContext = context;
            this.dataSet = dataSet;
            this.callback = callback;
            this.id = id;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_coach_item, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull FilterCoachSpecilitiesDropDownViewo.CoachFilterAdapter.MyViewHolder holder, int position) {
            Specialization dataModel = dataSet.get(position);
            holder.txtName.setText(dataModel.getTitle());
            holder.cb.setChecked(dataSet.get(position).getSelected());
            holder.cb.setTag(position);
            holder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Integer pos = (Integer) holder.cb.getTag();
                    Toaster.customToast(dataSet.get(pos).getSelected() + "");

                    if (dataSet.get(pos).getSelected()) {
                        try{
                            id.remove(dataModel.getId());
                            nameList.remove(dataModel.getTitle());
                            callback.buttonClick(id,nameList);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        dataSet.get(pos).setSelected(false);
                    } else {
                        id.add(dataModel.getId());
                        nameList.add(dataModel.getTitle());
                        callback.buttonClick(id, nameList);
                        dataSet.get(pos).setSelected(true);
                    }
                }
            });

//            if(id.size()==0){
//                holder.cb.setChecked(false);
//            }else{
//                for (int i=0;i<id.size();i++){
//                    if(id.get(i)==dataSet.get(position).getId()){
//                        holder.cb.setChecked(true);
//                        //break;
//                    }
////                    else{
////                       // holder.cb.setChecked(false);
////                    }
//                }
//            }


//            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    if (isChecked) {
//                        id.add(dataModel.getId());
//                        nameList.add(dataModel.getTitle());
//                        callback.buttonClick(id,nameList);
//                    }else{
//                        try{
//                            id.remove(dataModel.getId());
//                            nameList.remove(dataModel.getTitle());
//                            callback.buttonClick(id,nameList);
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                }
//            });
        }


        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            Specialization object = dataSet.get(position);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            CheckBox cb;
            TextView txtName;

            MyViewHolder(View itemView) {
                super(itemView);
                cb = itemView.findViewById(R.id.cb);
                txtName = itemView.findViewById(R.id.text);
                // get the reference of item view's
            }
        }

    }

    public interface clickCallback {
        public void buttonClick(ArrayList<Integer> id, ArrayList<String> name);
    }
}

