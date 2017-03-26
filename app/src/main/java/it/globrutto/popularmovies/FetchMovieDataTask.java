package it.globrutto.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import it.globrutto.popularmovies.data.MovieContract;
import it.globrutto.popularmovies.http.response.PopularResponse;
import it.globrutto.popularmovies.model.Movie;
import it.globrutto.popularmovies.utility.NetworkUtility;
import it.globrutto.popularmovies.utility.PopularMoviesJsonUtils;

/**
 * Created by giuseppelobrutto on 30/01/17.
 */
public class FetchMovieDataTask extends AsyncTask<String, Void, List<Movie>>{

    private final String TAG = FetchMovieDataTask.class.getSimpleName();

    private Context mContext;

    private AsyncTaskCompleteListener<List<Movie>> listener;

    /**
     * Constructor
     *
     * @param aContext
     * @param aListener
     */
    public FetchMovieDataTask(Context aContext, AsyncTaskCompleteListener<List<Movie>> aListener) {
        mContext = aContext;
        listener = aListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        if (strings.length == 0) {
            return null;
        }

        PopularResponse popularResponse = null;
        switch (strings[0]) {
            case MainActivity.DEFAULT_ORDER:
            case MainActivity.TOP_RATED:
                popularResponse = getTheMovieDbMoviesResponse(strings[0]);
                break;
            case MainActivity.FAVORITES:
                popularResponse = new PopularResponse();
                popularResponse.setResults(getFavoriteMovies());
                break;
            default:
                Log.w(TAG, String.format("Wrong API name to call: %s", strings[0]));
                return null;
        }

        return popularResponse.getResults();
    }

    /**
     * Query the mDb to get all preferred movies from the movie table
     *
     * @return The list of movies
     */
    private List<Movie> getFavoriteMovies() {
        List<Movie> movies = null;
        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null, null, null, MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        if (cursor != null && cursor.moveToFirst()) {
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
                movie.setFavorite(1);
                movies.add(movie);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return movies;
    }

    @Nullable
    private PopularResponse getTheMovieDbMoviesResponse(String string) {
        URL movieRequestUrl = NetworkUtility.buildUrl(string);
        PopularResponse popularResponse = null;
        try {
            String jsonMovieResponse = NetworkUtility.getResponseFromHttpURL(movieRequestUrl);
            popularResponse = PopularMoviesJsonUtils.getMovieObjectsFromJson(
                    mContext, jsonMovieResponse);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return popularResponse;
    }

    @Override
    protected void onPostExecute(List<Movie> movieData) {
        super.onPostExecute(movieData);
        listener.onTaskComplete(movieData);
    }
}
