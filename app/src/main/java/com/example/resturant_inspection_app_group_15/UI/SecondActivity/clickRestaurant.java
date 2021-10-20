package com.example.resturant_inspection_app_group_15.UI.SecondActivity;

/*
        Collect the intent from first list, it then displays the information such as restaurant name,
        restaurant address, and restaurant gps coordinates.
        We then call the Adapter (for inspection) and Display (for inspection) in same package to display it here.
*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.resturant_inspection_app_group_15.Model.CSV.CSVReader;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.Restaurant;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.RestaurantManager;
import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.Model.CSV.ListModifiers;

import com.example.resturant_inspection_app_group_15.UI.MapActivity.mapPageActivity;
import com.example.resturant_inspection_app_group_15.UI.FirstActivity.MainActivity;
import com.example.resturant_inspection_app_group_15.UI.SecondActivity.AdapterForInspection;
import com.google.android.gms.maps.GoogleMap;

public class clickRestaurant extends AppCompatActivity {

    RecyclerView inspectionList;
    AdapterForInspection adapter;
    CSVReader csvReader = new CSVReader();
    RestaurantManager restaurantManager = RestaurantManager.getInstance();

    int restaurant_position = 0;

    public int get_restaurant_position() {
        return restaurant_position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_click_restaurant);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();

        final int position = intent.getIntExtra("Position",0);
        String desc = ListModifiers.setRestaurantDescription(position);
        String location = ListModifiers.giveCoors(position);
        restaurant_position = position;

        TextView textView = findViewById(R.id.restaurantNamePage);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),mapPageActivity.class);
                intent1.putExtra("lat",restaurantManager.getRestaurantList().get(position).getLatitude());
                intent1.putExtra("long",restaurantManager.getRestaurantList().get(position).getLongitude());
                intent1.putExtra("check",true);
                startActivity(intent1);
            }
        });

        Restaurant currentRestaurant = restaurantManager.getRestaurantList().get(position);
        ListModifiers.setInspectionsListDescription(currentRestaurant);

        toText(R.id.restaurantNamePage, intent.getStringExtra("Name"));
        toText(R.id.restaurantDescriptionPage, getString(R.string.adress) +" " + desc);
        toText(R.id.restCoords, getString(R.string.lat_long) + "\n" + location);

        inspectionList = (RecyclerView) findViewById(R.id.inspectionsList);
        inspectionList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdapterForInspection(this, restaurant_position, currentRestaurant.getInspectionsManager().getInspectionsList());
        inspectionList.setAdapter(adapter);

        setupFavCheckbox(restaurant_position);
    }
    private void setupFavCheckbox(final int restaurant_position){

        final CheckBox checkBoxfav = (CheckBox) findViewById(R.id.checkBox_fav);
        if (restaurantManager.getRestaurantList().get(restaurant_position).isFavStatus()){
            System.out.println("TURNING TO TRUE");
            checkBoxfav.setChecked(true);
        }
        else{checkBoxfav.setChecked(false);
        System.out.println("TURNING TO FALSE");}
        final boolean checked = checkBoxfav.isChecked();

        checkBoxfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (checked){
                        System.out.println("Now it's off");
                        CSVReader.writeFavouriteCsv(clickRestaurant.this,true,restaurantManager.getRestaurantList().get(restaurant_position).getTrackingNumber(),restaurant_position);
                        restaurantManager.getRestaurantList().get(restaurant_position).setFavStatus(false);
                    }
                    else{
                        System.out.println("Now it's On");
                        restaurantManager.getRestaurantList().get(restaurant_position).setFavStatus(true);
                        CSVReader.writeFavouriteCsv(clickRestaurant.this,false,restaurantManager.getRestaurantList().get(restaurant_position).getTrackingNumber(),restaurant_position);
                    }
            }
        });

    }
    private void toText(int id, String text) {
        TextView textView = (TextView) findViewById(id);
        textView.setText(text);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        super.onBackPressed();
        if (intent.getBooleanExtra("fromMap_toSecond",false)) {
            Intent toMap = new Intent(getApplicationContext(), mapPageActivity.class);
            // Put extras to intent here
            startActivity(toMap);
        }
        if (intent.getBooleanExtra("fromMain_toSecond",false)) {
            Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
            // Put extras to intent here
            startActivity(toMain);
        }
    }
}
