package com.udacity.popularmovies.api;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.udacity.popularmovies.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class TMDbTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);
    private CountDownLatch signal = new CountDownLatch(1);

    private TMDb spyTMDb() {
        TMDb tmdb = new TMDb(activityRule.getActivity()) {
            @Override
            public void fetchPopularMoviesJson() {
                super.fetchPopularMoviesJson();
                try {
                    signal.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void updateFromDownload(String result) {
                super.updateFromDownload(result);
                assertThat(result).isNotEmpty();
                signal.countDown();
            }
        };
        activityRule.getActivity().getTMDb().getNetworkFragment().setCallback(tmdb);
        return tmdb;
    }

    @Test
    public void fetchPopularMoviesJson() {
        spyTMDb().fetchPopularMoviesJson();
    }
}