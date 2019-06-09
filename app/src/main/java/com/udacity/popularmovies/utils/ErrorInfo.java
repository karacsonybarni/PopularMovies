package com.udacity.popularmovies.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.udacity.popularmovies.R;

public class ErrorInfo {

    public static void showNoInternetSnackbar(View view, View.OnClickListener action) {
        Snackbar
                .make(view, R.string.no_internet_error, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, action)
                .show();
    }
}
