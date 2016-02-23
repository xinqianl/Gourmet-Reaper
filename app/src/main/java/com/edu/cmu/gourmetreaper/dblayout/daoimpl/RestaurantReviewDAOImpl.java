/**
 * Implementation class of the RestaurantReviewDAO interface
 */
package com.edu.cmu.gourmetreaper.dblayout.daoimpl;

import com.edu.cmu.gourmetreaper.dblayout.RestaurantReviewDAO;
import com.edu.cmu.gourmetreaper.entities.RestaurantReview;
import com.edu.cmu.gourmetreaper.util.DBConnector;
import com.edu.cmu.gourmetreaper.util.LighteningOrderContract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Song Xiong
 * Team: Gourmet Reapers
 */
public class RestaurantReviewDAOImpl implements RestaurantReviewDAO {

    private Context context;

    public RestaurantReviewDAOImpl(Context context) {
        this.context = context;
    }

    @Override
    public void insertRestaurantReview(RestaurantReview restaurantReview) {
        ContentValues values = new ContentValues();
        values.put(LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RATING, restaurantReview.getRating());
        values.put(LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_COMMENT, restaurantReview.getComment());

        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();
        database.insert(LighteningOrderContract.RestaurantReviewTable.TABLE_NAME, null, values);
        connector.close();
    }

    @Override
    public List<RestaurantReview> getAllRestaurantReview() {
        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();

        String[] tableColumns = {
                LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RESTAURANT_REVIEW_ID,
                LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RATING,
                LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_COMMENT,
        };

        String sortOrder =
                LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RESTAURANT_REVIEW_ID + " DESC";

        Cursor c = database.query(
                LighteningOrderContract.RestaurantReviewTable.TABLE_NAME,  // The table to query
                tableColumns,                             // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<RestaurantReview> reviews = new ArrayList<>();

        c.moveToFirst();
        while (!c.isAfterLast()) {

            // get the column index for each data item
            int restaurantReviewID = c.getInt(c.getColumnIndex(
                    LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RESTAURANT_REVIEW_ID));
            int restaurantReviewRating = c.getInt(c.getColumnIndex(
                    LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RATING));
            String restaurantReviewComment = c.getString(c.getColumnIndex(
                    LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_COMMENT));

            RestaurantReview review = new RestaurantReview();
            review.setRestaurantReviewID(restaurantReviewID);
            review.setRating(restaurantReviewRating);
            review.setComment(restaurantReviewComment);

            reviews.add(review);

            c.moveToNext();
        }

        database.close();
        return reviews;
    }

    @Override
    public RestaurantReview getRestaurantReviewByID(int id) {
        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();

        String[] tableColumns = {
                LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RESTAURANT_REVIEW_ID,
                LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RATING,
                LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_COMMENT,
        };

        String whereClause = LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RESTAURANT_REVIEW_ID + " = ?";

        String[] whereArgs = new String[] {
                String.valueOf(id)
        };

        Cursor c = database.query(
                LighteningOrderContract.RestaurantReviewTable.TABLE_NAME,  // The table to query
                tableColumns,                             // The columns to return
                whereClause,                              // The columns for the WHERE clause
                whereArgs,                                // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );


        if (c.getCount() == 0) return null;
        c.moveToFirst();

        // get the column index for each data item
        int restaurantReviewID = c.getInt(c.getColumnIndex(
                LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RESTAURANT_REVIEW_ID));
        int restaurantReviewRating = c.getInt(c.getColumnIndex(
                LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RATING));
        String restaurantReviewComment = c.getString(c.getColumnIndex(
                LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_COMMENT));

        RestaurantReview review = new RestaurantReview();
        review.setRestaurantReviewID(restaurantReviewID);
        review.setRating(restaurantReviewRating);
        review.setComment(restaurantReviewComment);

        database.close();
        return review;
    }

    @Override
    public void updateRestaurantReviewByID(int id, RestaurantReview restaurantReview) {
        ContentValues editValues = new ContentValues();
        editValues.put(LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RATING, restaurantReview.getRating());
        editValues.put(LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_COMMENT, restaurantReview.getComment());

        String selection = LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RESTAURANT_REVIEW_ID + " = ?";

        String[] selectionArgs = new String[] {
                String.valueOf(id)
        };

        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();
        database.update(LighteningOrderContract.RestaurantReviewTable.TABLE_NAME, editValues, selection, selectionArgs);
        connector.close();
    }

    @Override
    public void deleteRestaurantReviewByID(int id) {

        String selection = LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RESTAURANT_REVIEW_ID + " = ?";

        String[] selectionArgs = new String[] {
                String.valueOf(id)
        };

        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();
        database.delete(LighteningOrderContract.RestaurantReviewTable.TABLE_NAME, selection, selectionArgs);
        connector.close();
    }
}
