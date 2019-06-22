package com.udacity.popularmovies.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.udacity.popularmovies.api.MoviesNetworkDataSource;
import com.udacity.popularmovies.data.database.Database;
import com.udacity.popularmovies.data.database.Movie;
import com.udacity.popularmovies.data.database.MovieDao;
import com.udacity.popularmovies.utils.AppExecutors;

import java.util.List;

public class Repository {

    private static Repository sInstance;
    private static final Object LOCK = new Object();

    private Database database;
    private MovieDao movieDao;
    private MoviesNetworkDataSource moviesNetworkDataSource;
    private AppExecutors executors;
    private Observer<? super List<Movie>> networkDataObserver;
    private UpdateErrorListener updateErrorListener;


    private Repository(Database database,
                       MoviesNetworkDataSource moviesNetworkDataSource,
                       AppExecutors executors) {
        this.database = database;
        this.moviesNetworkDataSource = moviesNetworkDataSource;
        this.executors = executors;
        movieDao = database.movieDao();

        networkDataObserver = newNetworkDataObserver();
        moviesNetworkDataSource.getMovies().observeForever(networkDataObserver);
    }

    private Observer<? super List<Movie>> newNetworkDataObserver() {
        return movies ->
                executors.diskIO().execute(() -> {
                    if (movies != null && !movies.isEmpty()) {
                        tryToUpdateMovies(movies);
                    } else {
                        notifyUpdateErrorListener();
                    }
                });
    }

    private void tryToUpdateMovies(List<Movie> movies) {
        try {
            movieDao.updateMovies(movies);
        } catch (IllegalStateException ignored) {
            executors.mainThread().execute(this::close);
        }
    }

    public void close() {
        updateErrorListener = null;
        closeNetworkDataSource();
        database.close();
        sInstance = null;
    }

    private void closeNetworkDataSource() {
        removeNetworkDataSourceObserver();
        if (moviesNetworkDataSource != null) {
            moviesNetworkDataSource.close();
        }
    }

    private void removeNetworkDataSourceObserver() {
        MutableLiveData<List<Movie>> moviesLiveData =
                moviesNetworkDataSource != null ? moviesNetworkDataSource.getMovies() : null;
        if (moviesLiveData != null) {
            moviesLiveData.removeObserver(networkDataObserver);
        }
    }

    private void notifyUpdateErrorListener() {
        if (updateErrorListener != null) {
            updateErrorListener.onNoMovies();
        }
    }

    @NonNull
    public static Repository getInstance(Database database,
                                         MoviesNetworkDataSource moviesNetworkDataSource,
                                         AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Repository(database, moviesNetworkDataSource, executors);
            }
        }
        return sInstance;
    }

    @Nullable
    public static Repository getInstance() {
        return sInstance;
    }

    public LiveData<List<Movie>> getMovies() {
        return movieDao.getMovies();
    }

    public LiveData<Movie> getMovie(int id) {
        return movieDao.getMovie(id);
    }

    public void setUpdateErrorListener(UpdateErrorListener updateErrorListener) {
        this.updateErrorListener = updateErrorListener;
    }

    public void update(Movie movie) {
        executors.diskIO().execute(() -> movieDao.insert(movie));
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return movieDao.getFavoriteMovies();
    }
}
