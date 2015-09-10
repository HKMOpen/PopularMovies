package com.bunk3r.popularmovies.network.responses;

import com.bunk3r.popularmovies.model.RelatedVideo;

import java.util.List;

public class RelatedVideosResponse {

    private long id;
    private List<RelatedVideo> results;

    public long getId() {
        return id;
    }

    public List<RelatedVideo> getResults() {
        return results;
    }

}