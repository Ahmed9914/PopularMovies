package nanodegree.udacity.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import nanodegree.udacity.popularmovies.model.Movie;

/**
 * Created by Ahmd on 16/02/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{
    private List<Movie> movies;
    private Context context;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }
    private MovieAdapterOnClickHandler clickHandler;

    public MoviesAdapter(Context mContext, MovieAdapterOnClickHandler mClickHandler) {
        context = mContext;
        clickHandler = mClickHandler;
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForGridItem = R.layout.grid_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForGridItem, parent, shouldAttachToParentImmediately);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        String posterLink = movies.get(position).getPosterLink();
        Picasso.with(context).load(posterLink).into(holder.posterImageView);

    }

    @Override
    public int getItemCount() {
        if (movies == null || movies.size() == 0) return 0;
        return movies.size();
    }

    public void loadMovies(List<Movie> mMovies) {
        movies = mMovies;
        notifyDataSetChanged();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView posterImageView;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.logo_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie currentMovie = movies.get(adapterPosition);
            clickHandler.onClick(currentMovie);
        }
    }
}
