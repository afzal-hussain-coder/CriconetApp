package com.pb.criconet.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder;
import com.allattentionhere.autoplayvideos.AAH_VideosAdapter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.card.MaterialCardView;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.pb.criconet.activity.LikeDislike;
import com.pb.criconet.activity.PagesDetails;
import com.pb.criconet.activity.PostListeners;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.activity.UserDetails;
import com.pb.criconet.activity.WebView_Activity;
import com.pb.criconet.fragments.FeedsPhotos;
import com.pb.criconet.models.ImageModel;
import com.pb.criconet.models.NewPostModel;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;


public class HomeAdapter extends AAH_VideosAdapter {

    private static final int TYPE_VIDEO = 0, TYPE_IMAGE = 1, TYPE_MULTI_IMAGE = 2, TYPE_TEXT = 3, TYPE_LINK = 4,
            TYPE_YOUTUBE = 5;
    private final ArrayList<NewPostModel> arrayList_image;
    private final Activity context;
    private final SharedPreferences prefs;
    private final PostListeners listeners;

    public HomeAdapter(Activity mcontext, ArrayList<NewPostModel> chatname_list1, PostListeners listeners) {
        this.arrayList_image = chatname_list1;
        context = mcontext;
        this.listeners = listeners;
        prefs = PreferenceManager.getDefaultSharedPreferences(mcontext);
    }


    @Override
    public int getItemViewType(int position) {
        final NewPostModel data = arrayList_image.get(position);

        if (data.getPost_type().equalsIgnoreCase("image")) {
            return TYPE_IMAGE;
        } else if (data.getPost_type().equalsIgnoreCase("video")) {
            return TYPE_VIDEO;
        } else if (data.getPost_type().equalsIgnoreCase("text")) {
            return TYPE_TEXT;
        } else if (data.getPost_type().equalsIgnoreCase("link")) {
            return TYPE_LINK;
        } else if (data.getPost_type().equalsIgnoreCase("youtube")) {
            return TYPE_YOUTUBE;
        } else if (data.getPost_type().equalsIgnoreCase("photo_multi")) {
            return TYPE_IMAGE;
        } else {
            return TYPE_IMAGE;
        }
    }

    @Override
    public AAH_CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_IMAGE: {
                // TYPE_IMAGE, TYPE_MULTI_IMAGE
                View itemView1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_adapter_item_viewpager, parent, false);
                return new MyImageViewHolder(itemView1);
            }
            case TYPE_VIDEO: {
                // TYPE_VIDEO
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_adapter_item_video, parent, false);
                return new MyViewHolder(itemView);
            }
            default: {
                // TYPE_TEXT, TYPE_LINK, TYPE_YOUTUBE
                View itemView1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_adapter_item, parent, false);
                return new MyImageViewHolder(itemView1);
            }
        }
    }

//    @Override
//    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
//        recyclerView.holde  ((MyImageViewHolder).youtube_view.release();
//    }

    @Override
    public void onBindViewHolder(final AAH_CustomViewHolder holder, final int position) {
        final NewPostModel data = arrayList_image.get(position);
        //todo
        switch (getItemViewType(position)) {
            case TYPE_IMAGE:
                /*-----------------------------------------------------------------------------------
                ------------------------------------ FOR IMAGE/MULTI IMAGE --------------------------
                -----------------------------------------------------------------------------------*/
            {
                ((MyImageViewHolder) holder).user_name.setText(Global.capitalizeFirstLatterOfString(data.getPublisherName()));
                Glide.with(context).load(data.getPublisherAvatar()).dontAnimate()
                        .into(((MyImageViewHolder) holder).user_image);

                if (data.getPost_type().equalsIgnoreCase("photo_multi")) {
                    if (getKeyList(data.getPhoto_multi()).size() > 0) {
                        ((MyImageViewHolder) holder).multi_img.setVisibility(View.VISIBLE);
                        ((MyImageViewHolder) holder).post_image.setVisibility(View.GONE);

                        ArrayList<String> temp = getKeyList(data.getPhoto_multi());
//                    temp.add(0, data.getImage());

                        MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter(context, temp);
                        ((MyImageViewHolder) holder).viewPager.setAdapter(myCustomPagerAdapter);
                        // Disable clip to padding
                        ((MyImageViewHolder) holder).viewPager.setClipToPadding(false);
                        // set padding manually, the more you set the padding the more you see of prev & next page
                        ((MyImageViewHolder) holder).viewPager.setPadding(50, 0, 50, 0);
                        // sets a margin b/w individual pages to ensure that there is a gap b/w them
                        ((MyImageViewHolder) holder).viewPager.setPageMargin(20);

//                    if (data.getMulti_files_feeds().size() == 1) {
//                        Glide.with(context).load(data.getMulti_files_feeds().get(0))
//                                .into(((MyImageViewHolder) holder).new_image);
//                    } else {
//                        ((MyImageViewHolder) holder).multi_img.setVisibility(View.VISIBLE);
//                        ((MyImageViewHolder) holder).img_count.setText((" + " + (data.getMulti_files_feeds().size() - 1)));
//                        Glide.with(context).load(data.getMulti_files_feeds().get(0))
//                                .into(((MyImageViewHolder) holder).new_image);
//                    }
                    }
                } else {
                    ((MyImageViewHolder) holder).multi_img.setVisibility(View.GONE);
                    ((MyImageViewHolder) holder).post_image.setVisibility(View.VISIBLE);
                    Glide.with(context).load(data.getPostFile()).into(((MyImageViewHolder) holder).post_image);


                    if (data.getPost_type().equalsIgnoreCase("profile_picture")) {

                        if(data.getPublisher().getUser().getGender().equalsIgnoreCase("female")){
                            ((MyImageViewHolder) holder).post_status.setText(R.string.change_profile_picc);
                        }else{
                            ((MyImageViewHolder) holder).post_status.setText(R.string.change_profile_pic);
                        }

                    } else {
                        ((MyImageViewHolder) holder).post_status.setText(R.string.post_a_new_photo);
                    }

                }


                ((MyImageViewHolder) holder).multi_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> photos = new ArrayList<>();
//                        photos.add(data.getPostFile());
                        photos.addAll(getKeyList(data.getPhoto_multi()));
                        Fragment fragment = new FeedsPhotos();
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("photos", photos);
                        fragment.setArguments(bundle);
//                        ((FragmentActivity) v.getContext()).getFragmentManager().beginTransaction()
//                                .replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                    }
                });

                ((MyImageViewHolder) holder).post_time.setText(data.getTime_text());
//                ((MyImageViewHolder) holder).like_count.setText((data.getCount_post_likes() + " " + context.getString(R.string.likes)));
//                ((MyImageViewHolder) holder).dislike_count.setText((data.getCount_post_wonders() + " " + context.getString(R.string.dislikes)));
                if (data.getCount_post_comments().equalsIgnoreCase("0") || data.getCount_post_comments().equalsIgnoreCase("1")) {
                    ((MyImageViewHolder) holder).comment_count.setText((data.getCount_post_comments() + " " + context.getString(R.string.comments)));
                } else {
                    ((MyImageViewHolder) holder).comment_count.setText((data.getCount_post_comments() + " " + context.getString(R.string.commentss)));
                }

//                ((MyImageViewHolder) holder).share_count.setText((data.getCount_post_shares() + " " + context.getString(R.string.share)));
                if (Integer.parseInt(data.getCount_post_likes()) > 0) {
                    ((MyImageViewHolder) holder).like_link.setVisibility(View.VISIBLE);
                    ((MyImageViewHolder) holder).like_link.setText((data.getCount_post_likes()));
                } else {
                    ((MyImageViewHolder) holder).like_link.setVisibility(View.GONE);
                }
                if (Integer.parseInt(data.getCount_post_wonders()) > 0) {
                    ((MyImageViewHolder) holder).dislike_link.setVisibility(View.VISIBLE);
                    ((MyImageViewHolder) holder).dislike_link.setText((data.getCount_post_wonders()));
                } else {
                    ((MyImageViewHolder) holder).dislike_link.setVisibility(View.GONE);
                }
                // new work here

                if (data.getIs_liked_by_me()) {
                    Glide.with(context).load(R.drawable.like_active).into(((MyImageViewHolder) holder).like_icon);
                    ((MyImageViewHolder) holder).like_count.setTextColor(context.getResources().getColor(R.color.liked_color));
                    ((MyImageViewHolder) holder).like_count.setText(context.getResources().getString(R.string.Liked));
                } else {
                    Glide.with(context).load(R.drawable.like).into(((MyImageViewHolder) holder).like_icon);
                    ((MyImageViewHolder) holder).like_count.setTextColor(context.getResources().getColor(R.color.white));
                    ((MyImageViewHolder) holder).like_count.setText(context.getResources().getString(R.string.Like));
                }
                if (data.getIs_wondered_by_me()) {
                    Glide.with(context).load(R.drawable.dislike_active).into(((MyImageViewHolder) holder).dislike_icon);
                    ((MyImageViewHolder) holder).dislike_count.setTextColor(context.getResources().getColor(R.color.disliked_color));
                } else {
                    Glide.with(context).load(R.drawable.dislike).into(((MyImageViewHolder) holder).dislike_icon);
                    ((MyImageViewHolder) holder).dislike_count.setTextColor(context.getResources().getColor(R.color.white));
                }

                if (data.getPostText_API().equalsIgnoreCase("")) {
                    ((MyImageViewHolder) holder).post_text.setVisibility(View.GONE);
                    ((MyImageViewHolder) holder).post_text_autolink.setVisibility(View.GONE);
                } else {

                    String text = data.getPostText_API().replace("\n", "<br>");

                    if (data.getTagsusers().isEmpty()) {
                        ((MyImageViewHolder) holder).post_text.setVisibility(View.VISIBLE);
                        ((MyImageViewHolder) holder).post_text_autolink.setVisibility(View.GONE);
                        ((MyImageViewHolder) holder).post_text.setText(data.getPostText_API());
                    } else {
                        ((MyImageViewHolder) holder).post_text.setVisibility(View.GONE);
                        ((MyImageViewHolder) holder).post_text_autolink.setVisibility(View.VISIBLE);
                        ((MyImageViewHolder) holder).post_text_autolink.addAutoLinkMode(
                                AutoLinkMode.MODE_HASHTAG,
                                AutoLinkMode.MODE_PHONE,
                                AutoLinkMode.MODE_URL,
                                AutoLinkMode.MODE_EMAIL,
                                AutoLinkMode.MODE_MENTION);
                        ((MyImageViewHolder) holder).post_text_autolink.setAutoLinkText(Global.capitalizeFirstLatterOfString(data.getPostText_API()));
                        ((MyImageViewHolder) holder).post_text_autolink.setHashtagModeColor(ContextCompat.getColor(context, R.color.app_light_red));
                        ((MyImageViewHolder) holder).post_text_autolink.setPhoneModeColor(ContextCompat.getColor(context, R.color.app_light_red));
                        ((MyImageViewHolder) holder).post_text_autolink.setCustomModeColor(ContextCompat.getColor(context, R.color.app_light_red));
                        ((MyImageViewHolder) holder).post_text_autolink.setMentionModeColor(ContextCompat.getColor(context, R.color.app_light_red));
                        ((MyImageViewHolder) holder).post_text_autolink.setUrlModeColor(ContextCompat.getColor(context, R.color.app_light_red));

                        ((MyImageViewHolder) holder).post_text_autolink.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
                            @Override
                            public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                                String match = matchedText.trim();
                                for (int i = 0; i < data.getTagsusers().size(); i++) {
                                    // Toaster.customToast(match + "/" + tagModelArrayList.get(i).getUser_id() + "?" + tagModelArrayList.get(i).getMatch_name());
                                    if (match.equalsIgnoreCase(data.getTagsusers().get(i).getMatch_name().trim())) {
                                        if (data.getPublisher_type().equalsIgnoreCase("user")) {
                                            Intent intent = new Intent(context, UserDetails.class);
                                            intent.putExtra("user_id", data.getTagsusers().get(i).getUser_id());
                                            intent.putExtra("FROM", "3");
                                            context.startActivity(intent);
                                            context.finish();
                                        } else {
                                            Intent intent = new Intent(context, PagesDetails.class);
                                            intent.putExtra("page_id", data.getPage_id());
                                            intent.putExtra("FROM", "3");
                                            context.startActivity(intent);
                                            context.finish();

                                        }

                                    }
                                }


                            }
                        });

                    }
                }

//                if (data.getShared().equalsIgnoreCase("No")) {
                if (data.getPost_type().equalsIgnoreCase("Image")) {
                    Glide.with(context).load(data.getPostFile()).into(((MyImageViewHolder) holder).post_image);

                    if (data.getPost_type().equalsIgnoreCase("profile_picture")) {
                        if(data.getPublisher().getUser().getGender().equalsIgnoreCase("female")){
                            ((MyImageViewHolder) holder).post_status.setText(R.string.change_profile_picc);
                        }else{
                            ((MyImageViewHolder) holder).post_status.setText(R.string.change_profile_pic);
                        }
                    } else {
                        ((MyImageViewHolder) holder).post_status.setText(R.string.post_a_new_photo);
                    }

                } else if (data.getPost_type().equalsIgnoreCase("Video")) {
                    holder.setImageUrl(data.getThumb());
                    ((MyImageViewHolder) holder).post_status.setText(R.string.post_a_new_video);
                } else {
                    Glide.with(context).load(data.getPostFile()).into(((MyImageViewHolder) holder).post_image);
                }
//                } else {
//                    if (data.getPost_type().equalsIgnoreCase("Image")) {
//                        Glide.with(context).load(data.getImage()).into(((MyImageViewHolder) holder).post_image);
//                        ((MyImageViewHolder) holder).post_status.setText(R.string.share_a_new_photo);
//                    } else if (data.getPost_type().equalsIgnoreCase("Video")) {
//                        holder.setImageUrl(data.getVideothums());
////                Glide.with(context).load(data.getVideothums()).into(((MyViewHolder) holder).post_image);
//                        ((MyImageViewHolder) holder).post_status.setText(R.string.share_a_new_video);
//                    } else {
//                        Glide.with(context).load(data.getImage()).into(((MyImageViewHolder) holder).post_image);
//                    }
//                }

                ((MyImageViewHolder) holder).post_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageViewDialog(data.getPostFile());
                    }
                });
                ((MyImageViewHolder) holder).post_options.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.getPublisher_type().equalsIgnoreCase("page")) {
                            if (data.getPublisher().getPageModel().getIs_page_onwer()) {
                                showFilterPopupDelete(v, arrayList_image.get(position));
                            } else {
                                showFilterPopup(v, arrayList_image.get(position));
                            }
                        } else {
                            if (data.getPublisherId().equals(SessionManager.get_user_id(prefs))) {
                                showFilterPopupDelete(v, arrayList_image.get(position));
                            } else {
                                showFilterPopup(v, arrayList_image.get(position));
                            }
                        }
                    }
                });
                ((MyImageViewHolder) holder).comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listeners.onCommentClickListener(data);
                    }
                });
                ((MyImageViewHolder) holder).share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (data.getSetting().equalsIgnoreCase("DONOTSHAREMYDATA"))
//                            Toast.makeText(context, R.string.post_non_sharable, Toast.LENGTH_SHORT).show();
//                        else
                        listeners.onShareClickListener(data);
                    }
                });
                // MyWork
                ((MyImageViewHolder) holder).like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int likes_c = Integer.parseInt(data.getCount_post_likes());
                        if (data.getIs_liked_by_me()) {
                            data.setIs_liked_by_me(false);
                            likes_c--;
                            data.setCount_post_likes(String.valueOf(likes_c));
                            ((MyImageViewHolder) holder).like_link.setText((likes_c + ""));
                            ((MyImageViewHolder) holder).like_count.setTextColor((context.getResources().getColor(R.color.white)));
                            Glide.with(context).load(R.drawable.like).into(((MyImageViewHolder) holder).like_icon);
                            ((MyImageViewHolder) holder).like_count.setText(context.getResources().getString(R.string.Like));
                        } else {
                            data.setIs_liked_by_me(true);
                            likes_c++;
                            data.setCount_post_likes(String.valueOf(likes_c));
                            ((MyImageViewHolder) holder).like_link.setText((likes_c + ""));
                            ((MyImageViewHolder) holder).like_count.setText(context.getResources().getString(R.string.Liked));
                            ((MyImageViewHolder) holder).like_count.setTextColor((context.getResources().getColor(R.color.liked_color)));
                            Glide.with(context).load(R.drawable.like_active).into(((MyImageViewHolder) holder).like_icon);
                        }
                        listeners.onLikeClickListener(data);
                        updatePost(data.getId(), position);
//                        if (likes_c > 0)
//                            ((MyImageViewHolder) holder).like_link.setVisibility(View.VISIBLE);
//                        else
//                            ((MyImageViewHolder) holder).like_link.setVisibility(View.GONE);
                    }
                });
                ((MyImageViewHolder) holder).dislike.setOnClickListener(v -> {
                    int likes_c = Integer.parseInt(data.getCount_post_wonders());
                    if (data.getIs_wondered_by_me()) {
                        data.setIs_wondered_by_me(false);
                        likes_c--;
                        data.setCount_post_wonders(String.valueOf(likes_c));
                        ((MyImageViewHolder) holder).dislike_link.setText((likes_c + ""));
                        Glide.with(context).load(R.drawable.dislike).into(((MyImageViewHolder) holder).dislike_icon);
                        ((MyImageViewHolder) holder).dislike_count.setTextColor((context.getResources().getColor(R.color.white)));
                    } else {
                        data.setIs_wondered_by_me(true);
                        likes_c++;
                        data.setCount_post_wonders(String.valueOf(likes_c));
                        ((MyImageViewHolder) holder).dislike_link.setText((likes_c + ""));
                        ((MyImageViewHolder) holder).dislike_count.setTextColor((context.getResources().getColor(R.color.disliked_color)));
                        Glide.with(context).load(R.drawable.dislike_active).into(((MyImageViewHolder) holder).dislike_icon);
                    }
//                        if (likes_c > 0)
//                            ((MyImageViewHolder) holder).dislike_link.setVisibility(View.VISIBLE);
//                        else
//                            ((MyImageViewHolder) holder).dislike_link.setVisibility(View.GONE);
                    listeners.onDislikeClickListener(data);
                    updatePost(data.getId(), position);
                });
                ((MyImageViewHolder) holder).user_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (!data.getPublisherId().equalsIgnoreCase(SessionManager.get_user_id(prefs))) {
                        listeners.onProfileClickListener(data);
//                        }
                    }
                });
                ((MyImageViewHolder) holder).user_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (!data.getPublisherId().equalsIgnoreCase(SessionManager.get_user_id(prefs))) {
                        listeners.onProfileClickListener(data);
//                        }
                    }
                });
                ((MyImageViewHolder) holder).like_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, LikeDislike.class);
                        i.putExtra(LikeDislike.Type, context.getString(R.string.likes));
                        i.putExtra(LikeDislike.PostId, data.getId());
                        context.startActivity(i);
                        context.finish();
                    }
                });
                ((MyImageViewHolder) holder).dislike_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, LikeDislike.class);
                        i.putExtra(LikeDislike.Type, context.getString(R.string.dislikes));
                        i.putExtra(LikeDislike.PostId, data.getId());
                        context.startActivity(i);
                        context.finish();
                    }
                });

                break;
            }
            case TYPE_VIDEO:
                /*-----------------------------------------------------------------------------------
                ------------------------------------ FOR VIDEO --------------------------------------
                -----------------------------------------------------------------------------------*/
            {
                ((MyViewHolder) holder).user_name.setText(Global.capitalizeFirstLatterOfString(data.getPublisherName()));
                Glide.with(context).load(data.getPublisherAvatar()).dontAnimate().into(((MyViewHolder) holder).user_image);

                ((MyViewHolder) holder).post_time.setText(data.getTime_text());

                if (data.getCount_post_comments().equalsIgnoreCase("0") || data.getCount_post_comments().equalsIgnoreCase("1")) {
                    ((MyViewHolder) holder).comment_count.setText((data.getCount_post_comments() + " " + context.getString(R.string.comments)));
                } else {
                    ((MyViewHolder) holder).comment_count.setText((data.getCount_post_comments() + " " + context.getString(R.string.commentss)));
                }

                if (Integer.parseInt(data.getCount_post_likes()) > 0) {
                    ((MyViewHolder) holder).like_link.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).like_link.setText((data.getCount_post_likes()));
                } else {
                    ((MyViewHolder) holder).like_link.setVisibility(View.GONE);
                }
                if (Integer.parseInt(data.getCount_post_wonders()) > 0) {
                    ((MyViewHolder) holder).dislike_link.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).dislike_link.setText((data.getCount_post_wonders()));
                } else {
                    ((MyViewHolder) holder).dislike_link.setVisibility(View.GONE);
                }

                // new work by me
                if (data.getIs_liked_by_me()) {
                    Glide.with(context).load(R.drawable.like_active).into(((MyViewHolder) holder).like_icon);
                    ((MyViewHolder) holder).like_count.setTextColor(context.getResources().getColor(R.color.liked_color));
                    ((MyViewHolder) holder).like_count.setText(context.getResources().getString(R.string.Liked));
                } else {
                    Glide.with(context).load(R.drawable.like).into(((MyViewHolder) holder).like_icon);
                    ((MyViewHolder) holder).like_count.setTextColor(context.getResources().getColor(R.color.white));
                    ((MyViewHolder) holder).like_count.setText(context.getResources().getString(R.string.Like));
                }
                if (data.getIs_wondered_by_me()) {
                    Glide.with(context).load(R.drawable.dislike_active).into(((MyViewHolder) holder).dislike_icon);
                    ((MyViewHolder) holder).dislike_count.setTextColor(context.getResources().getColor(R.color.disliked_color));
                } else {
                    Glide.with(context).load(R.drawable.dislike).into(((MyViewHolder) holder).dislike_icon);
                    ((MyViewHolder) holder).dislike_count.setTextColor(context.getResources().getColor(R.color.white));
                }

                if (data.getPostText_API().equalsIgnoreCase("")) {
                    ((MyViewHolder) holder).post_text.setVisibility(View.GONE);
                    ((MyViewHolder) holder).post_text_autolink.setVisibility(View.GONE);
                } else {

                    String text = data.getPostText_API().replace("\n", "<br>");

                    if (data.getTagsusers().isEmpty()) {
                        ((MyViewHolder) holder).post_text.setVisibility(View.VISIBLE);
                        ((MyViewHolder) holder).post_text_autolink.setVisibility(View.GONE);
                        ((MyViewHolder) holder).post_text.setText(data.getPostText_API());
                    } else {
                        ((MyViewHolder) holder).post_text.setVisibility(View.GONE);
                        ((MyViewHolder) holder).post_text_autolink.setVisibility(View.VISIBLE);
                        ((MyViewHolder) holder).post_text_autolink.addAutoLinkMode(
                                AutoLinkMode.MODE_HASHTAG,
                                AutoLinkMode.MODE_PHONE,
                                AutoLinkMode.MODE_URL,
                                AutoLinkMode.MODE_EMAIL,
                                AutoLinkMode.MODE_MENTION);
                        ((MyViewHolder) holder).post_text_autolink.setAutoLinkText(Global.capitalizeFirstLatterOfString(data.getPostText_API()));
                        ((MyViewHolder) holder).post_text_autolink.setHashtagModeColor(ContextCompat.getColor(context, R.color.app_light_red));
                        ((MyViewHolder) holder).post_text_autolink.setPhoneModeColor(ContextCompat.getColor(context, R.color.app_light_red));
                        ((MyViewHolder) holder).post_text_autolink.setCustomModeColor(ContextCompat.getColor(context, R.color.app_light_red));
                        ((MyViewHolder) holder).post_text_autolink.setMentionModeColor(ContextCompat.getColor(context, R.color.app_light_red));
                        ((MyViewHolder) holder).post_text_autolink.setUrlModeColor(ContextCompat.getColor(context, R.color.app_light_red));

                        ((MyViewHolder) holder).post_text_autolink.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
                            @Override
                            public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                                String match = matchedText.trim();
                                for (int i = 0; i < data.getTagsusers().size(); i++) {
                                    // Toaster.customToast(match + "/" + tagModelArrayList.get(i).getUser_id() + "?" + tagModelArrayList.get(i).getMatch_name());
                                    if (match.equalsIgnoreCase(data.getTagsusers().get(i).getMatch_name().trim())) {
                                        if (data.getPublisher_type().equalsIgnoreCase("user")) {
                                            Intent intent = new Intent(context, UserDetails.class);
                                            intent.putExtra("user_id", data.getTagsusers().get(i).getUser_id());
                                            intent.putExtra("FROM", "3");
                                            context.startActivity(intent);
                                            context.finish();
                                        } else {
                                            Intent intent = new Intent(context, PagesDetails.class);
                                            intent.putExtra("page_id", data.getPage_id());
                                            intent.putExtra("FROM", "3");
                                            context.startActivity(intent);
                                            context.finish();

                                        }

                                    }
                                }


                            }
                        });

                    }
                }

                if (data.getPost_type().equalsIgnoreCase("Image")) {
                    ((MyViewHolder) holder).setImageUrl(data.getThumb());
                    //((MyViewHolder) holder).post_status.setText(R.string.post_a_new_photo);

                    if (data.getPost_type().equalsIgnoreCase("profile_picture")) {
                        if(data.getPublisher().getUser().getGender().equalsIgnoreCase("female")){
                            ((MyViewHolder) holder).post_status.setText(R.string.change_profile_picc);
                        }else{
                            ((MyViewHolder) holder).post_status.setText(R.string.change_profile_pic);
                        }
                    } else {
                        ((MyViewHolder) holder).post_status.setText(R.string.post_a_new_photo);
                    }

                } else if (data.getPost_type().equalsIgnoreCase("Video")) {
                    ((MyViewHolder) holder).setImageUrl(data.getThumb());
                    ((MyViewHolder) holder).post_status.setText(R.string.post_a_new_video);
                } else {
                    ((MyViewHolder) holder).setImageUrl(data.getThumb());
                }

                holder.setVideoUrl(data.getPostFile());//Ranjeet
                holder.setLooping(true);

                //load image/thumbnail into imageview
                if (data.getThumb() != null && !data.getThumb().isEmpty()) {
                    Picasso.get().load(((MyViewHolder) holder).getImageUrl()).config(Bitmap.Config.RGB_565).into(((MyViewHolder) holder).getAAH_ImageView());
                }
                //((MyViewHolder) holder).playVideo();
                //to play pause videos manually (optional)
                ((MyViewHolder) holder).img_playback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (((MyViewHolder) holder).isPlaying()) {
                                ((MyViewHolder) holder).pauseVideo();
                                ((MyViewHolder) holder).setPaused(true);
                            } else {
                                ((MyViewHolder) holder).setPaused(false);
                                ((MyViewHolder) holder).playVideo();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


                //to mute/un-mute video (optional)
                ((MyViewHolder) holder).img_vol.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((MyViewHolder) holder).isMuted) {
                            ((MyViewHolder) holder).unmuteVideo();
                            ((MyViewHolder) holder).img_vol.setImageResource(R.drawable.ic_unmute);
                        } else {
                            ((MyViewHolder) holder).muteVideo();
                            ((MyViewHolder) holder).img_vol.setImageResource(R.drawable.ic_mute);
                        }
                        ((MyViewHolder) holder).isMuted = !((MyViewHolder) holder).isMuted;
                    }
                });

                if (data.getPostFile() == null) {
                    ((MyViewHolder) holder).img_vol.setVisibility(View.GONE);
                    ((MyViewHolder) holder).img_playback.setVisibility(View.GONE);
                } else {
                    ((MyViewHolder) holder).img_vol.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).img_playback.setVisibility(View.VISIBLE);
                }

                ((MyViewHolder) holder).post_options.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.getPublisher_type().equalsIgnoreCase("page")) {
                            if (data.getPublisher().getPageModel().getIs_page_onwer()) {
                                showFilterPopupDelete(v, arrayList_image.get(position));
                            } else {
                                showFilterPopup(v, arrayList_image.get(position));
                            }
                        } else {
                            if (data.getPublisherId().equals(SessionManager.get_user_id(prefs))) {
                                showFilterPopupDelete(v, arrayList_image.get(position));
                            } else {
                                showFilterPopup(v, arrayList_image.get(position));
                            }
                        }
                    }
                });

                ((MyViewHolder) holder).comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listeners.onCommentClickListener(data);
                    }
                });

                ((MyViewHolder) holder).share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listeners.onShareClickListener(data);
                    }
                });

// MyWork
                ((MyViewHolder) holder).like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int likes_c = Integer.parseInt(data.getCount_post_likes());
                        if (data.getIs_liked_by_me()) {
                            data.setIs_liked_by_me(false);
                            likes_c--;
                            data.setCount_post_likes(String.valueOf(likes_c));
                            ((MyViewHolder) holder).like_link.setText((likes_c + ""));
                            Glide.with(context).load(R.drawable.like).into(((MyViewHolder) holder).like_icon);
                            ((MyViewHolder) holder).like_count.setText(context.getResources().getString(R.string.Like));
                            ((MyViewHolder) holder).like_count.setTextColor((context.getResources().getColor(R.color.white)));

                        } else {
                            data.setIs_liked_by_me(true);
                            likes_c++;
                            data.setCount_post_likes(String.valueOf(likes_c));
                            ((MyViewHolder) holder).like_link.setText((likes_c + ""));
                            ((MyViewHolder) holder).like_count.setText(context.getResources().getString(R.string.Liked));
                            ((MyViewHolder) holder).like_count.setTextColor((context.getResources().getColor(R.color.liked_color)));
                            Glide.with(context).load(R.drawable.like_active).into(((MyViewHolder) holder).like_icon);
                        }
                        listeners.onLikeClickListener(data);
                        updatePost(data.getId(), position);

                    }
                });

                ((MyViewHolder) holder).dislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int likes_c = Integer.parseInt(data.getCount_post_wonders());
                        if (data.getIs_wondered_by_me()) {
                            data.setIs_wondered_by_me(false);
                            likes_c--;
                            data.setCount_post_wonders(String.valueOf(likes_c));
                            ((MyViewHolder) holder).dislike_link.setText((likes_c + ""));
                            Glide.with(context).load(R.drawable.dislike).into(((MyViewHolder) holder).dislike_icon);
                            ((MyViewHolder) holder).dislike_count.setTextColor((context.getResources().getColor(R.color.white)));
                        } else {
                            data.setIs_wondered_by_me(true);
                            likes_c++;
                            data.setCount_post_wonders(String.valueOf(likes_c));
                            ((MyViewHolder) holder).dislike_link.setText((likes_c + ""));
                            ((MyViewHolder) holder).dislike_count.setTextColor((context.getResources().getColor(R.color.disliked_color)));
                            Glide.with(context).load(R.drawable.dislike_active).into(((MyViewHolder) holder).dislike_icon);
                        }

                        listeners.onDislikeClickListener(data);
                        updatePost(data.getId(), position);
                    }
                });


                ((MyViewHolder) holder).user_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listeners.onProfileClickListener(data);
                    }
                });
                ((MyViewHolder) holder).user_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listeners.onProfileClickListener(data);
                    }
                });
                // MyWork
                ((MyViewHolder) holder).like_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, LikeDislike.class);
                        i.putExtra(LikeDislike.Type, context.getString(R.string.likes));
                        i.putExtra(LikeDislike.PostId, data.getId());
                        context.startActivity(i);
                    }
                });
                ((MyViewHolder) holder).dislike_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, LikeDislike.class);
                        i.putExtra(LikeDislike.Type, context.getString(R.string.dislikes));
                        i.putExtra(LikeDislike.PostId, data.getId());
                        context.startActivity(i);
                    }
                });


                break;
            }
            default:
                /*-----------------------------------------------------------------------------------
                ------------------------------------ FOR TEXT/LINK/YOUTUBE --------------------------
                -----------------------------------------------------------------------------------*/
            {
                ((MyImageViewHolder) holder).user_name.setText(Global.capitalizeFirstLatterOfString(data.getPublisherName()));
                Glide.with(context).load(data.getPublisherAvatar()).dontAnimate().into(((MyImageViewHolder) holder).user_image);
                ((MyImageViewHolder) holder).post_time.setText(data.getTime_text());

                if (getItemViewType(position) == TYPE_LINK) {
                    ((MyImageViewHolder) holder).post_link.setVisibility(View.VISIBLE);
                    ((MyImageViewHolder) holder).post_link_image.setVisibility(View.VISIBLE);
                    //((MyImageViewHolder) holder).post_text.setVisibility(View.VISIBLE);
                    ((MyImageViewHolder) holder).post_content.setVisibility(View.VISIBLE);

                    ((MyImageViewHolder) holder).post_status.setText(context.getResources().getString(R.string.Shared_a_new_Link));
                    ((MyImageViewHolder) holder).post_link.setText(data.getPostText_API());
                    //((MyImageViewHolder) holder).post_link.setLinkTextColor(Color.GREEN);
                    //((MyImageViewHolder) holder).post_link.setPaintFlags(((MyImageViewHolder) holder).post_link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    if (!data.getPostFile().isEmpty()) {
                        Glide.with(context).load(data.getPostFile()).dontAnimate()
                                .into(((MyImageViewHolder) holder).post_link_image);
                    } else {
                        ((MyImageViewHolder) holder).post_link_image.setVisibility(View.GONE);
                    }
                    ((MyImageViewHolder) holder).post_link_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageViewDialog(data.getPostFile());
                        }
                    });
                    ((MyImageViewHolder) holder).post_content.setText(data.getPostLinkContent());

                } else if (getItemViewType(position) == TYPE_YOUTUBE) {
                    ((MyImageViewHolder) holder).post_status.setText(context.getResources().getString(R.string.Shared_a_YouTube_Video));
                    ((MyImageViewHolder) holder).youtube_view.setVisibility(View.VISIBLE);
                    ((MyImageViewHolder) holder).post_link_image.setVisibility(View.GONE);
                    ((MyImageViewHolder) holder).youtube_view.initialize(
                            new YouTubePlayerInitListener() {
                                @Override
                                public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {
                                    initializedYouTubePlayer.addListener(
                                            new AbstractYouTubePlayerListener() {
                                                @Override
                                                public void onReady() {
                                                    initializedYouTubePlayer.cueVideo(getVideoKey(data.getPostLink()), 0);
                                                }
                                            });
                                }
                            }, true);

                } else {
                    ((MyImageViewHolder) holder).post_status.setText(context.getResources().getString(R.string.Shared_a_Message));
                    ((MyImageViewHolder) holder).post_link_image.setVisibility(View.VISIBLE);
                    if (!data.getPostFile().isEmpty()) {
                        Glide.with(context).load(data.getPostFile()).dontAnimate()
                                .into(((MyImageViewHolder) holder).post_link_image);
                    } else {
                        ((MyImageViewHolder) holder).post_link_image.setVisibility(View.GONE);
                    }

                    ((MyImageViewHolder) holder).post_link_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageViewDialog(data.getPostFile());
                        }
                    });
                    if (data.getPostText_API().equalsIgnoreCase("")) {
                        ((MyImageViewHolder) holder).post_text.setVisibility(View.GONE);
                        ((MyImageViewHolder) holder).post_text_autolink.setVisibility(View.GONE);
                    } else {

                        String text = data.getPostText_API().replace("\n", "<br>");

                        if (data.getTagsusers().isEmpty()) {
                            ((MyImageViewHolder) holder).post_text.setVisibility(View.VISIBLE);
                            ((MyImageViewHolder) holder).post_text_autolink.setVisibility(View.GONE);
                            ((MyImageViewHolder) holder).post_text.setText(data.getPostText_API());
                        } else {
                            ((MyImageViewHolder) holder).post_text.setVisibility(View.GONE);
                            ((MyImageViewHolder) holder).post_text_autolink.setVisibility(View.VISIBLE);
                            ((MyImageViewHolder) holder).post_text_autolink.addAutoLinkMode(
                                    AutoLinkMode.MODE_HASHTAG,
                                    AutoLinkMode.MODE_PHONE,
                                    AutoLinkMode.MODE_URL,
                                    AutoLinkMode.MODE_EMAIL,
                                    AutoLinkMode.MODE_MENTION);
                            ((MyImageViewHolder) holder).post_text_autolink.setAutoLinkText(Global.capitalizeFirstLatterOfString(data.getPostText_API()));
                            ((MyImageViewHolder) holder).post_text_autolink.setHashtagModeColor(ContextCompat.getColor(context, R.color.app_light_red));
                            ((MyImageViewHolder) holder).post_text_autolink.setPhoneModeColor(ContextCompat.getColor(context, R.color.app_light_red));
                            ((MyImageViewHolder) holder).post_text_autolink.setMentionModeColor(ContextCompat.getColor(context, R.color.app_light_red));
                            ((MyImageViewHolder) holder).post_text_autolink.setUrlModeColor(ContextCompat.getColor(context, R.color.app_light_red));
                            ((MyImageViewHolder) holder).post_text_autolink.setEmailModeColor(ContextCompat.getColor(context, R.color.app_light_red));

                            ((MyImageViewHolder) holder).post_text_autolink.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
                                @Override
                                public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                                    String match = matchedText.trim();
                                    for (int i = 0; i < data.getTagsusers().size(); i++) {
                                        // Toaster.customToast(match + "/" + tagModelArrayList.get(i).getUser_id() + "?" + tagModelArrayList.get(i).getMatch_name());
                                        if (match.equalsIgnoreCase(data.getTagsusers().get(i).getMatch_name().trim())) {
                                            if (data.getPublisher_type().equalsIgnoreCase("user")) {
                                                Intent intent = new Intent(context, UserDetails.class);
                                                intent.putExtra("user_id", data.getTagsusers().get(i).getUser_id());
                                                intent.putExtra("FROM", "3");
                                                context.startActivity(intent);
                                                context.finish();
                                            } else {
                                                Intent intent = new Intent(context, PagesDetails.class);
                                                intent.putExtra("page_id", data.getPage_id());
                                                intent.putExtra("FROM", "3");
                                                context.startActivity(intent);
                                                context.finish();

                                            }

                                        }
                                    }


                                }
                            });

                        }
                    }
                }
                ((MyImageViewHolder) holder).post_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, WebView_Activity.class);
                        i.putExtra(WebView_Activity.WebURL, data.getPostLink());
                        context.startActivity(i);
                    }
                });


                ((MyImageViewHolder) holder).post_image.setVisibility(View.GONE);
                ((MyImageViewHolder) holder).multi_img.setVisibility(View.GONE);


                if (data.getCount_post_comments().equalsIgnoreCase("0") || data.getCount_post_comments().equalsIgnoreCase("1")) {
                    ((MyImageViewHolder) holder).comment_count.setText((data.getCount_post_comments() + " " + context.getString(R.string.comments)));
                } else {
                    ((MyImageViewHolder) holder).comment_count.setText((data.getCount_post_comments() + " " + context.getString(R.string.commentss)));
                }

                if (Integer.parseInt(data.getCount_post_likes()) > 0) {
                    ((MyImageViewHolder) holder).like_link.setVisibility(View.VISIBLE);
                    ((MyImageViewHolder) holder).like_link.setText((data.getCount_post_likes()));
                } else {
                    ((MyImageViewHolder) holder).like_link.setVisibility(View.GONE);
                }
                if (Integer.parseInt(data.getCount_post_wonders()) > 0) {
                    ((MyImageViewHolder) holder).dislike_link.setVisibility(View.VISIBLE);
                    ((MyImageViewHolder) holder).dislike_link.setText((data.getCount_post_wonders()));
                } else {
                    ((MyImageViewHolder) holder).dislike_link.setVisibility(View.GONE);
                }

                // new work by me
                if (data.getIs_liked_by_me()) {
                    Glide.with(context).load(R.drawable.like_active).into(((MyImageViewHolder) holder).like_icon);
                    ((MyImageViewHolder) holder).like_count.setText(context.getResources().getString(R.string.Liked));
                    ((MyImageViewHolder) holder).like_count.setTextColor(context.getResources().getColor(R.color.liked_color));
                } else {
                    Glide.with(context).load(R.drawable.like).into(((MyImageViewHolder) holder).like_icon);
                    ((MyImageViewHolder) holder).like_count.setText(context.getResources().getString(R.string.Like));
                    ((MyImageViewHolder) holder).like_count.setTextColor(context.getResources().getColor(R.color.white));
                }
                if (data.getIs_wondered_by_me()) {
                    Glide.with(context).load(R.drawable.dislike_active).into(((MyImageViewHolder) holder).dislike_icon);
                    ((MyImageViewHolder) holder).dislike_count.setTextColor(context.getResources().getColor(R.color.disliked_color));
                } else {
                    Glide.with(context).load(R.drawable.dislike).into(((MyImageViewHolder) holder).dislike_icon);
                    ((MyImageViewHolder) holder).dislike_count.setTextColor(context.getResources().getColor(R.color.white));
                }

                ((MyImageViewHolder) holder).post_options.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.getPublisher_type().equalsIgnoreCase("page")) {
                            if (data.getPublisher().getPageModel().getIs_page_onwer()) {
                                showFilterPopupDelete(v, arrayList_image.get(position));
                            } else {
                                showFilterPopup(v, arrayList_image.get(position));
                            }
                        } else {
                            if (data.getPublisherId().equals(SessionManager.get_user_id(prefs))) {
                                showFilterPopupDelete(v, arrayList_image.get(position));
                            } else {
                                showFilterPopup(v, arrayList_image.get(position));
                            }
                        }

                    }
                });

                ((MyImageViewHolder) holder).comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listeners.onCommentClickListener(data);
                    }
                });
                ((MyImageViewHolder) holder).share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listeners.onShareClickListener(data);
                    }
                });
                ((MyImageViewHolder) holder).like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int likes_c = Integer.parseInt(data.getCount_post_likes());
                        if (data.getIs_liked_by_me()) {
                            data.setIs_liked_by_me(false);
                            likes_c--;
                            data.setCount_post_likes(String.valueOf(likes_c));
                            ((MyImageViewHolder) holder).like_count.setText(context.getResources().getString(R.string.Like));
                            ((MyImageViewHolder) holder).like_link.setText((String.valueOf(likes_c)));
                            Glide.with(context).load(R.drawable.like).into(((MyImageViewHolder) holder).like_icon);
                            //((MyImageViewHolder) holder).like_count.setTextColor((context.getResources().getColor(R.color.white)));
                        } else {
                            data.setIs_liked_by_me(true);
                            likes_c++;
                            data.setCount_post_likes(String.valueOf(likes_c));
                            ((MyImageViewHolder) holder).like_link.setText((String.valueOf(likes_c)));
                            ((MyImageViewHolder) holder).like_count.setText(context.getResources().getString(R.string.Liked));
                            ((MyImageViewHolder) holder).like_link.setTextColor((context.getResources().getColor(R.color.liked_color)));
                            Glide.with(context).load(R.drawable.like_active).into(((MyImageViewHolder) holder).like_icon);
                            // ((MyImageViewHolder) holder).like_count.setTextColor((context.getResources().getColor(R.color.liked_color)));
                        }

                        listeners.onLikeClickListener(data);
                        updatePost(data.getId(), position);
                    }
                });
                ((MyImageViewHolder) holder).dislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int likes_c = Integer.parseInt(data.getCount_post_wonders());
                        if (data.getIs_wondered_by_me()) {
                            data.setIs_wondered_by_me(false);
                            likes_c--;
                            data.setCount_post_wonders(String.valueOf(likes_c));
                            ((MyImageViewHolder) holder).dislike_link.setText((String.valueOf(likes_c)));
                            Glide.with(context).load(R.drawable.dislike).into(((MyImageViewHolder) holder).dislike_icon);
                            ((MyImageViewHolder) holder).dislike_count.setTextColor((context.getResources().getColor(R.color.liked_color)));
                        } else {
                            data.setIs_wondered_by_me(true);
                            likes_c++;
                            data.setCount_post_wonders(String.valueOf(likes_c));
                            ((MyImageViewHolder) holder).dislike_link.setText((String.valueOf(likes_c)));
                            Glide.with(context).load(R.drawable.dislike_active).into(((MyImageViewHolder) holder).dislike_icon);
                            ((MyImageViewHolder) holder).dislike_count.setTextColor((context.getResources().getColor(R.color.disliked_color)));
                        }
                        listeners.onDislikeClickListener(data);
                        updatePost(data.getId(), position);
                    }
                });
                ((MyImageViewHolder) holder).user_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (!data.getPublisherId().equalsIgnoreCase(SessionManager.get_user_id(prefs)))
                        listeners.onProfileClickListener(data);

                    }
                });
                ((MyImageViewHolder) holder).user_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listeners.onProfileClickListener(data);

                    }
                });
                ((MyImageViewHolder) holder).like_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, LikeDislike.class);
                        i.putExtra(LikeDislike.Type, context.getString(R.string.likes));
                        i.putExtra(LikeDislike.PostId, data.getId());
                        context.startActivity(i);
                    }
                });
                ((MyImageViewHolder) holder).dislike_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, LikeDislike.class);
                        i.putExtra(LikeDislike.Type, context.getString(R.string.dislikes));
                        i.putExtra(LikeDislike.PostId, data.getId());
                        context.startActivity(i);
                    }
                });
                break;
            }
        }
    }

    private ArrayList<String> getKeyList(ArrayList<ImageModel> arrayList) {
        ArrayList<String> list = new ArrayList<>();
        for (ImageModel image : arrayList) {
            list.add(image.getImage());
        }
        return list;
    }

    @Override
    public int getItemCount() {
        return arrayList_image.size();
    }


    void imageViewDialog(String url) {

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.profiledialog);
        dialog.setCancelable(true);
        PhotoView img = (PhotoView) dialog.findViewById(R.id.image_view);
        ImageView del = (ImageView) dialog.findViewById(R.id.del);
//        img.setScale(3);
        Glide.with(context).load(url)
                .into(img);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Display anchored popup menu based on view selected
    private void showFilterPopupDelete(View v, final NewPostModel feed) {
        PopupMenu popup = new PopupMenu(context, v,Gravity.END);
        // Inflate the menu from xml
        popup.inflate(R.menu.popup_filter_del);

        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        listeners.onDeleteFeedListener(feed.getId());
                        return true;
                    /*case R.id.menu_edit:
                        listeners.onEditFeedListener(feed.getId(),feed.getPostText());
                        return true;*/
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();
    }

    private void showFilterPopup(View v, final NewPostModel feed) {
        PopupMenu popup = new PopupMenu(context, v,Gravity.END);
        // Inflate the menu from xml
        popup.inflate(R.menu.popup_filter);
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_report:
                        ReportDialog(feed.getId());
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();
    }

    public void ReportDialog(final String id) {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_with_edittext);
        final EditText input = (EditText) dialog.findViewById(R.id.editxt);
        TextView ok = dialog.findViewById(R.id.ok);
//        TextView cancel = dialog.findViewById(R.id.cancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.validateLength(input.getText().toString(), 5)) {
                    input.setError(context.getResources().getString(R.string.Enter_your_reason_to_report_this_post));
                } else {
                    input.setError(null);
                    listeners.onReportFeedListener(id, input.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    private String getVideoKey(String url) {
        String key = "";
        if (url.contains("v=")) {
            key = url.substring(url.lastIndexOf("v=") + 2);
        } else {
            key = url.substring(url.lastIndexOf("/") + 1);
        }
        Timber.tag("Youtube Key :- ").e(key);
        return key;
    }

    public class MyViewHolder extends AAH_CustomViewHolder {
        ImageView img_vol, img_playback;
        boolean isMuted; //to mute/un-mute video (optional)
        CircleImageView user_image;
        ImageView post_options, like_icon, dislike_icon;
        TextView user_name, user_address, post_status, post_time, like_count, dislike_count, comment_count, share_count, post_text;
        TextView like_link, dislike_link;
        LinearLayout comment, share, like, dislike;
        MaterialCardView relative_dash;
        AutoLinkTextView post_text_autolink;

        MyViewHolder(View x) {
            super(x);
            relative_dash = x.findViewById(R.id.relative_dash);
            user_image =  x.findViewById(R.id.post_user_image);
            user_name = x.findViewById(R.id.post_user_name);
            post_status =  x.findViewById(R.id.post_status);
            post_time =  x.findViewById(R.id.post_time);
            post_text =  x.findViewById(R.id.post_text);
            post_text_autolink = x.findViewById(R.id.post_text_autolink);
            post_options =  x.findViewById(R.id.post_options);
//                post_image = (ImageView) x.findViewById(R.id.post_image);
            like_count =  x.findViewById(R.id.p);
            dislike_count =  x.findViewById(R.id.q);
            comment_count = x.findViewById(R.id.v);
            share_count =  x.findViewById(R.id.c);
            like_icon =  x.findViewById(R.id.like_icon);
            dislike_icon =  x.findViewById(R.id.dislike_icon);
            comment =  x.findViewById(R.id.comment);
            share =  x.findViewById(R.id.share);
            like =  x.findViewById(R.id.like);
            dislike =  x.findViewById(R.id.dislike);
            user_address =  x.findViewById(R.id.user_address);
            img_vol = x.findViewById(R.id.img_vol);
            img_playback =  x.findViewById(R.id.img_playback);
            like_link =  x.findViewById(R.id.like_link);
            dislike_link =  x.findViewById(R.id.dislike_link);
//          img_playback = (ImageView) x.findViewById(R.id.img_playback);

        }

        //override this method to get callback when video starts to play
        @Override
        public void videoStarted() {
            try {
                super.videoStarted();
                img_playback.setImageResource(R.drawable.ic_pause);
                if (isMuted) {
                    muteVideo();
                    img_vol.setImageResource(R.drawable.ic_mute);
                } else {
                    unmuteVideo();
                    img_vol.setImageResource(R.drawable.ic_unmute);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void pauseVideo() {
            super.pauseVideo();
            img_playback.setImageResource(R.drawable.ic_play);
        }
    }

    public class MyImageViewHolder extends AAH_CustomViewHolder {
        CircleImageView user_image;
        ImageView post_options, like_icon, dislike_icon, post_image, dark_filter;
        TextView user_name, user_address, post_status, post_time, like_count, dislike_count, comment_count, share_count, post_text;
        TextView post_link, post_content;
        TextView like_link, dislike_link;
        ImageView post_link_image;
        YouTubePlayerView youtube_view;
        LinearLayout comment, share, like, dislike;
        ViewPager viewPager;

        RelativeLayout multi_img;
        MaterialCardView relative_dash;
        ImageView new_image;
        TextView img_count;
        AutoLinkTextView post_text_autolink;

        public MyImageViewHolder(View x) {
            super(x);
//            img_playback = ButterKnife.findById(x, R.id.img_playback);

            relative_dash = x.findViewById(R.id.relative_dash);
            multi_img =  x.findViewById(R.id.multi_img);
            user_image =  x.findViewById(R.id.post_user_image);
            user_name =  x.findViewById(R.id.post_user_name);
            post_status =  x.findViewById(R.id.post_status);
            post_time =  x.findViewById(R.id.post_time);
            post_text =  x.findViewById(R.id.post_text);
            post_text_autolink = x.findViewById(R.id.post_text_autolink);
            post_options =  x.findViewById(R.id.post_options);
            post_image =  x.findViewById(R.id.post_image);
            dark_filter =  x.findViewById(R.id.dark_filter);
            new_image =  x.findViewById(R.id.new_image);
            img_count =  x.findViewById(R.id.img_count);
            like_count =  x.findViewById(R.id.p);
            dislike_count =  x.findViewById(R.id.q);
            comment_count =  x.findViewById(R.id.v);
            share_count =  x.findViewById(R.id.c);
            like_icon =  x.findViewById(R.id.like_icon);
            dislike_icon =  x.findViewById(R.id.dislike_icon);
            comment =  x.findViewById(R.id.comment);
            share =  x.findViewById(R.id.share);
            like = x.findViewById(R.id.like);
            dislike =  x.findViewById(R.id.dislike);
            user_address =  x.findViewById(R.id.user_address);
            viewPager = x.findViewById(R.id.viewPager);
            post_link_image =  x.findViewById(R.id.post_link_image);
            post_link =  x.findViewById(R.id.post_link);
            post_content =  x.findViewById(R.id.post_content);
            youtube_view = x.findViewById(R.id.youtube_view);
            like_link =  x.findViewById(R.id.like_link);
            dislike_link =  x.findViewById(R.id.dislike_link);
        }
    }

    public void updatePost(String Post_id, int pos) {
        RequestQueue queue = Volley.newRequestQueue(context);
        Global.Sleep(1);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_post_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e("response:  %s", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                NewPostModel object;
                                object = NewPostModel.fromJson(jsonObject.getJSONObject("post_data"));

                                arrayList_image.set(pos, object);
                                notifyItemChanged(pos);

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(context, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(context, context.getResources().getString(R.string.error_server));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                    Global.msgDialog(context, context.getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("post_id", Post_id);
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

}