package com.example.android.project2;

public class MovieTrailer {

    private String id;
    private String name;
    private String key;

    public MovieTrailer() {
        id = "";
        name = "";
        key = "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
