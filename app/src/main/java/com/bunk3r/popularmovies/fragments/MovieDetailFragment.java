package com.bunk3r.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunk3r.popularmovies.R;
import com.bunk3r.popularmovies.activities.MovieDetailActivity;
import com.bunk3r.popularmovies.activities.MovieListActivity;
import com.bunk3r.popularmovies.model.Movie;
import com.bunk3r.popularmovies.utils.StringUtils;

import java.util.Calendar;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {

    public static final String ARG_IN_MOVIE = "MovieDetailFragment_arg_in_movie";

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mDuration;
    private TextView mRating;
    private TextView mOverview;

    private Movie mCurrentMovie;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentMovie = getArguments().getParcelable(ARG_IN_MOVIE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mPoster = (ImageView) root.findViewById(R.id.movie_poster);
        mTitle = (TextView) root.findViewById(R.id.movie_title);
        mReleaseDate = (TextView) root.findViewById(R.id.movie_release_date);
        mDuration = (TextView) root.findViewById(R.id.movie_duration);
        mRating = (TextView) root.findViewById(R.id.movie_rating);
        mOverview = (TextView) root.findViewById(R.id.movie_overview);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!StringUtils.isEmpty(mCurrentMovie.getPosterUrl())) {
            Glide.with(this)
                    .load("http://image.tmdb.org/t/p/w185" + mCurrentMovie.getPosterUrl())
                    .into(mPoster);
        }

        mTitle.setText(mCurrentMovie.getTitle());

        if (mCurrentMovie.getReleaseDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mCurrentMovie.getReleaseDate());
            mReleaseDate.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        } else {
            mReleaseDate.setText("-");
        }

        mDuration.setText("999 min");

        mRating.setText(String.valueOf((float)((int) (mCurrentMovie.getVoteAverage() * 10)) / 10));

        mOverview.setText(mCurrentMovie.getOverview());
    }
}
