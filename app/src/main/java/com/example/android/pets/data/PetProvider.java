package com.example.android.pets.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;


/**
 * @link ContentProvider for the App
 */
public class PetProvider extends ContentProvider {

    //Tag for log messages
    private static final String LOG_TAG = PetProvider.class.getName();
    private PetsDbHelper dbHelper;

    //Codes or CASES
    private static final int PETS = 100;
    private static final int PET_ID = 101;

    //UriMatcher Object
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        /*
         * The calls to addURI() go here, for all of the content URI patterns that the provider
         * should recognize. For this snippet, only the calls for the petsTable are shown.
         */

        /*
         * Sets the integer value for multiple rows in table 3 to 1. Notice that no wildcard is used
         * in the path
         */
        sUriMatcher.addURI(PetContract.CONTACT_AUTHORITY, PetContract.PATH_PETS, PETS);

        /*
         * Sets the code for a single row to 2. In this case, the "#" wildcard is
         * used. "content://com.example.android.pets/pets/3" matches, but
         * "content://com.example.android.pets/pets doesn't.
         */
        sUriMatcher.addURI(PetContract.CONTACT_AUTHORITY, PetContract.PATH_PETS + "/#", PET_ID);
    }

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
