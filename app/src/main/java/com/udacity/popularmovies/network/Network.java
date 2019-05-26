package com.udacity.popularmovies.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class Network {

    private ProgressListener progressListener;

    Network(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    String getString(URL url) throws IOException {
        InputStream stream = null;
        HttpURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            progressListener.notifyOnProgress(DownloadCallback.Progress.CONNECT_SUCCESS);
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            stream = connection.getInputStream();
            progressListener.notifyOnProgress(
                    DownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS, 0);
            if (stream != null) {
                result = readStream(stream);
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    private String readStream(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        scanner.useDelimiter("\\A");

        boolean hasInput = scanner.hasNext();
        if (hasInput) {
            return scanner.next();
        } else {
            return null;
        }
    }
}
