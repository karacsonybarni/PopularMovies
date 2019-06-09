package com.udacity.popularmovies.api;

import com.udacity.popularmovies.model.Trailer;

import java.util.List;

public interface TrailersUpdateListener {
    void onTrailersFetched(List<Trailer> ids);
    void onNoTrailers();
}
