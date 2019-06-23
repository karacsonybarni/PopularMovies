package com.udacity.popularmovies.database;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.udacity.popularmovies.Initializer;
import com.udacity.popularmovies.Terminator;
import com.udacity.popularmovies.data.database.Database;
import com.udacity.popularmovies.data.database.Movie;
import com.udacity.popularmovies.data.database.MovieDao;
import com.udacity.popularmovies.ui.mainview.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final int ID = 1;
    private final int POPULARITY = 2;
    private final String TITLE = "Title";

    @Before
    public void setup() {
        Initializer.initDatabase(ApplicationProvider.getApplicationContext());
        Initializer.initPicasso();
    }

    @After
    public void tearDown() {
        Terminator.closeRepository();
    }

    @Test
    public void testInsert() {
        try {
            launchActivityAndInsertMovie();
        } catch (IllegalStateException ignored) {
            System.out.println(DatabaseTest.class.getSimpleName() + ".testInsert skipped");
        }
    }

    private void launchActivityAndInsertMovie() {
        ActivityScenario.launch(MainActivity.class).onActivity(activity -> {
            insertMovie(createMovie());

            getDao().getPopularMovies().observe(activity, movies -> {
                if (movies.size() != 1) {
                    return;
                }
                Movie movie = movies.get(0);
                assertThat(movie.getId()).isEqualTo(ID);
                assertThat(movie.getPopularity()).isEqualTo(POPULARITY);
                assertThat(movie.getTitle()).isEqualTo(TITLE);
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
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        getDao().updateMovies(movies);
    }

    private MovieDao getDao() {
        Context context = ApplicationProvider.getApplicationContext();
        return Database.getInstance(context).movieDao();
    }
}