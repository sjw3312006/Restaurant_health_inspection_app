package com.example.resturant_inspection_app_group_15.Model.RestaurantPackage;

/*
        RestaurantManager.java class will hold all of our restaurants, so we can easily access them in our first activity.
        We have an ArrayList that holds the restaurants and it has its own setter and getter.
*/

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class RestaurantManager {

    private ArrayList<Restaurant> restaurantList = new ArrayList<>();
    private static RestaurantManager manager;
    private static RestaurantManager cancelledlist;
    private static RestaurantManager aliveList;
    private static RestaurantManager favouriteList = new RestaurantManager();
    private static RestaurantManager favouriteListToShow = new RestaurantManager();
    private String getList;
    private String GetInspections;
    private String lastRestaurantModified;
    private String lastInspectionModified;
    private String getDate;
    private StringBuffer stringBuffer;
    private boolean Existed = false;


    public static RestaurantManager getAliveList() {
        return aliveList;
    }

    public static void setAliveList(RestaurantManager aliveList) {
        RestaurantManager.aliveList = aliveList;
    }

    public boolean isExisted() {
        return Existed;
    }

    public void setExisted(boolean existed) {
        Existed = existed;
    }

    public static RestaurantManager getFavouriteList() {
        return favouriteList;
    }

    public static void setFavouriteList(RestaurantManager favouriteList) {
        RestaurantManager.favouriteList = favouriteList;
    }

    public static RestaurantManager getCancelledlist() {
        return cancelledlist;
    }

    public static void setCancelledlist(RestaurantManager cancelledlist) {
        RestaurantManager.cancelledlist = cancelledlist;
    }

    public void add(Restaurant instance) {
        restaurantList.add(instance);
    }

    public ArrayList<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(ArrayList<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }
    public static RestaurantManager getInstance(){
        if (manager == null){
            manager = new RestaurantManager();
        }
        return manager;
    }

    public static RestaurantManager getManager() {
        return manager;
    }

    public static void setManager(RestaurantManager manager) {
        RestaurantManager.manager = manager;
    }

    public String getGetDate() {
        return getDate;
    }

    public void setGetDate(String getDate) {
        this.getDate = getDate;
    }

    public String getGetList() {
        return getList;
    }

    public void setGetList(String getList) {
        this.getList = getList;
    }

    public String getGetInspections() {
        return GetInspections;
    }

    public void setGetInspections(String getInspections) {
        GetInspections = getInspections;
    }

    public StringBuffer getStringBuffer() {
        return stringBuffer;
    }

    public void setStringBuffer(StringBuffer stringBuffer) {
        this.stringBuffer = stringBuffer;
    }

    public String getLastRestaurantModified() {
        return lastRestaurantModified;
    }

    public void setLastRestaurantModified(String lastRestaurantModified) {
        this.lastRestaurantModified = lastRestaurantModified;
    }

    public static RestaurantManager getFavouriteListToShow() {
        return favouriteListToShow;
    }

    public static void setFavouriteListToShow(RestaurantManager favouriteListToShow) {
        RestaurantManager.favouriteListToShow = favouriteListToShow;
    }

    public String getLastInspectionModified() {
        return lastInspectionModified;
    }

    public void setLastInspectionModified(String lastInspectionModified) {
        this.lastInspectionModified = lastInspectionModified;
    }
}

