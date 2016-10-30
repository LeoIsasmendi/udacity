package com.example.android.project2;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataParser {

    // JSON Node names
    private static final String TAG_RESULTS = "results";

    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_ORIGINAL_TITLE = "original_title";
    private static final String TAG_POSTER = "poster_path";
    private static final String TAG_OVERVIEW = "overview";
    private static final String TAG_VOTE_AVERAGE = "vote_average";
    private static final String TAG_RELEASE_DATE = "release_date";

    // REVIEWS
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_CONTENT = "content";

    //TRAILERS
    private static final String TAG_TRAILER_ID = "id";
    private static final String TAG_TRAILER_NAME = "name" ;
    private static final String TAG_TRAILER_KEY = "key";

    public MovieData parseMovie(JSONObject obj) {
        MovieData movie = new MovieData();
        movie.setId(this.parseId(obj));
        movie.setPoster(this.parsePoster(obj));
        movie.setTitle(this.parseTitle(obj));
        movie.setOriginalTitle(this.parseOriginalTitle(obj));
        movie.setOverview(this.parseOverview(obj));
        movie.setVoteAverage(this.parseVoteAverage(obj));
        movie.setReleaseDate(this.parseReleaseDate(obj));
        return movie;
    }

    public MovieData parseMovie(Cursor obj) {
        MovieData movie = new MovieData();
        movie.setId(obj.getLong(1));
        movie.setTitle(obj.getString(2));
        movie.setOriginalTitle(obj.getString(3));
        movie.setPoster(obj.getString(4));
        movie.setOverview(obj.getString(5));
        movie.setReleaseDate(obj.getString(6));
        movie.setVoteAverage(obj.getDouble(7));
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

    private String parseOriginalTitle(JSONObject obj) {
        return parseString(TAG_ORIGINAL_TITLE, obj);
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

    public MovieReview[] parseMovieReviews(JSONObject response) {
        try {

            JSONArray results = this.parsePage(response);
            MovieReview[] list = new MovieReview[results.length()];
            for (int i = 0, size = results.length(); i < size; i++)
            {
                JSONObject objectInArray = results.getJSONObject(i);
                list[i] = this.parseReview(objectInArray);

            }
            return list;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MovieReview parseReview(JSONObject obj) {
        MovieReview movie = new MovieReview();
        movie.setAuthor(this.parseAuthor(obj));
        movie.setContent(this.parseContent(obj));
        return movie;
    }

    private String parseAuthor(JSONObject obj) {
        return parseString(TAG_AUTHOR, obj);
    }

    private String parseContent(JSONObject obj) {
        return parseString(TAG_CONTENT, obj);
    }

    public MovieTrailer[] parseMovieTrailers(JSONObject response) {
        try {

            JSONArray results = this.parsePage(response);
            MovieTrailer[] list = new MovieTrailer[results.length()];
            for (int i = 0, size = results.length(); i < size; i++)
            {
                JSONObject objectInArray = results.getJSONObject(i);
                list[i] = this.parseTrailer(objectInArray);

            }
            return list;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MovieTrailer parseTrailer(JSONObject obj) {
        MovieTrailer movie = new MovieTrailer();
        movie.setId(this.parseTrailerId(obj));
        movie.setName(this.parseName(obj));
        movie.setKey(this.parseKey(obj));
        return movie;
    }

    private String parseTrailerId(JSONObject obj) {
        return parseString(TAG_TRAILER_ID, obj);
    }

    private String parseName(JSONObject obj) {
        return parseString(TAG_TRAILER_NAME, obj);
    }

    private String parseKey(JSONObject obj) {
        return parseString(TAG_TRAILER_KEY, obj);
    }

    public MovieData[] parseFavorites(Cursor mCursor) {
        MovieData[] moviesList = new MovieData[mCursor.getCount()];
        int i = 0;
        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            // The Cursor is now set to the right position
            moviesList[i] = this.parseMovie(mCursor);
            i++;
        }

        return moviesList;
    }
}
