package com.pb.criconet.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pb.criconet.R;

import java.util.ArrayList;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder> {
    ArrayList<String> personImages;
    Context context;
    public ImageGalleryAdapter(Context context, ArrayList<String> personImages) {
        this.context = context;
        this.personImages = personImages;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image_gallery, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //holder.image.setImageResource(personImages.get(position));
        if (!personImages.get(position).isEmpty()) {
            Glide.with(context).load(personImages.get(position))
                    .into(holder.image);
        } else {
            Glide.with(context).load(R.drawable.bg_certificate)
                    .into(holder.image);
        }
    }


    @Override
    public int getItemCount() {
        return personImages.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            image = (RoundedImageView) itemView.findViewById(R.id.img_gallery);
        }
    }
}
