package com.bunk3r.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bunk3r.popularmovies.R;
import com.bunk3r.popularmovies.model.Movie;
import com.bunk3r.popularmovies.model.RecyclerObject;
import com.bunk3r.popularmovies.model.RelatedVideo;
import com.bunk3r.popularmovies.model.Review;

import java.util.List;

public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RecyclerObject> mItems;
    private OnInteractionListener mListener;

    public MovieDetailsAdapter(List<RecyclerObject> items) {
        mItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View container;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case RecyclerObject.MOVIE_TYPE:
                container = layoutInflater.inflate(R.layout.item_movie, parent, false);
                holder = new MovieVH(container);
                break;
            case RecyclerObject.VIDEO_TYPE:
                container = layoutInflater.inflate(R.layout.item_movie, parent, false);
                holder = new RelatedVideoVH(container);
                break;
            case RecyclerObject.REVIEW_TYPE:
                container = layoutInflater.inflate(R.layout.item_movie, parent, false);
                holder = new ReviewVH(container);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final int type = viewHolder.getItemViewType();
        switch (type) {
            case RecyclerObject.MOVIE_TYPE:
                onBindMovieHolder((MovieVH) viewHolder, (Movie) mItems.get(position));
                break;
            case RecyclerObject.VIDEO_TYPE:
                onBindRelatedVideoHolder((RelatedVideoVH) viewHolder, (RelatedVideo) mItems.get(position));
                break;
            case RecyclerObject.REVIEW_TYPE:
                onBindReviewHolder((ReviewVH) viewHolder, (Review) mItems.get(position));
                break;
        }
    }

    public void onBindMovieHolder(MovieVH holder, Movie movie) {

    }

    public void onBindRelatedVideoHolder(RelatedVideoVH holder, RelatedVideo video) {

    }

    public void onBindReviewHolder(ReviewVH holder, Review review) {

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class MovieVH extends RecyclerView.ViewHolder {
        public ImageView picture;

        public MovieVH(View view) {
            super(view);
            picture = (ImageView) view.findViewById(R.id.movie_poster);
        }
    }

    public class RelatedVideoVH extends RecyclerView.ViewHolder {
        public ImageView picture;

        public RelatedVideoVH(View view) {
            super(view);
            picture = (ImageView) view.findViewById(R.id.movie_poster);
        }
    }

    public class ReviewVH extends RecyclerView.ViewHolder {
        public ImageView picture;

        public ReviewVH(View view) {
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

        void onVideoSelected(RelatedVideo video);

        void onReviewSelected(Review review);

    }

}