package com.bunk3r.popularmovies.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bunk3r.popularmovies.R;
import com.bunk3r.popularmovies.adapters.MoviesAdapter;
import com.bunk3r.popularmovies.listeners.MovieListener;
import com.bunk3r.popularmovies.model.Movie;
import com.bunk3r.popularmovies.network.TheMovieDB;
import com.bunk3r.popularmovies.network.responses.DiscoverMoviesResponse;
import com.bunk3r.popularmovies.utils.Constants;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A list fragment representing a list of Movies. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link MovieDetailFragment}.
 */
public class MovieListFragment extends Fragment implements Callback<DiscoverMoviesResponse>, MoviesAdapter.OnInteractionListener {

    private static final String NEXT_PAGE_ID = "next_page_to_load";
    private static final String LIST_OF_LOADED_MOVIES = "loaded_movies";
    private static final String FIRST_MOVIE_ON_SCREEN = "current_movie_on_Screen";

    private final AtomicBoolean isLoading = new AtomicBoolean(false);

    private int mNextPage = 1;
    private ArrayList<Movie> mMovies;
    private int mFirstVisibleMovie;
    private RecyclerView mList;
    private GridLayoutManager mLayoutManager;
    private MovieListener mCallback;

    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int numberOfColumns = getResources().getInteger(R.integer.number_of_columns);
        mLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);

        if (savedInstanceState != null) {
            mNextPage = savedInstanceState.getInt(NEXT_PAGE_ID);
            mFirstVisibleMovie = savedInstanceState.getInt(FIRST_MOVIE_ON_SCREEN);
            mMovies = savedInstanceState.getParcelableArrayList(LIST_OF_LOADED_MOVIES);
        } else {
            loadNextPage();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mList = (RecyclerView) root.findViewById(R.id.movie_list_results);
        mList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int maxScrollY = 0;
            private int overallScrollY = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                overallScrollY += dy;

                mFirstVisibleMovie = mLayoutManager.findFirstCompletelyVisibleItemPosition();

                if (maxScrollY <= overallScrollY) {
                    maxScrollY = overallScrollY;

                    if (mLayoutManager.findLastVisibleItemPosition() == mLayoutManager.getItemCount() - 1) {
                        loadNextPage();
                    }
                }
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList.setLayoutManager(mLayoutManager);

        if (mMovies != null) {
            MoviesAdapter adapter = new MoviesAdapter(mMovies);
            adapter.setOnInteractionListener(this);
            mList.setAdapter(adapter);

            // Load more pages until you can actually scroll
            if (mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0 && mLayoutManager.findLastCompletelyVisibleItemPosition() == mLayoutManager.getItemCount() - 1) {
                loadNextPage();
            } else {
                mLayoutManager.scrollToPosition(mFirstVisibleMovie);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof MovieListener) {
            mCallback = (MovieListener) activity;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NEXT_PAGE_ID, mNextPage);
        outState.putInt(FIRST_MOVIE_ON_SCREEN, mFirstVisibleMovie);
        outState.putParcelableArrayList(LIST_OF_LOADED_MOVIES, mMovies);
    }

    private void loadNextPage() {
        if (isLoading.compareAndSet(false, true)) {
            TheMovieDB.getInstance().getMovies(mNextPage, "popularity.desc", this);
        }
    }

    @Override
    public void success(DiscoverMoviesResponse discoverMoviesResponse, Response response) {
        if (isAdded() && !isRemoving()) {
            isLoading.set(false);
            mNextPage++;
            if (mMovies == null) {
                mMovies = new ArrayList<>(Constants.NUMBER_OF_MOVIES_PER_PAGE);
            }

            mMovies.addAll(discoverMoviesResponse.getResults());
            MoviesAdapter adapter = new MoviesAdapter(mMovies);
            adapter.setOnInteractionListener(this);
            mList.swapAdapter(adapter, false);

            // Load more pages until you can actually scroll
            if (mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0 && mLayoutManager.findLastCompletelyVisibleItemPosition() == mLayoutManager.getItemCount() - 1) {
                loadNextPage();
            }
        }
    }

    @Override
    public void failure(RetrofitError error) {
        if (isAdded() && !isRemoving()) {
            isLoading.set(false);
            Toast.makeText(getActivity(), R.string.error_loading_movie_page, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMovieSelected(Movie movie) {
        mCallback.showMovieDetails(movie);
    }

}