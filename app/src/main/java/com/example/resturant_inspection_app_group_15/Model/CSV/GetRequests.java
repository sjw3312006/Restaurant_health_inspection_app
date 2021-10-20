package com.example.resturant_inspection_app_group_15.Model.CSV;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.resturant_inspection_app_group_15.Model.RestaurantPackage.RestaurantManager;
import com.example.resturant_inspection_app_group_15.UI.FirstActivity.MainActivity;
import com.example.resturant_inspection_app_group_15.UI.MapActivity.mapPageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class GetRequests {

    public static void processCSVLink(final Context context, final mapPageActivity mainActivity, final ProgressDialog progressDialog, final ProgressDialog organizeDialog){
        final String[] returned = {""};
        final RestaurantManager restaurantManager = RestaurantManager.getInstance();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray jsonArray = result.getJSONArray("resources");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String csvLink = jsonObject.getString("url");
                    //System.out.println("The last date modified is " + lastmodified);
                    //Log.d("FIND ME" , "" + lastmodified);
                    //System.out.println(" The thing is " +jsonArray.getString(18) + " "+ jsonArray.getString(18)+ " "+jsonArray.getString(19));
                    returned[0] = csvLink;
                    //System.out.println("ALso it is " + returned[0]);
                    //System.out.println("IKt's this " + returned[0]);
                    getGetInspections(context,mainActivity,progressDialog,organizeDialog,returned[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);

    }
    public static void getGetInspections(final Context context, final mapPageActivity mainActivity, final ProgressDialog progressDialog, final ProgressDialog organizeDialog,String csvurl){

        final RestaurantManager restaurantManager = RestaurantManager.getInstance();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = csvurl;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("im responding and the response is " + response);
                System.out.println("This is the response");
                restaurantManager.setGetInspections(response);
                processCSVRestaurantLink(context,mainActivity,progressDialog,organizeDialog);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

    }
    public static void processCSVRestaurantLink (final Context context, final mapPageActivity mainActivity, final ProgressDialog progressDialog, final ProgressDialog organizeDialog){
        final String[] returned = {""};
        final RestaurantManager restaurantManager = RestaurantManager.getInstance();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://data.surrey.ca/api/3/action/package_show?id=restaurants";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray jsonArray = result.getJSONArray("resources");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String csvLink = jsonObject.getString("url");
                    //System.out.println("The last date modified is " + lastmodified);
                    //Log.d("FIND ME" , "" + lastmodified);
                    //System.out.println(" The thing is " +jsonArray.getString(18) + " "+ jsonArray.getString(18)+ " "+jsonArray.getString(19));
                    returned[0] = csvLink;
                    //System.out.println("ALso it is " + returned[0]);
                    //System.out.println("IKt's this " + returned[0]);
                    getRequest(context,mainActivity,progressDialog,organizeDialog,returned[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
    public static void getRequest(Context context, final mapPageActivity mainActivity, final ProgressDialog progressDialog, final ProgressDialog organizeDialog,String csvurl){
        final RestaurantManager restaurantManager = RestaurantManager.getInstance();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = csvurl;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("In getRequest");
                restaurantManager.setGetList(response);
                //System.out.println(restaurantManager.getGetList());
                mainActivity.startMain(1);
                progressDialog.dismiss();
                organizeDialog.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error can't retrieve get Restaurant");
                progressDialog.dismiss();
            }
        });
        queue.add(stringRequest);
        //progress
        progressDialog.show();

    }
    public static String getLastInspectModified(Context context, final File file, final mapPageActivity mainActivity){
        final String[] returned = {""};
        final RestaurantManager restaurantManager = RestaurantManager.getInstance();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray jsonArray = result.getJSONArray("resources");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String lastmodified = jsonObject.getString("last_modified");
                    returned[0] = lastmodified;
                    Date current = new Date();
                    InputStream inputStream = null;
                    String replace = current.getMonth() + " " + current.getDate() + " " + (current.getYear() + 1900) + " " + current.getHours() + " " + current.getMinutes() + " " + current.getSeconds() + "," + lastmodified;
                    //System.out.println("replace inspect is " + replace);
                    restaurantManager.setLastInspectionModified(lastmodified);
                    mainActivity.startCSVGen();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        return returned[0];
    }
    public static String getLastModified(Context context, final File file){
        final String[] returned = {""};
        final RestaurantManager restaurantManager = RestaurantManager.getInstance();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://data.surrey.ca/api/3/action/package_show?id=restaurants";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray jsonArray = result.getJSONArray("resources");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String lastmodified = jsonObject.getString("last_modified");
                    //System.out.println("The last date modified is " + lastmodified);
                    //Log.d("FIND ME" , "" + lastmodified);
                    //System.out.println(" The thing is " +jsonArray.getString(18) + " "+ jsonArray.getString(18)+ " "+jsonArray.getString(19));
                    returned[0] = lastmodified;
                    System.out.println("ALso it is " + returned[0]);
                    Date current = new Date();
                    InputStream inputStream = null;
                    String replace = current.getMonth() + " "+ current.getDate()+ " " + (current.getYear()+1900) +" " +current.getHours() + " " + current.getMinutes() + " " + current.getSeconds();
                    restaurantManager.setGetDate(replace);
                    //System.out.println("replace rest is " + replace);
                    restaurantManager.setLastRestaurantModified(lastmodified);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
        return returned[0];
    }
    public static boolean checkIsSurreyInspectionUpdated(Context context, final AlertDialog updatePrompt, final File file){
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        RequestQueue queue = Volley.newRequestQueue(context);
        final boolean[] needsUpdate = {false};
        String url = "http://data.surrey.ca/api/3/action/package_show?id=fraser-health-restaurant-inspection-reports";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray jsonArray = result.getJSONArray("resources");
                    //System.out.println(" The thing is ");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String lastmodified = jsonObject.getString("last_modified");
                    //System.out.println(" The thing is " +jsonArray.getString(18) + " "+ jsonArray.getString(18)+ " "+jsonArray.getString(19));
                    System.out.println("The last date modified for inspections is " + lastmodified);
                    InputStream inputStream = null;
                    String line = "";
                    try {
                        inputStream = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    try {
                        line = bufferedReader.readLine();
                        System.out.println("line is "+line);
                        String[] linetoken = line.split(",");
                        line = linetoken[3];
                    } catch (IOException e) {
                        Log.e("Error: " , " Can't read first line");
                        e.printStackTrace();
                    }
                    System.out.println("THE LAST MODIFIDED IS |" + lastmodified + "| vs |" + line + "|");
                    if(lastmodified.equals(line)){
                        Log.d("Reached", " No modification detected");
                    }
                    else{
                        Log.d("Reached "," Modification detected");
                        needsUpdate[0] = true;
                        if(!updatePrompt.isShowing()){
                            updatePrompt.show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
        return needsUpdate[0];
    }
    public static boolean checkIsSurreyRestaurantsUpdated(Context context, final AlertDialog updatePrompt, final File file){
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        RequestQueue queue = Volley.newRequestQueue(context);
        final boolean[] needsUpdate = {false};
        String url = "http://data.surrey.ca/api/3/action/package_show?id=restaurants";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject result = response.getJSONObject("result");
                    JSONArray jsonArray = result.getJSONArray("resources");
                    //System.out.println(" The thing is ");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String lastmodified = jsonObject.getString("last_modified");
                    //System.out.println(" The thing is " +jsonArray.getString(18) + " "+ jsonArray.getString(18)+ " "+jsonArray.getString(19));
                    System.out.println("The last date modified is also " + lastmodified);
                    InputStream inputStream = null;
                    String line = "";
                    try {
                        inputStream = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    try {
                        line = bufferedReader.readLine();
                        System.out.println("line is "+line);
                        String[] linetoken = line.split(",");
                        line = linetoken[1];
                    } catch (IOException e) {
                        Log.e("Error: " , " Can't read first line");
                        e.printStackTrace();
                    }

                    if(lastmodified.equals(line)){
                        Log.d("Reached", " No modification detected");
                    }
                    else{
                        Log.d("Reached "," Modification detected");
                        if (!updatePrompt.isShowing()) {
                            System.out.println("Over here");
                            updatePrompt.show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
                queue.add(request);
                return needsUpdate[0];
    }

}
