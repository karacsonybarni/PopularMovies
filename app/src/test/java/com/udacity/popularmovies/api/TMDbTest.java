package com.udacity.popularmovies.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.detailview.DetailActivity;
import com.udacity.popularmovies.mainview.MainActivity;
import com.udacity.popularmovies.model.Movie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class TMDbTest {

    private static final int ALADDIN_ID = 420817;

    private CountDownLatch signal = new CountDownLatch(1);

    @Before
    public void setup() {
        initPicasso();
    }

    private void initPicasso() {
        try {
            Picasso.get();
        } catch (IllegalStateException ignored) {
            Context context = ApplicationProvider.getApplicationContext();
            Picasso.Builder builder = new Picasso.Builder(context);
            Picasso.setSingletonInstance(builder.build());
        }
    }

    @Test
    public void fetchPopularMoviesJson() {
        ActivityScenario.launch(MainActivity.class).onActivity(activity -> {
            TMDb tmdb = spyTMDb(activity);
            tmdb.fetchPopularMovies();
        });
    }

    private TMDb spyTMDb(FragmentActivity activity) {
        TMDb tmdb = new TMDb(activity) {
            @Override
            public void fetchPopularMovies() {
                super.fetchPopularMovies();
                waitForSignal();
            }

            @Override
            public void fetchTrailers(int id) {
                super.fetchTrailers(id);
                waitForSignal();
            }

            @Override
            MoviesDownloadListener newMoviesDownloadListener(
                    MoviesUpdateListener moviesUpdateListener) {
                return spyMoviesDownloadListener();
            }

            @Override
            TrailersDownloadListener newTrailersDownloadListener(
                    TrailersUpdateListener trailersUpdateListener) {
                return spyTrailersDownloadListener();
            }

            @Override
            ReviewsDownloadListener newReviewsDownloadListener(
                    ReviewsUpdateListener reviewsUpdateListener) {
                return spyReviewsDownloadListener();
            }
        };
        tmdb.setMoviesUpdateListener(null);
        tmdb.setTrailersUpdateListener(null);
        tmdb.setReviewsUpdateListener(null);
        return tmdb;
    }

    private MoviesDownloadListener spyMoviesDownloadListener() {
        return new MoviesDownloadListener(null) {
            @Override
            public void onDataDownloaded(@NonNull String data) {
                assertThat(data).isNotEmpty();
                signal.countDown();
            }
        };
    }

    private TrailersDownloadListener spyTrailersDownloadListener() {
        return new TrailersDownloadListener(null) {
            @Override
            public void onDataDownloaded(@NonNull String data) {
                assertThat(data).isNotEmpty();
                signal.countDown();
            }
        };
    }

    private ReviewsDownloadListener spyReviewsDownloadListener() {
        return new ReviewsDownloadListener(null) {
            @Override
            public void onDataDownloaded(@NonNull String data) {
                assertThat(data).isNotEmpty();
                signal.countDown();
            }
        };
    }

    private void waitForSignal() {
        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fetchTrailers() {
        onDetailsActivity(activity -> {
            DetailActivity detailActivity = (DetailActivity) activity;
            spyTMDb(detailActivity).fetchTrailers(detailActivity.getMovieId());
        });
    }

    @Test
    public void fetchReviews() {
        onDetailsActivity(activity -> {
            DetailActivity detailActivity = (DetailActivity) activity;
            spyTMDb(detailActivity).fetchReviews(detailActivity.getMovieId());
        });
    }

    private void onDetailsActivity(ActivityScenario.ActivityAction<Activity> action) {
        Context context = ApplicationProvider.getApplicationContext();
        Intent startActivityIntent = new Intent(context, DetailActivity.class);
        Movie movie = new Movie();
        movie.setId(ALADDIN_ID);
        startActivityIntent.putExtra(DetailActivity.MOVIE_EXTRA, movie);
        ActivityScenario.launch(startActivityIntent).onActivity(action);
    }
}