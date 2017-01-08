package com.softtech.stevekamau.buyathome.activites;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dd.processbutton.iml.ActionProcessButton;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
    public static final String FIREBASE_URL = "https://buyathome-cc661.firebaseio.com/";
    private static final int RC_SIGN_IN = 007;
    private static final String TAG = "Login";
    EditText ed_email, ed_password;
    SweetAlertDialog pDialog;
    String email, password;
    String s_name, products_jArray;
    Boolean success;
    View view;
    CallbackManager callbackManager;
    ActionProcessButton button;
    com.softtech.stevekamau.buyathome.helper.BtnFacebook loginButton;
    com.softtech.stevekamau.buyathome.helper.BtnGoogleplus gmail_btn;
    String main_data_response;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent i = getIntent();
        main_data_response = i.getStringExtra("main_data_response");

        //facebook sign in
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        // final Firebase ref = new Firebase(FIREBASE_URL);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (com.softtech.stevekamau.buyathome.helper.BtnFacebook) findViewById(R.id.f_login_button);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        String result = "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken();
                        Log.d("facebook", result);

                        Snackbar.make(findViewById(android.R.id.content), result, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        // Facebook Email address
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        Log.v("LoginActivity Response ", response.toString());

                                        try {
                                            String Name = object.getString("name");

                                            String FEmail = object.getString("email");
                                            Log.v("Email = ", " " + FEmail);

                                            SharedPreferences sharedPreferences = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("name", Name);
                                            editor.putString("email", FEmail);
                                            editor.putBoolean("islogged", true);
                                            editor.putBoolean("isemail", false);
                                            editor.putBoolean("isfacebook", true);
                                            editor.putBoolean("isgoogle", false);
                                            editor.apply();

                                            registerSocialUsers(Name, FEmail, "");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Snackbar.make(findViewById(android.R.id.content), "Could not connect, please retry later", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Snackbar.make(findViewById(android.R.id.content), "Could not connect, please retry later", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile", "email"));
            }
        });

        //google sign in
        gmail_btn = (com.softtech.stevekamau.buyathome.helper.BtnGoogleplus) findViewById(R.id.sign_in_button);
        gmail_btn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        ed_email = (EditText) findViewById(R.id.email);
        ed_password = (EditText) findViewById(R.id.password);

        button = (ActionProcessButton) findViewById(R.id.btnSignIn);
        button.setMode(ActionProcessButton.Mode.PROGRESS);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);*/
                hideSoftKeyboard(Login.this);
                email = ed_email.getText().toString().trim();
                password = ed_password.getText().toString().trim();

                if (password.length() > 0) {
                    if (!validateEmail(email)) {
                        Snackbar.make(findViewById(android.R.id.content), "Not a valid email", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        button.setMode(ActionProcessButton.Mode.ENDLESS);
                        button.setProgress(1);
                        login(email, password);
                    }

                } else {
                    button.setProgress(0);
                    Snackbar.make(findViewById(android.R.id.content), "Please enter details", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        (findViewById(R.id.btnLinkToRegisterScreen)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                i.putExtra("main_data_response", main_data_response);
                startActivity(i);
                finish();
            }
        });

    }

    private boolean validateEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    private void signIn() {
        loadingDialog();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d(TAG, "handleSignInResult:" + acct.getEmail() + acct.getDisplayName());
            login(acct.getDisplayName(), "");
            SharedPreferences sharedPreferences = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", acct.getDisplayName());
            editor.putString("email", acct.getEmail());
            editor.putBoolean("islogged", true);
            editor.putBoolean("isemail", false);
            editor.putBoolean("isfacebook", false);
            editor.putBoolean("isgoogle", true);
            editor.apply();
            registerSocialUsers(acct.getDisplayName(), acct.getEmail(), "");
        } else {
            pDialog.dismissWithAnimation();
            Snackbar.make(findViewById(android.R.id.content), "Something went wrong, retry later", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void loadingDialog() {
        pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Signing in...");
        pDialog.show();
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.green_color));
    }

    private void startMainActivity(String displayName) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.putExtra("message", displayName);
        intent.putExtra("main_data_response", main_data_response);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void login(final String email, String password) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private String login_url = getResources().getString(R.string.server_url) + "login.php";
            private Dialog loadingDialog;

            //String login_url = "http://10.0.3.2/buyathome/userdetails/login.php";
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loadingDialog = ProgressDialog.show(Login.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];
                String pass = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("user_email", uname));
                nameValuePairs.add(new BasicNameValuePair("user_pass", pass));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(login_url);
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
                    s_name = jsonObject.getString("name");
                    success = jsonObject.getBoolean("success");
                    products_jArray = jsonObject.getString("products");

                    Log.d("json_name", s_name);
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
            }

            @Override
            protected void onPostExecute(String result) {
//                String s = result.trim();
//                loadingDialog.dismiss();
                if (success == null) {
                    button.setProgress(0);
                    Snackbar.make(findViewById(android.R.id.content), "Our servers are down for maintainance", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {
                    if (success) {
                        SharedPreferences sharedPreferences = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", s_name);
                        editor.putString("email", email);
                        editor.putBoolean("islogged", true);
                        editor.putBoolean("isfacebook", false);
                        editor.putBoolean("isgoogle", false);
                        editor.putBoolean("isemail", true);
                        editor.apply();
                        startMainActivity(s_name);
                    } else {
                        button.setProgress(0);
                        Snackbar.make(findViewById(android.R.id.content), "Invalid email or password", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }

            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(email, password);

    }

    public void registerSocialUsers(final String displayName, final String email, final String s) {
        loadingDialog();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.server_url) + "register.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response_regis", response);
                        pDialog.dismissWithAnimation();
                        startMainActivity(displayName);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("response_regis", error.toString());
                        pDialog.dismissWithAnimation();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", displayName);
                params.put("user_email", email);
                params.put("user_pass", s);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        pDialog.dismissWithAnimation();
        Snackbar.make(findViewById(android.R.id.content), "Could not connect, please retry later", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
