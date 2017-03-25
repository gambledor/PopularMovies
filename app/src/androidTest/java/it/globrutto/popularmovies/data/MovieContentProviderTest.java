package it.globrutto.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

/**
 * Created by giuseppelobrutto on 24/03/17.
 */
@RunWith(AndroidJUnit4.class)
public class MovieContentProviderTest {

    public static final String TAG = MovieContentProvider.class.getSimpleName();

    // Context used to access various parts of the system
    private final Context mContext = InstrumentationRegistry.getTargetContext();

    private static final Uri TEST_MOVIES = MovieContract.MovieEntry.CONTENT_URI;

    private static final Uri TEST_MOVIE_WITH_ID = TEST_MOVIES.buildUpon().appendEncodedPath("1").build();

    private static final int i = 0;

    @Before
    public void setUp() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(MovieContract.MovieEntry.TABLE_NAME, null, null);
        database.close();
    }

    @Test
    public void testProviderRegistry() {
        String packageName = mContext.getPackageName();
        String movieProviderClassName = MovieContentProvider.class.getName();
        ComponentName componentName = new ComponentName(packageName, movieProviderClassName);

        try {
            PackageManager pm = mContext.getPackageManager();
            // The providerInfo will contain the authority
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            String actualAuthority = providerInfo.authority;
            String expectedAuthority = packageName;
            String msg = String.format(
                    "Error: MovieContentProvider registered with authority: %s instead of expexcted authority: %s",
                    actualAuthority,
                    expectedAuthority
            );
            assertEquals(msg, actualAuthority, expectedAuthority);
        } catch (PackageManager.NameNotFoundException e) {
            String msg = String.format("Error: MovieContentProvider not registered at %s", mContext.getPackageName());
            fail(msg);
        }
    }

    /**
     * It tests the uriMatcher returns the correct integer value for each of Uri types that the ContentProvider can
     * handle.
     */
    @Test
    public void testUriMatcher() {
        UriMatcher testUriMatcher = MovieContentProvider.buildUriMactcher();

        // Test that the code returned from our matcher matches the expected MOVIES int
        String movieUriDoesNotMatch = "Error: The TASKS URI was matched incorrectly.";
        int actualMovieMatchCode = testUriMatcher.match(TEST_MOVIES);
        int expectedMovieMatchCode = MovieContentProvider.MOVIES;
        assertEquals(movieUriDoesNotMatch, actualMovieMatchCode, expectedMovieMatchCode);

        // Test that the code returned from our matcher matches the expected MOVIES_WITH_ID
        String movieWithIdDoesNotMatch = "Error: The MOVIE_WITH_ID URI was matched incorrectly";
        int actualMovieWithIdCode = testUriMatcher.match(TEST_MOVIE_WITH_ID);
        int expectedMovieWithIdCode = MovieContentProvider.MOVIES_WITH_ID;
        assertEquals(movieWithIdDoesNotMatch, actualMovieMatchCode, expectedMovieMatchCode);
    }

    @Test
    public void testInserMovie() {
        // create values to be inserted
        ContentValues cv = new ContentValues();

        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 550);
        cv.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, "Fight Club");
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "1999-10-12");
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, 7.8);
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, 3439);
        cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg");
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "");
        cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "A ticketing-time-bomb insomniac and a slipery soap (...)");

        // Allows us to test if notifyChange was called appropriately
        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = mContext.getContentResolver();

        contentResolver.registerContentObserver(
                MovieContract.MovieEntry.CONTENT_URI,
                true,
                movieObserver
        );

        Uri uri = contentResolver.insert(MovieContract.MovieEntry.CONTENT_URI, cv);

        Uri expectedUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, 1);

        String msg = "Unable to insert item through Provider";
        assertEquals(msg, uri, expectedUri);

        movieObserver.waitForNotificationOrFail();

        contentResolver.unregisterContentObserver(movieObserver);
    }
}