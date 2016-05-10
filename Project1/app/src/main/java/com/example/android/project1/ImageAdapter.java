package com.example.android.project1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;


public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    // references to our images
    private MovieData[] mThumbMovies;

    public ImageAdapter(Context c, MovieData[] movies) {
        mContext = c;
        this.mThumbMovies = movies;
    }

    public int getCount() {
        return mThumbMovies.length;
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
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(mThumbMovies[position].getPoster()).into(imageView);
        return imageView;
    }
}
