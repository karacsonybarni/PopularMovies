package com.udacity.popularmovies.utils;

import androidx.fragment.app.FragmentActivity;

import com.udacity.popularmovies.api.MoviesNetworkDataSource;
import com.udacity.popularmovies.data.Repository;
import com.udacity.popularmovies.data.database.Database;

public class InjectorUtils {

    public static Repository getRepository(FragmentActivity activity) {
        Database database = Database.getInstance(activity.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        MoviesNetworkDataSource moviesNetworkDataSource =
                MoviesNetworkDataSource.getInstance(activity);
        return Repository.getInstance(database, moviesNetworkDataSource, executors);
    }
}
