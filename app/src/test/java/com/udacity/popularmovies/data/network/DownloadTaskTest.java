package com.udacity.popularmovies.data.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.udacity.popularmovies.Initializer;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.Terminator;
import com.udacity.popularmovies.ui.mainview.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class DownloadTaskTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private ActivityScenario<MainActivity> scenario;
    private CountDownLatch signal = new CountDownLatch(1);

    @Before
    public void setup() {
        Initializer.initDatabase(ApplicationProvider.getApplicationContext());
        Initializer.initPicasso();
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    @After
    public void tearDown() {
        Terminator.closeRepository();
    }

    @Test
    public void execute() {
        scenario.onActivity(activity -> {
            DownloadTask task = spyFetchTask(activity);
            String apiKey = activity.getString(R.string.api_key);
            String popularMoviesUrl = activity.getString(R.string.popular_movies_query, apiKey);
            task.execute(popularMoviesUrl);
            try {
                signal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private DownloadTask spyFetchTask(Activity activity) {
        return new DownloadTask(mockCallback(activity)) {
            @Override
            protected void onPostExecute(Result result) {
                assertThat(result.resultValue).isNotEmpty();
                assertThat(result.exception).isNull();
                signal.countDown();
            }
        };
    }

    private DownloadCallback<String> mockCallback(Activity activity) {
        return new DownloadCallback<String>() {
            @Override
            public void updateFromDownload(String result) {

            }

            @Override
            public NetworkInfo getActiveNetworkInfo() {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
                return connectivityManager.getActiveNetworkInfo();
            }
        };
    }
}