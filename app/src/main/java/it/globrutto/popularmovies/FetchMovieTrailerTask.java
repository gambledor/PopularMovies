package it.globrutto.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import it.globrutto.popularmovies.http.response.TrailerResponse;
import it.globrutto.popularmovies.model.Trailer;
import it.globrutto.popularmovies.utility.NetworkUtility;
import it.globrutto.popularmovies.utility.TrailerMoviesJsonUtils;

/**
 * Created by giuseppelobrutto on 04/03/17.
 */

public class FetchMovieTrailerTask extends AsyncTask<Integer, Void, List<Trailer>> {

    public static final String TAG = FetchMovieTrailerTask.class.getSimpleName();

    private final Context context;

    private final AsyncTaskCompleteListener<List<Trailer>> listener;

    public FetchMovieTrailerTask(Context aContext, AsyncTaskCompleteListener<List<Trailer>> aListener) {
        context = aContext;
        listener = aListener;
    }

    @Override
    protected List<Trailer> doInBackground(Integer... integers) {
        if (integers.length == 0) return null;


        URL url = NetworkUtility.buildTrailerUrl(integers[0]);
        Log.d(TAG, "URL -> " + url.toString());
        TrailerResponse trailerResponse  = null;
        try {
            String jsonTrailerResponse = NetworkUtility.getResponseFromHttpURL(url);
            Log.d(TAG, "trailer response -> " + jsonTrailerResponse);
            trailerResponse = TrailerMoviesJsonUtils.getTrailerObjectsFromJson(jsonTrailerResponse);
        } catch (IOException | JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return trailerResponse.getResults();
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        super.onPostExecute(trailers);
        listener.onTaskComplete(trailers);
    }
}
