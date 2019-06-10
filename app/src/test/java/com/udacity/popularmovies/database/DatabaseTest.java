package com.udacity.popularmovies.database;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.udacity.popularmovies.Initializer;
import com.udacity.popularmovies.data.database.Database;
import com.udacity.popularmovies.data.database.Movie;
import com.udacity.popularmovies.data.database.MovieDao;
import com.udacity.popularmovies.ui.mainview.MainActivity;
import com.udacity.popularmovies.utils.AppExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private final int ID = 1;
    private final int POPULARITY = 2;
    private final String TITLE = "Title";

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
    public void testInsert() {
        ActivityScenario.launch(MainActivity.class).onActivity(activity -> {
            insertMovie(createMovie());

            getDao().getMovies().observe(activity, movies -> {
                Movie movie = movies.get(0);
                assertThat(movie.getId()).isEqualTo(ID);
                assertThat(movie.getPopularity()).isEqualTo(POPULARITY);
                assertThat(movie.getTitle()).isEqualTo(TITLE);
                signal.countDown();
            });
        });
    }

    private Movie createMovie() {
        Movie movie = new Movie();
        movie.setId(ID);
        movie.setPopularity(POPULARITY);
        movie.setTitle(TITLE);
        return movie;
    }

    private void insertMovie(Movie movie) {
        getDiskIO().execute(() -> {
            List<Movie> movies = new ArrayList<>();
            movies.add(movie);
            getDao().bulkInsert(movies);
            waitForSignal();
        });
    }

    private Executor getDiskIO() {
        return AppExecutors.getInstance().diskIO();
    }

    private MovieDao getDao() {
        Context context = ApplicationProvider.getApplicationContext();
        return Database.getInstance(context).movieDao();
    }

    private void waitForSignal() {
        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}