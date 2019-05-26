package com.udacity.popularmovies.network;

public interface ProgressListener {
    void notifyOnProgress(Integer... values);
}
