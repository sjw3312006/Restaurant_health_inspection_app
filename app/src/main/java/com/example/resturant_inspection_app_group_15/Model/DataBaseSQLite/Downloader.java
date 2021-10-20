package com.example.resturant_inspection_app_group_15.Model.DataBaseSQLite;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Downloader {
    public static String TAG = "Downloader";

    public Downloader(){}

    private MetaData parseJSON(JSONObject json) throws JSONException{
        String success = json.getString("success");
        String created = json.getJSONObject("result").getString("metadata_created");
        String modified = json.getJSONObject("result").getString("metadata_modified");
        String csvDownloadUrl = json.getJSONObject("result").getJSONArray("resources")
                .getJSONObject(0).getString("url");

        Log.e(TAG, "success: " + success);
        Log.e(TAG, "created: " + created);
        Log.e(TAG, "modified " + modified);
        Log.e(TAG, "CSV: " + csvDownloadUrl);

        MetaData metaData = null;
        if(success.equals("true")){
            metaData = new MetaData(created, modified, csvDownloadUrl);
        }
        return metaData;
    }

    private static String getStringFromInputStream(InputStream inputStream){
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        try{
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(bufferedReader != null){
                try{
                    bufferedReader.close();
                }catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    public MetaData downloadMetaData(URL url){
        MetaData metaData = null;

        try{
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.connect();

            InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            JSONObject jsonObject = new JSONObject(getStringFromInputStream(inputStream));

            metaData = parseJSON(jsonObject);
        }catch(MalformedURLException e){
            Log.e(TAG, e.toString());
        }catch(JSONException e){
            Log.e(TAG, e.toString());
        }catch(IOException e){
            Log.e(TAG, e.toString());
        }
        return metaData;
    }

    public ArrayList<String> downloadCSV(URL url){
        ArrayList<String> csv = new ArrayList<>();

        try{
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                csv.add(line);
            }
            bufferedReader.close();
        }catch (MalformedURLException e){

        }catch (IOException e){

        }return csv;
    }
}
