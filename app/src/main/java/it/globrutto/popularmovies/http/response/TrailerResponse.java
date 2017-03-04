package it.globrutto.popularmovies.http.response;

import java.util.List;

import it.globrutto.popularmovies.model.Trailer;

/**
 * Created by giuseppelobrutto on 04/03/17.
 */

public class TrailerResponse {
    private int id;

    private List<Trailer> results;

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
