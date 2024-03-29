package com.example.android.pets.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.pets.data.PetContract.PetsEntry;


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
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS);

        /*
         * Sets the code for a single row to 2. In this case, the "#" wildcard is
         * used. "content://com.example.android.pets/pets/3" matches, but
         * "content://com.example.android.pets/pets doesn't.
         */
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS + "/#", PET_ID);
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
     * @param uri           for performing the query
     * @param projection    of the table
     * @param selection     for implementing the WHERE clause
     * @param selectionArgs the value of the WHERE clause
     * @param sortOrder     order of arrangement
     * @return null
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        //Get readable Database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //This cursor will hold the result of the query
        Cursor cursor;

        //Figure out if the UriMatcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                // Perform database query on pets table
                cursor = db.query(PetsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PET_ID:
                /**
                 * For the PETS_ID code extract the ID from the URI
                 * For an example such as "content://com.example.android.pets/pets/5",
                 * the selection will be "_id = ?" and the selectionArgs will be a
                 * String containing the actual ID of 5 in this case.
                 *
                 * For every "?" in the selection, we need to have an element in the selection
                 * argument that will fill in the "?". since we have one question mark in the
                 * selection, we have 1 string in the selection arguments' String array
                 *
                 */
                selection = PetsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                //This will perform a query on the pets table where the _id equals to 5 to return a
                //cursor containing that row of the table
                cursor = db.query(PetsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        //Set notification URI on the cursor
        //so we know what content URI the cursor was created for
        //If the data at this URI changes, then we know that we need to update the cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        //Return the cursor
        return cursor;
    }


    /**
     * @return the MIME type of data for the Content URI
     */
    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetsEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown Uri " + uri + " with match " + match);
        }
    }


    /**
     * Inserts new data into the content provider with the given ContentValues
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri, values);
            default:
                throw new IllegalArgumentException("Insertion not supported for " + uri);
        }

    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPet(Uri uri, ContentValues values) {

        //Check that name is not null
        String name = values.getAsString(PetsEntry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }
        // breed can be null so theres no need to check in the provider
        //Checks if the gender is valid
        Integer gender = values.getAsInteger(PetsEntry.COLUMN_GENDER);
        if (gender == null || !PetsEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Pet gender must be valid");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer weight = values.getAsInteger(PetsEntry.COLUMN_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }

        //Get Writeable database
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        // Insert the new pet with the given values
        long id = database.insert(PetsEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //Notify all listener that data has changed for the PET CONTENT_URI
        getContext().getContentResolver().notifyChange(uri, null);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Deletes the Data at the given selection and selectionArgs
     */

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //Get a writeable database
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int rowsDeleted; // To track the number of rows deleted

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:
                //Delete all rows that match the selection and selectionArgs
                rowsDeleted = database.delete(PetsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PET_ID:
                //Delete a single row given by the ID in the URI
                selection = PetsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(PetsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return updatePet(uri, values, selection, selectionArgs);
            case PET_ID:
                // For PET_ID code, extract the ID from the UI
                //so we know which row to update, selection will be "_id=?" selction
                //argument will be a String array containing the actual id
                selection = PetsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(PetsEntry.COLUMN_NAME)) {
            //Sanity Checks
            String name = values.getAsString(PetsEntry.COLUMN_NAME);
            //Checks if the name is empty
            if (name == null) {
                throw new IllegalArgumentException("Pet name is requires");
            }
        }

        if (values.containsKey(PetsEntry.COLUMN_GENDER)) {
            Integer gender = values.getAsInteger(PetsEntry.COLUMN_GENDER);
            //Checks if the gender is valid
            if (gender == null || !PetsEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet gender must be valid");
            }
        }

        if (values.containsKey(PetsEntry.COLUMN_WEIGHT)) {
            Integer weight = values.getAsInteger(PetsEntry.COLUMN_WEIGHT);
            if (weight < 0 || weight == null) {
                throw new IllegalArgumentException("Pets weight must be a positive value ");
            }
        }


        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        //Get a writeable database
        SQLiteDatabase database = dbHelper.getWritableDatabase();


        //  Update the selected pets in the pets database table with the given ContentValues
        //Update the pets table or a single pet with the given value
        int rowsUpdated = database.update(PetsEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            //Notify all listener that data has changed for the PET CONTENT_URI
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
