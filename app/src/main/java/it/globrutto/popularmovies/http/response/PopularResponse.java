package it.globrutto.popularmovies.http.response;

import java.io.Serializable;
import java.util.List;

import it.globrutto.popularmovies.model.Movie;

/**
 * Created by giuseppelobrutto on 22/01/17.
 */

public class PopularResponse implements Serializable {

    private int page = 0;

    private List<Movie> results = null;

    private int totalPages = 0;

    private int totalResults = 0;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
