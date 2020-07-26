package com.example.android.storehouse;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.storehouse.data.StoreContract;

import static android.R.attr.data;
import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER = 0;
    StoreCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView itemListView = (ListView) findViewById(R.id.list);

        View emptyView = (View) findViewById(R.id.empty_text_view);
        itemListView.setEmptyView(emptyView);


        mCursorAdapter = new StoreCursorAdapter(this, null);
        itemListView.setAdapter(mCursorAdapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentItemUri = ContentUris.withAppendedId(StoreContract.StoreEntry.CONTENT_URI, id);
                intent.setData(currentItemUri);
                startActivity(intent);

            }
        });

        getSupportLoaderManager().initLoader(ITEM_LOADER, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllItems();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper method to delete all item in the database.
     */
    private void deleteAllItems() {
        int rowsDeleted = getContentResolver().delete(StoreContract.StoreEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from item database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                StoreContract.StoreEntry._ID,
                StoreContract.StoreEntry.COLUMN_NAME,
                StoreContract.StoreEntry.COLUMN_QUANTITY,
                StoreContract.StoreEntry.COLUMN_PRICE,
                StoreContract.StoreEntry.COLUMN_IMAGE};

        return new CursorLoader(this,
                StoreContract.StoreEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCursorAdapter.swapCursor(null);
    }
}
