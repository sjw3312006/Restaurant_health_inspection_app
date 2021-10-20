package com.example.resturant_inspection_app_group_15.UI.FirstActivity;

/*
        AdapterForDisplay.java class will make the adapter needed for recyclerView (for list of all restaurants).
        It inflates the row xml we made, so we can add multiple items to our list all with the same 'look'.
        This also contains the onItemListener to start second activity with certain info from intent.
        */

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.Restaurant;
import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.UI.ItemClickListener;
import com.example.resturant_inspection_app_group_15.UI.SecondActivity.clickRestaurant;

import java.util.ArrayList;

public class AdapterForDisplay extends RecyclerView.Adapter<DisplayListActivity> {

    Context c;
    ArrayList<Restaurant> restaurants;

    public AdapterForDisplay(Context c, ArrayList<Restaurant> restaurants) {
        this.c = c;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public DisplayListActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);
        return new DisplayListActivity(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DisplayListActivity holder, int position) {
        holder.restaurantName.setText(restaurants.get(position).getName());
        holder.restaurantDesc.setText(restaurants.get(position).getDescription());
        holder.restaurantDesc2.setText(restaurants.get(position).getDescription2());
        String resName = restaurants.get(position).getName();

        //holder.restaurantImg.setImageResource(R.drawable.med);

        //System.out.println("The hrating for restaurant " + restaurants.get(position).getName() + " " + restaurants.get(position).getLatestInspection().getHazardRating());
        if (restaurants.get(position).getLatestInspection() == null) {
            holder.restaurantImg.setImageResource(R.drawable.good);
            //System.out.println(restaurants.get(position).getLatestInspection().getHazardRating());
        } else if (restaurants.get(position).getLatestInspection().getHazardRating().equals("Low")) {
            holder.restaurantImg.setImageResource(R.drawable.good);
        } else if (restaurants.get(position).getLatestInspection().getHazardRating().equals("Moderate")) {
            holder.restaurantImg.setImageResource(R.drawable.med);
        } else if (restaurants.get(position).getLatestInspection().getHazardRating().equals("High")) {
            holder.restaurantImg.setImageResource(R.drawable.bad);
        }

        if (resName.contains("104 Sushi & Co.")) {
            System.out.println("breaks here?" + restaurants.get(position).getName());
            holder.restaurantPic.setImageResource(R.drawable.sushi);
        } else if (resName.contains("Top in Town Pizza")) {
            holder.restaurantPic.setImageResource(R.drawable.pizza);
        } else if (resName.contains("Tim Hortons")) {
            holder.restaurantPic.setImageResource(R.drawable.timhortons);
        } else if (resName.contains("7-Eleven")) {
            holder.restaurantPic.setImageResource(R.drawable.seveneleven);
        } else if (resName.contains("A&W") || resName.contains("A & W")) {
            holder.restaurantPic.setImageResource(R.drawable.anw);
        } else if (resName.contains("Subway")) {
            holder.restaurantPic.setImageResource(R.drawable.subway);
        } else if (resName.contains("Domino's Pizza")) {
            holder.restaurantPic.setImageResource(R.drawable.dominospizza);
        } else if (resName.contains("McDonald's")) {
            holder.restaurantPic.setImageResource(R.drawable.mcdonalds);
        } else if (resName.contains("Starbucks Coffee")) {
            holder.restaurantPic.setImageResource(R.drawable.starbucks);
        } else if (resName.contains("Wendy's")) {
            holder.restaurantPic.setImageResource(R.drawable.wendys);
        } else if (resName.contains("Boston Pizza")) {
            holder.restaurantPic.setImageResource(R.drawable.bp);
        }
        else if (restaurants.get(position).isFavStatus()){
            holder.restaurantPic.setImageDrawable(null);
            holder.restaurantPic.setImageResource(R.drawable.hearts);
        }
        else {
            holder.restaurantPic.setImageResource(R.drawable.food);
        }

        if (restaurants.get(position).isFavStatus()) {
            holder.restaurantPic.setImageDrawable(null);
            holder.restaurantPic.setImageResource(R.drawable.hearts);

        }


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String rName = restaurants.get(position).getName();
                String rDescription = restaurants.get(position).getDescription();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.restaurantImg.getDrawable();

                Intent intent = new Intent(c, clickRestaurant.class);

                intent.putExtra("Position", position);
                intent.putExtra("Desc", rDescription);
                intent.putExtra("Name", rName);


                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void submit(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public void clear() {
        this.restaurants.clear();
    }
}
