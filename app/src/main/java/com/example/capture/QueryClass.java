package com.example.capture;

import static com.example.capture.Private.APIKey;
import static com.example.capture.Private.DeveloperAPI;

public class QueryClass {
    public static  String getQueryForSearchingKeyword(String search , String page){
        if (search.equals("")){
            search = "random";
        }
        if (page.equals("")){
            page = "1";
        }
        return DeveloperAPI + "search/photos?page="+page+"&query=" +search + "&" +  APIKey;
    }
}
