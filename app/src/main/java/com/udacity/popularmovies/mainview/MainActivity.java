package com.udacity.popularmovies.mainview;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.api.MoviesUpdateListener;
import com.udacity.popularmovies.api.TMDb;
import com.udacity.popularmovies.model.Movie;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesUpdateListener {

    private MoviesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new MoviesAdapter(this);
        initPosterGrid();
        fetchMovies();
    }

    private void initPosterGrid() {
        RecyclerView posterGrid = findViewById(R.id.posters);
        posterGrid.setLayoutManager(new GridLayoutManager(this, getGridColumnCount()));
        posterGrid.setAdapter(adapter);
    }

    private int getGridColumnCount() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return 2;
        } else {
            return 4;
        }
    }

    private void fetchMovies() {
        TMDb tmdb = new TMDb(this);
        tmdb.setMoviesUpdateListener(this);
        tmdb.fetchPopularMovies();
    }

    @Override
    public void onMoviesUpdated(List<Movie> movies) {
        adapter.updateAll(movies);
    }
}
