package com.udacity.popularmovies.ui.detailview;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.api.TMDb;
import com.udacity.popularmovies.data.Repository;
import com.udacity.popularmovies.data.database.Movie;
import com.udacity.popularmovies.utils.ErrorInfo;
import com.udacity.popularmovies.utils.InjectorUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_EXTRA = "movieExtra";

    private int movieId;
    private Movie movie;
    private TMDb tmdb;
    private boolean noInternetErrorAlreadyShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initMovieId(getIntent().getExtras());
        initMovie();

        tmdb = new TMDb(this);
        fetchExtraContent();
    }

    private void fetchExtraContent() {
        new TrailersPresenter(this).fetchTrailers();
        new ReviewsPresenter(this).fetchReviews();
    }

    private void initMovieId(Bundle extras) {
        if (extras != null) {
            movieId = extras.getInt(MOVIE_EXTRA);
        } else {
            throw new IllegalStateException("no extras found in the intent");
        }
    }

    private void initMovie() {
        DetailActivityViewModel viewModel = newViewModel();
        viewModel.getMovie(movieId).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                viewModel.getMovie(movieId).removeObserver(this);

                DetailActivity.this.movie = movie;
                populateViews();
                loadPoster();
            }
        });
    }

    private DetailActivityViewModel newViewModel() {
        Repository repository = InjectorUtils.getRepository(this);
        DetailViewModelFactory factory = new DetailViewModelFactory(repository);
        return ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);
    }

    private void populateViews() {
        fillTextView(R.id.title, movie.getTitle());
        fillTextView(R.id.release_date, movie.getReleaseDate());
        fillTextView(R.id.rating, getString(R.string.rating, movie.getRating()));
        fillTextView(R.id.plot_synopsis, movie.getPlotSynopsis());
    }

    private void fillTextView(int textViewId, String text) {
        TextView textView = findViewById(textViewId);
        textView.setText(text);
    }

    private void loadPoster() {
        String posterUrl = getString(R.string.movie_poster_url, movie.getPosterPath());
        ImageView poster = findViewById(R.id.poster);
        Picasso.get().load(posterUrl).into(poster);
    }

    public int getMovieId() {
        return movieId;
    }

    TMDb getTmdb() {
        return tmdb;
    }

    void onNoData() {
        if (!noInternetErrorAlreadyShown) {
            ErrorInfo.showNoInternetSnackbar(findViewById(R.id.root), v -> {
                tmdb.fetchTrailers(movieId);
                tmdb.fetchReviews(movieId);
            });
            noInternetErrorAlreadyShown = true;
        }
    }
}
