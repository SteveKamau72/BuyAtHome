package com.softtech.stevekamau.buyathome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.softtech.stevekamau.buyathome.helper.SQLiteHandler;

public class NameMail extends AppCompatActivity {
    EditText ed1, ed2, ed3;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_mail);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        mToolBarTextView.setText("You missed something");
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ed1 = (EditText) findViewById(R.id.e1);
        ed2 = (EditText) findViewById(R.id.e3);

        db = new SQLiteHandler(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_name_mail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_accept) {
            String name = ed1.getText().toString();
            String email = ed2.getText().toString();
            String password = ed2.getText().toString();

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                registerUser(name, email, password);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Please enter your details!", Toast.LENGTH_LONG)
                        .show();
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void registerUser(final String name, final String email,
                              final String password) {
        String uid = ed2.getText().toString();
        String created_at = ed2.getText().toString();

        db.addUser(name, email, uid, created_at);
    }
}
