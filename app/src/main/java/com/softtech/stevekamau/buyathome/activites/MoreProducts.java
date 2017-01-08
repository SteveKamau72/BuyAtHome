package com.softtech.stevekamau.buyathome.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.adapter.CustomGridAdapter;
import com.softtech.stevekamau.buyathome.adapter.MoreHotDealsAdapter;
import com.softtech.stevekamau.buyathome.databaseHandlers.CartDB;
import com.softtech.stevekamau.buyathome.databaseHandlers.WishDB;
import com.softtech.stevekamau.buyathome.helper.BadgeDrawable;
import com.softtech.stevekamau.buyathome.helper.UrlFormatter;
import com.softtech.stevekamau.buyathome.interfaces.OnOptionsSelectedInterface;
import com.softtech.stevekamau.buyathome.interfaces.UpdateCartCount;
import com.softtech.stevekamau.buyathome.model.HotDealsModel;
import com.softtech.stevekamau.buyathome.model.NewModel;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.library.MDDialog;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MoreProducts extends AppCompatActivity implements OnOptionsSelectedInterface {

    CustomGridAdapter adapter;
    String data, p_name, img_url, p_amount, p_tags, p_details;
    int p_id, p_rating;
    String title, json;
    String[] prices;
    MenuItem itemCart;
    LayerDrawable icon;
    int range1, range2;
    CartDB cartDB = new CartDB(this);
    String count;
    private List<NewModel> modelList = new ArrayList<NewModel>();
    private List<HotDealsModel> hotDealsModelList = new ArrayList<HotDealsModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        title = i.getStringExtra("title");
        if (title.equals("Hot Deals")) {
            setContentView(R.layout.activity_more_hot_deals);
        } else {
            setContentView(R.layout.activity_new_products);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prices = new String[]{"None", "Below 5000", "5000 - 10,000", "10,000 - 20,000", "Above 20,000"};


        SharedPreferences sharedPreferences = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        json = sharedPreferences.getString("product_list", "");
        Log.d("json_str", json);
        if (!json.equals("")) {
            loadData(json);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSortOption();
            }
        });
    }

    private void showSortOption() {
        new MDDialog.Builder(this)
                .setMessages(prices)
                .setTitle("Sort by prices")
                .setShowTitle(true)
                .setShowButtons(false)
                .setOnItemClickListener(new MDDialog.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int index) {
                        //Toast.makeText(getActivity(), prices[index], Toast.LENGTH_SHORT).show();
                        // switch ()
                        if (index == 0) {
                            loadData(json);
                        } else if (index == 1) {
                            range1 = 0;
                            range2 = 5000;
                            sortMore(range1, range2);
                            // filter_amount
                        } else if (index == 2) {
                            range1 = 5000;
                            range2 = 10000;
                            sortMore(range1, range2);
                        } else if (index == 3) {
                            range1 = 10000;
                            range2 = 20000;
                            sortMore(range1, range2);
                        } else if (index == 4) {
                            range1 = 20000;
                            range2 = 10000000;
                            sortMore(range1, range2);
                        }

                    }
                })
                .setWidthMaxDp(600)
                .create()
                .show();
    }

    private void sortMore(int range1, int range2) {
        modelList.clear();
        hotDealsModelList.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);

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
                    NewModel model = new NewModel();

                    model.setId(p_id);
                    model.setName(p_name);
                    model.setImage_url(img_url);
                    model.setAmount(p_amount);
                    model.setTags(p_tags);
                    model.setDetails(p_details);
                    model.setRatings(p_rating);
                    Log.d("range_values", p_amount);

                    HotDealsModel hotDealsModel = new HotDealsModel();
                    hotDealsModel.setId(p_id);
                    hotDealsModel.setName(p_name);
                    hotDealsModel.setImage_url(img_url);
                    hotDealsModel.setAmount(p_amount);
                    hotDealsModel.setTags(p_tags);
                    hotDealsModel.setDetails(p_details);
                    hotDealsModel.setRatings(p_rating);
                    hotDealsModel.setDiscountedAmount(obj.getString("discounted_amount"));
                    Log.d("range_values", p_amount);


//                    int numberToSort = 0;
                    if (!p_amount.equals("")) {
                        int numberToSort = Integer.parseInt(p_amount);


                        if (numberToSort > range1 && numberToSort < range2) {
                            if (title.equals("New Stuff")) {
                                if (p_tags.contains("new_product")) {

                                    modelList.add(model);
                                }
                            }
                            if (title.equals("Recommended")) {
                                if (p_tags.contains("recommended_product")) {

                                    modelList.add(model);
                                }
                            }
                            if (title.equals("Hot Deals")) {
                                if (p_tags.contains("hot_deal")) {

                                    hotDealsModelList.add(hotDealsModel);
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (title.equals("New Stuff")) {
            if (modelList.size() == 0) {
                (findViewById(R.id.no_products)).setVisibility(View.VISIBLE);
            } else {
                (findViewById(R.id.no_products)).setVisibility(View.INVISIBLE);
            }
        } else if (title.equals("Recommended")) {
            if (modelList.size() == 0) {
                (findViewById(R.id.no_products)).setVisibility(View.VISIBLE);
            } else {
                (findViewById(R.id.no_products)).setVisibility(View.INVISIBLE);
            }
        } else {
            if (hotDealsModelList.size() == 0) {
                (findViewById(R.id.no_products)).setVisibility(View.VISIBLE);
            } else {
                (findViewById(R.id.no_products)).setVisibility(View.INVISIBLE);
            }
        }
        if (title.equals("Hot Deals")) {
            MoreHotDealsAdapter MoreHotDealsAdapter = new MoreHotDealsAdapter(this, hotDealsModelList, this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(MoreHotDealsAdapter);
        } else {
            adapter = new CustomGridAdapter(this, modelList);
            GridView gridView = (GridView) findViewById(R.id.grid);
            gridView.setAdapter(adapter);
        }

    }

    private void loadData(String json) {
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
                    NewModel model = new NewModel();

                    model.setId(p_id);
                    model.setName(p_name);
                    model.setImage_url(img_url);
                    model.setAmount(p_amount);
                    model.setTags(p_tags);
                    model.setDetails(p_details);
                    model.setRatings(p_rating);

                    HotDealsModel hotDealsModel = new HotDealsModel();
                    hotDealsModel.setId(p_id);
                    hotDealsModel.setName(p_name);
                    hotDealsModel.setImage_url(img_url);
                    hotDealsModel.setAmount(p_amount);
                    hotDealsModel.setTags(p_tags);
                    hotDealsModel.setDetails(p_details);
                    hotDealsModel.setRatings(p_rating);
                    hotDealsModel.setDiscountedAmount(obj.getString("discounted_amount"));
                    Log.d("range_values", p_amount);


                    if (title.equals("New Stuff")) {
                        if (p_tags.contains("new_product")) {

                            modelList.add(model);
                        }
                    }
                    if (title.equals("Recommended")) {
                        if (p_tags.contains("recommended_product")) {

                            modelList.add(model);
                        }
                    }
                    if (title.equals("Hot Deals")) {
                        if (p_tags.contains("hot_deal")) {

                            hotDealsModelList.add(hotDealsModel);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (title.equals("New Stuff")) {
            if (modelList.size() == 0) {
                (findViewById(R.id.no_products)).setVisibility(View.VISIBLE);
            } else {
                (findViewById(R.id.no_products)).setVisibility(View.INVISIBLE);
            }
        } else if (title.equals("Recommended")) {
            if (modelList.size() == 0) {
                (findViewById(R.id.no_products)).setVisibility(View.VISIBLE);
            } else {
                (findViewById(R.id.no_products)).setVisibility(View.INVISIBLE);
            }
        } else {
            if (hotDealsModelList.size() == 0) {
                (findViewById(R.id.no_products)).setVisibility(View.VISIBLE);
            } else {
                (findViewById(R.id.no_products)).setVisibility(View.INVISIBLE);
            }
        }
        if (title.equals("Hot Deals")) {

            MoreHotDealsAdapter MoreHotDealsAdapter = new MoreHotDealsAdapter(this, hotDealsModelList, this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(MoreHotDealsAdapter);
        } else {
            adapter = new CustomGridAdapter(this, modelList);
            GridView gridView = (GridView) findViewById(R.id.grid);
            gridView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        itemCart = menu.findItem(R.id.action_cart);

        icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(MoreProducts.this, icon);
        menu.findItem(R.id.action_delete).setVisible(false);
        menu.findItem(R.id.action_edit).setVisible(false);


        return super.onCreateOptionsMenu(menu);
    }

    public void setBadgeCount(final Context context, final LayerDrawable icon) {

        new Thread() {
            public void run() {
                final int profile_counts = cartDB.numberOfRows();
                count = String.valueOf(profile_counts);
                //show
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BadgeDrawable badge;

                        // Reuse drawable if possible
                        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
                        if (reuse != null && reuse instanceof BadgeDrawable) {
                            badge = (BadgeDrawable) reuse;
                        } else {
                            badge = new BadgeDrawable(context);
                        }

                        badge.setCount(count);
                        icon.mutate();
                        icon.setDrawableByLayerId(R.id.ic_badge, badge);
                    }
                });

            }
        }.start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                setBadgeCount(MoreProducts.this, icon);
            }
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
            finish();
        }

        if (id == R.id.action_cart) {
            Intent intent = new Intent(getApplicationContext(), Cart.class);
            startActivityForResult(intent, 1);
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
        addTCart(String.valueOf(id), name, amount, details, encodedImageData, "1", amount);
        setBadgeCount(MoreProducts.this, icon);
        //setBadgeCount(MainActivity.this, icon);
    }

    @Override
    public void onAddToWishList(int id, String name, String amount, String details, String encodedImageData, String s, String mAmount) {
        addToWishList(String.valueOf(id), name, amount, details, encodedImageData, "1", mAmount);
    }

    @Override
    public void onDeleteItem() {

    }

    public void addToWishList(String id, String name, String amount, String details, String encodedImageData, String s1, String mAmount) {
        WishDB wishDB = new WishDB(this);
        wishDB.insertIntoWishList(id, name, amount, details, encodedImageData, "1", mAmount);

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Successful!")
                .setContentText("Item added item to wishlist!")
                .show();
        EventBus.getDefault().post(new UpdateCartCount(1));
    }

    public void addTCart(String id, String name, String amount, String details, String encodedImageData, String s1, String amount1) {
        cartDB.insertIntoCart(id, name, amount, details, encodedImageData, "1", amount);
//Log.d("cart_clicked")
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Successful!")
                .setContentText("Item added item to cart!")
                .show();
        setBadgeCount(MoreProducts.this, icon);
        EventBus.getDefault().post(new UpdateCartCount(1));
    }

}
