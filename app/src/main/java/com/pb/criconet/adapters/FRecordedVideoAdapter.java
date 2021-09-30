package com.pb.criconet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.activity.PlayRecordedVideoActivity;
import com.pb.criconet.activity.Play_Live_Stream_Single;
import com.pb.criconet.models.LiveStreamingModel;
import com.pb.criconet.models.RecodedVideo;

import java.util.ArrayList;

public class FRecordedVideoAdapter extends RecyclerView.Adapter<FRecordedVideoAdapter.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<RecodedVideo> arrayList_image;
    public FRecordedVideoAdapter(ArrayList<RecodedVideo> chatname_list1, Context context) {
        this.context = context;
        this.arrayList_image=chatname_list1;
    }

    @Override
    public FRecordedVideoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recorded_video_child, parent, false);
        FRecordedVideoAdapter.MyViewHolder vh = new FRecordedVideoAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FRecordedVideoAdapter.MyViewHolder holder, final int position) {


        Glide.with(context).load(arrayList_image.get(position).getPostVideo()).dontAnimate()
                .into(holder.logo_imageview);
        holder.tv_recoded_at.setText("Video recorded at "+arrayList_image.get(position).getCreated());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayRecordedVideoActivity.class);
                intent.putExtra("data", arrayList_image.get(position).getPostVideo());
                context.startActivity(intent);
            }
        });


        Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
        holder.relative_dash.startAnimation(scaleUp);
    }

    @Override
    public int getItemCount() {
        return arrayList_image.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView logo_imageview;
        TextView tv_recoded_at;
        MaterialCardView relative_dash;

        MyViewHolder(View itemView) {
            super(itemView);
            relative_dash = itemView.findViewById(R.id.relative_dash);
            logo_imageview =  itemView.findViewById(R.id.logo_imageview);
            tv_recoded_at = itemView.findViewById(R.id.tv_recoded_at);


        }
    }
}