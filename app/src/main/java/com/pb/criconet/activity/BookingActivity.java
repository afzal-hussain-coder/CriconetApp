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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ProgressBar;
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
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.BookingHistoryAdapter;
import com.pb.criconet.models.BookingHistory;
import com.pb.criconet.models.CoachAccept;
import com.pb.criconet.models.ConstantApp;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class BookingActivity extends AppCompatActivity implements BookingHistoryAdapter.clickCallback {
    private final String TAG = BookingActivity.class.getSimpleName();
    private RecyclerView coachlist;
    private RequestQueue queue;
    private SharedPreferences prefs;
    private ProgressDialog progressDialog;
    private Context mContext;
    private Activity mActivity;
    private boolean loading = true;
    private SlideUp slideUp;
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
    TextView tv_from_date,tvInfo;
    TextView tv_to_date,notfound;
    FrameLayout fl_search;
    private ArrayList<DataModel> option = new ArrayList<>();
    private String filterType="" ;
    private String from_date="";
    private String to_date="";


    //WebView web_chat;
    private SlideUp slideUp_chat;
    private View dim_chat, rootView_chat;
    private View slideView_chat;
    String webUrl="";
    String coach_id="";

    private final static int FCR = 1;
    WebView webView;
    LoadingView progressBar;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    WebSettings webSettings;



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
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
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
        mActivity =this;
        loaderView = CustomLoaderView.initialize(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
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

        webView =findViewById(R.id.web_chat);
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
                        }
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(rootView_chat)
                .build();

        //........End Web Chat...

        notfound = findViewById(R.id.notfound);
        coachlist=findViewById(R.id.coachlist);
        coachlist.setHasFixedSize(true);
        coachlist.setLayoutManager(new LinearLayoutManager(this));
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvCoachName =findViewById(R.id.tvCoachName);
        tvSessionDetails =findViewById(R.id.tvSessionDetails);
        tvSessionDateTime =findViewById(R.id.tvSessionDateTime);
        tvBookingDate=findViewById(R.id.tvBookingDate);
        tvSessionAmount=findViewById(R.id.tvSessionAmount);
        tvBookingStataus=findViewById(R.id.tvBookingStataus);
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

                if(name.equalsIgnoreCase("Cancelled booking")){
                    filterType= "cancelled";
                }else if(name.equalsIgnoreCase("Confirmed booking")){
                    filterType= "confirmed";
                }else{
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
                to_date= name;
                tv_to_date.setText(name);
            }

            @Override
            public void onDismiss() {

            }
        });

        fl_search = findViewById(R.id.fl_search);
        fl_search.setOnClickListener(v -> {
            getBookingHistory();
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
                Log.d("Boking Response",response);
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                Gson gson = new Gson();
                BookingHistory modelArrayList = gson.fromJson(response, BookingHistory.class);

                if(modelArrayList.getData().isEmpty()){
                    notfound.setVisibility(View.VISIBLE);
                    coachlist.setVisibility(View.GONE);
                }else{
                    notfound.setVisibility(View.GONE);
                    coachlist.setVisibility(View.VISIBLE);
                    coachlist.setAdapter(new BookingHistoryAdapter(mContext,modelArrayList.getData(), BookingActivity.this));
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                Global.msgDialog((Activity) mContext, "Error from server");
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
    public void buttonClick(String id,long timeDuration,String action,String channel_id,String booking_id,String userId,String coachId,String coachName) {
        if(action.equalsIgnoreCase("join")){
            Intent intent=new Intent(this, CallActivity.class);
            intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,channel_id);
            intent.putExtra("UserId",userId);
            intent.putExtra("CoachId",coachId);
            intent.putExtra("booking_id",booking_id);
            intent.putExtra("id",id);
            intent.putExtra("timeDuration",timeDuration);
            intent.putExtra("Name",coachName);
            startActivity(intent);
        }else {
            validateAction(booking_id,action);
        }
    }

    @Override
    public void viewDetails(BookingHistory.Datum data) {
        bookingId = data.getId();
        if (!data.getAvatar().isEmpty()){
            Glide.with(mActivity).load(data.getAvatar())
                    .into(ivProfileImage);
        }else{
            Glide.with(mActivity).load(R.drawable.placeholder_user)
                    .into(ivProfileImage);
        }
        tvCoachName.setText(Global.capitalizeFirstLatterOfString(data.getName()));

        if(data.getDevice_message()==null || data.getDevice_message().isEmpty()){
            tvInfo.setVisibility(View.GONE);
        }else{
            tvInfo.setVisibility(View.VISIBLE);
            tvInfo.setText(Html.fromHtml(data.getDevice_message()));
        }

        tvSessionDetails.setText("Booking ID: "+" "+data.getBookingId());
        tvSessionDateTime.setText(Global.convertUTCDateToLocalDate(data.getBookingSlotDate())+" , "+data.getBookingSlotTxt());
        tvBookingDate.setText(data.getBooking_date());
        if(data.getPay_leter_str().isEmpty()){
            tv_booking_paymnet_status.setText("Online e-Coaching session paid amount");
        }else{
            tv_booking_paymnet_status.setText(data.getPay_leter_str());
        }

        tvSessionAmount.setText("\u20B9"+data.getPayment_amount());
        tvBookingStataus.setText("Your booking has been "+data.getBtn1().toLowerCase()+"!");

        if((data.getBtn1().equalsIgnoreCase("Confirmed") &&data.getBtn2().equalsIgnoreCase(""))){

            if(Global.getCurrentDate().compareTo(Global.convertUTCDateToLocalDate(data.getBookingSlotDate()))>=0 && Global.getCurrentDateAndTime().compareTo(data.getOnlineSessionStartTime())>0 ){

                fl_cancel_booking.setVisibility(View.GONE);
                fl_cancelled.setVisibility(View.GONE);
                fl_join_session.setVisibility(View.GONE);
            }else{

                if(data.getCancel_enable().equalsIgnoreCase("1")){
                    fl_cancel_booking.setVisibility(View.VISIBLE);
                }else{
                    fl_cancel_booking.setVisibility(View.GONE);
                }
                fl_cancelled.setVisibility(View.GONE);
                fl_join_session.setVisibility(View.GONE);
            }

        }else if(data.getBtn1().equalsIgnoreCase("Cancelled")){
            fl_cancel_booking.setVisibility(View.GONE);
            fl_cancelled.setVisibility(View.VISIBLE);
            fl_join_session.setVisibility(View.GONE);
        }else if(data.getBtn2().equalsIgnoreCase("Join") && data.getBtn1().equalsIgnoreCase("Confirmed")){
            fl_cancel_booking.setVisibility(View.GONE);
            fl_cancelled.setVisibility(View.GONE);
            if(data.getChanel_id().equalsIgnoreCase("")){
                fl_join_session.setVisibility(View.GONE);
            }else{
                fl_join_session.setVisibility(View.VISIBLE);
            }

        }

        fl_cancel_booking.setOnClickListener(v -> {
            slideUp.hide();
            cancelAlertDialog();
        });
        fl_join_session.setOnClickListener(v -> {
            Intent intent=new Intent(this, CallActivity.class);
            intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,data.getChanel_id());
            intent.putExtra("UserId",data.getUserId());
            intent.putExtra("CoachId",data.getCoachUserId());
            intent.putExtra("booking_id",data.getBookingId());
            intent.putExtra("id",data.getId());
            intent.putExtra("timeDuration",data.getDuration_in_milisecond());
            intent.putExtra("Name",data.getName());
            startActivity(intent);
            slideUp.hide();
        });

        slideUp.show();
    }

    @Override
    public void chatClick(String coachId, String userId) {
        loadWebView(coachId,userId);
        slideUp_chat.show();

    }

    private boolean isValidateSearch(){
        boolean isOk = true;
        if(from_date.equalsIgnoreCase("")){
            Toaster.customToast("Select From date!");
            isOk =false;
        }else if(to_date.equalsIgnoreCase("")){
            Toaster.customToast("Select To date!");
            isOk =false;
        }else if(filterType.equalsIgnoreCase("")){
            Toaster.customToast("Select Booking Status!");
            isOk =false;
        }

        return isOk;
    }

    private void validateAction(String booking_id,String action) {
        loaderView.showLoader();
//        progressDialog = Global.getProgressDialog(this, CCResource.getString(this, R.string.loading_dot), false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "booking_action", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                Gson gson = new Gson();
                CoachAccept modelArrayList = gson.fromJson(response, CoachAccept.class);
                Toaster.customToast(modelArrayList.getData().getMessage() );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                loaderView.hideLoader();
                //Global.dismissDialog(progressDialog);
                Global.msgDialog((Activity) mContext, "Error from server");
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
            startActivity(new Intent(mContext,CancellationFeedbackFormActivity.class).putExtra("BookingId",bookingId));
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
    public void loadWebView(String coach_id, String user_Id){
        if(SessionManager.getProfileType(prefs).equalsIgnoreCase("Coach")){
            webUrl= Global.URL_CHAT+"/"+"messages"+"/"+user_Id+"?"+"user_id="+SessionManager.get_user_id(prefs)+"&"+"s="+SessionManager.get_session_id(prefs);
        }else{
            webUrl= Global.URL_CHAT+"/"+"messages"+"/"+coach_id+"?"+"user_id="+SessionManager.get_user_id(prefs)+"&"+"s="+SessionManager.get_session_id(prefs);
        }

        Log.d("WevURL",webUrl);
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
    private void openFile(File url) {

        try {

            Uri uri = Uri.fromFile(url);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip")) {
                // ZIP file
                intent.setDataAndType(uri, "application/zip");
            } else if (url.toString().contains(".rar")){
                // RAR file
                intent.setDataAndType(uri, "application/x-rar-compressed");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mActivity, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
           try{
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

           }catch (Exception e){
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
            },2000);

            super.onPageFinished(view, url);

        }

        // ProgressBar will disappear once page is loaded

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
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
}