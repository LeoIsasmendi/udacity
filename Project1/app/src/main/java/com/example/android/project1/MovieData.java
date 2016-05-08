package com.example.android.project1;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieData {
    private long id;
    private String title;
    private String original_title;
    private String poster;
    private String overview;
    private Float vote_average;

    private String TAG_TITLE = "title";
    private String TAG_POSTER = "poster_path";
    private String TAG_ID = "id";


    public void loadData(JSONObject obj) {
        try {

            this.id = obj.getLong(TAG_ID);
            this.poster = obj.getString(TAG_POSTER);
            this.title = obj.getString(TAG_TITLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return this.title;
    }

    public long getId() {
        return this.id;
    }

    public String getPoster() {
        return this.poster;
    }


}
