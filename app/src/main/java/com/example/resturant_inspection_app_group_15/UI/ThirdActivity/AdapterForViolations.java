package com.example.resturant_inspection_app_group_15.UI.ThirdActivity;

/*
        AdapterForViolations.java class will make the adapter needed for recyclerView (for scrollable list of violations)
        It inflates the violation_row xml we made, so we can add multiple items to our list all with the same 'look'.
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resturant_inspection_app_group_15.Model.ViolationPackage.Violations;
import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.UI.ItemClickListener;
import com.example.resturant_inspection_app_group_15.UI.SecondActivity.clickRestaurant;

import java.util.ArrayList;
import java.util.List;

public class AdapterForViolations extends RecyclerView.Adapter<DisplayViolationActivity> {
    Context c;
    ArrayList<Violations> violations;

    public AdapterForViolations(Context c, ArrayList<Violations> violations) {
        this.c = c;
        this.violations = violations;
    }

    @NonNull
    @Override
    public DisplayViolationActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.violation_row,parent, false);
        return new DisplayViolationActivity(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayViolationActivity holder, int position) {
        List<String> operationList = new ArrayList<String>();
        operationList.add("101");
        operationList.add("102");
        operationList.add("103");
        operationList.add("104");
        operationList.add("311");
        operationList.add("314");
        operationList.add("401");
        operationList.add("402");
        operationList.add("403");
        operationList.add("404");
        operationList.add("501");
        operationList.add("502");

        List<String> foodList = new ArrayList<String>();
        foodList.add("201");
        foodList.add("202");
        foodList.add("203");
        foodList.add("204");
        foodList.add("205");
        foodList.add("206");
        foodList.add("208");
        foodList.add("209");
        foodList.add("210");
        foodList.add("211");
        foodList.add("212");
        foodList.add("306");

        List<String> equipList = new ArrayList<String>();
        equipList.add("301");
        equipList.add("302");
        equipList.add("303");
        equipList.add("307");
        equipList.add("308");
        equipList.add("309");
        equipList.add("310");
        equipList.add("312");
        equipList.add("315");

        List<String> pestList = new ArrayList<String>();
        pestList.add("304");
        pestList.add("305");
        pestList.add("315");


        //display description
        final String desc = violations.get(position).getDescription();

        //display critical/non-critical image
        if(violations.get(position).isItCritical()==true){
            holder.ivSeverity.setImageResource(R.drawable.bad);
        }
        else {
            holder.ivSeverity.setImageResource(R.drawable.good);
        }

        //display image of violation type
        String vio = violations.get(position).getNature();
        if (pestList.contains(vio)){
            holder.ivViolation.setImageResource(R.drawable.pest);
            holder.tvDescription.setText(R.string.pests_message);
            System.out.printf(String.valueOf(R.string.pests_message));
        }
        else if(equipList.contains(vio)){
            holder.ivViolation.setImageResource(R.drawable.equipment);
            holder.tvDescription.setText(R.string.equip_message);
            System.out.printf(String.valueOf(R.string.equip_message));
        }
        else if(foodList.contains(vio)){
            holder.ivViolation.setImageResource(R.drawable.food);
            holder.tvDescription.setText(R.string.food_message);
            System.out.printf(String.valueOf(R.string.food_message));
        }
        else{
            holder.ivViolation.setImageResource(R.drawable.operation);
            holder.tvDescription.setText(R.string.operation_message);
            System.out.printf(String.valueOf(R.string.operation_message));
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                //checks to see what violation message to post in the the toast
                if(desc.contains("101")){
                    Toast.makeText(c, R.string.v101 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("102")){
                    Toast.makeText(c, R.string.v102 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("103")){
                    Toast.makeText(c, R.string.v103 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("104")){
                    Toast.makeText(c, R.string.v104 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("201")){
                    Toast.makeText(c, R.string.v201 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("202")){
                    Toast.makeText(c, R.string.v202 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("203")){
                    Toast.makeText(c, R.string.v203 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("204")){
                    Toast.makeText(c, R.string.v204 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("205")){
                    Toast.makeText(c, R.string.v205 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("206")){
                    Toast.makeText(c, R.string.v206 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("208")){
                    Toast.makeText(c, R.string.v208 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("209")){
                    Toast.makeText(c, R.string.v209 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("210")){
                    Toast.makeText(c, R.string.v210 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("211")){
                    Toast.makeText(c, R.string.v211 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("212")){
                    Toast.makeText(c, R.string.v212 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("301")){
                    Toast.makeText(c, R.string.v301 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("302")){
                    Toast.makeText(c, R.string.v302 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("303")){
                    Toast.makeText(c, R.string.v303 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("304")){
                    Toast.makeText(c, R.string.v304 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("305")){
                    Toast.makeText(c, R.string.v305 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("306")){
                    Toast.makeText(c, R.string.v306 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("307")){
                    Toast.makeText(c, R.string.v307 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("308")){
                    Toast.makeText(c, R.string.v308 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("309")){
                    Toast.makeText(c, R.string.v309 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("310")){
                    Toast.makeText(c, R.string.v310 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("311")){
                    Toast.makeText(c, R.string.v311 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("312")){
                    Toast.makeText(c, R.string.v312 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("313")){
                    Toast.makeText(c, R.string.v313 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("314")){
                    Toast.makeText(c, R.string.v314 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("315")){
                    Toast.makeText(c, R.string.v315,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("401")){
                    Toast.makeText(c, R.string.v401 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("402")){
                    Toast.makeText(c, R.string.v402 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("403")){
                    Toast.makeText(c, R.string.v403 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("404")){
                    Toast.makeText(c, R.string.v404 ,Toast.LENGTH_SHORT).show();
                }
                else if(desc.contains("501")){
                    Toast.makeText(c, R.string.v501 ,Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(c, R.string.v502 ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return violations.size();
    }
}
