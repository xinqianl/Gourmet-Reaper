/**
 * Implementation class of the CuisineCategoryDAO interface
 */
package com.edu.cmu.gourmetreaper.dblayout.daoimpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu.cmu.gourmetreaper.dblayout.CuisineCategoryDAO;
import com.edu.cmu.gourmetreaper.entities.CuisineCategory;
import com.edu.cmu.gourmetreaper.util.DBConnector;
import com.edu.cmu.gourmetreaper.util.LighteningOrderContract;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Song Xiong
 * Team: Gourmet Reapers
 */
public class CuisineCategoryDAOImpl implements CuisineCategoryDAO {

    private Context context;

    public CuisineCategoryDAOImpl(Context context) {
        this.context = context;
    }

    @Override
    public void insertCuisineCategory(CuisineCategory cuisineCategory) {
        ContentValues values = new ContentValues();
        values.put(LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CATEGORY_NAME, cuisineCategory.getCategoryName());

        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();
        database.insert(LighteningOrderContract.CuisineCategoryTable.TABLE_NAME, null, values);
        connector.close();
    }

    @Override
    public List<CuisineCategory> getAllCuisineCategory() {
        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();

        String[] tableColumns = {
                LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CUISINE_CATEGORY_ID,
                LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CATEGORY_NAME,
        };

        String sortOrder =
                LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CUISINE_CATEGORY_ID;

        Cursor c = database.query(
                LighteningOrderContract.CuisineCategoryTable.TABLE_NAME,  // The table to query
                tableColumns,                             // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (c == null || c.getCount() == 0) return null;

        List<CuisineCategory> categories = new ArrayList<>();
        c.moveToFirst();
        while (!c.isAfterLast()) {

            // get the column index for each data item
            int cuisineCategoryID = c.getInt(c.getColumnIndex(
                    LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CUISINE_CATEGORY_ID));
            String cuisineCategoryName = c.getString(c.getColumnIndex(
                    LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CATEGORY_NAME));

            CuisineCategory category = new CuisineCategory();
            category.setCategoryID(cuisineCategoryID);
            category.setCategoryName(cuisineCategoryName);

            categories.add(category);

            c.moveToNext();
        }

        database.close();
        return categories;
    }

    @Override
    public CuisineCategory getCuisineCategoryByID(int id) {
        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();

        String[] tableColumns = {
                LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CUISINE_CATEGORY_ID,
                LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CATEGORY_NAME,
        };

        String whereClause = LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CUISINE_CATEGORY_ID + " = ?";

        String[] whereArgs = new String[] {
                String.valueOf(id)
        };

        Cursor c = database.query(
                LighteningOrderContract.CuisineCategoryTable.TABLE_NAME,  // The table to query
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
        int cuisineCategoryID = c.getInt(c.getColumnIndex(
                LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CUISINE_CATEGORY_ID));
        String cuisineCategoryName = c.getString(c.getColumnIndex(
                LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CATEGORY_NAME));

        CuisineCategory category = new CuisineCategory();
        category.setCategoryID(cuisineCategoryID);
        category.setCategoryName(cuisineCategoryName);

        database.close();
        return category;
    }

    @Override
    public CuisineCategory getCuisineCategoryByName(String name) {
        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();

        String[] tableColumns = {
                LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CUISINE_CATEGORY_ID,
                LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CATEGORY_NAME,
        };

        String whereClause = LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CATEGORY_NAME + " = ?";

        String[] whereArgs = new String[] {
                name
        };

        Cursor c = database.query(
                LighteningOrderContract.CuisineCategoryTable.TABLE_NAME,  // The table to query
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
        int cuisineCategoryID = c.getInt(c.getColumnIndex(
                LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CUISINE_CATEGORY_ID));
        String cuisineCategoryName = c.getString(c.getColumnIndex(
                LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CATEGORY_NAME));

        CuisineCategory category = new CuisineCategory();
        category.setCategoryID(cuisineCategoryID);
        category.setCategoryName(cuisineCategoryName);

        database.close();
        return category;
    }

    @Override
    public void updateCuisineCategoryNameByID(int id, String newName) {
        ContentValues editValues = new ContentValues();
        editValues.put(LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CATEGORY_NAME, newName);

        String selection = LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CUISINE_CATEGORY_ID + " = ?";

        String[] selectionArgs = new String[] {
                String.valueOf(id)
        };

        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();
        database.update(LighteningOrderContract.CuisineCategoryTable.TABLE_NAME, editValues, selection, selectionArgs);
        connector.close();
    }

    @Override
    public void deleteCuisineCategoryByID(int id) {
        String selection = LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CUISINE_CATEGORY_ID + " = ?";

        String[] selectionArgs = new String[] {
                String.valueOf(id)
        };

        DBConnector connector = new DBConnector(context);
        connector.open();
        SQLiteDatabase database = connector.getDatabase();
        database.delete(LighteningOrderContract.CuisineCategoryTable.TABLE_NAME, selection, selectionArgs);
        connector.close();
    }
}
