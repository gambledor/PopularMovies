package it.globrutto.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import it.globrutto.popularmovies.model.Movie;
import it.globrutto.popularmovies.utility.NetworkUtility;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private ImageView mHeaderImage = null;

    private ImageView mThumbnail = null;

    private TextView mTitle = null;

    private TextView mHeaderTitle = null;

    private TextView mOverview = null;

    private TextView mYear = null;

    private TextView mRating = null;

    private TextView mVotes = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mHeaderImage = (ImageView) findViewById(R.id.iv_header_image);
        mThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
        mHeaderTitle = (TextView) findViewById(R.id.tv_header_title);
        mOverview = (TextView) findViewById(R.id.tv_movie_overview) ;
        mTitle = (TextView) findViewById(R.id.tv_title);
        mYear = (TextView) findViewById(R.id.tv_year);
        mRating = (TextView) findViewById(R.id.tv_movie_rating);
        mVotes = (TextView) findViewById(R.id.tv_movie_votes);

        Intent intentThatStartedActivity = getIntent();
        if (intentThatStartedActivity != null ) {
            if (intentThatStartedActivity.hasExtra("movie")) {
                Movie movie = (Movie) intentThatStartedActivity.getParcelableExtra("movie");

                URL url = NetworkUtility.buildImageUrl(movie.getBackdropPath(), 1);
                Picasso.with(getApplicationContext()).load(url.toString()).into(mHeaderImage);

                url = NetworkUtility.buildImageUrl(movie.getPosterPath(), 0);
                Picasso.with(getApplicationContext()).load(url.toString()).into(mThumbnail);

                mHeaderTitle.setText(movie.getOriginalTitle());
                mTitle.setText(movie.getOriginalTitle());
                DateFormat dateFormat = new SimpleDateFormat("yyyy");
                try {
                    mYear.setText(dateFormat.format(dateFormat.parse(movie.getReleaseDate())));
                } catch (ParseException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                Log.d(TAG, "average " + movie.getVoteAverage());
                Log.d(TAG, "vote count " + movie.getVoteCount());

                mRating.setText("Rating: " + String.valueOf(movie.getVoteAverage()) + "/10");
                mVotes.setText("Votes: " + movie.getVoteCount() + "");
                mOverview.setText(movie.getOverview());
            }
        }
    }
}
