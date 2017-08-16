package com.example.android.booklistingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private BookListAdapter mAdapter ;
    private EditText itemSearch;
    View.OnClickListener textViewOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Create a new intent to open the list of booksto be displayed
            Intent bookListIntent = new Intent(MainActivity.this, DisplayBookListActivity.class);
            bookListIntent.putExtra("searchItem",itemSearch.getText().toString());
            // Start the new activity
            startActivity(bookListIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find the view that shows a list based on books
        ImageView imageBook = (ImageView) findViewById(R.id.image_find_book);
        imageBook.setImageResource(R.drawable.find_book);

         itemSearch = (EditText) findViewById(R.id.book_search_view);


        Button searchbuttonView = (Button) findViewById(R.id.search_button);
        searchbuttonView.setOnClickListener(textViewOnclickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsIntent.putExtra("searchItem", itemSearch.getText().toString());
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
