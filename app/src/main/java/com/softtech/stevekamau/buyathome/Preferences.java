package com.softtech.stevekamau.buyathome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.softtech.stevekamau.buyathome.activites.Login;


public class Preferences extends ActionBarActivity implements GoogleApiClient.OnConnectionFailedListener {
    private LinearLayout layout;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("Settings");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout1);
        LinearLayout layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "buyathome2015@gmail.com", null));
                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback for BuyAtHome");
                i.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(i, "Send via email"));
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutDialog();
            }
        });

        LinearLayout layout3 = (LinearLayout) findViewById(R.id.layout3);
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "stevekamau72@gmail.com", null));
                i.putExtra(Intent.EXTRA_SUBJECT, "Contacting Developer");
                i.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(i, "Send via email"));
            }
        });

        (findViewById(R.id.layout4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        TextView view = (TextView) findViewById(R.id.signed);
        view.setText("Signed in as " + getuser_name());
    }

    private void logout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Preferences.this);
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("User will be logged out");

        // Setting Positive "Retry" Button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (googleaccount()) {
                    signOut();
                } else if (facebookaccount()) {

                }
                SharedPreferences spref2 = getSharedPreferences(
                        "ACCOUNT", MODE_PRIVATE);
                SharedPreferences.Editor editor = spref2.edit();
                editor.putBoolean("islogged", false);
                editor.commit();
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.putExtra("message", "Successfully logged out");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Setting Negative "Quit" Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    String getuser_name() {
        SharedPreferences spref2 = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        String user_name = spref2.getString("name", "");
        return user_name;
    }

    Boolean googleaccount() {
        SharedPreferences spref2 = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        Boolean loggedaccount = spref2.getBoolean("isgoogle", false);
        return loggedaccount;
    }

    Boolean facebookaccount() {
        SharedPreferences spref2 = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        Boolean loggedaccount = spref2.getBoolean("isfacebook", false);
        return loggedaccount;
    }

    Boolean emailaccount() {
        SharedPreferences spref2 = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        Boolean loggedaccount = spref2.getBoolean("isemail", false);
        return loggedaccount;
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }

    private void aboutDialog() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.about, null);
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(layout);
        adb.setCancelable(false);

        adb.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        adb.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preferences, menu);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
