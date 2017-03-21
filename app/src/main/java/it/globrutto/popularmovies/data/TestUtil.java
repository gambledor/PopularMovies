package it.globrutto.popularmovies.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giuseppelobrutto on 13/03/17.
 */

public class TestUtil {

    private static final String TAG = TestUtil.class.getSimpleName();

    /**
     * To insert fake data into movie table
     *
     * @param db The dataase
     */
    public static void insertFakeData(SQLiteDatabase db) {
        if (db == null) return;

        List<ContentValues> values = new ArrayList<>();
        ContentValues cv = new ContentValues();

        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 550);
        cv.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, "Fight Club");
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "1999-10-12");
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, 7.8);
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, 3439);
        cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg");
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "");
        cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "A ticketing-time-bomb insomniac and a slipery soap (...)");
        values.add(cv);

        cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 551);
        cv.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, "Fight Club 2");
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2001-10-12");
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, 7.8);
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, 3439);
        cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg");
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "");
        cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "A ticketing-time-bomb insomniac and a slipery soap (...)");
        values.add(cv);

        try {
            db.beginTransaction();
            // clear the table first
            db.delete(MovieContract.MovieEntry.TABLE_NAME, null, null);
            for (ContentValues value : values) {
                db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }
    }
}
