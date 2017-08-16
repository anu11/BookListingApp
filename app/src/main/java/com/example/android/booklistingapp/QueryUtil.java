package com.example.android.booklistingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anu on 20/7/17.
 */

public final class QueryUtil {
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtil.class.getSimpleName();

    private QueryUtil() {
    }

    /**
     * Query the Book dataset and return a list of {@link Book} objects.
     */
    public static List<Book> fetchBookListeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link book}s
        List<Book> bookList = extractFeatureFromJson(jsonResponse);
        // Return the list
        return bookList;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setReadTimeout(10000 /* milliseconds */);
            //urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Book> extractFeatureFromJson(String bookJSON) {
        Bitmap imageIcon = null;
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Book> bookList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            // Extract the JSONArray associated with the key called "items",
            JSONArray booksArray = baseJsonResponse.getJSONArray("items");

            // For each book in the booksArray, create an {@link book} object
            for (int i = 0; i < booksArray.length(); i++) {

                // Get a single book at position i within the list of books
                JSONObject currentBook = booksArray.getJSONObject(i);

                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                // Extract the value for the key called "title"
                String title = volumeInfo.getString("title");

                ArrayList<String> authorList = new ArrayList<>();

                // Extract the value for the key called "author"
                if (volumeInfo.has("authors")) {
                    JSONArray authorJsonArray = volumeInfo.getJSONArray("authors");

                    for (int j = 0; j < authorJsonArray.length(); j++) {
                        authorList.add(authorJsonArray.getString(j));
                    }
                }
                // Extract the value for the key called "date"
                String publishedDate = volumeInfo.getString("publishedDate");

                JSONObject saleInfo = currentBook.getJSONObject("saleInfo");

                String amount = "Not for sale";
                String currencyCode = "";
                if (saleInfo.has("retailPrice")) {
                    JSONObject retailPrice = saleInfo.getJSONObject("retailPrice");
                    amount = retailPrice.getString("amount");
                    currencyCode = retailPrice.getString("currencyCode");
                }

                JSONObject imageLinkObject = volumeInfo.getJSONObject("imageLinks");

                String imageLink = imageLinkObject.getString("smallThumbnail");
                try {
                    imageIcon = getBitmapImage(imageLink);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // Create a new {@link book} object
                Book newBook = new Book(title, authorList, publishedDate, amount, currencyCode, imageIcon);

                // Add the new {@link book} to the list of books.
                bookList.add(newBook);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the books JSON results", e);
        }
        // Return the list of books
        return bookList;
    }

    public static Bitmap getBitmapImage(String url) throws IOException {
        Bitmap imageBitmap = null;
        try {
            URL newurl = new URL(url);
            imageBitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return imageBitmap;
    }
}