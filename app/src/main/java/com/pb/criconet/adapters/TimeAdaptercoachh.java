package com.pb.criconet.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.models.DataModel;
import com.pb.criconet.models.TimeSlot;
import com.pb.criconet.models.updatedTimeSlot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeAdaptercoachh extends RecyclerView.Adapter<TimeAdaptercoachh.MyViewHolder> {
    private Context context;
    private List<TimeSlot.Datum> data;
    private timeSelectt callback;
    ArrayList<String> arrayListUser;
    ArrayList<updatedTimeSlot>updatedSlotList;

    public TimeAdaptercoachh(Context context, List<TimeSlot.Datum> data, ArrayList<updatedTimeSlot>updatedSlotList, timeSelectt callback) {
        this.context = context;
        this.data = data;
        this.updatedSlotList = updatedSlotList;
        this.callback = callback;
        arrayListUser=new ArrayList<>();
    }

    @Override
    public TimeAdaptercoachh.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_time_coach, parent, false);
        TimeAdaptercoachh.MyViewHolder vh = new TimeAdaptercoachh.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TimeAdaptercoachh.MyViewHolder holder, final int position) {
        // set the data in items
        TimeSlot.Datum coachLanguage= data.get(position);
        holder.textView.setText(data.get(position).getSlotValue());

        for(int i =0;i<updatedSlotList.size();i++){
                if(updatedSlotList.get(i).getSlotId().equalsIgnoreCase(coachLanguage.getSlotId())){
                    holder.radio.setChecked(true);
                    arrayListUser.add(coachLanguage.getSlotId());
                    callback.getSlotId(arrayListUser);
                    break;
                }
        }

        holder.radio.setOnClickListener(view -> {
            final boolean isChecked = holder.radio.isChecked();

            if (isChecked) {
                if (!arrayListUser.contains(data.get(position).getSlotValue()))
                    arrayListUser.add(data.get(position).getSlotId());
            } else {
                arrayListUser.remove(data.get(position).getSlotId());
            }
            callback.getSlotId(arrayListUser);
        });


    }

    private String convertTime(String time) {
        Date dt = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
        return sdf.format(dt);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CheckBox radio;

        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            textView = itemView.findViewById(R.id.textView);
            radio = itemView.findViewById(R.id.radio);

        }
    }

    public interface timeSelectt {
        public void getSlotId(ArrayList<String> arrayListUser);
    }
}