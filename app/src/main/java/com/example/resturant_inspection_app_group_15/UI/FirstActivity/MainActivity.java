package com.example.resturant_inspection_app_group_15.UI.FirstActivity;

/*
        The Main Activity will call CSV Package to get CSVReader and ListModifier.
        CSVReader will get us all the information we need from the data and put it in variables.
        Then those variables will be manipulated by ListModifier to make them look the way customer requested,
        We then call the Adapter and Display in same package to display it here.
*/

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.example.resturant_inspection_app_group_15.Model.CSV.CSVReader;
import com.example.resturant_inspection_app_group_15.Model.CSV.GetRequests;
import com.example.resturant_inspection_app_group_15.Model.CSV.ListModifiers;

import com.example.resturant_inspection_app_group_15.Model.InspectionPackage.Inspections;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.Restaurant;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.RestaurantManager;
import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.UI.FavouriteActivity.AdapterForFavourite;
import com.example.resturant_inspection_app_group_15.UI.MapActivity.mapPageActivity;
import com.example.resturant_inspection_app_group_15.UI.SecondActivity.clickRestaurant;
import com.example.resturant_inspection_app_group_15.UI.WelcomeActivity.SplashScreen;
import com.example.resturant_inspection_app_group_15.Model.DataBaseSQLite.RestInspDBOpenHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    RestInspDBOpenHelper dbOpenHelper;

    String getList;
    public static Dialog dialog;
    RecyclerView restaurantList;
    AdapterForDisplay adapter;
    AlertDialog.Builder updatePrompt;
    RestaurantManager restaurantManager = RestaurantManager.getInstance();
    CSVReader csvReader = new CSVReader();
    boolean gotUpdated = false;
    ProgressDialog progressDialog;
    ProgressDialog organizeDialog;
    Thread emptyThread;
    boolean cancelled = false;
    public static MainActivity mainActivity;
    String filew;


    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filew = MainActivity.this.getFilesDir() + "/favouriteCSV.csv";
        //CSVReader.readFavouriteCsv(MainActivity.this,filew);

        final Intent booleanCheck = new Intent(getApplicationContext(), mapPageActivity.class);
        booleanCheck.putExtra("fromMain_toMap",true);
        //buttonTo(R.id.testMap,booleanCheck);
        System.out.println("I EXIST");

        final ToggleButton toggleButton = findViewById(R.id.toggleButton2);
        toggleButton.setText(R.string.map_view);
        toggleButton.setTextOff(getString(R.string.mapView));
        toggleButton.setTextOn(getString(R.string.list_view));

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    startActivity(booleanCheck);
                }
            }
        });

        final String regexString = "^[0-9]*$";

        Button Search = (Button) findViewById(R.id.search_button);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText query = (EditText) findViewById(R.id.queryDB);

                if ( "".equals(query.getText().toString()) || query.getText().toString().trim().matches(regexString) ) {
                    Toast.makeText(getApplicationContext(),
                            "Your search was invalid" +
                                    "Enter only a string",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    //Run database query
                    ArrayList<Restaurant> fromQuery = dbOpenHelper.getQueryRestaurants(query.toString());

                    if (fromQuery == null || fromQuery.isEmpty()) {
                        Toast.makeText(getApplicationContext(),
                                "Your search was invalid" +
                                        "Type in another restaurant",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        // Display the selected subset that user has chosen. Need adapter
                    }
                }
            }
        });

        /*
                This may be used later however android may be smart enough to just do it auto
                For now I just wait to have until map is added

        Intent booleanCheckBack = new Intent(getApplicationContext(), clickRestaurant.class);
        booleanCheckBack.putExtra("fromMain_toSecond",true);
        buttonTo(R.id.testSecond_main,booleanCheckBack);

         */
        /*
        String inspectFileName = MainActivity.this.getFilesDir() + "/inspectionreports_itr2.csv";
        String filename = MainActivity.this.getFilesDir() + "/restaurants_itr2.csv";
        String file3 = MainActivity.this.getFilesDir() + "/finalCSV.csv";
        File file = new File(filename);
        File ifile = new File(inspectFileName);
        File ffile = new File(file3);
        ifile.delete();
        file.delete();
        ffile.delete();

         */


        progressDialog = setupProgress();
        organizeDialog = setupOrganize();
        System.out.println(fileExists());
        updatePrompt = new AlertDialog.Builder(this);


//        boolean gotUpdated = setupupdatePrompt(updatePrompt,this);
//        if (fileExists()) {
//            System.out.println("SOMEHOW HERE");
//            gotUpdated = csvReader.getLastTime(MainActivity.this,updatePrompt);
//        }
//        if (!gotUpdated) {
//            if (fileExists()) {
//                System.out.println("in here");
//                startMain(3);
//            }
//            else {
//                System.out.println("IN ELSE");
//                GetRequests.getGetInspections(MainActivity.this,this,progressDialog,organizeDialog);
//            }
//        }
        setupDeletebtn();

        setupPrintbtn();
        //setupTimeTravelbtn();
        setupScramblebtn();
        setupFragbtn();
        setupPrintFav();
        setupDeleteFav(filew);
        setupScrambleFav();
        /*
        Intent intent = new Intent(MainActivity.this, mapPageActivity.class);
        startActivity(intent);

         */
    }

    @Override
    protected void onResume() {
        super.onResume();
        restaurantList = (RecyclerView) findViewById(R.id.restaurantsList);
        restaurantList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new AdapterForDisplay(MainActivity.this, restaurantManager.getRestaurantList());
        restaurantList.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent fromMap = getIntent();
        if (fromMap.getBooleanExtra("fromMap_toMain", false)) {
            finishAffinity();
            finish();
        }
    }
    private void setupDeleteFav(final String filew){
        Button btn_deletefav = (Button) findViewById(R.id.btn_fav_del);
        btn_deletefav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(filew);
                RestaurantManager.setFavouriteList(new RestaurantManager());
                file.delete();
            }
        });


    }
    private void setupScrambleFav(){
        Button btn_scramble_fav = (Button) findViewById(R.id.btn_scramble_fav);
        btn_scramble_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestaurantManager testMananager = new RestaurantManager();
                testMananager.getRestaurantList().add(restaurantManager.getRestaurantList().get(0));
                testMananager.getRestaurantList().add(restaurantManager.getRestaurantList().get(1));
                testMananager.getRestaurantList().add(restaurantManager.getRestaurantList().get(2));
                testMananager.getRestaurantList().add(restaurantManager.getRestaurantList().get(3));


                //RestaurantManager.getFavouriteList().getRestaurantList().clear();
                RestaurantManager.getFavouriteList().getRestaurantList().clear();
                RestaurantManager.setFavouriteList(testMananager);
                for (Restaurant r: RestaurantManager.getFavouriteList().getRestaurantList()){
                    r.setTotalLatestVio(0);
                }
                File file = new File(filew);
                file.delete();
                CSVReader.writeFavouriteCsv(MainActivity.this,false,restaurantManager.getRestaurantList().get(4).getTrackingNumber(),4);
            }
        });


    }
    private void setupPrintFav(){
        Button btn_printfav = (Button) findViewById(R.id.btn_printFav);
        btn_printfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CSVReader.printOutFav(MainActivity.this);
            }
        });

    }
    private ProgressDialog setupOrganize() {
        final ProgressDialog organizeDialog = new ProgressDialog(this);
        organizeDialog.setMessage(getString(R.string.orangize_restaurants_and_inspections));
        organizeDialog.setTitle(getString(R.string.app_name));
        organizeDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        organizeDialog.setMax(100);
        organizeDialog.setProgress(0);
        organizeDialog.setCancelable(true);
        organizeDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancelled = true;
                progressDialog.dismiss();
            }
        });
        return organizeDialog;
    }


//    private boolean setupupdatePrompt(final AlertDialog.Builder updatePrompt, final MainActivity mainActivity) {
//        final boolean[] returnbol = {false};
//        updatePrompt.setMessage("An update is available. \nDo you want to update?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //For yes
//                for (Restaurant r: restaurantManager.getRestaurantList()){
//                    for (Inspections in: r.getInspectionsManager().getInspectionsList()){
//                        in.getViolationsManager().getViolationsList().clear();
//                    }
//                    r.getInspectionsManager().getInspectionsList().clear();
//                }
//                restaurantManager.getRestaurantList().clear();
//
//                GetRequests.getGetInspections(MainActivity.this,mainActivity,progressDialog,organizeDialog);
//                dialogInterface.cancel();
//                returnbol[0] = true;
//            }
//        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //for no
//                dialogInterface.cancel();
//            }
//        });
//        updatePrompt.create();
//        updatePrompt.setTitle("Update Available");
//
//        return returnbol[0];
//    }

    private ProgressDialog setupProgress() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.getting_restaurants));
        progressDialog.setTitle(getString(R.string.app_name));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        return progressDialog;
    }
    private void setupFragbtn(){
        Button btn_frag = (Button) findViewById(R.id.btn_popFav);
        btn_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FragmentManager manager = getSupportFragmentManager();
                //dialog.show(manager, "MessageDialog");
                //Log.i("TAG!", "show work");
                showDial(MainActivity.this);

            }
        });
    }
    private void showDial(Activity activity){
        dialog = new Dialog(activity);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle("@string/the_updated_favourites_are");
        dialog.setContentView(R.layout.message_favourite);

        Button btn_exit = (Button) dialog.findViewById(R.id.btn_favExit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RecyclerView recyclerView = dialog.findViewById(R.id.fav_recycler);
        AdapterForFavourite adapter = new AdapterForFavourite(MainActivity.this,RestaurantManager.getFavouriteList().getRestaurantList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        dialog.show();

    }
    private void setupScramblebtn(){
        Button btn_scramble= (Button) findViewById(R.id.btn_scramble);
        btn_scramble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(MainActivity.this.getFilesDir() + "/finalCSV.csv");
                try {
                    InputStream inputStream = new FileInputStream(file);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer inputBuffer = new StringBuffer();
                    String time;
                    Date current = new Date();
                    time = ((current.getMonth())+ " " + current.getDate()+" " + (current.getYear()+1900) + " "+ current.getHours() + " " + current.getMinutes() + " " + current.getSeconds());
                    try {
                        System.out.println("The time is:  " + time);
                       String[] timetokens = time.split(" ");
                        Date date = new Date(Integer.parseInt(timetokens[2])-1900,Integer.parseInt(timetokens[0]),Integer.parseInt(timetokens[1]),Integer.parseInt(timetokens[3]),Integer.parseInt(timetokens[4]),Integer.parseInt(timetokens[5]));

                        System.out.println("Date is " + date);
                        long millies = date.getTime();
                        millies = millies - 72000000;
                        Date newdate = new Date(millies);
                        System.out.println("New time is: " + newdate);
                        String line = newdate.getMonth() + " " +  newdate.getDate() + " " +(newdate.getYear()+1900) +" " + newdate.getHours() + " " +newdate.getMinutes() + " " +newdate.getSeconds();
                        System.out.println("Adding in " + line);


                        BufferedWriter writer = null;
                        try {
                            FileWriter fw = new FileWriter(file);
                            writer = new BufferedWriter(fw);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("getlastrmod "+restaurantManager.getLastRestaurantModified());
                        System.out.println("getlostlmod" +restaurantManager.getLastInspectionModified());
                        inputBuffer.append(line + "," + "restaurantManager.getLastRestaurantModified()"+","+ line +","+ "restaurantManager.getLastInspectionModified()");
                        inputBuffer.append("\n");
                        //System.out.println("The res and inc is " + restaurantManager.getLastRestaurantModified() + " so " + restaurantManager.getLastInspectionModified());
                        if (restaurantManager.getStringBuffer() == null){
                            System.out.println("The buffer is null");
                        }
                        inputBuffer.append(restaurantManager.getStringBuffer());
                        bufferedReader.close();
                        //write new string
                        FileOutputStream fileOut = new FileOutputStream(file);
                        fileOut.write(inputBuffer.toString().getBytes());
                        fileOut.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static MainActivity getInstance(){
        return mainActivity;
    }
    public void setupAdapter(){
        restaurantList = (RecyclerView) findViewById(R.id.restaurantsList);
        restaurantList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new AdapterForDisplay(MainActivity.this, restaurantManager.getRestaurantList());
        restaurantList.setAdapter(adapter);
    }
//    private void setupTimeTravelbtn(){
//        Button btn_timetravel = (Button) findViewById(R.id.btn_timetravel20);
//        btn_timetravel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                File file = new File(MainActivity.this.getFilesDir() + "/finalCSV.csv");
//                try {
//                    InputStream inputStream = new FileInputStream(file);
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                    try {
//                        System.out.println("The available is " + inputStream.available());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    StringBuffer inputBuffer = new StringBuffer();
//                    String time;
//                    Date current = new Date();
//                    time = ((current.getMonth())+ " " + current.getDate()+" " + (current.getYear()+1900) + " "+ current.getHours() + " " + current.getMinutes() + " " + current.getSeconds());
//
//
//                    try {
//                        System.out.println("The time is:  " + time);
//                        String[] timetokens = time.split(" ");
//                        Date date = new Date(Integer.parseInt(timetokens[2])-1900,Integer.parseInt(timetokens[0]),Integer.parseInt(timetokens[1]),Integer.parseInt(timetokens[3]),Integer.parseInt(timetokens[4]),Integer.parseInt(timetokens[5]));
//
//                        System.out.println("Date is " + date);
//                        long millies = date.getTime();
//                        millies = millies - 72000000;
//                        Date newdate = new Date(millies);
//                        System.out.println("New time is: " + newdate);
//                        String line = newdate.getMonth() + " " +  newdate.getDate() + " " +(newdate.getYear()+1900) +" " + newdate.getHours() + " " +newdate.getMinutes() + " " +newdate.getSeconds();
//                        System.out.println("Adding in " + line);
//
//
//                        BufferedWriter writer = null;
//                        try {
//                            FileWriter fw = new FileWriter(file);
//                            writer = new BufferedWriter(fw);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        //printme += line+","+timesplit[1]+"\n";
//                        System.out.println("getlastrmod "+restaurantManager.getLastRestaurantModified());
//                        System.out.println("getlostlmod" +restaurantManager.getLastInspectionModified());
//                        inputBuffer.append(line+"," +restaurantManager.getLastRestaurantModified()+","+line+","+restaurantManager.getLastInspectionModified());
//                        inputBuffer.append("\n");
//                        //System.out.println("The res and inc is " + restaurantManager.getLastRestaurantModified() + " so " + restaurantManager.getLastInspectionModified());
//                        if (restaurantManager.getStringBuffer() == null){
//                            System.out.println("The buffer is null");
//                        }
//                        inputBuffer.append(restaurantManager.getStringBuffer());
//                        bufferedReader.close();
//                        //write new string
//                        FileOutputStream fileOut = new FileOutputStream(file);
//                        fileOut.write(inputBuffer.toString().getBytes());
//                        fileOut.close();
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//    }

    private void setupDeletebtn() {
        Button btn_delete =(Button) findViewById(R.id.deleter_button);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = MainActivity.this.getFilesDir() + "/finalCSV.csv";
                File file = new File(filename);
                file.delete();
                Log.d("Button Pressed: ", "Iteration 2 File Deleted");
            }
        });
    }

    private void setupPrintbtn(){
        Button btn_print =(Button) findViewById(R.id.printCSV_log);
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filename = MainActivity.this.getFilesDir() + "/restaurants_itr2.csv";
                csvReader.printOutList(filename,MainActivity.this);
            }
        });
    }

    public boolean fileExists(){
        String filepath = "finalCSV.csv";
        File file = MainActivity.this.getFileStreamPath(filepath);
        if(file == null || !file.exists()){
            return false;
        }
        return true;
    }
//    public void startCSVGen(){
//        System.out.println("start 3");
//
//        csvReader.setLatestDate();
//        System.out.println("Finished 3");
//        csvReader.setTotalVio();
//        System.out.println("Finished 4");
//        csvReader.populateViolations();
//        System.out.println("Finished 5");
//        ListModifiers.setDescription();
//        System.out.println("Finished 6");
//        ListModifiers.setRestaurantListDescription();
//        System.out.println("Finished 7");
//        ListModifiers.sortTimeline();
//        System.out.println("Finished 8");
//        System.out.println("Gen here");
//        csvReader.generateFinalCSV(MainActivity.this);
//        csvReader.remakeFinal(MainActivity.this,this);
//        restaurantList = (RecyclerView) findViewById(R.id.restaurantsList);
//        restaurantList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//
//        adapter = new AdapterForDisplay(MainActivity.this, restaurantManager.getRestaurantList());
//        restaurantList.setAdapter(adapter);
//    }

//    public void startMain(int cont){
//        if (cont == 1) {
//            csvReader.generateRestaurantList(MainActivity.this, restaurantManager,this);
//            System.out.println("Finished 1");
//            RestaurantManager canList = new RestaurantManager();
//            if (fileExists()) {
//                for (Restaurant r : restaurantManager.getRestaurantList()) {
//                    canList.add(r);
//                    canList.setLastRestaurantModified("TOGGLEME");
//                    canList.setLastInspectionModified("TOGGLEME");
//                }
//                RestaurantManager.setAliveList(canList);
//            }
//            csvReader.generateInspectionLists(MainActivity.this, restaurantManager,this,organizeDialog,emptyThread,cancelled,canList);
//            System.out.println("Finished 2");
//        }
//        else{
//            System.out.println("doing this");
//            csvReader.readFinalCSV(MainActivity.this);
//        }
//        if(cont == 3) {
//            System.out.println("start 3");
//            csvReader.setLatestDate();
//            System.out.println("Finished 3");
//            csvReader.setTotalVio();
//            System.out.println("Finished 4");
//            csvReader.populateViolations();
//            System.out.println("Finished 5");
//            ListModifiers.setDescription();
//            System.out.println("Finished 6");
//            ListModifiers.setRestaurantListDescription();
//            System.out.println("Finished 7");
//            ListModifiers.sortTimeline();
//            System.out.println("Finished 8");
//
//            restaurantList = (RecyclerView) findViewById(R.id.restaurantsList);
//            restaurantList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//
//            adapter = new AdapterForDisplay(MainActivity.this, restaurantManager.getRestaurantList());
//            restaurantList.setAdapter(adapter);
//        }
//    }

    private void buttonTo(int buttonID, final Intent intent) {
        Button btn = (Button) findViewById(buttonID);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }
}
