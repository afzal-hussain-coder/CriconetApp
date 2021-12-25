package com.pb.criconet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.pb.criconet.R;
import com.pb.criconet.models.BookingHistory;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookingDetails extends AppCompatActivity {

    private BookingHistory.Datum datum;
    private CircleImageView circleImageView;
    private TextView tvCoachName;
    private TextView tvBookingDate;
    private TextView tvTime;
    private TextView tvAmount;
    private TextView tvStatusLabel;
    private AppCompatActivity mActivity=BookingDetails.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        datum= (BookingHistory.Datum) getIntent().getSerializableExtra("MyClass");

        toolbar.setNavigationOnClickListener(v -> finish());

        circleImageView=findViewById(R.id.ivProfileImage);
        tvCoachName=findViewById(R.id.tvCoachName);
        tvBookingDate=findViewById(R.id.tvBookingDate);
        tvTime=findViewById(R.id.tvTime);
        tvAmount=findViewById(R.id.tvAmount);
        tvStatusLabel=findViewById(R.id.tvStatusLabel);

        Glide.with(mActivity).load(datum.getAvatar())
                .into(circleImageView);
        tvCoachName.setText(datum.getName());
        tvTime.setText(datum.getBookingSlotTxt());
        tvBookingDate.setText(datum.getBookingSlotDate());
        tvAmount.setText(getResources().getString(R.string.Payment_amount)+"\u20B9"+datum.getBookingAmount()+getResources().getString(R.string.Slot));
        tvStatusLabel.setText(datum.getBookingStatus());


    }
}
