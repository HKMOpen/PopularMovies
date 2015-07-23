package com.bunk3r.popularmovies.utils;

public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("Not meant to be initialized");
    }

    public static boolean isEmpty(String string) {
        return string == null || "".equals(string.trim());
    }

}
