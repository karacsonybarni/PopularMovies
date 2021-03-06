package com.udacity.popularmovies.utils.json;

import com.udacity.popularmovies.data.database.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class MoviesParser {

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String POPULARITY = "popularity";
    private static final String POSTER_PATH = "poster_path";
    private static final String PLOT_SYNOPSIS = "overview";
    private static final String USER_RATING = "vote_average";
    private static final String RELEASE_DATE = "release_date";

    ArrayList<Movie> parse(String moviesJSON) throws JSONException {
        JSONObject page1 = new JSONObject(moviesJSON);
        JSONArray results = page1.getJSONArray("results");
        return parseResults(results);
    }

    private ArrayList<Movie> parseResults(JSONArray results) throws JSONException {
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieJSON = results.getJSONObject(i);
            Movie movie = parseMovie(movieJSON);
            movies.add(movie);
        }
        return movies;
    }

    private Movie parseMovie(JSONObject movieJSON) throws JSONException {
        Movie movie = new Movie();
        movie.setId(movieJSON.getInt(ID));
        movie.setTitle(movieJSON.getString(TITLE));
        movie.setPopularity(movieJSON.getDouble(POPULARITY));
        movie.setPosterPath(movieJSON.getString(POSTER_PATH));
        movie.setPlotSynopsis(movieJSON.getString(PLOT_SYNOPSIS));
        movie.setRating(movieJSON.getDouble(USER_RATING));
        movie.setReleaseDate(movieJSON.getString(RELEASE_DATE));
        return movie;
    }
}
