package it.globrutto.popularmovies.utility;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.globrutto.popularmovies.http.response.TrailerResponse;
import it.globrutto.popularmovies.model.Trailer;

/**
 * Created by giuseppelobrutto on 04/03/17.
 */
public class TrailerMoviesJsonUtils {

    /**
     * To parse trailer movie json response
     *
     * @param context The context
     * @param jsonTrailerResponse the movie trailer json string response
     * @return The trailer response Trailer collection
     */
    public static TrailerResponse getTrailerObjectsFromJson(
            @NonNull Context context, @NonNull String jsonTrailerResponse) throws JSONException {

        final String RESULTS = "results";

        JSONObject jsonResponse = new JSONObject(jsonTrailerResponse);
        TrailerResponse trailerResponse = new TrailerResponse();
        if (jsonResponse.has(RESULTS)) {
            final String ID = "id";
            final String ISO_3166_1 = "iso_3166_1";
            final String ISO_639_1 = "iso_639_1";
            final String KEY = "key";
            final String NAME = "name";
            final String SITE = "site";
            final String SIZE = "size";
            final String TYPE = "type";

            if (jsonResponse.has(ID)) trailerResponse.setId(jsonResponse.getInt(ID));

            List<Trailer> trailers = new ArrayList<>();
            JSONArray results = jsonResponse.getJSONArray(RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject trailerJsonObjet = results.getJSONObject(i);
                Trailer trailer = new Trailer();
                if (trailerJsonObjet.has(ID)) trailer.setId(trailerJsonObjet.getString(ID));
                if (trailerJsonObjet.has(ISO_3166_1)) trailer.setIso3166_1(trailerJsonObjet.getString(ISO_3166_1));
                if (trailerJsonObjet.has(ISO_639_1)) trailer.setISO_639_1(trailerJsonObjet.getString(ISO_639_1));
                if (trailerJsonObjet.has(KEY)) trailer.setKey(trailerJsonObjet.getString(KEY));
                if (trailerJsonObjet.has(NAME)) trailer.setName(trailerJsonObjet.getString(NAME));
                if (trailerJsonObjet.has(SITE)) trailer.setSite(trailerJsonObjet.getString(SITE));
                if (trailerJsonObjet.has(SIZE)) trailer.setSize(trailerJsonObjet.getInt(SIZE));
                if (trailerJsonObjet.has(TYPE)) trailer.setType(trailerJsonObjet.getString(TYPE));

                trailers.add(trailer);
            }

            trailerResponse.setResults(trailers);
        }

        return trailerResponse;
    }
}
