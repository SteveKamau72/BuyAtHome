package com.softtech.stevekamau.buyathome.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.TopExceptionHandler;
import com.softtech.stevekamau.buyathome.adapter.SearchListAdapter;
import com.softtech.stevekamau.buyathome.helper.UrlFormatter;
import com.softtech.stevekamau.buyathome.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends ActionBarActivity {
    EditText search_view;
    String data, p_name, img_url, p_amount, p_tags, p_details;
    int p_id, p_rating;
    private ProgressDialog pDialog;
    private List<Model> modelList = new ArrayList<Model>();
    private SearchListAdapter adapter;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        search_view = (EditText) findViewById(R.id.search_view);

        MyTextWatcher textWatcher = new MyTextWatcher(search_view);
        search_view.addTextChangedListener(textWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }
/*
    private void allRequest() {


        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Please wait...");
        pDialog.show();
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        // Creating volley request obj
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JsonArrayRequest productReq = new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());
                                pDialog.dismiss();

                                // Parsing json
                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        JSONObject obj = response.getJSONObject(i);
                                        Model model = new Model();
                                       *//* model.setTitle(obj.getString("title"));
                                        model.setThumbnailUrl(obj.getString("image"));
                                        model.setRating((int) ((Number) obj.get("rating"))
                                                .doubleValue());

                                        // Genre is json array
                                        JSONArray genreArry = obj.getJSONArray("genre");
                                        ArrayList<String> genre = new ArrayList<String>();
                                        for (int j = 0; j < genreArry.length(); j++) {
                                            genre.add((String) genreArry.get(j));
                                        }
                                        model.setGenre(genre);*//*

                                        // adding model to model array
                                        modelList.add(model);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            if (!isFinishing()) {
                                showAlertDialog();
                            }
                            pDialog.dismiss();
                            Context context = getApplicationContext();
                        } else if (error instanceof AuthFailureError) {
                            if (!isFinishing()) {
                                serverDialog();
                            }
                            pDialog.dismiss();
                            Context context = getApplicationContext();
                        } else if (error instanceof ServerError) {
                            if (!isFinishing()) {
                                serverDialog();
                            }
                            pDialog.dismiss();
                            Context context = getApplicationContext();
                        } else if (error instanceof NetworkError) {
                            if (!isFinishing()) {
                                showAlertDialog();
                            }
                            pDialog.dismiss();
                            Context context = getApplicationContext();
                        } else if (error instanceof ParseError) {
                            if (!isFinishing()) {
                                serverDialog();
                            }
                            pDialog.dismiss();
                            Context context = getApplicationContext();
                        }

                    }

                    ;
                });


                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(productReq);
            }
        });
        thread.start();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Model model = (Model) adapter.getItem(position);

                String name = ((TextView) view.findViewById(R.id.title)).getText().toString();
                String rate = ((TextView) view.findViewById(R.id.rating)).getText().toString();
                String genres = ((TextView) view.findViewById(R.id.genre)).getText().toString();

                Intent intent = new Intent(SearchActivity.this, Details.class);
                intent.putExtra(Title, name);
               *//* intent.putExtra("images", model.getThumbnailUrl());*//*
                intent.putExtra(Rate, rate);
                intent.putExtra(Genre, genres);

                startActivity(intent);
            }
        });
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsActivity = new Intent(getBaseContext(),
                    Preferences.class);
            startActivity(settingsActivity);
        }
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public class MyTextWatcher implements TextWatcher {
        private EditText editText;

        public MyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            modelList.clear();
            SharedPreferences sharedPreferences = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
            String json = sharedPreferences.getString("product_list", "");
            Log.d("json_str", json);
            if (!json.equals("")) {
                try {
                    JSONArray jsonArray = new JSONArray(json);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {

                            JSONObject obj = jsonArray.getJSONObject(i);
                            p_id = obj.getInt("p_id");
                            p_name = obj.getString("p_name");
                            p_amount = obj.getString("p_amount");
                            p_details = obj.getString("p_details");
                            p_tags = obj.getString("tags");
                            p_rating = obj.getInt("p_rating");

                            UrlFormatter urlFormatter = new UrlFormatter();
                            img_url = urlFormatter.unescapeJavaString((obj.getString("image_url")));
                            Model model = new Model();

                            model.setId(p_id);
                            model.setName(p_name);
                            model.setImage_url(img_url);
                            model.setAmount(p_amount);
                            model.setTags(p_tags);
                            model.setDetails(p_details);
                            model.setRatings(p_rating);
                            if (p_name.contains(s.toString())) {

                                modelList.add(0,model);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    ListView lv = (ListView) findViewById(R.id.list);
                    adapter = new SearchListAdapter(SearchActivity.this, modelList);
                    lv.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}