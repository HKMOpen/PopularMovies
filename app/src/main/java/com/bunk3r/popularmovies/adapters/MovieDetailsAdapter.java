package com.bunk3r.popularmovies.adapters;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bunk3r.popularmovies.Constants;
import com.bunk3r.popularmovies.R;
import com.bunk3r.popularmovies.model.Movie;
import com.bunk3r.popularmovies.model.RecyclerObject;
import com.bunk3r.popularmovies.model.RelatedVideo;
import com.bunk3r.popularmovies.model.Review;
import com.bunk3r.popularmovies.utils.StringUtils;

import java.util.Calendar;
import java.util.List;

public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SortedList<RecyclerObject> mItems = new SortedList<>(RecyclerObject.class, new RecyclerComparator());
    private OnInteractionListener mListener;

    public MovieDetailsAdapter(Movie movie) {
        mItems.add(movie);
    }

    public <T extends RecyclerObject> void addItems(List<T> items) {
        mItems.beginBatchedUpdates();
        for (RecyclerObject item : items) {
            mItems.add(item);
        }
        mItems.endBatchedUpdates();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).recyclerType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View container;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case RecyclerObject.MOVIE_TYPE:
                container = layoutInflater.inflate(R.layout.item_movie_detail, parent, false);
                holder = new MovieVH(container);
                break;
            case RecyclerObject.VIDEO_TYPE:
                container = layoutInflater.inflate(R.layout.item_related_video, parent, false);
                holder = new RelatedVideoVH(container);
                break;
            case RecyclerObject.REVIEW_TYPE:
                container = layoutInflater.inflate(R.layout.item_review, parent, false);
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

    public void onBindMovieHolder(MovieVH holder, final Movie movie) {
        Context context = holder.itemView.getContext();

        // Only load the image if a url is available
        if (!StringUtils.isEmpty(movie.getPosterUrl())) {
            holder.poster.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context)
                    .load(movie.getPosterUrl())
                    .fitCenter()
                    .into(holder.poster);
        } else {
            holder.poster.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .into(holder.poster);
        }

        holder.favorite.setChecked(movie.isFavorite());
        holder.favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                movie.setFavorite(isChecked);
                movie.save();
            }
        });

        // If the date is not available we show a "dash" instead
        if (movie.getReleaseDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(movie.getReleaseDate());
            holder.releaseDate.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        } else {
            holder.releaseDate.setText(R.string.movie_release_missing);
        }

        // truncate the rating to 1 character after the dot
        final float movieRating = (float) ((int) (movie.getVoteAverage() * 10)) / 10;
        holder.rating.setText(context.getString(R.string.movie_rating_style, movieRating, Constants.MAX_RATING));

        holder.overview.setText(movie.getOverview());
    }

    public void onBindRelatedVideoHolder(final RelatedVideoVH holder, final RelatedVideo video) {
        Context context = holder.itemView.getContext();

        holder.name.setText(video.getName());

        // Only load the image if a url is available
        if (!StringUtils.isEmpty(video.getThumbnail())) {
            holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER);
            Glide.with(context)
                    .load(video.getThumbnail())
                    .fitCenter()
                    .into(holder.thumbnail);
        } else {
            holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Glide.with(context)
                    .load(R.drawable.no_image_available)
                    .into(holder.thumbnail);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onVideoSelected(video);
                }
            }
        });

        final int colorFrom = context.getResources().getColor(R.color.transparent);
        final int colorTo = context.getResources().getColor(R.color.ripple_color);
        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                holder.itemView.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    colorAnimation.setDuration(250);
                    colorAnimation.start();
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    colorAnimation.setDuration(250);
                    colorAnimation.reverse();
                }
                return false;
            }
        });
    }

    public void onBindReviewHolder(final ReviewVH holder, Review review) {
        Context context = holder.itemView.getContext();

        holder.author.setText(review.getAuthor());

        holder.comment.setText(review.getContent());

        final int colorFrom = context.getResources().getColor(R.color.transparent);
        final int colorTo = context.getResources().getColor(R.color.ripple_color);
        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                holder.itemView.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    colorAnimation.setDuration(250);
                    colorAnimation.start();
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    colorAnimation.setDuration(250);
                    colorAnimation.reverse();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class MovieVH extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView releaseDate;
        TextView rating;
        TextView overview;
        CheckBox favorite;

        public MovieVH(View view) {
            super(view);

            poster = (ImageView) view.findViewById(R.id.movie_poster);
            releaseDate = (TextView) view.findViewById(R.id.movie_release_date);
            rating = (TextView) view.findViewById(R.id.movie_rating);
            overview = (TextView) view.findViewById(R.id.movie_overview);
            favorite = (CheckBox) view.findViewById(R.id.favorite);
        }
    }

    public class RelatedVideoVH extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView name;

        public RelatedVideoVH(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.related_video_thumbnail);
            name = (TextView) view.findViewById(R.id.related_video_name);
        }
    }

    public class ReviewVH extends RecyclerView.ViewHolder {
        TextView author;
        TextView comment;

        public ReviewVH(View view) {
            super(view);
            author = (TextView) view.findViewById(R.id.review_author);
            comment = (TextView) view.findViewById(R.id.review_comment);
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

    }

    private class RecyclerComparator extends SortedList.Callback<RecyclerObject> {

        @Override
        public int compare(RecyclerObject o1, RecyclerObject o2) {
            return o1.recyclerType() - o2.recyclerType();
        }

        @Override
        public void onInserted(int position, int count) {
            Log.d("SortList", "Inserted: " + position + " to " + (position + count - 1));
            notifyItemRangeInserted(position, position + count - 1);
        }

        @Override
        public void onRemoved(int position, int count) {
            Log.d("SortList", "Removed: " + position + " to " + (position + count - 1));
            notifyItemRangeRemoved(position, position + count - 1);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            Log.d("SortList", "Moved: " + fromPosition + " to " + toPosition);
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            Log.d("SortList", "Changed: " + position + " to " + (position + count - 1));
            notifyItemRangeChanged(position, position + count - 1);
        }

        @Override
        public boolean areContentsTheSame(RecyclerObject oldItem, RecyclerObject newItem) {
            return oldItem.hasChanged(newItem);
        }

        @Override
        public boolean areItemsTheSame(RecyclerObject left, RecyclerObject right) {
            return left.equals(right);
        }

    }

}