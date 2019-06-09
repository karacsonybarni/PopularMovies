package com.udacity.popularmovies.api;

import com.udacity.popularmovies.model.Review;

import java.util.List;

public interface ReviewsUpdateListener {
    void onReviewsFetched(List<Review> ids);
    void onNoReviews();
}
