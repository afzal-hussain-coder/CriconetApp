package com.pb.criconet.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.myViewHolder> {

    private Context mContext;
    private List<Integer>color_list;
    ColorChooserInterface listner;
    public ColorAdapter(Context  mContext,List<Integer>color_list,ColorChooserInterface listner){
        this.mContext=mContext;
        this.color_list=color_list;
        this.listner=listner;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_color,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        ImageViewCompat.setImageTintList(holder.img_color, ColorStateList.valueOf(color_list.get(position)));
        holder.img_color.setOnClickListener(v -> listner.setColor(color_list.get(position)));

    }

    @Override
    public int getItemCount() {
        return color_list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_color;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img_color=itemView.findViewById(R.id.img_color);
        }
    }

    public interface ColorChooserInterface{
        void setColor(int color);
    }
}
