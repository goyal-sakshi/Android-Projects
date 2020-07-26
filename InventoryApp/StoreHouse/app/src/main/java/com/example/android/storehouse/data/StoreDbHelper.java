package com.example.android.storehouse.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hp on 8/29/2017.
 */

public class StoreDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public StoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + StoreContract.StoreEntry.TABLE_NAME + " ("
                + StoreContract.StoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StoreContract.StoreEntry.COLUMN_NAME + " TEXT NOT NULL,"
                + StoreContract.StoreEntry.COLUMN_QUANTITY + " INTEGER NOT NULL,"
                + StoreContract.StoreEntry.COLUMN_PRICE + " INTEGER NOT NULL ,"
                + StoreContract.StoreEntry.COLUMN_IMAGE + " TEXT);";

        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
