package com.example.capture.DownloadImage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission {
    public static  boolean checkPermission(Context context , String permission){
        boolean ans = false;
        if(ContextCompat.checkSelfPermission(context , permission) == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(context , "You Not given permission to download and store in storage. you can give it from setting", Toast.LENGTH_LONG).show();
            }
            else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, 1);
            }
        }
        else {
            ans = true;
        }
        return ans;
    }
}
