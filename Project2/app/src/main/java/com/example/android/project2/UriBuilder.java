package com.example.android.project2;

import android.net.Uri;

public class UriBuilder {

    public String buildTrailerUrl(String key) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("www.youtube.com")
                .appendPath("watch")
                .appendQueryParameter("v", key);

        return builder.build().toString();
    }

    public String buildMovieUrl(long movie_id, String category, String API_KEY) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(Long.toString(movie_id))
                .appendPath(category)
                .appendQueryParameter("api_key", API_KEY);

        return builder.build().toString();
    }

    public String buildMovieCategoryList(String category, String API_KEY) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(category)
                .appendQueryParameter("api_key", API_KEY);

        return builder.build().toString();
    }

    public String buildPosterUrl(String poster) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendEncodedPath(poster);

        return builder.build().toString();
    }
}
