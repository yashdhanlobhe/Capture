package com.example.capture.Services;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class GetStorageFileNames {
    public static ArrayList<String> DownloadedFilesName;
    
    public static ArrayList<String> GetDownloadedFilesNames(){
        DownloadedFilesName = new ArrayList<>();
        try{
            File loc = new File(Environment.getExternalStorageDirectory().getPath() + "/Capture");
            for (File f : loc.listFiles()) {
                DownloadedFilesName.add(f.getName());
            }
        }catch (Exception e){
        }
        return  DownloadedFilesName;
    }
}
