package com.softtech.stevekamau.buyathome.model;

/**
 * Created by steve on 12/27/16.
 */

public class FeaturedModel {
    String name, amount, image_url, details, tags;
    int id, ratings;
    String imageCode1,imageCode2,imageCode3,imageCode4;

    public FeaturedModel() {
    }

    public FeaturedModel(String name, String amount, String image_url, String details, String tags, int id, int ratings) {
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

    public String getImageCode1() {
        return imageCode1;
    }

    /**
     *
     * @param imageCode1
     * The image_code1
     */
    public void setImageCode1(String imageCode1) {
        this.imageCode1 = imageCode1;
    }

    /**
     *
     * @return
     * The imageCode2
     */
    public String getImageCode2() {
        return imageCode2;
    }

    /**
     *
     * @param imageCode2
     * The image_code2
     */
    public void setImageCode2(String imageCode2) {
        this.imageCode2 = imageCode2;
    }

    /**
     *
     * @return
     * The imageCode3
     */
    public String getImageCode3() {
        return imageCode3;
    }

    /**
     *
     * @param imageCode3
     * The image_code3
     */
    public void setImageCode3(String imageCode3) {
        this.imageCode3 = imageCode3;
    }

    /**
     *
     * @return
     * The imageCode4
     */
    public String getImageCode4() {
        return imageCode4;
    }

    /**
     *
     * @param imageCode4
     * The image_code4
     */
    public void setImageCode4(String imageCode4) {
        this.imageCode4 = imageCode4;
    }
}


