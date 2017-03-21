package it.globrutto.popularmovies.data;

import android.provider.BaseColumns;

/**
 * MovieContract is a companion class which explicitly specifies the layout of our schema.
 * <p>
 * It is a container of constants that defines names for URIs, tables and columns.
 * <p>
 * Created by giuseppelobrutto on 10/03/17.
 */
public class MovieContract {

    private MovieContract() {
    }

    /**
     * Defines the favorite movie table content
     */
    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_BACKDROP_PATH = "backdropPath";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_VOTE_COUNT = "voteCount";
        public static final String COLUMN_OVERVIEW = "overview";

    }

}
