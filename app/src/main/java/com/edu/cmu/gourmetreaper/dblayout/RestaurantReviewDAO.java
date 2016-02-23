/**
 * This class is the interface of Restaurant Review Data Access Object
 */
package com.edu.cmu.gourmetreaper.dblayout;

import com.edu.cmu.gourmetreaper.entities.RestaurantReview;

import java.util.List;

/**
 * @author Song Xiong
 * Team: Gourmet Reapers
 */
public interface RestaurantReviewDAO {

    /**
     * Insert a new restaurant review into the database
     * @param restaurantReview new restaurant review adding into the database
     */
    public void insertRestaurantReview(RestaurantReview restaurantReview);

    /**
     * Get all the restaurant reviews sorted by time, from latest to oldest
     * @return all the restaurant reviews sorted by time, from latest to oldest
     */
    public List<RestaurantReview> getAllRestaurantReview();

    /**
     * Get a restaurant review by a given ID
     * @param id id of the restaurant review
     * @return a restaurant review of a given ID
     */
    public RestaurantReview getRestaurantReviewByID(int id);

    /**
     * Update a restaurant review by a given id
     * @param id id of the restaurant review to update
     * @param restaurantReview the restaurant review to update, unchanged field should be filled in
     *                         with original value
     */
    public void updateRestaurantReviewByID(int id, RestaurantReview restaurantReview);

    /**
     * Delete a restaurant review by a given id
     * @param id id of the restaurant review to delete
     */
    public void deleteRestaurantReviewByID(int id);
}
