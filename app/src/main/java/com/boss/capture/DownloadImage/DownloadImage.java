package com.boss.capture.DownloadImage;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.boss.capture.R;
import com.boss.capture.Services.GetStorageFileNames;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImage {
    Context context;
    NotificationManagerCompat mNotificationManager;
    NotificationCompat.Builder mBuilder;
    int id ;
    public DownloadImage(String DownloadUrl , String ImageName , Context context){
        this.context = context;
        if (GetStorageFileNames.GetDownloadedFilesNames().contains(ImageName + ".jpg")){
            Toast.makeText(context , "Already Downloaded ! " , Toast.LENGTH_SHORT).show();
        }
        else {
            GetStorageFileNames.GetDownloadedFilesNames().add(ImageName + ".jpg");
            Toast.makeText(context , "Downloading Started " , Toast.LENGTH_SHORT).show();
            id = (int) Math.ceil(Math.random()*10);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                sendNotification(ImageName);
            }
            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) new GetImageAfterTen(DownloadUrl , ImageName).execute();
            else new GetImagesBelowTen(DownloadUrl , ImageName).execute();
        }
    }

    private class GetImageAfterTen extends AsyncTask<String, Void, Bitmap> {
        private String requestUrl, imageName;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        private GetImageAfterTen(String requestUrl , String _imageName_) {
            this.requestUrl = requestUrl;
            this.imageName = _imageName_ ;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = requestUrl;
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(urlDisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.println(Log.ASSERT, "error", "" + e);
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {

            try {
                saveImage(result , imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private  class GetImagesBelowTen extends AsyncTask<Object , Integer , Object>{
        private String requestUrl, imageName;

        private GetImagesBelowTen(String requestUrl , String _imageName_) {
            this.requestUrl = requestUrl;
            this.imageName = _imageName_ ;
        }
        @Override
        protected Object doInBackground(Object... objects) {
            File folder = new File(Environment.getExternalStorageDirectory() , "/Capture");
            folder.mkdir();
            File file = new File(folder.getAbsoluteFile(), imageName + ".jpg") ;

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
                Toast.makeText(context , "Downloaded " + imageName + ".jpg" , Toast.LENGTH_SHORT).show();
                NotifyDownloadComplete();
        }
    }

    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        boolean saved;
        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "Capture");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + "Capture";

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".jpg");
            fos = new FileOutputStream(image);

        }


        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        mNotificationManager.cancel(id);
        Toast.makeText(context , "Downloaded " , Toast.LENGTH_SHORT).show();
        NotifyDownloadComplete();

        fos.flush();
        fos.close();
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String imageName){
        mNotificationManager = NotificationManagerCompat.from(context);
        mBuilder = new NotificationCompat.Builder(context , "1");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            mBuilder.setContentTitle("Downloading image" )
                    .setContentText("Please wait")
                    .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
                    .setColor(context.getResources().getColor(R.color.main_green));
        }else{
            mBuilder.setContentTitle("Downloading " + imageName+ ".jpg" )
                    .setContentText("Download in progress...")
                    .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
                    .setColor(context.getResources().getColor(R.color.main_green))
                    .setProgress(100 , 0 , false);
        }
        mNotificationManager.notify(id , mBuilder.build());
    }
}
