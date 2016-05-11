package com.example.android.project1;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Parcelable gridState;
    private JSONParser parser;
    private String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.parser = new JSONParser();
        API_KEY = "78b685ac6e5ae6acfc568eb40c78b1f9";
//example:        http://api.themoviedb.org/3/movie/popular?api_key=78b685ac6e5ae6acfc568eb40c78b1f9
    }

    private AdapterView.OnItemClickListener itemClickListener () {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                MovieData movie = (MovieData)parent.getItemAtPosition(position);

                intent.putExtra("EXTRA_MOVIE_TITLE", movie.getTitle());
                intent.putExtra("EXTRA_MOVIE_OVERVIEW", movie.getOverview());
                intent.putExtra("EXTRA_MOVIE_VOTE_AVERAGE", movie.getVoteAverage());
                intent.putExtra("EXTRA_MOVIE_RELEASE_DATE", movie.getReleaseDate());
                intent.putExtra("EXTRA_MOVIE_POSTER", movie.getPoster());

                startActivity(intent);
            }
        };
        return listener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.popular_movies:
                loadPopular();
                return true;
            case R.id.users_movies:
                loadUsersRatings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPopular();
    }

    @Override
    protected void onPause() {
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridState = gridview.onSaveInstanceState();
        super.onPause();
    }

    private void loadPopular() {
        String url = "http://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        processingResponse(response);
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

    private void loadUsersRatings() {
        String url = "http://api.themoviedb.org/3/movie/top_rated?api_key="+API_KEY;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        processingResponse(response);
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

    private void processingResponse(JSONObject response) {
        ImageAdapter adapter = new ImageAdapter(this, parser.parseMoviesData(response));
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
        // Restore previous state (including selected item index and scroll position)
        if(gridState != null) {
            gridview.onRestoreInstanceState(gridState);
        }
        gridview.setOnItemClickListener(itemClickListener());
    }
}