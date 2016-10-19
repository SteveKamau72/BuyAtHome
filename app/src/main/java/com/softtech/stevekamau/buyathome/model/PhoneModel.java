package com.softtech.stevekamau.buyathome.model;

/**
 * Created by steve on 7/2/16.
 */
public class PhoneModel {
    String name, amount, image_url, details, tags;
    int id, ratings;

    public PhoneModel() {
    }

    public PhoneModel(String name, String amount, String image_url, String details, String tags, int id, int ratings) {
        this.name = name;
        this.amount = amount;
        this.image_url = image_url;
        this.details = details;
        this.tags = tags;
        this.id = id;
        this.ratings = ratings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }
}
