package com.udacity.popularmovies.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.udacity.popularmovies.mainview.MainActivity;
import com.udacity.popularmovies.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class DownloadTaskTest {

    private ActivityScenario<MainActivity> scenario;
    private CountDownLatch signal = new CountDownLatch(1);

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(MainActivity.class);
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