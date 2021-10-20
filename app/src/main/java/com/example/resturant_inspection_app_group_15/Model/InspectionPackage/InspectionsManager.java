package com.example.resturant_inspection_app_group_15.Model.InspectionPackage;

/*
        InspectionsManager.java class will hold all of our inspections, so we can easily access them in our third activity.
        We have an ArrayList that holds the inspection and it has its own setter and getter.
        */

        import java.util.ArrayList;

public class InspectionsManager {
    ArrayList<Inspections> inspectionsList = new ArrayList<>();
    private String getRequestInspection;
    private static InspectionsManager inspectionsManager;

    public ArrayList<Inspections> getInspectionsList() {
        return inspectionsList;
    }

    public void setInspectionsList(ArrayList<Inspections> inspectionsList) {
        this.inspectionsList = inspectionsList;
    }

    private InspectionsManager getInstance(){
        if (inspectionsManager == null){
            inspectionsManager = new InspectionsManager();
        }
        return inspectionsManager;
    }

    public String getGetRequestInspection() {
        return getRequestInspection;
    }

    public void setGetRequestInspection(String getRequestInspection) {
        this.getRequestInspection = getRequestInspection;
    }
}
