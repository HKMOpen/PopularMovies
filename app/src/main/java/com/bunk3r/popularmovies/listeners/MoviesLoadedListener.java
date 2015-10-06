package com.bunk3r.popularmovies.listeners;

import com.bunk3r.popularmovies.model.Movie;

import java.util.List;

import retrofit.RetrofitError;

public interface MoviesLoadedListener {

    void moviesLoaded(List<Movie> movies);

    void loadFail(RetrofitError error);

}