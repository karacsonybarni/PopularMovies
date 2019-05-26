package com.udacity.popularmovies;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.udacity.popularmovies.api.TMDb;

public class MainActivity extends AppCompatActivity {

    private TMDb tmdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tmdb = new TMDb(this);
    }

    public TMDb getTMDb() {
        return tmdb;
    }
}
