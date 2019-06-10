package com.udacity.popularmovies.utils;

import androidx.fragment.app.FragmentActivity;

import com.udacity.popularmovies.api.MoviesNetworkDataSource;
import com.udacity.popularmovies.data.Repository;
import com.udacity.popularmovies.data.database.Database;
import com.udacity.popularmovies.data.database.MovieDao;

public class InjectorUtils {

    public static Repository getRepository(FragmentActivity activity) {
        MovieDao movieDao = Database.getInstance(activity.getApplicationContext()).movieDao();
        AppExecutors executors = AppExecutors.getInstance();
        MoviesNetworkDataSource moviesNetworkDataSource =
                MoviesNetworkDataSource.getInstance(activity);
        return Repository.getInstance(movieDao, moviesNetworkDataSource, executors);
    }
}
