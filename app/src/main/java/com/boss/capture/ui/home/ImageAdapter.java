package com.boss.capture.ui.home;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.boss.capture.DownloadImage.DownloadImage;
import com.boss.capture.R;
import com.boss.capture.Services.GetStorageFileNames;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xyz.belvi.blurhash.BlurHashDecoder;

import static com.boss.capture.DownloadImage.Permission.checkPermission;

public class ImageAdapter extends  RecyclerView.Adapter<ImageAdapter.ImageAdapterHolder> {
    JSONArray jsonArray;
    Context mContext;
    ArrayList<String> downloaded ;
    public ImageAdapter(JSONArray jsonArray){
        this.jsonArray = jsonArray;
        downloaded = new ArrayList<>();
    }
    @NonNull
    @Override
    public ImageAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        mContext = parent.getContext();
        return new ImageAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapterHolder holder, int position) {
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonArray.get(position);
            JSONObject jsonObject1 = jsonObject.getJSONObject("urls");
            holder.textView.setText(jsonObject.getString("alt_description"));
            holder.pixel.setText("Pixel: " + jsonObject.getString("width") + "x" + jsonObject.getString("height"));
            holder.size.setText("Size: "+ getImgSize(jsonObject.getString("width") ,jsonObject.getString("height"))+ " MB");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && GetStorageFileNames.DownloadedFilesName.contains(jsonObject.getString("id")+ ".jpg")) {
                holder.downloadButton.setForeground(ContextCompat.getDrawable(mContext , R.drawable.ic_baseline_done_24));
            }

            Bitmap bitmap = (Bitmap) BlurHashDecoder
                    .INSTANCE.decode(jsonObject
                    .getString("blur_hash"), 20, 12 ,1.0f , false , null );
            Drawable d = new BitmapDrawable(mContext.getResources(), bitmap);

            Picasso.get()
                    .load(jsonObject1.getString("small"))
                    .placeholder(d)
                    .into(holder.imageView);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String getImgSize(String width, String height) {
        Float x = Float.parseFloat(width.substring(0 ,2));
        Float y = Float.parseFloat(height.substring(0,2));
        try{
            return String.valueOf(x * y * 0.003).substring(0, 4);
        }catch (Exception e){
            return String.valueOf(x * y * 0.003);
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class ImageAdapterHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Context mcontext;
        Button downloadButton;
        TextView textView , pixel , size;
        public ImageAdapterHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewForItem);
            mcontext = itemView.getContext();
            downloadButton = itemView.findViewById(R.id.downloadButton);
            textView = itemView.findViewById(R.id.imageTitleItem);
            pixel = itemView.findViewById(R.id.imagePixelItem);
            size = itemView.findViewById(R.id.imageSizeItem);
            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = (JSONObject) jsonArray.get(getAdapterPosition());
                        JSONObject jsonObject1 = jsonObject.getJSONObject("urls");
                        String name = jsonObject.getString("id") + ".jpg";

                        if (GetStorageFileNames.GetDownloadedFilesNames().contains(name)|| downloaded.contains(name) ){
                            Toast.makeText(mcontext , "Already Downloaded ! " , Toast.LENGTH_SHORT).show();
                        }
                        else {

                            if(checkPermission(mcontext , Manifest.permission.WRITE_EXTERNAL_STORAGE )){
                                try
                                {   downloaded.add(name);
                                    GetStorageFileNames.GetDownloadedFilesNames().add(name);
                                    new DownloadImage(jsonObject1.getString("full"), jsonObject.getString("id") , mcontext);
                                }
                                catch (Exception e)
                                {
                                }
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            });
        }
    }
}
