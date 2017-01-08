package com.softtech.stevekamau.buyathome.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.adapter.WishListAdapter;
import com.softtech.stevekamau.buyathome.databaseHandlers.CartDB;
import com.softtech.stevekamau.buyathome.databaseHandlers.WishDB;
import com.softtech.stevekamau.buyathome.interfaces.CartInterFace;
import com.softtech.stevekamau.buyathome.interfaces.OnOptionsSelectedInterface;
import com.softtech.stevekamau.buyathome.model.CartModel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class WishList extends AppCompatActivity implements CartInterFace, OnOptionsSelectedInterface {
    List<CartModel> itemList;
    WishDB wishDB;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("My WishList");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wishDB = new WishDB(this);
        createViews();
    }

    private void createViews() {
        itemList = wishDB.getAllWishListItems();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cardList);
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        final WishListAdapter itemAdapter = new WishListAdapter(this, this, WishList.this, itemList);
        recyclerView.setAdapter(itemAdapter);

        if (wishDB.checkForTables()) {
            (findViewById(R.id.empty_wishlist)).setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            (findViewById(R.id.empty_wishlist)).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        fab = (FloatingActionButton) findViewById(R.id.mel_fab);
        fab.setColorNormal(getResources().getColor(R.color.accentColor));
        fab.setColorPressed(getResources().getColor(R.color.accentColor_pressed));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onQuantityChanged(int position, String quantity, float amount, Integer id) {

    }

    @Override
    public void onDeleted(Integer id) {

        if (wishDB.checkForTables()) {
            (findViewById(R.id.empty_wishlist)).setVisibility(View.INVISIBLE);
            Log.d("wishlist_check_tables", "exist");
        } else {
            Log.d("wishlist_check_tables", "doesnt exist");
            (findViewById(R.id.empty_wishlist)).setVisibility(View.VISIBLE);
        }
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
            // NavUtils.navigateUpFromSameTask(this);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShareButtonclicked(String name, String amount) {
        final String appPackageName = getPackageName();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey, check out " + name + " at BuyAtHome app for Android at only Kshs." + amount
                        + "\nDownload the app for more amazing deals: "
                        + "https://play.google.com/store/apps/details?id=" + appPackageName);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BuyAtHome App for Android");
        startActivity(Intent.createChooser(shareIntent, "share..."));
    }

    @Override
    public void onAddToCartButtonClicked(int id, String name, String amount, String details, String encodedImageData, String s, String mAmount) {
        CartDB cartDB = new CartDB(this);
        cartDB.insertIntoCart(String.valueOf(id), name, amount, details, encodedImageData, "1", amount);

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Successful!")
                .setContentText("Item added item to wishlist!")
                .show();
    }

    @Override
    public void onAddToWishList(int id, String name, String amount, String details, String encodedImageData, String s, String mAmount) {

    }

    @Override
    public void onDeleteItem() {
        Snackbar.make(fab, "Item successfully removed.", Snackbar.LENGTH_SHORT).show();

    }
}
