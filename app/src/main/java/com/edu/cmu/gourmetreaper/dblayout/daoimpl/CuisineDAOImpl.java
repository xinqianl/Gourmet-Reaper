/**
 * Implementation class of the CuisineDAO interface
 */
package com.edu.cmu.gourmetreaper.dblayout.daoimpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.edu.cmu.gourmetreaper.dblayout.CuisineDAO;
import com.edu.cmu.gourmetreaper.entities.Cuisine;
import com.edu.cmu.gourmetreaper.util.DBConnector;
import com.edu.cmu.gourmetreaper.util.LighteningOrderContract;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Song Xiong
 * Team: Gourmet Reapers
 */
public class CuisineDAOImpl implements CuisineDAO {

    private Context context;

    public CuisineDAOImpl(Context context) {
        this.context = context;
    }

    @Override
    public void insertCuisine(Cuisine cuisine) {
        ContentValues values = new ContentValues();
        values.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_NAME, cuisine.getCuisineName());
        values.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_DESCRIPTION, cuisine.getCuisineDescription());
        values.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_PRICE, cuisine.getPrice());

        Bitmap photo = cuisine.getImage();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
        photo.recycle();
        byte[] bArray = bos.toByteArray();

        values.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_IMAGE, bArray);

        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();
        database.insert(LighteningOrderContract.CuisineTable.TABLE_NAME, null, values);
        connector.close();
        try {
            bos.close();
            bos = null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    @Override
    public void insertCuisineWithCategory(Cuisine cuisine, int cuisineCategoryID) {
        ContentValues values = new ContentValues();
        values.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_NAME, cuisine.getCuisineName());
        values.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_DESCRIPTION, cuisine.getCuisineDescription());
        values.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_PRICE, cuisine.getPrice());
        values.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_CATEGORY_ID, cuisineCategoryID);

        Bitmap photo = cuisine.getImage();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
        photo.recycle();
        byte[] bArray = bos.toByteArray();

        values.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_IMAGE, bArray);

        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();
        database.insert(LighteningOrderContract.CuisineTable.TABLE_NAME, null, values);
        connector.close();
        try {
            bos.close();
            bos = null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public List<Cuisine> getAllCuisine() {
        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();

        String[] tableColumns = {
                LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_NAME,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_DESCRIPTION,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_PRICE,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_IMAGE,
        };

        String sortOrder =
                LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID;

        Cursor c = database.query(
                LighteningOrderContract.CuisineTable.TABLE_NAME,  // The table to query
                tableColumns,                             // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<Cuisine> cuisines = new ArrayList<>();
        if (c == null || c.getCount() == 0) return cuisines;

        c.moveToFirst();
        while (!c.isAfterLast()) {

            // get the column index for each data item
            int cuisineID = c.getInt(c.getColumnIndex(
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID));
            String cuisineName = c.getString(c.getColumnIndex(
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_NAME));
            String cuisineDescription = c.getString(c.getColumnIndex(
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_DESCRIPTION));
            double cuisinePrice = c.getDouble(c.getColumnIndex(
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_PRICE));
            byte[] byteArray = c.getBlob(c.getColumnIndex(
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_IMAGE));

            Bitmap cuisineImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            Cuisine cuisine = new Cuisine();
            cuisine.setCuisineID(cuisineID);
            cuisine.setCuisineName(cuisineName);
            cuisine.setCuisineDescription(cuisineDescription);
            cuisine.setPrice(cuisinePrice);
            cuisine.setImage(cuisineImage);

            cuisines.add(cuisine);

            c.moveToNext();
        }

        database.close();
        return cuisines;
    }

    @Override
    public List<Cuisine> getAllCuisineByCategoryID(int cuisineCategoryID) {
        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();

        String[] tableColumns = {
                LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_NAME,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_DESCRIPTION,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_PRICE,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_IMAGE,
        };

        String sortOrder =
                LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID;

        String whereClause = LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_CATEGORY_ID + " = ?";

        String[] whereArgs = new String[] {
                String.valueOf(cuisineCategoryID)
        };

        Cursor c = database.query(
                LighteningOrderContract.CuisineTable.TABLE_NAME,  // The table to query
                tableColumns,                             // The columns to return
                whereClause,                              // The columns for the WHERE clause
                whereArgs,                                // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<Cuisine> cuisines = new ArrayList<>();

        if (c.getCount() == 0) return cuisines;
        c.moveToFirst();
        while (!c.isAfterLast()) {

            // get the column index for each data item
            int cuisineID = c.getInt(c.getColumnIndex(
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID));
            String cuisineName = c.getString(c.getColumnIndex(
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_NAME));
            String cuisineDescription = c.getString(c.getColumnIndex(
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_DESCRIPTION));
            double cuisinePrice = c.getDouble(c.getColumnIndex(
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_PRICE));
            byte[] byteArray = c.getBlob(c.getColumnIndex(
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_IMAGE));

            Bitmap cuisineImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            Cuisine cuisine = new Cuisine();
            cuisine.setCuisineID(cuisineID);
            cuisine.setCuisineName(cuisineName);
            cuisine.setCuisineDescription(cuisineDescription);
            cuisine.setPrice(cuisinePrice);
            cuisine.setImage(cuisineImage);

            cuisines.add(cuisine);

            c.moveToNext();
        }

        database.close();
        return cuisines;
    }

    @Override
    public Cuisine getCuisineByID(int id) {
        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();

        String[] tableColumns = {
                LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_NAME,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_DESCRIPTION,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_PRICE,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_IMAGE,
        };

        String whereClause = LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID + " = ?";

        String[] whereArgs = new String[] {
                String.valueOf(id)
        };

        Cursor c = database.query(
                LighteningOrderContract.CuisineTable.TABLE_NAME,  // The table to query
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
        int cuisineID = c.getInt(c.getColumnIndex(
                LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID));
        String cuisineName = c.getString(c.getColumnIndex(
                LighteningOrderContract.CuisineTable.COLUMN_NAME_NAME));
        String cuisineDescription = c.getString(c.getColumnIndex(
                LighteningOrderContract.CuisineTable.COLUMN_NAME_DESCRIPTION));
        double cuisinePrice = c.getDouble(c.getColumnIndex(
                LighteningOrderContract.CuisineTable.COLUMN_NAME_PRICE));
        byte[] byteArray = c.getBlob(c.getColumnIndex(
                LighteningOrderContract.CuisineTable.COLUMN_NAME_IMAGE));

        Bitmap cuisineImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        Cuisine cuisine = new Cuisine();
        cuisine.setCuisineID(cuisineID);
        cuisine.setCuisineName(cuisineName);
        cuisine.setCuisineDescription(cuisineDescription);
        cuisine.setPrice(cuisinePrice);
        cuisine.setImage(cuisineImage);

        database.close();
        return cuisine;
    }

    @Override
    public Cuisine getCuisineByName(String name) {
        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();

        String[] tableColumns = {
                LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_NAME,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_DESCRIPTION,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_PRICE,
                LighteningOrderContract.CuisineTable.COLUMN_NAME_IMAGE,
        };

        String whereClause = LighteningOrderContract.CuisineTable.COLUMN_NAME_NAME + " = ?";

        String[] whereArgs = new String[] {
                name
        };

        Cursor c = database.query(
                LighteningOrderContract.CuisineTable.TABLE_NAME,  // The table to query
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
        int cuisineID = c.getInt(c.getColumnIndex(
                LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID));
        String cuisineName = c.getString(c.getColumnIndex(
                LighteningOrderContract.CuisineTable.COLUMN_NAME_NAME));
        String cuisineDescription = c.getString(c.getColumnIndex(
                LighteningOrderContract.CuisineTable.COLUMN_NAME_DESCRIPTION));
        double cuisinePrice = c.getDouble(c.getColumnIndex(
                LighteningOrderContract.CuisineTable.COLUMN_NAME_PRICE));
        byte[] byteArray = c.getBlob(c.getColumnIndex(
                LighteningOrderContract.CuisineTable.COLUMN_NAME_IMAGE));

        Bitmap cuisineImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        Cuisine cuisine = new Cuisine();
        cuisine.setCuisineID(cuisineID);
        cuisine.setCuisineName(cuisineName);
        cuisine.setCuisineDescription(cuisineDescription);
        cuisine.setPrice(cuisinePrice);
        cuisine.setImage(cuisineImage);

        database.close();
        return cuisine;
    }

    @Override
    public void updateCuisineByID(int id, Cuisine cuisine, int cuisineCategoryID) {
        ContentValues editValues = new ContentValues();
        editValues.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_NAME, cuisine.getCuisineName());
        editValues.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_DESCRIPTION, cuisine.getCuisineDescription());
        editValues.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_PRICE, cuisine.getPrice());
        editValues.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_CATEGORY_ID, cuisineCategoryID);

        Bitmap photo = cuisine.getImage();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();

        editValues.put(LighteningOrderContract.CuisineTable.COLUMN_NAME_IMAGE, bArray);

        String selection = LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID + " = ?";

        String[] selectionArgs = new String[] {
                String.valueOf(id)
        };

        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();
        database.update(LighteningOrderContract.CuisineTable.TABLE_NAME, editValues, selection, selectionArgs);
        connector.close();
    }

    @Override
    public void deleteCuisineByID(int id) {
        String selection = LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID + " = ?";

        String[] selectionArgs = new String[] {
                String.valueOf(id)
        };

        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();
        database.delete(LighteningOrderContract.CuisineTable.TABLE_NAME, selection, selectionArgs);
        connector.close();
    }
}
