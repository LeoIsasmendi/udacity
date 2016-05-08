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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(itemClickListener());

    }

    private AdapterView.OnItemClickListener itemClickListener () {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + id,
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);

                // TODO: send movie object or params
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
    }

    private void loadUsersRatings() {
        // TODO: load users ratings
    }
}
