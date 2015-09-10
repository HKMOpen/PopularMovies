package com.bunk3r.popularmovies.network.apis;

import com.bunk3r.popularmovies.network.responses.DiscoverMoviesResponse;
import com.bunk3r.popularmovies.network.responses.RelatedVideosResponse;
import com.bunk3r.popularmovies.network.responses.ReviewsResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface TheMovieDbApi {

    @GET("/discover/movie")
    void discoverMovies(@Query("page") int pageId, @Query("sort_by") String sortBy, Callback<DiscoverMoviesResponse> callback);

    @GET("/movie/{id}/videos")
    void relatedVideos(@Path("id") long id, Callback<RelatedVideosResponse> callback);

    @GET("/movie/{id}/reviews")
    void reviews(@Query("page") int pageId, @Path("id") long id, Callback<ReviewsResponse> callback);

}