package com.example.android.storehouse.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by hp on 8/29/2017.
 */

public class StoreProvider extends ContentProvider {

    public static final String LOG_TAG = StoreProvider.class.getSimpleName();

    private static final int ITEMS = 100;
    private static final int ITEMS_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(StoreContract.CONTENT_AUTHORITY, StoreContract.PATH_STOREHOUSE, ITEMS);
        sUriMatcher.addURI(StoreContract.CONTENT_AUTHORITY, StoreContract.PATH_STOREHOUSE + "/#", ITEMS_ID);
    }

    private StoreDbHelper mDbHelper;

    @Override
    public boolean onCreate() {

        mDbHelper = new StoreDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                cursor = database.query(StoreContract.StoreEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ITEMS_ID:
                selection = StoreContract.StoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(StoreContract.StoreEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return StoreContract.StoreEntry.CONTENT_LIST_TYPE;
            case ITEMS_ID:
                return StoreContract.StoreEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not possible for " + uri);
        }
    }

    private Uri insertItem(Uri uri, ContentValues values) {

        String name = values.getAsString(StoreContract.StoreEntry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product requires a name");
        }

        Integer quantity = values.getAsInteger(StoreContract.StoreEntry.COLUMN_QUANTITY);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Product requires a valid quantity");
        }

        Float price = values.getAsFloat(StoreContract.StoreEntry.COLUMN_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Product requires a valid price");
        }

        String img = values.getAsString(StoreContract.StoreEntry.COLUMN_IMAGE);
        if (img == null) {
            throw new IllegalArgumentException("Product requires a valid image");
        }

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(StoreContract.StoreEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(StoreContract.StoreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEMS_ID:
                // Delete a single row given by the ID in the URI
                selection = StoreContract.StoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(StoreContract.StoreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEMS_ID:
                selection = StoreContract.StoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(StoreContract.StoreEntry.COLUMN_NAME)) {
            String name = values.getAsString(StoreContract.StoreEntry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        if (values.containsKey(StoreContract.StoreEntry.COLUMN_QUANTITY)) {

            Integer quantity = values.getAsInteger(StoreContract.StoreEntry.COLUMN_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Product requires valid quantity");
            }
        }

        if (values.containsKey(StoreContract.StoreEntry.COLUMN_PRICE)) {

            Float price = values.getAsFloat(StoreContract.StoreEntry.COLUMN_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Product requires valid price");
            }
        }

        if (values.containsKey(StoreContract.StoreEntry.COLUMN_IMAGE)) {

            String img = values.getAsString(StoreContract.StoreEntry.COLUMN_IMAGE);

            if (img == null) {
                throw new IllegalArgumentException("Product requires a valid image");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(StoreContract.StoreEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }
}
