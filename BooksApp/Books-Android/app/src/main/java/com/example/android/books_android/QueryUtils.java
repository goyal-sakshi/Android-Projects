package com.example.android.books_android;

import android.app.LoaderManager;
import android.content.Context;
import android.text.TextUtils;
import android.text.method.Touch;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.string.no;

/**
 * Created by hp on 8/12/2017.
 */

public class QueryUtils {


    private static final String LOG_TAGS = QueryUtils.class.getSimpleName();

    public QueryUtils() {

    }

    public static List<Books> fetchBooksData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAGS, "Problem making HTTP request", e);
        }

        List<Books> book = extractFeatureFromJson(jsonResponse);
        return book;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAGS, "Problem building url", e);
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
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAGS, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAGS, "Problem retrieving the books json response", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
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
     * Return a list of {@link Books} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Books> extractFeatureFromJson(String BooksJson) {
        if (TextUtils.isEmpty(BooksJson)) {
            return null;
        }

        List<Books> books = new ArrayList<>();
        try {

            JSONObject baseJsonResponse = new JSONObject(BooksJson);

            int totalItems = baseJsonResponse.getInt("totalItems");

            if (baseJsonResponse.has("items")) {

                JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {

                    JSONObject currentBook = itemsArray.getJSONObject(i);

                    JSONObject bookInfo = currentBook.getJSONObject("volumeInfo");

                    String title = bookInfo.getString("title");

                    String[] authors = new String[]{};
                    JSONArray authorJsonArray = bookInfo.optJSONArray("authors");
                    if (authorJsonArray != null) {
                        ArrayList<String> authorList = new ArrayList<String>();
                        for (int j = 0; j < authorJsonArray.length(); j++) {
                            authorList.add(authorJsonArray.get(j).toString());
                        }
                        authors = authorList.toArray(new String[authorList.size()]);
                    }

                    String description = "";
                    if (bookInfo.optString("description") != null)
                        description = bookInfo.optString("description");
                    else
                        description = "No Description";

                    String infoLink = "";
                    if (bookInfo.optString("infoLink") != null)
                        infoLink = bookInfo.optString("infoLink");

                    books.add(new Books(title, authors, description, infoLink));
                }
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem solving the book json results", e);
        }
        return books;
    }
}
