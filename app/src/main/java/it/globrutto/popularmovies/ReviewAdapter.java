package it.globrutto.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.globrutto.popularmovies.model.Review;

/**
 * Created by giuseppelobrutto on 09/03/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    public static final String TAG = ReviewAdapter.class.getSimpleName();

    private final Context mContext;

    private List<Review> mReviews;

    public ReviewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForReviewItem = R.layout.rewiew_row;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForReviewItem, parent, shouldAttachToParentImmediately);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        if (mReviews == null || mReviews.isEmpty()) {
            Log.w(TAG, "mReviews is null or empty");
            return;
        }

        Review review = mReviews.get(position);
        // set reviewer
        holder.mReviewAuthor.setText(review.getAuthor());
        // set review content
        holder.mReviewContent.setText(review.getContent().trim());

    }

    @Override
    public int getItemCount() {
        return mReviews == null ? 0 : mReviews.size();
    }

    public void setData(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_movie_review_author)
        TextView mReviewAuthor = null;

        @BindView(R.id.tv_movie_review)
        TextView mReviewContent = null;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
