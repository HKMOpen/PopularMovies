package com.bunk3r.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.bunk3r.popularmovies.R;
import com.bunk3r.popularmovies.fragments.MovieDetailFragment;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link MovieDetailFragment}.
 */
public class MovieDetailActivity extends AppCompatActivity {

    public static final String ARG_IN_MOVIE = "MovieDetailActivity_arg_in_movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        if (savedInstanceState == null) {
            String movieId = getIntent().getStringExtra(ARG_IN_MOVIE);
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(movieId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

}
