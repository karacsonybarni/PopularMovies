package com.udacity.popularmovies.detailview;

import android.view.View;

import com.udacity.popularmovies.api.ReviewsUpdateListener;
import com.udacity.popularmovies.api.TMDb;
import com.udacity.popularmovies.model.Review;

import java.util.List;

public class ReviewsPresenter implements ReviewsUpdateListener {

    private DetailActivity activity;
    private TMDb tmdb;
    private View reviewsView;

    ReviewsPresenter(DetailActivity activity) {
        this.activity = activity;
        tmdb = new TMDb(activity);
        tmdb.setReviewsUpdateListener(this);
    }

    @Override
    public void onReviewsFetched(List<Review> ids) {

    }

    @Override
    public void onNoReviews() {

    }
}
