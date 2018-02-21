package nanodegree.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import nanodegree.udacity.popularmovies.model.Movie;
import nanodegree.udacity.popularmovies.utils.JSONUtils;
import nanodegree.udacity.popularmovies.utils.NetworkUtils;

public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterOnClickHandler
        ,LoaderManager.LoaderCallbacks<List<Movie>> {
        
    private static final String TAG = MoviesActivity.class.getSimpleName();
    private static final int numberOfItemsPerRow = 2;
    private static final int MOVIE_LOADER_ID = 0;
    public static final String MOVIE_DETAILS_KEY = "movie-details";

    private MoviesAdapter moviesAdapter;

    private RecyclerView recyclerView;
    private TextView errorMessageDisplay;
    private ProgressBar loadingIndicator;

    // Default sorting is by top rated movies
    private String sortingCategory = NetworkUtils.TOP_RATED_QUERY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing views
        recyclerView = findViewById(R.id.movies_rv);
        errorMessageDisplay = findViewById(R.id.error_message_display_tv);
        loadingIndicator = findViewById(R.id.loading_indicator_pb);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfItemsPerRow);
        recyclerView.setLayoutManager(gridLayoutManager);
        moviesAdapter = new MoviesAdapter(this, this);
        recyclerView.setAdapter(moviesAdapter);

        LoaderManager.LoaderCallbacks<List<Movie>> callback = MoviesActivity.this;
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, callback);

    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this){
            List<Movie> movies = null;

            @Override
            protected void onStartLoading() {
                if (movies != null) {
                    deliverResult(movies);
                } else {
                    loadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {
                URL requestUrl = NetworkUtils.buildUrl(sortingCategory);

                try {
                    String jsonResponse = NetworkUtils
                            .getResponseFromHttpUrl(requestUrl);

                    List<Movie> returnedMovies = JSONUtils.parseJSON(jsonResponse);

                    return returnedMovies;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            public void deliverResult(List<Movie> data) {
                movies = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        loadingIndicator.setVisibility(View.INVISIBLE);
        moviesAdapter.loadMovies(data);
        if (data == null) {
            showErrorMessage();
        } else {
            showMoviesView();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    // For future purposes
    private void invalidateData() {
        moviesAdapter.loadMovies(null);
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showMoviesView() {
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sorting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_by_popularity) {
            sortingCategory = NetworkUtils.POPULAR_MOVIES_QUERY;
            invalidateData();
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            return true;
        }

        if (id == R.id.action_sort_by_rating) {
            sortingCategory = NetworkUtils.TOP_RATED_QUERY;
            invalidateData();
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movie) {
        //Log.d(TAG, "clicked on "+movie.getTitle());
        Intent intentToStartDetailsActivity = new Intent(this, MovieDetails.class);
        intentToStartDetailsActivity.putExtra(MOVIE_DETAILS_KEY, movie);
        startActivity(intentToStartDetailsActivity);
    }
}
