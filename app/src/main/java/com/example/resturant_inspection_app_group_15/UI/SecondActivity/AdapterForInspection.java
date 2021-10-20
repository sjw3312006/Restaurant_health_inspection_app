package com.example.resturant_inspection_app_group_15.UI.SecondActivity;

/*
        AdapterForInspection.java class will make the adapter needed for recyclerView (for list of inspections)
        It inflates the inspection_row xml we made, so we can add multiple items to our list all with the same 'look'.
        This also contains the onItemListener to start third activity with certain info from intent.
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resturant_inspection_app_group_15.Model.InspectionPackage.Inspections;
import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.UI.ItemClickListener;
import com.example.resturant_inspection_app_group_15.Model.CSV.ListModifiers;

import com.example.resturant_inspection_app_group_15.UI.ThirdActivity.clickInspection;

import java.util.ArrayList;

public class AdapterForInspection extends RecyclerView.Adapter<DisplayInspectionActivity> {

    Context c;
    ArrayList<Inspections> inspections;
    int inspection_position = 0;
    int restaurant_position = 0;
    String hazard_level = "";

    public AdapterForInspection(Context c, int restaurant_position, ArrayList<Inspections> inspections) {
        this.c = c;
        this.inspections = inspections;

        this.restaurant_position = restaurant_position;
    }

    @NonNull
    @Override
    public DisplayInspectionActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inspection_row,null);
        return new DisplayInspectionActivity(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DisplayInspectionActivity holder, int position) {
        final String intelligentTime = ListModifiers.inspectionDateToIntelligentFormat(inspections.get(position));
        holder.inspectionTime.setText(intelligentTime);
        holder.inspectionDesc.setText("" + inspections.get(position).getNumCritical());
        holder.inspectionDesc2.setText("" + inspections.get(position).getNumNonCritical());
        //holder.inspectionImg.setImageResource(R.drawable.med);
        //System.out.println("It is : " + inspections.get(position).getHazardRating());
        if(inspections.size() == 0){
            holder.inspectionImg.setImageResource(R.drawable.good);
            inspections.get(position).setHazardRating("Low");
        }
        else if(inspections.get(position).getHazardRating().equals("Low")){
            holder.inspectionImg.setImageResource(R.drawable.good);
            inspections.get(position).setHazardRating("Low");
        }
        else if(inspections.get(position).getHazardRating().equals("Moderate")){
            holder.inspectionImg.setImageResource(R.drawable.med);
            inspections.get(position).setHazardRating("Moderate");
        }
        else if(inspections.get(position).getHazardRating().equals("High")){
            holder.inspectionImg.setImageResource(R.drawable.bad);
            inspections.get(position).setHazardRating("High");
        }
        //System.out.println("It is also: " + hazard_level);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                inspection_position = position;
                //System.out.println("The hazard = " + hazard_level);
                String iTime = ListModifiers.inspectionDateToIntelligentFormat(inspections.get(position));
                String iDescription = "Critical: " + inspections.get(position).getNumCritical() + "\nNon Critical: " +  inspections.get(position).getNumNonCritical();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.inspectionImg.getDrawable();

                Intent intent = new Intent(c, clickInspection.class);
                //intent.putExtra("Position",position);
                //intent.putExtra("Desc",iDescription);
                //intent.putExtra("Name",iTime);
                intent.putExtra("restaurant_position", restaurant_position);
                intent.putExtra("inspection_position",inspection_position);
                intent.putExtra("critical", inspections.get(position).getNumCritical());
                intent.putExtra("noncritical", inspections.get(position).getNumNonCritical());
                intent.putExtra("type", inspections.get(position).getInspType());
                intent.putExtra("date", ListModifiers.inspectionDateToIntelligentFormat(inspections.get(position)));
                intent.putExtra("hazard_level", inspections.get(position).getHazardRating());
                //System.out.println("The hazard level is: " + hazard_level);
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inspections.size();
    }
}
