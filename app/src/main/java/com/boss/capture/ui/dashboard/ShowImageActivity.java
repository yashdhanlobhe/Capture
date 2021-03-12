package com.boss.capture.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.boss.capture.R;
import com.boss.capture.ui.home.ImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.boss.capture.QueryClass.getQueryForSearchingKeyword;

public class ShowImageActivity extends AppCompatActivity {
    String find;
    ImageAdapter imageAdapter;
    RecyclerView recyclerView;
    String url;
    Context mContext;
    RequestQueue queue;
    int currentPageNumber = 1 , TotalPages;
    TextView currentPage , subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        mContext=this;
        currentPage = findViewById(R.id.currentPageIDDash);
        subject = findViewById(R.id.PhotoSubject);
        queue= Volley.newRequestQueue(mContext);
        find = getIntent().getStringExtra("search");
        subject.setText(find);
        recyclerView= findViewById(R.id.recyclerViewDashboard);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        initRecyclerView();

        findViewById(R.id.idNextButtonDash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPageNumber +=1;
                if (currentPageNumber > TotalPages ){
                    currentPageNumber = TotalPages;
                }
                initRecyclerView();
            }
        });
       findViewById(R.id.idBackButtonDash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPageNumber -=1;
                if (currentPageNumber<1) {
                    currentPageNumber = 1;
                }
                initRecyclerView();
            }
        });
        findViewById(R.id.imageActivityBackBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initRecyclerView(){
        url= getQueryForSearchingKeyword(find , String.valueOf(currentPageNumber));
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
                    TotalPages = Integer.parseInt(response.getString("total_pages"));
                    imageAdapter = new ImageAdapter(jsonArray );
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
        findViewById(R.id.pbDashboard).setVisibility(View.INVISIBLE);
        currentPage.setText(currentPageNumber+ " - " + TotalPages);
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left , R.anim.slide_out_to_right);
    }
}