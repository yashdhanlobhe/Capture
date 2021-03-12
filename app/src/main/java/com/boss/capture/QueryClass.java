package com.boss.capture;

import static com.boss.capture.Private.APIKey;
import static com.boss.capture.Private.DeveloperAPI;

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
