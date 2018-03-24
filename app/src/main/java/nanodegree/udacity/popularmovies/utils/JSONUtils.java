package nanodegree.udacity.popularmovies.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nanodegree.udacity.popularmovies.data.FavoritesContract;
import nanodegree.udacity.popularmovies.model.Movie;
import nanodegree.udacity.popularmovies.model.Review;
import nanodegree.udacity.popularmovies.model.Trailer;

import static nanodegree.udacity.popularmovies.utils.NetworkUtils.FULL_IMAGE_URL;
import static nanodegree.udacity.popularmovies.utils.NetworkUtils.YOUTUBE_ENDPOINT;

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
    // Id
    public static final String JSON_ID_KEY = "id";

    // JSON reviews
    public static final String JSON_AUTHOR_KEY = "author";
    public static final String JSON_CONTENT_KEY = "content";

    // JSON trailers
    public static final String JSON_TRAILER_NAME_KEY = "name";
    public static final String JSON_TRAILER_KEY = "key";


    /**
     * This method will parse the JSON String to return a list of Movie objects
     * @param moviesJSON JSON string of response from TOP_RATED_QUERY or POPULAR_MOVIES_QUERY
     * @return a list of Movie objects to be populated through the MoviesAdapter
     */
    public static List<Movie> parseMovieJSON(Context context,String moviesJSON) {
        List<Movie> movies = new ArrayList<Movie>();
        try {
            JSONObject moviesJSONObject = new JSONObject(moviesJSON);
            if (moviesJSONObject.has(JSON_RESULTS_KEY)) {
                JSONArray moviesArray = moviesJSONObject.getJSONArray(JSON_RESULTS_KEY);
                for (int i = 0; i < moviesArray.length(); i++) {
                    Movie currentMovie = new Movie();
                    JSONObject currentMovieJSON = moviesArray.getJSONObject(i);
                    // Title
                    if (currentMovieJSON.has(JSON_TITLE_KEY)) {
                        currentMovie.setTitle(currentMovieJSON.optString(JSON_TITLE_KEY));
                        Log.v(TAG,"Title is:" + currentMovieJSON.optString(JSON_TITLE_KEY));
                    }
                    // ReleaseDate
                    if (currentMovieJSON.has(JSON_RELEASE_DATE_KEY)) {
                        currentMovie.setReleaseDate(currentMovieJSON.optString(JSON_RELEASE_DATE_KEY));
                        //Log.v(TAG,"ReleaseDate is:" + currentMovieJSON.optString(JSON_RELEASE_DATE_KEY));
                    }
                    // PosterLink
                    if (currentMovieJSON.has(JSON_POSTER_LINK_KEY)) {
                        currentMovie.setPosterLink(FULL_IMAGE_URL + currentMovieJSON.optString(JSON_POSTER_LINK_KEY));
                        //Log.v(TAG,"PosterLink is:" + currentMovieJSON.optString(JSON_POSTER_LINK_KEY));
                    }
                    // VoteAverage
                    if (currentMovieJSON.has(JSON_VOTES_KEY)) {
                        currentMovie.setVoteAverage(currentMovieJSON.getDouble(JSON_VOTES_KEY));
                        //Log.v(TAG,"VoteAverage is:" + currentMovieJSON.getDouble(JSON_VOTES_KEY));
                    }
                    // Overview
                    if (currentMovieJSON.has(JSON_OVERVIEW_KEY)) {
                        currentMovie.setOverview(currentMovieJSON.optString(JSON_OVERVIEW_KEY));
                        //Log.v(TAG,"Overview is:" + currentMovieJSON.optString(JSON_OVERVIEW_KEY));
                    }
                    // Id
                    if (currentMovieJSON.has(JSON_ID_KEY)) {
                        currentMovie.setId(currentMovieJSON.optString(JSON_ID_KEY));
                        //Log.v(TAG,"Id is:" + currentMovieJSON.optString(JSON_ID_KEY));
                    }
                    // IsFavorite
                    if (checkIsFavorite(context, currentMovie)){
                        currentMovie.setFavorite(true);
                    }
                    else{
                        currentMovie.setFavorite(false);
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

    /**
     * This method will parse the JSON String to return a list of Review objects
     * @param reviewsJSON JSON string of response from reviews
     * @return a list of Review objects to be populated through the ReviewsAdapter
     */
    public static List<Review> parseReviewsJSON(String reviewsJSON) {
        List<Review> reviews = new ArrayList<Review>();
        try {
            JSONObject reviewsJSONObject = new JSONObject(reviewsJSON);
            if (reviewsJSONObject.has(JSON_RESULTS_KEY)) {
                JSONArray reviewsArray = reviewsJSONObject.getJSONArray(JSON_RESULTS_KEY);
                for (int i = 0; i < reviewsArray.length(); i++) {
                    Review currentReview = new Review();
                    JSONObject currentReviewJSON = reviewsArray.getJSONObject(i);
                    // Author
                    if (currentReviewJSON.has(JSON_AUTHOR_KEY)) {
                        currentReview.setAuthor(currentReviewJSON.optString(JSON_AUTHOR_KEY));
                        //Log.v(TAG,"Author is:" + currentReview.getAuthor());
                    }
                    // Content
                    if (currentReviewJSON.has(JSON_CONTENT_KEY)) {
                        currentReview.setContent(currentReviewJSON.optString(JSON_CONTENT_KEY));
                        //Log.v(TAG,"Content is:" + currentReview.getContent());
                    }
                    reviews.add(currentReview);
                }
                return reviews;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return reviews;
    }

    /**
     * This method will parse the JSON String to return a list of Trailer objects
     * @param trailersJSON JSON string of response from reviews
     * @return a list of Trailer objects to be populated through the TrailersAdapter
     */
    public static List<Trailer> parseTrailersJSON(String trailersJSON) {
        List<Trailer> trailers = new ArrayList<Trailer>();
        try {
            JSONObject trailersJSONObject = new JSONObject(trailersJSON);
            if (trailersJSONObject.has(JSON_RESULTS_KEY)) {
                JSONArray trailersArray = trailersJSONObject.getJSONArray(JSON_RESULTS_KEY);
                for (int i = 0; i < trailersArray.length(); i++) {
                    Trailer currentTrailer = new Trailer();
                    JSONObject currentTrailerJSON = trailersArray.getJSONObject(i);
                    // Name
                    if (currentTrailerJSON.has(JSON_TRAILER_NAME_KEY)) {
                        currentTrailer.setName(currentTrailerJSON.optString(JSON_TRAILER_NAME_KEY));
                    }
                    // Content
                    if (currentTrailerJSON.has(JSON_TRAILER_KEY)) {
                        currentTrailer.setLink(
                                NetworkUtils.buildTrailerLink(currentTrailerJSON.optString(JSON_TRAILER_KEY)));
                    }


                    trailers.add(currentTrailer);
                }
                return trailers;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return trailers;
    }

    public static boolean checkIsFavorite(Context context, Movie movie) {
        Cursor cursor = context.getContentResolver().query(
                FavoritesContract.favoritesEnteries.CONTENT_URI
                        .buildUpon()
                        .appendPath(movie.getId())
                        .build(),
                null,
                null,
                null,
                FavoritesContract.favoritesEnteries.COLUMN_MOVIE_ID);
        if (cursor.getCount() > 0) return true;
        return false;
        //Toast.makeText(MovieDetails.this, isFavorite+"", Toast.LENGTH_SHORT).show();
    }

}
