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
import com.udacity.popularmovies.utils.ErrorInfo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesUpdateListener {

    private static final String MOVIES = "movies";
    private static final String MOVIE_SORTING = "movieSorting";
    private static final MovieSorting DEFAULT_MOVIE_SORTING = MovieSorting.SORTED_BY_POPULARITY;

    private MovieSorting movieSorting;
    private TMDb tmdb;
    private MoviesAdapter adapter;
    private RecyclerView posterGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tmdb = new TMDb(this);
        tmdb.setMoviesUpdateListener(this);
        adapter = new MoviesAdapter(this);
        initPosterGrid();

        if (savedInstanceState != null) {
            initFormSavedInstanceState(savedInstanceState);
        } else {
            movieSorting = DEFAULT_MOVIE_SORTING;
            fetchMovies();
        }
    }

    private void initPosterGrid() {
        posterGrid = findViewById(R.id.posters);
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

    private void initFormSavedInstanceState(Bundle savedInstanceState) {
        movieSorting = (MovieSorting) savedInstanceState.getSerializable(MOVIE_SORTING);
        if (movieSorting == null) {
            movieSorting = DEFAULT_MOVIE_SORTING;
        }
        ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(MOVIES);
        if (movies != null) {
            adapter.updateAll(movies);
        } else {
            fetchMovies();
        }
    }

    private void fetchMovies() {
        switch (movieSorting) {
            case SORTED_BY_POPULARITY:
                tmdb.fetchPopularMovies();
                return;
            case SORTED_BY_RATING:
                tmdb.fetchTopRatedMovies();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MOVIE_SORTING, movieSorting);
        outState.putParcelableArrayList(MOVIES, adapter.getMovies());
    }

    @Override
    public void onMoviesUpdated(ArrayList<Movie> movies) {
        adapter.updateAll(movies);
    }

    @Override
    public void onNoMovies() {
        ErrorInfo.showNoInternetSnackbar(posterGrid, v -> fetchMovies());
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
            movieSorting = MovieSorting.SORTED_BY_POPULARITY;
            tmdb.fetchPopularMovies();
            return true;
        }
        if (id == R.id.action_sort_by_rating) {
            movieSorting = MovieSorting.SORTED_BY_RATING;
            tmdb.fetchTopRatedMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
