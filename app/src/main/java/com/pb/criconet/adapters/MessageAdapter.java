package com.pb.criconet.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.pb.criconet.R;
import com.pb.criconet.models.MessageBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmMessageType;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private List<MessageBean> messageBeanList;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public MessageAdapter(Context context, List<MessageBean> messageBeanList, @NonNull OnItemClickListener listener) {
        this.inflater = ((Activity) context).getLayoutInflater();
        this.messageBeanList = messageBeanList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.msg_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        setupView(holder, position);
    }

    @Override
    public int getItemCount() {
        return messageBeanList.size();
    }

    @SuppressLint("CheckResult")
    private void setupView(MyViewHolder holder, int position) {
        MessageBean bean = messageBeanList.get(position);
        if (bean.isBeSelf()) {
            Glide.with(holder.itemView).load(bean.getAccount()).into(holder.textViewSelfName);
        } else {
            Glide.with(holder.itemView).load(bean.getAccount()).into(holder.textViewOtherName);
            if (bean.getBackground() != 0) {
                holder.textViewOtherName.setBackgroundResource(bean.getBackground());
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(bean);
        });

        RtmMessage rtmMessage = bean.getMessage();
        switch (rtmMessage.getMessageType()) {
            case RtmMessageType.TEXT:
                if (bean.isBeSelf()) {
                    holder.textViewSelfMsg.setVisibility(View.VISIBLE);
                    holder.textViewSelfMsg.setText(rtmMessage.getText());
                } else {
                    holder.textViewOtherMsg.setVisibility(View.VISIBLE);
                    holder.textViewOtherMsg.setText(rtmMessage.getText());
                }

                holder.frm_beself.setVisibility(View.GONE);
                holder.frm_other.setVisibility(View.GONE);
                break;
            case RtmMessageType.IMAGE:
                RtmImageMessage rtmImageMessage = (RtmImageMessage) rtmMessage;
                RequestBuilder<Drawable> builder = Glide.with(holder.itemView)
                        .load(rtmImageMessage.getThumbnail())
                        .override(rtmImageMessage.getThumbnailWidth(), rtmImageMessage.getThumbnailHeight());
                if (bean.isBeSelf()) {
                    holder.frm_beself.setVisibility(View.VISIBLE);
                    builder.into(holder.imageViewSelfImg);
                } else {
                    holder.frm_other.setVisibility(View.VISIBLE);
                    builder.into(holder.imageViewOtherImg);
                }

                holder.textViewSelfMsg.setVisibility(View.GONE);
                holder.textViewOtherMsg.setVisibility(View.GONE);
                break;

        }

        holder.layoutRight.setVisibility(bean.isBeSelf() ? View.VISIBLE : View.GONE);
        holder.layoutLeft.setVisibility(bean.isBeSelf() ? View.GONE : View.VISIBLE);
    }

    public interface OnItemClickListener {
        void onItemClick(MessageBean message);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView textViewOtherName;
        private TextView textViewOtherMsg;
        private ImageView imageViewOtherImg;
        private CircleImageView textViewSelfName;
        private TextView textViewSelfMsg;
        private ImageView imageViewSelfImg;
        private RelativeLayout layoutLeft;
        private RelativeLayout layoutRight;
        private FrameLayout frm_beself,frm_other;

        MyViewHolder(View itemView) {
            super(itemView);

            textViewOtherName = itemView.findViewById(R.id.item_name_l);
            textViewOtherMsg = itemView.findViewById(R.id.item_msg_l);
            imageViewOtherImg = itemView.findViewById(R.id.item_img_l);
            textViewSelfName = itemView.findViewById(R.id.item_name_r);
            textViewSelfMsg = itemView.findViewById(R.id.item_msg_r);
            imageViewSelfImg = itemView.findViewById(R.id.item_img_r);
            frm_beself=itemView.findViewById(R.id.frm_beself);
            frm_other=itemView.findViewById(R.id.frm_other);
            layoutLeft = itemView.findViewById(R.id.item_layout_l);
            layoutRight = itemView.findViewById(R.id.item_layout_r);
        }
    }
}
