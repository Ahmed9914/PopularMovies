package nanodegree.udacity.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Ahmd on 17/02/2018.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    // please enter your api
    public static final String API_KEY = "";

    // Base url building
    public static final String YOUTUBE_ENDPOINT = "https://m.youtube.com/watch";
    public static final String MOVIEDB_API_ENDPOINT = "http://api.themoviedb.org/3/movie/";
    public static final String POPULAR_PART = "popular";
    public static final String TOP_RATED_PART = "top_rated";
    public static final String REVIEWS_PART = "/reviews";
    public static final String TRAILERS_PART = "/videos";
    public static final String POPULAR_MOVIES_QUERY = MOVIEDB_API_ENDPOINT + POPULAR_PART;
    public static final String TOP_RATED_QUERY = MOVIEDB_API_ENDPOINT + TOP_RATED_PART;
    public static final String FAVORITES_QUERY = "favorites-query";

    // Image url building
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String SIZE_PART = "w342/";//"w185/"
    public static final String FULL_IMAGE_URL = BASE_IMAGE_URL + SIZE_PART;

    public static final String API_PARAM = "api_key";
    public static final String YOUTUBE_WATCH_PARAM = "v";


    /**
     * This method builds a url depending on the popularity or rating
     * @param requiredQuery should be TOP_RATED_CHOICE or MOST_POPULAR_CHOICE
     * @return the url which will be sent to respond with JSON
     */
    public static URL buildUrl(String requiredQuery) {
        Uri builtUri = Uri.parse(requiredQuery).buildUpon()
                    .appendQueryParameter(API_PARAM, API_KEY)
                    .build();
        URL url = null;
        Log.v(TAG,"url is:" + builtUri.toString());
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method builds a url for trailers or reviews
     * @param requiredQuery should be REVIEWS_PART or TRAILERS_PART
     * @param movieId the id of the movie
     * @return the url which will be sent to respond with JSON
     */
    public static URL buildExtrasUrl(String movieId, String requiredQuery) {
        requiredQuery = MOVIEDB_API_ENDPOINT + movieId + requiredQuery;
        Uri builtUri = Uri.parse(requiredQuery).buildUpon()
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();
        URL url = null;
        Log.v(TAG,"url is:" + builtUri.toString());
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the response of the HTTP Request
     * @param url this is the URL returned from buildUrl method
     * @return the JSON String which will be parsed in JSONUtils class
    */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * This method builds a trailer url
     * @param key this is the parameter of v in youtube link
     * @return the url of the trailer.
     */
    public static URL buildTrailerLink(String key) {
        Uri builtUri = Uri.parse(YOUTUBE_ENDPOINT).buildUpon()
                .appendQueryParameter(YOUTUBE_WATCH_PARAM, key)
                .build();
        URL url = null;
        Log.v(TAG,"url is:" + builtUri.toString());
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
