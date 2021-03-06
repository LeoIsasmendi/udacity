package com.example.android.project1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {

    // JSON Node names
    private static final String TAG_RESULTS = "results";

    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_POSTER = "poster_path";
    private static final String TAG_OVERVIEW = "overview";
    private static final String TAG_VOTE_AVERAGE = "vote_average";
    private static final String TAG_RELEASE_DATE = "release_date";


    public MovieData parseMovie(JSONObject obj) {
        MovieData movie = new MovieData();
        movie.setId(this.parseId(obj));
        movie.setPoster(this.parsePoster(obj));
        movie.setTitle(this.parseTitle(obj));
        movie.setOverview(this.parseOverview(obj));
        movie.setVoteAverage(this.parseVoteAverage(obj));
        movie.setReleaseDate(this.parseReleaseDate(obj));
        return movie;
    }

    private long parseId(JSONObject obj) {
        long id;
        try {
            id = obj.getLong(TAG_ID);
            return id;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String parseString(String TAG, JSONObject obj) {
        String result;
        try {
            result = obj.getString(TAG);
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String parsePoster(JSONObject obj) {
        return parseString(TAG_POSTER, obj);
    }

    private String parseTitle(JSONObject obj) {
        return parseString(TAG_TITLE, obj);
    }

    private String parseOverview(JSONObject obj) {
        return parseString(TAG_OVERVIEW, obj);
    }

    private Double parseVoteAverage(JSONObject obj) {
        Double vote;
        try {
            vote = obj.getDouble(TAG_VOTE_AVERAGE);
            return vote;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0d;
        }
    }

    private String parseReleaseDate(JSONObject obj) {
        return parseString(TAG_RELEASE_DATE, obj);
    }


    public JSONArray parsePage(JSONObject obj) {
        try {

            return obj.getJSONArray(TAG_RESULTS);

        } catch (JSONException e) {

            e.printStackTrace();
            return null;

        }
    }

    //temporal must be parsePage
    public MovieData[] parseMoviesData(JSONObject response) {

        try {

            JSONArray results = this.parsePage(response);

            MovieData[] moviesList = new MovieData[results.length()];
            for (int i = 0, size = results.length(); i < size; i++)
            {
                JSONObject objectInArray = results.getJSONObject(i);
                moviesList[i] = this.parseMovie(objectInArray);

            }
            return moviesList;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
