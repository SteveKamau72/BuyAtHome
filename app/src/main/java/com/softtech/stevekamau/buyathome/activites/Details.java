package com.softtech.stevekamau.buyathome.activites;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.TopExceptionHandler;
import com.softtech.stevekamau.buyathome.adapter.ReviewsAdapter;
import com.softtech.stevekamau.buyathome.app.AppController;
import com.softtech.stevekamau.buyathome.databaseHandlers.CartDB;
import com.softtech.stevekamau.buyathome.databaseHandlers.WishDB;
import com.softtech.stevekamau.buyathome.helper.TimeAgo;
import com.softtech.stevekamau.buyathome.model.ReviewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Details extends ActionBarActivity {
    static Button notifCount;
    static int mNotifCount = 0;
    CartDB cartDB;
    WishDB wishDB;
    TextView title, amount, description, number_reviews, add_review;
    int id_To_Update = 0;
    NetworkImageView thumbNail;
    ImageLoader imageLoader;
    String bitmap, p_id, message, url;
    EditText review_input;
    Context context;
    ReviewsAdapter ra;
    RecyclerView rv;
    int profile_counts;
    String s_name, s_amount, s_description;
    com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar circularbar, recyc_bar;
    MenuItem item;
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
        p_id = i.getStringExtra("p_id");
        bitmap = i.getStringExtra("image1_url");
        s_name = i.getStringExtra("title");
        s_amount = i.getStringExtra("amount");
        s_description = i.getStringExtra("description");

        title = (TextView) findViewById(R.id.title_label);
        amount = (TextView) findViewById(R.id.amount_label);
        description = (TextView) findViewById(R.id.description_label);
        title.setText(s_name);
        amount.setText(s_amount);
        description.setText(s_description);
        number_reviews = (TextView) findViewById(R.id.no_review_text);

        //image views
        thumbNail = (NetworkImageView) findViewById(R.id.img1);
        NetworkImageView img2 = (NetworkImageView) findViewById(R.id.img2);
        NetworkImageView img3 = (NetworkImageView) findViewById(R.id.img3);
        NetworkImageView img4 = (NetworkImageView) findViewById(R.id.img4);
        thumbNail.setImageUrl(bitmap, imageLoader);
        img2.setImageUrl(bitmap, imageLoader);
        img3.setImageUrl(bitmap, imageLoader);
        img4.setImageUrl(bitmap, imageLoader);
        thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable bd = (BitmapDrawable) ((NetworkImageView) view.findViewById(R.id.img1))
                        .getDrawable();
                Bitmap bitmapzoom = bd.getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bd.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] imgByte = baos.toByteArray();
                Intent intent = new Intent(getApplicationContext(), ZoomImage.class);
                intent.putExtra("image", imgByte);
                startActivity(intent);
            }
        });

        circularbar = (com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar) this.findViewById(R.id.progressBar);
        circularbar.setCircleBackgroundEnabled(false);
        circularbar.setColorSchemeResources(android.R.color.holo_green_light);
        recyc_bar = (com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar) this.findViewById(R.id.recycler_progressBar);
        recyc_bar.setCircleBackgroundEnabled(false);
        recyc_bar.setColorSchemeResources(android.R.color.holo_green_light);
        ImageView img = (ImageView) findViewById(R.id.share);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out " + title.getText() + " at BuyAtHome app for Android at only Kshs." + amount.getText() + "Download the android app on PlayStore");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BuyAtHome");
                startActivity(Intent.createChooser(shareIntent, "share..."));
            }
        });

        ImageView img_fav = (ImageView) findViewById(R.id.favourite);
        img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavourites();
            }
        });
        review_input = (EditText) findViewById(R.id.review_input);
        review_input.setVisibility(View.GONE);
        //review_input.addTextChangedListener(mTextEditorWatcher);
        networkRequests(p_id, "load reviews");

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
                thumbNail.setImageUrl(bitmap, imageLoader);
                Toast.makeText(getApplicationContext(), bitmap, Toast.LENGTH_SHORT).show();
                break;
            case R.id.img3:
                thumbNail.setImageUrl(bitmap, imageLoader);
                Toast.makeText(getApplicationContext(), bitmap, Toast.LENGTH_SHORT).show();
                break;
            case R.id.img4:
                thumbNail.setImageUrl(bitmap, imageLoader);
                Toast.makeText(getApplicationContext(), bitmap, Toast.LENGTH_SHORT).show();
                break;
            case R.id.call_button:

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Call us directly to make your order!")
                        .setCancelText("No,cancel")
                        .setConfirmText("Yes,do it!")
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
                        networkRequests(p_id, "add review");
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
        thumbNail.buildDrawingCache();
        Bitmap bmap = thumbNail.getDrawingCache();
        String encodedImageData = getEncoded64ImageStringFromBitmap(bmap);

        title.setText(s_name);
        amount.setText(s_amount);
        description.setText(s_description);
        cartDB.insertIntoCart(p_id, s_name, s_amount, s_description, encodedImageData, "1", s_amount);

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText("Successfully added item to cart!")
                .show();

        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        View view = MenuItemCompat.getActionView(item);
        notifCount = (Button) view.findViewById(R.id.notif_count);
        notifCount.setText(String.valueOf(profile_counts + 1));
        cartDB.close();
        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Details.this, Cart.class);
                startActivity(i);
            }
        });
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

    public void addToFavourites() {
        //get Image
        thumbNail.buildDrawingCache();
        Bitmap bmap = thumbNail.getDrawingCache();
        String encodedImageData = getEncoded64ImageStringFromBitmap(bmap);

        title.setText(s_name);
        amount.setText(s_amount);
        description.setText(s_description);
        wishDB.insertIntoWishList(p_id, s_name, s_amount, s_description, encodedImageData, "1", s_amount);

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText("Successfully added to Wishlist!")
                .show();

    }

    private void networkRequests(final String p_id, final String method) {
       /* if (method.equals("load reviews")) {
            url = "http://10.0.3.2/buyathome/userdetails/fetch_reviews.php";
        } else if (method.equals("add review")) {
            url = "http://10.0.3.2/buyathome/userdetails/add_review.php";
        }*/
        if (method.equals("load reviews")) {
            url = "http://snapt.t15.org/buyathome/fetch_reviews.php";
        } else if (method.equals("add review")) {
            url = "http://snapt.t15.org/buyathome/add_review.php";
        }
        recyc_bar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("reviews", response);
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
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        View view = MenuItemCompat.getActionView(item);
        profile_counts = cartDB.numberOfRows();
        notifCount = (Button) view.findViewById(R.id.notif_count);
        notifCount.setText(String.valueOf(profile_counts));
        cartDB.close();
        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Details.this, Cart.class);
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
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
