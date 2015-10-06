package com.bunk3r.popularmovies.enums;

public enum SortProperty {

    POPULARITY("popularity.desc"),
    //RELEASE("release_date.desc"),
    //REVENUE("revenue.desc"),
    //TITLE("original_title.asc"),
    RATING("vote_average.desc"),
    //VOTES("vote_count.desc"),
    FAVORITES("favorites.desc");

    private String mProperty;

    SortProperty(String property) {
        mProperty = property;
    }

    public static SortProperty of(String sort) {
        for (SortProperty property : values()) {
            if (property.mProperty.equals(sort)) {
                return property;
            }
        }

        return null;
    }

    public String getPropertyValue() {
        return mProperty;
    }

}