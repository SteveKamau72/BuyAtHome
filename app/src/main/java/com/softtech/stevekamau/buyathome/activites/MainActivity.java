package com.softtech.stevekamau.buyathome.activites;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.appevents.AppEventsLogger;
import com.softtech.stevekamau.buyathome.NavigationDrawerFragment;
import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.TopExceptionHandler;
import com.softtech.stevekamau.buyathome.adapter.CustomGridAdapter;
import com.softtech.stevekamau.buyathome.adapter.FeaturedGridAdapter;
import com.softtech.stevekamau.buyathome.adapter.NewGridAdapter;
import com.softtech.stevekamau.buyathome.adapter.RecomGridAdapter;
import com.softtech.stevekamau.buyathome.app.AppController;
import com.softtech.stevekamau.buyathome.databaseHandlers.CartDB;
import com.softtech.stevekamau.buyathome.helper.HorizontalListView;
import com.softtech.stevekamau.buyathome.helper.UrlFormatter;
import com.softtech.stevekamau.buyathome.model.NewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.FragmentDrawerListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    //private static final String url2 = "http://10.0.3.2/buyathome/userdetails/all_products.php";
    private static final String url2 = "http://snapt.t15.org/buyathome/all_products.php";
    static Button notifCount;
    static int mNotifCount = 0;
    private static String Title = "title";
    private static String Rate = "rating";
    private static String Genre = "genre";
    private static String bitmap = "thumbnailUrl";
    int numRows;
    int id_To_Update = 0;
    TextView one, two;
    CartDB cartDB;
    Boolean isBackPressed = false;
    MenuItem item;
    Button b1;
    ImageView img, img1, img2, img3;
    Context context;
    HorizontalListView lv, listView;
    com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar loadingProgressBar;
    NewGridAdapter ad;
    CustomGridAdapter adapter;
    String data;
    Activity activity;
    RecomGridAdapter ra;
    FeaturedGridAdapter feat_adapter;
    String p_name, img_url, p_amount, p_tags, p_details;
    int p_id, p_rating;
    TextView mToolBarTextView;
    String title_fragment;
    private Toolbar toolbar;
    private com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar bar;
    private List<NewModel> modelList = new ArrayList<NewModel>();
    private ViewFlipper mViewFlipper;
    private Animation.AnimationListener mAnimationListener;
    private Context mContext;

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

      /*  if(ad.i){
            Snackbar.make(findViewById(android.R.id.content), "empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else {
            Snackbar.make(findViewById(android.R.id.content), "full", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }*/
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
    }

    private void composeViews() {
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        ad = new NewGridAdapter(this, modelList);
        rv.setAdapter(ad);

        RecyclerView rv2 = (RecyclerView) findViewById(R.id.rv2);
        LinearLayoutManager llm2 = new LinearLayoutManager(context);
        rv2.setLayoutManager(llm2);
        llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
        ra = new RecomGridAdapter(this, modelList);
        rv2.setAdapter(ra);

        RecyclerView rv3 = (RecyclerView) findViewById(R.id.feat);
        LinearLayoutManager llm3 = new LinearLayoutManager(context);
        rv3.setLayoutManager(llm3);
        llm3.setOrientation(LinearLayoutManager.HORIZONTAL);
        feat_adapter = new FeaturedGridAdapter(this, modelList);
        rv3.setAdapter(feat_adapter);
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
                    NewModel model = new NewModel();

                    model.setId(p_id);
                    model.setName(p_name);
                    model.setImage_url(img_url);
                    model.setAmount(p_amount);
                    model.setTags(p_tags);
                    model.setDetails(p_details);
                    model.setRatings(p_rating);

                    if (p_tags.contains("new_product")) {

                        modelList.add(model);
                    }
                    if (p_tags.contains("featured_product")) {
                        modelList.add(model);
                    }
                    if (p_tags.contains("recommended_product")) {
                        modelList.add(model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void mainRequest(final String tags) {
        bar = (com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar) this.findViewById(R.id.progressBar);
        bar.setCircleBackgroundEnabled(false);
        bar.setColorSchemeResources(android.R.color.holo_green_light);
        bar.setVisibility(View.VISIBLE);
        // Creating volley request obj
        final JsonArrayRequest productReq = new JsonArrayRequest(url2,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        bar.setVisibility(View.GONE);
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            if (tags.contains("new")) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    NewModel model = new NewModel();
                                    model.setId(obj.getInt("p_id"));
                                    model.setName(obj.getString("p_name"));
                                    UrlFormatter urlFormatter = new UrlFormatter();
                                    String img_url = urlFormatter.unescapeJavaString((obj.getString("image_url")));
                                    Log.d("image_url", img_url);
                                    model.setImage_url(img_url);
                                    model.setAmount(obj.getString("p_amount"));
                                    model.setTags(obj.getString("tags"));
                                    model.setDetails(obj.getString("p_details"));
                                    model.setRatings(obj.getInt("p_rating"));
                                    modelList.add(model);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        ad.notifyDataSetChanged();
                        ra.notifyDataSetChanged();
                        feat_adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                       /* if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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
                        }*/


            }

            ;
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(productReq);
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
            fragmentTransaction.addToBackStack("Frag1");
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

        // Logs 'install' and 'app activate' App Events.
//        AppEventsLogger.activateApp(this);
        if (item != null) {
            MenuItemCompat.setActionView(item, R.layout.feed_update_count);
            View view = MenuItemCompat.getActionView(item);
            int profile_counts = cartDB.numberOfRows();
            notifCount = (Button) view.findViewById(R.id.notif_count);
            notifCount.setText(String.valueOf(profile_counts));
            cartDB.close();
            notifCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, Cart.class);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected void onPause() {
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
        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        View view = MenuItemCompat.getActionView(item);
        int profile_counts = cartDB.numberOfRows();
        notifCount = (Button) view.findViewById(R.id.notif_count);
        notifCount.setText(String.valueOf(profile_counts));
        cartDB.close();
        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Cart.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        View view = MenuItemCompat.getActionView(item);
        int profile_counts = cartDB.numberOfRows();
        notifCount = (Button) view.findViewById(R.id.notif_count);
        notifCount.setText(String.valueOf(profile_counts));
        cartDB.close();
        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Cart.class);
                startActivity(i);
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    private void setNotifCount(int count) {
        mNotifCount = count;
        supportInvalidateOptionsMenu();
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
        if (id == R.id.action_refresh) {
            // laptopsRequest();
        }

        return super.onOptionsItemSelected(item);
    }

}
