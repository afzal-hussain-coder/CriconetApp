package com.pb.criconet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.models.DataModel;
import com.pb.criconet.models.Datum;
import com.pb.criconet.models.Language;

import java.util.ArrayList;
import java.util.List;

public class CoachButtonAdapter extends RecyclerView.Adapter<CoachButtonAdapter.ViewHolder> {

    List<DataModel.Datum> mValues;
    Context mContext;
    private ArrayList<Datum>editList_speclization;
    ArrayList<String>checkedArray;
    protected ItemListenerr mListener;

    public CoachButtonAdapter(Context context, ArrayList<Datum>editList_speclizationn, List<DataModel.Datum> values, ItemListenerr itemListener) {
        editList_speclization=editList_speclizationn;
        mValues = values;
        mContext = context;
        mListener=itemListener;
        checkedArray = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvButton;
        DataModel.Datum items;

        public ViewHolder(View v) {
            super(v);
            //v.setOnClickListener(this);
            tvButton = (TextView) v.findViewById(R.id.tvButton);
        }

//        public void setData(DataModel.Datum item) {
//            this.items = item;
//            tvButton.setText(items.getTitle());
//
//            for(int j= 0; j<editList_speclization.size();j++){
//                if(editList_speclization.get(j).getTitle().equalsIgnoreCase(item.getTitle())){
//                    item.setCheck(true);
//                    mValues.get(j).setCheck(false);
//                    mListener.onItemClick(mValues);
//                    break;
//                }
//            }
//
//            if(item.isCheck()){
//                tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg_red));
//                tvButton.setTextColor(mContext.getResources().getColor(R.color.white));
//            }else {
//                tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg));
//                tvButton.setTextColor(mContext.getResources().getColor(R.color.dim_grey));
//            }
//        }
//
//
//        @Override
//        public void onClick(View view) {
//            if (mListener != null) {
//
//                if(mValues.get(getAbsoluteAdapterPosition()).isCheck()) {
//                    mValues.get(getAbsoluteAdapterPosition()).setCheck(false);
//
//                    notifyDataSetChanged();
//                }else {
//                    mValues.get(getAbsoluteAdapterPosition()).setCheck(true);
//                    //mListener.onItemClick(mValues);
//                    notifyDataSetChanged();
//                }
//                mListener.onItemClick(mValues);
//            }
//        }
    }

    @Override
    public CoachButtonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        DataModel.Datum coachLanguage= mValues.get(position);

        Vholder.tvButton.setText(coachLanguage.getTitle());
        //Vholder.itemView.setChecked(coachLanguage.isSelected());


        for(int j= 0; j<editList_speclization.size();j++){
            if(editList_speclization.get(j).getTitle().equalsIgnoreCase(coachLanguage.getTitle())){
                coachLanguage.setCheck(true);
                checkedArray.add(coachLanguage.getId());
                mListener.onItemClick(checkedArray);
                break;
            }
        }
        if(coachLanguage.isCheck()){
            Vholder.tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg_red));
            Vholder.tvButton.setTextColor(mContext.getResources().getColor(R.color.white));
            }else {
            Vholder.tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg));
            Vholder.tvButton.setTextColor(mContext.getResources().getColor(R.color.dim_grey));
            }

        Vholder.tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataModel.Datum coachLanguage=mValues.get(position);

                if(coachLanguage.isCheck()==true) {
                    coachLanguage.setCheck(false);

                    Vholder.tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg));
                    Vholder.tvButton.setTextColor(mContext.getResources().getColor(R.color.dim_grey));
                    checkedArray.remove(coachLanguage.getId());
                }else{
                    coachLanguage.setCheck(true);
                    Vholder.tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg_red));
                    Vholder.tvButton.setTextColor(mContext.getResources().getColor(R.color.white));
                    checkedArray.add(coachLanguage.getId());
                }

                mListener.onItemClick(checkedArray);
            }
        });


    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListenerr {
        void onItemClick(List<String> item);
    }
}