package com.bunk3r.popularmovies;

import android.app.Application;

import com.bunk3r.popularmovies.network.TheMovieDB;

public class MovieApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TheMovieDB.initServer(this);
    }

    @Override
    public void onTerminate() {
        TheMovieDB.destroyServer();

        super.onTerminate();
    }
}