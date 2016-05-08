package com.example.android.project1;

import android.content.Context;
import android.graphics.Movie;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ImageAdapter extends BaseAdapter {

    // JSON Node names
    private static final String TAG_RESULTS = "results";
    private static final String POSTER_PATH = "poster_path";


    private Context mContext;

    // references to our images
    private MovieData[] mThumbMovies;

    public ImageAdapter(Context c) {
        mContext = c;
        loadImages();
    }

    public int getCount() {
        int length = 6;
        //mThumbMovies.length
        return length;
    }

    public Object getItem(int position) {
        try {
            return mThumbMovies[position];
        } catch ( NullPointerException e ) {
            e.printStackTrace();
            return null;
        }
    }

    public long getItemId(int position) {
        try {
            return mThumbMovies[position].getId();
        } catch ( NullPointerException e ) {
            e.printStackTrace();
            return 0;
        }
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

//        Picasso.with(mContext).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        //Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + mThumbMovies[position]).into(imageView);
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + mThumbMovies[position].getPoster()).into(imageView);
        return imageView;
    }

    public void loadImages() {
        String baseUrl = "http://image.tmdb.org/t/p/";
        String size = "w185";

        String example = "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
        String API_KEY = "78b685ac6e5ae6acfc568eb40c78b1f9";
//        http://api.themoviedb.org/3/movie/popular?api_key=[API_KEY]
//        http://api.themoviedb.org/3/movie/popular?api_key=78b685ac6e5ae6acfc568eb40c78b1f9


        String url = "http://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;

        RequestQueue queue = Volley.newRequestQueue(mContext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        parseMovieData(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });


        // Access the RequestQueue through your singleton class.
        queue.add(jsObjRequest);
    }

    private void parseMovieData(JSONObject response) {

        try {

            JSONArray results = response.getJSONArray(TAG_RESULTS);

            this.mThumbMovies = new MovieData[results.length()];
            for (int i = 0, size = results.length()-1; i < size; i++)
            {
                JSONObject objectInArray = results.getJSONObject(i);

                //mThumbMovies[i] = objectInArray.getString(POSTER_PATH);
                MovieData movie = new MovieData();
                movie.loadData(objectInArray);
                mThumbMovies[i] = movie;

            }

//            Log.i("LEO String Array", mThumbMovies.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
