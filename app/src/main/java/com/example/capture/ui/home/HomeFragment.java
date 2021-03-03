package com.example.capture.ui.home;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.capture.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    RequestQueue queue;
    String url;
    EditText search;
    String find;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        queue = Volley.newRequestQueue(container.getContext());
        recyclerView= root.findViewById(R.id.recyclerViewHome);
        search = root.findViewById(R.id.searchBoxHome);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        initRecyclerView();


        root.findViewById(R.id.buttonForHomeSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRecyclerView();
            }
        });
        return root;
    }
    private void initRecyclerView(){
        find = search.getText().toString();
        if (find.equals("")){
            find = "cartoon";
        }
        url= "https://api.unsplash.com/search/photos?page=1&query="+find+"&client_id=z09b28_UcCHK5ULO8pE0jnBCYtErAnhR9Y_l3BkCKOI";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = response.getJSONArray("results");
                    Log.d("yd" , jsonArray.toString());
                    ImageAdapter imageAdapter = new ImageAdapter(jsonArray);
                    recyclerView.setAdapter(imageAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(objectRequest);
    }
}