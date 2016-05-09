package com.example.android.project1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {

    // JSON Node names
    private static final String TAG_RESULTS = "results";

    private static final String TAG_TITLE = "title";
    private static final String TAG_POSTER = "poster_path";
    private static final String TAG_ID = "id";

    public MovieData parseMovie(JSONObject obj) {
        try {

            MovieData movie = new MovieData(obj.getLong(TAG_ID), obj.getString(TAG_POSTER), obj.getString(TAG_TITLE));
            return movie;

        } catch (JSONException e) {

            e.printStackTrace();
            return new MovieData();

        }
    }

    public JSONArray parsePage(JSONObject obj) {
        try {

            return obj.getJSONArray(TAG_RESULTS);

        } catch (JSONException e) {

            e.printStackTrace();
            return new JSONArray();

        }
    }

}
