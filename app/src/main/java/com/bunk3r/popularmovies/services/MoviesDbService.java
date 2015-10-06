package com.bunk3r.popularmovies.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.bunk3r.popularmovies.Constants;
import com.bunk3r.popularmovies.listeners.MoviesLoadedListener;
import com.bunk3r.popularmovies.model.Movie;
import com.bunk3r.popularmovies.network.TheMovieDB;
import com.bunk3r.popularmovies.network.responses.DiscoverMoviesResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import co.uk.rushorm.core.RushSearch;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MoviesDbService extends Service {

    private static final AtomicBoolean IS_RUNNING = new AtomicBoolean(false);

    public static boolean isRunning() {
        return IS_RUNNING.get();
    }

    private final IBinder mBinder = new DestinyServiceBinder();

    public int onStartCommand(Intent intent, int flags, int startId) {
        IS_RUNNING.set(true);
        return Service.START_REDELIVER_INTENT;
    }

    public void loadNextPageFavorites(final int page, final MoviesLoadedListener callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Movie> favorites = new RushSearch()
                        .whereEqual("favorite", true)
                        .limit(Constants.NUMBER_OF_MOVIES_PER_PAGE)
                        .offset((page - 1) * Constants.NUMBER_OF_MOVIES_PER_PAGE)
                        .find(Movie.class);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.moviesLoaded(favorites);
                    }
                });
            }
        }).start();
    }

    public void loadNextPage(final int page, final String sortType, final MoviesLoadedListener callback) {
        TheMovieDB.getInstance().getMovies(page, sortType, new Callback<DiscoverMoviesResponse>() {
            @Override
            public void success(final DiscoverMoviesResponse discoverMoviesResponse, Response response) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final List<Movie> loadedMovies = new ArrayList<>(discoverMoviesResponse.getResults().size());
                        for (Movie movie : discoverMoviesResponse.getResults()) {
                            Movie storedMovie = new RushSearch().whereEqual("id", movie.getId()).findSingle(Movie.class);
                            if (storedMovie == null) {
                                movie.save();
                                loadedMovies.add(movie);
                            } else {
                                storedMovie.updateInfo(movie);
                                loadedMovies.add(storedMovie);
                            }
                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.moviesLoaded(loadedMovies);
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void failure(RetrofitError error) {
                callback.loadFail(error);
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class DestinyServiceBinder extends Binder {

        public MoviesDbService getInstance() {
            return MoviesDbService.this;
        }

    }

}