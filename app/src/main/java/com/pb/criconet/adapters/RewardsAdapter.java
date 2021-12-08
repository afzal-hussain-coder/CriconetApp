package com.pb.criconet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.models.RewardsModell;
import com.pb.criconet.models.TimeSlot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.MyViewHolder> {
    private Context context;
    ArrayList<RewardsModell> rewardsModells=null;

    public RewardsAdapter(Context context,ArrayList<RewardsModell> rewardsModells) {
        this.context = context;
        this.rewardsModells = rewardsModells;

    }

    @Override
    public RewardsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_item, parent, false);
        RewardsAdapter.MyViewHolder vh = new RewardsAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RewardsAdapter.MyViewHolder holder, final int position) {
       RewardsModell  rewardsModell= rewardsModells.get(position);
        holder.tv_referral_count.setText(rewardsModell.getPoints());
        holder.tv_rewards.setText(rewardsModell.getText_1());
        Glide.with(context).load(rewardsModell.getImage()).into(holder.img_rewards_banner);

    }


    @Override
    public int getItemCount() {
        return rewardsModells.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_referral_count;
        TextView tv_rewards;
        ImageView img_rewards_banner;

        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            tv_referral_count = itemView.findViewById(R.id.tv_referral_count);
            tv_rewards = itemView.findViewById(R.id.tv_rewards);
            img_rewards_banner= itemView.findViewById(R.id.img_rewards_banner);

        }
    }

}