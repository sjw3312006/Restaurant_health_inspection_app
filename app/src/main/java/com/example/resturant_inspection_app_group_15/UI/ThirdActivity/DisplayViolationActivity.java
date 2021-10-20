package com.example.resturant_inspection_app_group_15.UI.ThirdActivity;

/*
        DisplayViolationActivity.java class will actually display the cardView list we made.
        This works directly off of the violation_row.xml we made.
        So it only shows what happens with 1 list item,
        However the inflate from Adapter will grow everything to have this same style.
*/

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.UI.ItemClickListener;

public class DisplayViolationActivity extends RecyclerView.ViewHolder implements View.OnClickListener {
    ItemClickListener itemClickListener;
    ImageView ivViolation;
    ImageView ivSeverity;
    TextView tvDescription;

    public DisplayViolationActivity(@NonNull View itemView) {
        super(itemView);

        this.ivViolation = itemView.findViewById(R.id.violation);
        this.ivSeverity = itemView.findViewById(R.id.severity_violation);
        this.tvDescription = itemView.findViewById(R.id.ViolationDescription);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClickListener(v,getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
