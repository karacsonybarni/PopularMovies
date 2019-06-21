package com.udacity.popularmovies;

import com.udacity.popularmovies.data.Repository;

public class Terminator {

    public static void closeRepository() {
        Repository repository = Repository.getInstance();
        if (repository != null) {
            repository.close();
        }
    }
}
