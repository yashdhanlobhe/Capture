package com.example.capture.ui.home;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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

import static com.example.capture.GetJSONArray.setRecyclerView;
import static com.example.capture.QueryClass.getQueryForSearchingKeyword;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    public  static String find="random";
    public static  String url  ;
    public static ImageAdapter imageAdapter;
    EditText search;
    Context mContext;
    RequestQueue queue;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView= root.findViewById(R.id.recyclerViewHome);
        search = root.findViewById(R.id.searchBoxHome);
        mContext = container.getContext();
        queue= Volley.newRequestQueue(mContext);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

//        initRecyclerView();
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    find = search.getText().toString();
                    initRecyclerView();
                    handled = true;
                }
                return false;
            }
        });

        return root;
    }
    private void initRecyclerView(){
        url= getQueryForSearchingKeyword(find , "1");
        setRecyclerView(recyclerView , url , mContext);
    }
    private void setRecyclerView(RecyclerView recyclerView , String query , Context context){
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, query, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = response.getJSONArray("results");
                    imageAdapter = new ImageAdapter(jsonArray);
                    setView();
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

    private void setView(){
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }
}