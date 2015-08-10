package com.bunk3r.popularmovies.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bunk3r.popularmovies.Constants;
import com.bunk3r.popularmovies.R;
import com.bunk3r.popularmovies.adapters.MoviesAdapter;
import com.bunk3r.popularmovies.adapters.SortAdapter;
import com.bunk3r.popularmovies.enums.SortProperty;
import com.bunk3r.popularmovies.listeners.MovieListener;
import com.bunk3r.popularmovies.model.Movie;
import com.bunk3r.popularmovies.network.TheMovieDB;
import com.bunk3r.popularmovies.network.responses.DiscoverMoviesResponse;
import com.bunk3r.popularmovies.utils.StringUtils;

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
public class MovieListFragment extends Fragment implements Callback<DiscoverMoviesResponse>,
        MoviesAdapter.OnInteractionListener,
        AdapterView.OnItemSelectedListener {

    protected final String TAG = getClass().getSimpleName();

    private static final String NEXT_PAGE_ID = "next_page_to_load";
    private static final String SORTING_TYPE = "sort_type_to_use";
    private static final String LIST_OF_LOADED_MOVIES = "loaded_movies";
    private static final String FIRST_MOVIE_ON_SCREEN = "current_movie_on_Screen";

    private final AtomicBoolean isLoading = new AtomicBoolean(false);

    private int mNextPage = 1;
    private int mColumns;
    private String mSortType;
    private ArrayList<Movie> mMovies = new ArrayList<>(Constants.NUMBER_OF_MOVIES_PER_PAGE);
    private int mFirstVisibleMovie;
    private InfinityScrollListener mScrollerListener = new InfinityScrollListener();
    private View mProgressContainer;
    private RecyclerView mList;
    private GridLayoutManager mLayoutManager;
    private MovieListener mCallback;

    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        mColumns = getResources().getInteger(R.integer.number_of_columns);
        mLayoutManager = new GridLayoutManager(getActivity(), mColumns);

        if (savedInstanceState != null) {
            mNextPage = savedInstanceState.getInt(NEXT_PAGE_ID);
            mSortType = savedInstanceState.getString(SORTING_TYPE);
            mFirstVisibleMovie = savedInstanceState.getInt(FIRST_MOVIE_ON_SCREEN);
            mMovies = savedInstanceState.getParcelableArrayList(LIST_OF_LOADED_MOVIES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mProgressContainer = root.findViewById(R.id.loading_more);
        mList = (RecyclerView) root.findViewById(R.id.movie_list_results);
        mList.addOnScrollListener(mScrollerListener);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList.setLayoutManager(mLayoutManager);

        if (!mMovies.isEmpty()) {
            MoviesAdapter adapter = new MoviesAdapter(mMovies);
            adapter.setOnInteractionListener(this);
            mList.setAdapter(adapter);

            // Load more pages until you can actually scroll
            if (mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0 && mLayoutManager.findLastCompletelyVisibleItemPosition() == mLayoutManager.getItemCount() - 1) {
                loadNextPage();
            } else {
                mList.scrollToPosition(mFirstVisibleMovie);
            }
        } else {
            loadNextPage();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_list_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_sort_type);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        spinner.setAdapter(new SortAdapter(getActivity(), SortProperty.values()));
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NEXT_PAGE_ID, mNextPage);
        outState.putString(SORTING_TYPE, mSortType);
        outState.putInt(FIRST_MOVIE_ON_SCREEN, mFirstVisibleMovie);
        outState.putParcelableArrayList(LIST_OF_LOADED_MOVIES, mMovies);
    }

    private void loadNextPage() {
        if (isLoading.compareAndSet(false, true)) {
            mProgressContainer.setVisibility(View.VISIBLE);
            TheMovieDB.getInstance().getMovies(mNextPage, mSortType, this);
        }
    }

    @Override
    public void success(DiscoverMoviesResponse discoverMoviesResponse, Response response) {
        isLoading.set(false);
        if (isAdded() && !isRemoving()) {
            mProgressContainer.setVisibility(View.INVISIBLE);
            mNextPage++;

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
        isLoading.set(false);
        if (isAdded() && !isRemoving()) {
            mProgressContainer.setVisibility(View.INVISIBLE);
            Log.d(TAG, error.getMessage());
            Toast.makeText(getActivity(), R.string.error_loading_movie_page, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMovieSelected(Movie movie) {
        mCallback.showMovieDetails(movie);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SortProperty property = SortProperty.values()[position];
        String sortType = property.getPropertyValue();
        if (StringUtils.isEmpty(mSortType) || !mSortType.equals(sortType)) {
            mScrollerListener.reset();
            mSortType = sortType;
            mNextPage = 1;
            mMovies.clear();
            loadNextPage();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // STUB
    }

    private class InfinityScrollListener extends RecyclerView.OnScrollListener {

        private int maxScrollY;
        private int overallScrollY;

        public InfinityScrollListener() {
            reset();
        }

        public void reset() {
            maxScrollY = 0;
            overallScrollY = 0;
        }

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
                if (mLayoutManager.findLastVisibleItemPosition() >= mLayoutManager.getItemCount() - (mColumns * 3)) {
                    loadNextPage();
                }
            }
        }

    }

}