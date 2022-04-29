package com.pb.criconet.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.pb.criconet.R;
import com.pb.criconet.activity.Play_Live_Stream_Single;
import com.pb.criconet.activity.YoutubePlayerActivity;
import com.pb.criconet.fragments.LiveMatches;
import com.pb.criconet.models.LiveStreamingModel;
import com.pb.criconet.models.VideoModel;

import java.util.ArrayList;

public class LiveMatchAdapter extends RecyclerView.Adapter<LiveMatchAdapter.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<LiveStreamingModel> arrayList_image;
    public LiveMatchAdapter(ArrayList<LiveStreamingModel> chatname_list1, Context context) {
        this.context = context;
        this.arrayList_image=chatname_list1;
    }

    @Override
    public LiveMatchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stream_adapter, parent, false);
        LiveMatchAdapter.MyViewHolder vh = new LiveMatchAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(LiveMatchAdapter.MyViewHolder holder, final int position) {
        // set the data in items

        holder.ground_name.setText(arrayList_image.get(position).getTitle());
//            holder.team_name.setText(arrayList_image.get(position).getTeam_name());
        holder.location.setText(arrayList_image.get(position).getDesc());

        Glide.with(context).load(arrayList_image.get(position).getCover()).dontAnimate()
                .into(holder.play);

        if(arrayList_image.get(position).getIs_match_start().equalsIgnoreCase("0")){
            holder.tv_not_match.setVisibility(View.VISIBLE);
            holder.tv_not_match.setText(arrayList_image.get(position).getIs_match_start_lebel());
            holder.itemView.setClickable(false);
        }else{
            holder.itemView.setClickable(true);
            holder.tv_not_match.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, Play_Stream.class);
//                    Intent intent = new Intent(context, Play_Live_Stream.class);
                    Intent intent = new Intent(context, Play_Live_Stream_Single.class);
                    intent.putExtra("data", LiveStreamingModel.toJson(arrayList_image.get(position)).toString());
                    context.startActivity(intent);
                }
            });
        }

//            try {
//                Glide.with(context).load(arrayList_image.get(position).get())
//                        .apply(new RequestOptions().placeholder(R.drawable.app_icon).dontAnimate())
//                        .into(holder.roundedImageView);
//            } catch (Exception e) {
//                holder.roundedImageView.setImageResource(R.drawable.app_icon);
//            }


//        Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
//        holder.relative_dash.startAnimation(scaleUp);
    }

    @Override
    public int getItemCount() {
        return arrayList_image.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView roundedImageView, play;
        TextView ground_name, team_name, location,tv_not_match;
        Button play_icon;
        MaterialCardView relative_dash;

        MyViewHolder(View itemView) {
            super(itemView);
            relative_dash = itemView.findViewById(R.id.relative_dash);
            roundedImageView =  itemView.findViewById(R.id.roundedImageView);
            ground_name = itemView.findViewById(R.id.ground_name);
//                holder.team_name = (TextView) convertView.findViewById(R.id.team_name);
            location =  itemView.findViewById(R.id.location);
            play =  itemView.findViewById(R.id.logo_imageview);
            tv_not_match = itemView.findViewById(R.id.tv_not_match);
//                holder.play_icon = (Button) convertView.findViewById(R.id.play_icon);

        }
    }
}