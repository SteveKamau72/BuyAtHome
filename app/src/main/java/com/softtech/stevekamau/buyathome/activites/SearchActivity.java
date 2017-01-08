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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
    TextView tv_empty_text;
    String data, p_name, img_url, p_amount, p_tags, p_details;
    int p_id, p_rating;
    ListView lv;
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
        tv_empty_text = (TextView) findViewById(R.id.empty_text);
        lv = (ListView) findViewById(R.id.list);
        MyTextWatcher textWatcher = new MyTextWatcher(search_view);
        search_view.addTextChangedListener(textWatcher);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Details.class);
                intent.putExtra("p_id", modelList.get(i).getId());
                intent.putExtra("title", modelList.get(i).getName());
                intent.putExtra("amount", modelList.get(i).getAmount());
                intent.putExtra("description", modelList.get(i).getDetails());
                intent.putExtra("image1_url", modelList.get(i).getImage_url());
                // TODO: 12/6/16 this is totally dummy data
                intent.putExtra("rating", modelList.get(i).getRatings());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

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
            modelList.clear();
            SharedPreferences sharedPreferences = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
            String json = sharedPreferences.getString("product_list", "");
            Log.d("json_str", json);
            if (!s.equals("")) {
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

                                modelList.add(0, model);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    Log.d("jsearch", s.toString() + "==" + String.valueOf(modelList.size()));
                    adapter = new SearchListAdapter(SearchActivity.this, modelList);
                    lv.setAdapter(adapter);
                    if (modelList.size() == 0) {
                        lv.setVisibility(View.INVISIBLE);
                        tv_empty_text.setText("Tip: Use keywords to find items easily");
                        tv_empty_text.setVisibility(View.VISIBLE);
                    } else {
                        lv.setVisibility(View.VISIBLE);
                        tv_empty_text.setVisibility(View.INVISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                lv.setVisibility(View.INVISIBLE);
                tv_empty_text.setText("Tip: Use keywords to find items easily");
                tv_empty_text.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}