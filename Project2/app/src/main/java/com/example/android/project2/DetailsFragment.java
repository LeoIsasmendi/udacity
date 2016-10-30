package com.example.android.project2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsFragment extends Fragment {

    private String TAG = "DetailsFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private MovieDataParcelable mParcelable;
    private MovieReview[] mReviews;
    private MovieTrailer[] mTrailers;

    private OnFragmentInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParcelable = getArguments().getParcelable("movie");
            mReviews = (MovieReview[]) getArguments().get("reviews");
            Log.i(TAG, "onCreate: "+mParcelable.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mParcelable != null) {
            setTitle(mParcelable.getTitle());
            setSynopsis(mParcelable.getOverview());
            setPoster(mParcelable.getPoster());
            setUserRating(mParcelable.getVoteAverage());
            setReleaseDate(mParcelable.getReleaseDate());
            setFavorite(mParcelable);
        }
    }

    private void setTitle(String text) {
        TextView title = (TextView) getView().findViewById(R.id.original_title);
        title.setText(text);
    }

    private void setSynopsis(String text) {
        TextView synopsis = (TextView) getView().findViewById(R.id.synopsis);
        synopsis.setText(text);
    }

    private void setPoster(String text) {
        ImageView imageView = (ImageView) getView().findViewById(R.id.poster);
        UriBuilder uriBuilder = new UriBuilder();
        Picasso.with(getContext())
                .load(uriBuilder.buildPosterUrl(text))
                .placeholder(getContext().getResources().getDrawable(R.drawable.poster_default))
                .error(getContext().getResources().getDrawable(R.drawable.poster_default))
                .into(imageView);
    }

    private void setUserRating(Double vote) {
        TextView rating = (TextView) getView().findViewById(R.id.rating);
        rating.setText(String.format("Rating: %1$.2f / 10", vote));
    }

    private void setReleaseDate(String text) {
        TextView date = (TextView) getView().findViewById(R.id.released);
        date.setText("Released: " + text);
    }

    public void onToggleStar(View view) {
        CheckBox checkbox = (CheckBox) view;
        mListener.onToggleMovieAsFavorite(mParcelable, checkbox.isChecked());
    }

    public void setReviews(MovieReview[] list) {
        //TODO show all reviews
        Log.i(TAG, "setReviews: "+list.toString());
        if (list != null) {
            TextView reviews = (TextView) getView().findViewById(R.id.reviews);
            reviews.setText(list[0].getContent());
        }
    }

    public void setTrailers(MovieTrailer[] list) {
        mTrailers = list;
        TrailerAdapter adapter = new TrailerAdapter(getContext(), mTrailers);
        ListView trailers = (ListView) getView().findViewById(R.id.trailers);
        trailers.setEmptyView(getView().findViewById(R.id.empty_trailers));
        trailers.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mListener.onShowTrailer( (MovieTrailer) parent.getItemAtPosition(position));
                    }
                }
        );
        trailers.setAdapter(adapter);
    }

    public void setFavorite(MovieDataParcelable movie) {
        CheckBox button = (CheckBox) this.getView().findViewById(R.id.fav_button);
        button.setChecked(mListener.isFavorite(movie));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToggleStar(v);
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onToggleMovieAsFavorite(MovieDataParcelable movie, Boolean isFav);
        void onShowTrailer(MovieTrailer trailer);
        Boolean isFavorite(MovieDataParcelable movie);
    }

}
