package com.pb.criconet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pb.criconet.R;
import com.pb.criconet.Utills.GridSpacingItemDecoration;
import com.pb.criconet.adapters.TimeAdapter;
import com.pb.criconet.event.SlotId;
import com.pb.criconet.models.TimeSlot;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimePreode extends BottomSheetDialogFragment implements TimeAdapter.timeSelect {

    private View rootView;
    private RecyclerView recyclerView;
    private ImageView textViewClose;
    private TimeSlot modelArrayList;

    public TimePreode(TimeSlot modelArrayList) {
        // Required empty public constructor
        this.modelArrayList = modelArrayList;
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_time, container, false);


        return rootView;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textViewClose = rootView.findViewById(R.id.textViewClose);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 20, false));
        recyclerView.setAdapter(new TimeAdapter(getActivity(), modelArrayList.getData(), TimePreode.this::getSlotId));
        textViewClose.setOnClickListener(view -> dismiss());

    }

    @Override
    public void getSlotId(String sloteId) {
        EventBus.getDefault().post(new SlotId(sloteId,null));
        dismiss();
    }
}
