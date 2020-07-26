package com.example.android.books_android;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by hp on 8/12/2017.
 */

public class BooksLoader extends AsyncTaskLoader<List<Books>> {

    private String mUrl;

    public BooksLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Books> loadInBackground() {
        if (mUrl == null){
            return null;
        }
        List<Books> books = QueryUtils.fetchBooksData(mUrl);
        return books;
    }
}
