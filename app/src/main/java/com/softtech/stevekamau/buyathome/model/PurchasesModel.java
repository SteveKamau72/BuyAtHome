package com.softtech.stevekamau.buyathome.model;

import java.io.Serializable;

/**
 * Created by steve on 12/18/16.
 */
@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings

public class PurchasesModel implements Serializable {

    private String id;

    private String p_id;

    private String p_name;

    private String sub_total;

    private String quantity;

    private String city;

    private String email;

    private String landmark;

    private String order_at;

    private String payment_mode;

    private String phone;

    private String receipt_code;

    private String shipping_mode;

    private String user_name;
    private String purchaseId;
    private String viewedAt;
    private String viewedBy;
    private String completionStatus;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getP_id() {
        return this.p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getP_name() {
        return this.p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getSub_total() {
        return this.sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLandmark() {
        return this.landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getOrder_at() {
        return this.order_at;
    }

    public void setOrder_at(String order_at) {
        this.order_at = order_at;
    }

    public String getPayment_mode() {
        return this.payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReceipt_code() {
        return this.receipt_code;
    }

    public void setReceipt_code(String receipt_code) {
        this.receipt_code = receipt_code;
    }

    public String getShipping_mode() {
        return this.shipping_mode;
    }

    public void setShipping_mode(String shipping_mode) {
        this.shipping_mode = shipping_mode;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPurchaseId() {
        return this.purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getViewedAt() {
        return this.viewedAt;
    }

    public void setViewedAt(String viewedAt) {
        this.viewedAt = viewedAt;
    }

    public String getViewedBy() {
        return this.viewedBy;
    }

    public void setViewedBy(String viewedBy) {
        this.viewedBy = viewedBy;
    }

    public String getCompletionStatus() {
        return this.completionStatus;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }

}

