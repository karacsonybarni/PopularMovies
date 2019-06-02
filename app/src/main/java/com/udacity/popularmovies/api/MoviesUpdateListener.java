package com.udacity.popularmovies.api;

import com.udacity.popularmovies.model.Movie;

import java.util.ArrayList;

public interface MoviesUpdateListener {
    void onMoviesUpdated(ArrayList<Movie> movies);
}
