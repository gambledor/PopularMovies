package it.globrutto.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * To create the database
 * <p>
 * Created by giuseppelobrutto on 10/03/17.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";

    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor
     *
     * @param context The app context
     */
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuffer ddl = new StringBuffer();
        ddl.append("CREATE TABLE %s (")
                .append("%s INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append("%s INTEGER NOT NULL UNIQUE, ") // movieId
                .append("%s TEXT NOT NULL, ") // originalTitle
                .append("%s TEXT NOT NULL, ") // backdropPath
                .append("%s TEXT NOT NULL, ") // posterPath
                .append("%s TEXT NOT NULL, ") // releaseDate
                .append("%s REAL NOT NULL, ") // voteAverage
                .append("%s INTEGER NOT NULL, ") // voteCount
                .append("%s TEXT NOT NULL") // overview
                .append(");");
        final String DDL_CREATE_MOVIE_TABLE = String.format(
                ddl.toString(),
                MovieContract.MovieEntry.TABLE_NAME,
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
                MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieContract.MovieEntry.COLUMN_VOTE_COUNT,
                MovieContract.MovieEntry.COLUMN_OVERVIEW
        );

        sqLiteDatabase.execSQL(DDL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) { }
}
