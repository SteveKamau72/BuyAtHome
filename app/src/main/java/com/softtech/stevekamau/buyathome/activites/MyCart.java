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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.softtech.stevekamau.buyathome.model.CartModel;
import com.softtech.stevekamau.buyathome.databaseHandlers.CartDB;
import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.TopExceptionHandler;
import com.softtech.stevekamau.buyathome.adapter.ContactListAdapter;

import java.util.ArrayList;


public class MyCart extends ActionBarActivity {
    ListView obj;
    CartDB myDb;
    int numRows;
    int id_To_Update = 0;

    public static void setListViewHeightBasedOnChildren(ListView obj) {
        ContactListAdapter contactListAdapter = (ContactListAdapter) obj.getAdapter();
        if (contactListAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(obj.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < contactListAdapter.getCount(); i++) {
            view = contactListAdapter.getView(i, view, obj);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = obj.getLayoutParams();
        params.height = totalHeight + (obj.getDividerHeight() * (contactListAdapter.getCount() - 1));
        obj.setLayoutParams(params);
        obj.requestLayout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("My Cart");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myDb = new CartDB(this);
        obj = (ListView) findViewById(R.id.listView1);
        Button btn = (Button) findViewById(R.id.checkout);
        if (myDb.checkForTables()) {
            showTable();
            btn.setVisibility(View.VISIBLE);
        } else {
            showAlert();
            btn.setVisibility(View.GONE);
        }

        TextView txt = (TextView) findViewById(R.id.numRows);
        int profile_counts = myDb.numberOfRows();
        myDb.close();
        txt.setText(String.valueOf(profile_counts));
        TextView txt2 = (TextView) findViewById(R.id.Amount_textView);
        int i = myDb.getTotalOfAmount();
        txt2.setText("" + i);

        Button basketButton = (Button) findViewById(R.id.checkout);
        basketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyCart.this, Checkout.class);
                startActivity(i);
            }
        });
    }

    public void showAlert() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.empty_cart, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(layout);
        adb.setCancelable(false);

        adb.setPositiveButton("Add items to Cart", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MyCart.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        adb.show();
    }

    private void showTable() {
        final ArrayList<CartModel> contactList = myDb.getAllCartItems();

        final ContactListAdapter contactListAdapter = new ContactListAdapter(
                this, contactList, MyCart.this);
        //adding it to the list view.
        obj = (ListView) findViewById(R.id.listView1);
        obj.setAdapter(contactListAdapter);
        setListViewHeightBasedOnChildren(obj);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyCart.this);
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Delete item?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String title;
                        title = contactList.get(position).getTitle();
                       // myDb.removeSingleContact(title);
                        contactListAdapter.notifyDataSetChanged();

                        Intent intent = new Intent(getApplicationContext(), MyCart.class);
                        startActivity(intent);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();

            }

        });
    }

    private void deleteDialog() {
        final String title = "";
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyCart.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Delete item?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

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
            myDb.deleteCart();
            Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyCart.this, MyCart.class);
            startActivity(intent);
            finish();
            return true;

        }

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}