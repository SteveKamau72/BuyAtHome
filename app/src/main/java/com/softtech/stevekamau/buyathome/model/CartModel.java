package com.softtech.stevekamau.buyathome.model;

/**
 * Created by Steve Kamau on 4/12/2015.
 */
public class CartModel {
    Integer id;
    String title;
    Integer amount;
    String description;
    String imageFromPath;
    String quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageFromPath() {
        return imageFromPath;
    }

    public void setImageFromPath(String imageFromPath) {
        this.imageFromPath = imageFromPath;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
