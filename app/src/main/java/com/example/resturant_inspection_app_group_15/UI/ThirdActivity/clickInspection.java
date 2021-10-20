package com.example.resturant_inspection_app_group_15.UI.ThirdActivity;

/*
        Collect the intent from first list, it then displays the information, full date of the inspection,
        inspection type, # of critical and non-critical issues found, hazard level icon, hazard level in words,
        and colour coded.
        We then call the Adapter (for list of violations) and Display (for list of violations) in
        same package to display it here.
*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.resturant_inspection_app_group_15.Model.InspectionPackage.Inspections;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.Restaurant;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.RestaurantManager;
import com.example.resturant_inspection_app_group_15.Model.ViolationPackage.Violations;
import com.example.resturant_inspection_app_group_15.R;

import android.os.strictmode.Violation;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class clickInspection extends AppCompatActivity {

    RestaurantManager restaurantManager = RestaurantManager.getInstance();

    int restaurant_position = 0;
    int inspection_position = 0;

    int total_critical = 0;
    int total_noncritical = 0;

    String inspection_type = "";
    String hazard_level = "";
    String idate = "";

    RecyclerView violationList;
    AdapterForViolations adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_inspection);

        restaurant_position = getIntent().getIntExtra("restaurant_position", 0);
        inspection_position = getIntent().getIntExtra("inspection_position", 0);
        total_critical = getIntent().getIntExtra("critical", 0);
        total_noncritical = getIntent().getIntExtra("noncritical", 0);
        inspection_type = getIntent().getStringExtra("type");
        idate = getIntent().getStringExtra("date");
        hazard_level = getIntent().getStringExtra("hazard_level");

        TextView tvInpsectDate = (TextView)findViewById(R.id.Date_Inspection);
        tvInpsectDate.setText(getString(R.string.idate_add) + idate);

        TextView tvInspectType = (TextView)findViewById(R.id.Inspection_Type);

        if(inspection_type.contains("Routine")){
            tvInspectType.setText(getString(R.string.itype_add) + " " + getString(R.string.routine));
        }
        else{
            tvInspectType.setText(getString(R.string.itype_add) + " " + getString(R.string.follow_up));
        }

        //String msg = String.format("# Critical: %d\n# Noncritical: %d", total_critical, total_noncritical);
        String msg1 = getString(R.string.critical) + total_critical + "\n" + getString(R.string.noncritical) + total_noncritical;
        TextView tvCritical = (TextView)findViewById(R.id.Num_Critical_Non_Critical);
        tvCritical.setText(msg1);

        TextView tvHazardLevel = (TextView)findViewById(R.id.Hazard_Level);
        if(hazard_level.contains("High")){
            tvHazardLevel.setText(getString(R.string.hlevel_add) + " " + getString(R.string.hlevel_high));
        }
        else if(hazard_level.contains("Moderate")){
            tvHazardLevel.setText(getString(R.string.hlevel_add) + " " + getString(R.string.hlevel_mod));
        }
        else
            tvHazardLevel.setText(getString(R.string.hlevel_add) + " " + getString(R.string.hlevel_low));


        ImageView ivHazardLevel = (ImageView)findViewById(R.id.Hazard_Level_icon);

        //System.out.println("The hazard level for " + hazard_level);
        if (hazard_level.equals("Good") || hazard_level.equals("Low")) {
            ivHazardLevel.setImageResource(R.drawable.good);
        } else if (hazard_level.equals("Moderate")) {
            ivHazardLevel.setImageResource(R.drawable.med);
        } else if (hazard_level.equals("High")) {
            ivHazardLevel.setImageResource(R.drawable.bad);
        }

        Restaurant currentRestaurant = restaurantManager.getRestaurantList().get(restaurant_position);
        Inspections currentInspections =  currentRestaurant.getInspectionsManager().getInspectionsList().get(inspection_position);

        ArrayList<Violations> violations = currentInspections.getViolationsManager().getViolationsList();
        System.out.println(violations);

        violationList = (RecyclerView) findViewById(R.id.ViolationList);
        violationList.setLayoutManager(new LinearLayoutManager(this));




        adapter = new AdapterForViolations(this, violations);
        violationList.setAdapter(adapter);
    }
}