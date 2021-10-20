package com.example.resturant_inspection_app_group_15.Model.ViolationPackage;

/*
        Violation.java class will hold everything a violation would hold according to the User Stories.
        Just made setters and getters to be useful for our violationManager to get information.
*/

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.resturant_inspection_app_group_15.Model.DataBaseSQLite.RestInspDBOpenHelper;

public class Violations {
    private String numViolations;
    private boolean isItCritical;
    private String description;
    private String nature;

    public Violations() {
        numViolations = "";
        isItCritical = false;
        nature = "";
        description = "";
    }

    public Violations(Violations v) {
        numViolations = v.numViolations;
        isItCritical = v.isItCritical;
        nature = v.nature;
        description = v.description;
    }

    public static class Entry implements BaseColumns { //already contains id(index)
        public static final String TABLE_NAME = "violations";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_NUMVUILATIONS = "numviolation";
        public static final String COLUMN_NAME_ISITCRITICAL = "isitcritical";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_NATURE = "nature";
    }

    public String getNumViolations(){
        return numViolations;
    }

    public void setNumViolations(String numViolations){
        this.numViolations = numViolations;
    }

    public boolean isItCritical() {
        return isItCritical;
    }

    public void setItCritical(boolean itCritical) {
        isItCritical = itCritical;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

}