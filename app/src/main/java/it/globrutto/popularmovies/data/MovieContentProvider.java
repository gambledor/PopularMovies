package it.globrutto.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static it.globrutto.popularmovies.data.MovieContract.MovieEntry.TABLE_NAME;

/**
 * Created by giuseppelobrutto on 24/03/17.
 */
public class MovieContentProvider extends ContentProvider {

    private MovieDbHelper mMovieDbHelper = null;

    // final integer constant for directory of movies
    public static final int MOVIES = 100;

    // final integer constant for a single movie
    public static final int MOVIES_WITH_ID = 101;

    // The constructed uriMatcher
    public static final UriMatcher sUriMatcher = buildUriMactcher();

    /**
     * Associate URI's with their integer match
     *
     * @return The uri matcher
     */
    public static UriMatcher buildUriMactcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Add matches
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIE, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIE + "/#", MOVIES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        // initialize DbHelper to gain access to it
        Context context = getContext();
        mMovieDbHelper = new MovieDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projections, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // get readonly access to database
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        // write Uri matcher
        int match = sUriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case MOVIES:
                cursor = db.query(TABLE_NAME,
                        projections,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // set notification uri on the Cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // return the cursor
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        // get access to the movid database
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        // write Uri matching code to identify the match for the movie directory
        int match = sUriMatcher.match(uri);

        // insert new values into the database
        Uri result = null;
        switch (match) {
            case MOVIES:
                long id = db.insert(TABLE_NAME, null, contentValues);
                if (id <= 0) {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                result = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // notify the resolver if the uri has been changed
        getContext().getContentResolver().notifyChange(uri, null);

        // return the new inserted Uri
        return result;
    }

    @Nullable
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        // get access to the database
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        // uri matching code to recognize a single item
        int match = sUriMatcher.match(uri);

        int result = -1;
        switch (match) {
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                // use selections to delete an item by its row ID
                result = db.delete(TABLE_NAME, "movieId=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // notify the resolver of a change
        getContext().getContentResolver().notifyChange(uri, null);

        // the number of item deleted
        return result;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
}

