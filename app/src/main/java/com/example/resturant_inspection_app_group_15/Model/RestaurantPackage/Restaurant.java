package com.example.resturant_inspection_app_group_15.Model.RestaurantPackage;

/*
        Restaurant.java class will hold everything a restaurant would hold according to the User Stories.
        Just made setters and getters to be useful for our restaurantManager.
        The only function this holds besides setters/getters is just putting everything in a string to print.
        It calls inspectionManager in order to accurately get number of Inspections for Activity 2.
*/

import android.provider.BaseColumns;

import com.example.resturant_inspection_app_group_15.Model.InspectionPackage.Inspections;
import com.example.resturant_inspection_app_group_15.Model.InspectionPackage.InspectionsManager;

public class Restaurant {

    private String description;
    private String description2;
    private int hazardImg;
    private String trackingNumber;
    private String name;
    private String physicalAddress;
    private String physicalCity;
    private String facType;
    private double  latitude;
    private double longitude;
    private int totalLatestVio;
    private String latestDate;
    private Inspections latestInspection;
    private boolean favStatus = false;

    InspectionsManager inspectionsManager = new InspectionsManager();

    public Restaurant() {
    }

    public static class Entry implements BaseColumns{ //already contains id(index)
        public static final String TABLE_NAME = "restaurants";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TRACKING_NUMBER = "trackingnumber";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_ADDR = "addr";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
    }

    public Restaurant(String trackingnumber, String name, String addr, double latitude, double longitude, String recentInspectionDate, int hazardrating) {
        this.trackingNumber = trackingnumber;
        this.name = name;
        this.physicalAddress = addr;
        this.latitude = latitude;
        this.longitude = longitude;
        this.latestDate = recentInspectionDate;
        this.hazardImg = hazardrating;
    }

    public Restaurant(String name,String description) {
        this.name = name;
        this.description = description;
    }

    public Restaurant(String name,String description,int hazardImg) {
        this.name = name;
        this.description = description;
        this.hazardImg = hazardImg;
    }


    public int getHazardImg() {
        return hazardImg;
    }

    public String getDescription() {
        return description;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription(String description) {
        this.description2 = description;
    }

    public void setDescription2(String description) {
        this.description = description;
    }

    public void setHazardImg(int hazardImg) {
        this.hazardImg = hazardImg;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPhysicalCity() {
        return physicalCity;
    }

    public void setPhysicalCity(String physicalCity) {
        this.physicalCity = physicalCity;
    }

    public String getFacType() {
        return facType;
    }

    public void setFacType(String facType) {
        this.facType = facType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getTotalLatestVio() {
        return totalLatestVio;
    }

    public void setTotalLatestVio(int totalLatestVio) {
        this.totalLatestVio = totalLatestVio;
    }

    public String getLatestDate() {
        return latestDate;
    }

    public void setLatestDate(String latestDate) {
        this.latestDate = latestDate;
    }

    public Inspections getLatestInspection() {
        return latestInspection;
    }

    public void setLatestInspection(Inspections latestInspection) {
        this.latestInspection = latestInspection;
    }

    public InspectionsManager getInspectionsManager() {
        return inspectionsManager;
    }

    public void setInspectionsManager(InspectionsManager inspectionsManager) {
        this.inspectionsManager = inspectionsManager;
    }

    public boolean isFavStatus() {
        return favStatus;
    }

    public void setFavStatus(boolean favStatus) {
        this.favStatus = favStatus;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "trackingNumber='" + trackingNumber + '\'' +
                ", name='" + name + '\'' +
                ", physicalAddress='" + physicalAddress + '\'' +
                ", facType='" + facType + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
