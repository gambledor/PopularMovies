package it.globrutto.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import it.globrutto.popularmovies.data.MovieDao;
import it.globrutto.popularmovies.http.response.PopularResponse;
import it.globrutto.popularmovies.model.Movie;
import it.globrutto.popularmovies.utility.NetworkUtility;
import it.globrutto.popularmovies.utility.PopularMoviesJsonUtils;

/**
 * Created by giuseppelobrutto on 30/01/17.
 */
public class FetchMovieDataTask extends AsyncTask<String, Void, List<Movie>>{

    private final String TAG = FetchMovieDataTask.class.getSimpleName();

    private Context context;

    private AsyncTaskCompleteListener<List<Movie>> listener;

    /**
     * Constructor
     *
     * @param aContext
     * @param aListener
     */
    public FetchMovieDataTask(Context aContext, AsyncTaskCompleteListener<List<Movie>> aListener) {
        context = aContext;
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
            case "popular":
            case "top_rated":
                popularResponse = getTheMovieDbMoviesResponse(strings[0]);
                break;
            case "favorites":
                popularResponse = new PopularResponse();
                popularResponse.setResults(getFavoriteMovies());
                break;
            default:
                Log.i(TAG, String.format("Wrong API name to call: %s", strings[0]));
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
        MovieDao movieDao = new MovieDao(context);

        return movieDao.getAllFavoriteMovie();
    }

    @Nullable
    private PopularResponse getTheMovieDbMoviesResponse(String string) {
        URL movieRequestUrl = NetworkUtility.buildUrl(string);
        PopularResponse popularResponse = null;
        try {
            String jsonMovieResponse = NetworkUtility.getResponseFromHttpURL(movieRequestUrl);
            popularResponse = PopularMoviesJsonUtils.getMovieObjectsFromJson(
                    context, jsonMovieResponse);
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
