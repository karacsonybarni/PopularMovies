package com.udacity.popularmovies.ui.detailview;

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

import java.util.List;

class TrailersPresenter implements TrailersUpdateListener {

    private DetailActivity activity;
    private TMDb tmdb;
    private LinearLayout trailersLayout;

    TrailersPresenter(DetailActivity activity) {
        this.activity = activity;
        tmdb = activity.getTmdb();
        tmdb.setTrailersUpdateListener(this);

        trailersLayout = activity.findViewById(R.id.trailers);
    }

    void fetchTrailers() {
        tmdb.fetchTrailers(activity.getMovieId());
    }

    @Override
    public void onTrailersFetched(List<Trailer> trailers) {
        if (!trailers.isEmpty()) {
            setupSharingFirstTrailer(trailers.get(0).getKey());
            addTrailersToLayout(trailers);
            showTrailersLayout();
        }
    }

    private void setupSharingFirstTrailer(String trailerId) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        String url = activity.getString(R.string.trailer_web_url, trailerId);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        activity.getShareActionProvider().setShareIntent(sendIntent);
    }

    private void addTrailersToLayout(List<Trailer> trailers) {
        int i = 0;
        for (Trailer trailer : trailers) {
            addTrailerView(trailer);
            if (i < trailers.size() - 1) {
                addSeparator();
            }
            i++;
        }
    }

    private void addTrailerView(Trailer trailer) {
        TextView trailerView =
                (TextView) activity
                        .getLayoutInflater()
                        .inflate(R.layout.trailer, trailersLayout, false);
        trailerView.setText(trailer.getName());
        trailerView.setOnClickListener(v -> playTrailer(trailer.getKey()));
        trailersLayout.addView(trailerView);
    }

    private void playTrailer(String id) {
        try {
            shareAppUri(id);
        } catch (ActivityNotFoundException ex) {
            shareWebPageUrl(id);
        }
    }

    private void shareAppUri(String id) {
        String uriString = activity.getString(R.string.trailer_app_uri, id);
        Uri appUri = Uri.parse(uriString);
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, appUri);
        activity.startActivity(viewIntent);
    }

    private void shareWebPageUrl(String id) {
        String uriString = activity.getString(R.string.trailer_web_url, id);
        Uri uri = Uri.parse(uriString);
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(viewIntent);
    }

    private void addSeparator() {
        activity.getLayoutInflater().inflate(R.layout.thin_separator, trailersLayout);
    }

    private void showTrailersLayout() {
        activity.findViewById(R.id.trailers_separator).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.trailers_label).setVisibility(View.VISIBLE);
        trailersLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNoTrailers() {
        activity.onNoData();
    }
}
