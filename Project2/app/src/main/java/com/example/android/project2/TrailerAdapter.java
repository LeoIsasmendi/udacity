package com.example.android.project2;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TrailerAdapter extends BaseAdapter {

    private Context mContext;
    // references to our images
    private MovieTrailer[] trailers;

    public TrailerAdapter(Context c, MovieTrailer[] trailers) {
        mContext = c;
        this.trailers = trailers;
    }

    public int getCount() {
        return trailers.length;
    }

    public Object getItem(int position) {
        return trailers[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView text = new TextView(this.mContext);
        text.setText(trailers[position].getName());
        return text;
    }
}