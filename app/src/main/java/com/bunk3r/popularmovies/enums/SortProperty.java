package com.bunk3r.popularmovies.enums;

public enum SortProperty {

    POPULARITY("popularity.desc"),
    //RELEASE("release_date.desc"),
    //REVENUE("revenue.desc"),
    //TITLE("original_title.asc"),
    RATING("vote_average.desc");
    //VOTES("vote_count.desc");

    private String mProperty;

    SortProperty(String property) {
        mProperty = property;
    }

    public String getPropertyValue() {
        return mProperty;
    }

}