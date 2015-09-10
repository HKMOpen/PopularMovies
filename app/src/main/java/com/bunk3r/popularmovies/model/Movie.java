package com.bunk3r.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bunk3r.popularmovies.Constants;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@SuppressWarnings("unused")
public class Movie implements Parcelable, RecyclerObject{

    @SerializedName("id")private long mId;
    @SerializedName("original_title")private String mOrgTitle;
    @SerializedName("title")private String mTitle;
    @SerializedName("overview")private String mOverview;
    @SerializedName("release_date")private Date mRelease;
    @SerializedName("original_language")private String mLanguage;
    @SerializedName("genre_ids")private int[] mGenres;

    @SerializedName("backdrop_path")private String mBackdrop;
    @SerializedName("poster_path")private String mPoster;

    @SerializedName("popularity")private float mPopularity;
    @SerializedName("vote_count")private long mVoteCnt;
    @SerializedName("vote_average")private float mVoteAvg;

    @SerializedName("video")private boolean mVideo;
    @SerializedName("adult")private boolean mAdult;

    public long getMovieId(){
        return mId;
    }

    public String getOriginalTitle() {
        return mOrgTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public Date getReleaseDate() {
        return mRelease;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public int[] getGenres() {
        return mGenres;
    }

    public String getBackdropUrl() {
        return mBackdrop;
    }

    public String getPosterUrl() {
        return Constants.IMG_BASE_URL + mPoster;
    }

    public float getPopularity() {
        return mPopularity;
    }

    public long getVoteCount() {
        return mVoteCnt;
    }

    public float getVoteAverage() {
        return mVoteAvg;
    }

    public boolean isVideo() {
        return mVideo;
    }

    public boolean isAdultVideo() {
        return mAdult;
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
        mId = in.readLong();
        mOrgTitle = in.readString();
        mTitle = in.readString();
        mOverview = in.readString();
        long releaseTime = in.readLong();
        mRelease = releaseTime >= 0 ? new Date(releaseTime) : null;
        mLanguage = in.readString();
        mGenres = in.createIntArray();

        mBackdrop = in.readString();
        mPoster = in.readString();

        mPopularity = in.readFloat();
        mVoteCnt = in.readLong();
        mVoteAvg = in.readFloat();

        mVideo = in.readByte() != 0;
        mAdult = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mOrgTitle);
        parcel.writeString(mTitle);
        parcel.writeString(mOverview);
        parcel.writeLong(mRelease != null ? mRelease.getTime() : -1);
        parcel.writeString(mLanguage);
        parcel.writeIntArray(mGenres);

        parcel.writeString(mBackdrop);
        parcel.writeString(mPoster);

        parcel.writeFloat(mPopularity);
        parcel.writeLong(mVoteCnt);
        parcel.writeFloat(mVoteAvg);

        parcel.writeByte((byte) (mVideo ? 1 : 0));
        parcel.writeByte((byte)(mAdult ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

}