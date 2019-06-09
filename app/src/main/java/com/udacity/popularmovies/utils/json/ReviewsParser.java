package com.udacity.popularmovies.utils.json;

import com.udacity.popularmovies.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class ReviewsParser {

    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";

    ArrayList<Review> parse(String moviesJSON) throws JSONException {
        JSONObject page1 = new JSONObject(moviesJSON);
        JSONArray results = page1.getJSONArray("results");
        return parseResults(results);
    }

    private ArrayList<Review> parseResults(JSONArray results) throws JSONException {
        ArrayList<Review> reviews = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject movieJSON = results.getJSONObject(i);
            Review review = parseReview(movieJSON);
            reviews.add(review);
        }
        return reviews;
    }

    private Review parseReview(JSONObject reviewJSON) throws JSONException {
        Review review = new Review();
        review.setAuthor(reviewJSON.getString(AUTHOR));
        review.setContent(reviewJSON.getString(CONTENT));
        return review;
    }
}
