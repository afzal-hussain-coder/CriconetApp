package com.pb.criconet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pb.criconet.R;
import com.pb.criconet.models.FeedbackModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.myViewHolder>{

    private ArrayList<FeedbackModel> feedbackModelList;
    private Context mContext;
    private int lastSelectedPosition = -1;
    private  radioClickListener radioClickListener;

    public FeedbackAdapter(ArrayList<FeedbackModel> feedbackModelList,Context mContext ,radioClickListener radioClickListener){
        this.feedbackModelList= feedbackModelList;
        this.mContext= mContext;
        this.radioClickListener =radioClickListener;

    }

    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(mContext).inflate(R.layout.feedback_item,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FeedbackAdapter.myViewHolder holder, int position) {
        FeedbackModel  feedbackModel = feedbackModelList.get(position);
        holder.tv_message.setText(feedbackModel.getQuestion());

        holder.radio.setChecked(lastSelectedPosition == position);


    }

    @Override
    public int getItemCount() {
        return feedbackModelList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private RadioButton radio;
        private TextView tv_message;
        private LinearLayout li_edit;
        private EditText edit_type_feedback;
        public RadioButton selectionState;

        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            radio = itemView.findViewById(R.id.radio);
            tv_message = itemView.findViewById(R.id.tv_message);

            radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getLayoutPosition();
                    radioClickListener.radioClick(feedbackModelList.get(lastSelectedPosition).getQuestion_id(),lastSelectedPosition);
                    notifyDataSetChanged();

                }
            });
        }


    }

    public interface radioClickListener{
         void radioClick(String message,int pos);
    }
}
