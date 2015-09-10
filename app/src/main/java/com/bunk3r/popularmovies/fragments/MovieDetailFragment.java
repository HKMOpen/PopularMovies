package com.bunk3r.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunk3r.popularmovies.Constants;
import com.bunk3r.popularmovies.R;
import com.bunk3r.popularmovies.activities.MovieDetailActivity;
import com.bunk3r.popularmovies.activities.MovieListActivity;
import com.bunk3r.popularmovies.model.Movie;
import com.bunk3r.popularmovies.network.TheMovieDB;
import com.bunk3r.popularmovies.network.responses.RelatedVideosResponse;
import com.bunk3r.popularmovies.network.responses.ReviewsResponse;
import com.bunk3r.popularmovies.utils.StringUtils;

import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {

    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    public static final String ARG_IN_MOVIE = "MovieDetailFragment_arg_in_movie";

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mRating;
    private TextView mOverview;

    private Movie mCurrentMovie;

    public static MovieDetailFragment newInstance(Movie movie) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(MovieDetailFragment.ARG_IN_MOVIE, movie);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentMovie = getArguments().getParcelable(ARG_IN_MOVIE);
        if (mCurrentMovie != null) {
            TheMovieDB.getInstance().getRelatedVideos(mCurrentMovie.getMovieId(), new Callback<RelatedVideosResponse>() {
                @Override
                public void success(RelatedVideosResponse relatedVideosResponse, Response response) {
                    Log.d(TAG, "trailers: " + relatedVideosResponse.getResults().size());
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, error.getMessage());
                }
            });

            TheMovieDB.getInstance().getReviews(1, mCurrentMovie.getMovieId(), new Callback<ReviewsResponse>() {
                @Override
                public void success(ReviewsResponse reviewsResponse, Response response) {
                    Log.d(TAG, "reviews: " + reviewsResponse.getResults().size());
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, error.getMessage());
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mPoster = (ImageView) root.findViewById(R.id.movie_poster);
        mTitle = (TextView) root.findViewById(R.id.movie_title);
        mReleaseDate = (TextView) root.findViewById(R.id.movie_release_date);
        mRating = (TextView) root.findViewById(R.id.movie_rating);
        mOverview = (TextView) root.findViewById(R.id.movie_overview);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Only load the image if a url is available
        if (!StringUtils.isEmpty(mCurrentMovie.getPosterUrl())) {
            mPoster.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this)
                    .load(mCurrentMovie.getPosterUrl())
                    .fitCenter()
                    .into(mPoster);
        } else {
            mPoster.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Glide.with(this)
                    .load(R.drawable.no_image_available)
                    .into(mPoster);
        }

        mTitle.setText(mCurrentMovie.getTitle());

        // If the date is not available we show a "dash" instead
        if (mCurrentMovie.getReleaseDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mCurrentMovie.getReleaseDate());
            mReleaseDate.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        } else {
            mReleaseDate.setText(R.string.movie_release_missing);
        }

        // truncate the rating to 1 character after the dot
        final float movieRating = (float)((int) (mCurrentMovie.getVoteAverage() * 10)) / 10;
        mRating.setText(getString(R.string.movie_rating_style, movieRating, Constants.MAX_RATING));

        mOverview.setText(mCurrentMovie.getOverview());
    }
}
