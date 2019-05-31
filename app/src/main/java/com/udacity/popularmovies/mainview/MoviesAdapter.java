package com.udacity.popularmovies.mainview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Movie;

import java.util.List;

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.PosterViewHolder> {

    private Context context;
    private List<Movie> movies;

    MoviesAdapter(Context context) {
        this.context = context;
        initPicasso();
    }

    private void initPicasso() {
        try {
            Picasso.get();
        } catch (IllegalStateException ignored) {
            Picasso.Builder builder = new Picasso.Builder(context);
            Picasso.setSingletonInstance(builder.build());
        }
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
        String posterPath = movies.get(position).getPosterPath();
        String posterUrl = context.getString(R.string.movie_poster_url, posterPath);
        Picasso.get().load(posterUrl).into(holder.posterView);
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    class PosterViewHolder extends RecyclerView.ViewHolder {

        private ImageView posterView;

        private PosterViewHolder(@NonNull View itemView) {
            super(itemView);
            posterView = itemView.findViewById(R.id.posterIv);
        }
    }
}
