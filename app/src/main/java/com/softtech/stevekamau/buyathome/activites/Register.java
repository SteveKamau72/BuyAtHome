package com.softtech.stevekamau.buyathome.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.softtech.stevekamau.buyathome.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 3/17/16.
 */
public class Register extends AppCompatActivity {
    EditText ed_name, ed_email, ed_password;
    String name, email, password, message;
    ActionProcessButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ed_name = (EditText) findViewById(R.id.name);
        ed_email = (EditText) findViewById(R.id.email);
        ed_password = (EditText) findViewById(R.id.password);
        button = (ActionProcessButton) findViewById(R.id.btnRegister);
        button.setMode(ActionProcessButton.Mode.PROGRESS);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = ed_name.getText().toString().trim();
                email = ed_email.getText().toString().trim();
                password = ed_password.getText().toString().trim();
                if (name.length() > 0 && email.length() > 0 && password.length() > 0) {
                    button.setMode(ActionProcessButton.Mode.ENDLESS);
                    button.setProgress(1);
                    String method = "register";
                    BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
                    backgroundTask.execute(method, name, email, password);

                } else {
                    button.setProgress(0);
                    Snackbar.make(findViewById(android.R.id.content), "Please enter details", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        (findViewById(R.id.btnLinkToLoginScreen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

    public class BackgroundTask extends AsyncTask<String, Void, String> {
        Context ctx;
        Boolean success;
        View view;

        BackgroundTask(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://snapt.t15.org/buyathome/register.php";
            //String reg_url = "http://10.0.3.2/buyathome/userdetails/register.php";
            String method = params[0];
            if (method.equals("register")) {
                String name = params[1];
                String email = params[2];
                String password = params[3];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("user", name));
                nameValuePairs.add(new BasicNameValuePair("user_email", email));
                nameValuePairs.add(new BasicNameValuePair("user_pass", password));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(reg_url);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                    Log.d("json_results", result);

                    JSONObject jsonObject = new JSONObject(result);
                    success = jsonObject.getBoolean("success");
                    message = jsonObject.getString("message");
                    // Log.d("json_name", s_name);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return result;

            } else if (method.equals("register")) {

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            if (success == null) {
                Snackbar.make(findViewById(android.R.id.content), "Our servers are down for maintainance", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            } else {
                if (success) {
                    SharedPreferences sharedPreferences = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.putBoolean("islogged", true);
                    editor.apply();
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    intent.putExtra("message", name);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    button.setProgress(0);
                    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }
    }

}
