package com.bunk3r.popularmovies.model;

import com.bunk3r.popularmovies.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class RelatedVideo implements RecyclerObject {

    @SerializedName("id")
    private String id;

    @SerializedName("iso_639_1")
    private String language;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    @SerializedName("size")
    private int size;

    @SerializedName("type")
    private String type;

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public String getThumbnail() {
        return String.format(Locale.ENGLISH, Constants.IMG_YOUTUBE_URL, key);
    }

    @Override
    public int recyclerType() {
        return VIDEO_TYPE;
    }

}