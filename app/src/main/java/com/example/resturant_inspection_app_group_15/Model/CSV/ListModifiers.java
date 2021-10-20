package com.example.resturant_inspection_app_group_15.Model.CSV;

/*
    This ListModifiers.java class contains the constructors which sorts the restaurant name alphabetically to display
    list of all restaurants in first activity. Also, it sorts the inspection date in order and descriptions for inspection.
    We can also use another constructors in this class to display the information about each restaurants like addresses, latitude,
    logitude, total violation for latest, last inspection date, and lastly, number of critical and non-critical issues.
*/

import com.example.resturant_inspection_app_group_15.Model.InspectionPackage.Inspections;
import com.example.resturant_inspection_app_group_15.Model.InspectionPackage.InspectionsManager;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.Restaurant;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.RestaurantManager;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListModifiers {
    public static void sortAlpha(){
        final int[] i = {0};
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        List<Restaurant> restaurantList = restaurantManager.getRestaurantList();
        if (restaurantManager.getRestaurantList().size() > 0){
            Collections.sort(restaurantList, new Comparator<Restaurant>() {
                @Override
                public int compare(Restaurant restaurant, Restaurant restaurant2) {

                    if (restaurant.getName() == null){
                        System.out.println(restaurant.getTrackingNumber() + " is the offender");
                    }
                    else if (restaurant2.getName() == null){
                        System.out.println(restaurant2.getTrackingNumber() + " is the offender");

                    }
                    i[0]++;
                    return restaurant.getName().compareTo(restaurant2.getName());
                }
            });
        }
    }
    public static void sortTimeline(){
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        for (Restaurant r : restaurantManager.getRestaurantList()){
            if(restaurantManager.getRestaurantList().size() > 0){
                Collections.sort(r.getInspectionsManager().getInspectionsList(), new Comparator<Inspections>() {
                    @Override
                    public int compare(Inspections inspections, Inspections inspections2) {
                        if (Integer.parseInt(inspections.getInspectionDate()) > Integer.parseInt(inspections2.getInspectionDate())) {
                            return -1;
                        }
                        else if (Integer.parseInt(inspections.getInspectionDate()) == Integer.parseInt(inspections2.getInspectionDate())){
                            return 0;
                        }
                        else {
                            return 1;
                        }
                    }
                });
            }
        }
    }
    public static void setInspectionsListDescription(Restaurant r){
        InspectionsManager inspectionsManager = r.getInspectionsManager();
        String inspectionDate = "";
        String description = "";
        for (Inspections i : inspectionsManager.getInspectionsList()){
            inspectionDate = inspectionDateToIntelligentFormat(i);
            description = "Number of critical issues: " + i.getNumCritical() + "\nNumber of non-critical issues found: " + i.getNumNonCritical() + "\nInspection was: " + inspectionDate;
            i.setDescription(description);
        }
    }

    public static String inspectionDateToIntelligentFormat(Inspections i) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String intelligent = "";
        Date current = new Date();
        df.format(current);
        String date = i.getInspectionDate();
        Date inspectionDate = new Date();

        int dyear = Integer.parseInt(date.substring(0,4));
        int dmonth = Integer.parseInt(date.substring(4,6));
        int dday = Integer.parseInt(date.substring(6,8));

        try {
            inspectionDate = df.parse( dyear + "/" + dmonth + "/" + dday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diffInMillies = 0;
        if (date != null) {
            diffInMillies = Math.abs(current.getTime() - inspectionDate.getTime());
        }
        //Log.d("The diff in mill is ", "" + diffInMillies);
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        //Log.d("Date difference is " , diff + "");
        String monthString = null;

        if (date != null) {
            monthString = new DateFormatSymbols().getMonths()[inspectionDate.getMonth()];
        }
        if (diff > 365){
            intelligent = monthString + " " + dyear;
            return intelligent;
        }
        else if (diff > 30){
            intelligent = monthString + " " + dday;
            return intelligent;
        }
        else {
            intelligent = diff + " days ago";
            return intelligent;
        }
    }
    public static void setDescription(){
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        String description;
        for (Restaurant r: restaurantManager.getRestaurantList()){
            description = latestToIntelligentFormat(r);
            r.setDescription("Total Violations for Latest: "+ r.getTotalLatestVio()  + "\nLast Inspection was on: " + description);
        }
    }
    public static String setRestaurantDescription(int position){
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        String description = "";
        description = "" + restaurantManager.getRestaurantList().get(position).getPhysicalAddress() ;
        return description;
    }

    public static String giveCoors(int position){
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        String coor = "";
        coor ="" + restaurantManager.getRestaurantList().get(position).getLatitude() + ", " +restaurantManager.getRestaurantList().get(position).getLongitude();
        return coor;
    }

    public static void setRestaurantListDescription(){
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        String description;
        for (Restaurant r: restaurantManager.getRestaurantList()){
            description = latestToIntelligentFormat(r);
            r.setDescription2("" + r.getTotalLatestVio());
            r.setDescription("" + description);
        }
    }
    public static String latestToIntelligentFormat(Restaurant restaurant){

        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String intelligent;
        Inspections inspections = restaurant.getLatestInspection();
        Date current = new Date();
        df.format(current);
        int year = current.getYear()+1900;
        int month = current.getMonth()+1;
        int day = current.getDate();
        if (inspections == null){
            return "No Inspection found";
        }
        String date = inspections.getInspectionDate();
        Date date1 = new Date();
        int dyear = Integer.parseInt(date.substring(0,4));
        int dmonth = Integer.parseInt(date.substring(4,6));
        int dday = Integer.parseInt(date.substring(6,8));
        try {
            date1 = df.parse(dyear + "/" + dmonth + "/" + dday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Log.d("The current date is", current.toString());
        long diffInMillies = 0;
        if (date1 != null) {
            diffInMillies = Math.abs(current.getTime() - date1.getTime());
        }
        //Log.d("The diff in mill is ", "" + diffInMillies);
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        //Log.d("Date difference is " , diff + "");
        String monthString = null;
        if (date1 != null) {
            monthString = new DateFormatSymbols().getMonths()[date1.getMonth()];
        }
        if (diff > 365){
            intelligent = monthString + " " + dyear;
        }
        else if (diff > 30){
            intelligent = monthString + " " + dday;
        }
        else {
            intelligent = diff + " days";
        }
        return intelligent;
    }
}
