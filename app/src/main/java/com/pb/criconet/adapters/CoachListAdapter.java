package com.pb.criconet.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.activity.CoachDetailsActivity;
import com.pb.criconet.R;
import com.pb.criconet.activity.CoachProfileActivity;
import com.pb.criconet.models.CoachList;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CoachListAdapter extends RecyclerView.Adapter<CoachListAdapter.MyViewHolder> {

    private Context context;
    private List<CoachList.Datum> mdata;
    private String totalExperience;
    private SharedPreferences prefs;
    public CoachListAdapter(List<CoachList.Datum> data,Context context) {
        this.context = context;
        this.mdata=data;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public CoachListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_coch_list, parent, false);
        CoachListAdapter.MyViewHolder vh = new CoachListAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CoachListAdapter.MyViewHolder holder, final int position) {
        // set the data in items
        if (!mdata.get(position).getAvatar().isEmpty()) {
            Glide.with(context).load(mdata.get(position).getAvatar())
                    .into(holder.image);
        } else {
            Glide.with(context).load(R.drawable.placeholder_user)
                    .into(holder.image);
        }
        if (!mdata.get(position).getCover().isEmpty()) {
            Glide.with(context).load(mdata.get(position).getCover())
                    .into(holder.img_banner);
        } else {
            Glide.with(context).load(R.drawable.bg_coach)
                    .into(holder.img_banner);
        }


        if(mdata.get(position).getExps().equals("0")){
            holder.tvCoacExp.setVisibility(View.GONE);
        }else{
            holder.tvCoacExp.setVisibility(View.VISIBLE);
            //totalExperience=mdata.get(position).getExp_years()+"."+ mdata.get(position).getExp_months()+" "+context.getResources().getString(R.string.yearse_xperience);
            holder.tvCoacExp.setText(mdata.get(position).getExps());
        }

        if (mdata.get(position).getRating()==0){
         holder.rating_bar_review.setVisibility(View.GONE);
        }else{
            holder.rating_bar_review.setVisibility(View.VISIBLE);
            holder.rating_bar_review.setRating(Float.parseFloat(String.valueOf(mdata.get(position).getRating())));
        }
        holder.tvCoachName.setText(Global.capitizeString(mdata.get(position).getName()));
        holder.tvCoacTitle.setText(mdata.get(position).getCat_title());
        holder.tvDiscription.setText(mdata.get(position).getAboutCoachProfile());
        holder.tvPrice.setText("Price: "+"\u20B9" +mdata.get(position).getChargeAmount()+"/"+"Session");
        if(mdata.get(position).getPrice().getOfferPercentage().equalsIgnoreCase("0")){
            holder.tvOfferP.setVisibility(View.GONE);
        }else{
            holder.tvOfferP.setVisibility(View.VISIBLE);
            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvOfferP.setText("Offer Price: "+"\u20B9" +mdata.get(position).getPrice().getPaymentPrice() +"/"+"Session");
        }

        if(mdata.get(position).getADAYS().equalsIgnoreCase("0")){
            holder.tv_booked_session.setText(mdata.get(position).getADAYS_msg());
            holder.rl_session_available_or_not.setVisibility(View.VISIBLE);
            holder.rl_offer.setVisibility(View.GONE);
        }else{
            holder.rl_session_available_or_not.setVisibility(View.GONE);
            if(mdata.get(position).getPrice().getOfferId().equalsIgnoreCase("0")){
                holder.rl_offer.setVisibility(View.GONE);
            }else{
                if(mdata.get(position).getPrice().getOfferPercentage().equalsIgnoreCase("0")){
                    //holder.tvoffer.setText(mdata.get(position).getPrice().getOfferPercentage());
                    //holder.tvOfferl.setVisibility(View.GONE);
                }else{
                    //holder.tvOfferl.setVisibility(View.VISIBLE);
                    holder.tvoffer.setText(mdata.get(position).getPrice().getOfferPercentage() +"% "+"OFF");
                    holder.rl_offer.setVisibility(View.VISIBLE);
                }


            }
        }

        holder.btnDetails.setOnClickListener(view -> {

            CoachList.Price price = mdata.get(position).getPrice();
            Intent intent =new Intent(context, CoachProfileActivity.class);
            intent.putExtra("coachId",mdata.get(position).getUserId());
            intent.putExtra("image",mdata.get(position).getAvatar());
            intent.putExtra("Cover",mdata.get(position).getCover());
            intent.putExtra("name",mdata.get(position).getName());
            intent.putExtra("coachTitle",mdata.get(position).getProfileTitle());
            intent.putExtra("Language",mdata.get(position).getLang());
            Bundle args = new Bundle();
             try{
                 if(mdata.get(position).getCertificate().isEmpty()){

                 }else{

                     args.putSerializable("ARRAYLIST",(Serializable)mdata.get(position).getCertificate());
                     intent.putExtra("Certificate",args);
                 }
             }catch(Exception e){
                 e.printStackTrace();
             }



            intent.putExtra("Offer",mdata.get(position).getPrice().getOfferPercentage());
            intent.putExtra("OfferID",mdata.get(position).getPrice().getOfferId());
            intent.putExtra("OfferPrice",mdata.get(position).getPrice().getPaymentPrice());
            intent.putExtra("specialization",mdata.get(position).getCat_title());
            intent.putExtra("ADAYS",mdata.get(position).getADAYS());
            intent.putExtra("ADAYS_msg",mdata.get(position).getADAYS_msg());


            if(mdata.get(position).getAboutCoachProfile()==null){
                intent.putExtra("des","");
            }else{
                intent.putExtra("des",mdata.get(position).getAboutCoachProfile());
            }

            if(mdata.get(position).getAchievement()==null){
                intent.putExtra("achievementDetails","");
            }else{
                intent.putExtra("achievementDetails",mdata.get(position).getAchievement());
            }

            intent.putExtra("price",mdata.get(position).getChargeAmount());
            intent.putExtra("exp",mdata.get(position).getExps());
            intent.putExtra("rating",mdata.get(position).getRating());
            context.startActivity(intent);

        });

        holder.btnDetailss.setOnClickListener(view -> {

            CoachList.Price price = mdata.get(position).getPrice();
            Intent intent =new Intent(context, CoachProfileActivity.class);
            intent.putExtra("coachId",mdata.get(position).getUserId());
            intent.putExtra("image",mdata.get(position).getAvatar());
            intent.putExtra("Cover",mdata.get(position).getCover());
            intent.putExtra("name",mdata.get(position).getName());
            intent.putExtra("coachTitle",mdata.get(position).getProfileTitle());
            intent.putExtra("Language",mdata.get(position).getLang());
            Bundle args = new Bundle();



            try{
                if(mdata.get(position).getCertificate().isEmpty()){

                }else{

                    args.putSerializable("ARRAYLIST",(Serializable)mdata.get(position).getCertificate());
                    intent.putExtra("Certificate",args);
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            intent.putExtra("Offer",mdata.get(position).getPrice().getOfferPercentage());
            intent.putExtra("OfferPrice",mdata.get(position).getPrice().getPaymentPrice());
            intent.putExtra("OfferID",mdata.get(position).getPrice().getOfferId());
            intent.putExtra("specialization",mdata.get(position).getCat_title());
            intent.putExtra("ADAYS",mdata.get(position).getADAYS());
            intent.putExtra("ADAYS_msg",mdata.get(position).getADAYS_msg());

            if(mdata.get(position).getAboutCoachProfile()==null){
                intent.putExtra("des","");
            }else{
                intent.putExtra("des",mdata.get(position).getAboutCoachProfile());
            }

            if(mdata.get(position).getAchievement()==null){
                intent.putExtra("achievementDetails","");
            }else{
                intent.putExtra("achievementDetails",mdata.get(position).getAchievement());
            }

            intent.putExtra("price",mdata.get(position).getChargeAmount());
            intent.putExtra("exp",mdata.get(position).getExps());
            intent.putExtra("rating",mdata.get(position).getRating());
            context.startActivity(intent);

        });

        if(SessionManager.getProfileType(prefs).equalsIgnoreCase("coach")){
            holder.rl_user.setVisibility(View.GONE);
            holder.btnDetailss.setVisibility(View.VISIBLE);
            holder.btnBook.setVisibility(View.GONE);
        }else{
            holder.btnDetailss.setVisibility(View.GONE);
            holder.rl_user.setVisibility(View.VISIBLE);
            holder.btnBook.setVisibility(View.VISIBLE);
        }
        holder.btnBook.setOnClickListener(view -> {
            Intent intent =new Intent(context,CoachDetailsActivity.class);
            intent.putExtra("coachId",mdata.get(position).getUserId());
            intent.putExtra("rating",mdata.get(position).getRating());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout btnDetails,btnDetailss;
        TextView tvCoacTitle,tv_booked_session;
        TextView tvCoachName;
        TextView tvDiscription;
        TextView tvPrice;
        ImageView image;
        RatingBar rating_bar_review;
        TextView tvCoacExp;
        FrameLayout btnBook;
        RelativeLayout rl_session_available_or_not,rl_offer,rl_user;
        LinearLayout tvOfferl;
        TextView tvoffer,tvOfferP;
        RoundedImageView img_banner;


        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            rating_bar_review=itemView.findViewById(R.id.rating_bar_review);
            btnDetails=itemView.findViewById(R.id.btnDetails);
            tvCoacTitle=itemView.findViewById(R.id.tvCoacTitle);
            tvCoachName=itemView.findViewById(R.id.tvCoachName);
            tvDiscription=itemView.findViewById(R.id.tvDiscription);
            tvPrice=itemView.findViewById(R.id.tvPrice);


            image=itemView.findViewById(R.id.image);
            tvCoacExp=itemView.findViewById(R.id.tvCoacExp);
            btnBook= itemView.findViewById(R.id.btnBook);
            rl_session_available_or_not =itemView.findViewById(R.id.rl_session_available_or_not);
            rl_offer =itemView.findViewById(R.id.rl_offer);
            tvoffer =itemView.findViewById(R.id.tvoffer);
            img_banner = itemView.findViewById(R.id.img_banner);
            rl_user = itemView.findViewById(R.id.rl_user);
            btnDetailss=itemView.findViewById(R.id.btnDetailss);
            tvOfferP =itemView.findViewById(R.id.tvOfferP);
            tvOfferl =itemView.findViewById(R.id.tvOfferl);
            tv_booked_session =itemView.findViewById(R.id.tv_booked_session);
        }
    }
}