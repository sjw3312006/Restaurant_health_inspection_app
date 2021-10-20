package com.example.resturant_inspection_app_group_15.UI.SecondActivity;

/*
        DisplayInspectionActivity.java class will actually display the cardView list we made.
        This works directly off of the inspection_row.xml we made.
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

public class DisplayInspectionActivity extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView inspectionImg;
    TextView numCritical;
    TextView numNonCritical;
    TextView inspectionDesc;
    TextView inspectionDesc2;
    TextView inspectionTime;
    ItemClickListener itemClickListener;

    public DisplayInspectionActivity(@NonNull View itemView) {
        super(itemView);
        this.inspectionImg = itemView.findViewById(R.id.inspectionImage);
        this.inspectionDesc = itemView.findViewById(R.id.inspectionDescription);
        this.inspectionTime = itemView.findViewById(R.id.inspectionTime);
        this.inspectionDesc2 = itemView.findViewById(R.id.inspectionDescription2);

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

