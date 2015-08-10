package com.bunk3r.popularmovies.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.bunk3r.popularmovies.enums.SortProperty;

import java.util.Arrays;

public class SortAdapter extends ArrayAdapter<SortProperty> {

    /**
     * Constructor
     *
     * @param context The current context.
     * @param objects The objects to represent in the ListView.
     */
    public SortAdapter(Context context, SortProperty[] objects) {
        super(context, android.R.layout.simple_spinner_dropdown_item, 0, Arrays.asList(objects));
    }

}
