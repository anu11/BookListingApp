package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anu on 21/7/17.
 */

public class DisplayBookListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    BookListAdapter mAdapter;
    String mBookTitleSearched;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * URL for book data from the google api dataset
     */
    private static final String BASE_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    private static final int BOOK_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_page);
        ListView bookListView = (ListView) findViewById(R.id.booklist);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mBookTitleSearched = bundle.getString("searchItem");
        }

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Hide loading indicator and show empty state view
            View progressIndicator = findViewById(R.id.loading_indicator);
            progressIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.error_no_connection);
        }
        mAdapter = new BookListAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        String maxResults = sharedPrefs.getString(
                getString(R.string.settings_maxresults_key),
                getString(R.string.settings_maxresults_default)
        );

        Uri baseUri = Uri.parse(searchQuery());
        Uri.Builder uriBuilder = baseUri.buildUpon();
        // Append parameters obtained fro, SharedPreferences
        uriBuilder.appendQueryParameter("maxResults", maxResults);
        uriBuilder.appendQueryParameter("orderBy", orderBy);
        return new BookListLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> bookList) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_books);

        mAdapter.clear();

        // If there is a valid list of {@link books}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (bookList != null && !bookList.isEmpty()) {
            mAdapter.addAll(bookList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    public String searchQuery() {
        StringBuilder stringBuilder = new StringBuilder(BASE_REQUEST_URL);
        stringBuilder.append(mBookTitleSearched);
        return stringBuilder.toString();
    }
}
