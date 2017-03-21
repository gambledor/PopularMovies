package it.globrutto.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import it.globrutto.popularmovies.data.MovieContract;
import it.globrutto.popularmovies.data.MovieDbHelper;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by giuseppelobrutto on 14/03/17.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private final Context mContext = InstrumentationRegistry.getTargetContext();

    private final Class mDbHelperClass = MovieDbHelper.class;

    @Before
    public void setUp() {
        deleteTheDatabase();
    }

    @Test
    public void createDatabseTest() throws Exception {
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String databaseIsNotOpen = "The database should be open an isn't";
        assertEquals(databaseIsNotOpen, true, database.isOpen());

        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" + MovieContract.MovieEntry.TABLE_NAME
                        + "'", null);

        String errorInCreatingDatabase = "Error: the database has not been created correctly";
        assertTrue(errorInCreatingDatabase, tableNameCursor.moveToFirst());

        assertEquals("Error: your database was created without the expected tables.",
                MovieContract.MovieEntry.TABLE_NAME, tableNameCursor.getString(0));

        tableNameCursor.close();
    }

    @Test
    public void insertSingleRecordTest() throws Exception {
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues cv = getMovieContentValues();

        long firstRowId = database.insert(
                MovieContract.MovieEntry.TABLE_NAME, null, cv);

        assertNotEquals("Unable to insert into database", -1, firstRowId);

        Cursor cursor = database.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        String emptyQueryError = "Error: no records returned from query";
        assertTrue(emptyQueryError, cursor.moveToFirst());

        cursor.close();
        dbHelper.close();
    }

    @Test
    public void autoincrementTest() throws Exception {
        insertSingleRecordTest();

        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues cv = getMovieContentValues();

        long firstRowId = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);

        long secondRowId = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
        assertEquals("ID Autoincrement test failed!", firstRowId + 1, secondRowId);

        dbHelper.close();
    }

    @NonNull
    private ContentValues getMovieContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 550);
        cv.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, "Fight Club");
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "1999-10-12");
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, 7.8);
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, 3439);
        cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg");
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "");
        cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "A ticketing-time-bomb insomniac and a slipery soap (...)");
        return cv;
    }

    void deleteTheDatabase() {
        try {
            Field f = mDbHelperClass.getDeclaredField("DATABASE_NAME");
            f.setAccessible(true);
            mContext.deleteDatabase((String) f.get(null));
        } catch (NoSuchFieldException e) {
            fail("Make sure you hav a member called DATABASE_NAME in the MovieDbHelper class");
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
    }

}
