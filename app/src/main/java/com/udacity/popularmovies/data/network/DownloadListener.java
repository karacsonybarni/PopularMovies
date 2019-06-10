package com.udacity.popularmovies.data.network;

import androidx.annotation.NonNull;

public interface DownloadListener {
    void onDataDownloaded(@NonNull String data);
    void onNoData();
}
