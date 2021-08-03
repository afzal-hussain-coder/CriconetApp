package com.pb.criconet.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.BookingHistoryAdapter;
import com.pb.criconet.models.BookingHistory;
import com.pb.criconet.models.PageModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class Pages extends AppCompatActivity {
    SharedPreferences prefs;
    EditText etxt_search;
    TextView notfound;
    ArrayList<PageModel> arrayList_new, modelArrayList;
    pageAdapter adapter;
    ProgressDialog progress;
    RequestQueue queue;
    private RecyclerView friends_list;

    // For pagination.
    int page = 1;
    private int visibleThreshold = 10;
    private int previousTotal = 0;
    private boolean loading = true;

    private SlideUp slideUp;
    private View dim, rootViewPost;
    private View slideView;
    private String page_dess;
    EditText page_namee;
    EditText page_des;
    TextView tv_setDes_count;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pages.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        mTitle.setText(R.string.my_pages);

        prefs = PreferenceManager.getDefaultSharedPreferences(Pages.this);
        queue = Volley.newRequestQueue(Pages.this);
        progress = new ProgressDialog(Pages.this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);


        friends_list = findViewById(R.id.friends_list);
        friends_list.setHasFixedSize(true);
        friends_list.setLayoutManager(new LinearLayoutManager(this));
        etxt_search = (EditText) findViewById(R.id.etxt_search);
        notfound = (TextView) findViewById(R.id.notfound);
        notfound.setText("Sorry No Page Found");
        modelArrayList = new ArrayList<>();

        etxt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Timber.e(String.valueOf(s));
                if (s.length() > 0) {
//                    searchFriends(s.toString());
                    arrayList_new = new ArrayList<>();
                    for (int i = 0; i < modelArrayList.size(); i++) {
                        if (modelArrayList.get(i).getPage_title().toLowerCase().contains(s.toString().toLowerCase())) {
                            arrayList_new.add(modelArrayList.get(i));
                        }
                    }
                    adapter = new pageAdapter(Pages.this, arrayList_new);
                    friends_list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (arrayList_new.size() == 0) {
                        notfound.setVisibility(View.VISIBLE);
                    } else {
                        notfound.setVisibility(View.GONE);
                    }
                } else {
                    adapter = new pageAdapter(Pages.this, modelArrayList);
                    friends_list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (modelArrayList.size() == 0) {
                        notfound.setVisibility(View.VISIBLE);
                    } else {
                        notfound.setVisibility(View.GONE);
                    }
//                    RefreshList();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                try {
//                    if (arrayList_new.isEmpty()) {
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });


        /*friends_list.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        page++;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    // I load the next page of gigs using a background task,
                    // but you can call any function here.
                    getFriends();
                    loading = true;
                }
            }
        });*/

//        friends_list.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });

//        friends_list.setOnClickListener(v -> {
//            String search = etxt_search.getText().toString();
//            if (search.length() > 0) {
//                Intent intent = new Intent(Pages.this, PagesDetails.class);
//                intent.putExtra("page_id", arrayList_new.get(position).getPage_id());
////                    intent.putExtra("friendStatus", arrayList_new.get(position).getFriendStatus());
//                startActivity(intent);
//            } else {
//                Intent intent = new Intent(Pages.this, PagesDetails.class);
//                intent.putExtra("page_id", modelArrayList.get(position).getPage_id());
////                    intent.putExtra("friendStatus", modelArrayList.get(position).getFriendStatus());
//                startActivity(intent);
//            }
//        });

//        friends_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String search = etxt_search.getText().toString();
//                if (search.length() > 0) {
//                    Intent intent = new Intent(Pages.this, PagesDetails.class);
//                    intent.putExtra("page_id", arrayList_new.get(position).getPage_id());
////                    intent.putExtra("friendStatus", arrayList_new.get(position).getFriendStatus());
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(Pages.this, PagesDetails.class);
//                    intent.putExtra("page_id", modelArrayList.get(position).getPage_id());
////                    intent.putExtra("friendStatus", modelArrayList.get(position).getFriendStatus());
//                    startActivity(intent);
//                }
//            }
//        });
        initializeView();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //savedInstanceState.putString("MyString", String.valueOf(toolbar));

    /* remember you would need to actually initialize these variables before putting it in the
    Bundle */
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //toolbar = savedInstanceState.getString("MyString");

    }

    private void initializeView() {
        rootViewPost = findViewById(R.id.root_view);
        slideView = findViewById(R.id.slideView);
        dim = findViewById(R.id.dim);
        FrameLayout fl_create = findViewById(R.id.fl_create);
        page_namee = findViewById(R.id.page_name);
        page_des = findViewById(R.id.page_des);
        tv_setDes_count = findViewById(R.id.tv_setDes_count);
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
                .build();

        page_des.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_setDes_count.setText("Page description remaining " + (900 - s.toString().length()) + " character");
                tv_setDes_count.setTextColor(getResources().getColor(R.color.app_red_light));
                if (s.toString().length() == 900) {
                    tv_setDes_count.setText("Done");
                    tv_setDes_count.setTextColor(getResources().getColor(R.color.app_green));
                }

            }
        });
        fl_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_name = page_namee.getText().toString().trim();
                page_dess = page_des.getText().toString().trim();
                if (!Global.validateLength(page_name, 5)) {
                    Toaster.customToast("Page Name must be at least 5 characters.");
                } else if (!Global.validateLength(page_dess, 32)) {
                    Toaster.customToast("Enter Page Description at least 32 characters.!");
                } else {
                    createPage();
                }
            }
        });

        //.withSlideFromOtherView(getBaseContext())
    }


    @Override
    public void onResume() {
        super.onResume();
        etxt_search.setText("");
        page = 1;
        visibleThreshold = 10;
        previousTotal = 0;
        loading = true;

        RefreshList();
    }

    public void RefreshList() {
        page = 1;
        visibleThreshold = 10;
        previousTotal = 0;
        loading = true;

        modelArrayList = new ArrayList<>();
        adapter = new pageAdapter(Pages.this, modelArrayList);
        friends_list.setAdapter(adapter);
        if (Global.isOnline(Pages.this)) {
            getFriends();
            System.out.println("xxxxxxxxxxxxxxxx Search xxxxxxxxxxxxxxx");
        } else {
            Global.showDialog(Pages.this);
        }
    }

    public void getFriends() {
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "my_pages",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e("response:  %s", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                ArrayList<PageModel> object = PageModel.fromJson(jsonObject.getJSONArray("data"));
                                modelArrayList.addAll(object);
                                adapter.notifyDataSetChanged();
                                if (modelArrayList.size() == 0) {
                                    notfound.setVisibility(View.VISIBLE);
                                } else {
                                    notfound.setVisibility(View.GONE);
                                }

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                                Global.msgDialog(Pages.this, jsonObject.optJSONObject("errors").optString("error_text"));
                            } else {
                                Global.msgDialog(Pages.this, "Error in server");
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
                        Global.msgDialog(Pages.this, "Error from server");
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
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

    public void createPage() {
        slideUp.hide();
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "create_page",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Timber.e("response:  %s", response);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                RefreshList();
                                page_namee.setText("");
                                page_des.setText("");
//                                PageModel page = PageModel.fromJson(jsonObject.getJSONObject("data"));
//                                Intent intent = new Intent(Pages.this, EditPage.class);
//                                intent.putExtra("page_id", page.getPage_id());
//                                startActivity(intent);

                            } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
//                                Global.msgDialog(Pages.this, jsonObject.optJSONObject("errors").optString("error_text"));
                                Global.msgDialog(Pages.this, jsonObject.optString("message"));
                            } else {
                                Global.msgDialog(Pages.this, "Error in server");
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
                        Global.msgDialog(Pages.this, "Error from server");
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
//                "user_id:1735
//                s:4d9076ab6b1b5254aa8de046c65fd63a5908ee22134451b41ee035cc8eaa0aa8d1de0443860666293f78fa1cdb0e2fda88c2a935950ffdf1
//                page_name:create news
//                page_title:pageeae
//                phone:7532866377
//                page_description:Page description
//                website:https://mywebsite.com
//                company:Criconet online
//                address:Gurgaon

                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("page_title", page_name);
                param.put("page_description", page_dess);
                param.put("twitter", "");
                param.put("facebook", "");
                param.put("address", "");
                param.put("website", "");
                param.put("linkedin", "");
                param.put("instagram", "");
                param.put("youtube", "");
                param.put("page_name", page_name);
                System.out.println("data   :" + param);

                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    String page_name, page_title;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                // addPageDialog();
                slideUp.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    Dialog dialog;

    private void addPageDialog() {
        dialog = new Dialog(Pages.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_page_dialog);
        final EditText et_page_name = (EditText) dialog.findViewById(R.id.page_name);
        final EditText et_page_desc = (EditText) dialog.findViewById(R.id.page_desc);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public static class Friends_Adapter extends BaseAdapter {
        Activity context;
        ViewHolder holder = null;
        LayoutInflater inflater;
        ArrayList<PageModel> arrayList_image;

        Friends_Adapter(Activity mcontext, ArrayList<PageModel> chatname_list1) {
            // TODO Auto-generated constructor stub
            context = mcontext;
            arrayList_image = chatname_list1;
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrayList_image.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.page_adapter_item, null);
                holder = new ViewHolder();
                holder.relative_dash = (MaterialCardView) convertView.findViewById(R.id.relative_dash);
                holder.user_image = (ImageView) convertView.findViewById(R.id.user_image);
                //holder.user_status = (ImageView) convertView.findViewById(R.id.user_status);
                holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
                holder.tvdes = (TextView) convertView.findViewById(R.id.tvdes);
                holder.fl_edit = (FrameLayout) convertView.findViewById(R.id.fl_edit);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
                holder.relative_dash.startAnimation(scaleUp);
            }

            holder.user_name.setText(arrayList_image.get(position).getPage_title());
            // holder.tvdes.setText(arrayList_image.get(position).getPage_description());
            Glide.with(context).load(arrayList_image.get(position).getAvatar()).into(holder.user_image);
            holder.fl_edit.setVisibility(View.VISIBLE);
            holder.fl_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditPage.class);
                    intent.putExtra("page_id", arrayList_image.get(position).getPage_id());
                    context.startActivity(intent);
                }
            });


            return convertView;
        }

        private class ViewHolder {
            ImageView user_image, user_status;
            TextView user_name, user_address, tvdes;
            FrameLayout fl_edit;
            MaterialCardView relative_dash;
        }
    }


    public class pageAdapter extends RecyclerView.Adapter<pageAdapter.MyViewHolder> {
        Activity context;
        LayoutInflater inflater;
        ArrayList<PageModel> arrayList_image;

        public pageAdapter(Activity mcontext, ArrayList<PageModel> chatname_list1) {
            context = mcontext;
            arrayList_image = chatname_list1;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_adapter_item, parent, false);
            pageAdapter.MyViewHolder vh = new pageAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(pageAdapter.MyViewHolder holder, final int position) {
            // set the data in items

            holder.user_name.setText(arrayList_image.get(position).getPage_title());
            holder.tvdes.setText(arrayList_image.get(position).getPage_description());
            Glide.with(context).load(arrayList_image.get(position).getAvatar()).into(holder.user_image);
            holder.fl_edit.setVisibility(View.VISIBLE);
            holder.fl_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditPage.class);
                    intent.putExtra("page_id", arrayList_image.get(position).getPage_id());
                    context.startActivity(intent);
                }
            });

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(Pages.this, PagesDetails.class);
                intent.putExtra("page_id", arrayList_image.get(position).getPage_id());
//                    intent.putExtra("friendStatus", arrayList_new.get(position).getFriendStatus());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return arrayList_image.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView user_image, user_status;
            TextView user_name, user_address, tvdes;
            FrameLayout fl_edit;
            MaterialCardView relative_dash;

            MyViewHolder(View itemView) {
                super(itemView);
                relative_dash = (MaterialCardView) itemView.findViewById(R.id.relative_dash);
                user_image = (ImageView) itemView.findViewById(R.id.user_image);
                //holder.user_status = (ImageView) convertView.findViewById(R.id.user_status);
                user_name = (TextView) itemView.findViewById(R.id.user_name);
                tvdes = (TextView) itemView.findViewById(R.id.tvdes);
                fl_edit = (FrameLayout) itemView.findViewById(R.id.fl_edit);
//                Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
//                relative_dash.startAnimation(scaleUp);


            }
        }
    }
}








