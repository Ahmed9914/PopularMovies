package nanodegree.udacity.popularmovies.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nanodegree.udacity.popularmovies.model.Movie;

import static nanodegree.udacity.popularmovies.utils.NetworkUtils.FULL_IMAGE_URL;

/**
 * Created by Ahmd on 17/02/2018.
 */

public class JSONUtils {
    private static final String TAG = JSONUtils.class.getSimpleName();

    // JSON keys
    // This is the parent JSONArray key for all the results
    public static final String JSON_RESULTS_KEY = "results";
    // Title
    public static final String JSON_TITLE_KEY = "title";
    // ReleaseDate
    public static final String JSON_RELEASE_DATE_KEY = "release_date";
    // PosterLink (partial)
    public static final String JSON_POSTER_LINK_KEY = "poster_path";
    // VoteAverage
    public static final String JSON_VOTES_KEY = "vote_average";
    // Overview
    public static final String JSON_OVERVIEW_KEY = "overview";

    /**
     * This method will parse the JSON String to return a list of Movie objects
     * @param mMoviesJSON JSON string of reponse from TOP_RATED_QUERY or POPULAR_MOVIES_QUERY
     * @return a list of Movie objects to be populated through the MoviesAdapter
     */
    public static List<Movie> parseJSON(String mMoviesJSON) {
        List<Movie> movies = new ArrayList<Movie>();
        try {
            JSONObject moviesJSON = new JSONObject(mMoviesJSON);
            if (moviesJSON.has(JSON_RESULTS_KEY)) {
                JSONArray moviesArray = moviesJSON.getJSONArray(JSON_RESULTS_KEY);
                for (int i = 0; i < moviesArray.length(); i++) {
                    Movie currentMovie = new Movie();
                    JSONObject currentMovieJSON = moviesArray.getJSONObject(i);
                    // Title
                    if (currentMovieJSON.has(JSON_TITLE_KEY)) {
                        currentMovie.setTitle(currentMovieJSON.optString(JSON_TITLE_KEY));
                    }
                    // ReleaseDate
                    if (currentMovieJSON.has(JSON_RELEASE_DATE_KEY)) {
                        currentMovie.setReleaseDate(currentMovieJSON.optString(JSON_RELEASE_DATE_KEY));
                    }
                    // PosterLink
                    if (currentMovieJSON.has(JSON_POSTER_LINK_KEY)) {
                        currentMovie.setPosterLink(FULL_IMAGE_URL + currentMovieJSON.optString(JSON_POSTER_LINK_KEY));
                    }
                    // VoteAverage
                    if (currentMovieJSON.has(JSON_VOTES_KEY)) {
                        currentMovie.setVoteAverage(currentMovieJSON.getDouble(JSON_VOTES_KEY));
                    }
                    // Overview
                    if (currentMovieJSON.has(JSON_OVERVIEW_KEY)) {
                        currentMovie.setOverview(currentMovieJSON.optString(JSON_OVERVIEW_KEY));
                    }
                    movies.add(currentMovie);
                }
                return movies;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return movies;
    }

}
