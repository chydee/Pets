package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.pets.data.PetContract.PetsEntry;

public class PetsDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "shelter.db";
    public static final int DATABASE_VESION = 1;
    public static final String COMMA = ", ";
    public static final String PETS_CREATE_ENTRIES = "CREATE TABLE "+ PetsEntry.TABLE_NAME + "( " + PetsEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT"+ COMMA + PetsEntry.COLUMN_NAME + " TEXT NOT NULL" + COMMA + PetsEntry.COLUMN_BREED + " TEXT" +COMMA+ PetsEntry.COLUMN_GENDER + " INTEGER NOT NULL DEFAULT 0" + COMMA + PetsEntry.COLUMN_WEIGHT + " INTEGER NOT NULL" + ")";
    public static final String PETS_DELETE_ENTRIES = "DELETE FROM " + PetsEntry.TABLE_NAME;


    public PetsDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VESION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PETS_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PETS_DELETE_ENTRIES);
        onCreate(db);
    }
}
