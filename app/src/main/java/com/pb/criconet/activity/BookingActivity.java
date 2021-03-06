package com.pb.criconet.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.loadingview.LoadingDialog;
import com.github.loadingview.LoadingView;
import com.google.gson.Gson;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.DataModel;
import com.pb.criconet.Utills.DateDropDownView;
import com.pb.criconet.Utills.FilterBookingDropDownView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.MultipartRequest;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.BookingHistoryAdapter;
import com.pb.criconet.models.BookingHistory;
import com.pb.criconet.models.CoachAccept;
import com.pb.criconet.models.ConstantApp;
import com.pb.criconet.retrofit.ApiInterfaceService;
import com.pb.criconet.retrofit.ResultObject;
import com.pb.criconet.retrofit.VideoInterface;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;
import timber.log.Timber;

import static com.pb.criconet.Utills.Global.POST_TYPE_IMAGE;
import static com.pb.criconet.Utills.Global.POST_TYPE_LINK;
import static com.pb.criconet.Utills.Global.POST_TYPE_MULTI_IMAGE;
import static com.pb.criconet.Utills.Global.POST_TYPE_TEXT;
import static com.pb.criconet.Utills.Global.POST_TYPE_VIDEO;
import static com.pb.criconet.Utills.Global.POST_TYPE_YOUTUBE;

public class BookingActivity extends AppCompatActivity implements BookingHistoryAdapter.clickCallback {
    private final String TAG = BookingActivity.class.getSimpleName();
    private RecyclerView coachlist;
    private RequestQueue queue;
    private SharedPreferences prefs;
    private ProgressDialog progressDialog;
    private Context mContext;
    private Activity mActivity;
    private boolean loading = true;
    private SlideUp slideUp, slideUp_academyDetails;
    private View dim, rootView;
    private View slideView;
    private CircleImageView ivProfileImage;
    private TextView tvCoachName;
    private TextView tvSessionDetails;
    private TextView tvSessionDateTime;
    private TextView tvBookingDate;
    private TextView tvSessionAmount;
    private TextView tvBookingStataus;
    private TextView tv_booking_paymnet_status;
    private FrameLayout fl_cancel_booking;
    private FrameLayout fl_join_session;
    private FrameLayout fl_cancelled;
    private String bookingId;
    CustomLoaderView loaderView;
    FilterBookingDropDownView drop_filter_booking;
    DateDropDownView drop_date_from;
    DateDropDownView drop_date_to;
    TextView tv_from_date, tvInfo;
    TextView tv_to_date, notfound;
    FrameLayout fl_search;
    private ArrayList<DataModel> option = new ArrayList<>();
    private String filterType = "";
    private String from_date = "";
    private String to_date = "";

    //WebView web_chat;
    private SlideUp slideUp_chat;
    private View dim_chat, rootView_chat, root_view_academy, dim_academydetails, slideView_academy;
    private View slideView_chat;
    String webUrl = "";
    String coach_id = "";

    private final static int FCR = 1;
    WebView webView;
    LoadingView progressBar;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    WebSettings webSettings;
    String notification_count = "";

    /*Record Video*/
    private static final int CAPTURE_VIDEO = 3015;
    private String postFile = "";
    private String filemanagerstring = " ";
    private byte[] byteArray;
    FloatingTextButton fabCreate;
    /*End Record Video*/

    /*Academy Details IdS...*/
    TextView tvCoachNamee, tvAddress, tvSessionValid, tv_training, tvBookingId, tvSessionTime, tvBookingDate_academy,
            tvSessionAmount_academy, tvBookingStataus_academy;
    ImageView ivProfileImagee_academy;
    FrameLayout fl_join_session_academy;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;

            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {

                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                    mUMA.onReceiveValue(results);
                    mUMA = null;
                }
                //..record video code comment here.
                else if (requestCode == CAPTURE_VIDEO) {
                    Uri selectedVideo = intent.getData();

                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = mActivity.getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
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
//                            if (mblength > 51) {
//                                System.out.println("file.length() = " + mblength);
//                                Global.msgDialog(mActivity, "File Size Too Large,\n Must be less than 50 MB");
//                                //progress.dismiss();
//                                loaderView.hideLoader();
//                            } else {
//                                EmailOtpDialog(video_thumbnail,filemanagerstring);
//                                loaderView.hideLoader();
//
//                            }
                            EmailOtpDialog(video_thumbnail, filemanagerstring);
                            loaderView.hideLoader();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
//                mUMA.onReceiveValue(results);
//                mUMA = null;
            }

        } else {

            if (requestCode == FCR) {
                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mContext = this;
        mActivity = this;
        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BookingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText("Booking History");

        initializeView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Global.isOnline(this)) {
            getBookingHistory();
        } else {
            Global.showDialog(this);
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initializeView() {
        fabCreate = (FloatingTextButton) findViewById(R.id.action_button);
        fabCreate.setOnClickListener(view -> {
            openCameraVideo();
        });
        webView = findViewById(R.id.web_chat);
        progressBar = findViewById(R.id.loadingVieww);
        webView.setWebViewClient(new Callback());
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        assert webView != null;

        webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);


        rootView = findViewById(R.id.root_view);
        slideView = findViewById(R.id.slideView);
        dim = findViewById(R.id.dim);
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


        //........Web Chat .....
        rootView_chat = findViewById(R.id.root_view_chat);
        slideView_chat = findViewById(R.id.slideView_chat);
        dim_chat = findViewById(R.id.dim_chat);
        slideUp_chat = new SlideUpBuilder(slideView_chat)
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

                            if (Global.isOnline(mContext)) {
                                getBookingHistory();
                            } else {
                                Global.showDialog(mActivity);
                            }

                        }
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(rootView_chat)
                .build();

        //........End Web Chat...

        // Academy Details
        tvCoachNamee = findViewById(R.id.tvCoachNamee);
        tvAddress = findViewById(R.id.tvAddress);
        tvSessionValid = findViewById(R.id.tvSessionValid);
        tv_training = findViewById(R.id.tv_training);
        tvBookingId = findViewById(R.id.tvBookingId);
        tvSessionTime = findViewById(R.id.tvSessionTime);
        tvBookingDate_academy = findViewById(R.id.tvBookingDate_academy);
        tvSessionAmount_academy = findViewById(R.id.tvSessionAmount_academy);
        tvBookingStataus_academy = findViewById(R.id.tvBookingStataus_academy);
        fl_join_session_academy = findViewById(R.id.fl_join_session_academy);
        root_view_academy = findViewById(R.id.root_view_academy);
        slideView_academy = findViewById(R.id.slideView_academy);
        dim_academydetails = findViewById(R.id.dim_academydetails);
        ivProfileImagee_academy = findViewById(R.id.ivProfileImagee_academy);
        slideUp_academyDetails = new SlideUpBuilder(slideView_academy)
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
                .withSlideFromOtherView(root_view_academy)
                .build();

        notfound = findViewById(R.id.notfound);
        coachlist = findViewById(R.id.coachlist);
        coachlist.setHasFixedSize(true);
        coachlist.setLayoutManager(new LinearLayoutManager(this));
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvCoachName = findViewById(R.id.tvCoachName);
        tvSessionDetails = findViewById(R.id.tvSessionDetails);
        tvSessionDateTime = findViewById(R.id.tvSessionDateTime);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvSessionAmount = findViewById(R.id.tvSessionAmount);
        tvBookingStataus = findViewById(R.id.tvBookingStataus);
        tv_booking_paymnet_status = findViewById(R.id.tv_booking_paymnet_status);
        fl_cancel_booking = findViewById(R.id.fl_cancel_booking);
        fl_join_session = findViewById(R.id.fl_join_session);
        fl_cancelled = findViewById(R.id.fl_cancelled);
        tvInfo = findViewById(R.id.tvInfo);
        drop_filter_booking = findViewById(R.id.drop_filter_booking);
        option.add(new DataModel("All Booking"));
        option.add(new DataModel("Cancelled booking"));
        option.add(new DataModel("Confirmed booking"));
        drop_filter_booking.setOptionList(option);
        filterType = option.get(0).getName();
        drop_filter_booking.setText(filterType);

        drop_filter_booking.setClickListener(new FilterBookingDropDownView.onClickInterface() {
            @Override
            public void onClickAction() {
            }

            @Override
            public void onClickDone(String name) {

                if (name.equalsIgnoreCase("Cancelled booking")) {
                    filterType = "cancelled";
                } else if (name.equalsIgnoreCase("Confirmed booking")) {
                    filterType = "confirmed";
                } else {
                    filterType = name;
                }
            }


            @Override
            public void onDismiss() {
            }
        });

        drop_date_from = findViewById(R.id.drop_date_from);
        drop_date_to = findViewById(R.id.drop_date_to);
        tv_to_date = findViewById(R.id.tv_to_date);
        tv_from_date = findViewById(R.id.tv_from_date);
        drop_date_from.setClickListener(new DateDropDownView.onClickInterface() {
            @Override
            public void onClickAction() {

            }

            @Override
            public void onClickDone(String name) {
                from_date = name;
                tv_from_date.setText(name);
            }

            @Override
            public void onDismiss() {

            }
        });
        drop_date_to.setClickListener(new DateDropDownView.onClickInterface() {
            @Override
            public void onClickAction() {

            }

            @Override
            public void onClickDone(String name) {
                to_date = name;
                tv_to_date.setText(name);
            }

            @Override
            public void onDismiss() {

            }
        });

        fl_search = findViewById(R.id.fl_search);
        fl_search.setOnClickListener(v -> {
            if (Global.isOnline(this)) {
                getBookingHistory();
            } else {
                Global.showDialog(this);
            }
//            if (isValidateSearch()){
//                getBookingHistory();
//            }
        });

    }

    private void getBookingHistory() {
        loaderView.showLoader();
//        progressDialog = Global.getProgressDialog(this, CCResource.getString(this, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_booking_history", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Boking Response", response);
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);

                try {
                    Gson gson = new Gson();
                    BookingHistory modelArrayList = gson.fromJson(response, BookingHistory.class);

                    if (modelArrayList.getData().isEmpty()) {
                        notfound.setVisibility(View.VISIBLE);
                        coachlist.setVisibility(View.GONE);
                    } else {
                        notfound.setVisibility(View.GONE);
                        coachlist.setVisibility(View.VISIBLE);
                        coachlist.setAdapter(new BookingHistoryAdapter(mContext, modelArrayList.getData(), BookingActivity.this));
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
                //Global.dismissDialog(progressDialog);
                Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("bstatus", filterType);
                param.put("from_date", from_date);
                param.put("to_date", to_date);
                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    @Override
    public void buttonClick(String id, long timeDuration, String action, String channel_id, String booking_id, String userId, String coachId, String coachName) {
        if (action.equalsIgnoreCase("join")) {
            Intent intent = new Intent(this, CallActivity.class);
            intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, channel_id);
            intent.putExtra("UserId", userId);
            intent.putExtra("CoachId", coachId);
            intent.putExtra("booking_id", booking_id);
            intent.putExtra("id", id);
            intent.putExtra("timeDuration", timeDuration);
            intent.putExtra("Name", coachName);
            startActivity(intent);
        } else {
            validateAction(booking_id, action);
        }
    }

    @Override
    public void viewDetails(BookingHistory.Datum data) {
        bookingId = data.getId();

        if (data.getAcademy_id().isEmpty() || data.getAcademy_id().equalsIgnoreCase("0") || data.getAcademy_id().equalsIgnoreCase("null")) {
            if (!data.getAvatar().isEmpty()) {
                Glide.with(mActivity).load(data.getAvatar())
                        .into(ivProfileImage);
            } else {
                Glide.with(mActivity).load(R.drawable.placeholder_user)
                        .into(ivProfileImage);
            }
            tvCoachName.setText(Global.capitalizeFirstLatterOfString(data.getName()));

            if (data.getDevice_message() == null || data.getDevice_message().isEmpty()) {
                tvInfo.setVisibility(View.GONE);
            } else {
                tvInfo.setVisibility(View.VISIBLE);
                tvInfo.setText(Html.fromHtml(data.getDevice_message()));
            }

            tvSessionDetails.setText(getResources().getString(R.string.bookingId) + " " + data.getBookingId());
            tvSessionDateTime.setText(Global.convertUTCDateToLocalDate(data.getBookingSlotDate()) + " , " + data.getBookingSlotTxt());
            tvBookingDate.setText(data.getBooking_date());
            if (data.getPay_leter_str().isEmpty()) {
                tv_booking_paymnet_status.setText(getResources().getString(R.string.online_paid_amount));
            } else {
                tv_booking_paymnet_status.setText(data.getPay_leter_str());
            }

            tvSessionAmount.setText("\u20B9" + data.getPayment_amount());
            tvBookingStataus.setText(getResources().getString(R.string.Your_booking_has_been) + data.getBtn1().toLowerCase() + "!");

            if ((data.getBtn1().equalsIgnoreCase("Confirmed") && data.getBtn2().equalsIgnoreCase(""))) {

                if (Global.getCurrentDate().compareTo(Global.convertUTCDateToLocalDate(data.getBookingSlotDate())) >= 0 && Global.getCurrentDateAndTime().compareTo(data.getOnlineSessionStartTime()) > 0) {

                    fl_cancel_booking.setVisibility(View.GONE);
                    fl_cancelled.setVisibility(View.GONE);
                    fl_join_session.setVisibility(View.GONE);
                } else {

                    if (data.getCancel_enable().equalsIgnoreCase("1")) {
                        fl_cancel_booking.setVisibility(View.VISIBLE);
                    } else {
                        fl_cancel_booking.setVisibility(View.GONE);
                    }
                    fl_cancelled.setVisibility(View.GONE);
                    fl_join_session.setVisibility(View.GONE);
                }

            } else if (data.getBtn1().equalsIgnoreCase("Cancelled")) {
                fl_cancel_booking.setVisibility(View.GONE);
                fl_cancelled.setVisibility(View.VISIBLE);
                fl_join_session.setVisibility(View.GONE);
            } else if (data.getBtn2().equalsIgnoreCase("Join") && data.getBtn1().equalsIgnoreCase("Confirmed")) {
                fl_cancel_booking.setVisibility(View.GONE);
                fl_cancelled.setVisibility(View.GONE);
                if (data.getChanel_id().equalsIgnoreCase("")) {
                    fl_join_session.setVisibility(View.GONE);
                } else {
                    fl_join_session.setVisibility(View.VISIBLE);
                }

            }

            fl_cancel_booking.setOnClickListener(v -> {
                slideUp.hide();
                cancelAlertDialog();
            });
            fl_join_session.setOnClickListener(v -> {
                Intent intent = new Intent(this, CallActivity.class);
                intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, data.getChanel_id());
                intent.putExtra("UserId", data.getUserId());
                intent.putExtra("CoachId", data.getCoachUserId());
                intent.putExtra("booking_id", data.getBookingId());
                intent.putExtra("id", data.getId());
                intent.putExtra("timeDuration", data.getDuration_in_milisecond());
                intent.putExtra("Name", data.getName());
                startActivity(intent);
                slideUp.hide();
            });

            slideUp.show();
        } else {
            slideUp_academyDetails.show();
            if (!data.getAvatar().isEmpty()) {
                Glide.with(mActivity).load(data.getLogo())
                        .into(ivProfileImagee_academy);
            } else {
                Glide.with(mActivity).load(R.drawable.placeholder_user)
                        .into(ivProfileImagee_academy);
            }
            tvCoachNamee.setText(Global.capitalizeFirstLatterOfString(data.getAcademy_name()));
            tvAddress.setText(data.getAddress());
            tvSessionValid.setText(getString(R.string.valid_for)+" "+data.getPackage_valid_for());
            tvSessionTime.setText(data.getBookingSlotTxt());
            tvBookingDate_academy.setText(data.getBooking_date());
            tvSessionAmount_academy.setText(data.getPayment_amount());
            tvBookingId.setText(data.getBookingId());

            if(data.getTraining_type().isEmpty()|| data.getTraining_type().equalsIgnoreCase("null")){
                tv_training.setVisibility(View.GONE);
            }else{
                tv_training.setVisibility(View.VISIBLE);
                tv_training.setText(getString(R.string.training_type)+" "+data.getTraining_type());

            }


            tvBookingStataus_academy.setText(getResources().getString(R.string.Your_booking_has_been) + data.getBtn1().toLowerCase() + "!");
            if (data.getBtn2().equalsIgnoreCase("Join") && data.getBtn1().equalsIgnoreCase("Confirmed")) {

                if (data.getChanel_id().equalsIgnoreCase("")) {
                    fl_join_session_academy.setVisibility(View.GONE);
                } else {
                    fl_join_session_academy.setVisibility(View.VISIBLE);
                }

            }

            fl_join_session_academy.setOnClickListener(v -> {
                slideUp_academyDetails.hide();
                Intent intent = new Intent(this, CallActivity.class);
                intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, data.getChanel_id());
                intent.putExtra("UserId", data.getUserId());
                intent.putExtra("CoachId", data.getCoachUserId());
                intent.putExtra("booking_id", data.getBookingId());
                intent.putExtra("id", data.getId());
                intent.putExtra("timeDuration", data.getDuration_in_milisecond());
                intent.putExtra("Name", data.getName());
                startActivity(intent);

            });
        }

    }

    @Override
    public void chatClick(String coachId, String userId) {
        loadWebView(coachId, userId);
        slideUp_chat.show();

    }

    private boolean isValidateSearch() {
        boolean isOk = true;
        if (from_date.equalsIgnoreCase("")) {
            Toaster.customToast("Select From date!");
            isOk = false;
        } else if (to_date.equalsIgnoreCase("")) {
            Toaster.customToast("Select To date!");
            isOk = false;
        } else if (filterType.equalsIgnoreCase("")) {
            Toaster.customToast("Select Booking Status!");
            isOk = false;
        }

        return isOk;
    }

    private void validateAction(String booking_id, String action) {
        loaderView.showLoader();
//        progressDialog = Global.getProgressDialog(this, CCResource.getString(this, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "booking_action", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                Gson gson = new Gson();
                CoachAccept modelArrayList = gson.fromJson(response, CoachAccept.class);
                Toaster.customToast(modelArrayList.getData().getMessage());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                Global.msgDialog((Activity) mContext, getResources().getString(R.string.error_server));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("action", action);
                param.put("booking_id", booking_id);
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

    private void cancelAlertDialog() {
        final Dialog dialog = new Dialog(BookingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_cancel_booking);
        dialog.setCancelable(false);
        FrameLayout fl_no = dialog.findViewById(R.id.fl_no);
        fl_no.setOnClickListener(v -> {
            dialog.dismiss();
        });
        FrameLayout fl_yes = dialog.findViewById(R.id.fl_yes);
        fl_yes.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(mContext, CancellationFeedbackFormActivity.class).putExtra("BookingId", bookingId));
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BookingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // webchat....
    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility", "ObsoleteSdkInt"})
    public void loadWebView(String coach_id, String user_Id) {
        if (SessionManager.getProfileType(prefs).equalsIgnoreCase("Coach")) {
            webUrl = Global.URL_CHAT + "/" + "messages" + "/" + user_Id + "?" + "user_id=" + SessionManager.get_user_id(prefs) + "&" + "s=" + SessionManager.get_session_id(prefs);
        } else {
            webUrl = Global.URL_CHAT + "/" + "messages" + "/" + coach_id + "?" + "user_id=" + SessionManager.get_user_id(prefs) + "&" + "s=" + SessionManager.get_session_id(prefs);
        }

        Log.d("WevURL", webUrl);
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(BookingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            //webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.loadUrl(webUrl);
        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                //File file = new File(url);
                //openFile(file);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                BookingActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
            }

            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                BookingActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FCR);
            }

            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                BookingActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), BookingActivity.FCR);
            }

            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {

                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }

                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(BookingActivity.this.getPackageManager()) != null) {

                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch (IOException ex) {
                        Log.e(TAG, "Image file creation failed", ex);
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }

                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray;

                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);

                return true;
            }
        });
    }


    private File createImageFile() throws IOException {

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        try {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onKeyDown(keyCode, event);
    }

    public class Callback extends WebViewClient {
        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            webview.setVisibility(webView.INVISIBLE);
            progressBar.start();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.setVisibility(webView.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.stop();
                }
            }, 2000);

            super.onPageFinished(view, url);

        }

        // ProgressBar will disappear once page is loaded

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Failed_loading_app), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


    /*Record Video code start here..*/
    private void openCameraVideo() {
        File saveFolder = new File(Environment.getExternalStorageDirectory(), "Utopiaxxx");
        try {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);

            if (takeVideoIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, CAPTURE_VIDEO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void EmailOtpDialog(Bitmap thumbnail, String postFile) {
        Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_record_video_upload);
        dialog.setCancelable(false);
        ImageView img_video = dialog.findViewById(R.id.img_videoo);
        img_video.setImageBitmap(thumbnail);
        FrameLayout fl_cancel = dialog.findViewById(R.id.fl_cancell);
        fl_cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        FrameLayout fl_upload_video = dialog.findViewById(R.id.fl_upload_videoo);
        fl_upload_video.setOnClickListener(view -> {
            //PostFeedFinal(postFile);
            dialog.dismiss();
            uploadVideoToServer(postFile);
        });

        dialog.show();
    }

    public void PostFeedFinal(String postFile) {
        try {
            //progress.show();
            loaderView.showLoader();

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            Timber.e("Chuncked %b", entity.isChunked());
//            entity.addPart("s", new StringBody("1"));
            entity.addPart("user_id", new StringBody(SessionManager.get_user_id(prefs)));
            entity.addPart("s", new StringBody(SessionManager.get_session_id(prefs)));
            if (!postFile.isEmpty()) {
                File file = new File(postFile);
                FileBody fileBody = new FileBody(file);
                entity.addPart("postVideo ", fileBody);
            }


            MultipartRequest req = new MultipartRequest(Global.URL + Global.UPLOAD_VIDEO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //progress.dismiss();
                                loaderView.hideLoader();
                                Timber.e(response);
                                JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                                if (jsonObject.optString("api_text").equalsIgnoreCase("success")) {

                                } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                    Global.msgDialog(mActivity, jsonObject.optJSONObject("errors").optString("error_text"));
                                } else {
                                    Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
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

            Log.d("PostEntity", entity.toString());


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

    private void uploadVideoToServer(String postFile) {

        try {
            File videoFile = new File(postFile);
            RequestBody videoBody = RequestBody.create(MediaType.parse("application/octet-stream"), videoFile);
            MultipartBody.Part vFile = MultipartBody.Part.createFormData("postVideo", videoFile.getName(), videoBody);

            VideoInterface vInterface = ApiInterfaceService.getApiService();
            //VideoInterface vInterface = retrofit.create(VideoInterface.class);
            Call<ResultObject> serverCom = vInterface.uploadVideoToServerr(vFile,
                    bodyPart(SessionManager.get_user_id(prefs)),
                    bodyPart(SessionManager.get_session_id(prefs)),
                    bodyPart(postFile));

            //progress.show();
            loaderView.showLoader();

            serverCom.enqueue(new retrofit2.Callback<ResultObject>() {
                @Override
                public void onResponse(Call<ResultObject> call, retrofit2.Response<ResultObject> response) {
                    try {
                        //progress.dismiss();
                        loaderView.hideLoader();
//                        tv_post.setVisibility(View.GONE);
//                        img_addpost.setVisibility(View.VISIBLE);
                        Log.d("ResponseRecord", response + "");
                        Timber.e(String.valueOf(response));
                        ResultObject result = response.body();
                        Timber.e(result.toString());
                        if (result.getApi_text().equalsIgnoreCase("success")) {
                            startActivity(new Intent(mContext, RecordedVideoActivity.class));
                            finish();

                        } else if (result.getApi_text().equalsIgnoreCase("failed")) {
                            Global.msgDialog(mActivity, result.getApi_status());
//                        Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog(mActivity, getResources().getString(R.string.error_server));
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /*End Record Video Code */


}