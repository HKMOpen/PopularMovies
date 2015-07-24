package com.bunk3r.popularmovies.enums;

public enum SortProperty {

    POPULARITY("popularity"),
    RELEASE_DATE("release_date"),
    REVENUE("revenue"),
    PRIMARY_RELEASE_DATE("primary_release_date"),
    ORIGINAL_TITLE("original_title"),
    VOTE_AVERAGE("vote_average"),
    VOTE_COUNT("vote_count");

    private String mProperty;

    SortProperty(String property) {
        mProperty = property;
    }

}