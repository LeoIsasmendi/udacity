package com.example.android.project1;

public class MovieData {

    private long id;
    private String title;
    private String original_title;
    private String poster;
    private String overview;
    private Float vote_average;

    public MovieData() {
        id = 0;
        title = "";
        original_title = "";
        poster = "";
        overview = "";
        vote_average = 0f;
    }

    public MovieData (long id, String title, String poster ) {
        this.id = id;
        this.title = title;
        this.poster = poster;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPoster() {
        return this.poster;
    }

    public String getOriginal_title() { return this.original_title; }

    public String getOverview() { return this.overview; }

    public Float getVote_average() { return  this.vote_average; }
}
