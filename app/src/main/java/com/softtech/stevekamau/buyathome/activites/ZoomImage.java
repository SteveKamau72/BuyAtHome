package com.softtech.stevekamau.buyathome.activites;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.helper.UniversalImageLoader;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ZoomImage extends ActionBarActivity {
    ImageView back;
    PhotoViewAttacher mAttacher;
    String image_url;
    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        Bundle extras = getIntent().getExtras();
        image_url = extras.getString("image_url");
        imageView = (ImageView) findViewById(R.id.img2);

        mAttacher = new PhotoViewAttacher(imageView);

        UniversalImageLoader imgLoader = new UniversalImageLoader();
        imgLoader.setUpDisplayImageView(imageView, image_url, R.drawable.product_placeholder);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zoom_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
