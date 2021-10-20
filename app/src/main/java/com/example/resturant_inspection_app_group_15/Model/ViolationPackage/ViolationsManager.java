package com.example.resturant_inspection_app_group_15.Model.ViolationPackage;

/*
        ViolationsManager class will hold all of our violations, so we can easily access them in our third activity.
        We have an ArrayList that holds the violations and it has its own setter and getter.
*/

import com.example.resturant_inspection_app_group_15.Model.InspectionPackage.InspectionsManager;
import com.example.resturant_inspection_app_group_15.Model.ViolationPackage.Violations;

import java.util.ArrayList;

public class ViolationsManager {
    private ArrayList<Violations> violationsList = new ArrayList<>();
    private static ViolationsManager violationsManager;

    public ArrayList<Violations> getViolationsList() {
        return violationsList;
    }

    public void setViolationsList(ArrayList<Violations> violationsList) {
        this.violationsList = violationsList;
    }

    public static ViolationsManager getInstance(){
        if (violationsManager == null){
            violationsManager = new ViolationsManager();
        }
        return violationsManager;
    }
}
