/**
 * This class represents the model of a cuisine
 * e.g. Cheeseburger, Fried Rice
 */
package com.edu.cmu.gourmetreaper.entities;

import android.graphics.Bitmap;

import java.util.List;

/**
 * @author Song Xiong
 * Team: Gourmet Reapers
 */
public class Cuisine {

    private int cuisineID; // cuisine ID
    private String cuisineName; // name of cuisine
    private String cuisineDescription; // description of cuisine
    private double price; // price of cuisine
    private List<CuisineReview> reviews; // a list of reviews for this cuisine
    private Bitmap image; // image of this cuisine

    // Getters and setters

    public int getCuisineID() {
        return cuisineID;
    }

    public void setCuisineID(int cuisineID) {
        this.cuisineID = cuisineID;
    }

    public String getCuisineName() {
        return cuisineName;
    }

    public void setCuisineName(String cuisineName) {
        this.cuisineName = cuisineName;
    }

    public String getCuisineDescription() {
        return cuisineDescription;
    }

    public void setCuisineDescription(String cuisineDescription) {
        this.cuisineDescription = cuisineDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public List<CuisineReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<CuisineReview> reviews) {
        this.reviews = reviews;
    }
}
