package com.softtech.stevekamau.buyathome.activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.TopExceptionHandler;
import com.softtech.stevekamau.buyathome.adapter.CustomGridAdapter;
import com.softtech.stevekamau.buyathome.model.Model;

import java.util.ArrayList;
import java.util.List;


public class Phones extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "http://softtech.comuv.com/buyathome/phones.js";
    private static String Title = "title";
    private static String Rate = "rating";
    private static String Genre = "genre";
    private static String bitmap = "thumbnailUrl";
    private ProgressBar bar;
    private List<Model> modelList = new ArrayList<Model>();
    private CustomGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phones);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("Phones");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        phonesRequest();

    }

    private void phonesRequest() {
       /* InnerGridView gridView = (InnerGridView) findViewById(R.id.grid2);
        adapter = new CustomGridAdapter(this, modelList);
        gridView.setAdapter(adapter);

        bar = (ProgressBar) this.findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);
        // Creating volley request obj
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JsonArrayRequest productReq = new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());
                                bar.setVisibility(View.GONE);

                                // Parsing json
                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        JSONObject obj = response.getJSONObject(i);
                                        Model model = new Model();
                                        model.setTitle(obj.getString("title"));
                                        model.setThumbnailUrl(obj.getString("image"));
                                        model.setRating((int) ((Number) obj.get("rating"))
                                                .doubleValue());

                                        // Genre is json array
                                        JSONArray genreArry = obj.getJSONArray("genre");
                                        ArrayList<String> genre = new ArrayList<String>();
                                        for (int j = 0; j < genreArry.length(); j++) {
                                            genre.add((String) genreArry.get(j));
                                        }
                                        model.setGenre(genre);

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
                            bar.setVisibility(View.GONE);
                            Context context = getApplicationContext();
                        } else if (error instanceof AuthFailureError) {
                            if (!isFinishing()) {
                                serverDialog();
                            }
                            bar.setVisibility(View.GONE);
                            Context context = getApplicationContext();
                        } else if (error instanceof ServerError) {
                            if (!isFinishing()) {
                                serverDialog();
                            }
                            bar.setVisibility(View.GONE);
                            Context context = getApplicationContext();
                        } else if (error instanceof NetworkError) {
                            if (!isFinishing()) {
                                showAlertDialog();
                            }
                            bar.setVisibility(View.GONE);
                            Context context = getApplicationContext();
                        } else if (error instanceof ParseError) {
                            if (!isFinishing()) {
                                serverDialog();
                            }
                            bar.setVisibility(View.GONE);
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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView) view.findViewById(R.id.title))
                        .getText().toString();
                bitmap = ((Model) modelList.get(position)).getThumbnailUrl();
                String rate = ((TextView) view.findViewById(R.id.rating))
                        .getText().toString();
                String genres = ((TextView) view.findViewById(R.id.genre))
                        .getText().toString();

                Intent intent = new Intent(Phones.this, Details.class);
                intent.putExtra(Title, name);
                intent.putExtra("images", bitmap);
                intent.putExtra(Rate, rate);
                intent.putExtra(Genre, genres);

                startActivity(intent);
            }
        });*/
    }

    private void serverDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Phones.this);
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage("There was a problem completing your request. Please try again later.");

        // Setting Positive "Retry" Button
        alertDialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                phonesRequest();
            }
        });

        // Setting Negative "Quit" Button
        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Phones.this);
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage("Network error! Check your connection and retry.");

        // Setting Positive "Retry" Button
        alertDialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                phonesRequest();
            }
        });
        // Setting Negative "Quit" Button
        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_phones, menu);
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
        if (id == R.id.action_search) {
            Intent i = new Intent(Phones.this, SearchActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_refresh) {
            phonesRequest();
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
