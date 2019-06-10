package com.udacity.popularmovies.ui.detailview;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.udacity.popularmovies.data.Repository;

class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Repository repository;

    DetailViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailActivityViewModel(repository);
    }
}
