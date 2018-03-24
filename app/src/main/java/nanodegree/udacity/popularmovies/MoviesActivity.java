package nanodegree.udacity.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import nanodegree.udacity.popularmovies.adapters.FavoritesAdapter;
import nanodegree.udacity.popularmovies.adapters.MoviesAdapter;
import nanodegree.udacity.popularmovies.data.FavoritesContract;
import nanodegree.udacity.popularmovies.model.Movie;
import nanodegree.udacity.popularmovies.utils.JSONUtils;
import nanodegree.udacity.popularmovies.utils.NetworkUtils;

public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterOnClickHandler {

    public static boolean favoritesDbChanged = false;
    private static final String TAG = MoviesActivity.class.getSimpleName();
    private static final int numberOfItemsPerRow = 2;
    private static final int MOVIE_LOADER_ID = 0;
    private static final int FAVORITE_LOADER_ID = 1;
    public static final String MOVIE_DETAILS_KEY = "movie-details";

    private MoviesAdapter moviesAdapter;
    private FavoritesAdapter favoritesAdapter;

    private RecyclerView recyclerView;
    private TextView errorMessageDisplay;
    private ProgressBar loadingIndicator;

    // Default sorting is by popular movies
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
        favoritesAdapter = new FavoritesAdapter(this, this);
        setChosenCategory(sortingCategory);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (favoritesDbChanged) {
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, allMoviesLoader);
            getSupportLoaderManager().restartLoader(FAVORITE_LOADER_ID, null, favoriteMoviesLoader);
            favoritesDbChanged = false;
        }

    }

    private LoaderManager.LoaderCallbacks<Cursor> favoriteMoviesLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    return new AsyncTaskLoader<Cursor>(MoviesActivity.this) {

                        Cursor moviesCursor = null;

                        @Override
                        protected void onStartLoading() {
                            if (moviesCursor != null) {
                                deliverResult(moviesCursor);
                            } else {
                                forceLoad();
                            }
                        }

                        @Override
                        public Cursor loadInBackground() {
                            try {
                                return MoviesActivity.this
                                        .getContentResolver()
                                        .query(FavoritesContract.favoritesEnteries.CONTENT_URI,
                                        null,
                                        null,
                                        null,
                                                FavoritesContract.favoritesEnteries._ID);

                            } catch (Exception e) {
                                Log.e(TAG, "Failed to asynchronously load data.");
                                e.printStackTrace();
                                return null;
                            }
                        }

                        public void deliverResult(Cursor data) {
                            moviesCursor = data;
                            super.deliverResult(data);
                        }
                    };
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    loadingIndicator.setVisibility(View.INVISIBLE);
                    //recyclerView.setVisibility(View.VISIBLE);
                    favoritesAdapter.swapCursor(data);
                    if (data == null) {
                        showErrorMessage();
                    } else {
                        showMoviesView();
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {

                }
            };

    private LoaderManager.LoaderCallbacks<List<Movie>> allMoviesLoader =
            new LoaderManager.LoaderCallbacks<List<Movie>>() {
                @Override
                public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
                    return new AsyncTaskLoader<List<Movie>>(MoviesActivity.this) {
                        List<Movie> movies = null;

                        @Override
                        protected void onStartLoading() {
                            if (movies != null) {
                                deliverResult(movies);
                            } else {
                                //recyclerView.setVisibility(View.INVISIBLE);
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

                                List<Movie> returnedMovies = JSONUtils.parseMovieJSON(MoviesActivity.this, jsonResponse);

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
                    //recyclerView.setVisibility(View.VISIBLE);
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
            };

    private void invalidateData() {
        if (sortingCategory.equals(NetworkUtils.FAVORITES_QUERY)){
            favoritesAdapter.swapCursor(null);
        } else {
            moviesAdapter.loadMovies(null);
        }
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

        switch (id) {

            case R.id.action_sort_by_popularity:
                sortingCategory = NetworkUtils.POPULAR_MOVIES_QUERY;
                invalidateData();
                setChosenCategory(sortingCategory);
                return true;


            case R.id.action_sort_by_rating:
                sortingCategory = NetworkUtils.TOP_RATED_QUERY;
                invalidateData();
                setChosenCategory(sortingCategory);
                return true;

            case R.id.action_sort_by_favorite:
                sortingCategory = NetworkUtils.FAVORITES_QUERY;
                invalidateData();
                setChosenCategory(sortingCategory);
                return true;

            default:
                return super.onOptionsItemSelected(item);
            }
        }


    @Override
    public void onClick(Movie movie) {
        //Log.d(TAG, "clicked on "+movie.getTitle());
        if (sortingCategory.equals(NetworkUtils.POPULAR_MOVIES_QUERY) ||
                sortingCategory.equals(NetworkUtils.TOP_RATED_QUERY)) {
            Intent intentToStartDetailsActivity = new Intent(this, MovieDetails.class);
            intentToStartDetailsActivity.putExtra(MOVIE_DETAILS_KEY, movie);
            startActivity(intentToStartDetailsActivity);
        }
    }

    void setChosenCategory(String category){
        if (category.equals(NetworkUtils.FAVORITES_QUERY)){
            recyclerView.setAdapter(favoritesAdapter);
            getSupportLoaderManager().restartLoader(FAVORITE_LOADER_ID, null, favoriteMoviesLoader);
        } else{
            recyclerView.setAdapter(moviesAdapter);
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, allMoviesLoader);
        }
    }
}
