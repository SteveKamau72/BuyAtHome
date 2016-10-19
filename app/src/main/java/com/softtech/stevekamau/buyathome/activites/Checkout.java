package com.softtech.stevekamau.buyathome.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softtech.stevekamau.buyathome.NameMail;
import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.SharedPreference;
import com.softtech.stevekamau.buyathome.TopExceptionHandler;
import com.softtech.stevekamau.buyathome.helper.SQLiteHandler;
import com.softtech.stevekamau.buyathome.helper.SessionManager;

import java.util.HashMap;


public class Checkout extends ActionBarActivity {
    Button btnCheck, button4;
    EditText ed1, ed2, ed3, ed4;
    TextView tv;
    Activity context = this;
    private TextView txtName;
    private TextView txtEmail;
    private EditText textEtxt, textEtxt1, textEtxt2, textEtxt3;
    private SQLiteHandler db;
    private SessionManager session;
    private String text;
    private SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("Check Out");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ed1 = (EditText) findViewById(R.id.editText1);
        ed2 = (EditText) findViewById(R.id.editText2);
        ed3 = (EditText) findViewById(R.id.editText3);
        ed4 = (EditText) findViewById(R.id.editText4);
        tv = (TextView) findViewById(R.id.message);
        button4 = (Button) findViewById(R.id.button4);

        sharedPreference = new SharedPreference();
        findViewsById();

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            button4.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            ed1.setVisibility(View.INVISIBLE);
            ed2.setVisibility(View.INVISIBLE);
            ed3.setVisibility(View.INVISIBLE);
            ed4.setVisibility(View.INVISIBLE);
        } else {
            button4.setVisibility(View.INVISIBLE);
            tv.setVisibility(View.INVISIBLE);
        }


        if (db.checkForTables()) {
            // Fetching user details from sqlite
            HashMap<String, String> user = db.getUserDetails();

            String name = user.get("name");
            String email = user.get("email");

            // Displaying the user details on the screen
            ed1.setText(name);
            ed2.setText(email);

        } else {
            Intent intent = new Intent(getApplicationContext(), NameMail.class);
            startActivity(intent);
        }

        btnCheck = (Button) findViewById(R.id.btnCheckout);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Checkout.this, RegisterActivity.class);
                startActivity(i);

            }
        });
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if (textEtxt.getText().length() == 0) ;
                if (textEtxt1.getText().length() == 0) ;
                if (textEtxt2.getText().length() == 0) ;
                if (textEtxt3.getText().length() == 0) {
                    textEtxt.setError("Please enter your full name");
                    textEtxt1.setError("Please enter email");
                    textEtxt2.setError("Please enter phone number");
                    textEtxt3.setError("Please enter your location");
                } else {
                    text = "Name: " + textEtxt.getText().toString() + "\n" +
                            "Email: " + textEtxt1.getText().toString() + "\n" +
                            "Phone Number: " + textEtxt2.getText().toString() + "\n" +
                            "Residence: " + textEtxt3.getText().toString();
                    sharedPreference.save(context, text);
                    Intent intent = new Intent(Checkout.this, PaymentAndShipping.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void findViewsById() {
        textEtxt = (EditText) findViewById(R.id.editText1);
        textEtxt1 = (EditText) findViewById(R.id.editText2);
        textEtxt2 = (EditText) findViewById(R.id.editText3);
        textEtxt3 = (EditText) findViewById(R.id.editText4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
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
            Intent settingsActivity = new Intent(getBaseContext(),
                    Preferences.class);
            startActivity(settingsActivity);
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
