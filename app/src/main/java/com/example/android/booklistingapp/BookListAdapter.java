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
    private Context context = getContext();
    private Bitmap imageIcon = null;
    private List mBookList;

    public BookListAdapter(Context context, ArrayList<Book> bookList) {
        super(context, 0, bookList);
        mBookList = bookList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        View listItemView = convertView;
        if (listItemView == null) {
            holder = new ViewHolder();
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
            holder.bookTitle = (TextView) listItemView.findViewById(R.id.title);
            holder.authorName = (TextView) listItemView.findViewById(R.id.author);
            holder.priceBook = (TextView) listItemView.findViewById(R.id.price);
            holder.bookImageLink = (ImageView) listItemView.findViewById(R.id.imagethumbnail);
            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }
        Book newBook = getItem(position);
        holder.bookTitle.setText(newBook.getmBookName());

        String authors = "";
        if (newBook.getmAuthorName() != null) {
            for (String athr : newBook.getmAuthorName()) {
                authors += athr + " ,";
            }
            authors = authors.substring(0, authors.length() - 1);
            holder.authorName.setText(authors);
        }

        String price = newBook.getAmount().concat(" " + newBook.getCurrencyCode());
        holder.priceBook.setText(price);
        holder.bookImageLink.setImageBitmap(newBook.getImageBitmap());

        return listItemView;
    }

    static class ViewHolder {
        TextView bookTitle;
        TextView authorName;
        TextView priceBook;
        ImageView bookImageLink;
    }
}
