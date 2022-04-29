package com.pb.criconet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.models.AcademyListModel;
import com.pb.criconet.models.TimeSlot;
import com.pb.criconet.models.updatedTimeSlot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeAdapterAcademy extends RecyclerView.Adapter<TimeAdapterAcademy.MyViewHolder> {
    private Context context;
    private timeSelectt callback;
    ArrayList<String> arrayListUser;
    ArrayList<String> arrayListUser_slot;
    ArrayList<AcademyListModel.AcademySlot> updatedSlotList;
    private int selectedPosition = -1;

    public TimeAdapterAcademy(Context context, ArrayList<AcademyListModel.AcademySlot> updatedSlotList, timeSelectt callback) {
        this.context = context;
        this.updatedSlotList = updatedSlotList;
        this.callback = callback;
        arrayListUser = new ArrayList<>();
        arrayListUser_slot = new ArrayList<>();
        //Toaster.customToast(updatedSlotList.size()+"");
    }

    @Override
    public TimeAdapterAcademy.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_time_academy, parent, false);
        TimeAdapterAcademy.MyViewHolder vh = new TimeAdapterAcademy.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TimeAdapterAcademy.MyViewHolder holder, final int position) {
        // set the data in items
        AcademyListModel.AcademySlot date = updatedSlotList.get(position);
        holder.textView.setText(date.getSlot_time());


        holder.radio.setOnClickListener(view -> {
            selectedPosition = holder.getAbsoluteAdapterPosition();
            boolean isChecked =  holder.radio.isChecked();
            String slotId="";
            String slotTime="";
            if(isChecked){
                slotId=updatedSlotList.get(position).getSlot_id();
                slotTime = updatedSlotList.get(position).getSlot_time();
                //arrayListUser.add(updatedSlotList.get(position).getSlot_id());
                //arrayListUser_slot.add(updatedSlotList.get(position).getSlot_time());
            }else{

                //arrayListUser.remove(updatedSlotList.get(position).getSlot_id());
                //arrayListUser_slot.remove(updatedSlotList.get(position).getSlot_time());
            }

            callback.getSlotId(slotId,slotTime);
            notifyDataSetChanged();
        });

        if (selectedPosition == position) {
            holder.radio.setChecked(true);
        } else {
            holder.radio.setChecked(false);

        }


//        for(int i =0;i<updatedSlotList.size();i++){
//                if(updatedSlotList.get(i).getSlot_id().equalsIgnoreCase(coachLanguage.getSlotId())){
//                    holder.radio.setChecked(true);
//                    arrayListUser.add(coachLanguage.getSlotId());
//                    callback.getSlotId(arrayListUser);
//                    break;
//                }
//        }


        //Multiple selection
//        holder.radio.setOnClickListener(view -> {
//            final boolean isChecked = holder.radio.isChecked();
//
//            if (isChecked) {
//                if (!arrayListUser.contains(updatedSlotList.get(position).getSlot_time()))
//                    arrayListUser.add(updatedSlotList.get(position).getSlot_id());
//                    arrayListUser_slot.add(updatedSlotList.get(position).getSlot_time());
//            } else {
//                arrayListUser.remove(updatedSlotList.get(position).getSlot_id());
//                arrayListUser_slot.remove(updatedSlotList.get(position).getSlot_time());
//            }
//            callback.getSlotId(arrayListUser,arrayListUser_slot);
//        });


    }

    private String convertTime(String time) {
        Date dt = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
        return sdf.format(dt);

    }

    @Override
    public int getItemCount() {
        return updatedSlotList.size();
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
        public void getSlotId(String slotId, String slot_time);
    }
}