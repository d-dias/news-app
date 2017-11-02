package com.example.dilkidias.newsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DILKI DIAS on 11-Jun-17.
 */

public class QueryUtils {

    public static List<News> news;

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list_find of {@link News}s
        news = extractFeatureFromJson(jsonResponse);

        // Return the list_find of {@link News}s
        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, String.valueOf(R.string.problem_url), e);
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
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.e(LOG_TAG,"Error response code:" + urlConnection.getResponseCode());

            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);

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

    private QueryUtils() {
    }

    public static ArrayList<News> extractFeatureFromJson(String newsJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding news to
        ArrayList<News> news = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the SAMPLE_JSON_RESPONSE string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject baseJsonArray = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "features",
            // which represents a list_find of features (or news).
            if (baseJsonArray.getInt("pageSize") > 0) {

                JSONArray newsArray = baseJsonArray.getJSONArray("results");

                // For each news in the newsArray, create an {@link News} object
                for (int i = 0; i < newsArray.length(); i++) {

                    // Get a single news at position i within the list_find of news
                    JSONObject currentNews = newsArray.getJSONObject(i);

                    String url = currentNews.getString("webUrl");
                    String section = currentNews.getString("sectionId");
                    String publishedDate = (currentNews.getString("webPublicationDate")).substring(0,10);

                    JSONObject properties = currentNews.getJSONObject("fields");
                    String properties_string = properties.toString();

                    String title = properties.getString("headline");
                    int ratings = 0;
                    if (properties_string.contains("starRating")){
                        ratings = properties.getInt("starRating");
                    }

                    Bitmap image_bitmap;

                    if (!(properties_string.contains("thumbnail"))) {
                        image_bitmap = null;
                    } else {
                        String image_url = properties.getString("thumbnail");
                        image_bitmap = download_Image(image_url);
                    }

                    String publisher = null;

                    JSONArray published_properties = currentNews.getJSONArray("tags");
                    String publishedString = published_properties.toString();
                    for (int x = 0; x < published_properties.length(); x++) {
                        JSONObject publishedDetails = published_properties.getJSONObject(x);
                        if (publishedString.contains("webTitle")) {
                            publisher = publishedDetails.getString("webTitle");
                        }
                    }
                    News news_array = new News(title, section, url, image_bitmap, ratings, publisher, publishedDate);
                    news.add(news_array);
                }
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list_find of news
        return news;
    }

    private static Bitmap download_Image(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("Hub", "Error getting the image from server : " + e.getMessage().toString());
        }
        return bm;
    }
}