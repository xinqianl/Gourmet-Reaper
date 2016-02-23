/**
 * This class represents the model of a cuisine category
 * e.g. Appetizers, Drinks, Desserts
 */
package com.edu.cmu.gourmetreaper.entities;

import java.util.List;

/**
 * @author Song Xiong
 * Team: Gourmet Reapers
 */

public class CuisineCategory {
    private int categoryID; // Category ID
    private String categoryName; // Name of the category
    private List<Cuisine> cuisines; // A list of cuisines in this category

    // Getters and setters
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Cuisine> getCuisines() {
        return cuisines;
    }

    public void setCuisines(List<Cuisine> cuisines) {
        this.cuisines = cuisines;
    }
}
