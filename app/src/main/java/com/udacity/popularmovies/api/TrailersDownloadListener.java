package com.udacity.popularmovies.api;

import androidx.annotation.NonNull;

import com.udacity.popularmovies.model.Trailer;
import com.udacity.popularmovies.network.DownloadListener;
import com.udacity.popularmovies.utils.json.Parser;

import org.json.JSONException;

import java.util.List;

class TrailersDownloadListener implements DownloadListener {

    private TrailersUpdateListener listener;

    TrailersDownloadListener(TrailersUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDataDownloaded(@NonNull String data) {
        try {
            List<Trailer> trailers = Parser.parseTrailers(data);
            listener.onTrailersFetched(trailers);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNoData() {
        listener.onNoTrailers();
    }
}
