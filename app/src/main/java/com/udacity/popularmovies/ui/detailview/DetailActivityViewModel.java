package com.udacity.popularmovies.ui.detailview;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.popularmovies.data.Repository;
import com.udacity.popularmovies.data.database.Movie;

class DetailActivityViewModel extends ViewModel {

    private LiveData<Movie> movie;

    DetailActivityViewModel(@NonNull Repository repository, int movieId) {
        movie = repository.getMovie(movieId);
    }

    LiveData<Movie> getMovie() {
        return movie;
    }
}
