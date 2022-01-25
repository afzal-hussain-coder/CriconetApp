//package com.pb.criconet.Utills;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.os.Handler;
//import android.preference.PreferenceManager;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.ArrayAdapter;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.RetryPolicy;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.applandeo.materialcalendarview.CalendarView;
//import com.applandeo.materialcalendarview.EventDay;
//import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
//import com.google.gson.Gson;
//import com.pb.criconet.R;
//import com.pb.criconet.adapters.LanguageAdapter;
//import com.pb.criconet.fontsview.OSLTextView;
//import com.pb.criconet.models.DataModel;
//import com.pb.criconet.models.Language;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class SelectLanguageDropDownView extends OSLTextView implements View.OnClickListener, PopupWindow.OnDismissListener{
//    private onClickInterface onClickInterface;
//    final Calendar myCalendar = Calendar.getInstance();
//    private Language languageModelArrayList;
//    ArrayList<String> language=null;
//    String[] langArray = null;
//    boolean[] selectedLanguage;
//    private SharedPreferences prefs;
//    private RequestQueue queue;
//    ArrayList<Integer> langList = new ArrayList<>();
//    Context context;
//    public SelectLanguageDropDownView(Context context) {
//        super(context);
//        this.context= context;
//        initView();
//    }
//
//    public SelectLanguageDropDownView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
//        super(context, attrs);
//        initView();
//    }
//
//    public SelectLanguageDropDownView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initView();
//    }
//    private void initView() {
//        this.setOnClickListener(this);
//
//        prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        queue = Volley.newRequestQueue(context);
//    }
//
//
//    @SuppressLint("NewApi")
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private PopupWindow popupWindowSort(Context context,View view) {
//        PopupWindow popupWindow = new PopupWindow();
//        popupWindow.setWidth(this.getWidth());
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(context.getDrawable(R.drawable.booking_fileter_bg));
//        popupWindow.setElevation(10);
//        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//
//
//        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
//        View popupView = inflater.inflate(R.layout.selectlanguagepopup, null);
//        RecyclerView rv_language=findViewById(R.id.rv_language);
//        rv_language.setHasFixedSize(true);
//        rv_language.setLayoutManager(new LinearLayoutManager(context));
//
//        popupWindow.setOnDismissListener(this);
//        popupWindow.setFocusable(true);
//        popupWindow.setContentView(popupView);
//        return popupWindow;
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        if (v == this) {
//            PopupWindow window = popupWindowSort(v.getContext(),v.getRootView());
//            window.showAsDropDown(v, 0, 0);
//            if (onClickInterface != null)
//                onClickInterface.onClickAction();
//        }
//
//    }
//
//    public void setClickListener(onClickInterface listener) {
//        this.onClickInterface = listener;
//    }
//
//    @Override
//    public void onDismiss() {
//        if (onClickInterface != null)
//            onClickInterface.onDismiss();
//    }
//
//    public interface onClickInterface {
//        void onClickAction();
//
//        void onClickDone(String name);
//
//        void onDismiss();
//    }
//
//    private void getLanguage() {
//        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + Global.GET_LANGUAGE, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("ResponseCountry",response);
//                Gson gson = new Gson();
//                languageModelArrayList = gson.fromJson(response, Language.class);
//                if(languageModelArrayList.getApiStatus().equalsIgnoreCase("200")) {
//                    language = new ArrayList<>();
//                    language.add("Language");
//                    for (Language.Datum datum : languageModelArrayList.getData()) {
//                        language.add(datum.getName_eng());
//                    }
//                    language.remove(0);
//                    langArray= language.toArray(new String[language.size()]);
//                    selectedLanguage = new boolean[langArray.length];
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                Global.msgDialogg(context, getResources().getString(R.string.error_server));
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> param = new HashMap<String, String>();
//                param.put("user_id", SessionManager.get_user_id(prefs));
//                param.put("s", SessionManager.get_session_id(prefs));
//                return param;
//            }
//        };
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        queue.add(postRequest);
//    }
//
//    public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
//
//        List<com.pb.criconet.models.DataModel.Datum> mValues;
//        Context mContext;
//
//        public LanguageAdapter(Context context, List<com.pb.criconet.models.DataModel.Datum> values, com.pb.criconet.adapters.LanguageAdapter.ItemListener itemListener) {
//
//            mValues = values;
//            mContext = context;
//            mListener=itemListener;
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//            public TextView tvButton;
//            com.pb.criconet.models.DataModel.Datum items;
//
//            public ViewHolder(View v) {
//                super(v);
//                v.setOnClickListener(this);
//                tvButton = (TextView) v.findViewById(R.id.tvButton);
//            }
//
//            public void setData(com.pb.criconet.models.DataModel.Datum item) {
//                this.items = item;
//                tvButton.setText(items.getTitle());
//                if(item.isCheck()){
//                    tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg_red));
//                    tvButton.setTextColor(mContext.getResources().getColor(R.color.white));
//                }else {
//                    tvButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_bg));
//                    tvButton.setTextColor(mContext.getResources().getColor(R.color.dim_grey));
//                }
//            }
//
//
//            @Override
//            public void onClick(View view) {
////                if ( //mListener != null) {
////
////                    if(mValues.get(getAbsoluteAdapterPosition()).isCheck()) {
////                        mValues.get(getAbsoluteAdapterPosition()).setCheck(false);
////
////                        notifyDataSetChanged();
////                    }else {
////                        mValues.get(getAbsoluteAdapterPosition()).setCheck(true);
////                        //mListener.onItemClick(mValues);
////                        notifyDataSetChanged();
////                    }
////                   // mListener.onItemClick(mValues);
////                }
//            }
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//            View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
//
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder Vholder, int position) {
//            Vholder.setData(mValues.get(position));
//
//        }
//
//        @Override
//        public int getItemCount() {
//
//            return mValues.size();
//        }
//
//
//    }
//}
