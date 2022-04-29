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
import com.pb.criconet.activity.CoachDetailsActivity;
import com.pb.criconet.activity.visitAcademyCoachProfileActivity;
import com.pb.criconet.activity.visitCoachProfileActivity;
import com.pb.criconet.models.AcademyListModel;
import com.pb.criconet.models.CoachList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AcademyCoachListAdapter extends RecyclerView.Adapter<AcademyCoachListAdapter.MyViewHolder> {

    private Context context;
    ArrayList<AcademyListModel.AcademyCoaches> mdata;
    private String totalExperience;
    private SharedPreferences prefs;
    public AcademyCoachListAdapter(ArrayList<AcademyListModel.AcademyCoaches> academyCoachesArrayList, Context context) {
        this.context = context;
        this.mdata=academyCoachesArrayList;
        try{
            prefs =  PreferenceManager.getDefaultSharedPreferences(context);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public AcademyCoachListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_academycoch_list, parent, false);
        AcademyCoachListAdapter.MyViewHolder vh = new AcademyCoachListAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AcademyCoachListAdapter.MyViewHolder holder, final int position) {
        // set the data in items
        if (!mdata.get(position).getAvatar().isEmpty()) {
            Glide.with(context).load(mdata.get(position).getAvatar())
                    .into(holder.image);
        } else {
            Glide.with(context).load(R.drawable.placeholder_user)
                    .into(holder.image);
        }
//        if (!mdata.get(position).getCover().isEmpty()) {
//            Glide.with(context).load(mdata.get(position).getCover())
//                    .into(holder.img_banner);
//        } else {
//            Glide.with(context).load(R.drawable.bg_coach)
//                    .into(holder.img_banner);
//        }


        /*if(mdata.get(position).getExps().equals("0")){
            holder.tvCoacExp.setVisibility(View.GONE);
        }else{
            holder.tvCoacExp.setVisibility(View.VISIBLE);
            //totalExperience=mdata.get(position).getExp_years()+"."+ mdata.get(position).getExp_months()+" "+context.getResources().getString(R.string.yearse_xperience);
            holder.tvCoacExp.setText(mdata.get(position).getExps());
        }*/

        if((mdata.get(position).getExp_years().equalsIgnoreCase("") || mdata.get(position).getExp_years().equalsIgnoreCase("0")) && (mdata.get(position).getExp_months().equalsIgnoreCase("")||mdata.get(position).getExp_months().equalsIgnoreCase("0")) ){
            holder.tvCoacExp.setVisibility(View.GONE);
        }else{
            holder.tvCoacExp.setVisibility(View.VISIBLE);
        }

        if(mdata.get(position).getExp_years().equalsIgnoreCase("1") && mdata.get(position).getExp_months().equalsIgnoreCase("1") ){
            holder.tvCoacExp.setText(mdata.get(position).getExp_years()+"."+mdata.get(position).getExp_months()+" "+"Year Experience");
        }else if(mdata.get(position).getExp_years().equalsIgnoreCase("") && mdata.get(position).getExp_months().equalsIgnoreCase("1")){
            holder.tvCoacExp.setText(mdata.get(position).getExp_months()+" "+"Month Experience");
        }else if(mdata.get(position).getExp_months().equalsIgnoreCase("")){
            if(mdata.get(position).getExp_years().equalsIgnoreCase("1")){

                holder.tvCoacExp.setText(mdata.get(position).getExp_years()+" "+"Year Experience");
            }else{

                holder.tvCoacExp.setText(mdata.get(position).getExp_years()+" "+"Years Experience");
            }

        }else if(mdata.get(position).getExp_years().equalsIgnoreCase("") && mdata.get(position).getExp_months().equalsIgnoreCase("0")){
            holder.tvCoacExp.setText(mdata.get(position).getExp_months()+" "+"Month Experience");
        }
        else if(mdata.get(position).getExp_years().equalsIgnoreCase("") && Integer.parseInt(mdata.get(position).getExp_months())>1){
            holder.tvCoacExp.setText(mdata.get(position).getExp_months()+" "+"Months Experience");
        }

        else if(mdata.get(position).getExp_years().equalsIgnoreCase("0")){
            if(mdata.get(position).getExp_months().equalsIgnoreCase("1")){
                holder.tvCoacExp.setText(mdata.get(position).getExp_months()+" "+"Month Experience");
            }else{
                holder.tvCoacExp.setText(mdata.get(position).getExp_months()+" "+"Months Experience");
            }


        }
        else {
            holder.tvCoacExp.setText(mdata.get(position).getExp_years()+"."+mdata.get(position).getExp_months()+" "+"Years Experience");
        }




        if (mdata.get(position).getRating()==0){
         holder.rating_bar_review.setVisibility(View.GONE);
        }else{
            holder.rating_bar_review.setVisibility(View.VISIBLE);
            holder.rating_bar_review.setRating(Float.parseFloat(String.valueOf(mdata.get(position).getRating())));
        }
        holder.tvCoachName.setText(Global.capitizeString(mdata.get(position).getName()));
        holder.tvCoacTitle.setText(mdata.get(position).getCat_title());
        holder.tvDiscription.setText(mdata.get(position).getAbout_coach_profile());
        //holder.tvPrice.setText(context.getString(R.string.price)+"\u20B9" +mdata.get(position).getCharge_amount()+"/"+context.getString(R.string.session));
//        if(mdata.get(position).getPrice().getOffer_percentage()==0){
//            holder.tvOfferP.setVisibility(View.GONE);
//        }else{
//            holder.tvOfferP.setVisibility(View.VISIBLE);
//            //holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            holder.tvOfferP.setText(context.getString(R.string.offer_price)+"\u20B9" +mdata.get(position).getPrice().getPayment_price() +"/"+context.getString(R.string.session));
//        }

//        if(mdata.get(position).getADAYS().equalsIgnoreCase("")||mdata.get(position).getA.equalsIgnoreCase("0")){
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

        holder.tv_viewDetails.setOnClickListener(view -> {

            //AcademyListModel.AcademyCoaches.Price price = mdata.get(position).getPrice();
            Intent intent =new Intent(context, visitAcademyCoachProfileActivity.class);
            intent.putExtra("coachId",mdata.get(position).getUser_id());
            intent.putExtra("what_you_teach",mdata.get(position).getWhat_you_teach());
            intent.putExtra("skills_you_learn",mdata.get(position).getSkills_you_learn());
            intent.putExtra("image",mdata.get(position).getAvatar());
            intent.putExtra("Cover",mdata.get(position).getCover());
            intent.putExtra("name",mdata.get(position).getName());
            intent.putExtra("coachTitle",mdata.get(position).getProfile_title());
            intent.putExtra("Language",mdata.get(position).getLang());
            intent.putExtra("lang",mdata.get(position).getLang());
            Bundle args = new Bundle();
             try{
                 //Toaster.customToast(mdata.get(position).getCertificatesList()+"");
                 if(mdata.get(position).getCertificatesList().isEmpty()){
                     args.putSerializable("ARRAYLIST",(Serializable)mdata.get(position).getCertificatesList());
                     intent.putExtra("Certificate",args);
                 }else{
                     args.putSerializable("ARRAYLIST",(Serializable)mdata.get(position).getCertificatesList());
                     intent.putExtra("Certificate",args);
                 }
             }catch(Exception e){
                 e.printStackTrace();
             }



            intent.putExtra("Offer",mdata.get(position).getPrice().getOffer_percentage());
            intent.putExtra("OfferID",mdata.get(position).getPrice().getOffer_id());
            intent.putExtra("OfferPrice",mdata.get(position).getPrice().getPayment_price());
            intent.putExtra("specialization",mdata.get(position).getCat_title());
//            intent.putExtra("ADAYS",mdata.get(position).getADAYS());
//            intent.putExtra("ADAYS_msg",mdata.get(position).getADAYS_msg());


            if(mdata.get(position).getAbout_coach_profile()==null){
                intent.putExtra("des","");
            }else{
                intent.putExtra("des",mdata.get(position).getAbout_coach_profile());
            }

            if(mdata.get(position).getAchievement()==null){
                intent.putExtra("achievementDetails","");
            }else{
                intent.putExtra("achievementDetails",mdata.get(position).getAchievement());
            }

            intent.putExtra("price",mdata.get(position).getCharge_amount());
            intent.putExtra("exp",mdata.get(position).getExps());
            intent.putExtra("expY",mdata.get(position).getExp_years());
            intent.putExtra("expM",mdata.get(position).getExp_months());
            intent.putExtra("rating",mdata.get(position).getRating());
            context.startActivity(intent);

        });



//        if(SessionManager.getProfileType(prefs).equalsIgnoreCase("coach")){
//            holder.rl_user.setVisibility(View.GONE);
//            holder.btnDetailss.setVisibility(View.VISIBLE);
//            holder.btnBook.setVisibility(View.GONE);
//        }else{
//            holder.btnDetailss.setVisibility(View.GONE);
//            holder.rl_user.setVisibility(View.VISIBLE);
//            holder.btnBook.setVisibility(View.VISIBLE);
//        }
//        holder.btnBook.setOnClickListener(view -> {
//            Intent intent =new Intent(context,CoachDetailsActivity.class);
//            intent.putExtra("coachId",mdata.get(position).getUser_id());
//            intent.putExtra("rating",mdata.get(position).getRating());
//            context.startActivity(intent);
//        });
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
        TextView tv_viewDetails;


        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            rating_bar_review=itemView.findViewById(R.id.rating_bar_review);
            btnDetails=itemView.findViewById(R.id.btnDetails);
            tvCoacTitle=itemView.findViewById(R.id.tvCoacTitle);
            tvCoachName=itemView.findViewById(R.id.tvCoachName);
            tvDiscription=itemView.findViewById(R.id.tvDiscription);
            tv_viewDetails = itemView.findViewById(R.id.tv_viewDetails);
           // tvPrice=itemView.findViewById(R.id.tvPrice);


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