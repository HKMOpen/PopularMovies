package com.bunk3r.popularmovies;

import android.app.Application;

import com.bunk3r.popularmovies.network.TheMovieDB;

import co.uk.rushorm.android.AndroidInitializeConfig;
import co.uk.rushorm.core.RushCore;

public class MovieApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidInitializeConfig config = new AndroidInitializeConfig(getApplicationContext());
        RushCore.initialize(config);
        TheMovieDB.initServer(this);
    }

    @Override
    public void onTerminate() {
        TheMovieDB.destroyServer();

        super.onTerminate();
    }
}