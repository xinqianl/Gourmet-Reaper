/**
 * This class is the interface of Cuisine Data Access Object
 */
package com.edu.cmu.gourmetreaper.dblayout;

import com.edu.cmu.gourmetreaper.entities.Cuisine;

import java.util.List;

/**
 * @author Song Xiong
 * Team: Gourmet Reapers
 */
public interface CuisineDAO {

    /**
     * Insert a new cuisine into the database, it does not contains reviews of this cuisine
     * @param cuisine new cuisine adding into the database
     */
    public void insertCuisine(Cuisine cuisine);

    /**
     * Insert a new cuisine with its category into the database,
     * it does not contains reviews of this cuisine
     * @param cuisine new cuisine adding into the database
     * @param cuisineCategoryID category id of the category that this cuisine belongs to
     */
    public void insertCuisineWithCategory(Cuisine cuisine, int cuisineCategoryID);

    /**
     * Get all the cuisines sorted by id, it does not contains the reviews of each cuisine
     * please use getAllReviewsByCuisineID in CuisineReviewDAO to do that.
     * @return all the cuisine categories sorted by id
     */
    public List<Cuisine> getAllCuisine();

    /**
     * Get all the cuisines of a given category sorted by id, it does not contains the reviews
     * of each cuisine, please use getAllReviewsByCuisineID in CuisineReviewDAO to do that.
     * @param cuisineCategoryID category id of the category that these cuisines belong to
     * @return all the cuisine categories sorted by id of a given category
     */
    public List<Cuisine> getAllCuisineByCategoryID(int cuisineCategoryID);

    /**
     * Get a cuisine by a given ID
     * @param id id of the cuisine
     * @return a cuisine of a given ID
     */
    public Cuisine getCuisineByID(int id);

    /**
     * Get a cuisine by a given cuisine name
     * @param name name of the cuisine
     * @return a cuisine of a given name
     */
    public Cuisine getCuisineByName(String name);

    /**
     * Update cuisine by a given id
     * @param id id of the cuisine to update
     * @param cuisine the cuisine to update, unchanged field should be filled in with
     *                original value
     * @param cuisineCategoryID the category id of the category to which this cuisine belongs
     */
    public void updateCuisineByID(int id, Cuisine cuisine, int cuisineCategoryID);

    /**
     * Delete a cuisine by a given id
     * @param id id of the cuisine to delete
     */
    public void deleteCuisineByID(int id);
}
