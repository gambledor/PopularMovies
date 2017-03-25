package it.globrutto.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.globrutto.popularmovies.data.MovieContract;
import it.globrutto.popularmovies.http.response.ReviewResponse;
import it.globrutto.popularmovies.model.Movie;
import it.globrutto.popularmovies.model.Trailer;
import it.globrutto.popularmovies.utility.NetworkUtility;
import it.globrutto.popularmovies.utility.ReviewMovieJsonUtil;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = DetailActivity.class.getSimpleName();

    // The review movie loader
    public static final int REVIEW_MOVIE_LOADER = 1001;

    private static final String MOVIE_ID_EXTRA = "MOVIE_ID";

    @BindView(R.id.iv_header_image)
    ImageView mHeaderImage = null;

    @BindView(R.id.iv_thumbnail)
    ImageView mThumbnail = null;

    @BindView(R.id.tv_title)
    TextView mTitle = null;

    @BindView(R.id.tv_header_title)
    TextView mHeaderTitle = null;

    @BindView(R.id.tv_movie_overview)
    TextView mOverview = null;

    @BindView(R.id.tv_year)
    TextView mYear = null;

    @BindView(R.id.tv_movie_rating)
    TextView mRating = null;

    @BindView(R.id.tv_movie_votes)
    TextView mVotes = null;

    @BindView(R.id.rv_movie_trailers)
    RecyclerView mTrailerRecyclerView = null;

    @BindView(R.id.rv_movie_reviews)
    RecyclerView mReviewRecyclerView = null;

    @BindView(R.id.ib_movie_favorite)
    ImageButton mFavoriteImageButton = null;

    private TrailerAdapter mTrailerAdapter = null;

    private ReviewAdapter mReviewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intentThatStartedActivity = getIntent();
        if (intentThatStartedActivity != null ) {
            if (intentThatStartedActivity.hasExtra("movie")) {

                LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this);
                mTrailerRecyclerView.setLayoutManager(trailerLayoutManager);
                mTrailerRecyclerView.addItemDecoration(
                        new DividerItemDecoration(mTrailerRecyclerView.getContext(), trailerLayoutManager.getOrientation()));
                // create trailer adapter
                mTrailerAdapter = new TrailerAdapter(getApplicationContext(), this);
                // attach trailer adapter to recycler view
                mTrailerRecyclerView.setAdapter(mTrailerAdapter);

                LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
                // attach review adapter to recicler view
                mReviewRecyclerView.setLayoutManager(reviewLayoutManager);
                mReviewRecyclerView.addItemDecoration(
                        new DividerItemDecoration(mReviewRecyclerView.getContext(), reviewLayoutManager.getOrientation()));
                // create review adapter
                mReviewAdapter = new ReviewAdapter(this);
                mReviewRecyclerView.setAdapter(mReviewAdapter);

                final Movie movie = (Movie) intentThatStartedActivity.getParcelableExtra("movie");

                // Load movie trailers
                new FetchMovieTrailerTask(this, new FetchMovieTrailerTaskCompleteListener()).execute(movie.getId());

                // Load movie reviews
                Bundle reviewBundle = new Bundle();
                reviewBundle.putInt(MOVIE_ID_EXTRA, movie.getId());
                // Initialize the loader
                getSupportLoaderManager().initLoader(REVIEW_MOVIE_LOADER, reviewBundle, this);

                URL url = NetworkUtility.buildImageUrl(movie.getBackdropPath(), 1);
                Picasso.with(getApplicationContext())
                        .load(url.toString())
                        .placeholder(R.mipmap.ic_image)
                        .error(R.mipmap.ic_image)
                        .into(mHeaderImage);

                url = NetworkUtility.buildImageUrl(movie.getPosterPath(), 0);
                Picasso.with(getApplicationContext())
                        .load(url.toString())
                        .placeholder(R.mipmap.ic_image)
                        .error(R.mipmap.ic_image)
                        .into(mThumbnail);

                mHeaderTitle.setText(movie.getOriginalTitle());
                mTitle.setText(movie.getOriginalTitle());
                DateFormat dateFormat = new SimpleDateFormat("yyyy");
                try {
                    mYear.setText(dateFormat.format(dateFormat.parse(movie.getReleaseDate())));
                } catch (ParseException e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                mRating.setText("Rating: " + String.format("%.1f", movie.getVoteAverage()) + "/10");
                mVotes.setText("Votes: " + movie.getVoteCount() + "");
                mOverview.setText(movie.getOverview());


                if (isFavoriteMovie(movie.getId())) {
                    mFavoriteImageButton.setImageResource(android.R.drawable.btn_star_big_on);
                }

                mFavoriteImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (movie.getFavorite() != 1) {
                            ContentValues cv = getMovieAsContentValues(movie);
                            getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
                            movie.setFavorite(1);
                            mFavoriteImageButton.setImageResource(android.R.drawable.btn_star_big_on);
                            Toast.makeText(
                                    getBaseContext(),
                                    movie.getOriginalTitle() + " added as favorite",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Uri uri = MovieContract.MovieEntry.CONTENT_URI
                                    .buildUpon()
                                    .appendPath(Integer.toString(movie.getId()))
                                    .build();
                            getContentResolver().delete(uri, null, null);
                            movie.setFavorite(0);
                            mFavoriteImageButton.setImageResource(android.R.drawable.btn_star_big_off);
                        }
                    }
                });

            }
        }
    }

    /**
     * To check if the selected movie is a favorite movie
     * @param id the movie id
     * @return true if it is a favorite movie
     */
    private boolean isFavoriteMovie(int id) {
        Log.d(TAG, "the movie id: " + id);
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                "movieId=?",
                new String[] {Integer.toString(id)},
                null,
                null
        );

        return cursor != null && cursor.getCount() > 0;
    }

    private ContentValues getMovieAsContentValues(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        cv.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());

        return cv;
    }

    private void showErrorMessage() {
        // hide the visible data
        mTrailerRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showTrailerView() {
        // show trailer data view
        mTrailerRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * The action to perform on click item
     *
     * @param trailer The trailer object
     */
    @Override
    public void onClickItem(Trailer trailer) {
        Uri uri = Uri.parse(
                NetworkUtility.buildYoutubeUrl(trailer.getKey()).toString());
        // Build the intent
        Intent intentStartYouTube = new Intent(Intent.ACTION_VIEW);
        intentStartYouTube.setPackage("com.google.android.youtube");
        intentStartYouTube.setData(uri);

        // Verify it resolves
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(intentStartYouTube, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(intentStartYouTube);
        } else {
            Intent intentStartBrowser = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intentStartBrowser);
        }
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                int movieId = args.getInt(MOVIE_ID_EXTRA);

                if (movieId <= 0) return null;

                URL url = NetworkUtility.buildReviewUrl(movieId);
                String jsonReviewResponse = null;
                try {
                    jsonReviewResponse = NetworkUtility.getResponseFromHttpURL(url);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }

                return jsonReviewResponse;
            }

            @Override
            public void deliverResult(String data) {
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data != null && !data.equals("")) {
            try {
                ReviewResponse reviewResponse = ReviewMovieJsonUtil.getReviewObjectsFromJson(data);
                // set revies into review adapter
                mReviewAdapter.setData(reviewResponse.getReviews());
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private class FetchMovieTrailerTaskCompleteListener implements AsyncTaskCompleteListener<List<Trailer>> {
        /**
         * Invoked when the AsyncTask has completed its execution.
         *
         * @param result The resulting object from the AsyncTask.
         */
        @Override
        public void onTaskComplete(List<Trailer> result) {
            if (result != null) {
                mTrailerAdapter.setTrailers(result);
                showTrailerView();
            } else {
                showErrorMessage();
            }
        }
    }
}
