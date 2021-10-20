package com.example.resturant_inspection_app_group_15.UI.FavouriteActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.Restaurant;
import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.UI.FirstActivity.DisplayListActivity;
import com.example.resturant_inspection_app_group_15.UI.ItemClickListener;
import com.example.resturant_inspection_app_group_15.UI.SecondActivity.clickRestaurant;

import java.util.ArrayList;

public class AdapterForFavourite extends RecyclerView.Adapter<DisplayFavourite> {
    Context c;
    ArrayList<Restaurant> restaurants;

    public AdapterForFavourite(Context c,ArrayList<Restaurant> restaurants) {
        this.c = c;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public DisplayFavourite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_row,null);
        return new DisplayFavourite(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DisplayFavourite holder, int position) {
        holder.restaurantName.setText(restaurants.get(position).getName());
        holder.restaurantDesc.setText(restaurants.get(position).getDescription());
        String resName = restaurants.get(position).getName();

        //holder.restaurantImg.setImageResource(R.drawable.med);

        //System.out.println("The hrating for restaurant " + restaurants.get(position).getName() + " " + restaurants.get(position).getLatestInspection().getHazardRating());
        if(restaurants.get(position).getLatestInspection() == null){
            holder.restaurantImg.setImageResource(R.drawable.good);
        }
        else if(restaurants.get(position).getLatestInspection().getHazardRating().equals("Low")){
            holder.restaurantImg.setImageResource(R.drawable.good);
        }
        else if(restaurants.get(position).getLatestInspection().getHazardRating().equals("Moderate")){
            holder.restaurantImg.setImageResource(R.drawable.med);
        }
        else if(restaurants.get(position).getLatestInspection().getHazardRating().equals("High")){
            holder.restaurantImg.setImageResource(R.drawable.bad);
        }

        if(resName.contains("104 Sushi & Co.")){
            System.out.println("breaks here?" + restaurants.get(position).getName());
            holder.restaurantPic.setImageResource(R.drawable.sushi);
        }
        else if(resName.contains("Top in Town Pizza")){
            holder.restaurantPic.setImageResource(R.drawable.pizza);
        }
        else if(resName.contains("Tim Hortons")){
            holder.restaurantPic.setImageResource(R.drawable.timhortons);
        }
        else if(resName.contains("7-Eleven")){
            holder.restaurantPic.setImageResource(R.drawable.seveneleven);
        }
        else if(resName.contains("A&W") || resName.contains("A & W")){
            holder.restaurantPic.setImageResource(R.drawable.anw);
        }
        else if(resName.contains("Subway")){
            holder.restaurantPic.setImageResource(R.drawable.subway);
        }
        else if(resName.contains("Domino's Pizza")){
            holder.restaurantPic.setImageResource(R.drawable.dominospizza);
        }
        else if(resName.contains("McDonald's")){
            holder.restaurantPic.setImageResource(R.drawable.mcdonalds);
        }
        else if(resName.contains("Starbucks Coffee")){
            holder.restaurantPic.setImageResource(R.drawable.starbucks);
        }
        else if(resName.contains("Wendy's")){
            holder.restaurantPic.setImageResource(R.drawable.wendys);
        }
        else if(resName.contains("Boston Pizza")){
            holder.restaurantPic.setImageResource(R.drawable.bp);
        }
        else {
            holder.restaurantPic.setImageResource(R.drawable.food);
        }



        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String rName = restaurants.get(position).getName();
                String rDescription = restaurants.get(position).getDescription();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.restaurantImg.getDrawable();
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
