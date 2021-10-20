package com.example.resturant_inspection_app_group_15.UI.MapActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.resturant_inspection_app_group_15.Model.CSV.CSVReader;
import com.example.resturant_inspection_app_group_15.Model.CSV.GetRequests;
import com.example.resturant_inspection_app_group_15.Model.CSV.ListModifiers;
import com.example.resturant_inspection_app_group_15.Model.DataBaseSQLite.RestInspDBOpenHelper;
import com.example.resturant_inspection_app_group_15.Model.DataBaseSQLite.RestaurantViewModel;
import com.example.resturant_inspection_app_group_15.Model.InspectionPackage.Inspections;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.Restaurant;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.RestaurantManager;
import com.example.resturant_inspection_app_group_15.Model.ViolationPackage.Violations;
import com.example.resturant_inspection_app_group_15.R;
import com.example.resturant_inspection_app_group_15.UI.FavouriteActivity.AdapterForFavourite;
import com.example.resturant_inspection_app_group_15.UI.FirstActivity.MainActivity;
import com.example.resturant_inspection_app_group_15.UI.SecondActivity.clickRestaurant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.File;

/*
            Here we created a map that shows off each restaurant with a peg
            It is the first page to show on app start and shows users current location
            Integrated it seamlessly between Activity 2 and 1 with back button,
            clicking on pegs as well as clicking the switch button
 */

public class mapPageActivity extends AppCompatActivity {
    //updates for SQLite database
    private RestInspDBOpenHelper mHelper = null;
    private SQLiteDatabase mDB = null;
    private RestaurantViewModel mViewModel = null;

    private static final String TAG = "MapActivity";
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private boolean populatedSQL = false;

    private static final int DEFAULT_ZOOM = 15;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private CameraPosition cameraPosition;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean locationPermissionGranted;

    private Location lastKnownLocation;

    private static Dialog dialog;
    private boolean shownDialog = false;

    AlertDialog.Builder updatePrompt;
    RestaurantManager restaurantManager = RestaurantManager.getInstance();
    CSVReader csvReader = new CSVReader();
    ProgressDialog progressDialog;
    ProgressDialog organizeDialog;
    Thread emptyThread;
    boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_page);

        mViewModel = ViewModelProviders.of(this).get(RestaurantViewModel.class);

        String filew = mapPageActivity.this.getFilesDir() + "/favouriteCSV.csv";
        CSVReader.readFavouriteCsv(mapPageActivity.this, filew);

        final Intent booleanCheckExit = new Intent(getApplicationContext(), MainActivity.class);
        booleanCheckExit.putExtra("fromMap_toMain", true);

//        final ToggleButton toggleButton = findViewById(R.id.toggleButton);
//        toggleButton.setText(getString(R.string.list_view));
//        toggleButton.setTextOn(getString(R.string.map_view));
//        toggleButton.setTextOff(getString(R.string.list_view));
//
//        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if (isChecked) {
//                    startActivity(booleanCheckExit);
//                    finish();
//                }
//            }
//        });

        // fragment toggle button
        Button btnToggle = (Button)findViewById(R.id.toggle);
        btnToggle.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = getDefaultFragment();
                if (f == null) {
                    setDefaultFragment(new RestMapFragment());
                } else if (f instanceof RestMapFragment) {
                    Fragment found = findFragment(RestListFragment.TAG);
                    if (found == null) {
                        found = new RestListFragment();
                    }
                    setDefaultFragment(found);
                } else if (f instanceof RestListFragment) {
                    Fragment found = findFragment(RestMapFragment.TAG);
                    if (found == null) {
                        found = new RestMapFragment();
                    }
                    setDefaultFragment(found);
                }
            }
        });

        // search button
        Button btnSearch = (Button)findViewById(R.id.search);
        btnSearch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.query);
                String value = editText.getText().toString();
                mViewModel.search(mDB, value);
            }
        });

        // create map fragment and set as default
        Fragment found = findFragment(RestMapFragment.TAG);
        if (found == null) {
            found = new RestMapFragment();
        }
        setDefaultFragment(new RestMapFragment());

        getLocationPermission();
//        initMap();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        //TODO: IF FILE GETS CORRUPTED UNCOMMENT THIS OUT AND RUN


//        String inspectFileName = mapPageActivity.this.getFilesDir() + "/inspectionreports_itr2.csv";
//        String filename = mapPageActivity.this.getFilesDir() + "/favouriteCSV.csv";
//        String file3 = mapPageActivity.this.getFilesDir() + "/finalCSV.csv";
//        File file = new File(filename);
//        File ifile = new File(inspectFileName);
//        File ffile = new File(file3);
//        ifile.delete();
//        file.delete();
//        ffile.delete();


        progressDialog = setupProgress();
        organizeDialog = setupOrganize();
        System.out.println(fileExists());
        updatePrompt = new AlertDialog.Builder(this);

        boolean gotUpdated = setupupdatePrompt(updatePrompt, this);
        if (fileExists()) {
            System.out.println("SOMEHOW HERE");
            gotUpdated = csvReader.getLastTime(mapPageActivity.this, updatePrompt);
        }
        if (!gotUpdated) {
            if (fileExists()) {
                System.out.println("in here");
                startMain(3);
            } else {
                System.out.println("IN ELSE");
                GetRequests.processCSVLink(mapPageActivity.this, this, progressDialog, organizeDialog);
            }
        }

        //making SQLite database
        if (mHelper == null) {
            mHelper = new RestInspDBOpenHelper(this);
        }
        mDB = mHelper.getWritableDatabase();

        // search all data
        //mViewModel.search(mDB,null);
    }

    protected void onDestroy() {
        if (mDB != null) {
            mDB.close();
        }
        super.onDestroy();
    }

    private Fragment findFragment(String fragmentTag) {
        FragmentManager fm = this.getSupportFragmentManager();
        return fm.findFragmentByTag(fragmentTag);
    }

    private Fragment getDefaultFragment() {
        Fragment f = null;
        FragmentManager fm = this.getSupportFragmentManager();

        int count = fm.getBackStackEntryCount();
        if (count > 0) {
            f = (Fragment) fm.findFragmentById(R.id.fragmentContainer);
        }

        return f;
    }

    private void setDefaultFragment(Fragment fragment) {
        String tag = fragment.getClass().getName();

        FragmentManager fm = this.getSupportFragmentManager();
        boolean popped = fm.popBackStackImmediate(tag, 0);

        if (!popped && fm.findFragmentByTag(tag) == null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContainer, fragment, tag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(tag);
            ft.commit();
        }
    }

    public void populateRestaurantsSQL_DB() {
        for (Restaurant r : restaurantManager.getRestaurantList()) {
            appendRestaurantsIfNotExistsInDB(r, mDB);
            for (Inspections i : r.getInspectionsManager().getInspectionsList()) {
                appendInspectionsIfNotExistsInDB(i, mDB);
                for(Violations v: i.getViolationsManager().getViolationsList()){
                    appendViolationsIfNotExistsInDB(v, mDB);
                }
            }
        }
    }

    public Boolean appendRestaurantsIfNotExistsInDB(Restaurant currentrestaurant, SQLiteDatabase db) {
        if (db == null) {
            return false;
        }

        Boolean result = false;
        db.beginTransaction();

        try {
            //need to check if the data already exists
            String trackingNumber = currentrestaurant.getTrackingNumber();

            String sql = "SELECT trackingnumber, name FROM restaurants WHERE trackingnumber='" + trackingNumber + "'";

            Cursor cursor = null;
            Boolean found = false;
            try{
                cursor = db.rawQuery(sql, null);
                if(cursor != null && cursor.getCount() > 0){ //found, data already exists
                    found = true;
                }
            } finally {
                if(cursor != null){
                    cursor.close();
                }
            }
            if(found){ //skip append b/c already exists
                return false;
            }

            //insert new restaurants
            ContentValues values = new ContentValues();

            values.put(Restaurant.Entry.COLUMN_NAME_TRACKING_NUMBER, currentrestaurant.getTrackingNumber());
            values.put(Restaurant.Entry.COLUMN_NAME_NAME, currentrestaurant.getName());
            values.put(Restaurant.Entry.COLUMN_NAME_CITY, currentrestaurant.getPhysicalCity());
            values.put(Restaurant.Entry.COLUMN_NAME_ADDR, currentrestaurant.getPhysicalAddress());
            values.put(Restaurant.Entry.COLUMN_NAME_TYPE, currentrestaurant.getFacType());
            values.put(Restaurant.Entry.COLUMN_NAME_LATITUDE, currentrestaurant.getLatitude());
            values.put(Restaurant.Entry.COLUMN_NAME_LONGITUDE, currentrestaurant.getLongitude());

            long id = db.insert(Restaurant.Entry.TABLE_NAME, null, values);
            if (id > 0) {
                //nothing to do
            }
            db.setTransactionSuccessful();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return result;
    }

    public Boolean appendInspectionsIfNotExistsInDB(Inspections currentinspection, SQLiteDatabase db){
        if(db == null){
            return false;
        }

        Boolean result = false;
        db.beginTransaction();

        try{
            //need to check if the data already exists
            String trackingNumber = currentinspection.getTrackingNumber();
            String inspectionDate = currentinspection.getInspectionDate();

            String[] projection = {
                        Inspections.Entry.COLUMN_NAME_TRACKING_NUMBER,
                        Inspections.Entry.COLUMN_NAME_INSPECTION_DATE
            };

            String selection = Inspections.Entry.COLUMN_NAME_TRACKING_NUMBER + " = ? AND " +
                    Inspections.Entry.COLUMN_NAME_INSPECTION_DATE + "= ?";
            String[] selectionArgs = {trackingNumber, inspectionDate};

            Cursor cursor = null;
            Boolean found = false;

            try{
                cursor = db.query(
                        Inspections.Entry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                if(cursor != null && cursor.getCount() > 0){ //found, data already exists
                    found = true;
                }
            } finally {
                if(cursor != null){
                    cursor.close();
                }
            }

            if(found){ //skip append b/c already exists
                return result;
            }

            //insert new restaurants
            ContentValues values = new ContentValues();

            values.put(Inspections.Entry.COLUMN_NAME_TRACKING_NUMBER, currentinspection.getTrackingNumber());
            values.put(Inspections.Entry.COLUMN_NAME_INSPECTION_DATE, currentinspection.getInspectionDate());
            values.put(Inspections.Entry.COLUMN_NAME_INSPEC_TYPE, currentinspection.getInspType());
            values.put(Inspections.Entry.COLUMN_NAME_NUM_CRITICAL, currentinspection.getNumCritical());
            values.put(Inspections.Entry.COLUMN_NAME_NUM_NON_CRITICAL, currentinspection.getNumNonCritical());
            values.put(Inspections.Entry.COLUMN_NAME_HAZARD_RATING, currentinspection.getHazardRating());
            values.put(Inspections.Entry.COLUMN_NAME_VIO_LUMP, currentinspection.getVioLump());

            long id = db.insert(Inspections.Entry.TABLE_NAME, null, values);
            if(id > 0){
                //nothing to do
            }
            db.setTransactionSuccessful();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            db.endTransaction();
        }

        return result;
    }

    public Boolean appendViolationsIfNotExistsInDB(Violations currentviolation , SQLiteDatabase db){
        if(db == null){
            return false;
        }

        Boolean result = false;
        db.beginTransaction();

        try{
            //need to check if the data already exists
            String numviolation = currentviolation.getNumViolations();

            String[] projection = {
                    Violations.Entry.COLUMN_NAME_NUMVUILATIONS
            };

            String selection = Violations.Entry.COLUMN_NAME_NUMVUILATIONS + " = ?";
            String[] selectionArgs = {numviolation};

            Cursor cursor = null;
            Boolean found = false;

            try{
                cursor = db.query(
                        Violations.Entry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                if(cursor != null && cursor.getCount() > 0){ //found, data already exists
                    found = true;
                }
            } finally {
                if(cursor != null){
                    cursor.close();
                }
            }
            if(found){ //skip append b/c already exists
                return result;
            }
            //insert new restaurants
            ContentValues values = new ContentValues();

            values.put(Violations.Entry.COLUMN_NAME_NUMVUILATIONS, currentviolation.getNumViolations());
            values.put(Violations.Entry.COLUMN_NAME_ISITCRITICAL, currentviolation.isItCritical());
            values.put(Violations.Entry.COLUMN_NAME_DESCRIPTION, currentviolation.getDescription());
            values.put(Violations.Entry.COLUMN_NAME_NATURE, currentviolation.getNature());

            long id = db.insert(Violations.Entry.TABLE_NAME, null, values);
            if(id > 0){
                //nothing to do
            }
            db.setTransactionSuccessful();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            db.endTransaction();
        }
        return result;
    }

    private boolean setupupdatePrompt(AlertDialog.Builder updatePrompt, final mapPageActivity mapPageActivity) {
        final boolean[] returnbol = {false};
        updatePrompt.setMessage(R.string.update_avail_want_to_update).setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //For yes
                for (Restaurant r: restaurantManager.getRestaurantList()){
                    for (Inspections in: r.getInspectionsManager().getInspectionsList()){
                        in.getViolationsManager().getViolationsList().clear();
                    }
                    r.getInspectionsManager().getInspectionsList().clear();
                }
                restaurantManager.getRestaurantList().clear();

                GetRequests.processCSVLink(mapPageActivity.this,mapPageActivity,progressDialog,organizeDialog);
                dialogInterface.cancel();
                returnbol[0] = true;
            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //for no
                dialogInterface.cancel();
            }
        });
        updatePrompt.create();
        updatePrompt.setTitle(R.string.update_available);

        return returnbol[0];
    }

    public boolean fileExists() {
        String filepath = "finalCSV.csv";
        File file = mapPageActivity.this.getFileStreamPath(filepath);
        if(file == null || !file.exists()){
            return false;
        }
        return true;
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
                shownDialog = true;
                progressDialog.dismiss();
            }
        });
        return organizeDialog;
    }

    private ProgressDialog setupProgress() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.getting_restaurants));
        progressDialog.setTitle(getString(R.string.app_name));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

//    private void initMap(){
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
//        mapFragment.getMapAsync(mapPageActivity.this);
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
//                    initMap();
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionsGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        final LatLng defaultLocation = new LatLng(0,0);
        try {
            if (mLocationPermissionsGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void buttonTo(int buttonID, final Intent intent) {
        Button btn = (Button) findViewById(buttonID);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent fromMain = getIntent();
        if (fromMain.getBooleanExtra("fromMain_toMap", false)) {
            finishAffinity();
        }
    }

    public void startMain(int cont) {
        if (cont == 1) {
            csvReader.generateRestaurantList(mapPageActivity.this, restaurantManager, this);
            System.out.println("Finished 1");
            RestaurantManager canList = new RestaurantManager();
            if (fileExists()) {
                for (Restaurant r : restaurantManager.getRestaurantList()) {
                    canList.add(r);
                    canList.setLastRestaurantModified("NeedsUpdate");
                    canList.setLastInspectionModified("NeedsUpdate");
                }
                //RestaurantManager.setAliveList(canList);
            }
            csvReader.generateInspectionLists(mapPageActivity.this, restaurantManager, this, organizeDialog, emptyThread, cancelled, canList);
            System.out.println("Finished 2");
        } else {
            System.out.println("doing this");
            csvReader.readFinalCSV(mapPageActivity.this,this);
        }
    }
    public void finish3(int map){
        Log.d(getClass().getName(), "start 3-1");
        csvReader.setLatestDate();
        Log.d(getClass().getName(), "Finished 3");
        csvReader.setTotalVio();
        Log.d(getClass().getName(), "Finished 4");
        if (map != 1) {
            System.out.println("finished 5-finish3");
            csvReader.populateViolations();
        }
        Log.d(getClass().getName(), "Finished 5");
        ListModifiers.setDescription();
        Log.d(getClass().getName(), "Finished 6");
        ListModifiers.setRestaurantListDescription();
        Log.d(getClass().getName(), "Finished 7");
        ListModifiers.sortTimeline();
        Log.d(getClass().getName(), "Finished 8");
        System.out.println("NOW IT IS " + restaurantManager.getRestaurantList().size());
//        createMap();

        if(populatedSQL){
            populateRestaurantsSQL_DB();
        }
        populatedSQL = true;

        /*
        if (!shownDialog) {
            if (RestaurantManager.getFavouriteList().getRestaurantList().size() != 0) {
                showDial(mapPageActivity.this);
                Log.i("The Favourite List is:", " Full");
                shownDialog = true;
            } else {
                Log.e("The Favourite List is:", " Empty");
                shownDialog = true;
            }
        }
         */
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        Log.d(TAG, "onMapReady: map is ready");
//        mMap = googleMap;
//        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                View infoWindow = getLayoutInflater().inflate(R.layout.restaurant_marker, null);
//
//                TextView title = (TextView)infoWindow.findViewById(R.id.title);
//                title.setText(marker.getTitle());
//
//                TextView snippet = infoWindow.findViewById(R.id.snippet);
//                snippet.setText(marker.getSnippet());
//
//                return infoWindow;
//            }
//        });
//
//        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                String restaurantName = marker.getTitle();
//                int position = Integer.parseInt(marker.getId());
//                Intent intent = new Intent(getApplicationContext(),clickRestaurant.class);
//                intent.putExtra("Name",restaurantName);
//                intent.putExtra("Position",position);
//                startActivity(intent);
//                Log.i(TAG, restaurantName + "Clicked");
//            }
//        });
//        finish3(1);
//    }

//    public void createMap(){
//        System.out.println("IM IN HERE MAP");
//        LatLng location = new LatLng(0,0);
//        if (mMap != null) {
//            System.out.println("IN HERE 23");
//            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//                @Override
//                public View getInfoWindow(Marker marker) {
//                    return null;
//                }
//
//                @Override
//                public View getInfoContents(Marker marker) {
//                    View infoWindow = getLayoutInflater().inflate(R.layout.restaurant_marker, null);
//
//                    TextView title = (TextView) infoWindow.findViewById(R.id.title);
//                    title.setText(marker.getTitle());
//
//                    TextView snippet = (TextView) infoWindow.findViewById(R.id.snippet);
//                    snippet.setText(marker.getSnippet());
//
//                    return infoWindow;
//                }
//            });
//
//            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                @Override
//                public void onInfoWindowClick(Marker marker) {
//                    String restaurantName = marker.getTitle();
//                    int position =(int) marker.getZIndex();
//                    Intent intent = new Intent(getApplicationContext(),clickRestaurant.class);
//                    intent.putExtra("Name",restaurantName);
//                    intent.putExtra("Position",position);
//                    startActivity(intent);
//                }
//            });
//
//            for (Restaurant r : restaurantManager.getRestaurantList()) {
//                location = new LatLng(r.getLatitude(), r.getLongitude());
//
//                long mostRecentInsDate = 0;
//                String hLevel = "";
//                //System.out.println("FOR " + r.getName() + " THE INSPECTION COUNT IS " + r.getInspectionsManager().getInspectionsList().size());
//                for (Inspections i : r.getInspectionsManager().getInspectionsList()) {
//
//                    long date = Long.parseLong(i.getInspectionDate());
//                    if (r.getName().equals("Tim Horton's (RCMP)")){
//                        System.out.println("date is " + date + " and the most recent is " + mostRecentInsDate);
//
//                    }
//                    if (date > mostRecentInsDate) {
//                        mostRecentInsDate = date;
//                        hLevel = i.getHazardRating();
//                        if (r.getName().equals("Tim Horton's (RCMP)")){
//                            System.out.println("hlevel is " + hLevel);
//                        }
//                    }
//                }
//
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.zIndex(restaurantManager.getRestaurantList().indexOf(r));
//                markerOptions.title(r.getName());
//
//                if(hLevel.equals("Low")){
//                    markerOptions.snippet(r.getPhysicalAddress() + "\n" + getText(R.string.hlevel_add) + " " + getText(R.string.hlevel_low));
//                }
//                else if(hLevel.equals("Moderate")){
//                    markerOptions.snippet(r.getPhysicalAddress() + "\n" + getText(R.string.hlevel_add) + " " + getText(R.string.hlevel_mod));
//                }
//                else{
//                    markerOptions.snippet(r.getPhysicalAddress() + "\n" + getText(R.string.hlevel_add) + " " + getText(R.string.hlevel_high));
//                }
//
//
//                markerOptions.position(location);
//
//                if (hLevel.equals("Low")) {
//                    BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.good);
//                    Bitmap b = bitmapDraw.getBitmap();
//                    Bitmap lowMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
//
//                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(lowMarker));
//                } else if (hLevel.equals("Moderate")) {
//                    BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.med);
//                    Bitmap b = bitmapDraw.getBitmap();
//                    Bitmap moderateMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
//
//                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(moderateMarker));
//                }
//
//                else if (hLevel.equals("High")) {
//                    BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.bad);
//                    Bitmap b = bitmapDraw.getBitmap();
//                    Bitmap badMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
//
//                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(badMarker));
//                }
//                else{
//                    BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.good);
//                    Bitmap b = bitmapDraw.getBitmap();
//                    Bitmap lowMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
//
//                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(lowMarker));
//                }
//
//                mMap.addMarker(markerOptions);
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
//            }
//
//            Intent innt = getIntent();
//            if (innt.getBooleanExtra("check",false)) {
//                double longitude = innt.getDoubleExtra("long",location.latitude);
//                double latitude = innt.getDoubleExtra("lat",location.longitude);
//                mMap.moveCamera((CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude))));
//            }
//            else {
//                getDeviceLocation();
//            }
//        }
//    }

    public void startCSVGen() {
        System.out.println("start 3");
        csvReader.setLatestDate();
        System.out.println("Finished 3");
        csvReader.setTotalVio();
        System.out.println("Finished 4");
        csvReader.populateViolations();
        System.out.println("Finished 5-csv");
        ListModifiers.setDescription();
        System.out.println("Finished 6");
        ListModifiers.setRestaurantListDescription();
        System.out.println("Finished 7");
        ListModifiers.sortTimeline();
        System.out.println("Finished 8");
        System.out.println("Gen here");
        csvReader.generateFinalCSV(mapPageActivity.this);
        csvReader.remakeFinal(mapPageActivity.this,this);

//        createMap();

        if (!shownDialog) {
            if (RestaurantManager.getFavouriteList().getRestaurantList().size() != 0) {
                showDial(mapPageActivity.this);
                Log.i("The Favourite List is:", " Full");
                shownDialog = true;
            } else {
                Log.e("The Favourite List is:", " Empty");
                shownDialog = true;
            }
        }
//        populateRestaurantsSQL_DB();
    }

    private void showDial(Activity activity){
        if (RestaurantManager.getFavouriteListToShow().getRestaurantList().size() != 0) {
            System.out.println("The thign is " + RestaurantManager.getFavouriteListToShow().getRestaurantList().size());

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
            AdapterForFavourite adapter = new AdapterForFavourite(mapPageActivity.this, RestaurantManager.getFavouriteListToShow().getRestaurantList());
            //AdapterForFavourite adapter = new AdapterForFavourite(mapPageActivity.this,RestaurantManager.getFavouriteList().getRestaurantList());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            dialog.create();
            dialog.show();
            String filew = mapPageActivity.this.getFilesDir() + "/favouriteCSV.csv";
            csvReader.refreshFavList(mapPageActivity.this,filew);

        }
        else{
            Toast.makeText(activity, "No Favourite Restaurant has been updated", Toast.LENGTH_SHORT).show();
        }
    }
}
