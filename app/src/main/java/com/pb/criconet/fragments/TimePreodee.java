package com.pb.criconet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pb.criconet.R;
import com.pb.criconet.Utills.GridSpacingItemDecoration;
import com.pb.criconet.Utills.Toaster;
import com.pb.criconet.adapters.TimeAdapter;
import com.pb.criconet.adapters.TimeAdaptercoach;
import com.pb.criconet.event.SlotId;
import com.pb.criconet.models.TimeSlot;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TimePreodee extends BottomSheetDialogFragment implements TimeAdaptercoach.timeSelectt {

    private View rootView;
    private RecyclerView recyclerView;
    private ImageView textViewClose;
    private Button btn_done;

    private TimeSlot modelArrayList;
    ArrayList<String> arrayListselectedSlot= new ArrayList<>();
    selectedSlot  selectedSlot;

    public TimePreodee(TimeSlot modelArrayList) {
        // Required empty public constructor
        this.modelArrayList = modelArrayList;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        selectedSlot = (selectedSlot)context ;

    }

//    @Override
//    public void onAttachFragment(@NonNull @NotNull Fragment childFragment) {
//        super.onAttachFragment(childFragment);
//        selectedSlot = (selectedSlot)childFragment ;
//
//    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_time_coach, container, false);


        return rootView;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textViewClose = rootView.findViewById(R.id.textViewClose);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        btn_done = rootView.findViewById(R.id.btn_done);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 20, false));
        recyclerView.setAdapter(new TimeAdaptercoach(getActivity(), modelArrayList.getData(), TimePreodee.this::getSlotId));
        textViewClose.setOnClickListener(view -> dismiss());



        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayListselectedSlot.size()>0){
                    dismiss();
                    selectedSlot.getSelectedSlot(arrayListselectedSlot);
                }else{
                    Toaster.customToast("Select Time Slot");
                }
                
            }
        });

    }

    @Override
    public void getSlotId(ArrayList<String> arrayListUser) {
        arrayListselectedSlot=arrayListUser;
       // Toaster.customToast(arrayListUser.get(0)+"");
       // EventBus.getDefault().post(new SlotId(sloteId,null));
        //dismiss();
    }

    public interface selectedSlot{
        public void getSelectedSlot(ArrayList<String> arrayListUser);
    }
}
