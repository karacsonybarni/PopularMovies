package com.udacity.popularmovies.ui.detailview;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.popularmovies.data.Repository;
import com.udacity.popularmovies.data.database.Movie;

class DetailActivityViewModel extends ViewModel {

    private Repository repository;

    DetailActivityViewModel(@NonNull Repository repository) {
        this.repository = repository;
    }

    LiveData<Movie> getMovie(int id) {
        return repository.getMovie(id);
    }
}
