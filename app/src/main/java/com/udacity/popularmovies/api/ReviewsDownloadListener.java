package com.udacity.popularmovies.api;

import androidx.annotation.NonNull;

import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.network.DownloadListener;
import com.udacity.popularmovies.utils.json.Parser;

import org.json.JSONException;

import java.util.List;

class ReviewsDownloadListener implements DownloadListener {

    private ReviewsUpdateListener listener;

    ReviewsDownloadListener(ReviewsUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDataDownloaded(@NonNull String data) {
        try {
            List<Review> reviews = Parser.parseReviews(data);
            listener.onReviewsFetched(reviews);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNoData() {
        listener.onNoReviews();
    }
}
