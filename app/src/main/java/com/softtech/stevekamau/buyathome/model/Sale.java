package com.softtech.stevekamau.buyathome.model;

/**
 * Created by steve on 9/8/16.
 */

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by steve on 9/8/16.
 */
@IgnoreExtraProperties
public class Sale {

    public String uid;
    public String name;
    public String email;
    public String phone;
    public String payment_mode;
    public String total_amount;
    public String shipping_mode;
    public String city;
    public String nearest_landmark;
    public String product_name;
    public String product_amount;
    public String product_id;
    public String time;


    public Sale() {
        // Default constructor required for calls to DataSnapshot.getValue(Sale.class)
    }

    public Sale(String uid, String name, String email/*, String payment_mode, String total_amount,
                String shipping_mode, String city, String nearest_landmark, String product_name,
                String product_amount, String product_id, String time*/) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.payment_mode = payment_mode;
        this.total_amount = total_amount;
        this.shipping_mode = shipping_mode;
        this.city = city;
        this.nearest_landmark = nearest_landmark;
        this.product_name = product_name;
        this.product_amount = product_amount;
        this.product_id = product_id;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
