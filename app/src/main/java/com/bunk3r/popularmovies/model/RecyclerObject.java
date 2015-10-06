package com.bunk3r.popularmovies.model;

public interface RecyclerObject {

    int MOVIE_TYPE = 1;
    int VIDEO_TYPE = 2;
    int REVIEW_TYPE = 3;

    int recyclerType();

    boolean equals(RecyclerObject recyclerObject);

    boolean hasChanged(RecyclerObject updated);

}