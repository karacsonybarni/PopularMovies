package com.udacity.popularmovies.network;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class NetworkTest {

    private Network network;

    public NetworkTest() {
        network = new Network(mockProgressListener());
    }

    private ProgressListener mockProgressListener() {
        return new ProgressListener() {
            @Override
            public void notifyOnProgress(Integer... values) {

            }
        };
    }

    @Test
    public void getString() throws IOException {
        URL url = new URL("https://www.google.com");
        String result = network.getString(url);
        assertThat(result).isNotEmpty();
    }
}