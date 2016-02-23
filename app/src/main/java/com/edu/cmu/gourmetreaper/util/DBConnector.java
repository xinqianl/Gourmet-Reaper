/**
 * This class is a helper class to get the connection of a database,
 * open it and close it.
 */
package com.edu.cmu.gourmetreaper.util;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Song Xiong
 * Team: Gourmet Reapers
 */
public class DBConnector {

    private static final String SQL_CREATE_CUISINE_CATEGORY =
            "CREATE TABLE " + LighteningOrderContract.CuisineCategoryTable.TABLE_NAME + " (" +
                    LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CUISINE_CATEGORY_ID +
                    " INTEGER primary key autoincrement," +
                    LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CATEGORY_NAME +
                    " TEXT not null" +
            " )";

    private static final String SQL_CREATE_CUISINE =
            "CREATE TABLE " + LighteningOrderContract.CuisineTable.TABLE_NAME + " (" +
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID +
                    " INTEGER primary key autoincrement," +
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_NAME + " TEXT not null, " +
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_DESCRIPTION + " TEXT not null, " +
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_PRICE + " DOUBLE not null, " +
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_IMAGE + " BLOB not null, " +
                    LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_CATEGORY_ID + " INTEGER, " +
                    " FOREIGN KEY (" + LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_CATEGORY_ID +
                    ") REFERENCES " + LighteningOrderContract.CuisineCategoryTable.TABLE_NAME +
                    " (" + LighteningOrderContract.CuisineCategoryTable.COLUMN_NAME_CUISINE_CATEGORY_ID +
            "));";


    private static final String SQL_CREATE_CUISINE_REVIEW =
            "CREATE TABLE " + LighteningOrderContract.CuisineReviewTable.TABLE_NAME + " (" +
                    LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_REVIEW_ID +
                    " INTEGER primary key autoincrement, " +
                    LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_RATING + " INTEGER not null, " +
                    LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_COMMENT + " TEXT not null, " +
                    LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_ID + " INTEGER, " +
                    " FOREIGN KEY (" + LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_ID +
                    ") REFERENCES " + LighteningOrderContract.CuisineTable.TABLE_NAME +
                    " (" + LighteningOrderContract.CuisineTable.COLUMN_NAME_CUISINE_ID +
            "));";

    private static final String SQL_CREATE_CUISINE_IMAGE =
            "CREATE TABLE " + LighteningOrderContract.CuisineImageTable.TABLE_NAME + " (" +
                    LighteningOrderContract.CuisineImageTable.COLUMN_NAME_CUISINE_IMAGE_ID +
                    " INTEGER primary key autoincrement," +
                    LighteningOrderContract.CuisineImageTable.COLUMN_NAME_IMAGE_DATA + " BLOB not null," +
                    LighteningOrderContract.CuisineImageTable.COLUMN_NAME_CUISINE_REVIEW_ID + " INTEGER," +
                    " FOREIGN KEY (" + LighteningOrderContract.CuisineImageTable.COLUMN_NAME_CUISINE_REVIEW_ID +
                    ") REFERENCES " + LighteningOrderContract.CuisineReviewTable.TABLE_NAME +
                    " (" + LighteningOrderContract.CuisineReviewTable.COLUMN_NAME_CUISINE_REVIEW_ID +
            "));";

    private static final String SQL_CREATE_RESTAURANT_REVIEW =
            "CREATE TABLE " + LighteningOrderContract.RestaurantReviewTable.TABLE_NAME + " (" +
                    LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RESTAURANT_REVIEW_ID +
                    " INTEGER primary key autoincrement," +
                    LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_RATING + " INTEGER not null," +
                    LighteningOrderContract.RestaurantReviewTable.COLUMN_NAME_COMMENT + " TEXT not null);";

    // database name
    private static final String DATABASE_NAME = "app_lightening_order";
    private SQLiteDatabase database; // database object
    private DatabaseOpenHelper databaseOpenHelper; // database helper

    // public constructor for DatabaseConnector
    public DBConnector(Context context)
    {
        // create a new DatabaseOpenHelper
        databaseOpenHelper =
                new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
    } // end DatabaseConnector constructor

    // open the database connection
    public void open() throws SQLException
    {
        // create or open a database for reading/writing
        database = databaseOpenHelper.getWritableDatabase();
    } // end method open

    // close the database connection
    public void close()
    {
        if (database != null)
            database.close(); // close the database connection
    } // end method close

    // get the database connection
    public SQLiteDatabase getDatabase() {
        return database;
    }

    // Helper functions to initialize database
    private class DatabaseOpenHelper extends SQLiteOpenHelper
    {
        // public constructor
        public DatabaseOpenHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        } // end DatabaseOpenHelper constructor

        // creates table when the database is created
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            // query to create a set of new tables
            db.execSQL(SQL_CREATE_CUISINE_CATEGORY);
            db.execSQL(SQL_CREATE_CUISINE);
            db.execSQL(SQL_CREATE_CUISINE_REVIEW);
            db.execSQL(SQL_CREATE_CUISINE_IMAGE);
            db.execSQL(SQL_CREATE_RESTAURANT_REVIEW);
        } // end method onCreate

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
        } // end method onUpgrade
    } // end class DatabaseOpenHelper
}
