package com.pb.criconet.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;


public class FeedsPhotos extends Fragment {
    View rootView;
    SharedPreferences prefs;
    RecyclerView photo_video_list;
    ArrayList<String> photosVideosModelArrayList;
    TextView notfound;
    ProgressDialog progress;
    RequestQueue queue;
    public static FeedsPhotos newInstance() {
        return new FeedsPhotos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.feeds_photos, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(R.string.photo);


        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        progress = new ProgressDialog(getActivity());
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        notfound = (TextView) rootView.findViewById(R.id.notfound);

        photo_video_list = (RecyclerView) rootView.findViewById(R.id.photo_video_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        photo_video_list.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        Bundle bundle = getArguments();
        if (bundle != null) {
            photosVideosModelArrayList = bundle.getStringArrayList("photos");
        }


//        photosVideosModelArrayList = new ArrayList<>();
        CustomAdapterPhoto adapter = new CustomAdapterPhoto(getActivity(), photosVideosModelArrayList);
        photo_video_list.setAdapter(adapter);


        return rootView;
    }

    public void imageViewDialog(String url) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.profiledialog);
        dialog.setCancelable(true);
        PhotoView img = (PhotoView) dialog.findViewById(R.id.image_view);
        ImageView del = (ImageView) dialog.findViewById(R.id.del);
//        img.setScale(3);
        Glide.with(getActivity())
                .load(url)
//                .asBitmap()
//                .placeholder(R.drawable.loading).dontAnimate()
//                .error(R.drawable.app_icon).dontAnimate()
                .into(img);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    class CustomAdapterPhoto extends RecyclerView.Adapter<CustomAdapterPhoto.MyViewHolder> {
        ArrayList<String> photovideolist;
        Context context;

        CustomAdapterPhoto(Context context, ArrayList<String> personNames) {
            this.context = context;
            this.photovideolist = personNames;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_video_items, parent, false);
            // set the view's size, margins, paddings and layout parameters
            MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            // set the data in items
            Glide.with(context).load(photovideolist.get(position)).into(holder.image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewDialog(photovideolist.get(position));
                }
            });

            holder.del_icon.setVisibility(View.GONE);
            holder.play_icon.setVisibility(View.GONE);

        }

        @Override
        public int getItemCount() {
            return photovideolist.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView image;// init the item view's
            ImageView play_icon;// init the item view's
            ImageView del_icon;// init the item view's

            MyViewHolder(View itemView) {
                super(itemView);
                // get the reference of item view's
                image = (ImageView) itemView.findViewById(R.id.image);
                play_icon = (ImageView) itemView.findViewById(R.id.play_icon);
                del_icon = (ImageView) itemView.findViewById(R.id.del_icon);
            }
        }
    }

}
