package com.example.android.project1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setTitle(extras.getString("EXTRA_MOVIE_TITLE"));
            setSinopsis(extras.getString("EXTRA_MOVIE_OVERVIEW"));
/*            String title = extras.getString("EXTRA_MOVIE_TITLE");
            String overview = extras.getString("EXTRA_MOVIE_OVERVIEW");
            Double voteAverage = extras.getDouble("EXTRA_MOVIE_VOTE_AVERAGE");
            Log.i("LEO",String.format("DetailsActivity TITLE = %s ; OVERVIEW = %s ; VOTE = %3$.2f", title, overview, voteAverage));*/
        }
    }

    private void setTitle(String text) {
        TextView title = (TextView)findViewById(R.id.original_title);
        title.setText(text);
    }

    private void setSinopsis(String text) {
        TextView sinopsis = (TextView)findViewById(R.id.sinopsis);
        sinopsis.setText(text);
    }

}
