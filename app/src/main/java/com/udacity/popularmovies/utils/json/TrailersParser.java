package com.udacity.popularmovies.utils.json;

import com.udacity.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class TrailersParser {

    private static final String KEY = "key";
    private static final String NAME = "name";

    List<Trailer> parse(String trailersJSON) throws JSONException {
        JSONArray results = new JSONObject(trailersJSON).getJSONArray("results");
        return parseResults(results);
    }

    private List<Trailer> parseResults(JSONArray results) throws JSONException {
        List<Trailer> trailers = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject trailerJSON = results.getJSONObject(i);
            Trailer trailer = parse(trailerJSON);
            trailers.add(trailer);
        }
        return trailers;
    }

    private Trailer parse(JSONObject trailerJSON) throws JSONException {
        Trailer trailer = new Trailer();
        trailer.setKey(trailerJSON.getString(KEY));
        trailer.setName(trailerJSON.getString(NAME));
        return trailer;
    }
}
