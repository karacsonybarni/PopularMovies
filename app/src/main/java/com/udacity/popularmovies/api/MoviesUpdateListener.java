package com.udacity.popularmovies.api;

import com.udacity.popularmovies.data.database.Movie;

import java.util.List;

interface MoviesUpdateListener {
    void onMoviesUpdated(List<Movie> movies);
}
