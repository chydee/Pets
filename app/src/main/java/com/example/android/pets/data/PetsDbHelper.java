package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.pets.data.PetContract.PetsEntry;

public class PetsDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "shelter.db";
    public static final int DATABASE_VESION = 1;


    public PetsDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VESION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + PetsEntry.TABLE_NAME + " ("
                + PetsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetsEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + PetsEntry.COLUMN_BREED + " TEXT, "
                + PetsEntry.COLUMN_GENDER + " INTEGER NOT NULL, "
                + PetsEntry.COLUMN_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";


        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Database is still in version 1 so theres nothing to do here
       /* String PETS_DELETE_ENTRIES = "DELETE FROM " + PetsEntry.TABLE_NAME;
        db.execSQL(PETS_DELETE_ENTRIES);
        onCreate(db);*/
    }
}
