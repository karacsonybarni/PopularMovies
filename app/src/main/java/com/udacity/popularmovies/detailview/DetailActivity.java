package com.udacity.popularmovies.detailview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Movie;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_EXTRA = "movieExtra";

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initMovie(getIntent().getExtras());
        populateViews();
        loadPoster();
    }

    private void initMovie(Bundle extras) {
        if (extras != null) {
            movie = extras.getParcelable(MOVIE_EXTRA);
        } else {
            throw new IllegalStateException("no extras found in the intent");
        }
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
}
