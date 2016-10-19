package com.softtech.stevekamau.buyathome.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.adapter.CartAdapter;
import com.softtech.stevekamau.buyathome.databaseHandlers.CartDB;
import com.softtech.stevekamau.buyathome.interfaces.CartInterFace;
import com.softtech.stevekamau.buyathome.model.CartModel;

import java.text.DecimalFormat;
import java.util.List;

public class Cart extends AppCompatActivity implements CartInterFace {
    List<CartModel> itemList;
    CartDB cartDB;
    TextView sub_total, sub_total2, grand_total;
    CartDB cd;
    String s_amount;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("My Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cartDB = new CartDB(this);
        createViews();
    }

    private void createViews() {
        sub_total = (TextView) findViewById(R.id.sub_total);
        sub_total2 = (TextView) findViewById(R.id.sub_total2);
        grand_total = (TextView) findViewById(R.id.grand_total);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setColorNormal(getResources().getColor(R.color.accentColor));
        fab.setColorPressed(getResources().getColor(R.color.accentColor_pressed));
        fab.setColorRipple(getResources().getColor(R.color.accentColor_pressed));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PaymentAndShipping.class);
                intent.putExtra("total_amount", s_amount);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // EventBus.getDefault().register(this);
        itemList = cartDB.getAllCartItems();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cardList);
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        final CartAdapter itemAdapter = new CartAdapter(this, getApplicationContext(), itemList);
        recyclerView.setAdapter(itemAdapter);

        double amount = Double.parseDouble(String.valueOf(cartDB.getTotalOfAmount()));
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        if (cartDB.checkForTables()) {
            (findViewById(R.id.empty_cart)).setVisibility(View.INVISIBLE);
            (findViewById(R.id.scroll_content)).setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
            sub_total.setText("kshs. " + formatter.format(amount));
            sub_total2.setText("kshs. " + formatter.format(amount));
            grand_total.setText("kshs. " + formatter.format(amount));
            s_amount = String.valueOf(formatter.format(amount));
        } else {
            (findViewById(R.id.empty_cart)).setVisibility(View.VISIBLE);
            (findViewById(R.id.scroll_content)).setVisibility(View.INVISIBLE);
            FloatingActionButton mel_fab = (FloatingActionButton) findViewById(R.id.mel_fab);
            mel_fab.setColorNormal(getResources().getColor(R.color.accentColor));
            mel_fab.setColorPressed(getResources().getColor(R.color.accentColor_pressed));
            mel_fab.setColorRipple(getResources().getColor(R.color.accentColor_pressed));
            mel_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            fab.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onQuantityChanged(int position, String quantity, float amount, Integer id) {

        Double currentPrice = Double.valueOf(amount);
        final Double d_quantity = Double.valueOf(quantity);
        final Double calculated = d_quantity * currentPrice;

        cartDB.updateQuantity(id, quantity, calculated.toString());

        double total_amount = Double.parseDouble(String.valueOf(cartDB.getTotalOfAmount()));
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        sub_total.setText("kshs. " + formatter.format(total_amount));
        sub_total2.setText("kshs. " + formatter.format(total_amount));
        grand_total.setText("kshs. " + formatter.format(total_amount));
        s_amount = String.valueOf(formatter.format(total_amount));
        Log.d("On_cart_item_changed", id + " " + quantity + " " + calculated.toString() + " " + cartDB.getTotalOfAmount());
    }

    @Override
    public void onDeleted(Integer id) {

        cartDB.deleteSingleCartItem(id);
        double amount = Double.parseDouble(String.valueOf(cartDB.getTotalOfAmount()));
        DecimalFormat formatter = new DecimalFormat("#,###.00");

        if (cartDB.checkForTables()) {
            Log.d("On_cart_item_deleted","full");
            (findViewById(R.id.empty_cart)).setVisibility(View.INVISIBLE);
            (findViewById(R.id.scroll_content)).setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
            sub_total.setText("kshs. " + formatter.format(amount));
            sub_total2.setText("kshs. " + formatter.format(amount));
            grand_total.setText("kshs. " + formatter.format(amount));
            s_amount = String.valueOf(formatter.format(amount));
        } else {
            Log.d("On_cart_item_deleted","empty");
            (findViewById(R.id.empty_cart)).setVisibility(View.VISIBLE);
            (findViewById(R.id.scroll_content)).setVisibility(View.INVISIBLE);
            FloatingActionButton mel_fab = (FloatingActionButton) findViewById(R.id.mel_fab);
            mel_fab.setColorNormal(getResources().getColor(R.color.accentColor));
            mel_fab.setColorPressed(getResources().getColor(R.color.accentColor_pressed));
            mel_fab.setColorRipple(getResources().getColor(R.color.accentColor_pressed));
            mel_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            fab.setVisibility(View.INVISIBLE);
        }
    }
}
