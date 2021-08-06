package com.pb.criconet.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.pb.criconet.R;
import com.pb.criconet.Utills.CustomLoaderView;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.Utills.SpaceItemDecoration;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.activity.MainActivity;
import com.pb.criconet.activity.YoutubePlayerActivity;
import com.pb.criconet.adapters.RecordedMatchAdapter;
import com.pb.criconet.models.VideoModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RecMatches extends Fragment {
    private SharedPreferences prefs;

    private View rootView;
    private RecyclerView weeklist;
    private ArrayList<VideoModel> data;
    private RecordedMatchAdapter adapter;
    private ProgressDialog progress;
    private RequestQueue queue;
    private TextView notfound;
    CustomLoaderView loaderView;
    EditText edit_search;
    FrameLayout fl_search;
    String search_value="";

    public static RecMatches newInstance() {
        return new RecMatches();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.rec_matches, container, false);
//        setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbartext);
        String text = "Recorded Matches";
        mTitle.setText(text.toUpperCase());
        ImageView img_addpost = toolbar.findViewById(R.id.img_addpost);
        TextView tv_post = toolbar.findViewById(R.id.tv_post);
        tv_post.setVisibility(View.GONE);
        img_addpost.setVisibility(View.GONE);
        img_addpost.setVisibility(View.GONE);
        ImageView img_close = toolbar.findViewById(R.id.img_close);
        img_close.setVisibility(View.GONE);
        loaderView = CustomLoaderView.initialize(getActivity());
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading");
        progress.setCancelable(false);
        edit_search = rootView.findViewById(R.id.edit_search);
        fl_search = rootView.findViewById(R.id.fl_search);

        weeklist = rootView.findViewById(R.id.week_list);
        weeklist.setLayoutManager(new LinearLayoutManager(getContext()));
        notfound = (TextView) rootView.findViewById(R.id.notfound);

        fl_search.setOnClickListener(v -> {
            search_value = edit_search.getText().toString().trim();

            if(search_value.isEmpty()){

            }else{
                if (Global.isOnline(getActivity())) {
                    getHelp();
                } else {
                    Global.showDialog(getActivity());
                }
            }


        });
        /*edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()==0) {
                    Toaster.customToast(s.toString());
                }else{
                }
            }
        });*/


        if (Global.isOnline(getActivity())) {
            getHelp();
        } else {
            Global.showDialog(getActivity());
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.bottomNavigation.setSelectedIndex(3, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:

                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getHelp() {
       // progress.show();
        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_recorded_video_lists",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loaderView.hideLoader();
                       // progress.dismiss();
                        try {
                            JSONObject jsonObject2, jsonObject = new JSONObject(response.toString());
                            if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                                data = new ArrayList<>();
                                edit_search.setText("");
                                data = VideoModel.fromJson(jsonObject.getJSONArray("data"));
//                                int size = data.size();
//                                size =0;
                                if (data.size() == 0) {
                                    notfound.setVisibility(View.VISIBLE);
                                } else {
                                    notfound.setVisibility(View.GONE);
                                    adapter = new RecordedMatchAdapter(data,getContext());
                                    weeklist.setAdapter(adapter);
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //progress.dismiss();
                        loaderView.hideLoader();
                        Global.msgDialog(getActivity(), "Error from server");
//                Global.msgDialog(getActivity(), "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("country_id", "101");
                param.put("state_id", "");
                param.put("search_key", search_value);

                System.out.println("data   " + param);
                return param;
//
//                country_id: {optional ) select box
//                    state_id:{optional ) select box
//                        city_id: {optional ) select box
//                            search_key { text }
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    /*public static class RecordedMatchAdapter extends BaseAdapter {
        Activity context;
        ViewHolder holder = null;
        LayoutInflater inflater;
        ArrayList<VideoModel> arrayList_image;

        RecordedMatchAdapter(Activity mcontext, ArrayList<VideoModel> chatname_list1) {
            // TODO Auto-generated constructor stub
            context = mcontext;
            arrayList_image = chatname_list1;
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arrayList_image.size();
//            return 10;
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

        private class ViewHolder {
            ImageView roundedImageView;
            TextView ground_name, team_name, location, date_time;
            ImageButton play_icon;
            MaterialCardView relative_dash;
            YouTubeThumbnailView ImageView;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.matches_adapter_item, null);
                holder = new ViewHolder();
                holder.relative_dash =  convertView.findViewById(R.id.relative_dash);
                holder.roundedImageView = (ImageView) convertView.findViewById(R.id.roundedImageView);
//                holder.ImageView = (YouTubeThumbnailView) convertView.findViewById(R.id.ImageView);
                holder.ground_name = (TextView) convertView.findViewById(R.id.ground_name);
                holder.team_name = (TextView) convertView.findViewById(R.id.team_name);
                holder.location = (TextView) convertView.findViewById(R.id.location);
                holder.date_time = (TextView) convertView.findViewById(R.id.date_time);
                holder.play_icon = (ImageButton) convertView.findViewById(R.id.play_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                Animation scaleUp = AnimationUtils.loadAnimation(context, R.anim.list_anim_side);
                holder.relative_dash.startAnimation(scaleUp);
            }
            holder.ground_name.setText(arrayList_image.get(position).getGround_name());
            holder.team_name.setText(arrayList_image.get(position).getTeam_name());
            holder.location.setText(arrayList_image.get(position).getLocation());
            holder.date_time.setText(arrayList_image.get(position).getDate_time());
            holder.play_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, YoutubePlayerActivity.class);
                    intent.putExtra("url", arrayList_image.get(position).getVideo_link());
                    context.startActivity(intent);
                }
            });

            try {
                Glide.with(context).load(arrayList_image.get(position).getVideo_details().getThumbnail_url())
                        .apply(new RequestOptions().placeholder(R.drawable.app_icon).dontAnimate())
                        .into(holder.roundedImageView);
            } catch (Exception e) {
                holder.roundedImageView.setImageResource(R.drawable.app_icon);
            }

            return convertView;
        }
    }*/
}
