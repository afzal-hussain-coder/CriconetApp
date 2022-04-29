package com.pb.criconet.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.pb.criconet.R;

import java.util.ArrayList;

public class MediaRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private ArrayList<String> mediaObjects;
  private RequestManager requestManager;

  public MediaRecyclerAdapter(ArrayList<String> mediaObjects,
                              RequestManager requestManager) {
    this.mediaObjects = mediaObjects;
    this.requestManager = requestManager;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new PlayerViewHolder(
        LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.row_video_gallery, viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    ((PlayerViewHolder) viewHolder).onBind(mediaObjects.get(i), requestManager);
  }

  @Override
  public int getItemCount() {
    return mediaObjects.size();
  }
}
