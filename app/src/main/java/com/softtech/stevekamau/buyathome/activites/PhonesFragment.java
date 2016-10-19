package com.softtech.stevekamau.buyathome.activites;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.softtech.stevekamau.buyathome.InnerGridView;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.adapter.PhoneGridAdapter;
import com.softtech.stevekamau.buyathome.helper.UrlFormatter;
import com.softtech.stevekamau.buyathome.model.PhoneModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.library.MDDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhonesFragment extends Fragment {

    View view;
    String p_name, img_url, p_amount, p_tags, p_details, json;
    int p_id, p_rating;
    CircleProgressBar progressBar;
    InnerGridView ig;
    Activity activity;
    String[] prices;
    int range1, range2;
    private List<PhoneModel> modelList = new ArrayList<PhoneModel>();

    public PhonesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_phones, container, false);
        prices = new String[]{"None", "Below 5000", "5000 - 10,000", "10,000 - 20,000", "Above 20,000"};

        progressBar = (CircleProgressBar) view.findViewById(R.id.recycler_progressBar);
        ig = (InnerGridView) view.findViewById(R.id.grid);
        activity = getActivity();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ACCOUNT", getActivity().MODE_PRIVATE);
        json = sharedPreferences.getString("product_list", "");
        Log.d("json_str", json);
        if (!json.equals("")) {
            loadPhones(json);
        }

        ig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(activity, Details.class);

                intent.putExtra("p_id", modelList.get(position).getId());
                intent.putExtra("title", modelList.get(position).getName());
                intent.putExtra("amount", modelList.get(position).getAmount());
                intent.putExtra("description", modelList.get(position).getDetails());
                intent.putExtra("image1_url", modelList.get(position).getImage_url());
                startActivity(intent);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSortOption();
            }
        });
        return view;
    }

    private void showSortOption() {
        new MDDialog.Builder(getActivity())
                .setMessages(prices)
                .setTitle("Sort by prices")
                .setShowTitle(true)
                .setShowButtons(false)
                .setOnItemClickListener(new MDDialog.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int index) {
                        //Toast.makeText(getActivity(), prices[index], Toast.LENGTH_SHORT).show();
                        // switch ()
                        if (index == 0) {
                            loadPhones(json);
                        } else if (index == 1) {
                            range1 = 0;
                            range2 = 5000;
                            sortPhones(range1, range2);
                            // filter_amount
                        } else if (index == 2) {
                            range1 = 5000;
                            range2 = 10000;
                            sortPhones(range1, range2);
                        } else if (index == 3) {
                            range1 = 10000;
                            range2 = 20000;
                            sortPhones(range1, range2);
                        } else if (index == 4) {
                            range1 = 20000;
                            range2 = 10000000;
                            sortPhones(range1, range2);
                        }

                    }
                })
                .setWidthMaxDp(600)
                .create()
                .show();
    }

    private void sortPhones(int range1, int range2) {
        modelList.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                try {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    /// JSONObject obj = new JSONObject(data);
                    //JSONObject obj = data.getJSONObject(i);
                    p_id = obj.getInt("p_id");
                    p_name = obj.getString("p_name");
                    p_amount = obj.getString("p_amount");
                    p_details = obj.getString("p_details");
                    p_tags = obj.getString("tags");
                    p_rating = obj.getInt("p_rating");

                    UrlFormatter urlFormatter = new UrlFormatter();
                    img_url = urlFormatter.unescapeJavaString((obj.getString("image_url")));
                    PhoneModel model = new PhoneModel();

                    model.setId(p_id);
                    model.setName(p_name);
                    model.setImage_url(img_url);
                    model.setAmount(p_amount);
                    model.setTags(p_tags);
                    model.setDetails(p_details);
                    model.setRatings(p_rating);
                    int numberToSort = Integer.parseInt(p_amount);
                    if (numberToSort > range1 && numberToSort < range2) {
                        modelList.add(model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (modelList.size() == 0) {
                (view.findViewById(R.id.no_products)).setVisibility(View.VISIBLE);
            } else {
                (view.findViewById(R.id.no_products)).setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PhoneGridAdapter customGridAdapter = new PhoneGridAdapter(activity, modelList);
        ig.setAdapter(customGridAdapter);

    }

    private void loadPhones(String data) {
        modelList.clear();
        try {
            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {
                try {

                    JSONObject obj = jsonArray.getJSONObject(i);
                    /// JSONObject obj = new JSONObject(data);
                    //JSONObject obj = data.getJSONObject(i);
                    p_id = obj.getInt("p_id");
                    p_name = obj.getString("p_name");
                    p_amount = obj.getString("p_amount");
                    p_details = obj.getString("p_details");
                    p_tags = obj.getString("tags");
                    p_rating = obj.getInt("p_rating");

                    UrlFormatter urlFormatter = new UrlFormatter();
                    img_url = urlFormatter.unescapeJavaString((obj.getString("image_url")));
                    PhoneModel model = new PhoneModel();

                    model.setId(p_id);
                    model.setName(p_name);
                    model.setImage_url(img_url);
                    model.setAmount(p_amount);
                    model.setTags(p_tags);
                    model.setDetails(p_details);
                    model.setRatings(p_rating);

                    if (p_tags.contains("phone")) {

                        modelList.add(model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (modelList.size() == 0) {
                (view.findViewById(R.id.no_products)).setVisibility(View.VISIBLE);
            } else {
                (view.findViewById(R.id.no_products)).setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PhoneGridAdapter customGridAdapter = new PhoneGridAdapter(activity, modelList);
        ig.setAdapter(customGridAdapter);

    }
}