package com.pb.criconet.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.MultipartRequest;
import com.pb.criconet.Utills.RecycleViewPaginationScrollListener;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.adapters.HomeAdapter;
import com.pb.criconet.models.NewPostModel;
import com.pb.criconet.models.PageModel;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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


public class PagesDetails extends AppCompatActivity implements BSImagePicker.OnMultiImageSelectedListener, PostListeners {
    private static final int CAMERA_REQUEST_id = 2015;
    private static final int PICK_IMAGE_id = 100;
    private static final int CAPTURE_VIDEO = 3015;
    private static final int PICK_VIDEO = 300;
    private ImageView user_image, up_image;
    private RelativeLayout add_photo, add_video, add_chat;
    private EditText up_text;
    private String feedText = "";
    private String postType = "";
    private TextView privacy;
    private int postPrivacy = 0;
    private ImageView link_image;
    private RelativeLayout link_layout;
    private TextView link_title, link_content;
    private Spinner spn_privacy;
    private Switch privacy_setting;
    private String postFile = "";
    private String url_link, url_title, url_content, url_image;
    private String filemanagerstring = " ";
    private byte[] byteArray;
    private Uri selectedImageid, mCapturedImageURIid;
    private Cursor cursor;
    private int columnindex, i;
    private Uri URIid = null;
    private String file_pathid = "", image_pathid = "";
    TextView up_post;


    private SharedPreferences prefs;
    private ProgressDialog progress;
    private RequestQueue queue;
    private String page_id, friendStatus;
    private TextView mTitle;
    private RoundedImageView ep_user_image;
    private RelativeLayout send_request;
    private TextView tv_name, tv_address, status, accept, reject;
    //    private RelativeLayout acc_rej;
    //    private UserModel user;
    private PageModel data;
    private ImageView cover;

    private AAH_CustomRecyclerView post_list;
    public ArrayList<NewPostModel> modelArrayList;
    private HomeAdapter adapter;
    private String after_post_id = "0";
    LinearLayoutManager mLayoutManager;
    ArrayList<String> images;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private TextView notfound;
    private LinearLayout send_panel;
    String from_where="",feed_id="";
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);

        prefs = PreferenceManager.getDefaultSharedPreferences(PagesDetails.this);
        queue = Volley.newRequestQueue(PagesDetails.this);
        progress = new ProgressDialog(PagesDetails.this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            page_id = bundle.getString("page_id");
            from_where = bundle.getString("FROM");
            feed_id=bundle.getString("feed_id");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from_where.equalsIgnoreCase("1")){
                    Intent intent = new Intent(PagesDetails.this, FeedDetails.class);
                    intent.putExtra("feed_id", feed_id);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(PagesDetails.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        initSendPost();

        post_list = (AAH_CustomRecyclerView) findViewById(R.id.post_list);
        ep_user_image = (RoundedImageView) findViewById(R.id.ep_user_image);
        send_request = (RelativeLayout) findViewById(R.id.send_request);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        status = (TextView) findViewById(R.id.p);
        cover = (ImageView) findViewById(R.id.rlt);
        send_panel = (LinearLayout) findViewById(R.id.send_panel);
        notfound = (TextView) findViewById(R.id.notfound);
        notfound.setText("Sorry No Post Found");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ep_user_image.setClipToOutline(true);
        }

        ep_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewDialog(data.getAvatar());
            }
        });


        if (Global.isOnline(PagesDetails.this)) {
            ResetFeed();
            System.out.println("xxxxxxxxxxxxxxxx UsersDetails xxxxxxxxxxxxxxx");
        } else {
            Global.showDialog(PagesDetails.this);
        }

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
    }

    private void initSendPost() {
        user_image = (ImageView) findViewById(R.id.user_image);
        up_image = (ImageView) findViewById(R.id.up_image);
        up_post = (TextView) findViewById(R.id.up_post);
        add_photo = (RelativeLayout) findViewById(R.id.add_photo);
        add_video = (RelativeLayout) findViewById(R.id.add_video);
        add_chat = (RelativeLayout) findViewById(R.id.add_chat);
        up_text = (EditText) findViewById(R.id.up_text);
        privacy_setting = (Switch) findViewById(R.id.privacy_setting);
        privacy = (TextView) findViewById(R.id.privacy);
        link_image = (ImageView) findViewById(R.id.link_image);
        link_layout = (RelativeLayout) findViewById(R.id.link_layout);
        link_title = (TextView) findViewById(R.id.link_title);
        link_content = (TextView) findViewById(R.id.link_content);
        spn_privacy = (Spinner) findViewById(R.id.spn_privacy);

        Glide.with(PagesDetails.this).load(SessionManager.get_image(prefs)).into(user_image);

        up_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    up_post.setVisibility(View.VISIBLE);
                    if (!postType.equalsIgnoreCase(POST_TYPE_IMAGE) &&
                            !postType.equalsIgnoreCase(POST_TYPE_MULTI_IMAGE) &&
                            !postType.equalsIgnoreCase(POST_TYPE_VIDEO)) {
                        if (s.toString().startsWith("https://") || s.toString().startsWith("http://")) {
                            if (s.toString().contains("youtube")) {
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
                        up_post.setVisibility(View.GONE);
                    }
                }
//                Toast.makeText(PagesDetails.this, postType, Toast.LENGTH_SHORT).show();
            }

        });

        up_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up_post.setVisibility(View.GONE);
                feedText = up_text.getText().toString().trim();
                PostFeedFinal(feedText);
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
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PagesDetails.this);
        builder.setTitle("Post Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    openCameraid();
                } else if (items[item].equals("Gallery")) {
//                    openGalleryid();
                    multiGalery();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    postType = "";
                }
            }
        });
        builder.show();
    }

    private void selectVideo() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PagesDetails.this);
        builder.setTitle("Post Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    openCameraVideo();
                } else if (items[item].equals("Gallery")) {
                    openGalleryVideo();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    postType = "";
                }
            }
        });
        builder.show();
    }

    private void openCameraid() {

        try {
            String fileName = "profile.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURIid = PagesDetails.this.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURIid);
            startActivityForResult(intent, CAMERA_REQUEST_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void multiGalery() {
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
//        pickerDialog.show(getChildFragmentManager(), "Select Pictures");
        pickerDialog.show(getSupportFragmentManager(), "Select Pictures");
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList) {
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
        up_post.setVisibility(View.VISIBLE);
//            Glide.with(this).load(uriList.get(i)).into(iv);
    }

    private void openCameraVideo() {
        File saveFolder = new File(Environment.getExternalStorageDirectory(), "Utopiaxxx");
        try {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);

            if (takeVideoIntent.resolveActivity(PagesDetails.this.getPackageManager()) != null) {
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_id) {
                try {
                    selectedImageid = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    cursor = PagesDetails.this.getContentResolver().query(selectedImageid, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    columnindex = cursor.getColumnIndex(filePathColumn[0]);
                    file_pathid = cursor.getString(columnindex);
                    // Log.e("Attachment Path:", attachmentFile);
                    URIid = Uri.parse("file://" + file_pathid);
                    postFile = file_pathid;

                    cursor.close();
                    Log.e("TAG", "1234789" + postFile);

                    if (resultCode == 0) {
//                    dialog_camera.dismiss();
                    } else {
//                    dialog_camera.dismiss();
                        System.out.println("cccccccc   " + postFile);
                        up_image.setVisibility(View.VISIBLE);
                        up_image.setImageURI(Uri.parse(postFile));
//                    updateImageTask(postFile);
                        up_post.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == CAMERA_REQUEST_id) {
                try {
                    String[] projection = {MediaStore.Images.Media.DATA};
                    @SuppressWarnings("deprecation")
                    Cursor cursor = PagesDetails.this.managedQuery(mCapturedImageURIid, projection, null, null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String capturedImageFilePath = cursor.getString(column_index_data);
                    postFile = capturedImageFilePath;

                    if (resultCode == 0) {
//                    dialog_camera.dismiss();
                    } else {
//                    dialog_camera.dismiss();

//                    profile_image.setImageURI(Uri.parse(imagepath));
                        up_image.setVisibility(View.VISIBLE);
                        Glide.with(PagesDetails.this).load(postFile).into(up_image);

                        System.out.println("cccccccc   " + postFile);
//                    updateImageTask(imagepath);
                        up_post.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_VIDEO) {
                Uri selectedImageUri = data.getData();
                String[] filePathColumn = {MediaStore.Video.Media.DATA};
                Cursor cursor = PagesDetails.this.getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnindex = cursor.getColumnIndex(filePathColumn[0]);
                String file_path = cursor.getString(columnindex);
                // Log.e("Attachment Path:", attachmentFile);
                filemanagerstring = file_path;
                postFile = filemanagerstring;
                cursor.close();
                System.out.println("selectedVideoPath y " + filemanagerstring);
//                dialog_camera.dismiss();
                if (filemanagerstring != null) {
                    progress.show();
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
                        if (mblength > 50) {
                            System.out.println("file.length() = " + mblength);
                            Global.msgDialog(PagesDetails.this, getResources().getString(R.string.File_Size_Too_Large_Must_be_less_than_50_MB));
                            progress.dismiss();
                        } else {
                            up_image.setVisibility(View.VISIBLE);
                            up_image.setImageBitmap(thumb);
                            progress.dismiss();
                            up_post.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (requestCode == CAPTURE_VIDEO) {
                Uri selectedVideo = data.getData();

                String[] filePathColumn = {MediaStore.Video.Media.DATA};
                Cursor cursor = PagesDetails.this.getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnindex = cursor.getColumnIndex(filePathColumn[0]);
                filemanagerstring = cursor.getString(columnindex);
                cursor.close();
                System.out.println("filemanagerstring x  " + filemanagerstring);
                postFile = filemanagerstring;
//                dialog_camera.dismiss();
                if (filemanagerstring != null) {
                    progress.show();
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
                        if (mblength > 50) {
                            System.out.println("file.length() = " + mblength);
                            Global.msgDialog(PagesDetails.this, getResources().getString(R.string.File_Size_Too_Large_Must_be_less_than_50_MB));
                            progress.dismiss();
                        } else {
                            up_image.setVisibility(View.VISIBLE);
                            up_image.setImageBitmap(video_thumbnail);
                            progress.dismiss();
                            up_post.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }


    public void PostFeedFinal(final String postText) {
        try {
            checkPrivacy();
            progress.show();

            MultipartEntity entity = new MultipartEntity();
            entity.addPart("user_id", new StringBody(SessionManager.get_user_id(prefs)));
            entity.addPart("page_id", new StringBody(page_id));
//            entity.addPart("s", new StringBody("1"));
            entity.addPart("s", new StringBody(SessionManager.get_session_id(prefs)));
            entity.addPart("postPrivacy", new StringBody(String.valueOf(postPrivacy))); //{0: public, 3 : only me}
            switch (postType) {
                case POST_TYPE_IMAGE:
                    entity.addPart("postText", new StringBody(postText));
                    if (!(postFile.equals(""))) {
                        File file = new File(postFile);
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postFile", fileBody);
                    }
                    break;
                case POST_TYPE_VIDEO:
                    entity.addPart("postText", new StringBody(postText));
                    if (!(postFile.equals(""))) {
                        File file = new File(postFile);
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postVideo", fileBody);
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
                    if (!(postFile.equals(""))) {
                        File file = new File(postFile);
                        FileBody fileBody = new FileBody(file);
                        entity.addPart("postFile", fileBody);
                    }
                    entity.addPart("postText", new StringBody(postText));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + postType);
            }
            MultipartRequest req = new MultipartRequest(Global.URL + "new_post", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        progress.dismiss();
                        JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
//                            JSONArray array = jsonObject.getJSONArray("posts");
                            ResetFeed();
//                            Global.msgDialog(PagesDetails.this, jsonObject.optString("msg"));
                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Global.msgDialog(PagesDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                    error.printStackTrace();
                }
            }, entity);

            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            req.setRetryPolicy(policy);
            queue.add(req);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getURLDetails(final String url) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "fetch_url", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
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
                    Glide.with(PagesDetails.this).load(url_image).into(link_image);

//                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
//                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
//                    } else {
//                        Global.msgDialog(getActivity(), "Error in server");
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progress.dismiss();
                Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
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
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    private void ResetFeed() {
        up_text.setText("");
        feedText = "";
        postType = "";
        postFile = "";


        after_post_id = "0";
        isLoading = false;
        isLastPage = false;
        up_image.setVisibility(View.GONE);
        up_post.setVisibility(View.GONE);
        link_layout.setVisibility(View.GONE);

        modelArrayList = new ArrayList<>();
        adapter = new HomeAdapter(PagesDetails.this, modelArrayList, this);
        mLayoutManager = new LinearLayoutManager(PagesDetails.this);

        post_list.setLayoutManager(mLayoutManager);
        post_list.setItemAnimator(new DefaultItemAnimator());

        post_list.setActivity(PagesDetails.this); //todo before setAdapter
        //optional - to play only first visible video
        post_list.setPlayOnlyFirstVideo(true); // false by default
        //optional - by default we check if url ends with ".mp4". If your urls do not end with mp4, you can set this param to false and implement your own check to see if video points to url
//        post_list.setCheckForMp4(false); //true by default

        //optional - download videos to local storage (requires "android.permission.WRITE_EXTERNAL_STORAGE" in manifest or ask in runtime)
//        post_list.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo"); // (Environment.getExternalStorageDirectory() + "/Video") by default
//        post_list.setDownloadVideos(true); // false by default
        post_list.setVisiblePercent(90); // percentage of View that needs to be visible to start playing

        post_list.setAdapter(adapter);
        //call this functions when u want to start autoplay on loading async lists (eg firebase)
//        post_list.smoothScrollBy(0,1);
//        post_list.smoothScrollBy(0,-1);

        if (Global.isOnline(PagesDetails.this)) {
            getPageDetails(page_id);
            getFeed();
            System.out.println("xxxxxxxxxx getFeed " + after_post_id + "xxxxxxxxxx");
        } else {
            Global.showDialog(PagesDetails.this);
        }
    }

    public void getPageDetails(final String page_id) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_page_data",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                JSONObject object = jsonObject.getJSONObject("page_data");
                                data = PageModel.fromJson(object);
                                setData(data);

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(PagesDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        progress.dismiss();
                        Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                "user_id:1735
//                s:
//                page_profile_id:177"
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("page_profile_id", page_id);
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    private void setData(final PageModel userData) {

        mTitle.setText(userData.getPage_title());
        tv_name.setText(userData.getPage_title());
        tv_address.setText("");
        Glide.with(PagesDetails.this).load(userData.getAvatar()).into(ep_user_image);
        Glide.with(PagesDetails.this).load(userData.getCover()).into(cover);
        if (userData.getIs_page_onwer()) {
            send_request.setVisibility(View.GONE);
            send_panel.setVisibility(View.VISIBLE);
        } else {
            send_request.setVisibility(View.VISIBLE);
            send_panel.setVisibility(View.GONE);
            if (userData.getIs_liked()) {
                status.setText(getResources().getString(R.string.Unlike_Page));
            } else {
                status.setText(getResources().getString(R.string.Like_Page));
            }
        }

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userData.getIs_page_onwer()) {
                    send_request.setVisibility(View.VISIBLE);
                    if (userData.getIs_liked()) {
                        likePage();
//                        status.setText("Unlike Page");
                    } else {
                        likePage();
//                        status.setText("Like Page");
                    }
                }
            }
        });

    }

    private void getFeed() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "home_posts", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Timber.e(String.valueOf(response));
                progress.dismiss();
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
                        Global.msgDialog(PagesDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
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
                Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
//                Global.msgDialog(Login.this, "Internet connection is slow");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                "user_id:1735
//                s:1
//                limit:20
//                publisher_id:1735"
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("page_id", page_id);
                param.put("after_post_id", after_post_id);
                param.put("limit", "20");
                param.put("s", SessionManager.get_session_id(prefs));
//                param.put("s", "1");
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void imageViewDialog(String url) {
        final Dialog dialog = new Dialog(PagesDetails.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.profiledialog);
        dialog.setCancelable(true);
        PhotoView img = (PhotoView) dialog.findViewById(R.id.image_view);
        ImageView del = (ImageView) dialog.findViewById(R.id.del);
        Glide.with(PagesDetails.this)
                .load(url)
//                .asBitmap()
                .into(img);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.block_menu, menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_block:
                BlockUser();
                break;
            case R.id.menu_report:
                ReportDialog();
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);

    }

    public void BlockUser() {
        final JSONObject json = new JSONObject();
        try {
//      "action:blockUnblock
//            user_id:55
//            blockuser_id:18"
            json.put("action", "blockUnblock");
            json.put("blockuser_id", page_id);
            json.put("user_id", SessionManager.get_user_id(prefs));
            Log.e("delete feeds : ", " data :  " + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Global.URL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("delete response : ", "" + response);
// {"status":"Success","msg":"Deleted."}
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = response;
                            if (jsonObject.getString("status").equals("Success")) {
//                                message
                                Toast.makeText(PagesDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                PagesDetails.this.onBackPressed();
                            } else if (jsonObject.getString("status").equals("Fail")) {
                                Global.msgDialog(PagesDetails.this, jsonObject.getString("msg"));
                            } else {
                                Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
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
                Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }



    public void ReportDialog() {

        Dialog dialog = new Dialog(mContext);
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
                    input.setError(getResources().getString(R.string.Enter_your_reason_to_report_this_post));
                } else {
                    input.setError(null);
                    ReportUser(input.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    public void ReportUser(String msg) {
        final JSONObject json = new JSONObject();
        try {
//    "action:reportuser
//            user_id:55
//            profile_id:18
//            message:
//            Test "
            json.put("action", "reportuser");
            json.put("user_id", SessionManager.get_user_id(prefs));
            json.put("profile_id", page_id);
            json.put("message", msg);
            Log.e("delete feeds : ", " data :  " + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progress.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, Global.URL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("delete response : ", "" + response);
// {"status":"Success","msg":"Deleted."}
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = response;
                            if (jsonObject.getString("status").equals("Success")) {
//                                message
                                Toast.makeText(PagesDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                PagesDetails.this.onBackPressed();
                            } else if (jsonObject.getString("status").equals("Fail")) {
                                Global.msgDialog(PagesDetails.this, jsonObject.getString("msg"));
                            } else {
                                Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
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
                Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        queue.add(jsonObjectRequest);
    }


//    public void getUsersDetails(String user_id) {
//        final JSONObject json = new JSONObject();
//        try {
////        action:userdetails
////            user_id:113
//            json.put("action", "userdetails");
//            json.put("user_id", user_id);
//            Log.e("getUsersDetails", " data : " + json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        progress.show();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST, Global.URL, json,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.v("login reponse", "" + response);
//
//                        progress.dismiss();
//                        try {
//                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
//                            if (jsonObject.getString("status").equals("Success")) {
//                                jsonObject2 = jsonObject.getJSONObject("response");
//                                user = new UserModel();
//                                user.setId(jsonObject2.getString("id"));
//                                user.setFirstName(jsonObject2.getString("firstName"));
//                                user.setEmailId(jsonObject2.getString("emailId"));
//                                user.setAddress(jsonObject2.getString("address"));
//                                user.setGender(jsonObject2.getString("gender"));
//                                user.setFirebaseId(jsonObject2.getString("firebaseId"));
//                                user.setSchool(jsonObject2.getString("school"));
//                                user.setStudied(jsonObject2.getString("studied"));
//                                user.setEmployment(jsonObject2.getString("employment"));
//                                user.setImage(jsonObject2.getString("image"));
//                                user.setDob(jsonObject2.getString("dob"));
//                                user.setPhone(jsonObject2.getString("mobile"));
//
//
//                                mTitle.setText(jsonObject2.getString("firstName"));
//                                tv_name.setText(jsonObject2.getString("firstName"));
//                                tv_address.setText(jsonObject2.getString("address"));
//                                Glide.with(UserDetails.this).load(jsonObject2.getString("image")).into(ep_user_image);
//
//                            } else if (jsonObject.getString("status").equals("Fail")) {
//                                Global.msgDialog(UserDetails.this, jsonObject.getString("msg"));
//                            } else {
//                                Global.msgDialog(UserDetails.this, "Error in server");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                progress.dismiss();
//                Global.msgDialog(UserDetails.this, "Error from server");
////                Global.msgDialog(UserDetails.this, "Internet connection is slow");
//            }
//        });
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        queue.add(jsonObjectRequest);
//    }

    private void likePage() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "like_page", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject mdata, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        mdata = jsonObject.getJSONObject("data");
                        if (mdata.getString("like").equalsIgnoreCase("liked")) {
                            data.setIs_liked(false);
                            status.setText("Unlike Page");
                        } else {
                            data.setIs_liked(true);
                            status.setText("Like Page");
                        }
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(PagesDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
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
                Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
//                Global.msgDialog(Login.this, "Internet connection is slow");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//        "user_id:1703
//        s:1
//        page_id:8939"
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("page_id", page_id);
//                param.put("s", "1");
                param.put("s", SessionManager.get_session_id(prefs));
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void DeleteFeed(final String id) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "delete_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(PagesDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
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
                Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
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
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    private void likeFeed(final String id) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "like_on_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
//                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(PagesDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
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
                Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
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
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void dislikeFeed(final String id) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "unlike_on_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
//                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(PagesDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
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
                Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
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
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void ReportFeed(final String id, final String message) {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "report_post", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                    if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                        Global.msgDialog(PagesDetails.this, getResources().getString(R.string.Post_reported_Successfully));
//                        Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                        ResetFeed();
                    } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                        Global.msgDialog(PagesDetails.this, jsonObject.optJSONObject("errors").optString("error_text"));
                    } else {
                        Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
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
                Global.msgDialog(PagesDetails.this, getResources().getString(R.string.error_server));
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
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

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
        Intent intent = new Intent(PagesDetails.this, FeedDetails.class);
        intent.putExtra("feed_id", post.getId());
        startActivity(intent);
    }

    @Override
    public void onShareClickListener(NewPostModel post) {
//        shareFeed(post.getId());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, post.getDetails_url());
        shareIntent.setType("text/html");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));
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
//        Intent intent = new Intent(UserDetails.this, UserDetails.class);
//        intent.putExtra("user_id", user_id);
//        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(from_where.equalsIgnoreCase("1")){
            Intent intent = new Intent(PagesDetails.this, FeedDetails.class);
            intent.putExtra("feed_id", feed_id);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(PagesDetails.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
