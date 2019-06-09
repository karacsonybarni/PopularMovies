package com.udacity.popularmovies.api;

import androidx.fragment.app.FragmentActivity;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.network.TextDownloader;

public class TMDb {

    private FragmentActivity activity;
    private TextDownloader moviesDownloader;
    private TextDownloader trailersDownloader;
    private TextDownloader reviewsDownloader;

    private String apiKey;
    private String popularMoviesUrl;
    private String topRatedMoviesUrl;

    public TMDb(FragmentActivity activity) {
        this.activity = activity;
        initUrls();
    }

    private void initUrls() {
        apiKey = activity.getString(R.string.api_key);
        popularMoviesUrl = activity.getString(R.string.popular_movies_query, apiKey);
        topRatedMoviesUrl = activity.getString(R.string.top_rated_movies_query, apiKey);
    }

    public void setMoviesUpdateListener(MoviesUpdateListener moviesUpdateListener) {
        moviesDownloader =
                new TextDownloader(activity, newMoviesDownloadListener(moviesUpdateListener));
    }

    MoviesDownloadListener newMoviesDownloadListener(
            MoviesUpdateListener moviesUpdateListener) {
        return new MoviesDownloadListener(moviesUpdateListener);
    }

    public void fetchPopularMovies() {
        moviesDownloader.download(popularMoviesUrl);
    }

    public void fetchTopRatedMovies() {
        moviesDownloader.download(topRatedMoviesUrl);
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
}
