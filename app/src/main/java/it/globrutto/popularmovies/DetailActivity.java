package it.globrutto.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.globrutto.popularmovies.model.Movie;
import it.globrutto.popularmovies.model.Trailer;
import it.globrutto.popularmovies.utility.NetworkUtility;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String TAG = DetailActivity.class.getSimpleName();

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

    @BindView(R.id.tv_error_trailer_message_display)
    TextView mErrorTrailerMessageDisplay = null;

    @BindView(R.id.pb_loading_trailer_indicator)
    ProgressBar mLoadingTrailerIndicator = null;

    private TrailerAdapter mTrailerAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intentThatStartedActivity = getIntent();
        if (intentThatStartedActivity != null ) {
            if (intentThatStartedActivity.hasExtra("movie")) {

                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                mTrailerRecyclerView.setLayoutManager(layoutManager);
                mTrailerRecyclerView.addItemDecoration(
                        new DividerItemDecoration(mTrailerRecyclerView.getContext(), layoutManager.getOrientation()));
                // create trailer adapter
                mTrailerAdapter = new TrailerAdapter(getApplicationContext(), this);
                // attach adapter to recycler view
                mTrailerRecyclerView.setAdapter(mTrailerAdapter);

                Movie movie = (Movie) intentThatStartedActivity.getParcelableExtra("movie");

                // Load movie trailers
                new FetchMovieTrailerTask(this, new FetchMovieTrailerTaskCompleteListener()).execute(movie.getId());

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

                mRating.setText("Rating: " + String.valueOf(movie.getVoteAverage()) + "/10");
                mVotes.setText("Votes: " + movie.getVoteCount() + "");
                mOverview.setText(movie.getOverview());
            }
        }
    }

    private void showErrorMessage() {
        // hide the visible data
        mTrailerRecyclerView.setVisibility(View.INVISIBLE);
        // show error message
        mErrorTrailerMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showTrailerView() {
        // hide error message
        mErrorTrailerMessageDisplay.setVisibility(View.INVISIBLE);
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
        Log.d(TAG, "onClickItem start intent");
        Intent intentStartYouTube = new Intent(Intent.ACTION_VIEW);
        intentStartYouTube.setPackage("com.google.android.youtube");
        intentStartYouTube.setData(Uri.parse(
                NetworkUtility.buildYoutubeUrl(trailer.getKey()).toString()));

        startActivity(intentStartYouTube);
    }

    private class FetchMovieTrailerTaskCompleteListener implements AsyncTaskCompleteListener<List<Trailer>> {
        /**
         * Invoked when the AsyncTask has completed its execution.
         *
         * @param result The resulting object from the AsyncTask.
         */
        @Override
        public void onTaskComplete(List<Trailer> result) {
            Log.d(TAG, "Enter onTaskComplete");
            mLoadingTrailerIndicator.setVisibility(View.INVISIBLE);
            if (result != null) {
                mTrailerAdapter.setTrailers(result);
                showTrailerView();
            } else {
                showErrorMessage();
            }
            Log.d(TAG, "Exit onTaskComplete");
        }
    }
}
