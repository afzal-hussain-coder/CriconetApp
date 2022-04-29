package com.pb.criconet.Utills;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    public HorizontalSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.right = space;

        // Add top margin only for the first item to avoid double space between items
        if(parent.getChildAdapterPosition(view) == 0) {
            outRect.left = space;
        } else {
            outRect.left = 0;
        }

    }

}
