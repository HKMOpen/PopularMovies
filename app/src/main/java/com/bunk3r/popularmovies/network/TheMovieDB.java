package com.bunk3r.popularmovies.network;

import android.content.Context;

import com.bunk3r.popularmovies.R;
import com.bunk3r.popularmovies.network.apis.TheMovieDbApi;
import com.bunk3r.popularmovies.network.responses.DiscoverMoviesResponse;
import com.bunk3r.popularmovies.network.responses.RelatedVideosResponse;
import com.bunk3r.popularmovies.network.responses.ReviewsResponse;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

public class TheMovieDB {

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String HEADER_API_KEY_NAME = "api_key";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String DEFAULT_CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final Converter GSON_PARSER = new GsonConverter(new GsonBuilder()
            .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                @Override
                public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                        throws JsonParseException {
                    try {
                        return df.parse(json.getAsString());
                    } catch (ParseException e) {
                        return null;
                    }
                }
            })
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
                request.addQueryParam(HEADER_CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
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

    public void getRelatedVideos(long movieId, Callback<RelatedVideosResponse> callback) {
        TheMovieDbApi theMovieDb = getRestAdapter().create(TheMovieDbApi.class);
        theMovieDb.relatedVideos(movieId, callback);
    }

    public void getReviews(int pageId, long movieId, Callback<ReviewsResponse> callback) {
        TheMovieDbApi theMovieDb = getRestAdapter().create(TheMovieDbApi.class);
        theMovieDb.reviews(pageId, movieId, callback);
    }

}