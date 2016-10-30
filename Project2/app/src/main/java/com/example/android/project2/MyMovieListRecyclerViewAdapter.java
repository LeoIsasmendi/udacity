package com.example.android.project2;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.project2.MovieListFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;


public class MyMovieListRecyclerViewAdapter extends RecyclerView.Adapter<MyMovieListRecyclerViewAdapter.ViewHolder> {

    private final MovieData[] mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;

    public MyMovieListRecyclerViewAdapter(Context context, MovieData[] items, OnListFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues[position];
        Picasso.with(mContext)
                .load(buildPosterUrl(holder.mItem.getPoster()))
                .placeholder(mContext.getResources().getDrawable(R.drawable.poster_default))
                .error(mContext.getResources().getDrawable(R.drawable.poster_default))
                .into(holder.mPosterView);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        //FIX THIS
        if (mValues != null) {
            return mValues.length;
        } else {
            return 0;
        }
    }

    private String buildPosterUrl(String poster) {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendEncodedPath(poster);

        return builder.build().toString();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public ImageView mPosterView;
        public MovieData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPosterView = (ImageView) view.findViewById(R.id.movie_poster);
        }

    }

}
