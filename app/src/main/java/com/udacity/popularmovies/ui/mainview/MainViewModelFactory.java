package com.udacity.popularmovies.ui.mainview;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.udacity.popularmovies.data.Repository;

class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Repository repository;

    MainViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(repository);
    }
}
