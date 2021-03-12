package com.boss.capture.DownloadImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveImage {
    public static  String saveToSdCard(Bitmap bitmap , File file , Context context) throws IOException {
        String stored = null;
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
