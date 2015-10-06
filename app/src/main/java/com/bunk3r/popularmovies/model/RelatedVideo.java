package com.bunk3r.popularmovies.model;

import com.bunk3r.popularmovies.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import co.uk.rushorm.core.RushObject;
import co.uk.rushorm.core.annotations.RushTableAnnotation;

@RushTableAnnotation
public class RelatedVideo extends RushObject implements RecyclerObject {

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

    public RelatedVideo() {}

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

    public String getVideoUrl() {
        return Constants.VIDEO_YOUTUBE_URL + key;
    }

    public String getThumbnail() {
        return String.format(Locale.ENGLISH, Constants.IMG_YOUTUBE_URL, key);
    }

    @Override
    public int recyclerType() {
        return VIDEO_TYPE;
    }

    @Override
    public boolean equals(RecyclerObject recyclerObject) {
        if (recyclerType() != recyclerObject.recyclerType()) {
            return false;
        }

        RelatedVideo relatedVideo = (RelatedVideo) recyclerObject;
        return id.equals(relatedVideo.id);
    }

    @Override
    public boolean hasChanged(RecyclerObject updated) {
        if (!equals(updated)) {
            return true;
        }

        RelatedVideo relatedVideo = (RelatedVideo) updated;

        return !(key.equals(relatedVideo.key) &&
                name.equals(relatedVideo.name));
    }

}