package com.example.android.project2;

import android.database.Cursor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;


public class MainActivity extends AppCompatActivity implements MovieListFragment.OnListFragmentInteractionListener, DetailsFragment.OnFragmentInteractionListener {

    private final String API_KEY = "";
    private final String TAG = "MainActivity";
    //    private Parcelable gridState;
    private DataParser parser;
    private Bundle movieBundle;
    private FavoritesDB favoritesDB;
    private UriBuilder uriBuilder;

    //Fragments
    private DetailsFragment detailFragment = new DetailsFragment();
    private MovieListFragment movieListFragment = new MovieListFragment();

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parser = new DataParser();
        uriBuilder = new UriBuilder();
        fragmentManager = getSupportFragmentManager();
        favoritesDB = new FavoritesDB(this);

        FragmentTransaction ft = fragmentManager.beginTransaction();

        Log.i(TAG, "onCreate: twoPaneMode " + getResources().getBoolean(R.bool.twoPaneMode));
        if (getResources().getBoolean(R.bool.twoPaneMode)) {
            ft.add(R.id.movies_container, movieListFragment);
            ft.add(R.id.details_container, detailFragment);
        } else {
            ft.add(R.id.main_container, movieListFragment);
        }

        ft.commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.popular_movies:
                setPreferences(getString(R.string.app_preferences_op1));
                loadData(getPreferences());
                return true;
            case R.id.users_movies:
                setPreferences(getString(R.string.app_preferences_op2));
                loadData(getPreferences());
                return true;
            case R.id.favorites_movies:
                setPreferences(getString(R.string.app_preferences_op3));
                loadData(getPreferences());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getPreferences() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.app_preferences_file), MODE_PRIVATE);
        String defaultValue = getString(R.string.saved_default_category);
        String savedPreferences = prefs.getString("saved_selected_category", defaultValue);
        return savedPreferences;
    }

    private void setPreferences(String string) {
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.app_preferences_file), MODE_PRIVATE).edit();
        editor.putString("saved_selected_category", string);
        editor.commit();
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
        loadData(getPreferences());
    }

    @Override
    protected void onStop() {
        favoritesDB.close();
        super.onStop();
    }

    private MovieListFragment getMovieListFragment() {

        if (getResources().getBoolean(R.bool.twoPaneMode)) {
            return (MovieListFragment) fragmentManager.findFragmentById(R.id.movies_container);
        } else {
            return (MovieListFragment) fragmentManager.findFragmentById(R.id.main_container);
        }

    }

    private DetailsFragment getDetailsFragment() {

        if (getResources().getBoolean(R.bool.twoPaneMode)) {
            return (DetailsFragment) fragmentManager.findFragmentById(R.id.details_container);
        } else {
            return (DetailsFragment) fragmentManager.findFragmentById(R.id.main_container);
        }

    }

    private void loadData(String category) {

        closeDetails();

        if (category == getResources().getString(R.string.app_preferences_op3)) {

            Cursor mCursor = favoritesDB.getFavorites();
            getMovieListFragment().setMovieList(parser.parseFavorites(mCursor));

        } else {

            String url = uriBuilder.buildMovieCategoryList(category, API_KEY);
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
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            // Access the RequestQueue through your singleton class.
            queue.add(jsObjRequest);

        }

    }

    private void closeDetails() {
        if (this.detailFragment.isVisible()) {
            fragmentManager.popBackStack();
        }
    }

    private void processingResponse(JSONObject response) {
        getMovieListFragment().setMovieList(parser.parseMoviesData(response));
    }


    private void loadReviews(long movieId) {
        String url = uriBuilder.buildMovieUrl(movieId, "reviews", API_KEY);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        parseReviews(response);
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

    private void parseReviews(JSONObject response) {
        MovieReview[] list = parser.parseMovieReviews(response);
        if (list.length > 0) {
            getDetailsFragment().setReviews(list);
        }
    }


    // TRAILERS
    private void loadTrailers(long movieId) {

        String url = uriBuilder.buildMovieUrl(movieId, "videos", API_KEY);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        parseTrailers(response);
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

    private void parseTrailers(JSONObject response) {

        MovieTrailer[] list = parser.parseMovieTrailers(response);
        if (list.length > 0) {
            getDetailsFragment().setTrailers(list);
        }

    }

    /* Fragments implementations */
    public void onListFragmentInteraction(MovieData movie) {

        detailFragment = new DetailsFragment();

        MovieDataParcelable data = new MovieDataParcelable();
        data.setId(movie.getId());
        data.setTitle(movie.getTitle());
        data.setOverview(movie.getOverview());
        data.setOriginalTitle(movie.getOriginalTitle());
        data.setVoteAverage(movie.getVoteAverage());
        data.setReleaseDate(movie.getReleaseDate());
        data.setPoster(movie.getPoster());

        movieBundle = new Bundle();
        movieBundle.putParcelable("movie", data);
        detailFragment.setArguments(movieBundle);

        loadReviews(movie.getId());
        loadTrailers(movie.getId());

        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (getResources().getBoolean(R.bool.twoPaneMode)) {
            ft.replace(R.id.details_container, detailFragment);
        } else {
            ft.replace(R.id.main_container, detailFragment);
            ft.addToBackStack(null);
        }
        ft.commit();

    }

    public void onToggleMovieAsFavorite(MovieDataParcelable movie, Boolean isFav) {
        if (isFav) {
            Log.i("DetailsActivity", "addToFavorites");
            favoritesDB.insertToFavorites(movie);
        } else {
            Log.i("DetailsActivity", "removeFromFavorites");
            favoritesDB.removeFromFavorites(movie.getId());
        }
    }

    public void onShowTrailer(MovieTrailer trailer) {
        Intent trailerPlayer = new Intent(Intent.ACTION_VIEW, Uri.parse(uriBuilder.buildTrailerUrl(trailer.getKey())));
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(trailerPlayer, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            startActivity(trailerPlayer);
        } else {
            Toast.makeText(getApplicationContext(), R.string.error_trailers, Toast.LENGTH_LONG).show();
        }
    }


    public Boolean isFavorite(MovieDataParcelable movie) {
        return favoritesDB.checkMovie(movie.getId());
    }
}