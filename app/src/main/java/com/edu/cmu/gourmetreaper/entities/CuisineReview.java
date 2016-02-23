/**
 * This class represents the model of a cuisine review
 * It contains rating, comment and images
 */
package com.edu.cmu.gourmetreaper.entities;

import android.graphics.Bitmap;

import java.util.List;

/**
 * @author Song Xiong
 * Team: Gourmet Reapers
 */
public class CuisineReview {
    private int cuisineReviewID; // ID of cuisine review
    private int rating; // rating as stars from 0 to 5
    private String comment; // text comment of in this review
    private List<Bitmap> images; // a set of images of this cuisine

    // Getters and setters

    public int getCuisineReviewID() {
        return cuisineReviewID;
    }

    public void setCuisineReviewID(int cuisineReviewID) {
        this.cuisineReviewID = cuisineReviewID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Bitmap> getImages() {
        return images;
    }

    public void setImages(List<Bitmap> images) {
        this.images = images;
    }
}
