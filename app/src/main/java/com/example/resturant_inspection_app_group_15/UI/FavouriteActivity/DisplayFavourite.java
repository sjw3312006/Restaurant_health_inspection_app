package com.example.resturant_inspection_app_group_15.UI.FavouriteActivity;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.UI.ItemClickListener;

public class DisplayFavourite extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView restaurantImg, restaurantPic;
    TextView restaurantDesc,restaurantName;
    ItemClickListener itemClickListener;

    public DisplayFavourite(@NonNull View itemView) {
        super(itemView);

        this.restaurantImg = itemView.findViewById(R.id.img_fav_hazard);
        this.restaurantDesc = itemView.findViewById(R.id.txt_fav_desc);
        this.restaurantName = itemView.findViewById(R.id.txt_fav_name);
        this.restaurantPic = itemView.findViewById(R.id.img_fav_icon);

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

