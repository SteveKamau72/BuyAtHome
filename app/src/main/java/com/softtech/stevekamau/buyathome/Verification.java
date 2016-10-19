package com.softtech.stevekamau.buyathome;

/**
 * Created by Steve Kamau on 15-Jul-15.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.softtech.stevekamau.buyathome.activites.Checkout;
import com.softtech.stevekamau.buyathome.activites.MainActivity;
import com.softtech.stevekamau.buyathome.activites.MyCart;
import com.softtech.stevekamau.buyathome.activites.PaymentAndShipping;
import com.softtech.stevekamau.buyathome.adapter.VerificationListAdapter;
import com.softtech.stevekamau.buyathome.databaseHandlers.CartDB;
import com.softtech.stevekamau.buyathome.model.CartModel;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVWriter;

public class Verification extends ActionBarActivity {
    Button button;
    GMailSender sender;
    TextView tv1, tv2, tv3, tv4;
    ListView listV;
    CartDB myDb;
    ImageView img1, img2, img3;
    Activity context = this;
    private TextView textTxt;
    private String text;
    private SharedPreference sharedPreference;
    private ProgressDialog pDialog;

    public static void setListViewHeightBasedOnChildren(ListView listV) {
        VerificationListAdapter verificationListAdapter = (VerificationListAdapter) listV.getAdapter();
        if (verificationListAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listV.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < verificationListAdapter.getCount(); i++) {
            view = verificationListAdapter.getView(i, view, listV);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listV.getLayoutParams();
        params.height = totalHeight + (listV.getDividerHeight() * (verificationListAdapter.getCount() - 1));
        listV.setLayoutParams(params);
        listV.requestLayout();
    }

    @SuppressLint("NewApi")

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("Verification");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (checkInterNet()) {
        } else {
            showDATADisabledAlertToUser();
        }
        exportDB();
        sharedPreference = new SharedPreference();
        myDb = new CartDB(this);
        listV = (ListView) findViewById(R.id.listView);
        findViewsById();
        Intent intent = getIntent();
        final String check_box = intent.getStringExtra("Checked1");
        final String check_box1 = intent.getStringExtra("Checked2");
        final String check_box2 = intent.getStringExtra("Checked3");
        final String check_box3 = intent.getStringExtra("Checked4");

        tv1.setText("Through " + check_box);
        tv2.setText("Through " + check_box1);
        tv3.setText("Pay through  " + check_box2);
        tv4.setText("Pay through " + check_box3);
        //Retrieve a value from SharedPreference
        text = sharedPreference.getValue(context);
        textTxt.setText(text);
        img1 = (ImageView) findViewById(R.id.imageView);
        img2 = (ImageView) findViewById(R.id.imageView2);
        img3 = (ImageView) findViewById(R.id.imageView3);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Verification.this, Checkout.class);
                startActivity(i);
                finish();
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Verification.this, MyCart.class);
                startActivity(i);
                finish();
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Verification.this, PaymentAndShipping.class);
                startActivity(i);
                finish();
            }
        });

        button = (Button) findViewById(R.id.sendEmail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                new Thread() {
                    public void run() {

                        try {
                            Mail m = new Mail("buyathome2015@gmail.com", "jumia2.0");

                            String[] toArr = {"buyathome2015@gmail.com"};
                            m.setTo(toArr);
                            m.setFrom("buyathome2015@gmail.com");
                            m.setSubject("Order for BuyAtHome products");
                            m.setBody(text + "\n" + check_box + "\n" + check_box1 + "\n" + check_box2 + "\n" + check_box3);
                            m.addAttachment("/sdcard/BuyAtHome.csv");

                            if (m.send()) {
                                Toast.makeText(Verification.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Verification.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                            Log.e("MailApp", "Could not send email", e);
                        }
                    }
                }.start();
                showAlertDialog();

            }
        });
        final ArrayList<CartModel> contactList = myDb.getAllCartItems();

        VerificationListAdapter verificationListAdapter = new VerificationListAdapter(
                Verification.this, contactList);
        //adding it to the list view.
        listV = (ListView) findViewById(R.id.listView);
        listV.setAdapter(verificationListAdapter);
        setListViewHeightBasedOnChildren(listV);

        TextView txt2 = (TextView) findViewById(R.id.textView9);
        int i = myDb.getTotalOfAmount();
        txt2.setText("Total Amount: Kshs." + i);
    }

    private void findViewsById() {
        textTxt = (TextView) findViewById(R.id.name);
        tv1 = (TextView) findViewById(R.id.payment);
        tv2 = (TextView) findViewById(R.id.payment2);
        tv3 = (TextView) findViewById(R.id.mpesa);
        tv4 = (TextView) findViewById(R.id.cash);
    }

    private void showAlertDialog() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.thanks, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(layout);
        adb.setCancelable(false);

        adb.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                myDb.deleteCart();
                finish();
            }
        });
        adb.show();
    }

    private void exportDB() {

        File dbFile = getDatabasePath("MyDBName.db");
        CartDB dbhelper = new CartDB(getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "BuyAtHome.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM contacts", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    private void showDATADisabledAlertToUser() {
//Show AlertDialog to help to Enable DATA(3G)/WIFI connection
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Network is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private boolean checkInterNet() {//CHECK the WIFI/DATA(3G) connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // Check if wifi or mobile network is available or not. If any of them is
        // available or connected then it will return true, otherwise false;
        return mobile.isConnected() || wifi.isConnected();

    }
}