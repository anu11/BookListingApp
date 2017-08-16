package com.example.android.booklistingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anu on 20/7/17.
 */

public class BookListAdapter extends ArrayAdapter<Book> {

    Bitmap imageIcon = null;
    private List mBookList;

    public BookListAdapter(Context context, ArrayList<Book> bookList) {
        super(context, 0, bookList);
        mBookList = bookList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }
        Book newBook = getItem(position);

        TextView bookTitle = (TextView) listItemView.findViewById(R.id.title);
        bookTitle.setText(newBook.getmBookName());

        TextView authorName = (TextView) listItemView.findViewById(R.id.author);
        String authors = "";
        if (newBook.getmAuthorName() != null) {
            for (String athr : newBook.getmAuthorName()) {
                authors += athr + " ,";
            }
            authors = authors.substring(0, authors.length() - 1);
            authorName.setText(authors);
        }
        TextView priceBook = (TextView) listItemView.findViewById(R.id.price);
        String price = newBook.getAmount().concat(" " + newBook.getCurrencyCode());

        priceBook.setText(price);

        ImageView imageLink = (ImageView) listItemView.findViewById(R.id.imagethumbnail);

        imageLink.setImageBitmap(newBook.getImageBitmap());

        return listItemView;
    }
}
