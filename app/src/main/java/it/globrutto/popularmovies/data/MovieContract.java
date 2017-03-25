package it.globrutto.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * MovieContract is a companion class which explicitly specifies the layout of our schema.
 * <p>
 * It is a container of constants that defines names for URIs, tables and columns.
 * <p>
 * Created by giuseppelobrutto on 10/03/17.
 */
public class MovieContract {

    // Add content provider constants to the contract
    // 1. Content authority
    public static final String AUTHORITY = "it.globrutto.popularmovies";

    // 2. Base content uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // 3. Paths to the movie directory
    public static final String PATH_MOVIE = "movie";


    private MovieContract() {
    }

    /**
     * Defines the favorite movie table content
     */
    public static final class MovieEntry implements BaseColumns {

        // 4. Content URI for data in the MovieEntry class
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

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
