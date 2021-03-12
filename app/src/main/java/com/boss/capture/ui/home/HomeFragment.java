package com.boss.capture.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.boss.capture.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.boss.capture.QueryClass.getQueryForSearchingKeyword;

public class HomeFragment extends Fragment {
    public  static String find="travel";
    public static  String url  ;
    public static ImageAdapter imageAdapter;
    public static  int currentPageNumber = 1 , TotalPages;
    RecyclerView recyclerView;
    TextView currentPage , instruction;
    EditText search;
    Context mContext;
    RequestQueue queue;
    ProgressDialog pd;
    View linearLayoutHomeFrag;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView= root.findViewById(R.id.recyclerViewHome);
        search = root.findViewById(R.id.searchBoxHome);
        currentPage = root.findViewById(R.id.currentPageID);
        instruction = root.findViewById(R.id.instructionTextview);
        mContext = container.getContext();
        pd = new ProgressDialog(mContext);
        pd.setMessage("Loading...");
        queue= Volley.newRequestQueue(mContext);
        linearLayoutHomeFrag = root.findViewById(R.id.linearLayoutHomeFrag);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        search.setOnEditorActionListener((v, actionId, event) -> {
            Boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                currentPageNumber = 1;
                search.clearFocus();
                InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(search.getWindowToken(), 0);
                pd.show();
                find = search.getText().toString();
                initRecyclerView(root);
                handled = true;
            }
            return handled;
        });

        root.findViewById(R.id.idNextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                currentPageNumber +=1;
                if (currentPageNumber > TotalPages ){
                    currentPageNumber = TotalPages;
                }
                initRecyclerView(root);
            }
        });
        root.findViewById(R.id.idBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                currentPageNumber -=1;
                if (currentPageNumber<1) {
                    currentPageNumber = 1;
                }

                initRecyclerView(root);
            }
        });
        return root;
    }
    private void initRecyclerView(View root){
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
                JSONArray jsonArray;
                try {
                    if (response.getString("total").equals("0")){
                        instruction.setText("Not Found :(");
                        linearLayoutHomeFrag.setVisibility(View.INVISIBLE);
                    }else {
                        linearLayoutHomeFrag.setVisibility(View.VISIBLE);
                    }
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
        pd.dismiss();
        currentPage.setText(currentPageNumber+ " - " + TotalPages);
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
        if (imageAdapter != null ){
            linearLayoutHomeFrag.setVisibility(View.VISIBLE);
        }
    }
}