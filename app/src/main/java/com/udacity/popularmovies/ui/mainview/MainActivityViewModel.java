package com.udacity.popularmovies.ui.mainview;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.popularmovies.api.TMDb;
import com.udacity.popularmovies.data.Repository;
import com.udacity.popularmovies.data.UpdateErrorListener;
import com.udacity.popularmovies.data.database.Movie;

import java.util.List;

class MainActivityViewModel extends ViewModel {

    private Repository repository;
    private TMDb tmdb;
    private LiveData<List<Movie>> popularMovies;
    private LiveData<List<Movie>> topRatedMovies;
    private LiveData<List<Movie>> favoriteMovies;

    MainActivityViewModel(@NonNull Repository repository) {
        this.repository = repository;
    }

    LiveData<List<Movie>> getPopularMovies() {
        if (popularMovies == null) {
            tmdb.fetchPopularMovies();
            popularMovies = repository.getPopularMovies();
        }
        return popularMovies;
    }

    LiveData<List<Movie>> getTopRatedMovies() {
        if (topRatedMovies == null) {
            tmdb.fetchTopRatedMovies();
            topRatedMovies = repository.getTopRatedMovies();
        }
        return topRatedMovies;
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

    void removeObservers(LifecycleOwner lifecycleOwner) {
        if (popularMovies != null) {
            popularMovies.removeObservers(lifecycleOwner);
        }
        if (topRatedMovies != null) {
            topRatedMovies.removeObservers(lifecycleOwner);
        }
        if (favoriteMovies != null) {
            favoriteMovies.removeObservers(lifecycleOwner);
        }
    }

    void setTmdb(TMDb tmdb) {
        this.tmdb = tmdb;
    }
}
