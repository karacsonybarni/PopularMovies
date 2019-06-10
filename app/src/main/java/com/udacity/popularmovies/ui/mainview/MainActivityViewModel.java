package com.udacity.popularmovies.ui.mainview;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.popularmovies.data.Repository;
import com.udacity.popularmovies.data.UpdateErrorListener;
import com.udacity.popularmovies.data.database.Movie;

import java.util.List;

class MainActivityViewModel extends ViewModel {

    private Repository repository;

    MainActivityViewModel(@NonNull Repository repository) {
        this.repository = repository;
    }

    LiveData<List<Movie>> getMovies() {
        return repository.getMovies();
    }

    void setUpdateErrorListener(UpdateErrorListener listener) {
        repository.setUpdateErrorListener(listener);
    }
}
