package it.globrutto.popularmovies.utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import it.globrutto.popularmovies.BuildConfig;

/**
 * Created by giuseppelobrutto on 22/01/17.
 */
public class NetworkUtility {
    private final static String TAG = NetworkUtility.class.getSimpleName();

    private static final String API_KEY = BuildConfig.MOVIE_DB_API_KEY;

    private static final String BASE_API_URL = "https://api.themoviedb.org/3/movie";

    private static final String LANGUAGE = "en_US";

    private static final String BASE_IMAGE_MOVIE_URL = "http://image.tmdb.org/t/p/";

    private static final String BASE_IMG_SIZE = "w185/";

    private static final String CARD_HEADER_IMAGE_SIZE = "w500/";

    private static final String API_KEY_PARAM = "api_key";

    private static final String LANGUAGE_PARAM = "language";

    private static final String PAGE_PARAM = "page";

    private static final String TRAILER_PATH = "/videos";

    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch";

    private static final String REVIEW_PATH = "/reviews";

    private static final long CONNECTION_TIMEOUT = TimeUnit.MINUTES.toMillis(1);

    /**
     * Build the URL used to talk to the movie db server using popularity.
     *
     * @param movieQuery
     * @return the url to use to call the movie database server
     */
    public static URL buildUrl(String movieQuery) {
        Uri buildUri = Uri.parse(BASE_API_URL + "/" + movieQuery).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .appendQueryParameter(PAGE_PARAM, "1")
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }

        return url;
    }

    /**
     * To build the trailer url
     *
     * @param movieId The movie id
     * @return The trailer url
     */
    public static URL buildTrailerUrl(int movieId) {
        Uri buildUri = Uri.parse(BASE_API_URL + "/" + movieId + TRAILER_PATH).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }

        return url;
    }

    public static URL buildYoutubeUrl(String key) {
        Uri buildUri = Uri.parse(BASE_YOUTUBE_URL).buildUpon()
                .appendQueryParameter("v", key).build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
           Log.e(TAG, e.getMessage()) ;
        }

        return url;
    }

    public static URL buildReviewUrl(int movieId) {
        Uri buildUri = Uri.parse(BASE_API_URL + '/' + movieId + REVIEW_PATH).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .appendQueryParameter(PAGE_PARAM, "1")
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }

        return url;
    }

    /**
     * To build the image url
     * @param aImagePath The image path
     * @param aImageSizeType The image type size we ask for. 0 poster image w185, 1 backdrop image w500
     * @return The image URL
     */
    public static URL buildImageUrl(String aImagePath, int aImageSizeType) {
        String imageSize = aImageSizeType == 0 ? BASE_IMG_SIZE : CARD_HEADER_IMAGE_SIZE;
        Uri builtUri = Uri.parse(BASE_IMAGE_MOVIE_URL + imageSize + aImagePath).buildUpon().build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param movieRequestUrl The url to fetch data from
     * @return The content of the HTTP response
     * @throws IOException For network or stream reading
     */
    public static String getResponseFromHttpURL(URL movieRequestUrl) throws IOException {
        HttpURLConnection connection =  (HttpURLConnection) movieRequestUrl.openConnection();
        connection.setConnectTimeout((int) CONNECTION_TIMEOUT);
        try {
            InputStream inputStream = connection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            }
            return null;
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Looks for network connectivity
     *
     * @param activity The caller activity
     * @return The network state
     */
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    /**
     * Looks for internet connectivity
     * @return The internet availability
     */
    public static boolean isInternetAvailable() {
        Runtime runtime = Runtime.getRuntime();
        boolean result = false;
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");

            result = ipProcess.waitFor() == 0;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return result;
    }

}
