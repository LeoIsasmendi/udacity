package com.example.jokeslibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeLibraryActivity extends AppCompatActivity {

    static final String EXTRA_JOKE = "com.example.jokeslibrary.EXTRA_JOKE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_library);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setText(extras.getString("EXTRA_JOKE"));
        }

    }

    public void setText(String text) {

        TextView t = (TextView) findViewById(R.id.joke_text);
        t.setText(text);
        
    }
}
