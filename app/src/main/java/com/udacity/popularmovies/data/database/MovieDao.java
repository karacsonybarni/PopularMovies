package com.udacity.popularmovies.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public abstract class MovieDao {

    @Query("SELECT * FROM movie")
    public abstract LiveData<List<Movie>> getMovies();

    @Query("SELECT * FROM movie WHERE id = :id")
    public abstract LiveData<Movie> getMovie(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Movie movie);

    @Transaction
    public void updateMovies(List<Movie> movies) {
        deleteAllMovies();
        bulkInsert(movies);
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract void bulkInsert(List<Movie> movies);

    @Query("DELETE FROM movie WHERE favorite = 0")
    abstract void deleteAllMovies();

    @Query("SELECT * FROM movie WHERE favorite = 1")
    public abstract LiveData<List<Movie>> getFavoriteMovies();
}
