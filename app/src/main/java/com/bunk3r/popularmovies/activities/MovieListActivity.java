package com.bunk3r.popularmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bunk3r.popularmovies.R;
import com.bunk3r.popularmovies.fragments.MovieDetailFragment;
import com.bunk3r.popularmovies.fragments.MovieListFragment;
import com.bunk3r.popularmovies.listeners.MovieListener;
import com.bunk3r.popularmovies.model.Movie;


/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MovieListFragment} and the item details
 * (if present) is a {@link MovieDetailFragment}.
 * <p/>
 */
public class MovieListActivity extends AppCompatActivity implements MovieListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    public void showMovieDetails(Movie movie) {
        if (mTwoPane) {
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(movie.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        } else {
            Intent detailIntent = new Intent(this, MovieDetailActivity.class);
            detailIntent.putExtra(MovieDetailActivity.ARG_IN_MOVIE, movie.getId());
            startActivity(detailIntent);
        }
    }
}
