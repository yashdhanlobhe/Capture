package com.example.capture.DownloadImage;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import com.example.capture.MainActivity;
import com.example.capture.R;
import com.example.capture.Services.GetStorageFileNames;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static androidx.core.content.FileProvider.getUriForFile;
import static com.example.capture.DownloadImage.SaveImage.saveToSdCard;
import static java.security.AccessController.getContext;

public class DownloadImageBitmap {
    Context context;
    NotificationManagerCompat mNotificationManager;
    NotificationCompat.Builder mBuilder;
    int id ;
    public DownloadImageBitmap(String DownloadUrl , String ImageName , Context context){
        this.context = context;
        if (GetStorageFileNames.GetDownloadedFilesNames().contains(ImageName + ".jpg")){
            Toast.makeText(context , "Already Downloaded ! " , Toast.LENGTH_SHORT).show();
        }
        else {
            GetStorageFileNames.GetDownloadedFilesNames().add(ImageName + ".jpg");
            Toast.makeText(context , "Downloading Started " , Toast.LENGTH_SHORT).show();
            id = (int) Math.ceil(Math.random()*10);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                sendNotifiacaiton(ImageName);
            }
            new GetImages(DownloadUrl , ImageName).execute();
        }
    }
    private  class GetImages extends AsyncTask<Object , Integer , Object>{
        private String requestUrl, imagename;
        private Bitmap bitmap ;

        private GetImages(String requestUrl , String _imagename_) {
            this.requestUrl = requestUrl;
            this.imagename = _imagename_ ;
        }
        @Override
        protected Object doInBackground(Object... objects) {
            try{
                URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }



        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                File file = MakeFile(imagename);
                saveToSdCard(bitmap , file , context);

                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                PendingIntent pendingIntent = PendingIntent.getActivity(context , 1 , intent , PendingIntent.FLAG_ONE_SHOT);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setAutoCancel(true);
                mBuilder.setContentText("Open Gallery");
                mBuilder.setContentTitle("Photo Downloaded");
                Toast.makeText(context , "Downloaded " + imagename + ".jpg" , Toast.LENGTH_SHORT).show();
                mNotificationManager.notify(id , mBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File MakeFile(String imagename) {
        File sdcard = Environment.getExternalStorageDirectory();
        File folder = new File(sdcard.getAbsolutePath() , "/Capture");
        folder.mkdir();
        File file = new File(folder.getAbsoluteFile(), imagename + ".jpg") ;

        return file;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotifiacaiton(String imageName){
        mNotificationManager = NotificationManagerCompat.from(context);
        mBuilder = new NotificationCompat.Builder(context , "1");
        mBuilder.setContentTitle("Downloading " + imageName+ ".jpg" )
                .setContentText("Download in progress...")
                .setSmallIcon(R.drawable.ic_baseline_cloud_download_24);
        mNotificationManager.notify(id , mBuilder.build());
    }
}
