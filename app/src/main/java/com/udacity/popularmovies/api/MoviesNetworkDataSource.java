package com.udacity.popularmovies.api;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.data.database.Movie;
import com.udacity.popularmovies.data.network.TextDownloader;

import java.util.List;

public class MoviesNetworkDataSource implements MoviesUpdateListener {

    private static MoviesNetworkDataSource sInstance;
    private final TextDownloader textDownloader;
    private final MutableLiveData<List<Movie>> downloadedMovies;

    private String popularMoviesUrl;
    private String topRatedMoviesUrl;

    private MoviesNetworkDataSource(FragmentActivity activity) {
        textDownloader = new TextDownloader(activity, new MoviesDownloadListener(this));
        downloadedMovies = new MutableLiveData<>();
        initUrls(activity);
    }

    private void initUrls(Context context) {
        String apiKey = context.getString(R.string.api_key);
        popularMoviesUrl = context.getString(R.string.popular_movies_query, apiKey);
        topRatedMoviesUrl = context.getString(R.string.top_rated_movies_query, apiKey);
    }

    public static MoviesNetworkDataSource getInstance(FragmentActivity activity) {
        if (sInstance == null) {
            sInstance = new MoviesNetworkDataSource(activity);
        }
        return sInstance;
    }

    void fetchPopularMovies() {
        textDownloader.download(popularMoviesUrl);
    }

    void fetchTopRatedMovies() {
        textDownloader.download(topRatedMoviesUrl);
    }

    public MutableLiveData<List<Movie>> getMovies() {
        return downloadedMovies;
    }

    @Override
    public void onMoviesUpdated(List<Movie> movies) {
        downloadedMovies.setValue(movies);
    }

    public void close() {
        sInstance = null;
    }
}
