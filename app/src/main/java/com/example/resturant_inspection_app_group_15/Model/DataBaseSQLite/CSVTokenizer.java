package com.example.resturant_inspection_app_group_15.Model.DataBaseSQLite;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVTokenizer {
    private static String regex = "\"([^\"]*)\"[,]|(.+?)[,?]|(.+?)$";

    public static ArrayList<String> parse(String line){
        ArrayList<String> result = new ArrayList<>();

        Matcher matcher = Pattern.compile(regex).matcher(line);
        while(matcher.find()){
            for(int i = 1; i <= matcher.groupCount(); i++){
                String value = matcher.group(i);
                if(value != null){
                    result.add(value);
                    break;
                }
            }
        }
        return result;
    }
}
