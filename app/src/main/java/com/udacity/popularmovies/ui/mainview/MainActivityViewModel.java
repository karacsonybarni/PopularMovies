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
    private LiveData<List<Movie>> allMovies;
    private LiveData<List<Movie>> favoriteMovies;

    MainActivityViewModel(@NonNull Repository repository) {
        this.repository = repository;
    }

    LiveData<List<Movie>> getMovies() {
        if (allMovies == null) {
            allMovies = repository.getMovies();
        }
        return allMovies;
    }

    LiveData<List<Movie>> getFavoriteMovies() {
        if (favoriteMovies == null) {
            favoriteMovies = repository.getFavoriteMovies();
        }
        return favoriteMovies;
    }

    void setUpdateErrorListener(UpdateErrorListener listener) {
        repository.setUpdateErrorListener(listener);
    }

    void removeUpdateErrorListener() {
        repository.setUpdateErrorListener(null);
    }

    @Override
    protected void onCleared() {
        removeUpdateErrorListener();
    }
}
