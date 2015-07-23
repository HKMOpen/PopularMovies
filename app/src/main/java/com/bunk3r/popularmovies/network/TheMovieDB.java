package com.bunk3r.popularmovies.network;

import android.content.Context;

import com.bunk3r.popularmovies.R;
import com.bunk3r.popularmovies.network.apis.TheMovieDbApi;
import com.bunk3r.popularmovies.network.responses.DiscoverMoviesResponse;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

public class TheMovieDB {

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String HEADER_API_KEY_NAME = "api_key";
    private static final Converter GSON_PARSER = new GsonConverter(new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create());

    private static TheMovieDB mInstance;

    private final String mApiKey;

    private TheMovieDB(Context context) {
        mApiKey = context.getString(R.string.the_movie_db_api_key);
    }

    public static void initServer(Context context) {
        mInstance = new TheMovieDB(context);
    }

    public static void destroyServer() {
        mInstance = null;
    }

    public static TheMovieDB getInstance() {
        return mInstance;
    }

    private RestAdapter getRestAdapter() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addQueryParam(HEADER_API_KEY_NAME, mApiKey);
            }
        };

        return new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setRequestInterceptor(requestInterceptor)
                .setConverter(GSON_PARSER)
                .build();
    }

    public void getMovies(int pageId, String sortBy, Callback<DiscoverMoviesResponse> callback) {
        TheMovieDbApi theMovieDb = getRestAdapter().create(TheMovieDbApi.class);
        theMovieDb.discoverMovies(pageId, sortBy, callback);
    }

}