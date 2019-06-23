package com.udacity.popularmovies.api;

import androidx.fragment.app.FragmentActivity;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.data.network.TextDownloader;

public class TMDb {

    private FragmentActivity activity;
    private MoviesNetworkDataSource moviesNetworkDataSource;
    private TextDownloader trailersDownloader;
    private TextDownloader reviewsDownloader;

    private String apiKey;

    public TMDb(FragmentActivity activity) {
        this.activity = activity;
        moviesNetworkDataSource = MoviesNetworkDataSource.getInstance(activity);
        initUrls();
    }

    private void initUrls() {
        apiKey = activity.getString(R.string.api_key);
    }

    public void fetchPopularMovies() {
        moviesNetworkDataSource.fetchPopularMovies();
    }

    public void fetchTopRatedMovies() {
        moviesNetworkDataSource.fetchTopRatedMovies();
    }

    public void setTrailersUpdateListener(TrailersUpdateListener movieDetailsListener) {
        trailersDownloader =
                new TextDownloader(activity, newTrailersDownloadListener(movieDetailsListener));
    }

    TrailersDownloadListener newTrailersDownloadListener(
            TrailersUpdateListener trailersUpdateListener) {
        return new TrailersDownloadListener(trailersUpdateListener);
    }

    public void fetchTrailers(int id) {
        String trailersUrl = activity.getString(R.string.trailers_url, id, apiKey);
        trailersDownloader.download(trailersUrl);
    }

    public void setReviewsUpdateListener(ReviewsUpdateListener movieDetailsListener) {
        reviewsDownloader =
                new TextDownloader(activity, newReviewsDownloadListener(movieDetailsListener));
    }

    ReviewsDownloadListener newReviewsDownloadListener(
            ReviewsUpdateListener reviewsUpdateListener) {
        return new ReviewsDownloadListener(reviewsUpdateListener);
    }

    public void fetchReviews(int id) {
        String reviewsUrl = activity.getString(R.string.reviews_url, id, apiKey);
        reviewsDownloader.download(reviewsUrl);
    }

    public void close() {
        activity = null;
    }
}
