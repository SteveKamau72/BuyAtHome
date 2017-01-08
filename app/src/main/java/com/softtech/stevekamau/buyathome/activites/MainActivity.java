package com.softtech.stevekamau.buyathome.activites;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softtech.stevekamau.buyathome.NavigationDrawerFragment;
import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.TopExceptionHandler;
import com.softtech.stevekamau.buyathome.adapter.CustomGridAdapter;
import com.softtech.stevekamau.buyathome.adapter.FeaturedGridAdapter;
import com.softtech.stevekamau.buyathome.adapter.HotDealsAdapter;
import com.softtech.stevekamau.buyathome.adapter.NewGridAdapter;
import com.softtech.stevekamau.buyathome.adapter.RecomGridAdapter;
import com.softtech.stevekamau.buyathome.app.AppConfig;
import com.softtech.stevekamau.buyathome.databaseHandlers.CartDB;
import com.softtech.stevekamau.buyathome.databaseHandlers.WishDB;
import com.softtech.stevekamau.buyathome.helper.BadgeDrawable;
import com.softtech.stevekamau.buyathome.helper.HorizontalListView;
import com.softtech.stevekamau.buyathome.helper.NotificationUtils;
import com.softtech.stevekamau.buyathome.helper.UrlFormatter;
import com.softtech.stevekamau.buyathome.interfaces.OnOptionsSelectedInterface;
import com.softtech.stevekamau.buyathome.interfaces.UpdateCartCount;
import com.softtech.stevekamau.buyathome.model.FeaturedModel;
import com.softtech.stevekamau.buyathome.model.HotDealsModel;
import com.softtech.stevekamau.buyathome.model.NewModel;
import com.softtech.stevekamau.buyathome.model.RecommendedModel;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.FragmentDrawerListener, OnOptionsSelectedInterface {
    private static final String TAG = MainActivity.class.getSimpleName();
    //private static final String url2 = "http://10.0.3.2/buyathome/userdetails/all_products.php";
    private static final String url2 = "http://snapt.t15.org/buyathome/all_products.php";
    static Button notifCount;
    static int mNotifCount = 0;
    int profile_counts;
    CartDB cartDB;
    Boolean isBackPressed = false;
    MenuItem itemCart;
    Button b1;
    ImageView img, img1, img2, img3;
    Context context;
    HorizontalListView lv, listView;
    com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar loadingProgressBar;
    NewGridAdapter newGridAdapter;
    CustomGridAdapter adapter;
    String data;
    Activity activity;
    RecomGridAdapter recommendedAdapter;
    FeaturedGridAdapter featuredAdapter;
    String p_name, img_url, p_amount, p_tags, p_details;
    int p_id, p_rating;
    TextView mToolBarTextView;
    LayerDrawable icon;
    String count;
    private Toolbar toolbar;
    private com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar bar;
    private List<NewModel> modelList = new ArrayList<NewModel>();
    //private List<NewModel> featured_modelList = new ArrayList<NewModel>();
    private ViewFlipper mViewFlipper;
    private Animation.AnimationListener mAnimationListener;
    private Context mContext;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!getstatus()) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("message", "Successfully logged out");
            startActivity(intent);
            finish();
        }

        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("Home");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
        displayView(0);
        getDataFromIntent();
        context = this;
        cartDB = new CartDB(this);
        cartDB.getSalesSold();
        composeViews();

        //setListViewHeightBasedOnChildren(lv);

        (findViewById(R.id.whatsNew_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MoreProducts.class);
                i.putExtra("title", "New Stuff");
                startActivity(i);
            }
        });
        (findViewById(R.id.recom_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MoreProducts.class);
                i.putExtra("title", "Recommended");
                startActivity(i);
            }
        });
        (findViewById(R.id.hotdeals_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MoreProducts.class);
                i.putExtra("title", "Hot Deals");
                startActivity(i);
            }
        });

    }


    private void composeViews() {
        //new stuff adapter
        RecyclerView newStuffRecyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager newStuffLlm = new LinearLayoutManager(context);
        newStuffRecyclerView.setLayoutManager(newStuffLlm);
        newStuffLlm.setOrientation(LinearLayoutManager.HORIZONTAL);
        newGridAdapter = new NewGridAdapter(this, modelList, this);
        newStuffRecyclerView.setAdapter(newGridAdapter);

        cartDB = new CartDB(this);
        //convert sqlite rows into Json fields
        Gson gson = new GsonBuilder().create();
        Log.d("json_array", gson.toJson(cartDB.getSalesSold()));
    }

    private void getDataFromIntent() {
        Intent i = getIntent();
        String message = i.getStringExtra("message");
        if (message == null || message.equals("")) {
        } else {
            if (message.equalsIgnoreCase(getuser_name())) {

                Snackbar.make(findViewById(android.R.id.content), "Signed in as " + message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {

                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
        data = i.getStringExtra("main_data_response");
        if (data == null) {
            //laptopsRequest();
        } else {
            parseMainData(data);
        }
    }

    private void parseMainData(String data) {
        List<FeaturedModel> featured_modelList = new ArrayList<FeaturedModel>();
        List<RecommendedModel> recommended_modelList = new ArrayList<RecommendedModel>();
        List<HotDealsModel> hotDealsModelList = new ArrayList<HotDealsModel>();
        List<Integer> randomIdsList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);

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

                    //new list
                    NewModel model = new NewModel();

                    model.setId(p_id);
                    model.setName(p_name);
                    model.setImage_url(img_url);
                    model.setAmount(p_amount);
                    model.setTags(p_tags);
                    model.setDetails(p_details);
                    model.setRatings(p_rating);

                    if (p_tags.contains("new_product") && !p_tags.contains("featured_product")) {
                        if (modelList.size() < 10) {
                            modelList.add(model);
                        }

                    }

                    //featured list
                    FeaturedModel featuredModel = new FeaturedModel();
                    featuredModel.setId(p_id);
                    featuredModel.setName(p_name);
                    featuredModel.setImage_url(img_url);
                    featuredModel.setAmount(p_amount);
                    featuredModel.setTags(p_tags);
                    featuredModel.setDetails(p_details);
                    featuredModel.setRatings(p_rating);

                    if (p_tags.contains("featured_product")) {
                        if (featured_modelList.size() < 10) {
                            featured_modelList.add(featuredModel);
                        }
                    }

                    //randomize recommended
                    int recommended_modelSize = jsonArray.length();
                    Random r = new Random();
                    int randomObjectIndex = r.nextInt(recommended_modelSize - 0) + 0;
                    JSONObject selectedRandomObject = jsonArray.getJSONObject(randomObjectIndex);

                    UrlFormatter random_urlFormatter = new UrlFormatter();
                    String random_img_url = random_urlFormatter.unescapeJavaString((selectedRandomObject.getString("image_url")));

                    RecommendedModel recommendedModel = new RecommendedModel();
                    recommendedModel.setId(selectedRandomObject.getInt("p_id"));
                    recommendedModel.setName(selectedRandomObject.getString("p_name"));
                    recommendedModel.setImage_url(random_img_url);
                    recommendedModel.setAmount(selectedRandomObject.getString("p_amount"));
                    recommendedModel.setTags(selectedRandomObject.getString("tags"));
                    recommendedModel.setDetails(selectedRandomObject.getString("p_details"));
                    recommendedModel.setRatings(selectedRandomObject.getInt("p_rating"));
                    recommendedModel.setDiscountedAmount(selectedRandomObject.getString("discounted_amount"));

                    if (!selectedRandomObject.getString("tags").contains("featured_product")
                            && !randomIdsList.contains(selectedRandomObject.getInt("p_id"))) {
                        if (recommended_modelList.size() < 10) {
                            randomIdsList.add(selectedRandomObject.getInt("p_id"));
                            recommended_modelList.add(recommendedModel);
                            Log.d("tags_2", recommended_modelList.get(0).getName());
                        }
                    }

                    //randomize hot deals
                    HotDealsModel hotDealsModel = new HotDealsModel();
                    hotDealsModel.setId(selectedRandomObject.getInt("p_id"));
                    hotDealsModel.setName(selectedRandomObject.getString("p_name"));
                    hotDealsModel.setImage_url(random_img_url);
                    hotDealsModel.setAmount(selectedRandomObject.getString("p_amount"));
                    hotDealsModel.setTags(selectedRandomObject.getString("tags"));
                    hotDealsModel.setDetails(selectedRandomObject.getString("p_details"));
                    hotDealsModel.setRatings(selectedRandomObject.getInt("p_rating"));
                    hotDealsModel.setDiscountedAmount(selectedRandomObject.getString("discounted_amount"));

                    if (selectedRandomObject.getString("tags").contains("hot_deal")) {
                        if (hotDealsModelList.size() < 10) {
                            hotDealsModelList.add(hotDealsModel);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            setAdapters(featured_modelList, recommended_modelList, hotDealsModelList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setAdapters(List<FeaturedModel> featured_modelList,
                             List<RecommendedModel> recommended_modelList, List<HotDealsModel> hotDealsModelList) {
        //featured adapter
        RecyclerView featuredRecyclerView = (RecyclerView) findViewById(R.id.feat);
        LinearLayoutManager featuredLlm = new LinearLayoutManager(context);
        featuredRecyclerView.setLayoutManager(featuredLlm);
        featuredLlm.setOrientation(LinearLayoutManager.HORIZONTAL);
        FeaturedGridAdapter featuredAdapter = new FeaturedGridAdapter(this, featured_modelList);
        featuredRecyclerView.setAdapter(featuredAdapter);

        //recommended adapter
        RecyclerView recommendedRecyclerView = (RecyclerView) findViewById(R.id.rv2);
        LinearLayoutManager recommendedLlm = new LinearLayoutManager(context);
        recommendedRecyclerView.setLayoutManager(recommendedLlm);
        recommendedLlm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recommendedAdapter = new RecomGridAdapter(this, recommended_modelList, this);
        recommendedRecyclerView.setAdapter(recommendedAdapter);

        //hot deals
        RecyclerView hot_dealsrvRecyclerView = (RecyclerView) findViewById(R.id.hot_dealsrv);
        LinearLayoutManager fhot_dealsrvLlm = new LinearLayoutManager(context);
        hot_dealsrvRecyclerView.setLayoutManager(fhot_dealsrvLlm);
        fhot_dealsrvLlm.setOrientation(LinearLayoutManager.HORIZONTAL);
        HotDealsAdapter hotDealsAdapter = new HotDealsAdapter(this, hotDealsModelList, this);
        hot_dealsrvRecyclerView.setAdapter(hotDealsAdapter);


    }

    Boolean getstatus() {
        SharedPreferences spref2 = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        Boolean islogged = spref2.getBoolean("islogged", false);
        return islogged;
    }

    String getuser_name() {
        SharedPreferences spref2 = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        String user_name = spref2.getString("name", "");
        return user_name;
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:

                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                isBackPressed = true;
                break;

            case 1:
                fragment = new PhonesFragment();
                title = getString(R.string.title_phones);
                isBackPressed = false;
                break;

            case 2:
                fragment = new LaptopsFragment();
                title = getString(R.string.title_activity_laptops);
                isBackPressed = false;
                break;

            case 3:
                fragment = new PhoneAccessoriesFragment();
                title = getString(R.string.title_accessories);
                break;

            case 4:
                fragment = new LaptopsAccessoriesFragment();
                title = getString(R.string.title_accessories);
                isBackPressed = false;
                break;
            case 5:
                /*fragment = new Categories();
                title = getString(R.string.title_categories);*/
                break;
            default:
                break;

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // set the toolbar title
            mToolBarTextView.setText(title);
            // getSupportActionBar().setTitle(title);
        }
    }

    private void socialDialog() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.social, null);
        final android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(this);
        img1 = (ImageView) layout.findViewById(R.id.imageView);
        img2 = (ImageView) layout.findViewById(R.id.imageView1);
        img3 = (ImageView) layout.findViewById(R.id.imageView2);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "0711219490");
                smsIntent.putExtra("sms_body", "");
                startActivity(smsIntent);

            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = "+254711219490";
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "buyathome2015@gmail.com", null));
                i.putExtra(Intent.EXTRA_SUBJECT, "Direct Contact to BuyAtHome");
                i.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(i, "Send via email"));
            }
        });
        adb.setView(layout);
        adb.setCancelable(true);

        adb.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        adb.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        // Logs 'install' and 'app activate' App Events.
//        AppEventsLogger.activateApp(this);

        // setBadgeCount(MainActivity.this, icon);
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.PURCHASE_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(AppConfig.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onBackPressed() {
        if (!isBackPressed) {
            isBackPressed = true;
            getSupportFragmentManager().popBackStackImmediate("Frag1", 0);
            mToolBarTextView.setText(R.string.title_home);
            Fragment fragment = new HomeFragment();
            mToolBarTextView.setText(R.string.title_home);
            // getFragmentManager().popBackStack();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

            }
        }*/
        setBadgeCount(MainActivity.this, icon);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        itemCart = menu.findItem(R.id.action_cart);
        icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(MainActivity.this, icon);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        itemCart = menu.findItem(R.id.action_cart);

        icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(MainActivity.this, icon);

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
        if (id == R.id.action_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Buying Laptops,Mobile Phones,Mobile and Laptop accessories " +
                    "has never been simpler from the comfort of your home!!Download our android app BuyAtHome https://play.google.com/store/apps/details?id=com.softtech.stevekamau.buyathome on Google PlayStore and shop with us.");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BuyAtHome");
            startActivity(Intent.createChooser(shareIntent, "Share this app with..."));
        }

        if (id == R.id.action_search) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(i);
        }
        if (id == R.id.menu_contact) {
            socialDialog();
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
        setBadgeCount(MainActivity.this, icon);
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
        setBadgeCount(MainActivity.this, icon);
        EventBus.getDefault().post(new UpdateCartCount(1));
    }

}
