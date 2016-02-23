/**
 * This class is the interface of Cuisine Category Data Access Object
 */
package com.edu.cmu.gourmetreaper.dblayout;

import com.edu.cmu.gourmetreaper.entities.CuisineCategory;

import java.util.List;

/**
 * @author Song Xiong
 * Team: Gourmet Reapers
 */
public interface CuisineCategoryDAO {

    /**
     * Insert a new cuisine category into the database, it only contains a category name
     * @param cuisineCategory new cuisine category adding into the database
     */
    void insertCuisineCategory(CuisineCategory cuisineCategory);

    /**
     * Get all the cuisine category sorted by id, it does not contains the cuisines of every category,
     * please use getAllCuisineByCategoryID in cuisineDAO to do that.
     * @return all the cuisine categories sorted by id
     */
    List<CuisineCategory> getAllCuisineCategory();

    /**
     * Get a cuisine category by a given ID
     * @param id id of the cuisine category
     * @return a cuisine category of a given ID
     */
    CuisineCategory getCuisineCategoryByID(int id);

    /**
     * Get a cuisine category by a given category name
     * @param name category name of the cuisine category
     * @return a cuisine category of a given category name
     */
    CuisineCategory getCuisineCategoryByName(String name);

    /**
     * Update the name of a cuisine category by a given id
     * @param id id of the cuisine category to update
     * @param newName the new name of this cuisine category
     */
    void updateCuisineCategoryNameByID(int id, String newName);

    /**
     * Delete a cuisine category by a given id
     * @param id id of the cuisine category to delete
     */
    void deleteCuisineCategoryByID(int id);
}
