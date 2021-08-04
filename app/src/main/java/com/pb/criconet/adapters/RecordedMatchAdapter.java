package com.pb.criconet.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.pb.criconet.R;
import com.pb.criconet.activity.CoachDetailsActivity;
import com.pb.criconet.activity.YoutubePlayerActivity;
import com.pb.criconet.fragments.RecMatches;
import com.pb.criconet.models.CoachList;
import com.pb.criconet.models.VideoModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class RecordedMatchAdapter extends RecyclerView.Adapter<RecordedMatchAdapter.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<VideoModel> arrayList_image;
    public RecordedMatchAdapter(ArrayList<VideoModel> arrayList_image, Context context) {
        this.context = context;
        this.arrayList_image=arrayList_image;
    }

    @Override
    public RecordedMatchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.matches_adapter_item, parent, false);
        RecordedMatchAdapter.MyViewHolder vh = new RecordedMatchAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecordedMatchAdapter.MyViewHolder holder, final int position) {
        // set the data in items

        holder.ground_name.setText(arrayList_image.get(position).getGround_name());
        holder.team_name.setText(arrayList_image.get(position).getTeam_name());
        holder.location.setText(arrayList_image.get(position).getLocation());
        holder.date_time.setText(arrayList_image.get(position).getDate_time());
        holder.play_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, YoutubePlayerActivity.class);
                intent.putExtra("url", arrayList_image.get(position).getVideo_link());
                context.startActivity(intent);
            }
        });

        try {
            Glide.with(context).load(arrayList_image.get(position).getVideo_details().getThumbnail_url())
                    .apply(new RequestOptions().placeholder(R.drawable.app_icon).dontAnimate())
                    .into(holder.roundedImageView);
        } catch (Exception e) {
            holder.roundedImageView.setImageResource(R.drawable.app_icon);
        }
        Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
        holder.relative_dash.startAnimation(scaleUp);
    }

    @Override
    public int getItemCount() {
        return arrayList_image.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView roundedImageView;
        TextView ground_name, team_name, location, date_time;
        ImageButton play_icon;
        MaterialCardView relative_dash;
        YouTubeThumbnailView ImageView;

        MyViewHolder(View itemView) {
            super(itemView);
            relative_dash =  itemView.findViewById(R.id.relative_dash);
            roundedImageView = itemView.findViewById(R.id.roundedImageView);
//          holder.ImageView = (YouTubeThumbnailView) convertView.findViewById(R.id.ImageView);
            ground_name =  itemView.findViewById(R.id.ground_name);
            team_name =  itemView.findViewById(R.id.team_name);
            location =  itemView.findViewById(R.id.location);
            date_time =  itemView.findViewById(R.id.date_time);
            play_icon =  itemView.findViewById(R.id.play_icon);
        }
    }

    public String convertUTCDateToLocalDate(String date_string) {
        if (date_string.isEmpty()) {
            return "";
        }

        SimpleDateFormat oldFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //oldFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        String dueDateAsNormal = "";
        try {
            value = oldFormatter.parse(date_string);
            SimpleDateFormat newFormatter = new SimpleDateFormat("dd MMM yyyy (hh:mm a)");

           /* Date date = new SimpleDateFormat("yyyy-M-d").parse(date_string);
            String dayOfWeek = new SimpleDateFormat("EEE", Locale.ENGLISH).format(newFormatter);

            Log.d("day",dayOfWeek);*/
            newFormatter.setTimeZone(TimeZone.getDefault());
            dueDateAsNormal = newFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("date", dueDateAsNormal);

        return dueDateAsNormal;
    }
}