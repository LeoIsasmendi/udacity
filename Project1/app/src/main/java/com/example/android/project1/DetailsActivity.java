package com.example.android.project1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setTitle(extras.getString("EXTRA_MOVIE_TITLE"));
            setSynopsis(extras.getString("EXTRA_MOVIE_OVERVIEW"));
            setPoster(extras.getString("EXTRA_MOVIE_POSTER"));
            setUserRating(extras.getDouble("EXTRA_MOVIE_VOTE_AVERAGE"));
            setReleaseDate(extras.getString("EXTRA_MOVIE_RELEASE_DATE"));
        }
    }

    private void setTitle(String text) {
        TextView title = (TextView)findViewById(R.id.original_title);
        title.setText(text);
    }

    private void setSynopsis(String text) {
        TextView synopsis = (TextView)findViewById(R.id.synopsis);
        synopsis.setText(text);
    }

    private void setPoster(String text) {
        ImageView imageView = (ImageView)findViewById(R.id.poster);
        Picasso.with(getBaseContext()).load(text).into(imageView);
    }

    private void setUserRating(Double vote) {
        TextView rating = (TextView)findViewById(R.id.rating);
        rating.setText(String.format(" %1$.2f / 10", vote ));
    }

    private void setReleaseDate(String text) {
        TextView date = (TextView)findViewById(R.id.released);
        date.setText(text);
    }

}
