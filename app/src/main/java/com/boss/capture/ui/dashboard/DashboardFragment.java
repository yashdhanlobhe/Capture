package com.boss.capture.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.boss.capture.R;

public class DashboardFragment extends Fragment implements View.OnClickListener {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        root.findViewById(R.id.MountainCardView).setOnClickListener(this);
        root.findViewById(R.id.TechCardview).setOnClickListener(this);
        root.findViewById(R.id.TravelCardView).setOnClickListener(this);
        root.findViewById(R.id.oceansCardView).setOnClickListener(this);
        root.findViewById(R.id.StarsCardView).setOnClickListener(this);
        root.findViewById(R.id.historyCardView).setOnClickListener(this);
        root.findViewById(R.id.FoodCardView).setOnClickListener(this);
        root.findViewById(R.id.cityCardView).setOnClickListener(this);
        root.findViewById(R.id.SpiritualityCardView).setOnClickListener(this);
        root.findViewById(R.id.animalsCardView).setOnClickListener(this);
        root.findViewById(R.id.architectureCardView).setOnClickListener(this);
        root.findViewById(R.id.FashionCardView).setOnClickListener(this);
        root.findViewById(R.id.carsCardView).setOnClickListener(this);
        root.findViewById(R.id.sportCardView).setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        String search= "tiger";
        switch (v.getId()){
            case R.id.MountainCardView:
                search = "mountains";
                break;
            case R.id.TechCardview:
                search = "technology";
                break;
            case R.id.TravelCardView:
                search = "travel";
                break;
            case R.id.oceansCardView:
                search = "ocean";
                break;
            case R.id.StarsCardView:
                search = "stars";
                break;
            case R.id.historyCardView:
                search = "history";
                break;
            case R.id.FoodCardView:
                search = "food";
                break;
            case R.id.cityCardView:
                search = "city";
                break;
            case R.id.SpiritualityCardView:
                search = "Spirituality";
                break;
            case R.id.animalsCardView:
                search = "animal";
                break;
            case R.id.architectureCardView:
                search = "architecture";
                break;
            case R.id.FashionCardView:
                search = "fashion";
                break;
            case R.id.carsCardView:
                search = "car";
                break;
            case R.id.sportCardView:
                search = "sport";
                break;
            default:
                break;
        }
        Log.d("yd" , search);
        startActivity(new Intent(v.getContext(), ShowImageActivity.class).putExtra("search" , search));
        getActivity().overridePendingTransition(R.anim.slide_in_from_right , R.anim.slide_out_to_left);
    }
}