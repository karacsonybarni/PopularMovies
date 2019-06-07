package com.udacity.popularmovies.api;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.network.DownloadListener;
import com.udacity.popularmovies.network.TextDownloader;
import com.udacity.popularmovies.utils.json.MoviesParser;

import org.json.JSONException;

import java.util.ArrayList;

public class TMDb implements DownloadListener {

    private Context context;
    private TextDownloader textDownloader;
    private String popularMoviesUrl;
    private String topRatedMoviesUrl;
    private MoviesUpdateListener moviesUpdateListener;

    public TMDb(FragmentActivity activity) {
        this.context = activity;
        textDownloader = new TextDownloader(activity, this);
        initUrl();
    }

    private void initUrl() {
        String apiKey = context.getString(R.string.api_key);
        popularMoviesUrl = context.getString(R.string.popular_movies_query, apiKey);
        topRatedMoviesUrl = context.getString(R.string.top_rated_movies_query, apiKey);
    }

    public void fetchPopularMovies() {
        textDownloader.download(popularMoviesUrl);
    }

    public void fetchTopRatedMovies() {
        textDownloader.download(topRatedMoviesUrl);
    }

    @Override
    public void onDataDownloaded(String data) {
        if (data == null) {
            moviesUpdateListener.onNoMovies();
            return;
        }
        try {
            ArrayList<Movie> movies = MoviesParser.parse(data);
            moviesUpdateListener.onMoviesUpdated(movies);
        } catch (JSONException e) {
            // shouldn't happen in the production version
            Log.e(TMDb.class.getSimpleName(), "onDataDownloaded: ", e);
        }
    }

    public void setMoviesUpdateListener(MoviesUpdateListener moviesUpdateListener) {
        this.moviesUpdateListener = moviesUpdateListener;
    }
}
