package com.example.android.project1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        parser = new JSONParser();
        API_KEY = "78b685ac6e5ae6acfc568eb40c78b1f9";
    }

    private AdapterView.OnItemClickListener itemClickListener () {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                MovieData movie = (MovieData)parent.getItemAtPosition(position);

                // in CollectDataActivity, populate the Parcelable User object using its setter methods

                MovieDataParcelable data = new MovieDataParcelable();
                data.setTitle(movie.getTitle());
                data.setOverview(movie.getOverview());
                data.setVoteAverage(movie.getVoteAverage());
                data.setReleaseDate(movie.getReleaseDate());
                data.setPoster(movie.getPoster());

                // pass it to another component
                intent.putExtra("data", data);
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
                savePreferences(getString(R.string.app_preferences_op1));
                loadData();
                return true;
            case R.id.users_movies:
                savePreferences(getString(R.string.app_preferences_op2));
                loadData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void savePreferences(String string) {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.app_preferences_file), MODE_PRIVATE).edit();
        editor.putString("saved_selected_category", string);
        editor.commit();
    }

    public String getSavedPreferences() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.app_preferences_file), MODE_PRIVATE);
        String defaultValue = getString(R.string.saved_default_category);
        String savedPreferences = prefs.getString("saved_selected_category", defaultValue);
        return savedPreferences;
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
        loadData();
    }

    @Override
    protected void onPause() {
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridState = gridview.onSaveInstanceState();
        super.onPause();
    }

    private void loadData() {
        loadData(getSavedPreferences());
    }
    private void loadData(String category) {
        String url = "http://api.themoviedb.org/3/movie/"+category+"?api_key="+API_KEY;
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