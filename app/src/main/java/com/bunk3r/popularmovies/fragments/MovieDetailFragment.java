package com.bunk3r.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunk3r.popularmovies.R;
import com.bunk3r.popularmovies.activities.MovieDetailActivity;
import com.bunk3r.popularmovies.activities.MovieListActivity;
import com.bunk3r.popularmovies.model.Movie;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {

    public static final String ARG_IN_MOVIE = "MovieDetailFragment_arg_in_movie";

    private TextView mTitle;

    private Movie mCurrentMovie;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentMovie = getArguments().getParcelable(ARG_IN_MOVIE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mTitle = (TextView) root.findViewById(R.id.movie_detail);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle.setText(mCurrentMovie.getOriginalTitle());
    }
}
