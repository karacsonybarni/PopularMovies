package com.udacity.popularmovies.ui.mainview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.data.database.Movie;
import com.udacity.popularmovies.ui.detailview.DetailActivity;

import java.util.List;

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.PosterViewHolder> {

    private Context context;
    private List<Movie> movies;

    MoviesAdapter(Context context) {
        this.context = context;
    }

    void updateAll(List<Movie> movies) {
        this.movies = movies;
        if (movies != null) {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View posterView =
                LayoutInflater.from(context).inflate(R.layout.poster, parent, false);
        return new PosterViewHolder(posterView);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {
        Movie movie = movies.get(position);
        ImageView posterView = holder.posterView;

        loadPoster(movie.getPosterPath(), posterView);
        posterView.setOnClickListener(newClickListener(movie));
    }

    private void loadPoster(String posterPath, ImageView posterView) {
        String posterUrl = context.getString(R.string.movie_poster_url, posterPath);
        Picasso.get().load(posterUrl).into(posterView);
    }

    private View.OnClickListener newClickListener(Movie movie) {
        return v -> {
            Intent startDetailActivityIntent = new Intent(context, DetailActivity.class);
            startDetailActivityIntent.putExtra(DetailActivity.MOVIE_EXTRA, movie.getId());
            context.startActivity(startDetailActivityIntent);
        };
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    class PosterViewHolder extends RecyclerView.ViewHolder {

        private ImageView posterView;

        private PosterViewHolder(@NonNull View itemView) {
            super(itemView);
            posterView = itemView.findViewById(R.id.poster);
        }
    }
}
