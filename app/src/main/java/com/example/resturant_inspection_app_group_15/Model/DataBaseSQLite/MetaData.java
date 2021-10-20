package com.example.resturant_inspection_app_group_15.Model.DataBaseSQLite;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MetaData {
    private static final String TAG = "MetaData";
    private Date created = null;
    private Date modified = null;
    private String csvDownloadUrl = "";

    MetaData(){
        this.created = null;
        this.modified = null;
        this.csvDownloadUrl = "";
    }

    MetaData(String created, String modified, String csvDownloadUrl){
        Date date = null;

        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");
            this.created = format.parse(created);
            this.modified = format.parse(modified);
            this.csvDownloadUrl = csvDownloadUrl;
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }
    }

    public Date getCreate(){
        return this.created;
    }

    public Date getModified(){
        return this.modified;
    }

    public String getCsvDownloadUrl(){
        return this.csvDownloadUrl;
    }
}
