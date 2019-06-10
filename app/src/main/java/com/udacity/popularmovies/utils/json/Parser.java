package com.udacity.popularmovies.utils.json;

import com.udacity.popularmovies.data.database.Movie;
import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.model.Trailer;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static Parser sInstance;

    private MoviesParser moviesParser;
    private TrailersParser trailersParser;
    private ReviewsParser reviewsParser;

    private Parser() {
        moviesParser = new MoviesParser();
        trailersParser = new TrailersParser();
        reviewsParser = new ReviewsParser();
    }

    private static Parser getInstance() {
        if (sInstance == null) {
            sInstance = new Parser();
        }
        return sInstance;
    }

    public static ArrayList<Movie> parseMovies(String moviesJSON) throws JSONException {
        return getInstance().moviesParser.parse(moviesJSON);
    }

    public static List<Trailer> parseTrailers(String trailersJSON) throws JSONException {
        return getInstance().trailersParser.parse(trailersJSON);
    }

    public static List<Review> parseReviews(String reviewsJSON) throws JSONException {
        return getInstance().reviewsParser.parse(reviewsJSON);
    }
}
