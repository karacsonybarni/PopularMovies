package com.udacity.popularmovies.mainview;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.api.MoviesUpdateListener;
import com.udacity.popularmovies.api.TMDb;
import com.udacity.popularmovies.model.Movie;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesUpdateListener {

    private TMDb tmdb;
    private MoviesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tmdb = new TMDb(this);
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
        tmdb.setMoviesUpdateListener(this);
        tmdb.fetchPopularMovies();
    }

    @Override
    public void onMoviesUpdated(List<Movie> movies) {
        adapter.updateAll(movies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sorting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_by_popularity) {
            adapter.updateAll(tmdb.getMoviesSortedByPopularity());
            return true;
        }
        if (id == R.id.action_sort_by_rating) {
            adapter.updateAll(tmdb.getMoviesSortedByRating());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
