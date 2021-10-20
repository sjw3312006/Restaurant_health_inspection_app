package com.example.resturant_inspection_app_group_15.Model.DataBaseSQLite;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.Restaurant;

import java.util.ArrayList;

public class RestaurantViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Restaurant>> mRests = new MutableLiveData<>();

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<Restaurant>> search(SQLiteDatabase db, String queryName) {
        ArrayList<Restaurant> found = new ArrayList<>();

        String sql = "select A.trackingnumber, A.name, A.addr, A.latitude, A.longitude, max(B.inspectiondate) most_recent_date, B.hazardrating " +
                "from restaurants A " +
                "left outer join inspections B " +
                "on A.trackingnumber=B.trackingnumber " +
                " group by A.trackingnumber";
        //if (queryName != null && queryName != "") {
        //    sql += " WHERE A.name LIKE '%" + queryName + "%'";
        //}

        //Log.e("SQL:", sql);

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            if (queryName != null && queryName != "") {
                if (!name.contains(queryName)) {
                    continue;
                }
            }

            String trackingNumber = cursor.getString(0);

            String addr = cursor.getString(2);
            double latitude = cursor.getDouble(3);
            double longitude = cursor.getDouble(4);
            String date = cursor.getString(5);
            String hazardRating = cursor.getString(6);
//            System.out.println(date);
//            System.out.println(hazardRating);


            int hazardImg = 0;
            if(hazardRating == null){
                hazardImg = 700085;
            }
            else if (hazardRating.equals("High")) {
                hazardImg = 700003;
            } else if (hazardRating.equals("Low")) {
                hazardImg = 700085;
            } else { 
                hazardImg = 700121;
            }

            System.out.println(hazardImg);
            Restaurant entity = new Restaurant(trackingNumber, name, addr, latitude, longitude, date, hazardImg);
            //date might be messed up
            found.add(entity);

            //Log.e("NAME:", entity.getName());
        }
        cursor.close();

        mRests.setValue(found); //search result

        return mRests;
    }

    public MutableLiveData<ArrayList<Restaurant>> getAll() {
        return mRests;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
