package com.softtech.stevekamau.buyathome.activites;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.softtech.stevekamau.buyathome.R;
import com.softtech.stevekamau.buyathome.helper.AccountSharedPreferences;

public class SplashActivity extends AppCompatActivity {
    TextView txt1, txt2;
    //String url2 = "http://snapt.t15.org/buyathome/all_products.php";
    String new_products, recommended_products, featured_products;
    int greenHeight = 0;
    com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar cp;
    AccountSharedPreferences shp;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_splash);
        shp = new AccountSharedPreferences(this);
        txt1 = (TextView) findViewById(R.id.txt1);
        cp = (com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar) findViewById(R.id.progressBar);
        cp.setCircleBackgroundEnabled(false);
        cp.setColorSchemeResources(R.color.green_color);
        mainRequest();


    }

    private void loadMainData(final String response) {

        SharedPreferences sharedPreferences = getSharedPreferences("ACCOUNT", MODE_PRIVATE);
        Boolean islogged = sharedPreferences.getBoolean("islogged", false);
        if (islogged) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("main_data_response", response);
            startActivity(i);
            finish();

        } else {
            Intent i = new Intent(getApplicationContext(), Login.class);
            i.putExtra("main_data_response", response);
            startActivity(i);
            finish();
        }

    }

    private void mainRequest() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(getResources().getString(R.string.server_url) + "all_products.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                (findViewById(R.id.error)).setVisibility(View.INVISIBLE);
                cp.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                Log.d("splash_screen_req", s);
                System.out.print(s);
                cp.setVisibility(View.INVISIBLE);
                // Parsing json
                shp.setProductList(s);
                if (!s.equals("")) {
                    loadMainData(s);
                } else {
                    displayRetry();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                displayRetry();

            }


        });
    }

    private void displayRetry() {
        cp.setVisibility(View.INVISIBLE);
        (findViewById(R.id.error)).setVisibility(View.VISIBLE);
        (findViewById(R.id.retry)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainRequest();
            }
        });

        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.cont);
        final LinearLayout ly = (LinearLayout) findViewById(R.id.linear);
        final ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < 16) {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                greenHeight = 100;

            }
        });
        mHandler.postDelayed(new Runnable() {
            public void run() {

                AnimatorSet set = new AnimatorSet();
                set.playTogether(
                        // animate from on-screen and out
                        ObjectAnimator.ofFloat(ly, "translationY", 0, -greenHeight),
                        ObjectAnimator.ofFloat(ly, "alpha", 1, 1, 1)
                        // add other animations if you wish
                );
                set.setStartDelay(0);
                set.setDuration(2000).start();
            }
        }, 0);

    }
}



