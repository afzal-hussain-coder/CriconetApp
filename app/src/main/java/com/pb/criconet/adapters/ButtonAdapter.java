package com.pb.criconet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.models.DataModel;

import java.util.ArrayList;
import java.util.List;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ViewHolder> {

    List<DataModel.Datum> mValues;
    Context mContext;
    protected ItemListener mListener;

    public ButtonAdapter(Context context, List<DataModel.Datum> values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvButton;
        DataModel.Datum items;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            tvButton = (TextView) v.findViewById(R.id.tvButton);
        }

        public void setData(DataModel.Datum item) {
            this.items = item;
            tvButton.setText(items.getTitle());
            if(item.isCheck()){
                tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg_red));
                tvButton.setTextColor(mContext.getResources().getColor(R.color.white));
            }else {
                tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg));
                tvButton.setTextColor(mContext.getResources().getColor(R.color.dim_grey));
            }
        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {

                if(mValues.get(getAbsoluteAdapterPosition()).isCheck()) {
                    mValues.get(getAbsoluteAdapterPosition()).setCheck(false);

                    notifyDataSetChanged();
                }else {
                    mValues.get(getAbsoluteAdapterPosition()).setCheck(true);
                    //mListener.onItemClick(mValues);
                    notifyDataSetChanged();
                }
                mListener.onItemClick(mValues);
            }
        }
    }

    @Override
    public ButtonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
       Vholder.setData(mValues.get(position));

    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(List<DataModel.Datum> item);
    }
}