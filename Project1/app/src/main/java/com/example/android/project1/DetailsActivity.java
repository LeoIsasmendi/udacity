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
        MovieDataParcelable extras = getIntent().getParcelableExtra("data");
        if (extras != null) {
            setTitle(extras.getTitle());
            setSynopsis(extras.getOverview());
            setPoster(extras.getPoster());
            setUserRating(extras.getVoteAverage());
            setReleaseDate(extras.getReleaseDate());
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
        rating.setText(String.format("Rating: %1$.2f / 10", vote ));
    }

    private void setReleaseDate(String text) {
        TextView date = (TextView)findViewById(R.id.released);
        date.setText("Released: "+text);
    }

}
