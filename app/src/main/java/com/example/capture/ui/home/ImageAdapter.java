package com.example.capture.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.capture.DownloadImage.DownloadImageBitmap;
import com.example.capture.R;
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageAdapter extends  RecyclerView.Adapter<ImageAdapter.ImageAdapterHolder> {
    JSONArray jsonArray;
    Context mContext;

    public ImageAdapter(JSONArray jsonArray){
        this.jsonArray = jsonArray;
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
//            Glide.with(holder.mcontext).load(jsonObject1.getString("regular")).into(holder.imageView);
            Picasso.get().load(jsonObject1.getString("regular")).into(holder.imageView);
            Log.d("yd", jsonObject1.getString("regular"));
        } catch (JSONException e) {
            e.printStackTrace();
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
        public ImageAdapterHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewForItem);
            mcontext = itemView.getContext();
            downloadButton = itemView.findViewById(R.id.downloadButton);

            downloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject jsonObject = null;
                        jsonObject = (JSONObject) jsonArray.get(getAdapterPosition());
                        JSONObject jsonObject1 = jsonObject.getJSONObject("urls");
                        new DownloadImageBitmap(jsonObject1.getString("full"), jsonObject.getString("id"));
                    }catch (Exception e){

                    }
                }
            });
        }
    }
}
