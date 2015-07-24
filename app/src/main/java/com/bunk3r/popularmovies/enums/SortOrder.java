package com.bunk3r.popularmovies.enums;

public enum SortOrder {

    ASCENDANT("asc"),
    DESCENDANT("desc");

    private String mOrder;

    SortOrder(String order) {
        mOrder = order;
    }

}