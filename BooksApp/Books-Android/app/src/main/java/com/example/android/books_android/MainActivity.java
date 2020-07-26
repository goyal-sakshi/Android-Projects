package com.example.android.books_android;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<Books>> {

    private static final int LOADER_ID = 1;
    private BooksAdapter madapter;
    private EditText search_box;
    private Button search_bttn;
    private String search_str;
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    private static final String GOOGLEAPIS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_box = (EditText) findViewById(R.id.edit_text);
        search_bttn = (Button) findViewById(R.id.search_btn);
        search_str = search_box.getText().toString();

        search_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }

                if (search_str.length() == 0){
                    if (netInfo == null) {
                        mEmptyStateTextView.setText(R.string.no_internet_connection);
                    }
                    if(netInfo != null && netInfo.isConnectedOrConnecting()){
                        Toast.makeText(MainActivity.this, "Enter Text to search", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        madapter = new BooksAdapter(this, new ArrayList<Books>());
        ListView bookListView = (ListView) findViewById(R.id.list);
        bookListView.setAdapter(madapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Books currentBook = madapter.getItem(position);

                Uri bookUri = Uri.parse(currentBook.getLink());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW,bookUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){

            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }


    @Override
    public Loader <List<Books>> onCreateLoader(int id, Bundle args) {

        search_str = search_box.getText().toString();

        return new BooksLoader(this, GOOGLEAPIS_REQUEST_URL + search_str);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> book) {
        View loadingIndicator = (View) findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        madapter.clear();


        mEmptyStateTextView.setText(R.string.no_books_available);

        if (book != null){
            madapter.addAll(book);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        madapter.clear();
    }

}
