package com.udacity.popularmovies.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.fragment.app.FragmentActivity;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.network.FetchCallback;
import com.udacity.popularmovies.network.NetworkFragment;

public class TMDb implements FetchCallback<String> {

    private FragmentActivity activity;
    private NetworkFragment networkFragment;

    private final String popularMoviesUrl;

    public TMDb(FragmentActivity activity) {
        this.activity = activity;
        String apiKey = activity.getString(R.string.api_key);
        popularMoviesUrl = activity.getString(R.string.popular_movies_query, apiKey);
        networkFragment =
                NetworkFragment.getInstance(
                        activity.getSupportFragmentManager(),
                        this);
    }

    public void fetchPopularMoviesJson() {
        networkFragment.fetch(popularMoviesUrl);
    }

    @Override
    public void updateFromDownload(String result) {

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    @Override
    public void finishDownloading() {

    }

    NetworkFragment getNetworkFragment() {
        return networkFragment;
    }
}
