package it.globrutto.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import it.globrutto.popularmovies.http.response.PopularResponse;
import it.globrutto.popularmovies.model.Movie;
import it.globrutto.popularmovies.utility.NetworkUtility;
import it.globrutto.popularmovies.utility.PopularMoviesJsonUtils;
import it.globrutto.popularmovies.utility.Utility;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView = null;

    private TextView mErrorMessageDisplay = null;

    private ProgressBar mLoadingIndicator = null;

    private MovieAdapter mMovieAdapter = null;

    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // save reference to context
        mContext = getApplicationContext();
        // get reference to recyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_images);
        // reference to error text view to display error messages
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        // The MovieAdapter is responsible for linking our movies data to the View that will end up
        // displaing our data.
        mMovieAdapter = new MovieAdapter(mContext);
        // attach the adapter to recycler view
        mRecyclerView.setAdapter(mMovieAdapter);
        // create a GridLayoutManager
        int columns = Utility.calculateNumberOfColumns(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, columns);
        // set the layout manager to recycler view
        mRecyclerView.setLayoutManager(gridLayoutManager);
        // set this to true if you know this has fixed size
        mRecyclerView.setHasFixedSize(true);
        // get reference to loading indicator
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loadingIndicator);
        // Once all our view are setup we can load the data
        loadMovieData();
    }

    private void loadMovieData() {
        boolean isNetworkAvailable = NetworkUtility.isNetworkAvailable(this);
        boolean isInternetAvailable = NetworkUtility.isInternetAvailable();
        Log.d(TAG, "network: " + isNetworkAvailable);
        Log.d(TAG, "internet: " + isInternetAvailable);
        if (!isNetworkAvailable) {
            Toast.makeText(mContext, R.string.network_connectivity_error, Toast.LENGTH_LONG).show();
        } else if (!isInternetAvailable) {
            Toast.makeText(mContext, R.string.internet_availability_error, Toast.LENGTH_LONG).show();
        } else {
            new MovieTask().execute("popular");
        }
    }

    /**
     * This method will make the error message visible and hide the movie view.
     */
    private void showErrorMessage() {
        // First hide the currently visible data
        mRecyclerView.setVisibility(View.INVISIBLE);
        // then show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the movie view data visible, hiding the error view
     */
    private void showMovieDataView() {
        // First hide the error view
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // then show the data view
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();
        if (selectedItem == R.id.action_refresh) {
            loadMovieData();
            return true;
        }
        if (selectedItem == R.id.action_settings) {
            Log.d(TAG, "action_settings clicked");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Movie async task
     */
    class MovieTask extends AsyncTask<String, Void, List<Movie>>{
        private final String TAG = MovieTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
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
                    Log.d(TAG, String.format("Wrong API name to call: %s", strings[0]));
                    return null;
            }
            URL movieRequestUrl = NetworkUtility.buildUrl(strings[0]);
            PopularResponse popularResponse = null;
            try {
                String jsonMovieResponse = NetworkUtility.getResponseFromHttpURL(movieRequestUrl);
                Log.d(TAG, jsonMovieResponse);
                popularResponse = PopularMoviesJsonUtils.getMovieObjectsFromJson(
                        MainActivity.this, jsonMovieResponse);
                Log.d(TAG, String.valueOf(popularResponse.getResults().size()));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }

            return popularResponse.getResults();
        }

        @Override
        protected void onPostExecute(List<Movie> movieData) {
            Log.d(TAG, "Enter onPostExecute");
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                mMovieAdapter.setMovieData(movieData);
                showMovieDataView();
            } else {
                showErrorMessage();
            }
            Log.d(TAG, "Exit onPostExecute");
        }
    }
}
