package it.globrutto.popularmovies.utility;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.globrutto.popularmovies.http.response.PopularResponse;
import it.globrutto.popularmovies.model.Movie;

/**
 * Created by giuseppelobrutto on 22/01/17.
 */
public class PopularMoviesJsonUtils {

    public static PopularResponse getMovieObjectsFromJson(Context context, String moviesJsonStr)
        throws JSONException {

        final String PAGE = "page";
        final String TOTAL_PAGES = "total_pages";
        final String TOTAL_RESULTS = "total_results";
        final String RESULTS = "results";

        JSONObject jsonResponse = new JSONObject(moviesJsonStr);
        PopularResponse popularResponse = new PopularResponse();

        if (jsonResponse.has(PAGE)) {
            popularResponse.setPage(jsonResponse.getInt(PAGE));
        }
        if (jsonResponse.has(TOTAL_PAGES)) {
            popularResponse.setTotalPages(jsonResponse.getInt(TOTAL_PAGES));
        }
        if (jsonResponse.has(TOTAL_RESULTS)) {
            popularResponse.setTotalResults(jsonResponse.getInt(TOTAL_RESULTS));
        }
        if (jsonResponse.has(RESULTS)) {
            final String ID = "id";
            final String ADULT = "adult";
            final String BACKDROP_PATH = "backdrop_path";
            final String GENRE_IDS = "genre_ids";
            final String ORIGINAL_LANGUAGE = "original_language";
            final String ORIGINAL_TITLE = "original_title";
            final String POSTER_PATH = "poster_path";
            final String RELEASE_DATE = "release_date";
            final String OVERVIEW = "overview";
            final String VOTE_AVERAGE = "vote_average";
            final String VOTE_COUNT = "vote_count";

            List<Movie> movies = new ArrayList<>();
            JSONArray results = jsonResponse.getJSONArray(RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJsonObject = results.getJSONObject(i);
                Movie movie = new Movie();
                if (movieJsonObject.has(ID)) {
                    movie.setId(movieJsonObject.getInt(ID));
                }
                if (movieJsonObject.has(POSTER_PATH)) {
                    movie.setPosterPath(movieJsonObject.getString(POSTER_PATH));
                }
                if (movieJsonObject.has(ADULT)) {
                    movie.setAdult(movieJsonObject.getBoolean(ADULT));
                }
                if (movieJsonObject.has(BACKDROP_PATH)) {
                    movie.setBackdropPath(movieJsonObject.getString(BACKDROP_PATH));
                }
                if (movieJsonObject.has(GENRE_IDS)) {
                    JSONArray genreIds = movieJsonObject.getJSONArray(GENRE_IDS);
                    List<Integer> movieGenreIds = new ArrayList<>(genreIds.length());
                    for (int j = 0; j < genreIds.length(); j++) {
                       movieGenreIds.add(genreIds.getInt(j));
                    }
                    movie.setGenreIds(movieGenreIds);
                }
                if (movieJsonObject.has(ORIGINAL_LANGUAGE)) {
                    movie.setOriginalLanguage(movieJsonObject.getString(ORIGINAL_LANGUAGE));
                }
                if (movieJsonObject.has(ORIGINAL_TITLE)) {
                    movie.setOriginalTitle(movieJsonObject.getString(ORIGINAL_TITLE));
                }
                if (movieJsonObject.has(RELEASE_DATE)) {
                    movie.setReleaseDate(movieJsonObject.getString(RELEASE_DATE));
                }
                if (movieJsonObject.has(OVERVIEW)) {
                    movie.setOverview(movieJsonObject.getString(OVERVIEW));
                }
                if (movieJsonObject.has(VOTE_AVERAGE)) {
                    movie.setVoteAverage(movieJsonObject.getDouble(VOTE_AVERAGE));
                }
                if (movieJsonObject.has(VOTE_COUNT)) {
                    movie.setVoteCount(movieJsonObject.getInt(VOTE_COUNT));
                }
                movies.add(movie);
            }
            popularResponse.setResults(movies);
        }

        return popularResponse;
    }
}
