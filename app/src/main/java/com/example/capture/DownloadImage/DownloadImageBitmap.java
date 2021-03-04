package com.example.capture.DownloadImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static com.example.capture.DownloadImage.SaveImage.saveToSdCard;

public class DownloadImageBitmap {
    public DownloadImageBitmap(String DownloadUrl , String ImageName){
        Log.d("yd" , DownloadUrl);
        new GetImages(DownloadUrl , ImageName).execute();
    }
    private  class GetImages extends AsyncTask<Object , Object , Object>{
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
                saveToSdCard(bitmap , imagename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
