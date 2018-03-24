package nanodegree.udacity.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import nanodegree.udacity.popularmovies.adapters.ReviewsAdapter;
import nanodegree.udacity.popularmovies.adapters.TrailersAdapter;
import nanodegree.udacity.popularmovies.data.FavoritesContract;
import nanodegree.udacity.popularmovies.model.Movie;
import nanodegree.udacity.popularmovies.model.Review;
import nanodegree.udacity.popularmovies.model.Trailer;
import nanodegree.udacity.popularmovies.utils.JSONUtils;
import nanodegree.udacity.popularmovies.utils.NetworkUtils;

public class MovieDetails extends AppCompatActivity implements TrailersAdapter.TrailerAdapterOnClickHandler{
    private static final String TAG = MovieDetails.class.getSimpleName();
    private Movie movie;
    private static final int TRAILERS_LOADER_ID = 1;
    private static final int REVIEWS_LOADER_ID = 2;
    private TrailersAdapter trailersAdapter;
    private ReviewsAdapter reviewsAdapter;

    private RecyclerView trailersRecyclerView;
    private RecyclerView reviewsRecyclerView;

    private ImageView posterIv;
    private TextView releaseDateTv;
    private TextView votesTv;
    private TextView overviewTv;
    private TextView favoriteTV;
    private ImageButton favoriteIB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        movie = intent.getParcelableExtra(MoviesActivity.MOVIE_DETAILS_KEY);

        posterIv = findViewById(R.id.poster_iv);
        releaseDateTv = findViewById(R.id.release_date_tv);
        votesTv = findViewById(R.id.votes_tv);
        overviewTv = findViewById(R.id.overview_tv);
        reviewsRecyclerView = findViewById(R.id.reviews_rv);
        trailersRecyclerView = findViewById(R.id.trailers_rv);
        favoriteTV = findViewById(R.id.fav_text);
        favoriteIB = findViewById(R.id.fav_button);

        Picasso.with(this)
                .load(movie.getPosterLink())
                .into(posterIv);

        releaseDateTv.setText(movie.getReleaseDate());
        votesTv.setText(String.valueOf(movie.getVoteAverage()));
        overviewTv.setText(movie.getOverview());

        // Checking is the movie favorite or no
        if (movie.isFavorite()) {
            favoriteIB.setImageResource(R.drawable.ic_favorite_true);
            favoriteTV.setText(R.string.added_to_favorites_text);
        }
        else {
            favoriteIB.setImageResource(R.drawable.ic_favorite_false);
            favoriteTV.setText(R.string.add_to_favorites_text);
        }

        favoriteIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movie.isFavorite()) {
                    addMovieToFavorites();
                    movie.setFavorite(true);
                    favoriteIB.setImageResource(R.drawable.ic_favorite_true);
                    favoriteTV.setText(R.string.added_to_favorites_text);
                } else{
                    removeFromFavorites();
                    movie.setFavorite(false);
                    favoriteIB.setImageResource(R.drawable.ic_favorite_false);
                    favoriteTV.setText(R.string.add_to_favorites_text);
                    //Toast.makeText(MovieDetails.this, "the movie exists on favorites", Toast.LENGTH_SHORT).show();
                }
                MoviesActivity.favoritesDbChanged = true;

            }
        });

        // Reviews Loader
        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        reviewsAdapter = new ReviewsAdapter(this);
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        LoaderManager.LoaderCallbacks<List<Review>> reviewsCallback = reviewsLoader;
        getSupportLoaderManager().initLoader(REVIEWS_LOADER_ID, null, reviewsCallback);

        // Trailers Loader
        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(this);
        trailersRecyclerView.setLayoutManager(trailersLayoutManager);
        trailersAdapter = new TrailersAdapter(this, this);
        trailersRecyclerView.setAdapter(trailersAdapter);
        LoaderManager.LoaderCallbacks<List<Trailer>> trailersCallback = trailersLoader;
        getSupportLoaderManager().initLoader(TRAILERS_LOADER_ID, null, trailersCallback);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private LoaderManager.LoaderCallbacks<List<Review>> reviewsLoader =
            new LoaderManager.LoaderCallbacks<List<Review>>() {
                @Override
                public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
                    return new AsyncTaskLoader<List<Review>>(MovieDetails.this){
                        List<Review> reviews = null;

                        @Override
                        protected void onStartLoading() {
                            if (reviews != null) {
                                deliverResult(reviews);
                            }
                            else {
                                //loadingIndicator.setVisibility(View.VISIBLE);
                                forceLoad();
                            }
                        }

                        @Override
                        public List<Review> loadInBackground() {
                            URL requestUrl = NetworkUtils.buildExtrasUrl(movie.getId(), NetworkUtils.REVIEWS_PART);

                            try {
                                String jsonResponse = NetworkUtils
                                        .getResponseFromHttpUrl(requestUrl);

                                List<Review> returnedReviews = JSONUtils.parseReviewsJSON(jsonResponse);

                                return returnedReviews;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                        @Override
                        public void deliverResult(List<Review> data) {
                            reviews = data;
                            super.deliverResult(data);
                        }
                    };
                }

                @Override
                public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
                    //loadingIndicator.setVisibility(View.INVISIBLE);
                    reviewsAdapter.loadReviews(data);
                    if (data == null) {
                        reviewsRecyclerView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<Review>> loader) {

                }
            };

    private LoaderManager.LoaderCallbacks<List<Trailer>> trailersLoader =
            new LoaderManager.LoaderCallbacks<List<Trailer>>() {
                @Override
                public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
                    return new AsyncTaskLoader<List<Trailer>>(MovieDetails.this){
                        List<Trailer> trailers = null;

                        @Override
                        protected void onStartLoading() {
                            if (trailers != null) {
                                deliverResult(trailers);
                            }
                            else {
                                forceLoad();
                            }
                        }

                        @Override
                        public List<Trailer> loadInBackground() {
                            URL requestUrl = NetworkUtils.buildExtrasUrl(movie.getId(), NetworkUtils.TRAILERS_PART);

                            try {
                                String jsonResponse = NetworkUtils
                                        .getResponseFromHttpUrl(requestUrl);

                                List<Trailer> returnedTrailers = JSONUtils.parseTrailersJSON(jsonResponse);

                                return returnedTrailers;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }

                        @Override
                        public void deliverResult(List<Trailer> data) {
                            trailers = data;
                            super.deliverResult(data);
                        }
                    };
                }

                @Override
                public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
                    trailersAdapter.loadTrailers(data);
                    if (data == null) {
                        trailersRecyclerView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<Trailer>> loader) {

                }
            };

    void addMovieToFavorites(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesContract.favoritesEnteries.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(FavoritesContract.favoritesEnteries.COLUMN_MOVIE_POSTER, movie.getPosterLink());
        contentValues.put(FavoritesContract.favoritesEnteries.COLUMN_MOVIE_TITLE, movie.getTitle());
        Uri uri = getContentResolver().insert(FavoritesContract.favoritesEnteries.CONTENT_URI, contentValues);
        if(uri != null) {
            Toast.makeText(getBaseContext(),
                    "Added "+movie.getTitle()+" to favorites",
                    Toast.LENGTH_LONG).show();
        }
    }

    void removeFromFavorites(){
        int deletedRows;
        deletedRows = getContentResolver().delete(
                FavoritesContract.favoritesEnteries.CONTENT_URI
                        .buildUpon()
                        .appendPath(movie.getId())
                        .build(),
                null,
                null);
        if (deletedRows == 1){
            Toast.makeText(getBaseContext(),
                    "Removed "+movie.getTitle()+" from favorites",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(Trailer trailer) {
        //Toast.makeText(this,trailer.getLink().toString(), Toast.LENGTH_SHORT).show();
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
        trailerIntent.setData(Uri.parse(trailer.getLink().toString()));
        if (trailerIntent.resolveActivity(getPackageManager())!=null){
            startActivity(trailerIntent);
        }
    }

}
