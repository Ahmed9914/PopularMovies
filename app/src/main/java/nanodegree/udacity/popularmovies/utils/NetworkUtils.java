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
    public static final String BASE_API_URL = "http://api.themoviedb.org/3";
    public static final String POPULAR_PART = "/movie/popular";
    public static final String TOP_RATED_PART = "/movie/top_rated";
    public static final String POPULAR_MOVIES_QUERY = BASE_API_URL + POPULAR_PART;
    public static final String TOP_RATED_QUERY = BASE_API_URL + TOP_RATED_PART;

    // Image url building
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String SIZE_PART = "w342/";//"w185/"
    public static final String FULL_IMAGE_URL = BASE_IMAGE_URL + SIZE_PART;

    public static final String API_PARAM = "api_key";

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

}
