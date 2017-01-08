package com.softtech.stevekamau.buyathome.model;

/**
 * Created by steve on 3/20/16.
 */
public class RecommendedModel {
    String name, amount, image_url, details, tags, discounted_amount;
    int id, ratings;

    public RecommendedModel(String name, String amount, String image_url, String details, String tags, int id, int ratings) {
        this.name = name;
        this.amount = amount;
        this.image_url = image_url;
        this.details = details;
        this.tags = tags;
        this.id = id;
        this.ratings = ratings;
    }

    public RecommendedModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    // public NewModel(String name, String amount, String image_url, String details, String tags, int id, int ratings) {

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

    public String getDiscountedAmount() {
        return discounted_amount;
    }

    public void setDiscountedAmount(String discounted_amount) {
        this.discounted_amount = discounted_amount;
    }
}
