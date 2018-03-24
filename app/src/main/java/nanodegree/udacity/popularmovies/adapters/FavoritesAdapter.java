package nanodegree.udacity.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;

import com.squareup.picasso.Picasso;

import nanodegree.udacity.popularmovies.data.FavoritesContract;

/**
 * Created by Ahmd on 24/03/2018.
 */

public class FavoritesAdapter extends MoviesAdapter {
    Context context;
    Cursor favoritesCursor;

    public FavoritesAdapter(Context mContext, MovieAdapterOnClickHandler mClickHandler) {
        super(mContext, null);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        int posterIndex = favoritesCursor.getColumnIndex(FavoritesContract.favoritesEnteries.COLUMN_MOVIE_POSTER);
        favoritesCursor.moveToPosition(position);
        String posterLink = favoritesCursor.getString(posterIndex);
        Picasso.with(context).load(posterLink).into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        if (favoritesCursor == null) {
            return 0;
        }
        return favoritesCursor.getCount();
    }

    public Cursor swapCursor(Cursor mCursor) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (favoritesCursor == mCursor) {
            return null; // bc nothing has changed
        }
        Cursor temp = favoritesCursor;
        this.favoritesCursor = mCursor; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (mCursor != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
