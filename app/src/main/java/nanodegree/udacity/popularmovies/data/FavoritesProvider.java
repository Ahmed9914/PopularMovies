package nanodegree.udacity.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Ahmd on 22/03/2018.
 */

public class FavoritesProvider extends ContentProvider {

    public static final int FAVORITES = 100;
    public static final int FAVORITE_WITH_ID = 101;
    private FavoritesDbHelper favoritesDbHelper;
    UriMatcher sMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_FAVORITES, FAVORITES);
        matcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_FAVORITES+"/#", FAVORITE_WITH_ID);
        return matcher;
    }
    @Override
    public boolean onCreate() {
        favoritesDbHelper = new FavoritesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = favoritesDbHelper.getReadableDatabase();
        int match = sMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case FAVORITES:
                retCursor =  db.query(FavoritesContract.favoritesEnteries.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case FAVORITE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = FavoritesContract.favoritesEnteries.COLUMN_MOVIE_ID + "=?";
                String[] mSelectionArgs = new String[]{id};
                retCursor =  db.query(FavoritesContract.favoritesEnteries.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    // This will be called in MovieDetails if the movie doesn't exist in the database and the fab is pressed
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = favoritesDbHelper.getWritableDatabase();
        int match = sMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case FAVORITES:
                long id = db.insert(FavoritesContract.favoritesEnteries.TABLE_NAME,
                    null,
                    contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(uri, id);
                } else {
                    throw new android.database.SQLException("Failed to add movie to db");
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;
        switch (sMatcher.match(uri)) {
            case FAVORITE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = FavoritesContract.favoritesEnteries.COLUMN_MOVIE_ID + "=?";
                String[] mSelectionArgs = new String[]{id};
                numRowsDeleted = favoritesDbHelper.getWritableDatabase().delete(
                        FavoritesContract.favoritesEnteries.TABLE_NAME,
                        mSelection,
                        mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
