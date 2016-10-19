package com.softtech.stevekamau.buyathome.helper;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.softtech.stevekamau.buyathome.R;

/**
 * Created by steve on 7/18/16.
 */
public class BaseActivity extends AppCompatActivity {

    protected void setUpToolbarWithTitle(String title, boolean hasBackButton) {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayShowHomeEnabled(hasBackButton);
            getSupportActionBar().setDisplayHomeAsUpEnabled(hasBackButton);
            TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
            mToolBarTextView.setText("My Cart");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

  /*  protected void enterFromBottomAnimation() {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
    }

    protected void exitToBottomAnimation() {
        overridePendingTransition(R.anim.activity_no_animation, R.anim.activity_close_translate_to_bottom);
    }*/
}
