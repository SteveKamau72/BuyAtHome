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
import com.softtech.stevekamau.buyathome.adapter.NewGridAdapter;
import com.softtech.stevekamau.buyathome.model.Model;

import java.util.ArrayList;
import java.util.List;


public class LatestPhones extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "http://softtech.comuv.com/buyathome/new_phones.js";
    private static String Title = "title";
    private static String Rate = "rating";
    private static String Genre = "genre";
    private static String bitmap = "thumbnailUrl";
    private ProgressBar bar;
    private List<Model> modelList = new ArrayList<Model>();
    private NewGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_rated);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("Latest Phones");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        requestLaptops();

    }

    private void requestLaptops() {

    }

    private void serverDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LatestPhones.this);
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage("There was a problem completing your request. Please try again later.");

        // Setting Positive "Retry" Button
        alertDialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestLaptops();
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LatestPhones.this);
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage("Network error! Check your connection and retry.");

        // Setting Positive "Retry" Button
        alertDialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestLaptops();
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
        getMenuInflater().inflate(R.menu.menu_latest_phones, menu);
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
            Intent i = new Intent(LatestPhones.this, SearchActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_refresh) {
            requestLaptops();
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
