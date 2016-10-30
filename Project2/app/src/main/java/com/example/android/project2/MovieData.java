package com.example.android.project2;

public class MovieData {

    private long id;
    private String title;
    private String original_title;
    private String poster;
    private String overview;
    private String release_date;
    private Double vote_average;

    public MovieData() {
        id = 0;
        title = "";
        original_title = "";
        poster = "";
        overview = "";
        release_date = "";
        vote_average = 0d;

    }

    public MovieData(long id, String title, String poster) {
        this();
        this.id = id;
        this.title = title;
        this.poster = poster;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPoster() {
        return this.poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getVoteAverage() {
        return this.vote_average;
    }


    public void setVoteAverage(Double voteAverage) {
        this.vote_average = voteAverage;
    }

    public String getReleaseDate() {
        return this.release_date;
    }

    public void setReleaseDate(String date) {
        this.release_date = date;
    }

    public String getOriginalTitle() {
        return this.original_title;
    }

    public void setOriginalTitle(String originalTitle) {
        this.original_title = originalTitle;
    }
}
