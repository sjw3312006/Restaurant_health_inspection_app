package com.example.resturant_inspection_app_group_15.Model.CSV;

/*
    This CSVReader.java class includes the constructors which opens and generates the data in the res -> raw,
    which contains restaurants_itr and inspectionreports_itr1. One constructor separates the each field by each restaurant's
    tracking #, name, physical address, physical city, facility type, longitude and latitude from restaurants_itr.
    This class also contains the constructor which sets the latest date for inspections.
    There also exists another constructor which opens and generates the data from inspectionreports_itr1, which separates the
    each field by each inspection's tracking #, date of inspection, type of inspection, # of critical and non-critical,
    hazard rating, and violump (violations).
    Lastly, there is another constructor which includes the violations on each restaurants.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.example.resturant_inspection_app_group_15.Model.InspectionPackage.Inspections;
import com.example.resturant_inspection_app_group_15.Model.InspectionPackage.InspectionsManager;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.Restaurant;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.RestaurantManager;
import com.example.resturant_inspection_app_group_15.Model.ViolationPackage.Violations;
import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.UI.FirstActivity.MainActivity;
import com.example.resturant_inspection_app_group_15.UI.MapActivity.mapPageActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CSVReader {
    RestaurantManager realManager = new RestaurantManager();
    RestaurantManager aliveList = new RestaurantManager();
    public void readFinalCSV(Context context,mapPageActivity mapPageActivity){
        RestaurantManager restaurantManager = RestaurantManager.getInstance();

        File file = new File(context.getFilesDir() + "/finalCSV.csv");
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            FileReader fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            file.createNewFile();
            String output = bufferedReader.readLine();
            //System.out.println("discarding " + output);
            String[] otokens = output.split(",");
            restaurantManager.setLastRestaurantModified(otokens[0]+","+otokens[1]);
            restaurantManager.setLastInspectionModified(otokens[2]+","+otokens[3]);

            while((output = bufferedReader.readLine()) != null){

                String[] atoken = output.split("~");
                if (atoken.length > 1) {
                    stringBuffer.append(output+"\n");
                    String[] itoken = atoken[1].split("%");
                    String[] rtoken = atoken[0].split(",");
                    Restaurant currentRestaurant = new Restaurant();
                    currentRestaurant.setTrackingNumber(rtoken[0]);
                    currentRestaurant.setName(rtoken[1]);
                    currentRestaurant.setPhysicalAddress(rtoken[2]);
                    currentRestaurant.setPhysicalCity(rtoken[3]);
                    currentRestaurant.setFacType(rtoken[4]);
                    currentRestaurant.setLatitude(Double.parseDouble(rtoken[5]));
                    currentRestaurant.setLongitude(Double.parseDouble(rtoken[6]));
                    //System.out.println("THE SIZE OF FAV IS " + RestaurantManager.getFavouriteList().getRestaurantList().size());
                    for (Restaurant r: RestaurantManager.getFavouriteList().getRestaurantList()) {
                        //System.out.println(r.getTrackingNumber() + "v" + currentRestaurant.getTrackingNumber());
                        if (currentRestaurant.getTrackingNumber().equals(r.getTrackingNumber())){
                            System.out.println("Setting " + r.getName() + " is set to true");
                            currentRestaurant.setFavStatus(true);
                        }
                    }
                    for (int i = 0; i < itoken.length; i++) {
                        String[] iitoken = itoken[i].split(",");
                        Inspections currentInspection = new Inspections();
                        currentInspection.setTrackingNumber(iitoken[0]);
                        currentInspection.setInspectionDate(iitoken[1]);
                        currentInspection.setInspType(iitoken[2]);
                        currentInspection.setNumCritical(Integer.parseInt(iitoken[3]));
                        currentInspection.setNumNonCritical(Integer.parseInt(iitoken[4]));
                        String conc = "";
                        for (int j = 5; j < iitoken.length-1; j++) {

                                conc = conc.concat(iitoken[j]+",");

                        }
                        currentInspection.setVioLump(conc);

                        iitoken[6] = iitoken[6].replace("\"","");
                        iitoken[6] = iitoken[6].replace("%","");
                        currentInspection.setHazardRating(iitoken[iitoken.length-1]);
                        currentRestaurant.getInspectionsManager().getInspectionsList().add(currentInspection);
                    }
                    restaurantManager.getRestaurantList().add(currentRestaurant);
                    aliveList.getRestaurantList().add(currentRestaurant);
                }
                else{
                    String[] rtoken = atoken[0].split(",");
                    if (rtoken.length > 1) {
                        Restaurant currentRestaurant = new Restaurant();
                        currentRestaurant.setTrackingNumber(rtoken[0]);
                        currentRestaurant.setName(rtoken[1]);
                        currentRestaurant.setPhysicalAddress(rtoken[2]);
                        currentRestaurant.setPhysicalCity(rtoken[3]);
                        currentRestaurant.setFacType(rtoken[4]);
                        currentRestaurant.setLatitude(Double.parseDouble(rtoken[5]));
                        currentRestaurant.setLongitude(Double.parseDouble(rtoken[6]));
                        restaurantManager.getRestaurantList().add(currentRestaurant);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //for (Restaurant r: restaurantManager.getRestaurantList()){

        //    aliveList.getRestaurantList().add(r);
        //}
        System.out.println("FOR THE FIRST " + aliveList.getRestaurantList().get(0) + " the num is " + aliveList.getRestaurantList().get(0).getInspectionsManager().getInspectionsList().size());
        RestaurantManager.setAliveList(aliveList);
        for (Restaurant r: restaurantManager.getRestaurantList()){
            Restaurant current = new Restaurant();
            InspectionsManager inspectionsManager = new InspectionsManager();
            for (Inspections I : r.getInspectionsManager().getInspectionsList()){
                Inspections curI = new Inspections();
                curI.setHazardRating(I.getHazardRating());
                curI.setVioLump(I.getVioLump());
                curI.setNumNonCritical(I.getNumNonCritical());
                curI.setNumCritical(I.getNumCritical());
                curI.setInspType(I.getInspType());
                curI.setInspectionDate(I.getInspectionDate());
                curI.setTrackingNumber(I.getTrackingNumber());
                curI.setDescription(I.getDescription());
                curI.setViolationsManager(I.getViolationsManager());

                inspectionsManager.getInspectionsList().add(curI);
            }
            current.setLongitude(r.getLongitude());
            current.setLatitude(r.getLatitude());
            current.setFacType(r.getFacType());
            current.setPhysicalCity(r.getPhysicalCity());
            current.setPhysicalAddress(r.getPhysicalAddress());
            current.setName(r.getName());
            current.setTrackingNumber(r.getTrackingNumber());
            current.setDescription(r.getDescription());
            current.setHazardImg(r.getHazardImg());
            current.setInspectionsManager(inspectionsManager);
            current.setLatestDate(r.getLatestDate());
            current.setLatestInspection(r.getLatestInspection());
            current.setTotalLatestVio(r.getTotalLatestVio());
            //System.out.println("adding for r4 " + current.getName() + " with " + current.getInspectionsManager().getInspectionsList().size() );
            realManager.getRestaurantList().add(current);
        }
        restaurantManager.setStringBuffer(stringBuffer);
        System.out.println("IT IS " + restaurantManager.getRestaurantList().size());
        restaurantManager.setExisted(true);
       // restaurantManager.getRestaurantList().size();
        mapPageActivity.finish3(2);

    }
    public void remakeFinal(Context context, mapPageActivity mainActivity){
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        StringBuffer startBuffer = new StringBuffer();
        StringBuffer stringBuffer = new StringBuffer();
        if (mainActivity.fileExists() && mainActivity.isCancelled()){
            restaurantManager.setLastRestaurantModified("Filler");
            restaurantManager.setLastInspectionModified("Filler");
            restaurantManager.setGetDate("6 12 2020 1 28 28");
        }
        String time = restaurantManager.getGetDate()+ "," + restaurantManager.getLastRestaurantModified()+","+restaurantManager.getGetDate()+","+restaurantManager.getLastInspectionModified();
        File file = new File(context.getFilesDir() + "/finalCSV.csv");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        startBuffer.append(time+"\n");

        try {
            time = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while((time = bufferedReader.readLine()) != null){
                stringBuffer.append(time+"\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("THE STRING BUFFER IS " + stringBuffer);
        restaurantManager.setStringBuffer(stringBuffer);
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            startBuffer.append(stringBuffer);
            try {
                fileOut.write(startBuffer.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void generateFinalCSV(Context context){
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        //System.out.println("ACTUALLY WORKING");
        File file = new File(context.getFilesDir() + "/finalCSV.csv");
        BufferedWriter writer = null;
        try {
            FileWriter fileWriter = new FileWriter(file);
            writer = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
            try {
                file.createNewFile();
                writer.write(restaurantManager.getLastRestaurantModified() + "," + restaurantManager.getLastInspectionModified() + "\n");
                for (Restaurant r : restaurantManager.getRestaurantList()) {
                    writer.write(r.getTrackingNumber() + ",");
                    writer.write(r.getName() + ",");
                    writer.write(r.getPhysicalAddress() + ",");
                    writer.write(r.getPhysicalCity() + ",");
                    writer.write(r.getFacType() + ",");
                    writer.write(r.getLatitude() + ",");
                    writer.write(r.getLongitude() + "~");
                    for (Inspections i : r.getInspectionsManager().getInspectionsList()) {
                        writer.write(i.getTrackingNumber() + ",");
                        writer.write(i.getInspectionDate() + ",");
                        writer.write(i.getInspType() + ",");
                        writer.write(i.getNumCritical() + ",");
                        writer.write(i.getNumNonCritical() + ",");
                        writer.write(i.getVioLump() + ",");
                        writer.write(i.getHazardRating() + "%");
                    }
                    writer.write("\n");
                    //System.out.println("Finishing writing for " + r.getName());
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    public boolean getLastTime(Context context, AlertDialog.Builder updatePrompt) {
        //System.out.println("in 3");
        boolean needsUpdate = false;
        File file = new File(context.getFilesDir() + "/finalCSV.csv");
        try {
            FileReader fr = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fr);
            try {
                String date = bufferedReader.readLine();
                System.out.println("String to parse is1 " + date);
                String[] datesplit = date.split(",");
                date = datesplit[0];
                System.out.println("String to parse is " + date);
                String[] tokens = date.split(" ");
                Date olddate = new Date(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]));
                //System.out.println("The old date is " + olddate);

                String[] timetokens = date.split(" ");
                Date ddate = new Date(Integer.parseInt(timetokens[2]) - 1900, Integer.parseInt(timetokens[0]), Integer.parseInt(timetokens[1]), Integer.parseInt(timetokens[3]), Integer.parseInt(timetokens[4]), Integer.parseInt(timetokens[5]));
                Date current = new Date();
                long diffcurrent = current.getTime();
                long diffold = ddate.getTime();
                if ((diffcurrent - diffold) > 72000000) {
                    System.out.println("IM NOT HERE");
                    AlertDialog updateD = updatePrompt.create();
                    needsUpdate = GetRequests.checkIsSurreyRestaurantsUpdated(context,updateD,file);
                    if (needsUpdate == false){
                        needsUpdate = GetRequests.checkIsSurreyInspectionUpdated(context,updateD,file);
                    }




                    //System.out.println("returning " + needsUpdate);
                    return needsUpdate;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Date current = new Date();
        System.out.println("THE Current DATE IS:" + current);
        return needsUpdate;
    }
    public static void printOutFav(Context context){
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        File filed = new File(context.getFilesDir() + "/favouriteCSV.csv");
        FileReader fr3;
        try {
            fr3 = new FileReader(filed);
            BufferedReader bufferedReader = new BufferedReader(fr3);
            String output;
            try {
                output = bufferedReader.readLine();
                System.out.println("The discard is "+ output);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("doing print here");
            int count = 0;
            try {
                while((output = bufferedReader.readLine()) != null){
                    System.out.println("The out is " + output);
                    count++;
                }
                System.out.println(count);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void printOutList(String basedir,Context context) {
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        File filed = new File(context.getFilesDir() + "/finalCSV.csv");
        FileReader fr3;
        try {
            fr3 = new FileReader(filed);
            BufferedReader bufferedReader = new BufferedReader(fr3);
            String output;
            try {
                output = bufferedReader.readLine();
                System.out.println("The discard is "+ output);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("doing print here");
            int count = 0;
            try {
                while((output = bufferedReader.readLine()) != null){
                    //System.out.println("The out is " + output);
                    count++;
                }
                System.out.println(count);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void writeEmptyInspectionFile(final Context context, final File file, final mapPageActivity mainActivity, final ProgressDialog organizeDialog, Thread emptyThread, final boolean cancelled) {
        RestaurantManager restaurantManag = RestaurantManager.getInstance();
        if (RestaurantManager.getAliveList() != null) {
            RestaurantManager.getAliveList().getRestaurantList().clear();
        }
        for (Restaurant r: restaurantManag.getRestaurantList()){
            r.getInspectionsManager().getInspectionsList().clear();
        }
        final RestaurantManager favShow = new RestaurantManager();
        emptyThread = new Thread() {

            @Override
            public void run() {
                super.run();

                //RestaurantManager realManager = new RestaurantManager();
                RestaurantManager restaurantManager = RestaurantManager.getInstance();

                InputStream inputStream = new ByteArrayInputStream(restaurantManager.getGetInspections().getBytes(StandardCharsets.UTF_8));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                List<String> listGetInspections = new ArrayList<>();
                String buffer;
                try {
                    buffer = bufferedReader.readLine();
                    Date current = new Date();
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
                try {
                    while ((buffer = bufferedReader.readLine()) != null) {
                        listGetInspections.add(buffer);
                    }
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }

                int restNum = 0;
                //Test Code
                /*
                for (Restaurant r: RestaurantManager.getFavouriteList().getRestaurantList()){
                    if (restNum < 5) {
                        r.setTotalLatestVio(0);
                    }
                    restNum++;
                    System.out.println("THE NAME IS " + r.getName() + " and the Tracking code is " + r.getTrackingNumber() + " and the total is " + r.getTotalLatestVio());


                }
                */
                //Test Code
                RestaurantManager favshow = new RestaurantManager();
                for (int j = 0;j<restaurantManager.getRestaurantList().size();j++) {
                    int totalInspect = 0;
                    Restaurant r = restaurantManager.getRestaurantList().get(j);
                    if (mainActivity.isCancelled()){
                        j = restaurantManager.getRestaurantList().size();
                    }
                    double in = restaurantManager.getRestaurantList().indexOf(r);
                    double size = restaurantManager.getRestaurantList().size();
                    //System.out.println("It should be" + restaurantManager.getRestaurantList().indexOf(r) + "/" + restaurantManager.getRestaurantList().size());
                    organizeDialog.setProgress((int) ((in) / (size) * 100));
                    //System.out.println("The Restaurant " + r.getName() + " is number " + restNum + " and is looking");
                    restNum++;
                    double inspectionsremoved = listGetInspections.size();

                    if (file.exists()) {
                        double count = 0;
                        for (Iterator<String> iterator = listGetInspections.iterator(); iterator.hasNext(); ) {
                            //System.out.println("The inspections left is " + inspectionsremoved);

                            String inspectionOutput = iterator.next();
                            //System.out.println("The String is " + inspectionOutput);


                            if (inspectionOutput.equals(",,,,,,")) {
                                //System.out.println("Catch the 6 random commas");
                                //iterator.remove();
                            } else {
                                count++;
                                String[] itokens = inspectionOutput.split(",");
                                if (itokens[0].equals(r.getTrackingNumber())) {
                                    totalInspect++;
                                    Inspections currentInspection = new Inspections();
                                    currentInspection.setTrackingNumber(itokens[0]);

                                    currentInspection.setInspectionDate(itokens[1]);

                                    currentInspection.setInspType(itokens[2]);

                                    currentInspection.setNumCritical(Integer.parseInt(itokens[3]));

                                    currentInspection.setNumNonCritical(Integer.parseInt(itokens[4]));

                                    String concVioLump = "";
                                    itokens[itokens.length - 1] = itokens[itokens.length - 1].replace("\"", "");
                                    currentInspection.setHazardRating(itokens[(itokens.length - 1)]);

                                    if (itokens.length == 6) {
                                        currentInspection.setVioLump("");
                                    } else {
                                        for (int i = 5; i < itokens.length - 1; i++) {
                                            if (i == 5) {
                                                concVioLump = concVioLump.concat(itokens[5]);
                                            } else {
                                                concVioLump = (concVioLump + ",").concat(itokens[i]);
                                            }
                                        }
                                        currentInspection.setVioLump(concVioLump);

                                    }
                                    if (currentInspection.getVioLump() == null) {
                                        currentInspection.setVioLump("");

                                    }

                                    r.getInspectionsManager().getInspectionsList().add(currentInspection);
                                    iterator.remove();
                                }
                            }
                        }
                        r.setTotalLatestVio(totalInspect);
                        //System.out.println("Working on Restaurant " +  r.getName());
                        for (Restaurant v: RestaurantManager.getFavouriteList().getRestaurantList()) {
                            String currentResTrack = r.getTrackingNumber().replace("\"","");
                            String favRestTrack = v.getTrackingNumber().replace("\"","");
                            if(currentResTrack.equals(favRestTrack) && r.getTotalLatestVio() != v.getTotalLatestVio()){
                                System.out.println("The check has " + r.getTotalLatestVio() + " and the favourite " + v.getName() + " has " + v.getTotalLatestVio());
                                favShow.getRestaurantList().add(r);
                                //RestaurantManager.getFavouriteListToShow().getRestaurantList().add(r);
                                //RestaurantManager.setFavouriteListToShow(favshow);
                                //System.out.println("It is size " + RestaurantManager.getFavouriteListToShow().getRestaurantList().size());

                            }
                        }
                    }
                }
                RestaurantManager.setFavouriteListToShow(favshow);
                System.out.println("It is size " + RestaurantManager.getFavouriteListToShow().getRestaurantList().size());
                if (mainActivity.isCancelled()){
                    if (!mainActivity.fileExists()){
                        System.out.println("FILE IS DEAD");
                        restaurantManager.getRestaurantList().clear();
                        for (Restaurant r: realManager.getRestaurantList()){
                            if (r.getInspectionsManager().getInspectionsList().size() != 0) {
                                System.out.println("ITS HERE and it has something" +
                                        "");
                            }
                        }
                        RestaurantManager.setManager(realManager);
                    }
                    else{
                        restaurantManager.getRestaurantList().clear();
                        if(aliveList.getRestaurantList().size()!=0){
                            System.out.println("IM HERE");
                            RestaurantManager.setManager(realManager);

                        }
                        else if(RestaurantManager.getCancelledlist() != null){
                            Log.e("Im"," in cancelled list");
                            for (Restaurant c: RestaurantManager.getCancelledlist().getRestaurantList()){
                                restaurantManager.getRestaurantList().add(c);
                            }
                        }
                        ListModifiers.sortAlpha();
                    }
                }
                GetRequests.getLastInspectModified(context, file, mainActivity);
                organizeDialog.dismiss();
                RestaurantManager.setFavouriteListToShow(favShow);
                mainActivity.populateRestaurantsSQL_DB();
            }
        };
        emptyThread.start();
    }

    public void writeEmptyFile(File file, Context context, List<Restaurant> clonekiller, mapPageActivity mainActivity) {
        System.out.println("WRITING AN EMPTY FILE");
        RestaurantManager restaurantManager = RestaurantManager.getInstance();

        restaurantManager.getRestaurantList().clear();
        InputStream inputStream = new ByteArrayInputStream(restaurantManager.getGetList().getBytes(StandardCharsets.UTF_8));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String buffer;
        try {
            buffer = bufferedReader.readLine();
            System.out.println("Buffer is " + buffer);
            Date current = new Date();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            while ((buffer = bufferedReader.readLine()) != null) {
                boolean hasClone = false;
                String[] tokens = buffer.split(",");
                /*
                for(Restaurant r: clonekiller){
                    String rPhyAd = r.getPhysicalAddress().replace("\"","");
                    System.out.println("The address: " + tokens[2] + " matches: " + rPhyAd + " for " + r.getName());
                    if (tokens[2].equals(rPhyAd)) {
                        System.out.println("The Restaurant " + r.getName() + " has a clone");
                        hasClone = true;
                    }
                }

                 */
                if(!hasClone) {
                    Restaurant gRestaurant = new Restaurant();
                    gRestaurant.setTrackingNumber(tokens[0]);
                    tokens[1] = tokens[1].replace("\"", "");
                    gRestaurant.setName(tokens[1]);

                    gRestaurant.setDescription("ADD DESCRIPTION");
                    gRestaurant.setHazardImg(R.drawable.med);
                    gRestaurant.setPhysicalAddress(tokens[2]);
                    gRestaurant.setPhysicalCity(tokens[3]);
                    gRestaurant.setFacType(tokens[4]);
                    try {
                        gRestaurant.setLatitude(Double.parseDouble(tokens[5]));
                    } catch (Exception NumberFormatException) {
                        gRestaurant.setLatitude(0);
                    }
                    try {
                        gRestaurant.setLongitude(Double.parseDouble(tokens[6]));
                    } catch (Exception NumberFormatException) {
                        gRestaurant.setLongitude(0);
                    }
                    //System.out.println("Just created: " + gRestaurant);


                    restaurantManager.getRestaurantList().add(gRestaurant);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        GetRequests.getLastModified(context,file);
    }

    public void generateRestaurantList(Context context, RestaurantManager restaurantManager, mapPageActivity mainActivity) {
        System.out.println("IN GRL");
        List<Restaurant> clonekiller = new ArrayList<>();
        InputStream restaurantStream = context.getResources().openRawResource(R.raw.restaurants_itr1);
        if (RestaurantManager.getCancelledlist() != null) {
            RestaurantManager.getCancelledlist().getRestaurantList().clear();
        }
        RestaurantManager cancelledList = new RestaurantManager();
        BufferedReader reader = new BufferedReader(new InputStreamReader(restaurantStream, StandardCharsets.UTF_8));
        String line = "";
        int linecount = 0;
        //Skip the first line
        try {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while ((line = reader.readLine()) != null) {
                //Split by ','
                String[] token = line.split(",");
                //Read the data
                Restaurant currentRestaurant = new Restaurant();
                Restaurant cancelledRestaurant = new Restaurant();
                token[0] = token[0].replace("\"","");
                System.out.println("CURRENTREST HAS " + token[0]);
                currentRestaurant.setTrackingNumber(token[0]);
                cancelledRestaurant.setTrackingNumber(token[0]);
                token[1] = token[1].replace("\"", "");
                currentRestaurant.setName(token[1]);
                cancelledRestaurant.setName(token[1]);

                currentRestaurant.setDescription("ADD DESCRIPTION");
                currentRestaurant.setHazardImg(R.drawable.med);
                token[2] = token[2].replace("\"","");
                currentRestaurant.setPhysicalAddress(token[2]);
                cancelledRestaurant.setPhysicalAddress(token[2]);
                currentRestaurant.setPhysicalCity(token[3]);
                cancelledRestaurant.setPhysicalCity(token[3]);
                currentRestaurant.setFacType(token[4]);
                cancelledRestaurant.setFacType(token[4]);
                try {
                    currentRestaurant.setLatitude(Double.parseDouble(token[5]));
                    cancelledRestaurant.setLatitude(Double.parseDouble(token[5]));
                } catch (Exception NumberFormatException) {
                    currentRestaurant.setLatitude(0);
                    cancelledRestaurant.setLatitude(0);
                }

                try {
                    currentRestaurant.setLongitude(Double.parseDouble(token[6]));
                    cancelledRestaurant.setLongitude(Double.parseDouble(token[6]));
                } catch (Exception NumberFormatException) {
                    currentRestaurant.setLongitude(0);
                    cancelledRestaurant.setLongitude(0);
                }
                restaurantManager.getRestaurantList().add(currentRestaurant);
                cancelledList.getRestaurantList().add(cancelledRestaurant);
                Log.d("CancelledList", "Just added to clone :" + currentRestaurant);
                System.out.println("");
                clonekiller.add(currentRestaurant);
                //Log.d("ResturantInspector", "Just Created :" + currentRestaurant);
            }

        } catch (IOException e) {
            Log.wtf("Restaurant Inspection", "Error reading data file on line " + line, e);
            e.printStackTrace();
        }
        RestaurantManager.setCancelledlist(cancelledList);
        String basicdir = "";
        StringBuffer rBuffer = new StringBuffer();


        //Launches if there has been a get request
        System.out.println("IN GRL2 and it is ");
        if (restaurantManager.getGetList() != null) {
            BufferedWriter writer = null;
            System.out.println("IN GRL3");
            String filename = "fileCSV.csv";
            String compDate = "";
            String basedir = context.getFilesDir() + "/" + filename;
            //System.out.println("the base dir is " + basedir);
            File file = new File(basedir);
            System.out.println("inhere");
                try {
                    Log.d("ERROR FINDING FILE", " FILE CREATED");
                    file.createNewFile();

                    writeEmptyFile(file, context,clonekiller,mainActivity);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

            ListModifiers.sortAlpha();
    }

    public void setTotalVio(){

        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        for(Restaurant r: restaurantManager.getRestaurantList()){
            if(r.getLatestInspection() != null) {
                r.setTotalLatestVio(r.getLatestInspection().getNumCritical() + r.getLatestInspection().getNumNonCritical());
            }
            else{
                r.setTotalLatestVio(0);
            }
        }
    }
    public void setLatestDate(){
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date latest = null;
        Date test = null;

        try {
            test = df.parse("0001/01/01");
            latest = df.parse("0001/01/01");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Error parse test", "parsing has failed");
        }
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        for (Restaurant r: restaurantManager.getRestaurantList()){
            try {
                test = df.parse("0001/01/01");
                latest = df.parse("0001/01/01");
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("Error parse test", "parsing has failed");
            }
            //Log.d("It has ", "started a new Class for " + r.getName());
            //System.out.println("FOR RESTAURANT " + r.getName() + " THE SIZE IS " + r.getInspectionsManager().getInspectionsList().size());

            for (Inspections i: r.getInspectionsManager().getInspectionsList()){
                try {
                    test = df.parse(i.getInspectionDate().substring(0,4) + "/" + i.getInspectionDate().substring(4,6) + "/" + i.getInspectionDate().substring(6,8));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Log.d("Currently Testing ", "For restaurant:" + r.getName() + " Test Date: " + test + " vs Latest date " + latest);
                if (test.compareTo(latest) > 0){
                    r.setLatestInspection(i);
                    latest = test;
                    if (latest.getDate() < 10) {
                        if (latest.getMonth() < 9){
                            r.setLatestDate(latest.getYear() + 1900 + "/0" + (latest.getMonth()+1) + "/0" + (latest.getDate()));
                        }
                        else{
                            r.setLatestDate(latest.getYear() + 1900 + "/" + (latest.getMonth()+1) + "/0" + (latest.getDate()));
                        }
                    }
                    else{
                        if (latest.getMonth() < 10) {
                            r.setLatestDate(latest.getYear() + 1900 + "/0" + (latest.getMonth()+1) + "/" + (latest.getDate()));
                        }
                        else{
                            r.setLatestDate(latest.getYear() + 1900 + "/" + (latest.getMonth()+1) + "/" + (latest.getDate()));
                        }
                    }
                }
            }
            if(r.getLatestInspection() == null){
                r.setLatestDate("No Inspection found");
            }
        }
    }
    public void generateInspectionLists(Context context , RestaurantManager restaurantManager, mapPageActivity mainActivity, ProgressDialog organizeDialog, Thread emptyThread, boolean cancelled, RestaurantManager canlist) {
        System.out.println("EXISTS THERE");
        InputStream inspectionStream = context.getResources().openRawResource(R.raw.inspectionreports_itr1);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inspectionStream , StandardCharsets.UTF_8)
        );
        List<Inspections> inspectionsList = new ArrayList<>();
        List<Inspections> inspectionsListCan = new ArrayList<>();
        String line = "";
        int linecount = 0;
        try {
            reader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Set the inspections from itr1;
        try {
            while (((line = reader.readLine()) != null)) {
                String[] token = line.split(",");
                //Log.i("Number vs tracking", token[0] + " v " +r.getTrackingNumber());
                token[0] = token[0].replace("\"","");
                //if(token[0].equals(r.getTrackingNumber()) ) {
                    System.out.println("FIXED");
                    //Log.d("Number of tokens",token.length + "");
                    //Read the data
                    //r.getInspectionsManager().getInspectionsList().add(new Inspections());
                    Inspections currentInspection = new Inspections();
                    Inspections cancelledInspection = new Inspections();
                    currentInspection.setTrackingNumber(token[0]);
                    cancelledInspection.setTrackingNumber(token[0]);
                    try{currentInspection.setInspectionDate((token[1]));
                        cancelledInspection.setInspectionDate((token[1]));}
                    catch(Exception NumberFormatException)
                    {currentInspection.setNumCritical(-1);
                        cancelledInspection.setNumCritical(-1);}

                    currentInspection.setInspType(token[2]);
                    cancelledInspection.setInspType(token[2]);

                    try{currentInspection.setNumCritical(Integer.parseInt(token[3]));
                        cancelledInspection.setNumCritical(Integer.parseInt(token[3]));
                    }
                    catch(Exception NumberFormatException)
                    {currentInspection.setNumCritical(-1);
                        cancelledInspection.setNumCritical(-1);
                    }

                    try{currentInspection.setNumNonCritical(Integer.parseInt(token[4]));
                        cancelledInspection.setNumNonCritical(Integer.parseInt(token[4]));
                    }
                    catch(Exception NumberFormatException)
                    {currentInspection.setNumNonCritical(-1);
                        cancelledInspection.setNumNonCritical(-1);}
                    token[5] = token[5].replace("\"","");
                    currentInspection.setHazardRating(token[5]);
                    cancelledInspection.setHazardRating(token[5]);

                    //concatonating violump string
                    String concVioLump = "";
                    if(token.length == 6){
                        currentInspection.setVioLump("");
                        cancelledInspection.setVioLump("");
                    }
                    else{
                        for (int i = 6; i < token.length; i++){
                            if (i == 6){
                                concVioLump = concVioLump.concat(token[i]);
                            }
                            else {
                                concVioLump = (concVioLump + ",").concat(token[i]);
                            }

                        }
                        currentInspection.setVioLump(concVioLump);
                        cancelledInspection.setVioLump(concVioLump);
                    }
                    linecount++;
                    //Log.d("InspectionInspector", "Just Created :" + currentInspection + " for " + r.getName());
                    //Log.d("The number of lines is", linecount + "");
                //}
                inspectionsList.add(currentInspection);
                inspectionsListCan.add(cancelledInspection);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.wtf("Inspection Inspection", "Error reading data file on line " +  line, e);
        }
        for (Restaurant r : restaurantManager.getRestaurantList()) {
            for (Inspections i : inspectionsList) {
                if (i.getTrackingNumber().equals(r.getTrackingNumber())) {
                    r.getInspectionsManager().getInspectionsList().add(i);
                }
            }
        }


            //r.getInspectionsManager().getInspectionsList().add(currentInspection);
            //System.out.println("THE SIZE OF CANCELLED LIST IS" + RestaurantManager.getCancelledlist().getRestaurantList().size());
            for (Restaurant t: RestaurantManager.getCancelledlist().getRestaurantList()) {
                //System.out.println(r.getTrackingNumber() + " vs " + t.getTrackingNumber());
                for (Inspections i: inspectionsListCan) {
                    if (t.getTrackingNumber().equals(i.getTrackingNumber())) {
                        //if (cancelledInspection.getInspectionDate() != null) {
                        //System.out.println("FOR " + r.getName() + " we are adding " + cancelledInspection.getInspectionDate());
                        t.getInspectionsManager().getInspectionsList().add(i);
                        //}
                    }
                }
            }

            //Skip the first line
        //Populate from itr2
        if (restaurantManager.getGetInspections() != null) {
            BufferedWriter writer = null;
            String filename = "finalCSV.csv";
            String basedir = context.getFilesDir() + "/" + filename;
            //System.out.println("the base dir is " + basedir);
            File file = new File(basedir);
            //if there has been a get request and there's no file which should only be this
                try {
                    Log.d("ERROR FINDING INSPECTION FILE", " FILE CREATED");
                    file.createNewFile();
                    writeEmptyInspectionFile(context,file,mainActivity,organizeDialog,emptyThread,cancelled);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
    public void populateViolations(){
        RestaurantManager restaurantManager = RestaurantManager.getInstance();

        for (Restaurant r : restaurantManager.getRestaurantList()) {
            for (Inspections inspection: r.getInspectionsManager().getInspectionsList()) {
                inspection.getViolationsManager().getViolationsList().clear();
                String vioLump = inspection.getVioLump();
                if (inspection.getTrackingNumber().equals("SHEN-9ZSST8")){
                    System.out.println("For inspect " + inspection.getInspectionDate() + "+" + inspection.getTrackingNumber() + " the violump is " + vioLump);
                };
                //
                if (vioLump == null) {
                    inspection.setVioLump("");
                }
                else if(inspection.getVioLump().equals("")){

                }
                else{
                    String regexString = "^[0-9]*$";
                    String[] sepVio = vioLump.split("\\|");
                    for (int i = 0; i < sepVio.length; i++) {
                        String[] tokens = sepVio[i].split(",");
                        //System.out.println("" + tokens.length + " " + sepVio[i]);
                        Violations violation = new Violations();
                        String desc = "";
                        for (int j = 0; j < tokens.length; j++) {
                            if (j == 0) {
                                tokens[0] = tokens[0].replace("\"","");
                                violation.setNature(tokens[0]);
                            }
                            if (tokens[j].equals("Critical")) {
                                violation.setItCritical(true);
                            }
                            else if(tokens[j].equals("Not Critical")){
                                violation.setItCritical(false);
                            }
                            else if(tokens[j].matches(regexString)) {
                                violation.setNumViolations(tokens[j]);
                            }
                            else {
                                desc = desc.concat(tokens[j]);
                                violation.setDescription(tokens[j]);
                            }
                        }
                        violation.setDescription(desc);
                        inspection.getViolationsManager().getViolationsList().add(violation);
                    }
                }
            }
        }
    }
    public static void readFavouriteCsv(Context context,String filew){
        RestaurantManager.getFavouriteList().getRestaurantList().clear();
        boolean exists = false;
        System.out.println("reading attempt");
        String filepath = "favouriteCSV.csv";
        File filex = new File(filew);

        if(filex == null || !filex.exists()){
            exists = false;
        }
        else {exists = true;}

        if (exists == true) {
            System.out.println("IM IN HERE READING THIS");

            File file = new File(context.getFilesDir() + "/favouriteCSV.csv");
            StringBuffer stringBuffer = new StringBuffer();
            BufferedReader bufferedReader = null;
            try {
                FileReader fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                file.createNewFile();
                String output;
                output = bufferedReader.readLine();
                System.out.println("discarding " + output);

                while ((output = bufferedReader.readLine()) != null) {

                    String[] atoken = output.split("~");
                    if (atoken.length > 1) {
                        stringBuffer.append(output + "\n");
                        String[] itoken = atoken[1].split("%");
                        String[] rtoken = atoken[0].split(",");
                        Restaurant currentRestaurant = new Restaurant();
                        currentRestaurant.setTrackingNumber(rtoken[0]);
                        currentRestaurant.setName(rtoken[1]);
                        currentRestaurant.setPhysicalAddress(rtoken[2]);
                        currentRestaurant.setPhysicalCity(rtoken[3]);
                        currentRestaurant.setFacType(rtoken[4]);
                        currentRestaurant.setLatitude(Double.parseDouble(rtoken[5]));
                        currentRestaurant.setLongitude(Double.parseDouble(rtoken[6]));
                        String description;


                        for (int i = 0; i < itoken.length; i++) {
                            String[] iitoken = itoken[i].split(",");
                            Inspections currentInspection = new Inspections();
                            currentInspection.setTrackingNumber(iitoken[0]);
                            currentInspection.setInspectionDate(iitoken[1]);
                            currentInspection.setInspType(iitoken[2]);
                            currentInspection.setNumCritical(Integer.parseInt(iitoken[3]));
                            currentInspection.setNumNonCritical(Integer.parseInt(iitoken[4]));
                            String conc = "";
                            for (int j = 5; j < iitoken.length - 1; j++) {

                                conc = conc.concat(iitoken[j] + ",");

                            }
                            currentInspection.setVioLump(conc);

                            iitoken[6] = iitoken[6].replace("\"", "");
                            iitoken[6] = iitoken[6].replace("%", "");
                            currentInspection.setHazardRating(iitoken[iitoken.length - 1]);
                            currentRestaurant.getInspectionsManager().getInspectionsList().add(currentInspection);
                        }
                        Inspections latestInspect = null;
                        String latest = "0";
                        for (Inspections i: currentRestaurant.getInspectionsManager().getInspectionsList()){
                            if (Integer.parseInt(i.getInspectionDate()) > Integer.parseInt(latest)){
                                latest = i.getInspectionDate();
                                currentRestaurant.setLatestInspection(i);
                            }
                        }

                        description = ListModifiers.latestToIntelligentFormat(currentRestaurant);
                        System.out.println("The number of inspections for this is " +  currentRestaurant.getInspectionsManager().getInspectionsList().size());
                        currentRestaurant.setTotalLatestVio(itoken.length);
                        if (currentRestaurant.getLatestInspection() == null){
                            currentRestaurant.setDescription("Hazard rating for Last Inspection: "+  "No Inspection Found"  + "\nLast Inspection was on: " + description);
                        }
                        else {
                            currentRestaurant.setDescription("Hazard rating for Last Inspection: " + currentRestaurant.getLatestInspection().getHazardRating() + "\nLast Inspection was on: " + description);
                        }
                        System.out.println(currentRestaurant.getDescription() + " Is the Desc");
                        RestaurantManager.getFavouriteList().getRestaurantList().add(currentRestaurant);
                    } else {
                        String[] rtoken = atoken[0].split(",");
                        if (rtoken.length > 1) {
                            Restaurant currentRestaurant = new Restaurant();
                            currentRestaurant.setTrackingNumber(rtoken[0]);
                            currentRestaurant.setName(rtoken[1]);
                            currentRestaurant.setPhysicalAddress(rtoken[2]);
                            currentRestaurant.setPhysicalCity(rtoken[3]);
                            currentRestaurant.setFacType(rtoken[4]);
                            currentRestaurant.setLatitude(Double.parseDouble(rtoken[5]));
                            currentRestaurant.setLongitude(Double.parseDouble(rtoken[6]));
                            RestaurantManager.getFavouriteList().getRestaurantList().add(currentRestaurant);
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i("Printing out"," Favourites");
        for (Restaurant r: RestaurantManager.getFavouriteList().getRestaurantList()){
            Log.i("The restaurant is: ", r.getName() + " with " + r.getInspectionsManager().getInspectionsList().size());
        }

    }
    public static void writeFavouriteCsv(Context context,boolean toggledOff,String trackingNum,int restaurant_position){


        if (RestaurantManager.getFavouriteList() != null) {
            System.out.println("IM IN HERE");
            BufferedWriter writer = null;
            String filename = "favouriteCSV.csv";
            String basedir = context.getFilesDir() + "/" + filename;
            //System.out.println("the base dir is " + basedir);
            File file = new File(basedir);
            //if there has been a get request and there's no file which should only be this
            file.delete();
            try {
                Log.d("CREATING FAVOURITE FILE", " FILE CREATED");
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileWriter fileWriter = new FileWriter(file);
                writer = new BufferedWriter(fileWriter);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //add or remove?
            //remove
            if (toggledOff) {
                System.out.println("Deleting r");
                for (int i = 0; i < RestaurantManager.getFavouriteList().getRestaurantList().size(); i++) {
                    if (RestaurantManager.getFavouriteList().getRestaurantList().get(i).getTrackingNumber().equals(trackingNum)) {
                        System.out.println("Removing " + RestaurantManager.getFavouriteList().getRestaurantList().get(i).getName());
                        RestaurantManager.getFavouriteList().getRestaurantList().remove(i);
                    }
                }
            }
            //add

            else {
                RestaurantManager restaurantManager = RestaurantManager.getInstance();
                Restaurant toCopyRestaurant = restaurantManager.getRestaurantList().get(restaurant_position);
                InspectionsManager inspectionsManager = new InspectionsManager();
                Restaurant copyRestaurant = new Restaurant();
                copyRestaurant.setTotalLatestVio(toCopyRestaurant.getTotalLatestVio());
                copyRestaurant.setTrackingNumber(toCopyRestaurant.getTrackingNumber());
                copyRestaurant.setLatestInspection(toCopyRestaurant.getLatestInspection());
                copyRestaurant.setLatestDate(toCopyRestaurant.getLatestDate());
                //copyRestaurant.setInspectionsManager(inspectionsManager);
                copyRestaurant.setHazardImg(toCopyRestaurant.getHazardImg());
                String description;
                description = ListModifiers.latestToIntelligentFormat(copyRestaurant);
                if (copyRestaurant.getLatestInspection() != null) {
                    copyRestaurant.setDescription("Hazard rating for Last Inspection : " + copyRestaurant.getLatestInspection().getHazardRating() + "\nLast Inspection was on: " + description);
                }
                else {
                    copyRestaurant.setDescription("Hazard rating for Last Inspection : " + "No Inspection Found" + "\nLast Inspection was on: " + description);
                }
                //copyRestaurant.setDescription(toCopyRestaurant.getDescription());

                copyRestaurant.setName(toCopyRestaurant.getName());
                copyRestaurant.setPhysicalAddress(toCopyRestaurant.getPhysicalAddress());
                copyRestaurant.setPhysicalCity(toCopyRestaurant.getPhysicalCity());
                copyRestaurant.setFacType(toCopyRestaurant.getFacType());
                copyRestaurant.setLatitude(toCopyRestaurant.getLatitude());
                copyRestaurant.setLongitude(toCopyRestaurant.getLongitude());
                for (Inspections i: toCopyRestaurant.getInspectionsManager().getInspectionsList()){
                    Inspections copyInspection = new Inspections();
                    copyInspection.setViolationsManager(i.getViolationsManager());
                    copyInspection.setDescription(i.getDescription());
                    copyInspection.setTrackingNumber(i.getTrackingNumber());
                    copyInspection.setInspectionDate(i.getInspectionDate());
                    copyInspection.setInspType(i.getInspType());
                    copyInspection.setNumCritical(i.getNumCritical());
                    copyInspection.setNumNonCritical(i.getNumNonCritical());
                    copyInspection.setVioLump(i.getVioLump());
                    copyInspection.setHazardRating(i.getHazardRating());

                    inspectionsManager.getInspectionsList().add(copyInspection);
                }
                copyRestaurant.setInspectionsManager(inspectionsManager);
                RestaurantManager.getFavouriteList().getRestaurantList().add(copyRestaurant);
            }
            //PrintCheck
            RestaurantManager restaurantManager = RestaurantManager.getInstance();
            if (RestaurantManager.getFavouriteList() != null){
                for (Restaurant r: RestaurantManager.getFavouriteList().getRestaurantList()){
                    System.out.println("THE RESTAURANT " + r.getName() +  " IS HERE WITH " + r.getInspectionsManager().getInspectionsList().size() + " INSPECTIONS and the description is " + r.getDescription());
                }


            }
            //Finally write it
            try {
                file.createNewFile();

                for (Restaurant r: RestaurantManager.getFavouriteList().getRestaurantList()){
                    writer.write(r.getTrackingNumber() + ",");
                }
                writer.write("\n");

                for (Restaurant r : RestaurantManager.getFavouriteList().getRestaurantList()) {
                    writer.write(r.getTrackingNumber() + ",");
                    writer.write(r.getName() + ",");
                    writer.write(r.getPhysicalAddress() + ",");
                    writer.write(r.getPhysicalCity() + ",");
                    writer.write(r.getFacType() + ",");
                    writer.write(r.getLatitude() + ",");
                    writer.write(r.getLongitude() + "~");
                    for (Inspections i : r.getInspectionsManager().getInspectionsList()) {
                        writer.write(i.getTrackingNumber() + ",");
                        writer.write(i.getInspectionDate() + ",");
                        writer.write(i.getInspType() + ",");
                        writer.write(i.getNumCritical() + ",");
                        writer.write(i.getNumNonCritical() + ",");
                        writer.write(i.getVioLump() + ",");
                        writer.write(i.getHazardRating() + "%");
                    }
                    writer.write("\n");
                    //System.out.println("Finishing writing for " + r.getName());
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        }

       public void refreshFavList(Context context,String filew){
            RestaurantManager restaurantManager = RestaurantManager.getInstance();
            File file = new File(filew);
            StringBuffer stringBuffer = new StringBuffer();
            BufferedReader bufferedReader = null;
            String output = "";
            try {
                FileReader fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
           try {
               output = bufferedReader.readLine();
               System.out.println(output);
           } catch (IOException e) {
               e.printStackTrace();
           }
           RestaurantManager.getFavouriteList().getRestaurantList().clear();
           String[] otoken = output.split(",");
           for (Restaurant r: restaurantManager.getRestaurantList()){
               for (int i = 0; i < otoken.length; i++) {
                    if (otoken[i].equals(r.getTrackingNumber())){
                        r.setFavStatus(true);
                        writeFavouriteCsv(context,false,r.getTrackingNumber(),restaurantManager.getRestaurantList().indexOf(r));
                    }

               }
           }
       }
}
