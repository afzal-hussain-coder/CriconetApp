package com.pb.criconet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.models.RewardsModell;

import java.util.ArrayList;

public class AmbassdorRewardsAdapter extends RecyclerView.Adapter<AmbassdorRewardsAdapter.MyViewHolder> {
    private Context context;
    ArrayList<RewardsModell> rewardsModells=null;

    public AmbassdorRewardsAdapter(Context context, ArrayList<RewardsModell> rewardsModells) {
        this.context = context;
        this.rewardsModells = rewardsModells;

    }

    @Override
    public AmbassdorRewardsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ambassdor_rewards_item, parent, false);
        AmbassdorRewardsAdapter.MyViewHolder vh = new AmbassdorRewardsAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AmbassdorRewardsAdapter.MyViewHolder holder, final int position) {
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