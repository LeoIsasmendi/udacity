package com.example.android.project1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private JSONParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.parser = new JSONParser();
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

    private void loadPopular() {
        // TODO: load popular movies

//        String example = "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
        String API_KEY = "78b685ac6e5ae6acfc568eb40c78b1f9";
//        http://api.themoviedb.org/3/movie/popular?api_key=78b685ac6e5ae6acfc568eb40c78b1f9


        String url = "http://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        processingPopular(response);
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

    private void processingPopular(JSONObject response) {
        ImageAdapter adapter = new ImageAdapter(this, parser.parseMoviesData(response));
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(itemClickListener());
    }

    private void loadUsersRatings() {
        // TODO: load users ratings
    }
}