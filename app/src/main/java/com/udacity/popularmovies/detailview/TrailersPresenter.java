package com.udacity.popularmovies.detailview;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.api.TMDb;
import com.udacity.popularmovies.api.TrailersUpdateListener;
import com.udacity.popularmovies.model.Trailer;
import com.udacity.popularmovies.utils.ErrorInfo;

import java.util.List;

class TrailersPresenter implements TrailersUpdateListener {

    private DetailActivity activity;
    private TMDb tmdb;
    private View trailersView;

    TrailersPresenter(DetailActivity activity) {
        this.activity = activity;
        tmdb = new TMDb(activity);
        tmdb.setTrailersUpdateListener(this);
    }

    void fetchTrailers() {
        tmdb.fetchTrailers(activity.getMovieId());
    }

    @Override
    public void onTrailersFetched(List<Trailer> trailers) {
        addTrailersToLayout(trailers);
        showTrailersLayout();
    }

    private void showTrailersLayout() {
        activity.findViewById(R.id.trailers_separator).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.trailers_label).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.trailers).setVisibility(View.VISIBLE);
    }

    private void addTrailersToLayout(List<Trailer> trailers) {
        LinearLayout trailersView = activity.findViewById(R.id.trailers);
        int i = 0;
        for (Trailer trailer : trailers) {
            addTrailerView(trailersView, trailer);
            if (i < trailers.size() - 1) {
                addSeparator(trailersView);
            }
            i++;
        }
    }

    private void addTrailerView(LinearLayout trailersView, Trailer trailer) {
        TextView trailerView =
                (TextView) activity.getLayoutInflater()
                        .inflate(R.layout.trailer, trailersView, false);
        trailerView.setText(trailer.getName());
        trailerView.setOnClickListener(v -> playTrailer(trailer.getKey()));
        trailersView.addView(trailerView);
    }

    private void playTrailer(String id) {
        Uri appUri = Uri.parse("vnd.youtube:" + id);
        Intent appIntent = new Intent(Intent.ACTION_VIEW, appUri);
        try {
            activity.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            Uri webPageUri = Uri.parse("http://www.youtube.com/watch?v=" + id);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webPageUri);
            activity.startActivity(webIntent);
        }
    }

    private void addSeparator(LinearLayout trailersView) {
        activity.getLayoutInflater().inflate(R.layout.trailer_separator, trailersView);
    }

    @Override
    public void onNoTrailers() {
        ErrorInfo.showNoInternetSnackbar(
                getTrailersView(),
                v -> tmdb.fetchTrailers(activity.getMovieId()));
    }

    private View getTrailersView() {
        if (trailersView == null) {
            trailersView = activity.findViewById(R.id.trailers);
        }
        return trailersView;
    }
}
