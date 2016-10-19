package com.softtech.stevekamau.buyathome.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.adapter.WishListAdapter;
import com.softtech.stevekamau.buyathome.databaseHandlers.WishDB;
import com.softtech.stevekamau.buyathome.interfaces.CartInterFace;
import com.softtech.stevekamau.buyathome.model.CartModel;

import java.util.List;

public class WishList extends AppCompatActivity implements CartInterFace {
    List<CartModel> itemList;
    WishDB wishDB;

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
        final WishListAdapter itemAdapter = new WishListAdapter(this, getApplicationContext(), itemList);
        recyclerView.setAdapter(itemAdapter);

        if (wishDB.checkForTables()) {
            (findViewById(R.id.empty_wishlist)).setVisibility(View.INVISIBLE);
        } else {
            (findViewById(R.id.empty_wishlist)).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onQuantityChanged(int position, String quantity, float amount, Integer id) {

    }

    @Override
    public void onDeleted(Integer id) {
        if (wishDB.checkForTables()) {
            (findViewById(R.id.empty_wishlist)).setVisibility(View.INVISIBLE);
        } else {
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

}
