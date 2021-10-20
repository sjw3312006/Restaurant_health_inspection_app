package com.example.resturant_inspection_app_group_15.UI.FirstActivity;

/*
        DisplayListActivity.java class will actually display the cardView list we made.
        This works directly off of the row.xml we made.
        So it only shows what happens with 1 list item,
        However the inflate from Adapter will grow everything to have this same style
*/

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.UI.ItemClickListener;

import java.util.Objects;

public class DisplayListActivity extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView restaurantImg, restaurantPic;
    TextView restaurantDesc, restaurantDesc2, restaurantName;
    ItemClickListener itemClickListener;

    public DisplayListActivity(@NonNull View itemView) {
        super(itemView);

        this.restaurantImg = itemView.findViewById(R.id.restaurantImage);
        this.restaurantDesc = itemView.findViewById(R.id.restaurantDescription);
        this.restaurantName = itemView.findViewById(R.id.restaurantName);
        this.restaurantPic = itemView.findViewById(R.id.restaurantPic);
        this.restaurantDesc2 = itemView.findViewById(R.id.restaurantDescription2);

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
