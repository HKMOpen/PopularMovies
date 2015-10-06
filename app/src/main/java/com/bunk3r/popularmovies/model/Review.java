package com.bunk3r.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import co.uk.rushorm.core.RushObject;
import co.uk.rushorm.core.annotations.RushTableAnnotation;

@RushTableAnnotation
public class Review extends RushObject implements RecyclerObject {

    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

    public Review() {}

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int recyclerType() {
        return REVIEW_TYPE;
    }

    @Override
    public boolean equals(RecyclerObject recyclerObject) {
        if (recyclerType() != recyclerObject.recyclerType()) {
            return false;
        }

        Review review = (Review) recyclerObject;
        return id.equals(review.id);
    }

    @Override
    public boolean hasChanged(RecyclerObject updated) {
        if (equals(updated)) {
            return true;
        }

        Review review = (Review) updated;
        return !(author.equals(review.author) &&
                content.equals(review.content) &&
                url.equals(review.url));
    }
}