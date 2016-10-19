package com.softtech.stevekamau.buyathome.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.app.AppController;

public class FeaturedDetails extends AppCompatActivity {
    ImageLoader imageLoader;
    String bitmap, p_id, message, url;
    String s_name, s_amount, s_description;
    NetworkImageView thumbNail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);/*
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        Intent i = getIntent();
        imageLoader = AppController.getInstance().getImageLoader();
        p_id = i.getStringExtra("p_id");
        bitmap = i.getStringExtra("image1_url");
        s_name = i.getStringExtra("title");
        s_amount = i.getStringExtra("amount");
        s_description = i.getStringExtra("description");

        composeViews();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void composeViews() {
        //image views
        thumbNail = (NetworkImageView) findViewById(R.id.img1);
        thumbNail.setImageUrl(bitmap, imageLoader);
    }
}
