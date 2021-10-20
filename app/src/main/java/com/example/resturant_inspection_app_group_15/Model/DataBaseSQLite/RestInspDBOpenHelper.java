package com.example.resturant_inspection_app_group_15.Model.DataBaseSQLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.resturant_inspection_app_group_15.Model.InspectionPackage.Inspections;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.Restaurant;
import com.example.resturant_inspection_app_group_15.Model.ViolationPackage.Violations;

import java.util.ArrayList;

import static com.example.resturant_inspection_app_group_15.Model.DataBaseSQLite.Downloader.TAG;



public class RestInspDBOpenHelper extends SQLiteOpenHelper {
    // ...


    //if you change the database schema, you must increment the database version.

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "RestInsp.db";

    //creating a table for restaurant info.
    private static final String SQL_CREATE_RESTAURANT_ENTRIES_IF_NOT_EXISTS =
            "CREATE TABLE IF NOT EXISTS " + Restaurant.Entry.TABLE_NAME + " ("+
                    Restaurant.Entry.COLUMN_NAME_TRACKING_NUMBER + " TEXT PRIMARY KEY," +
                    Restaurant.Entry.COLUMN_NAME_NAME + " TEXT," +
                    Restaurant.Entry.COLUMN_NAME_ADDR + " TEXT," +
                    Restaurant.Entry.COLUMN_NAME_CITY + " TEXT," +
                    Restaurant.Entry.COLUMN_NAME_TYPE + " TEXT," +
                    Restaurant.Entry.COLUMN_NAME_LATITUDE + " DOUBLE," +
                    Restaurant.Entry.COLUMN_NAME_LONGITUDE + " DOUBLE" +
                    ");";

    //creating a table for inspection info.
    private static final String SQL_CREATE_INSPECTION_ENTRIES_IF_NOT_EXISTS =
            "CREATE TABLE IF NOT EXISTS " + Inspections.Entry.TABLE_NAME + " ("+
                    Inspections.Entry._ID + " INT PRIMARY KEY," +
                    Inspections.Entry.COLUMN_NAME_TRACKING_NUMBER + " TEXT," +
                    Inspections.Entry.COLUMN_NAME_INSPECTION_DATE + " TEXT," +
                    Inspections.Entry.COLUMN_NAME_INSPEC_TYPE + " TEXT," +
                    Inspections.Entry.COLUMN_NAME_NUM_CRITICAL + " INT," +
                    Inspections.Entry.COLUMN_NAME_NUM_NON_CRITICAL + " INT," +
                    Inspections.Entry.COLUMN_NAME_HAZARD_RATING + " TEXT," +
                    Inspections.Entry.COLUMN_NAME_VIO_LUMP + " TEXT);";

    private static final String SQL_CREATE_VIOLATION_ENTRIES_IF_NOT_EXISTS =
            "CREATE TABLE IF NOT EXISTS " + Violations.Entry.TABLE_NAME + " ("+
                    Violations.Entry.COLUMN_NAME_NUMVUILATIONS + " TEXT PRIMARY KEY," +
                    Violations.Entry.COLUMN_NAME_ISITCRITICAL + " TEXT," +
                    Violations.Entry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    Violations.Entry.COLUMN_NAME_NATURE + " TEXT);";

    public RestInspDBOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("DB", SQL_CREATE_RESTAURANT_ENTRIES_IF_NOT_EXISTS);
        db.execSQL(SQL_CREATE_RESTAURANT_ENTRIES_IF_NOT_EXISTS);

        Log.e("DB", SQL_CREATE_INSPECTION_ENTRIES_IF_NOT_EXISTS);
        db.execSQL(SQL_CREATE_INSPECTION_ENTRIES_IF_NOT_EXISTS);

        Log.e("DB", SQL_CREATE_VIOLATION_ENTRIES_IF_NOT_EXISTS);
        db.execSQL(SQL_CREATE_VIOLATION_ENTRIES_IF_NOT_EXISTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<Restaurant> getQueryRestaurants(String search) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        // SELECT *
        //        FROM posts
        // WHERE posts MATCH 'fts5';
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM restaurants" +
                                " WHERE restaurants" +
                                "MATCH %s", search);
        // Its not restaurants right here

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Restaurant rest = new Restaurant();

                    rest.setName(cursor.getString(cursor.getColumnIndexOrThrow(Restaurant.Entry.COLUMN_NAME_NAME)));
                    rest.setTrackingNumber(cursor.getString(cursor.getColumnIndexOrThrow(Restaurant.Entry.COLUMN_NAME_TRACKING_NUMBER)));
                    rest.setPhysicalCity(cursor.getString(cursor.getColumnIndexOrThrow(Restaurant.Entry.COLUMN_NAME_CITY)));
                    rest.setPhysicalAddress(cursor.getString(cursor.getColumnIndexOrThrow(Restaurant.Entry.COLUMN_NAME_ADDR)));
                    rest.setFacType(cursor.getString(cursor.getColumnIndexOrThrow(Restaurant.Entry.COLUMN_NAME_TYPE)));
                    rest.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(Restaurant.Entry.COLUMN_NAME_LATITUDE)));
                    rest.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(Restaurant.Entry.COLUMN_NAME_LONGITUDE)));

                    restaurants.add(rest);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return restaurants;
    }
}
