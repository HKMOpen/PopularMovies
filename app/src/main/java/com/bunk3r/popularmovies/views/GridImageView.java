package com.bunk3r.popularmovies.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bunk3r.popularmovies.R;

public class GridImageView extends ImageView {

    private float mRatio = 1.0f;

    public GridImageView(Context context) {
        super(context);
    }

    public GridImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.GridImageView,
                0,
                0);

        try {
            mRatio = a.getFloat(R.styleable.GridImageView_ratio, 1.0f);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int modifiedHeight = (int)(getMeasuredWidth() / mRatio);
        setMeasuredDimension(getMeasuredWidth(), modifiedHeight);
    }

}