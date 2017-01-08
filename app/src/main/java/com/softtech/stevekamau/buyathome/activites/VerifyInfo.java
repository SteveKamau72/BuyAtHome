package com.softtech.stevekamau.buyathome.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.app.AppConfig;
import com.softtech.stevekamau.buyathome.databaseHandlers.CartDB;
import com.softtech.stevekamau.buyathome.helper.AccountSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class VerifyInfo extends AppCompatActivity {
    Button edit_payment, edit_personal_button, edit_shipping_button;
    EditText ed_phone, ed_city, ed_landmark, ed_name, ed_email;
    String s_phone, s_city, s_landmark, s_name, s_email, s_payment_details, s_shipping_details, total_amount;
    TextView tvName, tvEmail, tvPhone, tvCity, tvLand, tvAmount, tvPayment, tvShipping, tvTamount;
    SharedPreferences sharedPreferences;
    CartDB cartDB;
    AccountSharedPreferences asp;
    SweetAlertDialog pDialog;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPhone, inputLayoutCity, inputLayoutLandMark;
    private DatabaseReference mDatabase;
    private int i = -1;

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("Verify Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cartDB = new CartDB(this);
        //convert sqlite rows into Json fields
        Gson gson = new GsonBuilder().create();
        Log.d("json_array", gson.toJson(cartDB.getSalesSold()));
        asp = new AccountSharedPreferences(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("sales").push();

        Intent i = getIntent();
        s_payment_details = i.getStringExtra("payment_details");
        s_shipping_details = i.getStringExtra("shipping_details");
        total_amount = i.getStringExtra("total_amount");
        asp.setShippingMode(s_shipping_details);
        asp.setPaymentMode(s_payment_details);
        sharedPreferences = getSharedPreferences("ACCOUNT", MODE_PRIVATE);

        s_phone = sharedPreferences.getString("phone", "");
        s_city = sharedPreferences.getString("city", "");
        s_landmark = sharedPreferences.getString("landmark", "");
        s_name = sharedPreferences.getString("name", "");
        s_email = sharedPreferences.getString("email", "");
        createViews();

    }

    private void createViews() {
        edit_personal_button = (Button) findViewById(R.id.edit_personal_button);
        edit_payment = (Button) findViewById(R.id.edit_payment_button);
        edit_shipping_button = (Button) findViewById(R.id.edit_shipping_button);
        edit_shipping_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        edit_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        edit_personal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPersonalInfoDialog();
            }
        });

        tvName = (TextView) findViewById(R.id.name);
        tvPhone = (TextView) findViewById(R.id.phone);
        tvEmail = (TextView) findViewById(R.id.email);
        tvAmount = (TextView) findViewById(R.id.tvamount);
        tvPayment = (TextView) findViewById(R.id.payment);
        tvShipping = (TextView) findViewById(R.id.shipping);
        tvTamount = (TextView) findViewById(R.id.amount);
        tvCity = (TextView) findViewById(R.id.city);
        tvLand = (TextView) findViewById(R.id.landmark);
        tvName.setText("Name: " + s_name);
        tvPhone.setText("Phone: " + s_phone);
        tvEmail.setText("Email: " + s_email);
        tvAmount.setText("Grand Total: kshs." + total_amount);
        tvPayment.setText(s_payment_details);
        tvShipping.setText(s_shipping_details);
        tvTamount.setText(total_amount);
        tvCity.setText("City: " + s_city);
        tvLand.setText("Nearest LandMark: " + s_landmark);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

               /*     Map<String, Sale> map = new HashMap<String, Sale>();
                    map.put(getDateTime(), new Sale("", s_name, s_email));
                    //                Map<String, Sale> map = new HashMap<String, Sale>();
                    ////                Map<String, String> map_sales = new HashMap<String, String>();
                    //                map.put(getDateTime(), new Sale("", s_name, s_email));
                    ////                map_sales.put("hello", "test");
                    //                map.put("hello", new Sale("", "test1", "test2"));
                     cartDB.getSalesSold();*/
                    // TODO: 12/6/16 set up internet check here
                    // TODO: 12/6/16 delete orders
                    // TODO: 12/6/16 Trigger push notifications here
                    loadingDialog();
                    sendPushNotification();
                    sendPurchaseDetails();
                 /*   mDatabase.setValue(cartDB.getSalesSold(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null) {
                                Log.d("databaseError", "none");
                                startActivity(new Intent(getApplicationContext(), ThankYou.class));
                            } else {
                                Log.d("databaseError", databaseError.toString());
                            }
                        }
                    });*/
                    /* mDatabase*//*.child(getDateTime())*//*.setValue(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            Log.d("databaseError", "done");
                            startActivity(new Intent(getApplicationContext(), ThankYou.class));
                        }
                    });*/

                    // Firebase usersRef = ref.child("users");
                    // database.setValue(map);
                    // Write a message to the database
                    // FirebaseDatabase database = FirebaseDatabase.getInstance();
                    // DatabaseReference myRef = database.getReference("sales/"+getDateTime());

                    //myRef.setValue("Hello, World!");

                    /*DatabaseReference alanRef = database.getReference("sales").child("getDateTime()");
                    Sale alan = new Sale("", s_name, s_email);
                    alanRef.setValue(alan);*/
     /*// Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("message");

                    myRef.setValue("Hello, World!");*/
                   /* FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("message");

                    myRef.setValue("Hello, World!");*/

                }
            });
        }

    }

    private void loadingDialog() {
        pDialog = new SweetAlertDialog(VerifyInfo.this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading");
        pDialog.show();
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.green_color));
        // you can change the progress bar color by ProgressHelper every 800 millis
        /*i++;
        switch (i) {
            case 0:
                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                break;
            case 1:
                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                break;
            case 2:
                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                break;
            case 3:
                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                break;
            case 4:
                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                break;
            case 5:
                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                break;
            case 6:
                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                break;
        }*/
    }


    private void sendPurchaseDetails() {
        new Thread() {
            public void run() {

                //convert sqlite rows into Json fields
                Gson gson = new GsonBuilder().create();
                Log.d("json_array", gson.toJson(cartDB.getSalesSold()));
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(getResources().getString(R.string.server_url) + AppConfig.ADD_PURCHASE_URL);
                    httpPost.setEntity(new StringEntity(gson.toJson(cartDB.getSalesSold())));
                    //httpPost.setHeader("Content-type", "application/json");
                    HttpResponse response = client.execute(httpPost);
                    String responseStr = EntityUtils.toString(response.getEntity());

                    Log.d("json_purchase_resp", "[" + responseStr + "]");

                    JSONObject obj = new JSONObject(responseStr);
                    Boolean success = obj.getBoolean("success");
                    //show Syncing Progress
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.dismissWithAnimation();
                        }
                    });
                    if (success) {
                        cartDB.deleteCart();
                        String receipt_code = obj.getString("receipt_code");
                        Intent i = new Intent(getApplicationContext(), ThankYou.class);
                        i.putExtra("receipt_code", receipt_code);
                        startActivity(i);

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                errorDialog();
                            }
                        });
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorDialog();
                        }
                    });
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorDialog();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorDialog();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorDialog();
                        }
                    });
                }
            }
        }.start();
    }

    private void errorDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Ooops!")
                .setContentText("Something went wrong! Please retry.")
                .setCancelText("No, cancel!")
                .setConfirmText("Yes, retry!")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        pDialog.dismissWithAnimation();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        loadingDialog();
                        sendPushNotification();
                        sendPurchaseDetails();
                    }
                })
                .show();
    }

    private void sendPushNotification() {
        new Thread() {
            public void run() {

                //show Syncing Progress
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });


                JSONObject json = new JSONObject();
                JSONObject manJson = new JSONObject();
                try {
                    manJson.put("message", "My Message");
                    json.put("to", "/topics/" + AppConfig.TOPIC_GLOBAL);
                    json.put("data", manJson);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json_str = json.toString();
                Log.d("jsorn_to_firebase", json_str);
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("https://fcm.googleapis.com/fcm/send");
                    httpPost.setEntity(new StringEntity(json_str));
//            httpPost.setHeader("Accept", "application/json");
                    httpPost.setHeader("Authorization", "key=AIzaSyBNDpxJs_fN2CGYds4k7oncyeNK7DRc8kA");
                    httpPost.setHeader("Content-type", "application/json");
                    Log.d("firebase_notif_request", "true");
                    HttpResponse response = client.execute(httpPost);
                    String responseStr = EntityUtils.toString(response.getEntity());

                    Log.d("request_made7", "reached");
                    Log.d("firebase_notif_resp", responseStr);

                    Log.d("firebase_notif_resp", "true");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {

                    //sync failed so we rest the syncing icon
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    String getDateTime() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(now);
    }

    public void showPersonalInfoDialog() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.personal_details, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Personal Details");
        adb.setView(layout);
        adb.setCancelable(false);

        ed_name = (EditText) layout.findViewById(R.id.name);
        ed_email = (EditText) layout.findViewById(R.id.email);
        ed_phone = (EditText) layout.findViewById(R.id.phone);
        ed_city = (EditText) layout.findViewById(R.id.city);
        ed_landmark = (EditText) layout.findViewById(R.id.location);
        inputLayoutName = (TextInputLayout) layout.findViewById(R.id.inputLayoutName);
        inputLayoutEmail = (TextInputLayout) layout.findViewById(R.id.inputLayoutEmail);
        inputLayoutPhone = (TextInputLayout) layout.findViewById(R.id.inputLayoutPhone);
        inputLayoutCity = (TextInputLayout) layout.findViewById(R.id.inputLayoutCity);
        inputLayoutLandMark = (TextInputLayout) layout.findViewById(R.id.inputLayoutLandMark);
        ed_phone.addTextChangedListener(new MyTextWatcher(ed_phone));
        ed_city.addTextChangedListener(new MyTextWatcher(ed_city));
        ed_landmark.addTextChangedListener(new MyTextWatcher(ed_landmark));

        ed_name.setText(s_name);
        ed_email.setText(s_email);
        ed_phone.setText(s_phone);
        ed_city.setText(s_city);
        ed_landmark.setText(s_landmark);

        adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                hideKeyboard(VerifyInfo.this);
                submitForm();
                //dialog.cancel();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", ed_name.getText().toString().trim());
                editor.putString("email", ed_email.getText().toString().trim());
                editor.putString("phone", ed_phone.getText().toString().trim());
                editor.putString("city", ed_city.getText().toString().trim());
                editor.putString("landmark", ed_landmark.getText().toString().trim());
                editor.apply();
                Snackbar.make(findViewById(android.R.id.content), "Personal details saved successfully!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        adb.show();
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }

        if (!validateCity()) {
            return;
        }

        if (!validateLand()) {
            return;
        }
    }

    private boolean validateName() {
        if (ed_name.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(ed_name);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        if (ed_email.getText().toString().trim().isEmpty()) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(ed_email);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        if (ed_phone.getText().toString().trim().isEmpty()) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(ed_phone);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateCity() {
        if (ed_city.getText().toString().trim().isEmpty()) {
            inputLayoutCity.setError(getString(R.string.err_msg_city));
            requestFocus(ed_city);
            return false;
        } else {
            inputLayoutCity.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLand() {
        if (ed_landmark.getText().toString().trim().isEmpty()) {
            inputLayoutLandMark.setError(getString(R.string.err_msg_landMark));
            requestFocus(ed_landmark);
            return false;
        } else {
            inputLayoutLandMark.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
        return super.onOptionsItemSelected(item);
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.phone:
                    validatePhone();
                    break;
                case R.id.city:
                    validateCity();
                    break;
                case R.id.location:
                    validateLand();
                    break;
            }
        }
    }
}
