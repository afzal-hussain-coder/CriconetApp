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
                tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.round_corner_red));
            }else {
                tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg));
            }
        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(mValues);
                if(mValues.get(getAdapterPosition()).isCheck()) {
                    mValues.get(getAdapterPosition()).setCheck(false);
                    notifyDataSetChanged();
                }else {
                    mValues.get(getAdapterPosition()).setCheck(true);
                    notifyDataSetChanged();
                }
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