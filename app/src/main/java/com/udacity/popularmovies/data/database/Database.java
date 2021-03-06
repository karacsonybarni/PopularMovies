package com.udacity.popularmovies.data.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Movie.class}, version = 2, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movies";
    private static Database sInstance;

    public static Database getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = createInstance(context);
            }
        }
        return sInstance;
    }

    @Override
    public void close() {
        super.close();
        sInstance = null;
    }

    private static Database createInstance(Context context) {
        return getDatabaseBuilder(context).build();
    }

    public static RoomDatabase.Builder<Database> getDatabaseBuilder(Context context) {
        return Room
                .databaseBuilder(context.getApplicationContext(), Database.class, DATABASE_NAME)
                .fallbackToDestructiveMigration();
    }

    public abstract MovieDao movieDao();

    public static void setInstance(Database instance) {
        sInstance = instance;
    }
}
