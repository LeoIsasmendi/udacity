package com.example.android.project2;

public class MovieReview {

    private String author;
    private String content;

    public MovieReview() {
        author = "";
        content = "";
    }


    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
