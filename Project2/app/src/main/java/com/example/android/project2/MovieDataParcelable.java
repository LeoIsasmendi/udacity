package com.example.android.project2;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieDataParcelable implements Parcelable {

    /**
     * Static field used to regenerate object, individually or as arrays
     */
    public static final Parcelable.Creator<MovieDataParcelable> CREATOR = new Parcelable.Creator<MovieDataParcelable>() {
        public MovieDataParcelable createFromParcel(Parcel pc) {
            return new MovieDataParcelable(pc);
        }

        public MovieDataParcelable[] newArray(int size) {
            return new MovieDataParcelable[size];
        }
    };
    private long id;
    private String title;
    private String originalTitle;
    private String poster;
    private String overview;
    private Double voteAverage;
    private String releaseDate;

    // No-arg Ctor
    public MovieDataParcelable() {
    }

    /**
     * Ctor from Parcel, reads back fields IN THE ORDER they were written
     */
    public MovieDataParcelable(Parcel pc) {
        id = pc.readLong();
        title = pc.readString();
        originalTitle = pc.readString();
        poster = pc.readString();
        overview = pc.readString();
        voteAverage = pc.readDouble();
        releaseDate = pc.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pc, int flags) {
        pc.writeLong(id);
        pc.writeString(title);
        pc.writeString(originalTitle);
        pc.writeString(poster);
        pc.writeString(overview);
        pc.writeDouble(voteAverage);
        pc.writeString(releaseDate);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }
}