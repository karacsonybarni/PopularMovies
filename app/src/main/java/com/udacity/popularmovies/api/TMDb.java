package com.udacity.popularmovies.api;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.network.DownloadListener;
import com.udacity.popularmovies.network.TextDownloader;
import com.udacity.popularmovies.utils.json.MoviesParser;

import org.json.JSONException;

import java.util.Collections;
import java.util.List;

public class TMDb implements DownloadListener {

    private Context context;
    private TextDownloader textDownloader;
    private String popularMoviesUrl;
    private MoviesUpdateListener moviesUpdateListener;

    @Nullable
    private List<Movie> movies;

    public TMDb(FragmentActivity activity) {
        this.context = activity;
        textDownloader = new TextDownloader(activity, this);
        initUrl();
    }

    private void initUrl() {
        String apiKey = context.getString(R.string.api_key);
        popularMoviesUrl = context.getString(R.string.popular_movies_query, apiKey);
    }

    public void fetchPopularMovies() {
        textDownloader.download(popularMoviesUrl);
    }

    @Nullable
    public List<Movie> getMoviesSortedByPopularity() {
        if (movies == null) {
            return null;
        }
        Collections.sort(movies, (m1, m2) -> (int) (m2.getPopularity() - m1.getPopularity()));
        return movies;
    }

    @Nullable
    public List<Movie> getMoviesSortedByRating() {
        if (movies == null) {
            return null;
        }
        Collections.sort(movies, (m1, m2) -> (int) (m2.getRating() - m1.getRating()));
        return movies;
    }

    @Override
    public void onDataDownloaded(String data) {
        if (data == null) {
            Toast.makeText(context, R.string.no_internet_error, Toast.LENGTH_LONG).show();
            return;
        }
        try {
            movies = MoviesParser.parse(data);
            moviesUpdateListener.onMoviesUpdated(movies);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setMoviesUpdateListener(MoviesUpdateListener moviesUpdateListener) {
        this.moviesUpdateListener = moviesUpdateListener;
    }
}
