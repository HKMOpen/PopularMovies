package com.bunk3r.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bunk3r.popularmovies.Constants;
import com.bunk3r.popularmovies.R;
import com.bunk3r.popularmovies.model.Movie;
import com.bunk3r.popularmovies.utils.StringUtils;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieVH> {

    private List<Movie> mMovies;
    private OnInteractionListener mListener;

    public MoviesAdapter(List<Movie> tracks) {
        mMovies = tracks;
    }

    @Override
    public MovieVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View movieContainer = layoutInflater.inflate(R.layout.item_movie, parent, false);
        return new MovieVH(movieContainer);
    }

    @Override
    public void onBindViewHolder(MovieVH movieVH, int position) {
        final Movie currentMovie = mMovies.get(position);
        Context context = movieVH.itemView.getContext();

        if (!StringUtils.isEmpty(currentMovie.getPosterUrl())) {
            movieVH.picture.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context)
                    .load(Constants.IMG_BASE_URL + currentMovie.getPosterUrl())
                    .into(movieVH.picture);
        } else {
            movieVH.picture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .into(movieVH.picture);
        }

        movieVH.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onMovieSelected(currentMovie);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieVH extends RecyclerView.ViewHolder {
        public ImageView picture;

        public MovieVH(View view) {
            super(view);
            picture = (ImageView) view.findViewById(R.id.movie_poster);
        }
    }

    public void setOnInteractionListener(OnInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mListener = null;
    }

    public interface OnInteractionListener {

        void onMovieSelected(Movie movie);

    }
}
