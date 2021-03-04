package com.example.capture.DownloadImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveImage {
    public static  String saveToSdCard(Bitmap bitmap , String filename , Context context) throws IOException {
        String stored = null;
        Log.d("yd" , bitmap.toString());
        File sdcard = Environment.getExternalStorageDirectory();

        File folder = new File(sdcard.getAbsolutePath() , "/Capture");

        folder.mkdir();
        File file = new File(folder.getAbsoluteFile(), filename + ".jpg") ;

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            stored = "success";
            MediaScannerConnection.scanFile(context, new String[] { file.getPath() }, new String[] { "image/jpeg" }, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return stored;
    }
}
