package com.pb.criconet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.models.TimeSlot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TimeAdaptercoach extends RecyclerView.Adapter<TimeAdaptercoach.MyViewHolder> {
    private Context context;
    private List<TimeSlot.Datum> data;
    private timeSelectt callback;
    ArrayList<String> arrayListUser;
    ArrayList<String>updatedSlotList;

    public TimeAdaptercoach(Context context, List<TimeSlot.Datum> data, timeSelectt callback) {
        this.context = context;
        this.data = data;
        this.callback = callback;
        arrayListUser=new ArrayList<>();
    }

    @Override
    public TimeAdaptercoach.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_time_coach, parent, false);
        TimeAdaptercoach.MyViewHolder vh = new TimeAdaptercoach.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TimeAdaptercoach.MyViewHolder holder, final int position) {
        // set the data in items
        holder.textView.setText(data.get(position).getSlotValue());

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