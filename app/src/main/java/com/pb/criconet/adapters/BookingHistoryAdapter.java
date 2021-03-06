package com.pb.criconet.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.Utills.Global;
import com.pb.criconet.Utills.SessionManager;
import com.pb.criconet.models.BookingHistory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.MyViewHolder> {

    private Context context;
    private List<BookingHistory.Datum> data;
    private clickCallback callback;
    String notification_count = "";
    private SharedPreferences prefs;
    private RequestQueue queue;

    public BookingHistoryAdapter(Context context, List<BookingHistory.Datum> data, clickCallback callback) {
        this.context = context;
        this.data = data;
        this.callback = callback;
        this.notification_count = notification_count;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        queue = Volley.newRequestQueue(context);

    }

    @Override
    public BookingHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_booking_history, parent, false);
        BookingHistoryAdapter.MyViewHolder vh = new BookingHistoryAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(BookingHistoryAdapter.MyViewHolder holder, final int position) {
        // set the data in items
        holder.tvCoachName.setText(Global.capitalizeFirstLatterOfString(data.get(position).getName()));
        holder.tv_viewDetails.setOnClickListener(view -> {
            callback.viewDetails(data.get(position));
        });

        holder.fl_chat.setOnClickListener(v -> {
            callback.chatClick(data.get(position).getCoachUserId(), data.get(position).getUserId());
        });

        if (data.get(position).getBtn2().equalsIgnoreCase("join")) {
            holder.btn2.setOnClickListener(v -> {
                callback.buttonClick(data.get(position).getId(), data.get(position).getDuration_in_milisecond(), data.get(position).getBtn2(), data.get(position).getChanel_id(), data.get(position).getBookingId(),
                        data.get(position).getUserId(), data.get(position).getCoachUserId(), data.get(position).getName());
            });
        }

        if(data.get(position).getAcademy_id().isEmpty() || data.get(position).getAcademy_id().equalsIgnoreCase("0")||data.get(position).getAcademy_id().equalsIgnoreCase("null")){
            holder.lay_academy.setVisibility(View.GONE);
            holder.lay_user.setVisibility(View.VISIBLE);
            Glide.with(context).load(data.get(position).getAvatar())
                    .into(holder.ivProfileImage);

            holder.tvTime.setText(Global.convertUTCDateToLocalDate(data.get(position).getBookingSlotDate()) + " , " + data.get(position).getBookingSlotTxt());
            holder.tvSlotDuration.setText(data.get(position).getSlotDuration() + context.getResources().getString(R.string.min));
            holder.tvBookingDateTime.setText(data.get(position).getBooking_date());
            // holder.tvBookingDateTime.setText(data.get(position).getBooking_date());

//        holder.btn2.setOnClickListener(view -> {
//            Intent intent=new Intent(context, BookingDetails.class);
//            intent.putExtra("MyClass", data.get(position));
//            context.startActivity(intent);
//        });

            if (data.get(position).getBtn1() != null && !data.get(position).getBtn1().equalsIgnoreCase("")) {
                holder.btn1.setVisibility(View.VISIBLE);
                holder.tv_confirmed.setText(data.get(position).getBtn1());
            } else {
                holder.btn1.setVisibility(View.GONE);

            }

            if (data.get(position).getBtn1().equalsIgnoreCase("Confirmed")) {
                holder.fl_chat.setVisibility(View.VISIBLE);
                if (data.get(position).getMessage_counter().equalsIgnoreCase("0")) {
                    holder.rl_count.setVisibility(View.GONE);
                } else {
                    holder.rl_count.setVisibility(View.VISIBLE);
                    holder.tv_count.setText(data.get(position).getMessage_counter());
                }
            } else {
                holder.rl_count.setVisibility(View.GONE);
                holder.fl_chat.setVisibility(View.GONE);
            }

            if (data.get(position).getBtn2() != null && !data.get(position).getBtn2().equalsIgnoreCase("")) {

                try {
                    if (data.get(position).getChanel_id().equalsIgnoreCase("")) {
                        holder.btn2.setVisibility(View.GONE);
                    } else {
                        holder.btn2.setVisibility(View.VISIBLE);
                        holder.tv_join.setText(data.get(position).getBtn2() + " " + context.getResources().getString(R.string.session));
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            } else {
                holder.btn2.setVisibility(View.GONE);
            }


        }else{
            holder.lay_user.setVisibility(View.GONE);
            holder.lay_academy.setVisibility(View.VISIBLE);

            Glide.with(context).load(data.get(position).getLogo())
                    .into(holder.ivProfileImagee_academy);

            holder.tvAcademyName.setText(data.get(position).getAcademy_name());
            holder.tv_sessio_time_academy.setText(data.get(position).getBookingSlotTxt());
            holder.tvAddress_academy.setText(data.get(position).getAddress());
            holder.tvBookingId_academy.setText(data.get(position).getBookingId());
            holder.tv_valid.setText(context.getResources().getString(R.string.valid_for)+" "+data.get(position).getPackage_valid_for());
            // holder.tvBookingDateTime.setText(data.get(position).getBooking_date());

//        holder.btn2.setOnClickListener(view -> {
//            Intent intent=new Intent(context, BookingDetails.class);
//            intent.putExtra("MyClass", data.get(position));
//            context.startActivity(intent);
//        });

            if (data.get(position).getBtn1() != null && !data.get(position).getBtn1().equalsIgnoreCase("")) {
                holder.btn1.setVisibility(View.VISIBLE);
                holder.tv_confirmed.setText(data.get(position).getBtn1());
            } else {
                holder.btn1.setVisibility(View.GONE);

            }

            if (data.get(position).getBtn1().equalsIgnoreCase("Confirmed")) {
                holder.fl_chat.setVisibility(View.VISIBLE);
                if (data.get(position).getMessage_counter().equalsIgnoreCase("0")) {
                    holder.rl_count.setVisibility(View.GONE);
                } else {
                    holder.rl_count.setVisibility(View.VISIBLE);
                    holder.tv_count.setText(data.get(position).getMessage_counter());
                }
            } else {
                holder.rl_count.setVisibility(View.GONE);
                holder.fl_chat.setVisibility(View.GONE);
            }

            if (data.get(position).getBtn2() != null && !data.get(position).getBtn2().equalsIgnoreCase("")) {

                try {
                    if (data.get(position).getChanel_id().equalsIgnoreCase("")) {
                        holder.btn2.setVisibility(View.GONE);
                    } else {
                        holder.btn2.setVisibility(View.VISIBLE);
                        holder.tv_join.setText(data.get(position).getBtn2() + " " + context.getResources().getString(R.string.session));
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            } else {
                holder.btn2.setVisibility(View.GONE);
            }
        }



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvCoachName;
        TextView tvTime;
        TextView tvBookingDateTime;
        FrameLayout btn1;
        FrameLayout btn2;
        FrameLayout tv_viewDetails, fl_chat;
        TextView tvSlotDuration;
        TextView tv_confirmed;
        TextView tv_join;
        RelativeLayout constraint;
        TextView tv_count;
        RelativeLayout rl_count;
        RelativeLayout lay_academy, lay_user;
        ImageView ivProfileImagee_academy;
        TextView tvAcademyName, tvAddress_academy, tvBookingId_academy, tv_valid, tv_sessio_time_academy;

        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvCoachName = itemView.findViewById(R.id.tvCoachName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvBookingDateTime = itemView.findViewById(R.id.tvBookingDateTime);
            constraint = itemView.findViewById(R.id.constraint);
            btn1 = itemView.findViewById(R.id.btn1);
            btn2 = itemView.findViewById(R.id.btn2);
            tv_confirmed = itemView.findViewById(R.id.tv_confirmed);
            tv_join = itemView.findViewById(R.id.tv_join);
            tv_viewDetails = itemView.findViewById(R.id.tv_viewDetails);
            tvSlotDuration = itemView.findViewById(R.id.tvSlotDuration);
            fl_chat = itemView.findViewById(R.id.fl_chat);
            rl_count = itemView.findViewById(R.id.rl_count);
            tv_count = itemView.findViewById(R.id.tv_count);

//            academy ids...
            lay_academy = itemView.findViewById(R.id.lay_academy);
            lay_user = itemView.findViewById(R.id.lay_user);
            tvAcademyName = itemView.findViewById(R.id.tvAcademyName);
            tvAddress_academy = itemView.findViewById(R.id.tvAddress_academy);
            tvBookingId_academy = itemView.findViewById(R.id.tvBookingId_academy);
            tv_valid = itemView.findViewById(R.id.tv_valid);
            tv_sessio_time_academy = itemView.findViewById(R.id.tv_sessio_time_academy);
            ivProfileImagee_academy = itemView.findViewById(R.id.ivProfileImagee_academy);


        }
    }

    public interface clickCallback {
        void buttonClick(String id, long timeDuration, String action, String channel_id, String booking_id, String userid, String coachid, String coachName);

        void viewDetails(BookingHistory.Datum data);

        void chatClick(String coachId, String userId);
    }


}