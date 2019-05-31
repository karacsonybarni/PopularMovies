package com.udacity.popularmovies.api;

import androidx.fragment.app.FragmentActivity;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.udacity.popularmovies.mainview.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class TMDbTest {

    private ActivityScenario<MainActivity> scenario;
    private CountDownLatch signal = new CountDownLatch(1);

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void fetchPopularMoviesJson() {
        scenario.onActivity(activity -> {
            TMDb tmdb = spyTMDb(activity);
            tmdb.fetchPopularMovies();
        });
    }

    private TMDb spyTMDb(FragmentActivity activity) {
        return new TMDb(activity) {
            @Override
            public void fetchPopularMovies() {
                super.fetchPopularMovies();
                try {
                    signal.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDataDownloaded(String result) {
                assertThat(result).isNotEmpty();
                signal.countDown();
            }
        };
    }
}