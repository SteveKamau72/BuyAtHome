package com.softtech.stevekamau.buyathome.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.helper.AccountSharedPreferences;

public class ThankYou extends AppCompatActivity {
    AccountSharedPreferences asp;
    TextView tvReceipt_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_thank_you);
        Intent i = getIntent();
        String receipt_code = i.getStringExtra("receipt_code");
        tvReceipt_code = (TextView) findViewById(R.id.receipt_code);
        tvReceipt_code.setText(receipt_code);
        asp = new AccountSharedPreferences(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setColorNormal(getResources().getColor(R.color.green_color));
        fab.setColorPressed(getResources().getColor(R.color.green_color_dark));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("main_data_response", asp.getProductList());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("main_data_response", asp.getProductList());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
