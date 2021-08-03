package com.pb.criconet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.models.TimeSlot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.MyViewHolder> {
    private Context context;
    private List<TimeSlot.Datum> data;
    private timeSelect callback;
    public int mSelectedItem = -1;

    public TimeAdapter(Context context, List<TimeSlot.Datum> data, timeSelect callback) {
        this.context = context;
        this.data = data;
        this.callback = callback;
    }

    @Override
    public TimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_time, parent, false);
        TimeAdapter.MyViewHolder vh = new TimeAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TimeAdapter.MyViewHolder holder, final int position) {
        // set the data in items
        holder.textView.setText(data.get(position).getSlotValue());

        holder.radio.setOnClickListener(view -> {
            holder.radio.setChecked(true);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    callback.getSlotId(data.get(position).getSlotId());
                    data.get(position).setActive(true);
                }
            }, 1000);

            mSelectedItem = holder.getAbsoluteAdapterPosition();
            notifyDataSetChanged();
        });


        if (mSelectedItem == position) {
            holder.radio.setChecked(true);
        } else {
            holder.radio.setChecked(false);
        }


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
        RadioButton radio;

        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            textView = itemView.findViewById(R.id.textView);
            radio = itemView.findViewById(R.id.radio);

        }
    }

    public interface timeSelect {
        public void getSlotId(String sloteId);
    }
}