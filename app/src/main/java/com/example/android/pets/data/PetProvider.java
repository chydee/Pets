package com.example.android.pets.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;


/**
 * @link ContentProvider for the App
 */
public class PetProvider extends ContentProvider {

    //Tag for log messages
    private static final String LOG_TAG = PetProvider.class.getName();
    private PetsDbHelper dbHelper;

    /**
     * Initialize the provider and the databaseHelper object
     */
    @Override
    public boolean onCreate() {
        // Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.

        dbHelper = new PetsDbHelper(getContext());
        return false;
    }

    /**
     * @param uri for performing the query
     * @param projection of the table
     * @param selection for implementing the WHERE clause
     * @param selectionArgs the value of the WHERE clause
     * @param sortOrder order of arrangement
     * @return null
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }


    /**
     * @return the MIME type of data for the Content URI
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }


    /**
     * Inserts new data into the content provider with the given ContentValues
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    /**
     *Deletes the Data at the given selection and selectionArgs
     */

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
