package com.udacity.popularmovies.data.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.fragment.app.FragmentActivity;

public class TextDownloader implements DownloadCallback<String> {

    private NetworkFragment networkFragment;
    private DownloadListener downloadListener;
    private ConnectivityManager connectivityManager;

    public TextDownloader(FragmentActivity activity, DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
        networkFragment = NetworkFragment.getInstance(activity.getSupportFragmentManager());
        connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void download(String url) {
        networkFragment.startDownload(url, this);
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
        return connectivityManager.getActiveNetworkInfo();
    }
}
