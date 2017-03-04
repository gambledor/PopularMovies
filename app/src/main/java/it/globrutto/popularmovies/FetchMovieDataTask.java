package it.globrutto.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

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
        switch (strings[0]) {
            case "popular":
                break;
            case "top_rated":
                break;
            default:
                Log.i(TAG, String.format("Wrong API name to call: %s", strings[0]));
                return null;
        }
        URL movieRequestUrl = NetworkUtility.buildUrl(strings[0]);
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

        return popularResponse.getResults();
    }

    @Override
    protected void onPostExecute(List<Movie> movieData) {
        super.onPostExecute(movieData);
        listener.onTaskComplete(movieData);
    }
}
