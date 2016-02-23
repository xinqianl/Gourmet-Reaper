/**
 * This is the container for constants that define names for URIs, tables, and columns.
 */
package com.edu.cmu.gourmetreaper.util;

/**
 * @author Song Xiong
 * Team: Gourmet Reapers
 */
public class LighteningOrderContract {

    public LighteningOrderContract() {}

    /* Inner class that defines the table cuisine_category */
    public static abstract class CuisineCategoryTable {
        public static final String TABLE_NAME = "cuisine_category";
        public static final String COLUMN_NAME_CUISINE_CATEGORY_ID = "cuisine_category_id";
        public static final String COLUMN_NAME_CATEGORY_NAME = "category_name";
    }

    /* Inner class that defines the table cuisine */
    public static abstract class CuisineTable {
        public static final String TABLE_NAME = "cuisine";
        public static final String COLUMN_NAME_CUISINE_ID = "cuisine_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_CUISINE_CATEGORY_ID = "cuisine_category_id";
    }

    /* Inner class that defines the table cuisine_review */
    public static abstract class CuisineReviewTable {
        public static final String TABLE_NAME = "cuisine_review";
        public static final String COLUMN_NAME_CUISINE_REVIEW_ID = "cuisine_review_id";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_COMMENT = "comment";
        public static final String COLUMN_NAME_CUISINE_ID = "cuisine_id";
    }

    /* Inner class that defines the table cuisine_image */
    public static abstract class CuisineImageTable {
        public static final String TABLE_NAME = "cuisine_image";
        public static final String COLUMN_NAME_CUISINE_IMAGE_ID = "cuisine_image_id";
        public static final String COLUMN_NAME_IMAGE_DATA = "image_data";
        public static final String COLUMN_NAME_CUISINE_REVIEW_ID = "cuisine_review_id";
    }

    /* Inner class that defines the table restaurant_review */
    public static abstract class RestaurantReviewTable {
        public static final String TABLE_NAME = "restaurant_review";
        public static final String COLUMN_NAME_RESTAURANT_REVIEW_ID = "restaurant_review_id";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_COMMENT = "comment";
    }

}
