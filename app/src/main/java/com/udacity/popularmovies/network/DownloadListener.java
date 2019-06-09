package com.udacity.popularmovies.network;

import androidx.annotation.NonNull;

public interface DownloadListener {
    void onDataDownloaded(@NonNull String data);
    void onNoData();
}
