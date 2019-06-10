package com.udacity.popularmovies.ui.detailview;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.api.ReviewsUpdateListener;
import com.udacity.popularmovies.api.TMDb;
import com.udacity.popularmovies.model.Review;

import java.util.List;

public class ReviewsPresenter implements ReviewsUpdateListener {

    private DetailActivity activity;
    private TMDb tmdb;
    private LinearLayout reviewsLayout;

    ReviewsPresenter(DetailActivity activity) {
        this.activity = activity;
        tmdb = activity.getTmdb();
        tmdb.setReviewsUpdateListener(this);

        reviewsLayout = activity.findViewById(R.id.reviews);
    }

    void fetchReviews() {
        tmdb.fetchReviews(activity.getMovieId());
    }

    @Override
    public void onReviewsFetched(List<Review> reviews) {
        if (!reviews.isEmpty()) {
            addReviewsToLayout(reviews);
            showReviewsLayout();
        }
    }

    private void addReviewsToLayout(List<Review> reviews) {
        int i = 0;
        for (Review review : reviews) {
            addReviewView(review);
            if (i < reviews.size() - 1) {
                addSeparator();
            }
            i++;
        }
    }

    private void addReviewView(Review review) {
        LinearLayout reviewView =
                (LinearLayout) activity
                        .getLayoutInflater()
                        .inflate(R.layout.review, reviewsLayout, false);

        TextView authorView = reviewView.findViewById(R.id.author);
        authorView.setText(review.getAuthor());

        TextView contentView = reviewView.findViewById(R.id.content);
        contentView.setText(review.getContent());

        reviewsLayout.addView(reviewView);
    }

    private void addSeparator() {
        activity.getLayoutInflater().inflate(R.layout.thin_separator, reviewsLayout);
    }

    private void showReviewsLayout() {
        activity.findViewById(R.id.reviews_separator).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.reviews_label).setVisibility(View.VISIBLE);
        reviewsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNoReviews() {
        activity.onNoData();
    }
}
