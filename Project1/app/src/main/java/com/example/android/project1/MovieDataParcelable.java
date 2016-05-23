package com.example.android.project1;

import android.os.Parcelable;
import android.os.Parcel;

public class MovieDataParcelable implements Parcelable {

    private String title;
    private String overview;
    private Double voteAverage;
    private String releaseDate;
    private String poster;

    // No-arg Ctor
    public MovieDataParcelable(){}

    // all getters and setters go here //...

    /** Used to give additional hints on how to process the received parcel.*/
    @Override
    public int describeContents() {
// ignore for now
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pc, int flags) {
        pc.writeString(title);
        pc.writeString(overview);
        pc.writeDouble(voteAverage);
        pc.writeString(releaseDate);
        pc.writeString(poster);
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<MovieDataParcelable> CREATOR = new Parcelable.Creator<MovieDataParcelable>() {
        public MovieDataParcelable createFromParcel(Parcel pc) {
            return new MovieDataParcelable(pc);
        }
        public MovieDataParcelable[] newArray(int size) {
            return new MovieDataParcelable[size];
        }
    };

    /**Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public MovieDataParcelable(Parcel pc){
        title = pc.readString();
        overview = pc.readString();
        voteAverage = pc.readDouble();
        releaseDate = pc.readString();
        poster = pc.readString();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster() {
        return poster;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}



