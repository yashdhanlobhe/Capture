package com.example.capture.ui.local;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capture.R;
import com.example.capture.Services.GetStorageFileNames;

public class LocalFragment extends Fragment {
    RecyclerView  recyclerView ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_local, container, false);
        GetStorageFileNames.GetDownloadedFilesNames();
        recyclerView= root.findViewById(R.id.localRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        LocalRecyclerViewAdapter localRecyclerViewAdapter = new LocalRecyclerViewAdapter();
        recyclerView.setAdapter(localRecyclerViewAdapter);
        return root;
    }
}