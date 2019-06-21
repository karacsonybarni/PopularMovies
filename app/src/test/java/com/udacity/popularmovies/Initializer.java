package com.udacity.popularmovies;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.data.database.Database;

public class Initializer {

    public static void initDatabase(Context context) {
        Database database = Database.getDatabaseBuilder(context).allowMainThreadQueries().build();
        Database.setInstance(database);
    }

    public static void initPicasso() {
        try {
            Picasso.get();
        } catch (IllegalStateException ignored) {
            Context context = ApplicationProvider.getApplicationContext();
            Picasso.Builder builder = new Picasso.Builder(context);
            Picasso.setSingletonInstance(builder.build());
        }
    }
}
