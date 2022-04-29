package com.pb.criconet.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.models.Language;
import com.pb.criconet.models.PlayerModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayesrListAdapter extends RecyclerView.Adapter<PlayesrListAdapter.MyViewHolder> {
    Context context;
    static OnItemClickListener itemClickListener;
    ArrayList<PlayerModel> playerModelArrayList;
    ArrayList<PlayerModel> integers;
    ArrayList<String> teamName1List;
    ArrayList<String> teamName2List;
    String team1Name,team2Name;


    public PlayesrListAdapter(Context context,String team1Name,String team2Name, ArrayList<PlayerModel> playerModelArrayList, OnItemClickListener itemClickListener) {
        this.context = context;
        this.playerModelArrayList = playerModelArrayList;
        this.itemClickListener = itemClickListener;
        this.team1Name = team1Name;
        this.team2Name = team2Name;
        integers = new ArrayList<>();
        teamName1List = new ArrayList<>();
        teamName2List = new ArrayList<>();
    }

    @Override
    public PlayesrListAdapter.@NotNull MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.players_child, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(PlayesrListAdapter.MyViewHolder holder, final int position) {
        // set the data in items
       PlayerModel playerModel = playerModelArrayList.get(position);

        holder.tv_playerName.setText(playerModel.getName());
        holder.team_name.setText(playerModel.getTeamName());
        holder.tv_country.setText(playerModel.getCountry());

        if (playerModel.getBattingStyle().equalsIgnoreCase("")) {
            holder.li_battingStyle.setVisibility(View.GONE);
        } else {
            holder.li_battingStyle.setVisibility(View.VISIBLE);
            holder.tv_battingStyle.setText(playerModel.getBattingStyle());
        }
        if (playerModel.getBowlingStyle().equalsIgnoreCase("")) {
            holder.li_ballingStyle.setVisibility(View.GONE);
        } else {
            holder.li_ballingStyle.setVisibility(View.VISIBLE);
            holder.tv_bollingStyle.setText(playerModel.getBowlingStyle());
        }
        if (playerModel.getRole().equalsIgnoreCase("")) {
            holder.li_role.setVisibility(View.GONE);
        } else {
            holder.li_role.setVisibility(View.VISIBLE);
            holder.tv_role.setText(playerModel.getRole());
        }


        Glide.with(context).load(playerModel.getPlayerImg()).dontAnimate().placeholder(context.getResources().getDrawable(R.drawable.ipllogo))
                .into(holder.im_profile);



        holder.cv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (holder.cv.isChecked()) {
                    integers.add(playerModel);
                    if(team1Name.equalsIgnoreCase(playerModel.getTeamName())){
                        teamName1List.add(playerModel.getTeamName());
                    }else if(team2Name.equalsIgnoreCase(playerModel.getTeamName())){
                        teamName2List.add(playerModel.getTeamName());
                    }else{

                    }
                    holder.li.setBackgroundColor(context.getResources().getColor(R.color.select_player_color));
                    playerModel.setSelected(true);
                } else {
                    integers.remove(playerModel);
                     if(team1Name.equalsIgnoreCase(playerModel.getTeamName())){
                        teamName1List.remove(playerModel.getTeamName());
                    }else if(team2Name.equalsIgnoreCase(playerModel.getTeamName())){
                        teamName2List.remove(playerModel.getTeamName());
                    }else{

                    }
                    holder.li.setBackgroundColor(context.getResources().getColor(R.color.white_smoke));
                    playerModel.setSelected(false);
                }
                itemClickListener.onItemClick(integers,teamName1List,teamName2List);
            }
        });

        holder.cv.setChecked(playerModel.isSelected());

        holder.li.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cv.isChecked()) {
                    holder.li.setBackgroundColor(context.getResources().getColor(R.color.white_smoke));
                    holder.cv.setChecked(false);
                } else {
                    holder.li.setBackgroundColor(context.getResources().getColor(R.color.select_player_color));
                    holder.cv.setChecked(true);
                }
                itemClickListener.onItemClick(integers,teamName1List,teamName2List);
            }

        });


    }
    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        holder.cv.setOnCheckedChangeListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return playerModelArrayList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout li;
        LinearLayout li_role, li_battingStyle, li_ballingStyle;
        TextView tv_battingStyle, tv_bollingStyle, tv_role,
                tv_playerName, team_name, tv_country;
        CircleImageView im_profile;
        CheckBox cv;
        Context mContext;

        MyViewHolder(View itemView) {
            super(itemView);
            //this.setIsRecyclable(true);
            cv = itemView.findViewById(R.id.cv);
            li = itemView.findViewById(R.id.rl);
            im_profile = itemView.findViewById(R.id.im_profile);
            li_role = itemView.findViewById(R.id.li_role);
            li_battingStyle = itemView.findViewById(R.id.li_battingStyle);
            li_ballingStyle = itemView.findViewById(R.id.li_ballingStyle);
            tv_battingStyle = itemView.findViewById(R.id.tv_battingStyle);
            tv_bollingStyle = itemView.findViewById(R.id.tv_bollingStyle);
            tv_role = itemView.findViewById(R.id.tv_role);
            tv_playerName = itemView.findViewById(R.id.tv_playerName);
            team_name = itemView.findViewById(R.id.team_name);
            tv_country = itemView.findViewById(R.id.tv_country);

        }

    }

    public interface OnItemClickListener {
        void onItemClick(ArrayList<PlayerModel> integers,ArrayList<String> teamName1,ArrayList<String> teamName2);
    }
}