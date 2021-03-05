package com.example.capture.ui.local;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.VerifiedInputEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capture.R;
import com.example.capture.Services.GetStorageFileNames;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class LocalRecyclerViewAdapter extends  RecyclerView.Adapter<LocalRecyclerViewAdapter.LocalRecyclerViewHolder> {
    ArrayList<String> files;

    public LocalRecyclerViewAdapter(){
        files = GetStorageFileNames.DownloadedFilesName;
    }
    @NonNull
    @Override
    public LocalRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_recycler_view_holder , parent , false);
        return new LocalRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalRecyclerViewHolder holder, int position) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Capture/" + files.get(position));
        Log.d("yd" , file.toString());
        Picasso.get().load(file).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public  class  LocalRecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView ;
        public LocalRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.localItemViewHolder);
        }
    }
}
