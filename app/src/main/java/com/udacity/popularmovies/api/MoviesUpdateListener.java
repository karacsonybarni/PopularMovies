package com.udacity.popularmovies.api;

import com.udacity.popularmovies.model.Movie;

import java.util.List;

public interface MoviesUpdateListener {
    void onMoviesUpdated(List<Movie> movies);
}
