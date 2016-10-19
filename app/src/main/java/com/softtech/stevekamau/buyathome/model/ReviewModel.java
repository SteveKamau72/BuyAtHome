package com.softtech.stevekamau.buyathome.model;

/**
 * Created by steve on 3/24/16.
 */
public class ReviewModel {
    String name, comment, time_stamp;

    public ReviewModel() {

    }

    public ReviewModel(String name, String comment, String time_stamp) {
        this.name = name;
        this.comment = comment;
        this.time_stamp = time_stamp;
    }

    public String getUserReviewName() {
        return name;
    }

    public void setUserReviewName(String name) {
        this.name = name;
    }

    public String getcomment() {
        return comment;
    }

    public void setcomment(String comment) {
        this.comment = comment;
    }

    public String gettime_stamp() {
        return time_stamp;
    }

    public void settime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

}
