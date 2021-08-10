package com.pb.criconet.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.activity.FeedDetails;
import com.pb.criconet.activity.MainActivity;
import com.pb.criconet.activity.PagesDetails;
import com.pb.criconet.activity.PostListeners;
import com.pb.criconet.R;
import com.pb.criconet.activity.UserDetails;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.MultipartRequest;
import com.pb.criconet.Utills.RecycleViewPaginationScrollListener;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.adapters.HomeAdapter;
import com.pb.criconet.lazyload.AndroidMultiPartEntity;
import com.pb.criconet.models.NewPostModel;
import com.pb.criconet.retrofit.ApiInterfaceService;
import com.pb.criconet.retrofit.ResultObject;
import com.pb.criconet.retrofit.VideoInterface;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.pb.criconet.Utills.Global.POST_PRIVACY_PRIVATE;
import static com.pb.criconet.Utills.Global.POST_PRIVACY_PUBLIC;
import static com.pb.criconet.Utills.Global.POST_TYPE_IMAGE;
import static com.pb.criconet.Utills.Global.POST_TYPE_LINK;
import static com.pb.criconet.Utills.Global.POST_TYPE_MULTI_IMAGE;
import static com.pb.criconet.Utills.Global.POST_TYPE_TEXT;
import static com.pb.criconet.Utills.Global.POST_TYPE_VIDEO;
import static com.pb.criconet.Utills.Global.POST_TYPE_YOUTUBE;
import static com.pb.criconet.Utills.Global.PRIVACY_EVERYONE;
import static com.pb.criconet.Utills.Global.PRIVACY_MY_FOLLOWERS;
import static com.pb.criconet.Utills.Global.PRIVACY_ONLY_ME;
import static com.pb.criconet.Utills.Global.PRIVACY_PEOPLE_I_FOLLOW;

//import com.facebook.drawee.view.SimpleDraweeView;


public class HomeFragment extends Fragment implements BSImagePicker.OnMultiImageSelectedListener, PostListeners {
    private static final int CAMERA_REQUEST_id = 2015;
    private static final int PICK_IMAGE_id = 100;
    private static final int CAPTURE_VIDEO = 3015;
    private static final int PICK_VIDEO = 300;
    private View rootView;
    private SharedPreferences prefs;
    private AAH_CustomRecyclerView post_list;
    private ProgressDialog progress;
    private RequestQueue queue;
    public ArrayList<NewPostModel> modelArrayList;
    private HomeAdapter adapter;
    private ImageView user_image, up_image;
    private RelativeLayout add_photo, add_video, add_chat;
    private TextView notfound;
    private EditText up_text;
    private String feedText = "";
    private String postType = "";
    private Cursor cursor;
    private int columnindex, i;
    private Uri URIid = null;
    private Uri selectedImageid, mCapturedImageURIid;
    private String file_pathid = "", image_pathid = "";
    private String postFile = "";
    private String filemanagerstring = " ";
    private byte[] byteArray;
    private String after_post_id = "0";
    private Switch privacy_setting;
    private TextView privacy;
    private int postPrivacy = 0;
    private ImageView link_image;
    private RelativeLayout link_layout;
    private TextView link_title, link_content;
    private Spinner spn_privacy;
    private String url_link, url_title, url_content, url_image;
    private ProgressBar progressBar;
    CustomLoaderView loaderView;
    int page = 1;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<String> images;
    private boolean isLoading = false;
    // total no. of pages to load. Initial load is page 0, after which
    // If current page is the last page (Pagination will stop after this page load)
    private boolean isLastPage = false;
    private long totalSize = 0;
    private SlideUp slideUp;
    private View dim, rootViewPost;
    private View slideView;
    private TextView tv_camera;
    private TextView tvGallery;
    private TextView tvCancel;
    private TextView tv_choose;
    private LinearLayout send_panel;
    private TextView tv_post;
    private ImageView img_addpost,img_close;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        if (rootView != null) {
//            ViewGroup parent = (ViewGroup) container.getParent();
//            if (parent != null)
//                parent.removeView(container);
//        }
        try {
            rootView = inflater.inflate(R.layout.home_fragment, container, false);


            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
            tv_post = toolbar.findViewById(R.id.tv_post);
            img_addpost = toolbar.findViewById(R.id.img_addpost);
            img_addpost.setVisibility(View.VISIBLE);
            img_close = toolbar.findViewById(R.id.img_close);

            img_addpost.setOnClickListener(v -> {
                send_panel.setVisibility(View.VISIBLE);
                img_addpost.setVisibility(View.GONE);
                img_close.setVisibility(View.VISIBLE);

            });

            img_close.setOnClickListener(v -> {
                img_close.setVisibility(View.GONE);
                send_panel.setVisibility(View.GONE);
                img_addpost.setVisibility(View.VISIBLE);
            });
            mTitle.setText(R.string.home);

            loaderView = CustomLoaderView.initialize(getActivity());
            prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            queue = Volley.newRequestQueue(getActivity());
//            progress = new ProgressDialog(getActivity());
//            progress.setMessage(getString(R.string.loading));
//            progress.setCancelable(false);

            initializeView(rootView);

        } catch (InflateException e) {
            Timber.e(e);
            return rootView;
        }
        return rootView;
    }

    private void initializeView(View rootView) {
        rootViewPost = rootView.findViewById(R.id.root_view);
        slideView = rootView.findViewById(R.id.slideView);
        dim = rootView.findViewById(R.id.dim);
        tv_camera=rootView.findViewById(R.id.tv_camera);
        tvGallery=rootView.findViewById(R.id.tvGallery);
        tvCancel=rootView.findViewById(R.id.tvCancel);
        tv_choose=rootView.findViewById(R.id.tv_choose);
        send_panel = rootView.findViewById(R.id.send_panel);
        slideUp = new SlideUpBuilder(slideView)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                        dim.setAlpha(1 - (percent / 100));
                        if (percent < 100) {
                            // slideUp started showing

                        }
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                        }
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(rootView)
                .build();
        tvCancel.setOnClickListener(v -> {
            slideUp.hide();
        });

        post_list =  rootView.findViewById(R.id.post_list);
        user_image =rootView.findViewById(R.id.user_image);
        up_image =  rootView.findViewById(R.id.up_image);
        add_photo =  rootView.findViewById(R.id.add_photo);
        add_video = rootView.findViewById(R.id.add_video);
        progressBar =  rootView.findViewById(R.id.progressBar);
        add_chat =  rootView.findViewById(R.id.add_chat);
        notfound =  rootView.findViewById(R.id.notfound);
        up_text =  rootView.findViewById(R.id.up_text);
        privacy_setting =rootView.findViewById(R.id.privacy_setting);
        privacy =  rootView.findViewById(R.id.privacy);
        link_image =  rootView.findViewById(R.id.link_image);
        link_layout =  rootView.findViewById(R.id.link_layout);
        link_title =  rootView.findViewById(R.id.link_title);
        link_content =  rootView.findViewById(R.id.link_content);
        spn_privacy =  rootView.findViewById(R.id.spn_privacy);



        if (!SessionManager.get_image(prefs).isEmpty()){
            Glide.with(getActivity()).load(SessionManager.get_image(prefs)).into(user_image);
        }else{
            Glide.with(getActivity()).load(getResources().getDrawable(R.drawable.user_default)).into(user_image);
        }
        ResetFeed();

        up_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Timber.e("%s", s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    tv_post.setVisibility(View.VISIBLE);
                    img_close.setVisibility(View.GONE);
                    if (!postType.equalsIgnoreCase(POST_TYPE_IMAGE) &&
                            !postType.equalsIgnoreCase(POST_TYPE_MULTI_IMAGE) &&
                            !postType.equalsIgnoreCase(POST_TYPE_VIDEO)) {
                        if (s.toString().startsWith("https://") || s.toString().startsWith("http://")) {
                            if (s.toString().contains("youtube") || s.toString().contains("youtu.be")) {
                                postType = POST_TYPE_YOUTUBE;
                            } else {
                                postType = POST_TYPE_LINK;
                                getURLDetails(s.toString());
                            }
                        } else {
                            postType = POST_TYPE_TEXT;
                        }
                    }
                } else {

                    if (!postType.equalsIgnoreCase("Image") && !postType.equalsIgnoreCase("multi_image") && !postType.equalsIgnoreCase("Video")) {
                        postType = "";
                        tv_post.setVisibility(View.GONE);
                        img_close.setVisibility(View.VISIBLE);
                        //setHasOptionsMenu(false);
                    }
                }
//                Toast.makeText(getActivity(), postType, Toast.LENGTH_SHORT).show();
            }

        });

        post_list.addOnScrollListener(new RecycleViewPaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                    page++;
                after_post_id = modelArrayList.get(modelArrayList.size() - 1).getId();
                getFeed();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

        });

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postType = POST_TYPE_IMAGE;
                selectImage();

            }
        });
        add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postType = POST_TYPE_VIDEO;
                selectVideo();
            }
        });

        privacy_setting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    privacy.setText("Public");
                    postPrivacy = POST_PRIVACY_PUBLIC;
                } else {
                    privacy.setText("Private");
                    postPrivacy = POST_PRIVACY_PRIVATE;
                }
            }
        });


        tv_post.setOnClickListener(v -> {

            feedText = up_text.getText().toString().trim();
            if (postType.equals(POST_TYPE_VIDEO)) {
                //uploadVideoToServer(feedText);
                PostFeedFinal(feedText);
                //new UploadFileToServer().execute();
            } else {
                PostFeedFinal(feedText);
            }

        });

//            add_chat.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    getFragmentManager().beginTransaction().replace(R.id.frame_container, new PrivateChatList())
////                            .addToBackStack(null).commit();
//                }
//            });
//            getProfileDetails();
    }

    //    onPause
    @Override
    public void onPause() {
        super.onPause();
        //add this code to pause videos (when app is minimised or paused)
//        post_list.stopVideos();
    }

    @Override
    public void onStop() {
        super.onStop();
        post_list.stopVideos();
    }

    @Override
    public void onResume() {
        super.onResume();


//        MainActivity.bottomNavigation.setSelectedIndex(0);
        try{
            MainActivity.bottomNavigation.setSelectedIndex(0, true);
            tv_post.setVisibility(View.GONE);
            img_close.setVisibility(View.GONE);
//            if(tv_post.getVisibility()==View.VISIBLE){
//                tv_post.setVisibility(View.GONE);
//            }
//            if(img_close.getVisibility()==View.VISIBLE){
//                img_close.setVisibility(View.GONE);
////        MainActivity.bottomNavigation.setMenuItemCount(0)
//            };
        }catch(Exception e){
            e.printStackTrace();
        }

//        post_list.playAvailableVideos(0);
    }

    private void checkPrivacy() {
        switch (spn_privacy.getSelectedItemPosition()) {
            case 0:
                postPrivacy = PRIVACY_EVERYONE;
                break;
            case 1:
                postPrivacy = PRIVACY_ONLY_ME;
                break;
            case 2:
                postPrivacy = PRIVACY_PEOPLE_I_FOLLOW;
                break;
            case 3:
                postPrivacy = PRIVACY_MY_FOLLOWERS;
                break;
            default:
                postPrivacy = PRIVACY_EVERYONE;
                break;
        }
    }

    private void selectImage() {
        slideUp.show();
        tv_choose.setText(R.string.Choose_Post_Photo);
        tv_camera.setOnClickListener(v -> {
            slideUp.hide();
            openCamera();
        });
        tvGallery.setOnClickListener(v -> {
            slideUp.hide();
            multiGallery();
        });
        tvCancel.setOnClickListener(v -> {
            postType = "";
            slideUp.hide();
        });
    }

    private void selectVideo() {
        slideUp.show();
        tv_choose.setText(R.string.choose_post_video);
        tv_camera.setOnClickListener(v -> {
            slideUp.hide();
            openCameraVideo();
        });
        tvGallery.setOnClickListener(v -> {
            slideUp.hide();
            openGalleryVideo();
        });
        tvCancel.setOnClickListener(v -> {
            postType = "";
            slideUp.hide();
        });
    }

    private void openCamera() {

        try {
            String fileName = "profile.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURIid = getActivity().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURIid);
            startActivityForResult(intent, CAMERA_REQUEST_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void multiGallery() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        BSImagePicker pickerDialog = new BSImagePicker.Builder("com.pb.criconet.provider")
                .setMaximumDisplayingImages(Integer.MAX_VALUE)
                .isMultiSelect()
//                .setMinimumMultiSelectCount(1)
                .setMaximumMultiSelectCount(10)
//                .hideCameraTile()
                .setPeekHeight(metrics.heightPixels)
                .build();
//        pickerDialog.setCancelable(false);
        pickerDialog.show(getChildFragmentManager(), "Select Pictures");
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList) {
        Timber.e(uriList.toString());
        images = new ArrayList<>();
        for (int i = 0; i < uriList.size(); i++) {
            images.add(uriList.get(i).getPath());
        }

        postFile = images.get(0);
        up_image.setVisibility(View.VISIBLE);
        up_image.setImageURI(Uri.parse(postFile));
        postType = POST_TYPE_MULTI_IMAGE;
        if (images.size() == 1) {
            postType = POST_TYPE_IMAGE;
            postFile = images.get(0);
        }
        setHasOptionsMenu(true);
//            Glide.with(this).load(uriList.get(i)).into(iv);
    }

    private void openCameraVideo() {
        File saveFolder = new File(Environment.getExternalStorageDirectory(), "Utopiaxxx");
        try {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);

            if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, CAPTURE_VIDEO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGalleryVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_id) {
                try {
                    selectedImageid = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    cursor = getActivity().getContentResolver().query(selectedImageid, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    columnindex = cursor.getColumnIndex(filePathColumn[0]);
                    file_pathid = cursor.getString(columnindex);
                    URIid = Uri.parse("file://" + file_pathid);
                    postFile = file_pathid;
                    cursor.close();
                    System.out.println("cccccccc   " + postFile);
                    up_image.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(postFile).into(up_image);
                    setHasOptionsMenu(true);
                    slideUp.hide();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == CAMERA_REQUEST_id) {
                try {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    @SuppressWarnings("deprecation")
                    Cursor cursor = getActivity().managedQuery(mCapturedImageURIid, projection, null, null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String capturedImageFilePath = cursor.getString(column_index_data);
                    postFile = capturedImageFilePath;

                    up_image.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(postFile).into(up_image);

                    System.out.println("cccccccc   " + postFile);
                    setHasOptionsMenu(true);
                    slideUp.hide();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_VIDEO) {
                Uri selectedImageUri = data.getData();

                Cursor cursor = null;
                try {
                    String[] proj = { MediaStore.Images.Media.DATA };
                    CursorLoader loader = new CursorLoader(getActivity(), selectedImageUri, proj, null, null, null);
                    cursor = loader.loadInBackground();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String file_path = cursor.getString(column_index);

                    filemanagerstring = file_path;
                    postFile = filemanagerstring;
                    cursor.close();
                    System.out.println("selectedVideoPath y " + filemanagerstring);
//                dialog_camera.dismiss();
                    if (filemanagerstring != null) {
                        loaderView.showLoader();
                        //progress.show();
                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(file_path, MediaStore.Images.Thumbnails.MINI_KIND);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        thumb.compress(Bitmap.CompressFormat.PNG, 40, stream);
                        byteArray = stream.toByteArray();
//                    uploadVideo_taskimage(Global.URL, filemanagerstring, byteArray);
                        try {
                            FileBody filebodyVideo = new FileBody(new File(filemanagerstring));
                            long kblength = new File(filemanagerstring).length();
                            kblength = kblength / 1024;
                            long mblength = kblength / 1024;
                            System.out.println("file.mblength() = " + mblength);
                        if (mblength > 51) {
                            System.out.println("file.length() = " + mblength);
                            Global.msgDialog(getActivity(), "File Size Too Large, \n Must be less than 50 MB");
                            //progress.dismiss();
                            loaderView.hideLoader();
                        } else {
                            up_image.setVisibility(View.VISIBLE);
                            up_image.setImageBitmap(thumb);
                            //progress.dismiss();
                            loaderView.hideLoader();
                            setHasOptionsMenu(true);
                        }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    slideUp.hide();

                } catch (Exception e){
                    e.printStackTrace();
                }

                    finally
                 {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                // Log.e("Attachment Path:", attachmentFile);

            } else if (requestCode == CAPTURE_VIDEO) {
                Uri selectedVideo = data.getData();

                String[] filePathColumn = {MediaStore.Video.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnindex = cursor.getColumnIndex(filePathColumn[0]);
                filemanagerstring = cursor.getString(columnindex);
                cursor.close();
                System.out.println("filemanagerstring x  " + filemanagerstring);
                postFile = filemanagerstring;
//                dialog_camera.dismiss();
                if (filemanagerstring != null) {
                    //progress.show();
                    loaderView.showLoader();
//                    Bitmap bitmap = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
                    Bitmap video_thumbnail = ThumbnailUtils.createVideoThumbnail(filemanagerstring, MediaStore.Video.Thumbnails.MINI_KIND);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    video_thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    byteArray = stream.toByteArray();
//                    uploadVideo_taskimage(Global.URL, filemanagerstring, byteArray);
                    try {
                        FileBody filebodyVideo = new FileBody(new File(filemanagerstring));
                        long kblength = new File(filemanagerstring).length();
                        kblength = kblength / 1024;
                        long mblength = kblength / 1024;
                        System.out.println("file.mblength() = " + mblength);
                        if (mblength > 51) {
                            System.out.println("file.length() = " + mblength);
                            Global.msgDialog(getActivity(), "File Size Too Large,\n Must be less than 50 MB");
                            //progress.dismiss();
                            loaderView.hideLoader();
                        } else {
                            up_image.setVisibility(View.VISIBLE);
                            up_image.setImageBitmap(video_thumbnail);
                            //progress.dismiss();
                            loaderView.hideLoader();
                            setHasOptionsMenu(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                slideUp.hide();
            }

        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getFeed() {
        //progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "home_posts", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               Log.d("homeResponse",response);
                loaderView.hideLoader();
                //progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        JSONArray array = jsonObject.getJSONArray("posts");
                        if (array.length() < 1) {
                            isLastPage = true;
                        }
                        modelArrayList.addAll(NewPostModel.fromJson(array));
//                                Timber.e(modelArrayList.toString());

                        isLoading = false;
                        adapter.notifyDataSetChanged();

                        if (after_post_id.equals("0")) {
//                                if (page == 1) {
                            post_list.smoothScrollBy(0, 1);
                            post_list.smoothScrollBy(0, -1);
                            if (modelArrayList.size() == 0) {
                                notfound.setVisibility(View.VISIBLE);
                            } else {
                                notfound.setVisibility(View.GONE);
                            }
                        }

                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(getActivity(), "Error in server");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //progress.dismiss();
                loaderView.hideLoader();
                Global.msgDialog(getActivity(), "Error from server");
//                Global.msgDialog(Login.this, "Internet connection is slow");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                //            "user_id:1735
                //            s:1
                //            after_post_id:0"
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("after_post_id", after_post_id);
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

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.home_menu, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_send:
//                send_panel.setVisibility(View.GONE);
//                setHasOptionsMenu(false);
//                feedText = up_text.getText().toString().trim();
//                if (postType.equals(POST_TYPE_VIDEO)) {
//                    uploadVideoToServer(feedText);
//                    //new UploadFileToServer().execute();
//                } else {
//                    PostFeedFinal(feedText);
//                }
//                break;
//            case R.id.action_post:
//                send_panel.setVisibility(View.VISIBLE);
//                break;
//            default:
//                super.onOptionsItemSelected(item);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void DeleteFeed(final String id) {
        //progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "delete_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(" %s", response);
                //progress.dismiss();
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(getActivity(), "Error in server");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //progress.dismiss();
                loaderView.hideLoader();
                Global.msgDialog(getActivity(), "Error from server");
//                Global.msgDialog(Login.this, "Internet connection is slow");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                "user_id:1735
//                s:1
//                post_id:8754"
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
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

    private void shareFeed(final String id) {
        //progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "delete_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                //progress.dismiss();
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(getActivity(), "Error in server");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //progress.dismiss();
                loaderView.hideLoader();
                Global.msgDialog(getActivity(), "Error from server");
//                Global.msgDialog(Login.this, "Internet connection is slow");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                "user_id:1735
//                s:1
//                post_id:8754"
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
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

    private void likeFeed(final String id) {
        //progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "like_on_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                loaderView.hideLoader();
                //progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
//                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(getActivity(), "Error in server");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //progress.dismiss();
                loaderView.hideLoader();
                Global.msgDialog(getActivity(), "Error from server");
//                Global.msgDialog(Login.this, "Internet connection is slow");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//        "user_id:1703
//        s:1
//        post_id:8939"
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
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

    private void dislikeFeed(final String id) {
       // progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "unlike_on_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                //progress.dismiss();
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
//                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(getActivity(), "Error in server");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //progress.dismiss();
                loaderView.hideLoader();
                Global.msgDialog(getActivity(), "Error from server");
//                Global.msgDialog(Login.this, "Internet connection is slow");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//        "user_id:1703
//        s:1
//        post_id:8939"
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("post_id", id);
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

    public void ReportFeed(final String id, final String message) {
       // progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "report_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                loaderView.hideLoader();
               // progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        Global.msgDialog(getActivity(), "Post reported Successfully.");
//                        Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(getActivity(), "Error in server");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                //progress.dismiss();
                Global.msgDialog(getActivity(), "Error from server");
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

    private void getURLDetails(final String url) {
        //progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "fetch_url", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Timber.e(String.valueOf(response));
                //progress.dismiss();
                loaderView.hideLoader();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
//                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {

                    postType = POST_TYPE_LINK;
                    url_title = jsonObject.getString("title");
                    url_link = jsonObject.getString("url");
                    url_content = jsonObject.getString("content");
                    url_image = jsonObject.getJSONArray("images").getString(0);

                    link_layout.setVisibility(View.VISIBLE);
                    link_title.setText(url_title);
                    link_content.setText(url_content);
                    Glide.with(getActivity()).load(url_image).into(link_image);

//                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
//                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
//                    } else {
//                        Global.msgDialog(getActivity(), "Error in server");
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Timber.e(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //progress.dismiss();
                loaderView.hideLoader();
                Global.msgDialog(getActivity(), "Error from server");
//                Global.msgDialog(Login.this, "Internet connection is slow");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("url", url);
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

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void PostFeedFinal(final String postText) {
        try {
            checkPrivacy();
            //progress.show();
            loaderView.showLoader();

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            Timber.e("Chuncked %b", entity.isChunked());
//            entity.addPart("s", new StringBody("1"));
            entity.addPart("user_id", new StringBody(SessionManager.get_user_id(prefs)));
            entity.addPart("s", new StringBody(SessionManager.get_session_id(prefs)));
            entity.addPart("postPrivacy", new StringBody(String.valueOf(postPrivacy))); //{0: public, 3 : only me}

            switch (postType) {
                case POST_TYPE_IMAGE:
                    entity.addPart("postText", new StringBody(postText));
                    if (!postFile.isEmpty()) {
                        File file = new File(postFile);
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postFile", fileBody);
                    }
                    break;
                case POST_TYPE_VIDEO:
                    entity.addPart("postText", new StringBody(postText));
                    if (!postFile.isEmpty()) {
                        File file = new File(postFile);
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postVideo", fileBody);
                        //                        iStream = getActivity().getContentResolver().openInputStream(Uri.parse(postFile));
//                        InputStream iStream = getActivity().getContentResolver().openInputStream(file.toURI());
//                        byte[] body = getBytes(iStream);
//                        entity.addPart("postVideo", new ByteArrayBody(body, "postVideo"));
                    }
                    break;
                case POST_TYPE_MULTI_IMAGE:
                    entity.addPart("postText", new StringBody(postText));
                    for (int j = 0; j < images.size(); j++) {
                        File file = new File(images.get(j));
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postPhotos[" + (j) + "]", fileBody);
                    }
                    break;
                case POST_TYPE_YOUTUBE:
                    entity.addPart("postText", new StringBody(postText));
                    break;
                case POST_TYPE_LINK:
                    entity.addPart("url_link", new StringBody(url_link));
                    entity.addPart("url_title", new StringBody(url_title));
                    entity.addPart("url_content", new StringBody(url_content));
                    entity.addPart("postText", new StringBody(postText));
                    entity.addPart("url_image", new StringBody(url_image));
//                    if (!(url_image.equals(""))) {
//                        File file = new File(url_image);
//                        FileBody fileBody = new FileBody(file);
//                        entity.addPart("url_image", fileBody);
//                    }
                    break;
                case POST_TYPE_TEXT:
                    // POST_TYPE_TEXT
                    if (!postFile.isEmpty()) {
                        File file = new File(postFile);
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postFile", fileBody);
                    }
                    entity.addPart("postText", new StringBody(postText));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + postType);
            }

            MultipartRequest req = new MultipartRequest(Global.URL + "new_post",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //progress.dismiss();
                                loaderView.hideLoader();
                                up_text.setText("");
                                up_image.setImageURI(null);
                                Timber.e(response);
                                JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                                if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {
//                            JSONArray array = jsonObject.getJSONArray("posts");
                                    ResetFeed();
//                            Global.msgDialog(getActivity(), jsonObject.optString("msg"));
                                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                    Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                                } else {
                                    Global.msgDialog(getActivity(), "Error in server");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //progress.dismiss();
                            loaderView.hideLoader();
                            error.printStackTrace();
                        }
                    },
                    entity);

            Log.d("PostEntity",entity.toString());


            int socketTimeout = 50000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            queue.add(req);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private RequestBody bodyPart(String name) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), name);
    }

    private void uploadVideoToServer(final String postText) {

        try{
            File videoFile = new File(postFile);
//        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
//        RequestBody videoBody = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile);
            // RequestBody videoBody = RequestBody.create(MediaType.parse("application/octet-stream"), videoFile);
            RequestBody videoBody = RequestBody.create(MediaType.parse("application/octet-stream"), videoFile);
            MultipartBody.Part vFile = MultipartBody.Part.createFormData("postVideo", videoFile.getName(), videoBody);

            VideoInterface vInterface = ApiInterfaceService.getApiService();
            //VideoInterface vInterface = retrofit.create(VideoInterface.class);
            Call<ResultObject> serverCom = vInterface.uploadVideoToServer(vFile,
                    bodyPart(SessionManager.get_user_id(prefs)),
                    bodyPart(SessionManager.get_session_id(prefs)),
                    bodyPart(String.valueOf(postPrivacy)),
                    bodyPart(postText));

            //progress.show();
            loaderView.showLoader();

            serverCom.enqueue(new Callback<ResultObject>() {
                @Override
                public void onResponse(Call<ResultObject> call, retrofit2.Response<ResultObject> response) {
                    try {
                        //progress.dismiss();
                        loaderView.hideLoader();
//                        tv_post.setVisibility(View.GONE);
//                        img_addpost.setVisibility(View.VISIBLE);
                        Timber.e(String.valueOf(response));
                        ResultObject result = response.body();
                        Timber.e(result.toString());
                        JSONObject jsonObject2, jsonObject = new JSONObject(result.toString());
                        if (result.getApi_text().equalsIgnoreCase("Success")) {
//                            JSONArray array = jsonObject.getJSONArray("posts");
                            ResetFeed();
//                            Global.msgDialog(getActivity(), jsonObject.optString("msg"));
                        } else if (result.getApi_text().equalsIgnoreCase("failed")) {
                            Global.msgDialog(getActivity(), result.getApi_status());
//                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog(getActivity(), "Error in server");
                        }
                    } catch (Exception e) {
                        //progress.dismiss();
                        loaderView.hideLoader();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResultObject> call, Throwable t) {
                    //progress.dismiss();
                    loaderView.hideLoader();
                    Timber.e("Error message Home %s", t.getMessage());
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Uploading the file to server
     * */
    /*private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
           // txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Global.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                File sourceFile = new File(postFile);
                // Adding file data to http body
                entity.addPart("postVideo", new FileBody(sourceFile));
                // Extra parameters if you want to pass to server
                Log.e("RanjeetTest",">>>>>>>>"+SessionManager.get_user_id(prefs));
                Log.e("RanjeetTest",">>>>>>>>"+SessionManager.get_session_id(prefs));
                Log.e("RanjeetTest",">>>>>>>>"+String.valueOf(postPrivacy));
                Log.e("RanjeetTest",">>>>>>>>"+feedText);

                entity.addPart("user_id",new StringBody(SessionManager.get_user_id(prefs)));
                entity.addPart("s", new StringBody(SessionManager.get_session_id(prefs)));
                entity.addPart("postPrivacy", new StringBody(String.valueOf(postPrivacy)));
                entity.addPart("postText", new StringBody(feedText));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);



                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("RanjeetTest", "Response from server: " + result);

            // showing the server response in an alert dialog
            try {
                Gson gson = new Gson();
                ResultObject resultObject = gson.fromJson(result, ResultObject.class);
                if (resultObject.getApi_text().equalsIgnoreCase("Success")) {
                    ResetFeed();
                } else if (resultObject.getApi_text().equalsIgnoreCase("failed")) {
                    Global.msgDialog(getActivity(), resultObject.getApi_status());
                } else {
                    Global.msgDialog(getActivity(), "Error in server");
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }

            super.onPostExecute(result);
        }

    }*/

//    private void upload(final String pdfname, Uri pdffile) {
//        InputStream iStream = null;
//        try {
//            iStream = getActivity().getContentResolver().openInputStream(pdffile);
//            final byte[] inputData = getBytes(iStream);
//
//            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
//                    new Response.Listener<NetworkResponse>() {
//                        @Override
//                        public void onResponse(NetworkResponse response) {
//                            Log.d("ressssssoo", new String(response.data));
//                            rQueue.getCache().clear();
//                            try {
//                                progress.dismiss();
//                                Timber.e(String.valueOf(response.data));
//                                JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
//                                if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
////                            JSONArray array = jsonObject.getJSONArray("posts");
//                                    ResetFeed();
////                            Global.msgDialog(getActivity(), jsonObject.optString("msg"));
//                                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
//                                    Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
//                                } else {
//                                    Global.msgDialog(getActivity(), "Error in server");
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }) {
//
//                /*
//                 * If you want to add more parameters with the image
//                 * you can do it here
//                 * here we have only one parameter with the image
//                 * which is tags
//                 * */
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    // params.put("tags", "ccccc");  add string parameters
//                    return params;
//                }
//
//                /*
//                 *pass files using below method
//                 * */
//                @Override
//                protected Map<String, DataPart> getByteData() {
//                    Map<String, DataPart> params = new HashMap<>();
//
//                    params.put("filename", new DataPart(pdfname, inputData));
//                    return params;
//                }
//            };
//
//
//            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    0,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            rQueue = Volley.newRequestQueue(getActivity());
//            rQueue.add(volleyMultipartRequest);
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
    private void ResetFeed() {
        img_close.setVisibility(View.GONE);
        img_addpost.setVisibility(View.VISIBLE);
        send_panel.setVisibility(View.GONE);
        //up_text.setText("");
        feedText = "";
        postType = "";
        postFile = "";
        after_post_id = "0";

        isLoading = false;
        isLastPage = false;
        up_image.setVisibility(View.GONE);
        link_layout.setVisibility(View.GONE);

        modelArrayList = new ArrayList<>();
        adapter = new HomeAdapter(getActivity(), modelArrayList, this);
        mLayoutManager = new LinearLayoutManager(getActivity());

        post_list.setLayoutManager(mLayoutManager);
        post_list.setItemAnimator(new DefaultItemAnimator());

        post_list.setActivity(getActivity()); //todo before setAdapter
        //optional - to play only first visible video
        post_list.setPlayOnlyFirstVideo(true);
        // false by default
        //optional - by default we check if url ends with ".mp4". If your urls do not end with mp4, you can set this param to false and implement your own check to see if video points to url
//        post_list.setCheckForMp4(false); //true by default

        //optional - download videos to local storage (requires "android.permission.WRITE_EXTERNAL_STORAGE" in manifest or ask in runtime)
        //post_list.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo"); // (Environment.getExternalStorageDirectory() + "/Video") by default
        post_list.setDownloadVideos(true); // false by default
        post_list.setVisiblePercent(90); // percentage of View that needs to be visible to start playing

        post_list.setAdapter(adapter);
        //call this functions when u want to start autoplay on loading async lists (eg firebase)
        post_list.smoothScrollBy(0,1);
        post_list.smoothScrollBy(0,-1);

        if (Global.isOnline(getActivity())) {
            getFeed();
            System.out.println("xxxxxxxxxx getFeed " + after_post_id + "xxxxxxxxxx");
        } else {
            Global.showDialog(getActivity());
        }
    }


    @Override
    public void onLikeClickListener(NewPostModel post) {
        likeFeed(post.getId());
    }

    @Override
    public void onDislikeClickListener(NewPostModel post) {
        dislikeFeed(post.getId());
    }

    @Override
    public void onCommentClickListener(NewPostModel post) {
        Timber.e(post.toString());
        Intent intent = new Intent(getActivity(), FeedDetails.class);
        intent.putExtra("feed_id", post.getId());
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onShareClickListener(NewPostModel post) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, post.getDetails_url());
//        shareIntent.setType("text/html");
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    @Override
    public void onReportFeedListener(String id, String message) {
        ReportFeed(id, message);
    }

    @Override
    public void onDeleteFeedListener(String id) {
        DeleteFeed(id);
    }

    @Override
    public void onProfileClickListener(NewPostModel post) {
        if (post.getPublisher_type().equalsIgnoreCase("user")) {
            Intent intent = new Intent(getActivity(), UserDetails.class);
            intent.putExtra("user_id", post.getPublisherId());
            intent.putExtra("FROM","2");
            startActivity(intent);
            getActivity().finish();
        } else {
            Intent intent = new Intent(getActivity(), PagesDetails.class);
            intent.putExtra("page_id", post.getPublisherId());
            intent.putExtra("FROM","2");
            startActivity(intent);
            getActivity().finish();
        }
    }
}