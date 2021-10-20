package com.example.resturant_inspection_app_group_15.Model.InspectionPackage;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.resturant_inspection_app_group_15.Model.DataBaseSQLite.RestInspDBOpenHelper;
import com.example.resturant_inspection_app_group_15.Model.ViolationPackage.ViolationsManager;

/*
        Inspection.java class will hold everything an inspection would hold according to the User Stories.
        Just made setters and getters to be useful for our inspectionManager.
        The only function this holds besides setters/getters is just putting everything in a string to print.
        It calls violationsManager in order to accurately get number of Violations for Activity 3.
*/

import java.util.ArrayList;

public class Inspections extends ArrayList<Inspections> {
    private String description;
    private String trackingNumber;
    private String inspectionDate;
    private String inspType;
    private int numCritical;
    private int numNonCritical;
    private String hazardRating;
    private String vioLump;
    ViolationsManager violationsManager = new ViolationsManager();

    public static class Entry implements BaseColumns { //already contains id(index)
        public static final String TABLE_NAME = "inspections";
        public static final String COLUMN_NAME_TRACKING_NUMBER = "trackingnumber";
        public static final String COLUMN_NAME_INSPECTION_DATE = "inspectiondate";
        public static final String COLUMN_NAME_INSPEC_TYPE = "inspectiontype";
        public static final String COLUMN_NAME_NUM_CRITICAL = "numcritical";
        public static final String COLUMN_NAME_NUM_NON_CRITICAL = "numnoncritical";
        public static final String COLUMN_NAME_HAZARD_RATING = "hazardrating";
        public static final String COLUMN_NAME_VIO_LUMP = "violump";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getInspType() {
        return inspType;
    }

    public void setInspType(String inspType) {
        this.inspType = inspType;
    }

    public int getNumCritical() {
        return numCritical;
    }

    public void setNumCritical(int numCritical) {
        this.numCritical = numCritical;
    }

    public int getNumNonCritical() {
        return numNonCritical;
    }

    public void setNumNonCritical(int numNonCritical) {
        this.numNonCritical = numNonCritical;
    }

    public String getHazardRating() {
        return hazardRating;
    }

    public void setHazardRating(String hazardRating) {
        this.hazardRating = hazardRating;
    }

    public String getVioLump() {
        return vioLump;
    }

    public void setVioLump(String vioLump) {
        this.vioLump = vioLump;
    }

    public ViolationsManager getViolationsManager() {
        return violationsManager;
    }

    public void setViolationsManager(ViolationsManager violationsManager) {
        this.violationsManager = violationsManager;
    }

    @Override
    public String toString() {
        return "Inspections{" +
                "trackingNumber='" + trackingNumber + '\'' +
                ", inspectionDate=" + inspectionDate +
                ", inspType='" + inspType + '\'' +
                ", numCritical=" + numCritical +
                ", numNonCritical=" + numNonCritical +
                ", hazardRating='" + hazardRating + '\'' +
                ", vioLump='" + vioLump + '\'' +
                '}';
    }
}
