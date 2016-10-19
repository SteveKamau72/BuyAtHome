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
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.softtech.stevekamau.buyathome.Preferences;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.TopExceptionHandler;


public class PaymentAndShipping extends ActionBarActivity {
    CheckBox mpesa_CheckBox, cash_Checkbox, ship_Checkbox, pick_Checkbox;
    TextView tvAmount;
    String total_amount, payment_details, shipping_details, s_phone, s_city, s_landmark, s_name, s_email;
    SharedPreferences sharedPreferences;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPhone, inputLayoutCity, inputLayoutLandMark;
    private EditText ed_phone, ed_city, ed_landmark, ed_name, ed_email;

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("Payment And Shipping");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        total_amount = i.getStringExtra("total_amount");
        sharedPreferences = getSharedPreferences("ACCOUNT", MODE_PRIVATE);

        s_phone = sharedPreferences.getString("phone", "");
        s_city = sharedPreferences.getString("city", "");
        s_landmark = sharedPreferences.getString("landmark", "");
        s_name = sharedPreferences.getString("name", "");
        s_email = sharedPreferences.getString("email", "");

        createViews();
       /* tv1 = (TextView) findViewById(R.id.deliverTxt);
        tv2 = (TextView) findViewById(R.id.selfcollectTxt);
        tv3= (TextView) findViewById(R.id.mpesaTxt);
        tv4= (TextView) findViewById(R.id.cashTxt);
        final EditText editTill = (EditText) findViewById(R.id.editText);
        editTill.setText("178850");
        editTill.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mpesa = (Button) findViewById(R.id.button4);
        policy = (Button) findViewById(R.id.button3);
        toVerification = (Button) findViewById(R.id.btnPayment);
        toVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentAndShipping.this, Verification.class);
                intent.putExtra("Checked1",tv1.getText().toString());
                intent.putExtra("Checked2",tv2.getText().toString());
                intent.putExtra("Checked3",tv3.getText().toString());
                intent.putExtra("Checked4",tv4.getText().toString());
                startActivity(intent);
            }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliverypolicyDialog();
            }
        });
        mpesa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mpesaDialog();
            }
        });*/
    }

    private void createViews() {
        tvAmount = (TextView) findViewById(R.id.amount);
        tvAmount.setText(total_amount);
        mpesa_CheckBox = (CheckBox) findViewById(R.id.checkBox);
        cash_Checkbox = (CheckBox) findViewById(R.id.checkBox2);
        ship_Checkbox = (CheckBox) findViewById(R.id.deliver);
        pick_Checkbox = (CheckBox) findViewById(R.id.selfcollect);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payment_details != null && shipping_details != null) {
                    Intent intent = new Intent(getApplicationContext(), VerifyInfo.class);
                    intent.putExtra("payment_details", payment_details);
                    intent.putExtra("shipping_details", shipping_details);
                    intent.putExtra("total_amount", total_amount);
                    startActivity(intent);
                } else {
                    if (payment_details == null) {
                        Snackbar.make(findViewById(android.R.id.content), "Please select a payment method", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    if (shipping_details == null) {
                        Snackbar.make(findViewById(android.R.id.content), "Please select a shipping method", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                }
            }
        });
    }

    private void deliverypolicyDialog() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.deivery_policy_dialog, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(layout);
        adb.setCancelable(true);

        adb.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        adb.show();
    }

    private void mpesaDialog() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.mpesa_dialog, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(layout);
        adb.setCancelable(true);

        adb.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        adb.show();
    }

    public void onCheckBoxClicked(View v) {
        mpesa_CheckBox = (CheckBox) findViewById(R.id.checkBox);
        cash_Checkbox = (CheckBox) findViewById(R.id.checkBox2);
        ship_Checkbox = (CheckBox) findViewById(R.id.deliver);
        pick_Checkbox = (CheckBox) findViewById(R.id.selfcollect);
        switch (v.getId()) {
            case R.id.deliver:
                if (ship_Checkbox.isChecked()) {
                    pick_Checkbox.setEnabled(false);
                    shipping_details = "Ship to me";
                    showShippingDetails();

                } else {
                    ship_Checkbox.setEnabled(true);
                    pick_Checkbox.setEnabled(true);
                }
                break;
            case R.id.selfcollect:
                if (pick_Checkbox.isChecked()) {
                    ship_Checkbox.setEnabled(false);
                    shipping_details = "I'll Collect Myself";
                } else {
                    pick_Checkbox.setEnabled(true);
                    ship_Checkbox.setEnabled(true);
                }
                break;
            case R.id.checkBox:
                if (mpesa_CheckBox.isChecked()) {
                    cash_Checkbox.setEnabled(false);
                    payment_details = "Mpesa";
                    // tv3.setText("Mpesa");
                } else {
                    mpesa_CheckBox.setEnabled(true);
                    cash_Checkbox.setEnabled(true);
                }
                break;
            case R.id.checkBox2:
                if (cash_Checkbox.isChecked()) {
                    mpesa_CheckBox.setEnabled(false);
                    payment_details = "Cash On Delivery";
                    //tv4.setText("Cash on delivery");
                } else {
                    cash_Checkbox.setEnabled(true);
                    mpesa_CheckBox.setEnabled(true);
                }
                break;

        }
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
                hideKeyboard(PaymentAndShipping.this);
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

    private void showShippingDetails() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.shipping_details, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Shipping Details");
        adb.setView(layout);
        adb.setCancelable(false);
        final SharedPreferences sharedPreferences = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        String s_phone = sharedPreferences.getString("phone", "");
        String s_city = sharedPreferences.getString("city", "");
        String s_landmark = sharedPreferences.getString("landmark", "");

        ed_phone = (EditText) layout.findViewById(R.id.phone);
        ed_city = (EditText) layout.findViewById(R.id.city);
        ed_landmark = (EditText) layout.findViewById(R.id.location);
        ed_phone.setText(s_phone);
        ed_city.setText(s_city);
        ed_landmark.setText(s_landmark);

        inputLayoutPhone = (TextInputLayout) layout.findViewById(R.id.inputLayoutPhone);
        inputLayoutCity = (TextInputLayout) layout.findViewById(R.id.inputLayoutCity);
        inputLayoutLandMark = (TextInputLayout) layout.findViewById(R.id.inputLayoutLandMark);
        ed_phone.addTextChangedListener(new MyTextWatcher(ed_phone));
        ed_city.addTextChangedListener(new MyTextWatcher(ed_city));
        ed_landmark.addTextChangedListener(new MyTextWatcher(ed_landmark));

        adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

               /* */
                hideKeyboard(PaymentAndShipping.this);
                submitForm();
                //dialog.cancel();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phone", ed_phone.getText().toString().trim());
                editor.putString("city", ed_city.getText().toString().trim());
                editor.putString("landmark", ed_landmark.getText().toString().trim());
                editor.apply();
                Snackbar.make(findViewById(android.R.id.content), "Shipping details saved successfully!", Snackbar.LENGTH_LONG)
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
        if (!validatePhone()) {
            return;
        }

        if (!validateCity()) {
            return;
        }

        if (!validateLand()) {
            return;
        }

        // Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      /*  getMenuInflater().inflate(R.menu.menu_payment, menu);*/
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
