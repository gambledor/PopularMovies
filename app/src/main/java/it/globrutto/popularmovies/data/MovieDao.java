package it.globrutto.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import it.globrutto.popularmovies.model.Movie;

/**
 * Created by giuseppelobrutto on 16/03/17.
 */

public class MovieDao {

    private static final String TAG = MovieDao.class.getSimpleName();

    MovieDbHelper movieDbHelper = null;

    public MovieDao(Context context) {
        // instantiate the movie db helper class
        movieDbHelper = new MovieDbHelper(context);
        // Add fake data
//        TestUtil.insertFakeData(mDb);
    }

    public List<Movie> getAllFavoriteMovie() {
        SQLiteDatabase db = movieDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID
        );

        List<Movie> movies = null;
        if (cursor.moveToFirst()) {
            movies = new ArrayList<>(cursor.getCount());
            do {
                Movie movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
                movie.setOriginalTitle(cursor.getString(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
                movie.setBackdropPath(cursor.getString(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH)));
                movie.setPosterPath(cursor.getString(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
                movie.setReleaseDate(cursor.getString(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
                movie.setVoteAverage(cursor.getFloat(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
                movie.setVoteCount(cursor.getInt(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT)));
                movie.setOverview(cursor.getString(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
//                Log.d(TAG, "movie in db -> " + movie);
                movies.add(movie);
            } while (cursor.moveToNext());
        }

        return movies;
    }

    public void addFavoriteMovie(Movie movie) {
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        cv.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());

        db.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
        db.close();
    }
}
