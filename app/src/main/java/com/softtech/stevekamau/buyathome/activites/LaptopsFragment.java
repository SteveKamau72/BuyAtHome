package com.softtech.stevekamau.buyathome.activites;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

/**
 * Created by steve on 7/18/16.
 */
public class LaptopsFragment extends Fragment {

    View view;
    String p_name, img_url, p_amount, p_tags, p_details;
    int p_id, p_rating;
    CircleProgressBar progressBar;
    InnerGridView ig;
    Activity activity;
    private List<PhoneModel> modelList = new ArrayList<PhoneModel>();

    public LaptopsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_phones, container, false);
        progressBar = (CircleProgressBar) view.findViewById(R.id.recycler_progressBar);
        ig = (InnerGridView) view.findViewById(R.id.grid);
        activity = getActivity();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ACCOUNT", getActivity().MODE_PRIVATE);
        String json = sharedPreferences.getString("product_list", "");
        Log.d("json_str", json);
        if (!json.equals("")) {
            loadLaptops(json);
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
        return view;
    }

    private void loadLaptops(String data) {
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

                    if (p_tags.contains("laptop")) {

                        modelList.add(model);
                    }
                    /*if (p_tags.contains("featured_product")) {
                        modelList.add(model);
                    }
                    if (p_tags.contains("recommended_product")) {
                        modelList.add(model);
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
           /* if (modelList.size() == 0) {
                (view.findViewById(R.id.no_products)).setVisibility(View.INVISIBLE);
            } else {
                (view.findViewById(R.id.no_products)).setVisibility(View.VISIBLE);
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PhoneGridAdapter customGridAdapter = new PhoneGridAdapter(activity, modelList);
        ig.setAdapter(customGridAdapter);

    }
}
