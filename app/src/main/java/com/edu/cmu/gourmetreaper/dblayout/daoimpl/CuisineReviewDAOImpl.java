/**
 * Implementation class of the CuisineReview interface
 */
package com.edu.cmu.gourmetreaper.dblayout.daoimpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.edu.cmu.gourmetreaper.dblayout.CuisineReviewDAO;
import com.edu.cmu.gourmetreaper.entities.CuisineReview;
import com.edu.cmu.gourmetreaper.util.DBConnector;
import com.edu.cmu.gourmetreaper.util.LighteningOrderContract;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Song Xiong
 * Team: Gourmet Reapers
 */
public class CuisineReviewDAOImpl implements CuisineReviewDAO {

    private Context context;

    public CuisineReviewDAOImpl(Context context) {
        this.context = context;
    }

    @Override
    public void insertCuisineReview(CuisineReview cuisineReview, int cuisineID) {
        ContentValues values = new ContentValues();
        values.put(LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_RATING, cuisineReview.getRating());
        values.put(LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_COMMENT, cuisineReview.getComment());
        values.put(LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_ID, cuisineID);

        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();
        long reviewID = database.insert(LighteningOrderContract.CuisineReviewTable.TABLE_NAME, null, values);

        for (Bitmap image: cuisineReview.getImages()) {
            Bitmap photo = image;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArray = bos.toByteArray();

            ContentValues imageValues = new ContentValues();

            imageValues.put(LighteningOrderContract.CuisineImageTable.COLUMN_NAME_IMAGE_DATA, bArray);
            imageValues.put(LighteningOrderContract.CuisineImageTable.COLUMN_NAME_CUISINE_REVIEW_ID, reviewID);
            database.insert(LighteningOrderContract.CuisineImageTable.TABLE_NAME, null, imageValues);
        }

        connector.close();
    }

    @Override
    public List<CuisineReview> getAllCuisineReview() {
        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();

        String[] tableColumns = {
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_REVIEW_ID,
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_RATING,
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_COMMENT,
        };

        String sortOrder =
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_REVIEW_ID + " DESC";

        Cursor c = database.query(
                LighteningOrderContract.CuisineReviewTable.TABLE_NAME,  // The table to query
                tableColumns,                             // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c == null || c.getCount() == 0) return null;
        List<CuisineReview> reviews = new ArrayList<>();

        c.moveToFirst();
        while (!c.isAfterLast()) {

            // get the column index for each data item
            int cuisineReviewID = c.getInt(c.getColumnIndex(
                    LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_REVIEW_ID));
            int cuisineReviewRating = c.getInt(c.getColumnIndex(
                    LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_RATING));
            String cuisineReviewComment = c.getString(c.getColumnIndex(
                    LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_COMMENT));

            CuisineReview review = new CuisineReview();
            review.setCuisineReviewID(cuisineReviewID);
            review.setRating(cuisineReviewRating);
            review.setComment(cuisineReviewComment);

            reviews.add(review);

            c.moveToNext();
        }

        for (CuisineReview review: reviews) {
            String[] imageColumn = {
                    LighteningOrderContract.CuisineImageTable.COLUMN_NAME_IMAGE_DATA,
            };

            sortOrder =
                    LighteningOrderContract.CuisineImageTable.COLUMN_NAME_CUISINE_IMAGE_ID;

            String whereClause = LighteningOrderContract.CuisineImageTable.COLUMN_NAME_CUISINE_REVIEW_ID + " = ?";

            String[] whereArgs = new String[] {
                    String.valueOf(review.getCuisineReviewID())
            };

            Cursor cursor = database.query(
                    LighteningOrderContract.CuisineImageTable.TABLE_NAME,  // The table to query
                    imageColumn,                             // The columns to return
                    whereClause,                              // The columns for the WHERE clause
                    whereArgs,                                // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            List<Bitmap> images = new ArrayList<>();

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                // get the column index for each data item
                byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(
                        LighteningOrderContract.CuisineImageTable.COLUMN_NAME_IMAGE_DATA));

                Bitmap cuisineImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                images.add(cuisineImage);

                cursor.moveToNext();
            }

            review.setImages(images);
        }

        database.close();
        return reviews;
    }

    @Override
    public List<CuisineReview> getAllCuisineReviewWithCuisine(int cuisineID) {
        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();

        String[] tableColumns = {
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_REVIEW_ID,
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_RATING,
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_COMMENT,
        };

        String sortOrder =
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_REVIEW_ID + " DESC";

        String whereClause = LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_ID + " = ?";

        String[] whereArgs = new String[] {
                String.valueOf(cuisineID)
        };

        Cursor c = database.query(
                LighteningOrderContract.CuisineReviewTable.TABLE_NAME,  // The table to query
                tableColumns,                             // The columns to return
                whereClause,                              // The columns for the WHERE clause
                whereArgs,                                // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<CuisineReview> reviews = new ArrayList<>();

        if (c == null) return reviews;

        c.moveToFirst();
        while (!c.isAfterLast()) {

            // get the column index for each data item
            int cuisineReviewID = c.getInt(c.getColumnIndex(
                    LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_REVIEW_ID));
            int cuisineReviewRating = c.getInt(c.getColumnIndex(
                    LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_RATING));
            String cuisineReviewComment = c.getString(c.getColumnIndex(
                    LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_COMMENT));

            CuisineReview review = new CuisineReview();
            review.setCuisineReviewID(cuisineReviewID);
            review.setRating(cuisineReviewRating);
            review.setComment(cuisineReviewComment);

            reviews.add(review);

            c.moveToNext();
        }

        for (CuisineReview review: reviews) {
            String[] imageColumn = {
                    LighteningOrderContract.CuisineImageTable.COLUMN_NAME_IMAGE_DATA,
            };

            sortOrder =
                    LighteningOrderContract.CuisineImageTable.COLUMN_NAME_CUISINE_IMAGE_ID;

            String imageWhereClause = LighteningOrderContract.CuisineImageTable.COLUMN_NAME_CUISINE_REVIEW_ID + " = ?";

            String[] imageWhereArgs = new String[] {
                    String.valueOf(review.getCuisineReviewID())
            };

            Cursor cursor = database.query(
                    LighteningOrderContract.CuisineImageTable.TABLE_NAME,  // The table to query
                    imageColumn,                             // The columns to return
                    imageWhereClause,                              // The columns for the WHERE clause
                    imageWhereArgs,                                // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            List<Bitmap> images = new ArrayList<>();

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                // get the column index for each data item
                byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(
                        LighteningOrderContract.CuisineImageTable.COLUMN_NAME_IMAGE_DATA));

                Bitmap cuisineImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                images.add(cuisineImage);

                cursor.moveToNext();
            }

            review.setImages(images);
        }

        database.close();
        return reviews;
    }

    @Override
    public CuisineReview getCuisineReviewByID(int id) {
        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();

        String[] tableColumns = {
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_REVIEW_ID,
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_RATING,
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_COMMENT,
        };

        String sortOrder =
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_REVIEW_ID;

        String whereClause = LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_REVIEW_ID + " = ?";

        String[] whereArgs = new String[] {
                String.valueOf(id)
        };

        Cursor c = database.query(
                LighteningOrderContract.CuisineReviewTable.TABLE_NAME,  // The table to query
                tableColumns,                             // The columns to return
                whereClause,                              // The columns for the WHERE clause
                whereArgs,                                // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        if (c.getCount() == 0) return null;
        c.moveToFirst();

        // get the column index for each data item
        int cuisineReviewID = c.getInt(c.getColumnIndex(
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_REVIEW_ID));
        int cuisineReviewRating = c.getInt(c.getColumnIndex(
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_RATING));
        String cuisineReviewComment = c.getString(c.getColumnIndex(
                LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_COMMENT));

        CuisineReview review = new CuisineReview();
        review.setCuisineReviewID(cuisineReviewID);
        review.setRating(cuisineReviewRating);
        review.setComment(cuisineReviewComment);

        String[] imageColumn = {
                LighteningOrderContract.CuisineImageTable.COLUMN_NAME_IMAGE_DATA,
        };

        sortOrder =
                LighteningOrderContract.CuisineImageTable.COLUMN_NAME_CUISINE_IMAGE_ID;

        String imageWhereClause = LighteningOrderContract.CuisineImageTable.COLUMN_NAME_CUISINE_REVIEW_ID + " = ?";

        String[] imageWhereArgs = new String[] {
                String.valueOf(review.getCuisineReviewID())
        };

        Cursor cursor = database.query(
                LighteningOrderContract.CuisineImageTable.TABLE_NAME,  // The table to query
                imageColumn,                              // The columns to return
                imageWhereClause,                         // The columns for the WHERE clause
                imageWhereArgs,                           // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<Bitmap> images = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            // get the column index for each data item
            byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(
                    LighteningOrderContract.CuisineImageTable.COLUMN_NAME_IMAGE_DATA));

            Bitmap cuisineImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            images.add(cuisineImage);

            cursor.moveToNext();
        }

        review.setImages(images);

        database.close();
        return review;
    }

    @Override
    public void deleteCuisineReviewByID(int id) {
        String selection = LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_REVIEW_ID + " = ?";

        String[] selectionArgs = new String[] {
                String.valueOf(id)
        };

        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();
        database.delete(LighteningOrderContract.CuisineReviewTable.TABLE_NAME, selection, selectionArgs);
        connector.close();
    }
}
