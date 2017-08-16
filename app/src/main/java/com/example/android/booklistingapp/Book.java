package com.example.android.booklistingapp;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by anu on 19/7/17.
 */

public class Book {

    private String mBookName;

    private ArrayList<String> mAuthorName;

    private String mPublishedYear;

    private String mAmount;

    private String mCurrencyCode;

    private Bitmap mImageLink;

    public Book(String title,ArrayList<String>  authorList,String publishedDate,String amount,String currency,Bitmap imageBitmap){
        mBookName = title;
        mAuthorName = authorList;
        mPublishedYear = publishedDate;
        mAmount = amount;
        mCurrencyCode = currency;
        mImageLink = imageBitmap;
    }

    public String getmBookName() {
        return mBookName;
    }

    public ArrayList<String> getmAuthorName() {
        return mAuthorName;
    }

    public String getmPublishedYear() {
        return mPublishedYear;
    }

    public String getAmount() {
        return mAmount;
    }

    public Bitmap getImageBitmap() {
        return mImageLink;
    }

    public String getCurrencyCode() {
        return mCurrencyCode;
    }
}
