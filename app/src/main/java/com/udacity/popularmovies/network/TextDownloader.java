package com.udacity.popularmovies.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.fragment.app.FragmentActivity;

public class TextDownloader implements DownloadCallback<String> {

    private FragmentActivity activity;
    private NetworkFragment networkFragment;
    private DownloadListener downloadListener;

    public TextDownloader(FragmentActivity activity, DownloadListener downloadListener) {
        this.activity = activity;
        this.downloadListener = downloadListener;
        networkFragment =
                NetworkFragment.getInstance(
                        activity.getSupportFragmentManager(),
                        this);
    }

    public void download(String url) {
        networkFragment.startDownload(url);
    }

    @Override
    public void updateFromDownload(String result) {
        if (result != null) {
            downloadListener.onDataDownloaded(result);
        } else {
            downloadListener.onNoData();
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }
}
