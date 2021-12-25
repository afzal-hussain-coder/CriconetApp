package com.pb.criconet.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.allattentionhere.autoplayvideos.AAH_VideoImage;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.adapters.MyCustomPagerAdapter;
import com.pb.criconet.fragments.FeedsPhotos;
import com.pb.criconet.models.CommentModel;
import com.pb.criconet.models.ImageModel;
import com.pb.criconet.models.NewPostModel;
import com.pb.criconet.models.TagModel;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

import static com.pb.criconet.Utills.Global.TYPE_IMAGE;
import static com.pb.criconet.Utills.Global.TYPE_LINK;
import static com.pb.criconet.Utills.Global.TYPE_TEXT;
import static com.pb.criconet.Utills.Global.TYPE_VIDEO;
import static com.pb.criconet.Utills.Global.TYPE_YOUTUBE;


public class FeedDetails extends AppCompatActivity {
    SharedPreferences prefs;
    RecyclerView comment_list;
    ProgressDialog progress;
    RequestQueue queue;
    ArrayList<CommentModel> modelArrayList;
    Comments_Adapter adapter;
    TextView notfound;
    int page = 1;

    String feed_id;
    EditText edtxt_comment;
    ImageView image_send;

    TextView post_link, post_content;
    AutoLinkTextView post_text;
    ImageView post_link_image, post_image, img_playback, img_vol;
    RelativeLayout multi_img;
    ViewPager viewPager;
    YouTubePlayerView youtube_view;
    AAH_VideoImage AAH_video;
    VideoView videoview;
    NewPostModel data;
    Context mContext;
    MediaController mediaController;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_details);
        mContext = this;
        mediaController = new MediaController(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedDetails.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView toolbartext = (TextView) toolbar.findViewById(R.id.toolbartext);
        toolbartext.setText(R.string.comments);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            feed_id = bundle.getString("feed_id");
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(FeedDetails.this);
        queue = Volley.newRequestQueue(FeedDetails.this);
        progress = new ProgressDialog(FeedDetails.this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);


        post_link =  findViewById(R.id.post_link);
        post_text =  findViewById(R.id.post_text);
        post_content = findViewById(R.id.post_content);
        comment_list = findViewById(R.id.comment_list);
        comment_list.setHasFixedSize(true);
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        post_link_image = (ImageView) findViewById(R.id.post_link_image);
        post_image = (ImageView) findViewById(R.id.post_image);
        img_playback = (ImageView) findViewById(R.id.img_playback);
        img_vol = (ImageView) findViewById(R.id.img_vol);
        multi_img = (RelativeLayout) findViewById(R.id.multi_img);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        youtube_view = (YouTubePlayerView) findViewById(R.id.youtube_view);
        //AAH_video = (AAH_VideoImage)findViewById(R.id.AAH_video);
        videoview = (VideoView)findViewById(R.id.videoview);
        videoview.setMediaController(mediaController);

        edtxt_comment = (EditText) findViewById(R.id.edtxt_comment);
        image_send = (ImageView) findViewById(R.id.image_send_comment);
        notfound = (TextView) findViewById(R.id.notfound);

        image_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtxt_comment.getText().toString().trim().length() > 0) {
                    postComment(edtxt_comment.getText().toString());
                }
            }
        });

        getFeedDetails();


    }


    public void getFeedDetails() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_post_data", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        JSONObject array = jsonObject.getJSONObject("post_data");
                        data = NewPostModel.fromJson(array);
                        setPostData(data);
                        modelArrayList = new ArrayList<>();
                        modelArrayList.addAll(data.getGet_post_comments());
                        adapter = new Comments_Adapter(FeedDetails.this,data.getPublisher_type(),data.getPage_id(), modelArrayList);
                        comment_list.setAdapter(adapter);

                        //adapter.notifyDataSetChanged();

                        if (modelArrayList.size() == 0) {
                            notfound.setVisibility(View.VISIBLE);
                        } else {
                            notfound.setVisibility(View.GONE);
                        }
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(FeedDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(FeedDetails.this, getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progress.dismiss();
                Global.msgDialog(FeedDetails.this, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", feed_id);
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public int getItemViewType(NewPostModel data) {

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

    private void setPostData(final NewPostModel data) {
        switch (getItemViewType(data)) {
            case TYPE_IMAGE:
                /*-----------------------------------------------------------------------------------
                ------------------------------------ FOR IMAGE/MULTI IMAGE --------------------------
                -----------------------------------------------------------------------------------*/
            {

                if (data.getPost_type().equalsIgnoreCase("photo_multi")) {
                    if (getKeyList(data.getPhoto_multi()).size() > 0) {
                        multi_img.setVisibility(View.VISIBLE);
                        post_image.setVisibility(View.GONE);

                        ArrayList<String> temp = getKeyList(data.getPhoto_multi());

                        MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter(FeedDetails.this, temp);
                        viewPager.setAdapter(myCustomPagerAdapter);
                        // Disable clip to padding
                        viewPager.setClipToPadding(false);
                        // set padding manually, the more you set the padding the more you see of prev & next page
                        viewPager.setPadding(50, 0, 50, 0);
                        // sets a margin b/w individual pages to ensure that there is a gap b/w them
                        viewPager.setPageMargin(20);
                    }
                } else {
                    multi_img.setVisibility(View.GONE);
                    post_image.setVisibility(View.VISIBLE);
                    Glide.with(FeedDetails.this).load(data.getPostFile()).into(post_image);
                }

                multi_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> photos = new ArrayList<>();
                        photos.addAll(getKeyList(data.getPhoto_multi()));
                        Fragment fragment = new FeedsPhotos();
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("photos", photos);
                        fragment.setArguments(bundle);

                    }
                });

//                post_time.setText(data.getTime_text());
                if (data.getPostText().equalsIgnoreCase("")) {
                    post_text.setVisibility(View.GONE);
                } else {
                    post_text.setVisibility(View.VISIBLE);
                    ///new work today

//                  String postData=data.getPos;
//                    temp = postData.split("@");
//
//                    for(int i=0;i<data.getGet_post_comments().size();i++){
//                        for(int i=0;i<){
//
//                        }
//                    }


                    String text = data.getPostText().replace("\n", "<br>");
                   // post_text.setText(Html.fromHtml(text));
                    post_text.addAutoLinkMode(
                            AutoLinkMode.MODE_HASHTAG,
                            AutoLinkMode.MODE_PHONE,
                            AutoLinkMode.MODE_URL,
                            AutoLinkMode.MODE_MENTION,
                            AutoLinkMode.MODE_CUSTOM);
                    post_text.setAutoLinkText(Global.capitalizeFirstLatterOfString(data.getPostText_API()));

                    ArrayList<TagModel>tagModelArrayList=data.getTagsusers();
                    //Toaster.customToast(tagModelArrayList.size()+"");
                    post_text.setSelectedStateColor(ContextCompat.getColor(this, R.color.yourColor));
                    post_text.setHashtagModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_green));
                    post_text.setPhoneModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_green));
                    post_text.setCustomModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_green));
                    post_text.setUrlModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_green));
                    post_text.setMentionModeColor(ContextCompat.getColor(FeedDetails.this, R.color.yourColor));
                    post_text.setEmailModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_green));

                    post_text.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
                        @Override
                        public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                               String matched=matchedText.trim();
                            for(int i=0;i<tagModelArrayList.size();i++) {
                                //Toaster.customToast(matched+"/"+tagModelArrayList.get(i).getMatch_name().trim());
                                if (matched.trim().equalsIgnoreCase(tagModelArrayList.get(i).getMatch_name().trim())) {
                                    if (data.getPublisher_type().equalsIgnoreCase("user")) {
                                        Intent intent = new Intent(FeedDetails.this, UserDetails.class);
                                        intent.putExtra("user_id", tagModelArrayList.get(i).getUser_id());
                                        intent.putExtra("FROM","1");
                                        intent.putExtra("feed_id", feed_id);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(FeedDetails.this, PagesDetails.class);
                                        intent.putExtra("page_id", data.getPage_id());
                                        intent.putExtra("feed_id", feed_id);
                                        intent.putExtra("FROM","1");
                                        startActivity(intent);

                                    }

                                }
                            }

                        }
                    });
                }

                if (data.getPost_type().equalsIgnoreCase("Image")) {
                    Glide.with(FeedDetails.this).load(data.getPostFile()).into(post_image);
                } else {
                    Glide.with(FeedDetails.this).load(data.getPostFile()).into(post_image);
                }

                break;
            }
            case TYPE_VIDEO:
                /*-----------------------------------------------------------------------------------
                ------------------------------------ FOR VIDEO --------------------------------------
                -----------------------------------------------------------------------------------*/
            {

                if (data.getPostText().equalsIgnoreCase("")) {
                    post_text.setVisibility(View.GONE);
                } else {
                    post_text.setVisibility(View.VISIBLE);
                    String text = data.getPostText().replace("\n", "<br>");
                   // post_text.setText(Html.fromHtml(text));



                    post_text.addAutoLinkMode(
                            AutoLinkMode.MODE_HASHTAG,
                            AutoLinkMode.MODE_PHONE,
                            AutoLinkMode.MODE_URL,
                            AutoLinkMode.MODE_EMAIL,
                            AutoLinkMode.MODE_MENTION);
                    post_text.setAutoLinkText(Global.capitalizeFirstLatterOfString(data.getPostText_API()));
                    ArrayList<TagModel>tagModelArrayList=data.getTagsusers();
                    //Toaster.customToast(tagModelArrayList.size()+"");
                    post_text.setSelectedStateColor(ContextCompat.getColor(FeedDetails.this, R.color.yourColor));
                    post_text.setHashtagModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_green));
                    post_text.setPhoneModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_green));
                    post_text.setCustomModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_green));
                    post_text.setUrlModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_green));
                    post_text.setMentionModeColor(ContextCompat.getColor(FeedDetails.this, R.color.yourColor));
                    post_text.setEmailModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_green));
                    post_text.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
                        @Override
                        public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                            String matched=matchedText.trim();
                            for(int i=0;i<tagModelArrayList.size();i++) {
                               // Toaster.customToast(matched+"/"+tagModelArrayList.get(i).getMatch_name().trim());
                                if (matched.trim().equalsIgnoreCase(tagModelArrayList.get(i).getMatch_name().trim())) {
                                    if (data.getPublisher_type().equalsIgnoreCase("user")) {
                                        Intent intent = new Intent(FeedDetails.this, UserDetails.class);
                                        intent.putExtra("user_id", tagModelArrayList.get(i).getUser_id());
                                        intent.putExtra("FROM","1");
                                        intent.putExtra("feed_id", feed_id);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(FeedDetails.this, PagesDetails.class);
                                        intent.putExtra("page_id", data.getPage_id());
                                        intent.putExtra("FROM","1");
                                        intent.putExtra("feed_id", feed_id);
                                        startActivity(intent);

                                    }

                                }
                            }

                        }
                    });
                }

                if (data.getThumb() != null && !data.getThumb().isEmpty()) {
                    videoview.setVisibility(View.VISIBLE);
                    Uri uri = Uri.parse(data.getPostFile());
                    videoview.setVideoURI(uri);
                    videoview.start();
                }

//
//                //to play pause videos manually (optional)
//                img_playback.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            if (videoview.isPlaying()) {
//                                videoview.stopPlayback();
//                                //setPaused(true);
//                            } else {
//                                videoview.start();
//                                //playVideo();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//                //to mute/un-mute video (optional)
//                img_vol.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        if (isMuted) {
////                            unmuteVideo();
////                            img_vol.setImageResource(R.drawable.ic_unmute);
////                        } else {
////                            muteVideo();
////                            img_vol.setImageResource(R.drawable.ic_mute);
////                        }
////                        isMuted = !isMuted;
//                    }
//                });

//                if (data.getPostFile() == null) {
//                    img_vol.setVisibility(View.GONE);
//                    img_playback.setVisibility(View.GONE);
//                } else {
//                    img_vol.setVisibility(View.VISIBLE);
//                    img_playback.setVisibility(View.VISIBLE);
//                }

                break;
            }
            default:
                /*-----------------------------------------------------------------------------------
                ------------------------------------ FOR TEXT/LINK/YOUTUBE --------------------------
                -----------------------------------------------------------------------------------*/
            {

                if (getItemViewType(data) == TYPE_LINK) {
                    post_link.setVisibility(View.VISIBLE);
                    post_link_image.setVisibility(View.VISIBLE);
                    post_text.setVisibility(View.VISIBLE);
                    post_content.setVisibility(View.VISIBLE);

                    post_link.setText(Html.fromHtml(data.getPostLink().replace("\n", "<br>")));
                    if (!data.getPostLinkImage().isEmpty()) {
                        Glide.with(FeedDetails.this).load(data.getPostLinkImage())
                                .into(post_link_image);
                    } else {
                        post_link_image.setVisibility(View.GONE);
                    }
                    post_content.setText(Html.fromHtml(data.getPostLinkContent().replace("\n", "<br>")));

                } else if (getItemViewType(data) == TYPE_YOUTUBE) {
                    youtube_view.setVisibility(View.VISIBLE);
                    youtube_view.initialize(new YouTubePlayerInitListener() {
                        @Override
                        public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {
                            initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                                @Override
                                public void onReady() {
                                    initializedYouTubePlayer.cueVideo(getVideoKey(data.getPostLink()), 0);
                                }
                            });
                        }
                    }, true);
                }

                post_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(FeedDetails.this, WebView_Activity.class);
                        i.putExtra(WebView_Activity.WebURL, data.getPostLink());
                        FeedDetails.this.startActivity(i);
                    }
                });

                if (data.getPostText().equalsIgnoreCase("")) {
                    post_text.setVisibility(View.GONE);
                } else {
                    post_text.setVisibility(View.VISIBLE);
                    String text = data.getPostText().replace("\n", "<br>");
                    //post_text.setText(Html.fromHtml(text));

                    post_text.addAutoLinkMode(
                            AutoLinkMode.MODE_HASHTAG,
                            AutoLinkMode.MODE_PHONE,
                            AutoLinkMode.MODE_URL,
                            AutoLinkMode.MODE_EMAIL,
                            AutoLinkMode.MODE_MENTION);
                    post_text.setAutoLinkText(Global.capitalizeFirstLatterOfString(data.getPostText_API()));
                    ArrayList<TagModel>tagModelArrayList=data.getTagsusers();
                    //Toaster.customToast(tagModelArrayList.size()+"");
                    post_text.setSelectedStateColor(ContextCompat.getColor(FeedDetails.this, R.color.app_light_red));
                    post_text.setHashtagModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_light_red));
                    post_text.setPhoneModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_light_red));
                    post_text.setCustomModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_light_red));
                    post_text.setUrlModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_light_red));
                    post_text.setMentionModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_light_red));
                    post_text.setEmailModeColor(ContextCompat.getColor(FeedDetails.this, R.color.app_light_red));
                    post_text.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
                        @Override
                        public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                            String matched=matchedText.trim();
                            for(int i=0;i<tagModelArrayList.size();i++) {
                                //Toaster.customToast(matched+"/"+tagModelArrayList.get(i).getMatch_name().trim());
                                if (matched.trim().equalsIgnoreCase(tagModelArrayList.get(i).getMatch_name().trim())) {
                                    if (data.getPublisher_type().equalsIgnoreCase("user")) {
                                        Intent intent = new Intent(FeedDetails.this, UserDetails.class);
                                        intent.putExtra("user_id", tagModelArrayList.get(i).getUser_id());
                                        intent.putExtra("FROM","1");
                                        intent.putExtra("feed_id", feed_id);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(FeedDetails.this, PagesDetails.class);
                                        intent.putExtra("page_id", data.getPage_id());
                                        intent.putExtra("FROM","1");
                                        intent.putExtra("feed_id", feed_id);
                                        startActivity(intent);

                                    }

                                }
                            }

                        }
                    });
                }

                post_image.setVisibility(View.GONE);
                multi_img.setVisibility(View.GONE);

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

    public void postComment(final String comment) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "comment_on_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        edtxt_comment.setText("");
                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(FeedDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(FeedDetails.this, getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progress.dismiss();
                Global.msgDialog(FeedDetails.this, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                "user_id:1735
//                s:1
//                post_id:8832
//                text:comment"
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", feed_id);
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("text", comment);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    public void DeleteComment(final String id) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "delete_comment", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        edtxt_comment.setText("");
                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(FeedDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(FeedDetails.this, getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progress.dismiss();
                Global.msgDialog(FeedDetails.this, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                "user_id:1735
//                s:1
//                comment_id:872"

                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("comment_id", id);
//                param.put("post_id", feed_id);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
    public void ReportDialog(final String id) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(FeedDetails.this);
        alertbox.setTitle(FeedDetails.this.getResources().getString(R.string.app_name));
        alertbox.setMessage(getResources().getString(R.string.Are_you_sure_you_want_to_Report_this_feed));

        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null);

        final EditText input = (EditText) dialogLayout.findViewById(R.id.editxt);
        alertbox.setView(dialogLayout);

        alertbox.setPositiveButton(FeedDetails.this.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (input.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(FeedDetails.this, getResources().getString(R.string.Enter_Reason_First), Toast.LENGTH_SHORT).show();
                        } else {
                            ReportFeed(id, input.getText().toString());
                        }
                    }
                });
        alertbox.setNegativeButton(FeedDetails.this.getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        alertbox.show();
    }
    public void ReportFeed(final String id, final String message) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "report_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        Global.msgDialog(FeedDetails.this, getResources().getString(R.string.Post_reported_Successfully));
//                        Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(FeedDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(FeedDetails.this, getResources().getString(R.string.error_server));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progress.dismiss();
                Global.msgDialog(FeedDetails.this, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("post_id", id);
                param.put("report_text", message);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    public void ResetFeed() {
        if (Global.isOnline(FeedDetails.this)) {
//            getComments();
            getFeedDetails();
            System.out.println("xxxxxxxx getFeed " + page + "xxxxxxxxxxxxxxx");
        } else {
            Global.showDialog(FeedDetails.this);
        }
    }

//    public class Comments_Adapter extends BaseAdapter {
//        Activity context;
//        ViewHolder holder = null;
//        LayoutInflater inflater;
//        ArrayList<CommentModel> arrayList_image;
//
//        Comments_Adapter(Activity mcontext, ArrayList<CommentModel> chatname_list1) {
//            // TODO Auto-generated constructor stub
//            context = mcontext;
//            arrayList_image = chatname_list1;
//            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//        }
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return arrayList_image.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            return position;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return position;
//        }
//
//        @SuppressLint("ViewHolder")
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            // TODO Auto-generated method stub
//            if (convertView == null) {
//                convertView = inflater.inflate(R.layout.comment_adapter_item, null);
//                holder = new ViewHolder(convertView);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//                Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
//                holder.relative_dash.startAnimation(scaleUp);
//            }
//
//            final CommentModel comment = arrayList_image.get(position);
//
//            //String name; type; feeds_type; image; videothums; video; time; profile_id; username; userimage;
//            holder.user_name.setText(comment.getUser_name());
//            holder.user_comment.setText(comment.getComment());
//            holder.time.setText(Global.getTimeAgo(Long.parseLong(comment.getTime())));
//            Glide.with(context).load(comment.getUser_image()).into(holder.user_image);
//            if (comment.getUser_id().equals(SessionManager.get_user_id(prefs))) {
//                holder.delete_cmnt.setVisibility(View.VISIBLE);
//            } else {
//                holder.delete_cmnt.setVisibility(View.GONE);
//            }
//
//            holder.delete_cmnt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
//                    alertbox.setTitle(getResources().getString(R.string.app_name));
//                    alertbox.setMessage("Do you want to Delete this Comment ?");
//                    alertbox.setPositiveButton("Yes",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface arg0, int arg1) {
//                                    DeleteComment(comment.getId());
//                                }
//                            });
//                    alertbox.setNegativeButton("No",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface arg0, int arg1) {
//                                }
//                            });
//                    alertbox.show();
//                }
//            });
//
//
//            return convertView;
//        }
//
//        private class ViewHolder {
//            ImageView user_image, delete_cmnt;
//            TextView user_name, user_comment, time;
//            RelativeLayout relative_dash;
//
//            ViewHolder(View x) {
//                relative_dash = (RelativeLayout) x.findViewById(R.id.relative_dash);
//                user_image = (ImageView) x.findViewById(R.id.user_image);
//                delete_cmnt = (ImageView) x.findViewById(R.id.delete_cmnt);
//                user_name = (TextView) x.findViewById(R.id.user_name);
//                user_comment = (TextView) x.findViewById(R.id.user_comment);
//                time = (TextView) x.findViewById(R.id.time);
//            }
//        }
//
//    }

    public class Comments_Adapter extends RecyclerView.Adapter<Comments_Adapter.ViewHolder> {

        ArrayList<CommentModel> arrayList_image;
        Context mContext;
        String publisherType, pageId;

        public Comments_Adapter(Context context,String publisherType,String pageId, ArrayList<CommentModel> arrayList_imagee) {

            arrayList_image = arrayList_imagee;
            mContext = context;
            this.publisherType=publisherType;
            this.pageId=pageId;


        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView user_image, delete_cmnt;
            TextView user_name, time;
            AutoLinkTextView user_comment;
            RelativeLayout relative_dash;
            RecyclerView rec_tag;


            public ViewHolder(View x) {
                super(x);

                relative_dash = (RelativeLayout) x.findViewById(R.id.relative_dash);
                user_image = (ImageView) x.findViewById(R.id.user_image);
                delete_cmnt = (ImageView) x.findViewById(R.id.delete_cmnt);
                user_name = (TextView) x.findViewById(R.id.user_name);
                user_comment = x.findViewById(R.id.user_comment);
                time = (TextView) x.findViewById(R.id.time);
                rec_tag = x.findViewById(R.id.rec_tag);
                rec_tag.setHasFixedSize(true);
                //rec_tag.setLayoutManager(new GridLayoutManager(this, 0));
                rec_tag.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true));
            }
        }

        @Override
        public Comments_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.comment_adapter_item, parent, false);

            return new Comments_Adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final CommentModel comment = arrayList_image.get(position);
            ArrayList<TagModel>tagModelArrayList=comment.getTagsusers();

            holder.user_name.setText(Global.capitizeString(comment.getUser_name()));

            holder.user_comment.addAutoLinkMode(
                    AutoLinkMode.MODE_HASHTAG,
                    AutoLinkMode.MODE_PHONE,
                    AutoLinkMode.MODE_URL,
                    AutoLinkMode.MODE_EMAIL,
                    AutoLinkMode.MODE_MENTION);
            holder.user_comment.setAutoLinkText(Global.capitalizeFirstLatterOfString(comment.getComment()));
            holder.user_comment.setHashtagModeColor(ContextCompat.getColor(mContext, R.color.app_light_red));
            holder.user_comment.setPhoneModeColor(ContextCompat.getColor(mContext, R.color.app_light_red));
            holder.user_comment.setCustomModeColor(ContextCompat.getColor(mContext, R.color.app_light_red));
            holder.user_comment.setMentionModeColor(ContextCompat.getColor(mContext, R.color.app_light_red));
            holder.user_comment.setUrlModeColor(ContextCompat.getColor(mContext, R.color.app_light_red));

            holder.user_comment.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
                @Override
                public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                    String match =matchedText.trim();
                    for(int i=0;i<tagModelArrayList.size();i++) {
                       // Toaster.customToast(match + "/" + tagModelArrayList.get(i).getUser_id() + "?" + tagModelArrayList.get(i).getMatch_name());
                        if (match.equalsIgnoreCase(tagModelArrayList.get(i).getMatch_name().trim())) {
                            if (publisherType.equalsIgnoreCase("user")) {
                                Intent intent = new Intent(mContext, UserDetails.class);
                                intent.putExtra("user_id", tagModelArrayList.get(i).getUser_id());
                                intent.putExtra("FROM","1");
                                intent.putExtra("feed_id", feed_id);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(mContext, PagesDetails.class);
                                intent.putExtra("page_id", pageId);
                                intent.putExtra("FROM","1");
                                intent.putExtra("feed_id", feed_id);
                                startActivity(intent);
                                finish();

                            }

                        }
                    }


                }
            });
            holder.time.setText(Global.getTimeAgo(Long.parseLong(comment.getTime())));
            Glide.with(mContext).load(comment.getUser_image()).into(holder.user_image);
            if (comment.getUser_id().equals(SessionManager.get_user_id(prefs))) {
                holder.delete_cmnt.setVisibility(View.VISIBLE);
            } else {
                holder.delete_cmnt.setVisibility(View.GONE);
            }

            holder.delete_cmnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(mContext);
                    alertbox.setTitle(getResources().getString(R.string.app_name));
                    alertbox.setMessage(getResources().getString(R.string.Do_you_want_to_Delete_this_Comment));
                    alertbox.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    DeleteComment(comment.getId());
                                }
                            });
                    alertbox.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                }
            });
            Animation scaleUp = AnimationUtils.loadAnimation(mContext, R.anim.list_anim_side);
            holder.relative_dash.startAnimation(scaleUp);

        }

        @Override
        public int getItemCount() {

            return arrayList_image.size();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FeedDetails.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
