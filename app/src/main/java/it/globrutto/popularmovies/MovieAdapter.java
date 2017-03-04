package it.globrutto.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.globrutto.popularmovies.model.Movie;
import it.globrutto.popularmovies.utility.NetworkUtility;

/**
 * Created by giuseppelobrutto on 22/01/17.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private final MovieAdapterOnClickHandler mClickHandler;

    private List<Movie> mMovieData = null;

    private Context mContext = null;

    /**
     *
     */
    public interface MovieAdapterOnClickHandler {
        void onClickItem(Movie movie);
    }

    /**
     * Create a Movie Adapter
     *
     * @param context The app context
     * @param clickHandler The click handler reference class
     */
    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForGridItem = R.layout.movie_gried_view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForGridItem, parent, shouldAttachToParentImmediately);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        if (mMovieData == null || mMovieData.isEmpty()) {
            Log.d(TAG, "mMovieData is: null or empty");
            return;
        }
        Movie movie = mMovieData.get(position);
        URL url = NetworkUtility.buildImageUrl(movie.getPosterPath(), 0);
        Picasso.with(mContext).load(url.toString())
                .placeholder(R.mipmap.ic_image)
                .error(R.mipmap.ic_image)
                .into(holder.mPosterImageView);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mMovieData == null ? 0 : mMovieData.size();
    }

    public void setMovieData(List<Movie> movieData) {
        this.mMovieData = movieData;
        notifyDataSetChanged();
    }

    public List<Movie> getMovieData() {
        return mMovieData;
    }

    /**
     * Movie adapter view holder class
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final String TAG = MovieAdapterViewHolder.class.getSimpleName();

        @BindView(R.id.iv_poster_image)
        public ImageView mPosterImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            mPosterImageView = (ImageView) itemView.findViewById(R.id.iv_poster_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "Enter onClick");
            int position = getAdapterPosition();
            Movie movie = mMovieData.get(position);
            mClickHandler.onClickItem(movie);
            Log.d(TAG, "Exit onClick");
        }
    }
}
