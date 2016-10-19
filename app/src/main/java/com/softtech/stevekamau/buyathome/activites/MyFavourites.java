package com.softtech.stevekamau.buyathome.activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.TopExceptionHandler;
import com.softtech.stevekamau.buyathome.databaseHandlers.CartDB;
import com.softtech.stevekamau.buyathome.databaseHandlers.WishDB;


public class MyFavourites extends ActionBarActivity {
    ListView obj;
    WishDB favDB;
    int numRows;
    int id_To_Update = 0;
    TextView title;
    TextView amount;
    TextView description;
    TextView tvSlNo;
    TextView tvName;
    TextView tvPhone;
    private CartDB mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourites);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("My WishList");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        favDB = new WishDB(this);
        mydb = new CartDB(this);
        obj = (ListView) findViewById(R.id.listView1);

        tvSlNo = (TextView) findViewById(R.id.tv_slno);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPhone = (TextView) findViewById(R.id.tv_phone);

        if (favDB.checkForTables()) {
            showTable();

        } else {
            showAlert();

        }

        TextView txt = (TextView) findViewById(R.id.numRows);
        int profile_counts = favDB.numberOfRows();
        favDB.close();
        txt.setText(String.valueOf(profile_counts));
        TextView txt2 = (TextView) findViewById(R.id.Amount_textView);
        int i = favDB.getTotalOfAmount();
        txt2.setText("" + i);
    }

    public void showAlert() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.fav_empty_cart, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(layout);
        adb.setCancelable(false);

        adb.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MyFavourites.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        adb.show();
    }

    private void showTable() {
        // ArrayList<CartModel> contactList = favDB.getAllContacts();

        //  final CustomListAdapterFav contactListAdapter = new CustomListAdapterFav(this, contactList, MyFavourites.this);
        //adding it to the list view.
        obj = (ListView) findViewById(R.id.listView1);
        // obj.setAdapter(contactListAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = ((TextView) view.findViewById(R.id.tv_slno))
                        .getText().toString();
                String description = ((TextView) view.findViewById(R.id.tv_phone))
                        .getText().toString();
                String amount = ((TextView) view.findViewById(R.id.tv_name))
                        .getText().toString();
                addToCart(title, amount, description);
            }
        });
    }

    private void addToCart(final String title, final String amount, final String description) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyFavourites.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Add this to cart?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                float amountValue = Float.parseFloat(amount);

                // mydb.insertIntoCart(title, amountValue, description);
                Toast.makeText(getApplicationContext(), "Item successfully added to Cart", Toast.LENGTH_LONG).show();

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_cart, menu);
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
        if (id == R.id.action_delete) {
            //favDB.deleteContact();
            Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyFavourites.this, MyFavourites.class);
            startActivity(intent);
            return true;

        }
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}