package com.udacity.popularmovies.data;

import androidx.lifecycle.LiveData;

import com.udacity.popularmovies.api.MoviesNetworkDataSource;
import com.udacity.popularmovies.data.database.MovieDao;
import com.udacity.popularmovies.data.database.Movie;
import com.udacity.popularmovies.utils.AppExecutors;

import java.util.List;

public class Repository {

    private static Repository sInstance;
    private static final Object LOCK = new Object();

    private MovieDao movieDao;
    private UpdateErrorListener updateErrorListener;


    private Repository(MovieDao movieDao,
                       MoviesNetworkDataSource moviesNetworkDataSource,
                       AppExecutors executors) {
        this.movieDao = movieDao;

        LiveData<List<Movie>> networkData = moviesNetworkDataSource.getMovies();
        networkData.observeForever(movies ->
                executors.diskIO().execute(() -> {
                    if (movies != null && !movies.isEmpty()) {
                        movieDao.deleteAllMovies();
                        movieDao.bulkInsert(movies);
                    } else {
                        if (updateErrorListener != null) {
                            updateErrorListener.onNoMovies();
                        }
                    }
                }));
    }

    public static Repository getInstance(MovieDao movieDao,
                                         MoviesNetworkDataSource moviesNetworkDataSource,
                                         AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Repository(movieDao, moviesNetworkDataSource, executors);
            }
        }
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
}
