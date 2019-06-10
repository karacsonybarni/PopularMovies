package com.udacity.popularmovies.api;

import androidx.annotation.NonNull;

import com.udacity.popularmovies.data.network.DownloadListener;
import com.udacity.popularmovies.data.database.Movie;
import com.udacity.popularmovies.utils.json.Parser;

import org.json.JSONException;

import java.util.ArrayList;

class MoviesDownloadListener implements DownloadListener {

    private MoviesUpdateListener listener;

    MoviesDownloadListener(MoviesUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDataDownloaded(@NonNull String data) {
        try {
            ArrayList<Movie> movies = Parser.parseMovies(data);
            listener.onMoviesUpdated(movies);
        } catch (JSONException e) {
            // shouldn't happen in the production version
            e.printStackTrace();
        }
    }

    @Override
    public void onNoData() {
        listener.onMoviesUpdated(null);
    }
}
