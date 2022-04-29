package com.pb.criconet.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.activity.AcademyDetails;
import com.pb.criconet.activity.CoachDetailsActivity;
import com.pb.criconet.activity.visitCoachProfileActivity;
import com.pb.criconet.models.AcademyListModel;
import com.pb.criconet.models.CoachList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcademyListAdapter extends RecyclerView.Adapter<AcademyListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<AcademyListModel> academyListModels;
    private String totalExperience;
    private SharedPreferences prefs;
    itemclicklistner itemclicklistner;

    public AcademyListAdapter(ArrayList<AcademyListModel> academyListModels, Context context, itemclicklistner itemclicklistner) {
        this.context = context;
        this.academyListModels = academyListModels;
        this.itemclicklistner = itemclicklistner;
        try {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public AcademyListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_academy_list, parent, false);
        AcademyListAdapter.MyViewHolder vh = new AcademyListAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AcademyListAdapter.MyViewHolder holder, final int position) {
        // set the data in items
        AcademyListModel academyListModel = academyListModels.get(position);

        if (!academyListModel.getLogo().isEmpty()) {
            Glide.with(context).load(academyListModel.getLogo())
                    .into(holder.image);
        } else {
            Glide.with(context).load(R.drawable.placeholder_user)
                    .into(holder.image);
        }
        if (!academyListModel.getBanner_image().isEmpty()) {
            Glide.with(context).load(academyListModel.getBanner_image())
                    .into(holder.img_banner);
        } else {
            Glide.with(context).load(R.drawable.bg2)
                    .into(holder.img_banner);
        }


        /*if(mdata.get(position).getExps().equals("0")){
            holder.tvCoacExp.setVisibility(View.GONE);
        }else{
            holder.tvCoacExp.setVisibility(View.VISIBLE);
            //totalExperience=mdata.get(position).getExp_years()+"."+ mdata.get(position).getExp_months()+" "+context.getResources().getString(R.string.yearse_xperience);
            holder.tvCoacExp.setText(mdata.get(position).getExps());
        }*/

//        if((mdata.get(position).getExp_years().equalsIgnoreCase("") || mdata.get(position).getExp_years().equalsIgnoreCase("0")) && (mdata.get(position).getExp_months().equalsIgnoreCase("")||mdata.get(position).getExp_months().equalsIgnoreCase("0")) ){
//            holder.tvCoacExp.setVisibility(View.GONE);
//        }else{
//            holder.tvCoacExp.setVisibility(View.VISIBLE);
//        }

//        if(mdata.get(position).getExp_years().equalsIgnoreCase("1") && mdata.get(position).getExp_months().equalsIgnoreCase("1") ){
//            holder.tvCoacExp.setText(mdata.get(position).getExp_years()+"."+mdata.get(position).getExp_months()+" "+"Year Experience");
//        }else if(mdata.get(position).getExp_years().equalsIgnoreCase("") && mdata.get(position).getExp_months().equalsIgnoreCase("1")){
//            holder.tvCoacExp.setText(mdata.get(position).getExp_months()+" "+"Month Experience");
//        }else if(mdata.get(position).getExp_months().equalsIgnoreCase("")){
//            if(mdata.get(position).getExp_years().equalsIgnoreCase("1")){
//
//                holder.tvCoacExp.setText(mdata.get(position).getExp_years()+" "+"Year Experience");
//            }else{
//
//                holder.tvCoacExp.setText(mdata.get(position).getExp_years()+" "+"Years Experience");
//            }
//
//        }else if(mdata.get(position).getExp_years().equalsIgnoreCase("") && mdata.get(position).getExp_months().equalsIgnoreCase("0")){
//            holder.tvCoacExp.setText(mdata.get(position).getExp_months()+" "+"Month Experience");
//        }
//        else if(mdata.get(position).getExp_years().equalsIgnoreCase("") && Integer.parseInt(mdata.get(position).getExp_months())>1){
//            holder.tvCoacExp.setText(mdata.get(position).getExp_months()+" "+"Months Experience");
//        }
//
//        else if(mdata.get(position).getExp_years().equalsIgnoreCase("0")){
//            if(mdata.get(position).getExp_months().equalsIgnoreCase("1")){
//                holder.tvCoacExp.setText(mdata.get(position).getExp_months()+" "+"Month Experience");
//            }else{
//                holder.tvCoacExp.setText(mdata.get(position).getExp_months()+" "+"Months Experience");
//            }
//
//
//        }
//        else {
//            holder.tvCoacExp.setText(mdata.get(position).getExp_years()+"."+mdata.get(position).getExp_months()+" "+"Years Experience");
//        }


        if (academyListModel.getRating().isEmpty()) {
            holder.rating_bar_review.setVisibility(View.GONE);
        } else {
            holder.rating_bar_review.setVisibility(View.VISIBLE);
            holder.rating_bar_review.setRating(Float.parseFloat(academyListModel.getRating()));
        }
        holder.tvCoachName.setText(Global.capitizeString(academyListModel.getName()));
        holder.tvCoacExp.setText(academyListModel.getAddress());
        holder.tvCoacTitle.setText(academyListModel.getCat_title());
        holder.tvDiscription.setText(academyListModel.getShort_desc());
        holder.tvPrice.setText(context.getString(R.string.fees) + ": " + "\u20B9" + academyListModel.getFee() + "/" + context.getString(R.string.month));
//        if(mdata.get(position).getPrice().getOfferPercentage().equalsIgnoreCase("0")){
//            holder.tvOfferP.setVisibility(View.GONE);
//        }else{
//            holder.tvOfferP.setVisibility(View.VISIBLE);
//            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            holder.tvOfferP.setText(context.getString(R.string.offer_price)+"\u20B9" +mdata.get(position).getPrice().getPaymentPrice() +"/"+context.getString(R.string.session));
//        }

//        if(mdata.get(position).getADAYS().equalsIgnoreCase("")||mdata.get(position).getADAYS().equalsIgnoreCase("0")){
//            holder.tv_booked_session.setText(mdata.get(position).getADAYS_msg());
//            holder.rl_session_available_or_not.setVisibility(View.VISIBLE);
//            holder.rl_offer.setVisibility(View.GONE);
//        }else{
//            holder.rl_session_available_or_not.setVisibility(View.GONE);
//            if(mdata.get(position).getPrice().getOfferId().equalsIgnoreCase("0")){
//                holder.rl_offer.setVisibility(View.GONE);
//            }else{
//                if(mdata.get(position).getPrice().getOfferPercentage().equalsIgnoreCase("0")){
//                    //holder.tvoffer.setText(mdata.get(position).getPrice().getOfferPercentage());
//                    //holder.tvOfferl.setVisibility(View.GONE);
//                }else{
//                    //holder.tvOfferl.setVisibility(View.VISIBLE);
//                    holder.tvoffer.setText(mdata.get(position).getPrice().getOfferPercentage() +"% "+context.getResources().getString(R.string.OFF));
//                    holder.rl_offer.setVisibility(View.VISIBLE);
//                }
//
//
//            }
//        }

        holder.btnDetails.setOnClickListener(view -> {
            Intent intent = new Intent(context, AcademyDetails.class);
            intent.putExtra("Certificate", academyListModel);
            context.startActivity(intent);

        });

        holder.btnBook.setOnClickListener(view -> {
            itemclicklistner.book(academyListModel);
        });
    }

    @Override
    public int getItemCount() {
        return academyListModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout btnDetails, btnDetailss;
        TextView tvCoacTitle, tv_booked_session;
        TextView tvCoachName;
        TextView tvDiscription;
        TextView tvPrice;
        CircleImageView image;
        RatingBar rating_bar_review;
        TextView tvCoacExp;
        FrameLayout btnBook;
        RelativeLayout rl_session_available_or_not, rl_offer, rl_user;
        LinearLayout tvOfferl;
        TextView tvoffer, tvOfferP;
        RoundedImageView img_banner;


        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            rating_bar_review = itemView.findViewById(R.id.rating_bar_review);
            btnDetails = itemView.findViewById(R.id.btnDetails);
            tvCoacTitle = itemView.findViewById(R.id.tvCoacTitle);
            tvCoachName = itemView.findViewById(R.id.tvCoachName);
            tvDiscription = itemView.findViewById(R.id.tvDiscription);
            tvPrice = itemView.findViewById(R.id.tvPrice);


            image = itemView.findViewById(R.id.image);
            tvCoacExp = itemView.findViewById(R.id.tvCoacExp);
            btnBook = itemView.findViewById(R.id.btnBook);
            rl_session_available_or_not = itemView.findViewById(R.id.rl_session_available_or_not);
            rl_offer = itemView.findViewById(R.id.rl_offer);
            tvoffer = itemView.findViewById(R.id.tvoffer);
            img_banner = itemView.findViewById(R.id.img_banner);
            rl_user = itemView.findViewById(R.id.rl_user);
            btnDetailss = itemView.findViewById(R.id.btnDetailss);
            tvOfferP = itemView.findViewById(R.id.tvOfferP);
            tvOfferl = itemView.findViewById(R.id.tvOfferl);
            tv_booked_session = itemView.findViewById(R.id.tv_booked_session);
        }
    }

    public interface itemclicklistner {
        void book(AcademyListModel academyListModel);
    }
}