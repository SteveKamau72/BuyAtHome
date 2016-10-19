package com.softtech.stevekamau.buyathome.activites;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softtech.stevekamau.buyathome.databaseHandlers.CartDB;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.adapter.CustomGridAdapter;
import com.softtech.stevekamau.buyathome.adapter.FeaturedGridAdapter;
import com.softtech.stevekamau.buyathome.adapter.NewGridAdapter;
import com.softtech.stevekamau.buyathome.adapter.RecomGridAdapter;
import com.softtech.stevekamau.buyathome.model.NewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View view;
    String p_name, img_url, p_amount, p_tags, p_details;
    int p_id, p_rating;
    NewGridAdapter ad;
    CustomGridAdapter adapter;
    String data;
    Activity activity;
    RecomGridAdapter ra;
    FeaturedGridAdapter feat_adapter;
    CartDB myDb;
    private List<NewModel> modelList = new ArrayList<NewModel>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

}
