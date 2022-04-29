package com.pb.criconet.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.activity.Play_Live_Stream_Single;
import com.pb.criconet.activity.SelectTeamPlayerActivity;
import com.pb.criconet.models.LiveStreamingModel;
import com.pb.criconet.models.MatchList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpcomingMatchesAdapter extends RecyclerView.Adapter<UpcomingMatchesAdapter.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<MatchList> matchLists = null;
    String title = "";

    public UpcomingMatchesAdapter(Context context, ArrayList<MatchList> matchLists, String title) {
        this.context = context;
        this.matchLists = matchLists;
        this.title = title;

    }

    @Override
    public UpcomingMatchesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_matches_child, parent, false);
        UpcomingMatchesAdapter.MyViewHolder vh = new UpcomingMatchesAdapter.MyViewHolder(v);
        return vh;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(UpcomingMatchesAdapter.MyViewHolder holder, final int position) {
        // set the data in items
        MatchList matchList = matchLists.get(position);
        holder.header.setText(matchList.getName());
        holder.ground_name.setText(title.toUpperCase());
        holder.datetime.setText(Global.validateDateFormat(matchList.getDate_time()));
        ArrayList<MatchList.Teams> teams = matchList.getTeamsArrayList();
        holder.team1_name.setText(teams.get(0).getTeamName());
        Glide.with(context).load(teams.get(0).getLogo()).dontAnimate().placeholder(context.getResources().getDrawable(R.drawable.ipllogo))
                .into(holder.img1);
        holder.team2_name.setText(teams.get(1).getTeamName());
        Glide.with(context).load(teams.get(1).getLogo()).dontAnimate().placeholder(context.getResources().getDrawable(R.drawable.ipllogo))
                .into(holder.img2);


        long startTime = System.currentTimeMillis();
        if (holder.mCountDownTimer != null) {
            holder.mCountDownTimer.cancel();
        }

        holder.mCountDownTimer = new CountDownTimer(getTimer(matchList.getDate_time()), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                //startTime= startTime -1;
                Long serverUptimeSeconds = (millisUntilFinished - startTime) / 1000;

                String daysLeft = String.format("%d", serverUptimeSeconds / 86400);

                String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);

                String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);

                String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);

//                if(daysLeft.equalsIgnoreCase("0")){
//                    timerFinal1 =  hoursLeft + "h" + " : " + minutesLeft + "m" + " : " + secondsLeft + "s";
//                }else if(hoursLeft.equalsIgnoreCase("0")){
//                    timerFinal1 =  "0" + "0" + " : " + minutesLeft + "m" + " : " + secondsLeft + "s";
//                }else if(minutesLeft.equalsIgnoreCase("0")){
//                    timerFinal1 =  "0" + "0" + " : " + "0" + "0" + " : " + secondsLeft + "s";
//                }else{
//                    timerFinal1 = daysLeft + "d" + " : " + hoursLeft + "h" + " : " + minutesLeft + "m" + " : " + secondsLeft + "s";
//                }'



                if(Integer.parseInt(secondsLeft)<0){
                    holder.tv_matchStartTime.startAnimation(holder.anim);
                    holder.tv_matchStartTime.setText(context.getResources().getString(R.string.Match_Started));
                    holder.tv_matchStartTime.setTextColor(context.getResources().getColor(R.color.logWarn));
                }else{
                    holder.tv_matchStartTime.setTextColor(context.getResources().getColor(R.color.red));
                    holder.tv_matchStartTime.setAnimation(null);
                    String timerFinal1 = daysLeft + "d" + " : " + hoursLeft + "h" + " : " + minutesLeft + "m" + " : " + secondsLeft + "s";
                    holder.tv_matchStartTime.setText(timerFinal1);
                }



            }

            @Override
            public void onFinish() {
                holder.tv_matchStartTime.setText("00:00:00");
            }
        }.start();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SelectTeamPlayerActivity.class).putExtra("URL", matchList.getPlayer_api_pur()).
                        putExtra("Team1Logo", teams.get(0).getLogo())
                        .putExtra("Team2Logo", teams.get(1).getLogo())
                        .putExtra("MatchTime", matchList.getDate_time())
                        .putExtra("Team1Name", teams.get(0).getTeamName())
                        .putExtra("Team2Name", teams.get(1).getTeamName())
                        .putExtra("MatchId",matchList.getId())
                         .putExtra("League",title));

            }
        });


        Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
        holder.relative_dash.startAnimation(scaleUp);
    }



    @Override
    public int getItemCount() {
        return matchLists.size();
        //arrayList_image.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img1, img2;
        TextView ground_name, team1_name, team2_name, header, tv_matchStartTime, datetime,tv_matchStartTimeee;
        CountDownTimer mCountDownTimer=null;
        MaterialCardView relative_dash;
        Animation anim=null;

        MyViewHolder(View itemView) {
            super(itemView);
            relative_dash = itemView.findViewById(R.id.relative_dash);
            header = itemView.findViewById(R.id.header);
            ground_name = itemView.findViewById(R.id.ground_name);
            team1_name = itemView.findViewById(R.id.team1_name);
            team2_name = itemView.findViewById(R.id.team2_name);
            tv_matchStartTime = itemView.findViewById(R.id.tv_matchStartTime);
            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            datetime = itemView.findViewById(R.id.datetime);
            anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(600); //You can manage the blinking time with this parameter
            anim.setStartOffset(10);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);

        }
    }

    private long getTimer(String timer) {
        String timerFinal = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy, HH:mm");
        formatter.setLenient(false);
        long milliseconds = 0;

        final CountDownTimer mCountDownTimer;
        final long startTime;
        Date endDate;
        try {
            endDate = formatter.parse(timer);
            assert endDate != null;
            milliseconds = endDate.getTime();
            Log.d("mili", milliseconds + "");

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return milliseconds;
    }


}