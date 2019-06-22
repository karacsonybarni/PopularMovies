package com.udacity.popularmovies.ui.detailview;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.popularmovies.data.Repository;
import com.udacity.popularmovies.data.database.Movie;

class DetailActivityViewModel extends ViewModel {

    private Repository repository;
    private LiveData<Movie> movie;

    DetailActivityViewModel(@NonNull Repository repository, int movieId) {
        this.repository = repository;
        movie = repository.getMovie(movieId);
    }

    LiveData<Movie> getMovie() {
        return movie;
    }

    void markAsFavorite(Movie movie) {
        movie.setFavorite(true);
        repository.update(movie);
    }

    void markAsNotFavorite(Movie movie) {
        movie.setFavorite(false);
        repository.update(movie);
    }
}
