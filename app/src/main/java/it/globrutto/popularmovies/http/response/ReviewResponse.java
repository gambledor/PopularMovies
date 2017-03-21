package it.globrutto.popularmovies.http.response;

import java.util.List;

import it.globrutto.popularmovies.model.Review;

/**
 * Created by giuseppelobrutto on 09/03/17.
 */

public class ReviewResponse {
    private int id;
    private int page;
    private int totalPages;
    private int totalResults;
    private List<Review> reviews;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
