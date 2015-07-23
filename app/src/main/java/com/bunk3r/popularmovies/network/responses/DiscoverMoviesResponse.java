package com.bunk3r.popularmovies.network.responses;

import com.bunk3r.popularmovies.model.Movie;

import java.util.List;

public class DiscoverMoviesResponse {

    private int page;
    private int total_pages;
    private int total_results;
    private List<Movie> results;

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public List<Movie> getResults() {
        return results;
    }
}