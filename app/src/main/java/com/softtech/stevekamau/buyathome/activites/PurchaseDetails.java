package com.softtech.stevekamau.buyathome.activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.adapter.PurchasedItemsAdapter;
import com.softtech.stevekamau.buyathome.app.AppConfig;
import com.softtech.stevekamau.buyathome.databaseHandlers.PurchasesDB;
import com.softtech.stevekamau.buyathome.model.PurchasesModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PurchaseDetails extends AppCompatActivity {
    TextView tv_username, tv_timestamp, tv_email, tv_phone,
            tv_receipt_Code, tv_payment, tv_shipping, tv_landmark, tv_city;
    PurchasesModel nm;
    PurchasesDB nb;
    RecyclerView recyclerView;
    List<PurchasesModel> itemList = new ArrayList<PurchasesModel>();
    String ordered_at;
    CheckBox checkBox;
    ImageView icon;
    CircleProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_details);
        Intent i = getIntent();

        nb = new PurchasesDB(this);
        nm = (PurchasesModel) i.getSerializableExtra("model_class");
        Log.d("serialized_object", nm.getPurchaseId());
        composeViews();

        Log.d("related_items", String.valueOf(nb.getRelatedPurchasedItems(nm.getReceipt_code()).size()));

    }

    private void composeViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_username = (TextView) findViewById(R.id.title);
        tv_timestamp = (TextView) findViewById(R.id.time_stamp);
        tv_phone = (TextView) findViewById(R.id.phone);
        tv_payment = (TextView) findViewById(R.id.txt_payment);
        tv_shipping = (TextView) findViewById(R.id.txt_shipping);
        tv_email = (TextView) findViewById(R.id.txt_email);
        tv_landmark = (TextView) findViewById(R.id.txt_landmark);
        tv_city = (TextView) findViewById(R.id.txt_city);
        tv_receipt_Code = (TextView) findViewById(R.id.receipt_Code);
        if (nm.getPhone().equals("")) {
            tv_phone.setText("--");
        } else {
            tv_phone.setText(nm.getPhone());
        }
        tv_email.setText(nm.getEmail());
        tv_username.setText(nm.getUser_name());
        tv_receipt_Code.setText(nm.getReceipt_code());
        if (nm.getPayment_mode().equals("")) {
            tv_payment.setText("--");
        } else {
            tv_payment.setText(nm.getPayment_mode());
        }
        if (nm.getShipping_mode().equals("")) {
            tv_shipping.setText("--");
        } else {
            tv_shipping.setText(nm.getShipping_mode());
        }
        if (nm.getLandmark().equals("")) {
            tv_landmark.setText("--");
        } else {
            tv_landmark.setText(nm.getLandmark());
        }
        if (nm.getCity().equals("")) {
            tv_city.setText("--");
        } else {
            tv_city.setText(nm.getCity());
        }

        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "h:mm a dd MMM,yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        try {
            date = inputFormat.parse(nm.getOrder_at());
            ordered_at = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_timestamp.setText(ordered_at);
        recyclerView = (RecyclerView) findViewById(R.id.notifList);
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);

        setUpAdapter();

        //contact user
        (findViewById(R.id.contact)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nm.getPhone().equals("")) {
                    callNumber();
                }
            }
        });
        //email
        (findViewById(R.id.email)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nm.getEmail().equals("")) {
                    emailUser();
                }
            }
        });

        icon = (ImageView) findViewById(R.id.icon);
        if (nm.getCompletionStatus().equals("1")) {
            icon.setImageDrawable(getResources().getDrawable(R.drawable.accent_icon));
        } else {
            icon.setImageDrawable(getResources().getDrawable(R.drawable.green_circle));
        }
    }

    private void emailUser() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, nm.getEmail());
        intent.putExtra(Intent.EXTRA_SUBJECT, "BuyAtHome Order");
        intent.putExtra(Intent.EXTRA_TEXT, "This is to follow up on your order..");

        startActivity(Intent.createChooser(intent, "Send Email"));
    }


    private void setUpAdapter() {
        new Thread() {

            public void run() {
                itemList = nb.getRelatedPurchasedItems(nm.getReceipt_code());
                //show Syncing Progress
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final PurchasedItemsAdapter itemAdapter = new PurchasedItemsAdapter
                                (itemList);
                        recyclerView.setAdapter(itemAdapter);
                        updatePurchaseStatus("view_status", "1");
                    }
                });

            }
        }.start();


    }

    private void updatePurchaseStatus(String method, String value) {
        for (int i = 0; i < itemList.size(); i++) {
            networkRequests(itemList.get(i).getPurchaseId(), method, value);
            if (method.equals("view_status")) {
                nb.updateCompletionStatus(itemList.get(i).getPurchaseId(), "1");
            }
        }
    }

    String getDateTime() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(now);
    }

    private void networkRequests(final String p_id, final String method, final String value) {
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.server_url) + AppConfig.UPDATE_PURCHASE_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response_updates", response);
                        progressBar.setVisibility(View.INVISIBLE);
                        nb.updateViewStatus(nm.getPurchaseId(), getDateTime(), nm.getUser_name());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("response_updates", error.toString());
                        errorDialog(p_id, method, value);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(PurchaseDetails.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("response_updates_id", p_id);
                if (method.equals("completion_status")) {
                    params.put("completion_status", value);
                }
                params.put("purchase_id", p_id);
                params.put("type", method);
                params.put("viewed_at", getDateTime());
                params.put("viewed_by", "admin");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void callNumber() {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + nm.getPhone()));
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

    private void errorDialog(final String p_id, final String method, final String value) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Ooops!")
                .setContentText("Something went wrong! Please retry.")
                .setCancelText("No, cancel!")
                .setConfirmText("Yes, retry!")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        //pDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        networkRequests(p_id, method, value);
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_purchase_details, menu);
        checkBox = (CheckBox) menu.findItem(R.id.menuShowDue).getActionView();
        Log.d("completion_status", nm.getCompletionStatus());
        if (nm.getCompletionStatus().equals("1")) {
            checkBox.setChecked(true);
            checkBox.setText("Completed");
        } else {
            checkBox.setChecked(false);
            checkBox.setText("InComplete");
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // checkBox.setChecked(false);
                    updatePurchaseStatus("completion_status", "1");

                } else {
                    // checkBox.setChecked(true);
                    updatePurchaseStatus("completion_status", "0");
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.menuShowDue) {

        }
        return super.onOptionsItemSelected(item);
    }
}
