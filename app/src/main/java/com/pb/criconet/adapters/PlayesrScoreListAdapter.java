package com.pb.criconet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.models.LiveStreamingModel;
import com.pb.criconet.models.MyContestListModel;

import org.json.JSONException;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayesrScoreListAdapter extends RecyclerView.Adapter<PlayesrScoreListAdapter.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<LiveStreamingModel> arrayList_image;
    OnItemClickListener itemClickListener;
    int count=0;
    ArrayList<Integer> integers;
    String leaugeName="";
    private ArrayList<MyContestListModel> myContestListModelslist;
    public PlayesrScoreListAdapter(Context context, ArrayList<MyContestListModel> myContestListModelslist,String leaugeName) {
        this.context = context;
        this.myContestListModelslist = myContestListModelslist;
        this.leaugeName=leaugeName;
    }

    @Override
    public PlayesrScoreListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_card_parent, parent, false);
        PlayesrScoreListAdapter.MyViewHolder vh = new PlayesrScoreListAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PlayesrScoreListAdapter.MyViewHolder holder, final int position) {
        // set the data in items
        try {
            MyContestListModel myContestListModel= myContestListModelslist.get(position);
            holder.header.setText(myContestListModel.getMyContestJsonObject().getString("name"));
            holder.team1_name.setText(myContestListModel.getMyContestJsonObject().getJSONArray("teams").getJSONObject(0).getString("name"));
            holder.team2_name.setText(myContestListModel.getMyContestJsonObject().getJSONArray("teams").getJSONObject(1).getString("name"));
            Glide.with(context).load(myContestListModel.getMyContestJsonObject().getJSONArray("teams").getJSONObject(0).getString("logo")).dontAnimate().placeholder(context.getResources().getDrawable(R.drawable.ipllogo))
                    .into(holder.img1);
            Glide.with(context).load(myContestListModel.getMyContestJsonObject().getJSONArray("teams").getJSONObject(1).getString("logo")).dontAnimate().placeholder(context.getResources().getDrawable(R.drawable.ipllogo))
                    .into(holder.img2);
            holder.tv_matchDate.setText(Global.validateDateFormatt(myContestListModel.getMyContestJsonObject().getString("date")));
            holder.tv_status.setText(myContestListModel.getMyContestJsonObject().getString("status"));
            holder.tv_totalPoints.setText(myContestListModel.getTotal_points());
            holder.ground_name.setText(leaugeName);

            if(myContestListModel.getFantasyPlayerPointJsons()==null){
                //holder.playesrScoreListAdapterr = new PlayesrScoreListAdapterr(context, new ArrayList<>());
                //holder.rv_player_scoree.setAdapter(holder.playesrScoreListAdapterr);
            }else{
                holder.playesrScoreListAdapterr = new PlayesrScoreListAdapterr(context, myContestListModel.getFantasyPlayerPointJsons());
                holder.rv_player_scoree.setAdapter(holder.playesrScoreListAdapterr);
            }

            holder.btn_viewDetails.setOnClickListener(v -> {
                holder.rv_player_scoree.setVisibility(View.VISIBLE);
                holder.btn_viewDetails.setVisibility(View.GONE);
                holder.btn_hide.setVisibility(View.VISIBLE);
            });


                holder.btn_hide.setOnClickListener(v -> {
                    holder.rv_player_scoree.setVisibility(View.GONE);
                    holder.btn_viewDetails.setVisibility(View.VISIBLE);
                    holder.btn_hide.setVisibility(View.GONE);
                });



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return myContestListModelslist.size();
                //arrayList_image.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
      TextView ground_name,header,tv_matchDate,team1_name,team2_name,tv_totalPoints,tv_status;
      CircleImageView img1,img2;
      Button btn_viewDetails,btn_hide;
      RecyclerView rv_player_scoree;
        PlayesrScoreListAdapterr playesrScoreListAdapterr;

        MyViewHolder(View itemView) {
            super(itemView);
            ground_name = itemView.findViewById(R.id.ground_name);
            header = itemView.findViewById(R.id.header);
            tv_matchDate = itemView.findViewById(R.id.tv_matchDate);
            team1_name = itemView.findViewById(R.id.team1_name);
            team2_name = itemView.findViewById(R.id.team2_name);
            tv_totalPoints= itemView.findViewById(R.id.tv_totalPoints);
            img1=  itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            btn_viewDetails = itemView.findViewById(R.id.btn_viewDetails);
            tv_status = itemView.findViewById(R.id.tv_status);
            rv_player_scoree = itemView.findViewById(R.id.rv_player_scoree);
            rv_player_scoree.setLayoutManager(new LinearLayoutManager(context));
            btn_hide = itemView.findViewById(R.id.btn_hide);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(ArrayList<Integer>integers);
    }


    public class PlayesrScoreListAdapterr extends RecyclerView.Adapter<PlayesrScoreListAdapterr.MyViewHolderr> {
        Context context;
        ArrayList<MyContestListModel.FantasyPlayerPointJson> fantasyPlayerPointJsons;

        public PlayesrScoreListAdapterr(Context context, ArrayList<MyContestListModel.FantasyPlayerPointJson> fantasyPlayerPointJsons) {
            this.context = context;
            this.fantasyPlayerPointJsons = fantasyPlayerPointJsons;
        }

        @Override
        public PlayesrScoreListAdapterr.MyViewHolderr onCreateViewHolder(ViewGroup parent, int viewType) {
            // inflate the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_child, parent, false);
            PlayesrScoreListAdapterr.MyViewHolderr vh = new PlayesrScoreListAdapterr.MyViewHolderr(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(PlayesrScoreListAdapterr.MyViewHolderr holder, final int position) {
            // set the data in items

            try{
                MyContestListModel.FantasyPlayerPointJson fantasyPlayerPointJsonModel = fantasyPlayerPointJsons.get(position);
                holder.tv_playerName.setText(fantasyPlayerPointJsonModel.getPlayerName());
                holder.tv_point.setText(fantasyPlayerPointJsonModel.getPlayerPoint());
            }catch (Exception e){
                e.printStackTrace();
            }

            // holder.tv_team_name.setText(fantasyPlayerPointJsonModel.get.getJSONObject(1).getString("name"));
//                Glide.with(context).load(myContestListModel.getMyContestJsonObject().getJSONArray("teams").getJSONObject(0).getString("logo")).dontAnimate().placeholder(context.getResources().getDrawable(R.drawable.ipllogo))
//                        .into(holder.img1);


        }

        @Override
        public int getItemCount() {
            return myContestListModelslist.size();
            //arrayList_image.size();
        }

        class MyViewHolderr extends RecyclerView.ViewHolder {
            TextView tv_playerName, tv_team_name, tv_point;
            CircleImageView tv_photo;


            MyViewHolderr(View itemView) {
                super(itemView);
                tv_playerName = itemView.findViewById(R.id.tv_playerName);
                tv_team_name = itemView.findViewById(R.id.tv_team_name);
                tv_point = itemView.findViewById(R.id.tv_point);
                tv_photo = itemView.findViewById(R.id.tv_photo);

            }
        }
    }
    }