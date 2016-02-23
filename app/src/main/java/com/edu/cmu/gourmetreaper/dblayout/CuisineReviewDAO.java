/**
 * This class is the interface of Cuisine Review Data Access Object
 */
package com.edu.cmu.gourmetreaper.dblayout;

import com.edu.cmu.gourmetreaper.entities.CuisineReview;

import java.util.List;

/**
 * @author Song Xiong
 * Team: Gourmet Reapers
 */
public interface CuisineReviewDAO {

    /**
     * Insert a new cuisine review into the database
     * @param cuisineReview new cuisine review adding into the database
     */
    public void insertCuisineReview(CuisineReview cuisineReview, int cuisineID);

    /**
     * Get all the cuisine reviews sorted by time, from latest to oldest
     * @return all the cuisine reviews sorted by time, from latest to oldest
     */
    public List<CuisineReview> getAllCuisineReview();

    /**
     * Get all the cuisine reviews of a given cuisine sorted by id
     * @param cuisineID cuisine id of the cuisine to which these cuisine reviews belong
     * @return all the cuisine reviews sorted by id of a given cuisine
     */
    public List<CuisineReview> getAllCuisineReviewWithCuisine(int cuisineID);

    /**
     * Get a cuisine review by a given ID
     * @param id id of the cuisine review
     * @return a cuisine review of a given ID
     */
    public CuisineReview getCuisineReviewByID(int id);

    /**
     * Delete a cuisine review by a given id
     * @param id id of the cuisine review to delete
     */
    public void deleteCuisineReviewByID(int id);
}
