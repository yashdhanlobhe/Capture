package com.example.capture.DownloadImage;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.capture.MainActivity;
import com.example.capture.R;
import com.example.capture.Services.GetStorageFileNames;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

        private GetImages(String requestUrl , String _imagename_) {
            this.requestUrl = requestUrl;
            this.imagename = _imagename_ ;
        }
        @Override
        protected Object doInBackground(Object... objects) {
            File file = MakeFile(imagename);
            InputStream is = null;
            OutputStream os= null;
            HttpURLConnection con = null;
            int length;
            try{

                URL url = new URL(requestUrl);
                con = (HttpURLConnection) url.openConnection();

                if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                }

                length = con.getContentLength();

                is = con.getInputStream();
                os = new FileOutputStream(file.getAbsolutePath());

                byte data[] = new byte[4096];
                long total = 0;
                int count;

                while ((count = is.read(data)) != -1)
                {
                    if (isCancelled()){
                    }
                    total += count;
                    publishProgress((int) total , length);

                    os.write(data ,0 , count);

                }
            }
            catch (Exception e) {

            }
            finally {
                try {
                    MediaScannerConnection.scanFile(context, new String[] { file.getPath() }, new String[] { "image/jpeg" }, null);

                    if (is != null)
                        is.close();
                    if (os != null)
                        os.close();
                }catch (IOException ioException){
                }
                if (con != null)
                    con.disconnect();
            }

            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate();
            String total = String.valueOf(values[0]);
            String MB = "0" , KB = "0";
            if (total.length()>7){
                MB = total.substring(0 , 2);
                KB = total.substring(2 , 4);
            }
            else if (total.length()>6){
                MB = total.substring(0 , 1);
                KB = total.substring(1 , 3);
            }
            else if (total.length()>5){
                KB = total.substring(0 ,2);
            }
            else if(total.length()>4){
                KB= total.substring(0,1);
            }
            mBuilder.setContentText(MB + "." + KB + " MB" +" Downloaded" );
            mBuilder.setProgress(100 , (values[0]*100/values[1]), false);
            mNotificationManager.notify(id , mBuilder.build());

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
                mNotificationManager.cancel(id);
                Toast.makeText(context , "Downloaded " + imagename + ".jpg" , Toast.LENGTH_SHORT).show();
                NotifyDownloadComplete();
        }
    }

    private void NotifyDownloadComplete() {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context , 1 , intent , PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentText("Open Gallery");
        mBuilder.setContentTitle("Photo Downloaded");
        mNotificationManager.notify(id, mBuilder.build());
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
                .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
                .setColor(context.getResources().getColor(R.color.main_green))
                .setProgress(100 , 0 , false);
        mNotificationManager.notify(id , mBuilder.build());
    }
}
