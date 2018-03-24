package nanodegree.udacity.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ahmd on 22/03/2018.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorites.db";
    public static final int VERSION = 1;

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE =

                "CREATE TABLE " + FavoritesContract.favoritesEnteries.TABLE_NAME + " (" +

                /*
                 * WeatherEntry did not explicitly declare a column called "_ID". However,
                 * WeatherEntry implements the interface, "BaseColumns", which does have a field
                 * named "_ID". We use that here to designate our table's primary key.
                 */
                        FavoritesContract.favoritesEnteries._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        FavoritesContract.favoritesEnteries.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +

                        FavoritesContract.favoritesEnteries.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +

                        FavoritesContract.favoritesEnteries.COLUMN_MOVIE_POSTER + " TEXT NOT NULL);";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.favoritesEnteries.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
