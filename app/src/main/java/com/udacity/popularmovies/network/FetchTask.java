package com.udacity.popularmovies.network;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

/**
 * Implementation of AsyncTask designed to fetch data from the network.
 */
class FetchTask extends AsyncTask<String, Integer, FetchTask.Result> {

    private FetchCallback<String> callback;
    private Network network;

    /**
     * Wrapper class that serves as a union of a result value and an exception. When the download
     * task has completed, either the result value or exception can be a non-null value.
     * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
     */
    static class Result {
        String resultValue;
        Exception exception;

        Result(String resultValue) {
            this.resultValue = resultValue;
        }

        Result(Exception exception) {
            this.exception = exception;
        }
    }

    FetchTask(FetchCallback<String> callback) {
        this.callback = callback;
        network = new Network();
    }

    /**
     * Cancel background network operation if we do not have network connectivity.
     */
    @Override
    protected void onPreExecute() {
        if (callback != null) {
            NetworkInfo networkInfo = callback.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                // If no connectivity, cancel task and update Callback with null data.
                callback.updateFromDownload(null);
                cancel(true);
            }
        }
    }

    /**
     * Defines work to perform on the background thread.
     */
    @Override
    protected FetchTask.Result doInBackground(String... urls) {
        Result result = null;
        if (!isCancelled() && urls != null && urls.length > 0) {
            String urlString = urls[0];
            try {
                URL url = new URL(urlString);
                String resultString = network.getString(url);
                if (resultString != null) {
                    result = new Result(resultString);
                } else {
                    throw new IOException("No response received.");
                }
            } catch (Exception e) {
                result = new Result(e);
            }
        }
        return result;
    }

    /**
     * Updates the FetchCallback with the result.
     */
    @Override
    protected void onPostExecute(Result result) {
        if (result != null && callback != null) {
            if (result.exception != null) {
                callback.updateFromDownload(result.exception.getMessage());
            } else if (result.resultValue != null) {
                callback.updateFromDownload(result.resultValue);
            }
            callback.finishDownloading();
        }
    }
}
