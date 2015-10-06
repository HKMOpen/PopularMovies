package com.bunk3r.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bunk3r.popularmovies.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import co.uk.rushorm.core.RushObject;
import co.uk.rushorm.core.annotations.RushList;
import co.uk.rushorm.core.annotations.RushTableAnnotation;

@SuppressWarnings("unused")
@RushTableAnnotation
public class Movie extends RushObject implements Parcelable, RecyclerObject {

    @SerializedName("id")
    private long id;
    @SerializedName("original_title")
    private String orgTitle;
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private Date release;
    @SerializedName("original_language")
    private String language;
    @SerializedName("genre_ids")
    private int[] genres;

    @SerializedName("backdrop_path")
    private String backdrop;
    @SerializedName("poster_path")
    private String poster;

    @SerializedName("popularity")
    private float popularity;
    @SerializedName("vote_count")
    private long voteCnt;
    @SerializedName("vote_average")
    private float voteAvg;

    @SerializedName("video")
    private boolean video;
    @SerializedName("adult")
    private boolean adult;

    private boolean favorite;
    @RushList(classType = Review.class)
    private List<Review> reviews;
    @RushList(classType = RelatedVideo.class)
    private List<RelatedVideo> relatedVideos;

    public Movie() {
    }

    public void updateInfo(Movie movie) {
        orgTitle = movie.orgTitle;
        title = movie.title;
        overview = movie.overview;
        release = movie.release;
        language = movie.language;
        genres = movie.genres;
        backdrop = movie.backdrop;
        poster = movie.poster;
        popularity = movie.popularity;
        voteCnt = movie.voteCnt;
        voteAvg = movie.voteAvg;
        video = movie.video;
        adult = movie.adult;
    }

    public long getMovieId(){
        return id;
    }

    public String getOriginalTitle() {
        return orgTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Date getReleaseDate() {
        return release;
    }

    public String getLanguage() {
        return language;
    }

    public int[] getGenres() {
        return genres;
    }

    public String getBackdropUrl() {
        return backdrop;
    }

    public String getPosterUrl() {
        return Constants.IMG_BASE_URL + poster;
    }

    public float getPopularity() {
        return popularity;
    }

    public long getVoteCount() {
        return voteCnt;
    }

    public float getVoteAverage() {
        return voteAvg;
    }

    public boolean isVideo() {
        return video;
    }

    public boolean isAdultVideo() {
        return adult;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setRelatedVideos(List<RelatedVideo> relatedVideos) {
        this.relatedVideos = relatedVideos;
    }

    public List<RelatedVideo> getRelatedVideos() {
        return relatedVideos;
    }

    @Override
    public int recyclerType() {
        return MOVIE_TYPE;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        id = in.readLong();
        orgTitle = in.readString();
        title = in.readString();
        overview = in.readString();
        long releaseTime = in.readLong();
        release = releaseTime >= 0 ? new Date(releaseTime) : null;
        language = in.readString();
        genres = in.createIntArray();

        backdrop = in.readString();
        poster = in.readString();

        popularity = in.readFloat();
        voteCnt = in.readLong();
        voteAvg = in.readFloat();

        video = in.readByte() != 0;
        adult = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(orgTitle);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeLong(release != null ? release.getTime() : -1);
        parcel.writeString(language);
        parcel.writeIntArray(genres);

        parcel.writeString(backdrop);
        parcel.writeString(poster);

        parcel.writeFloat(popularity);
        parcel.writeLong(voteCnt);
        parcel.writeFloat(voteAvg);

        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeByte((byte) (adult ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(RecyclerObject recyclerObject) {
        if (recyclerType() != recyclerObject.recyclerType()) {
            return false;
        }

        Movie movie = (Movie) recyclerObject;
        return id == movie.id;
    }

    @Override
    public boolean hasChanged(RecyclerObject updated) {
        if (!equals(updated)) {
            return true;
        }

        Movie movie = (Movie) updated;
        return !(overview.equals(movie.overview) &&
                backdrop.equals(movie.backdrop) &&
                poster.equals(movie.poster) &&
                popularity == popularity &&
                voteCnt == movie.voteCnt &&
                voteAvg == movie.voteAvg);
    }
}