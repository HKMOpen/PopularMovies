package com.bunk3r.popularmovies.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bunk3r.popularmovies.Constants;
import com.bunk3r.popularmovies.R;
import com.bunk3r.popularmovies.activities.MovieDetailActivity;
import com.bunk3r.popularmovies.activities.MovieListActivity;
import com.bunk3r.popularmovies.adapters.MovieDetailsAdapter;
import com.bunk3r.popularmovies.model.Movie;
import com.bunk3r.popularmovies.model.RelatedVideo;
import com.bunk3r.popularmovies.network.TheMovieDB;
import com.bunk3r.popularmovies.network.responses.RelatedVideosResponse;
import com.bunk3r.popularmovies.network.responses.ReviewsResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailsAdapter.OnInteractionListener {

    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    public static final String ARG_IN_MOVIE = "MovieDetailFragment_arg_in_movie";

    private RecyclerView mMovieDetailsRecycler;
    private MovieDetailsAdapter mMovieDetailsAdapter;

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
                    mMovieDetailsAdapter.addItems(relatedVideosResponse.getResults());
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG, error.getMessage());
                }
            });

            TheMovieDB.getInstance().getReviews(1, mCurrentMovie.getMovieId(), new Callback<ReviewsResponse>() {
                @Override
                public void success(ReviewsResponse reviewsResponse, Response response) {
                    mMovieDetailsAdapter.addItems(reviewsResponse.getResults());
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

        mMovieDetailsRecycler = (RecyclerView) root.findViewById(R.id.movie_details_recycler);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMovieDetailsAdapter = new MovieDetailsAdapter(mCurrentMovie);
        mMovieDetailsAdapter.setOnInteractionListener(this);
        mMovieDetailsRecycler.setAdapter(mMovieDetailsAdapter);
        mMovieDetailsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onVideoSelected(RelatedVideo video) {
        Intent videoClient = new Intent(Intent.ACTION_VIEW);
        videoClient.setData(Uri.parse(video.getVideoUrl()));
        startActivityForResult(videoClient, Constants.OPEN_RELATED_VIDEO_CODE);
    }

}