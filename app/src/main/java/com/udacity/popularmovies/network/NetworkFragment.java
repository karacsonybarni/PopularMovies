package com.udacity.popularmovies.network;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * Implementation of headless Fragment that runs an AsyncTask to fetch data from the network.
 */
public class NetworkFragment extends Fragment {

    private static final String TAG = "NetworkFragment";

    private FetchCallback<String> callback;
    private FetchTask fetchTask;

    private NetworkFragment(FetchCallback<String> callback) {
        this.callback = callback;
    }

    public static NetworkFragment getInstance(
            FragmentManager fragmentManager,
            FetchCallback<String> fetchCallback) {

        // Recover NetworkFragment in case we are re-creating the Activity due to a config change.
        // This is necessary because NetworkFragment might have a task that began running before
        // the config change occurred and has not finished yet.
        // The NetworkFragment is recoverable because it calls setRetainInstance(true).
        NetworkFragment networkFragment = (NetworkFragment) fragmentManager
                .findFragmentByTag(NetworkFragment.TAG);
        if (networkFragment == null) {
            networkFragment = new NetworkFragment(fetchCallback);
            fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        }
        return networkFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity to avoid memory leak.
        callback = null;
    }

    @Override
    public void onDestroy() {
        // Cancel task when Fragment is destroyed.
        cancelDownload();
        super.onDestroy();
    }

    /**
     * Start non-blocking execution of FetchTask.
     */
    public void fetch(String urlString) {
        cancelDownload();
        fetchTask = new FetchTask(callback);
        fetchTask.execute(urlString);
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing FetchTask execution.
     */
    private void cancelDownload() {
        if (fetchTask != null) {
            fetchTask.cancel(true);
        }
    }

    public void setCallback(FetchCallback<String> callback) {
        this.callback = callback;
    }
}