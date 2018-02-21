package nanodegree.udacity.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Property;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import nanodegree.udacity.popularmovies.model.Movie;

public class MovieDetails extends AppCompatActivity {

    private ImageView posterIv;
    private TextView titleTv;
    private TextView releaseDateTv;
    private TextView votesTv;
    private TextView overviewTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        posterIv = findViewById(R.id.poster_iv);
        titleTv = findViewById(R.id.title_tv);
        releaseDateTv = findViewById(R.id.release_date_tv);
        votesTv = findViewById(R.id.votes_tv);
        overviewTv = findViewById(R.id.overview_tv);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(MoviesActivity.MOVIE_DETAILS_KEY);

        Picasso.with(this)
                .load(movie.getPosterLink())
                .into(posterIv);
        titleTv.setText(movie.getTitle());
        releaseDateTv.setText(movie.getReleaseDate());
        votesTv.setText(String.valueOf(movie.getVoteAverage()));
        overviewTv.setText(movie.getOverview());

    }
}
