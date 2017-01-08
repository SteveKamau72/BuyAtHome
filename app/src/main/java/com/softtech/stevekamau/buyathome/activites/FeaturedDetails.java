package com.softtech.stevekamau.buyathome.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.app.AppController;
import com.softtech.stevekamau.buyathome.helper.UniversalImageLoader;

import java.text.DecimalFormat;

public class FeaturedDetails extends AppCompatActivity {
    ImageLoader imageLoader;
    String image_url, p_id, message, url;
    String s_name, s_amount, s_description;
    ImageView thumbNail;
    TextView tvDescription, tvAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        Intent i = getIntent();
        imageLoader = AppController.getInstance().getImageLoader();
        p_id = i.getStringExtra("p_id");
        image_url = i.getStringExtra("image1_url");
        s_name = i.getStringExtra("title");
        s_amount = i.getStringExtra("amount");
        s_description = i.getStringExtra("description");

        getSupportActionBar().setTitle(s_name);
        composeViews();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                final String appPackageName = getPackageName();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        s_description+"\n"+ " Download BuyAtHome app for more amazing deals: "
                                + "https://play.google.com/store/apps/details?id=" + appPackageName);
               /* shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey, check out " + s_name + " on BuyAtHome app for Android at only Kshs." + s_amount
                                + " Download the app for more amazing deals: "
                                + "https://play.google.com/store/apps/details?id=" + appPackageName);*/
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BuyAtHome App for Android");
                startActivity(Intent.createChooser(shareIntent, "share..."));
            }
        });
    }

    private void composeViews() {
        //image views
        thumbNail = (ImageView) findViewById(R.id.img1);
        UniversalImageLoader imgLoader = new UniversalImageLoader();
        imgLoader.setUpDisplayImageView(thumbNail, image_url, R.drawable.product_placeholder);
        tvDescription = (TextView) findViewById(R.id.description_label);
        tvDescription.setText(s_description);
        tvAmount = (TextView) findViewById(R.id.amount_label);
        if (s_amount.equals("")) {
            tvAmount.setText("FREE");
        } else {
            double amount = Double.parseDouble(s_amount);
            DecimalFormat formatter = new DecimalFormat("#,###.00");

            tvAmount.setText("kshs. " + formatter.format(amount));
        }

        Linkify.addLinks(tvDescription, Linkify.WEB_URLS);
    }
}
