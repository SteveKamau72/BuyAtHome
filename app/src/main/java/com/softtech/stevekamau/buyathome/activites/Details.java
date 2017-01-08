package com.softtech.stevekamau.buyathome.activites;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.TopExceptionHandler;
import com.softtech.stevekamau.buyathome.adapter.ReviewsAdapter;
import com.softtech.stevekamau.buyathome.app.AppController;
import com.softtech.stevekamau.buyathome.databaseHandlers.CartDB;
import com.softtech.stevekamau.buyathome.databaseHandlers.WishDB;
import com.softtech.stevekamau.buyathome.helper.BadgeDrawable;
import com.softtech.stevekamau.buyathome.helper.TimeAgo;
import com.softtech.stevekamau.buyathome.helper.UniversalImageLoader;
import com.softtech.stevekamau.buyathome.model.ReviewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.ParseException;


public class Details extends ActionBarActivity {
    static Button notifCount;
    static int mNotifCount = 0;
    CartDB cartDB;
    WishDB wishDB;
    TextView title, tvAmount, description, number_reviews, add_review;
    int id_To_Update = 0;
    ImageView thumbNail;
    NetworkImageView img2, img3, img4;
    ImageLoader imageLoader;
    String bitmap, message, url, rating;
    EditText review_input;
    int p_id;
    Context context;
    ReviewsAdapter ra;
    RecyclerView rv;
    int profile_counts;
    String s_name, s_amount, s_description;
    com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar circularbar, recyc_bar;
    MenuItem itemCart;
    RatingBar pop_ratingbar;
    LayerDrawable icon;
    String count;
    SweetAlertDialog pDialog;
    private List<ReviewModel> reviewmodelList = new ArrayList<>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("View details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cartDB = new CartDB(this);
        wishDB = new WishDB(this);
        Intent i = getIntent();
        imageLoader = AppController.getInstance().getImageLoader();
        p_id = i.getIntExtra("p_id", 0);
        bitmap = i.getStringExtra("image1_url");
        s_name = i.getStringExtra("title");
        s_amount = i.getStringExtra("amount");
        s_description = i.getStringExtra("description");
        rating = String.valueOf(i.getIntExtra("rating", 0));

        Log.d("splash_screen_req", s_name + " " + p_id);

        setUpViews();

        title.setText(s_name);
        tvAmount.setText("kshs. " + s_amount);
        description.setText(s_description);

        setImage();
        img2.setImageUrl(bitmap, imageLoader);
        img3.setImageUrl(bitmap, imageLoader);
        img4.setImageUrl(bitmap, imageLoader);
        thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ZoomImage.class);
                intent.putExtra("image_url", bitmap);
                startActivity(intent);
            }
        });

        //review_input.addTextChangedListener(mTextEditorWatcher);
        networkRequests(String.valueOf(p_id), "load reviews");
    }

    private void setImage() {
        UniversalImageLoader imgLoader = new UniversalImageLoader();
        imgLoader.setUpDisplayImageView(thumbNail, bitmap, R.drawable.product_placeholder);
    }

    private void setUpViews() {
        title = (TextView) findViewById(R.id.title_label);
        tvAmount = (TextView) findViewById(R.id.amount_label);
        description = (TextView) findViewById(R.id.description_label);
        Linkify.addLinks(description, Linkify.WEB_URLS);
        number_reviews = (TextView) findViewById(R.id.no_review_text);
        pop_ratingbar = (RatingBar) findViewById(R.id.pop_ratingbar);
        pop_ratingbar.setRating(Float.parseFloat(rating));
        //image views
        thumbNail = (ImageView) findViewById(R.id.img1);
        img2 = (NetworkImageView) findViewById(R.id.img2);
        img3 = (NetworkImageView) findViewById(R.id.img3);
        img4 = (NetworkImageView) findViewById(R.id.img4);
        ImageView img_fav = (ImageView) findViewById(R.id.favourite);
        img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToWishList();
            }
        });
        circularbar = (com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar) this.findViewById(R.id.progressBar);
        circularbar.setCircleBackgroundEnabled(false);
        circularbar.setColorSchemeResources(R.color.green_color);
        recyc_bar = (com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar) this.findViewById(R.id.recycler_progressBar);
        recyc_bar.setCircleBackgroundEnabled(false);
        recyc_bar.setColorSchemeResources(R.color.green_color);
        ImageView img = (ImageView) findViewById(R.id.share);
        assert img != null;
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey, check out " + title.getText() + " at BuyAtHome app for Android at only Kshs." + tvAmount.getText()
                                + "\nDownload the app for more amazing deals: "
                                + "https://play.google.com/store/apps/details?id=" + appPackageName);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BuyAtHome App for Android");
                startActivity(Intent.createChooser(shareIntent, "share..."));
            }
        });
        review_input = (EditText) findViewById(R.id.review_input);
        review_input.setVisibility(View.GONE);

        rv = (RecyclerView) findViewById(R.id.rev_recycler);
        LinearLayoutManager llm2 = new LinearLayoutManager(context);
        rv.setLayoutManager(llm2);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);
        ra = new ReviewsAdapter(this, reviewmodelList);
        rv.setAdapter(ra);
        rv.setVisibility(View.GONE);
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rev, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rev.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rev, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }


    String getday() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(now);
    }

    String getuser_name() {
        SharedPreferences spref2 = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        String user_name = spref2.getString("name", "");
        return user_name;
    }

    String getuser_email() {
        SharedPreferences spref2 = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        String user_email = spref2.getString("email", "");
        return user_email;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img2:
                // thumbNail.setImageUrl(bitmap, imageLoader);
                Toast.makeText(getApplicationContext(), bitmap, Toast.LENGTH_SHORT).show();
                break;
            case R.id.img3:
                // thumbNail.setImageUrl(bitmap, imageLoader);
                Toast.makeText(getApplicationContext(), bitmap, Toast.LENGTH_SHORT).show();
                break;
            case R.id.img4:
                //thumbNail.setImageUrl(bitmap, imageLoader);
                Toast.makeText(getApplicationContext(), bitmap, Toast.LENGTH_SHORT).show();
                break;
            case R.id.call_button:

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Direct Call!")
                        .setContentText("Contact us to make your order!")
                        .setCancelText("No, cancel")
                        .setConfirmText("Yes, do it!")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                callNumber();

                            }
                        })
                        .show();
                // callNumber();
                break;
            case R.id.add_review:
                add_review = (TextView) findViewById(R.id.add_review);
                if (add_review.getText().toString().equals("Add your review")) {
                    review_input.setVisibility(View.VISIBLE);
                    add_review.setText("Send review");
                } else {
                    if (review_input.getText().toString().trim().length() > 0) {
                        networkRequests(String.valueOf(p_id), "add review");
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Cannot send empty review", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }

                break;
            case R.id.cart:
                saveToCart();
                break;
        }
    }

    private void saveToCart() {
        //get Image
      /*  thumbNail.buildDrawingCache();
        Bitmap bmap = thumbNail.getDrawingCache();
        String encodedImageData = getEncoded64ImageStringFromBitmap(bmap);*/

       /* title.setText(s_name);
        tvAmount.setText(s_amount);
        description.setText(s_description);*/
        cartDB.insertIntoCart(String.valueOf(p_id), s_name, s_amount, s_description, bitmap, "1", s_amount);

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Successful!")
                .setContentText("Item added item to cart!")
                .show();
        setBadgeCount(Details.this, icon);
    }


    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }


    private String saveToInternalStorage(Bitmap bitmapImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("cart", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "product.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath() + mypath;
    }

    private void callNumber() {
        String number = "+254700688591";
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

    public void addToWishList() {
        //get Image
       /* thumbNail.buildDrawingCache();
        Bitmap bmap = thumbNail.getDrawingCache();
        String encodedImageData = getEncoded64ImageStringFromBitmap(bmap);*/

      /*  title.setText(s_name);
        amount.setText(s_amount);
        description.setText(s_description);*/
        wishDB.insertIntoWishList(String.valueOf(p_id), s_name, s_amount, s_description, bitmap, "1", s_amount);

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Successful!")
                .setContentText("Item added item to cart!")
                .show();

    }

    private void networkRequests(final String p_id, final String method) {

        if (method.equals("load reviews")) {
            url = getResources().getString(R.string.server_url) + "fetch_reviews.php";
        } else if (method.equals("add review")) {
            url = getResources().getString(R.string.server_url) + "add_review.php";
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if (method.equals("load reviews")) {
            params.put("p_id", p_id);
        } else if (method.equals("add review")) {
            params.put("p_id", p_id);
            params.put("name", getuser_name());
            params.put("rev_date", getday());
            params.put("review", review_input.getText().toString());
            params.put("email", getuser_email());
        }
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                recyc_bar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.d("reviews" + "/" + p_id + "/", s);
                recyc_bar.setVisibility(View.INVISIBLE);
                if (method.equals("load reviews")) {
                    try {
                        JSONArray jArray = new JSONArray(s);
                        JSONObject jObject = null;
                        for (int i = 0; i < jArray.length(); i++) {
                            jObject = jArray.getJSONObject(i);
                            ReviewModel rm = new ReviewModel();
                            rm.setUserReviewName(jObject.getString("rev_name"));
                            rm.setcomment(jObject.getString("rev_comment"));

                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH); // I assume d-M, you may refer to M-d for month-day instead.
                            Date date = formatter.parse(jObject.getString("rev_date")); // You will need try/catch around this
                            long millis = date.getTime();
                            Log.d("another time", String.valueOf(millis));
                            TimeAgo timeAgo = new TimeAgo(getApplicationContext());
                            rm.settime_stamp(String.valueOf(timeAgo.timeAgo(millis)));

                            reviewmodelList.add(rm);
                            if (reviewmodelList.size() > 0) {
                                if (reviewmodelList.size() == 1) {
                                    number_reviews.setText(String.valueOf(reviewmodelList.size()) + " review");
                                } else {
                                    number_reviews.setText(String.valueOf(reviewmodelList.size()) + " reviews");
                                }

                                rv.setVisibility(View.VISIBLE);
                            }

                        }
                        ra.notifyDataSetChanged();
                    } catch (JSONException | ParseException | java.text.ParseException e) {
                        e.printStackTrace();
                    }
                } else if (method.equals("add review")) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        Boolean success = jsonObject.getBoolean("success");
                        message = jsonObject.getString("message");
                        if (success) {
                            review_input.setVisibility(View.GONE);
                            review_input.setText("");
                            add_review.setText("Add your review");
                            reviewmodelList.clear();
                            networkRequests(p_id, "load reviews");
                            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                recyc_bar.setVisibility(View.INVISIBLE);
                if (method.equals("load reviews")) {
                    Snackbar.make(findViewById(android.R.id.content), "Cannot load reviews at the moment, retry later", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (method.equals("add review")) {
                    Snackbar.make(findViewById(android.R.id.content), "Cannot add your review at the moment, retry later", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }


        });

       /* recyc_bar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("reviews" + "/" + p_id + "/", response);
                        recyc_bar.setVisibility(View.INVISIBLE);
                        if (method.equals("load reviews")) {
                            try {
                                JSONArray jArray = new JSONArray(response);
                                JSONObject jObject = null;
                                for (int i = 0; i < jArray.length(); i++) {
                                    jObject = jArray.getJSONObject(i);
                                    ReviewModel rm = new ReviewModel();
                                    rm.setUserReviewName(jObject.getString("rev_name"));
                                    rm.setcomment(jObject.getString("rev_comment"));

                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH); // I assume d-M, you may refer to M-d for month-day instead.
                                    Date date = formatter.parse(jObject.getString("rev_date")); // You will need try/catch around this
                                    long millis = date.getTime();
                                    Log.d("another time", String.valueOf(millis));
                                    TimeAgo timeAgo = new TimeAgo(getApplicationContext());
                                    rm.settime_stamp(String.valueOf(timeAgo.timeAgo(millis)));

                                    reviewmodelList.add(rm);
                                    if (reviewmodelList.size() > 0) {
                                        if (reviewmodelList.size() == 1) {
                                            number_reviews.setText(String.valueOf(reviewmodelList.size()) + " review");
                                        } else {
                                            number_reviews.setText(String.valueOf(reviewmodelList.size()) + " reviews");
                                        }

                                        rv.setVisibility(View.VISIBLE);
                                    }

                                }
                                ra.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (method.equals("add review")) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Boolean success = jsonObject.getBoolean("success");
                                message = jsonObject.getString("message");
                                if (success) {
                                    review_input.setVisibility(View.GONE);
                                    review_input.setText("");
                                    add_review.setText("Add your review");
                                    reviewmodelList.clear();
                                    networkRequests(p_id, "load reviews");
                                    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        recyc_bar.setVisibility(View.INVISIBLE);
                        if (method.equals("load reviews")) {
                            Snackbar.make(findViewById(android.R.id.content), "Cannot load reviews at the moment, retry later", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else if (method.equals("add review")) {
                            Snackbar.make(findViewById(android.R.id.content), "Cannot add your review at the moment, retry later", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (method.equals("load reviews")) {
                    params.put("p_id", p_id);
                } else if (method.equals("add review")) {
                    params.put("p_id", p_id);
                    params.put("name", getuser_name());
                    params.put("rev_date", getday());
                    params.put("review", review_input.getText().toString());
                    params.put("email", getuser_email());
                }
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        itemCart = menu.findItem(R.id.action_cart);

        icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(Details.this, icon);

        if (getuser_name().equalsIgnoreCase("Admin")) {
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_edit).setVisible(true);
        } else {
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_edit).setVisible(false);
        }

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
                setBadgeCount(Details.this, icon);
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
        if (id == R.id.action_delete) {
            deleteProductRequest();
        }
        if (id == R.id.action_cart) {
            Intent intent = new Intent(getApplicationContext(), Cart.class);
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteProductRequest() {
        String url = getResources().getString(R.string.server_url) + "delete_product.php";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("p_id", p_id);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                loadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                pDialog.dismissWithAnimation();
                Log.d("splash_screen_req", s + " " + p_id);
                System.out.print(s);
                try {
                    JSONObject obj = new JSONObject(s);

                    Boolean success = obj.getBoolean("success");
                    if (success) {
                        Snackbar.make(findViewById(android.R.id.content), "Record deleted", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Failed to delete, retry later", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Snackbar.make(findViewById(android.R.id.content), "Failed to delete, retry later", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                pDialog.dismissWithAnimation();
            }


        });

    }

    private void loadingDialog() {
        pDialog = new SweetAlertDialog(Details.this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading");
        pDialog.show();
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.green_color));
    }

}
