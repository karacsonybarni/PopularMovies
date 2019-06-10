package com.udacity.popularmovies.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.udacity.popularmovies.Initializer;
import com.udacity.popularmovies.data.Repository;
import com.udacity.popularmovies.data.database.Database;
import com.udacity.popularmovies.data.database.Movie;
import com.udacity.popularmovies.ui.detailview.DetailActivity;
import com.udacity.popularmovies.ui.mainview.MainActivity;
import com.udacity.popularmovies.utils.InjectorUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class TMDbTest {

    private static final int ALADDIN_ID = 420817;
    private Movie movie;

    private CountDownLatch signal = new CountDownLatch(1);

    @Before
    public void setup() {
        Initializer.initPicasso();
        Database.getInstance(ApplicationProvider.getApplicationContext());
    }

    @After
    public void tearDown() {
        Database.getInstance(ApplicationProvider.getApplicationContext()).close();
    }

    @Test
    public void fetchPopularMoviesJson() {
        ActivityScenario.launch(MainActivity.class).onActivity(activity -> {
            getMovies(activity).observe(activity, movies -> {
                assertThat(movies).isNotEmpty();
                signal.countDown();
            });
            TMDb tmdb = new TMDb(activity);
            tmdb.fetchPopularMovies();
        });
    }

    private LiveData<List<Movie>> getMovies(FragmentActivity activity) {
        Repository repository = InjectorUtils.getRepository(activity);
        return repository.getMovies();
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
        tmdb.setTrailersUpdateListener(null);
        tmdb.setReviewsUpdateListener(null);
        return tmdb;
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
            insertMoviesIntoDatabase(detailActivity);
            spyTMDb(detailActivity).fetchTrailers(detailActivity.getMovieId());
        });
    }

    private void insertMoviesIntoDatabase(FragmentActivity activity) {
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        MoviesNetworkDataSource.getInstance(activity).onMoviesUpdated(movies);
    }

    @Test
    public void fetchReviews() {
        onDetailsActivity(activity -> {
            DetailActivity detailActivity = (DetailActivity) activity;
            insertMoviesIntoDatabase(detailActivity);
            spyTMDb(detailActivity).fetchReviews(detailActivity.getMovieId());
        });
    }

    private void onDetailsActivity(ActivityScenario.ActivityAction<Activity> action) {
        Context context = ApplicationProvider.getApplicationContext();
        Intent startActivityIntent = new Intent(context, DetailActivity.class);
        movie = new Movie();
        movie.setId(ALADDIN_ID);
        startActivityIntent.putExtra(DetailActivity.MOVIE_EXTRA, movie.getId());
        ActivityScenario.launch(startActivityIntent).onActivity(action);
    }
}