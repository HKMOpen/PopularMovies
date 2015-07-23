package com.bunk3r.popularmovies.network.apis;

import com.bunk3r.popularmovies.network.responses.DiscoverMoviesResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface TheMovieDbApi {

    @GET("/discover/movie")
    void discoverMovies(@Query("page") int pageId, @Query("sort_by") String sortBy, Callback<DiscoverMoviesResponse> callback);
}
