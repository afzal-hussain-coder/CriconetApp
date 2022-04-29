package com.pb.criconet.adapters;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Toaster;

import java.util.ArrayList;

public class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.MyViewHolder> {
    ArrayList<String> videoGalleryList;
    Context context;


    public VideoGalleryAdapter(Context context, ArrayList<String> videoGalleryList) {
        this.context = context;
        this.videoGalleryList = videoGalleryList;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video_gallery, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(holder.simpleVideoView);
        holder.simpleVideoView.setMediaController(mediaController);
        holder.simpleVideoView.setKeepScreenOn(true);
        holder.simpleVideoView.setVideoPath(videoGalleryList.get(position));
        //holder.simpleVideoView.start();         //call this method for auto playing video

        holder.simpleVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                holder.videoView_thumbnail.setVisibility(View.GONE);
                holder.simpleVideoView.start();
                //you can Hide progress dialog here when video is start playing;

            }
        });
        holder.simpleVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                holder.simpleVideoView.stopPlayback();
                holder.videoView_thumbnail.setVisibility(View.VISIBLE);

            }
        });

//        holder.simpleVideoView.setOnPreparedListener(
//                new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mediaPlayer) {
//
//                        // Hide buffering message.
//                        holder.ib_play.setVisibility(VideoView.INVISIBLE);
//
//                        // Restore saved position, if available.
//                        if (mCurrentPosition > 0) {
//                            holder.simpleVideoView.seekTo(mCurrentPosition);
//                        } else {
//                            // Skipping to 1 shows the first frame of the video.
//                            holder.simpleVideoView.seekTo(1);
//                        }
//
//                        // Start playing!
//                        holder.simpleVideoView.start();
//                    }
//                });
//        holder.simpleVideoView.setOnCompletionListener(
//                new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        Toaster.customToast(context.getString(R.string.));
//                        Toast.makeText(MainActivity.this,
//                                R.string.toast_message,
//                                Toast.LENGTH_SHORT).show();
//
//                        // Return the video position to the start.
//                        mVideoView.seekTo(0);
//                    }
//                });
    }


    @Override
    public int getItemCount() {
        return videoGalleryList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        VideoView simpleVideoView;
        ImageView videoView_thumbnail;
       // ImageButton ib_play;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            simpleVideoView = (VideoView) itemView.findViewById(R.id.videoView);
           // videoView_thumbnail = itemView.findViewById(R.id.videoView_thumbnail);
//            MediaController controller = new MediaController(context);
//            controller.setMediaPlayer(simpleVideoView);
//            simpleVideoView.setMediaController(controller);
            //simpleVideoView.setZOrderOnTop(false);
            //ib_play = itemView.findViewById(R.id.ib_play);
        }
    }
}
