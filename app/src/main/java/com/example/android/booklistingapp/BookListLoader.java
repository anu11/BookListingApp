package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by anu on 20/7/17.
 */

public class BookListLoader extends AsyncTaskLoader<List<Book>> {

    private static final String LOG_TAG =  BookListLoader.class.getName();

    private String mUrl;

    public BookListLoader(Context context,String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<Book> bookList = QueryUtil.fetchBookListeData(mUrl);
        return bookList;
    }
}
