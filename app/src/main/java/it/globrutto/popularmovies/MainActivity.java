package it.globrutto.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.globrutto.popularmovies.model.Movie;
import it.globrutto.popularmovies.utility.NetworkUtility;
import it.globrutto.popularmovies.utility.Utility;

/**
 * Main activity
 */
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String DEFAULT_ORDER = "popular";

    private String currentSortOrder = DEFAULT_ORDER;

    @BindView(R.id.rv_movie_images)
    RecyclerView mRecyclerView = null;

    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay = null;

    @BindView(R.id.pb_loadingIndicator)
    ProgressBar mLoadingIndicator = null;

    private MovieAdapter mMovieAdapter = null;

    private Context mContext = null;

    private ArrayList<Movie> mCachedMovies = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // save reference to context
        mContext = getApplicationContext();
        // The MovieAdapter is responsible for linking our movies data to the View that will end up
        // displaing our data.
        mMovieAdapter = new MovieAdapter(mContext, this);
        // attach the adapter to recycler view
        mRecyclerView.setAdapter(mMovieAdapter);
        // create a GridLayoutManager
        int columns = Utility.calculateNumberOfColumns(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, columns);
        // set the layout manager to recycler view
        mRecyclerView.setLayoutManager(gridLayoutManager);
        // set this to true if you know this has fixed size
        mRecyclerView.setHasFixedSize(true);
        // Once all our view are setup we can load the data
        if (savedInstanceState != null ) {
            if (savedInstanceState.containsKey("movies")) {
                mCachedMovies = savedInstanceState.getParcelableArrayList("movies");
            }
            if (savedInstanceState.containsKey("sortOrder")) {
                currentSortOrder = savedInstanceState.getString("sortOrder");
            }
        }

        loadMovieData(currentSortOrder);
    }

    /**
     * To save the current app state
     *
     * @param outState The bundle
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", mCachedMovies);
        outState.putString("sortOrder", currentSortOrder);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClickItem(Movie movie) {
        Class destination = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(mContext, destination);
        intentToStartDetailActivity.putExtra("movie", movie);
        startActivity(intentToStartDetailActivity);
    }

    private void loadMovieData(String orderBy) {
        if (mCachedMovies != null && !mCachedMovies.isEmpty() && currentSortOrder.equals(orderBy)) {
            mMovieAdapter.setMovieData(mCachedMovies);
        } else {
            currentSortOrder = orderBy;
            boolean isNetworkAvailable = NetworkUtility.isNetworkAvailable(this);
            boolean isInternetAvailable = NetworkUtility.isInternetAvailable();
            if (!isNetworkAvailable) {
                Toast.makeText(mContext, R.string.network_connectivity_error, Toast.LENGTH_LONG).show();
                orderBy = "favorites";
            } else if (!isInternetAvailable) {
                Toast.makeText(mContext, R.string.internet_availability_error, Toast.LENGTH_LONG).show();
                orderBy = "favorites";
            }
            mLoadingIndicator.setVisibility(View.VISIBLE);
            new FetchMovieDataTask(this, new FetchMovieDataTaskCompleteListener()).execute(orderBy);
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
        if (selectedItem == R.id.action_popularity_sort) {
            loadMovieData(DEFAULT_ORDER);
            return true;
        }
        if (selectedItem == R.id.action_top_rated_sort) {
            loadMovieData("top_rated");
            return true;
        }

        if (selectedItem == R.id.action_favorite_sort) {
            loadMovieData("favorites");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     *
     */
    public class FetchMovieDataTaskCompleteListener implements AsyncTaskCompleteListener<List<Movie>> {

        /**
         * Invoked when the AsyncTask has completed its execution.
         *
         * @param result The resulting object from the AsyncTask.
         */
        @Override
        public void onTaskComplete(List<Movie> result) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (result != null) {
                mMovieAdapter.setMovieData(result);
                showMovieDataView();
            } else {
                showErrorMessage();
            }
        }
    }
}
