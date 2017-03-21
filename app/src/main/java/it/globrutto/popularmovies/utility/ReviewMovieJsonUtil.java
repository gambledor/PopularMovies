package it.globrutto.popularmovies.utility;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.globrutto.popularmovies.http.response.ReviewResponse;
import it.globrutto.popularmovies.model.Review;

/**
 * Created by giuseppelobrutto on 09/03/17.
 */

public class ReviewMovieJsonUtil {
    public static final String TAG = ReviewMovieJsonUtil.class.getSimpleName();

    /**
     * To parse the review json response
     *
     * @param jsonReviewResponse The review json response
     * @return Review objects
     */
    public static ReviewResponse getReviewObjectsFromJson(@NonNull String jsonReviewResponse) throws JSONException {
        final String RESULTS = "results";
        JSONObject json = new JSONObject(jsonReviewResponse);
        ReviewResponse reviewResponse = new ReviewResponse();
        if (json.has(RESULTS)) {
            final String ID = "id";
            final String PAGE = "page";
            final String AUTHOR = "author";
            final String CONTENT = "content";
            final String URL = "URL";
            final String TOTAL_PAGES = "total_pages";
            final String TOTAL_RESULTS = "total_results";

            if (json.has(ID)) reviewResponse.setId(json.getInt(ID));
            if (json.has(PAGE)) reviewResponse.setPage(json.getInt(PAGE));
            if (json.has(TOTAL_PAGES)) reviewResponse.setTotalPages(json.getInt(TOTAL_PAGES));
            if (json.has(TOTAL_RESULTS)) reviewResponse.setTotalResults(json.getInt(TOTAL_RESULTS));

            List<Review> reviews = new ArrayList<>();
            JSONArray results = json.getJSONArray(RESULTS);
            for (int i = 0; i < results.length(); i++) {
                Review review = new Review();
                JSONObject jsonReview = results.getJSONObject(i);
                if (jsonReview.has(ID)) review.setId(jsonReview.getString(ID));
                if (jsonReview.has(AUTHOR)) review.setAuthor(jsonReview.getString(AUTHOR));
                if (jsonReview.has(CONTENT)) review.setContent(jsonReview.getString(CONTENT));
                if (jsonReview.has(URL)) review.setUrl(jsonReview.getString(URL));

                reviews.add(review);
            }
            reviewResponse.setReviews(reviews);
        }

        return reviewResponse;
    }
}
