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

        //Get readable Database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //This cursor will hold the result of the query
        Cursor cursor;

        //Figure out if the UriMatcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match){
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
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                //This will perform a query on the pets table where the _id equals to 5 to return a
                //cursor containing that row of the table
                cursor = db.query(PetsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
                default:
                    throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
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
        final int match = sUriMatcher.match(uri);
        switch (match){
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

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        /*
         * Sets the values of each column and inserts the word. The arguments to the "put"
         * method are "column name" and "value"
         */
        values.put(PetsEntry.COLUMN_NAME, "Toto");
        values.put(PetsEntry.COLUMN_BREED, "Terrier");
        values.put(PetsEntry.COLUMN_GENDER, PetsEntry.GENDER_MALE);
        values.put(PetsEntry.COLUMN_WEIGHT, 7);

        long id = database.insert(PetsEntry.TABLE_NAME, null, values);

        if (id == -1){
        Log.e(LOG_TAG, "Failed to insert row for " + uri);
        return null;
        }
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
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
