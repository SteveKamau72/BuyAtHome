package com.softtech.stevekamau.buyathome.helper;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by steve on 10/19/16.
 */
public class AccountSharedPreferences {
    Activity activity;

    public AccountSharedPreferences(Activity activity) {
        this.activity = activity;
    }

    public String getProductList() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("ACCOUNT", activity.MODE_PRIVATE);
        String productList = sharedPreferences.getString("product_list", "");
        return productList;
    }

    public void setProductList(String response) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("ACCOUNT", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("product_list", response);
        editor.apply();
    }

    public String getShippingMode() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("ACCOUNT", activity.MODE_PRIVATE);
        String mode = sharedPreferences.getString("shipping_mode", "");
        return mode;
    }

    public void setShippingMode(String mode) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("ACCOUNT", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("shipping_mode", mode);
        editor.apply();
    }

    public String getPaymentMode() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("ACCOUNT", activity.MODE_PRIVATE);
        String mode = sharedPreferences.getString("payment_mode", "");
        return mode;
    }

    public void setPaymentMode(String mode) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("ACCOUNT", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("payment_mode", mode);
        editor.apply();
    }
}
