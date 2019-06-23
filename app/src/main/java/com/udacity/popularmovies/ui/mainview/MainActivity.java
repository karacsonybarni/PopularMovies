package com.udacity.popularmovies.ui.mainview;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.api.TMDb;
import com.udacity.popularmovies.data.Repository;
import com.udacity.popularmovies.data.UpdateErrorListener;
import com.udacity.popularmovies.data.database.Movie;
import com.udacity.popularmovies.utils.ErrorInfo;
import com.udacity.popularmovies.utils.InjectorUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements UpdateErrorListener {

    private static final String MOVIE_SORTING = "movieSorting";
    private static final MovieSorting DEFAULT_MOVIE_SORTING = MovieSorting.SORTED_BY_POPULARITY;

    private MainActivityViewModel viewModel;
    private MovieSorting movieSorting;
    private TMDb tmdb;
    private MoviesAdapter adapter;
    private RecyclerView posterGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tmdb = new TMDb(this);
        viewModel = newViewModel();
        adapter = new MoviesAdapter(this);
        initPosterGrid();

        if (savedInstanceState != null) {
            initFormSavedInstanceState(savedInstanceState);
        } else {
            movieSorting = DEFAULT_MOVIE_SORTING;
        }
        showMovies();
    }

    private MainActivityViewModel newViewModel() {
        Repository repository = InjectorUtils.getRepository(this);
        MainViewModelFactory factory = new MainViewModelFactory(repository);
        MainActivityViewModel viewModel =
                ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);
        viewModel.setUpdateErrorListener(this);
        viewModel.setTmdb(tmdb);
        return viewModel;
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
    }

    private void showMovies() {
        switch (movieSorting) {
            case SORTED_BY_POPULARITY:
                observePopularMovies();
                return;
            case SORTED_BY_RATING:
                observeTopRatedMovies();
                return;
            case FAVORITES:
                observeFavoriteMovies();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MOVIE_SORTING, movieSorting);
    }

    @Override
    public void onNoMovies() {
        ErrorInfo.showNoInternetSnackbar(posterGrid, v -> showMovies());
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
            selectPopularMovies();
            return true;
        }
        if (id == R.id.action_sort_by_rating) {
            selectTopRatedMovies();
            return true;
        }
        if (id == R.id.action_favorites) {
            selectFavoriteMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectPopularMovies() {
        movieSorting = MovieSorting.SORTED_BY_POPULARITY;
        observePopularMovies();
        tmdb.fetchPopularMovies();
    }

    private void observePopularMovies() {
        viewModel.removeObservers(this);
        viewModel.getPopularMovies().observe(this, this::updateAdapter);
    }

    private void selectTopRatedMovies() {
        movieSorting = MovieSorting.SORTED_BY_RATING;
        observeTopRatedMovies();
        tmdb.fetchTopRatedMovies();
    }

    private void observeTopRatedMovies() {
        viewModel.removeObservers(this);
        viewModel.getTopRatedMovies().observe(this, this::updateAdapter);
    }

    private void selectFavoriteMovies() {
        movieSorting = MovieSorting.FAVORITES;
        observeFavoriteMovies();
    }

    private void observeFavoriteMovies() {
        viewModel.removeObservers(this);
        viewModel.getFavoriteMovies().observe(this, this::updateAdapter);
    }

    private void updateAdapter(List<Movie> movies) {
        runOnUiThread(() -> adapter.updateAll(movies));
    }

    @Override
    protected void onDestroy() {
        tmdb.close();
        viewModel.removeObservers(this);
        viewModel.removeUpdateErrorListener();
        super.onDestroy();
    }
}
