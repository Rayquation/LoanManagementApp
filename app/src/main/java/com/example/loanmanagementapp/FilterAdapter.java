package com.example.loanmanagementapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FilterAdapter extends ArrayAdapter<Enum<?>> {
    private final Enum<?>[] values;
    private final String allOption;

    public FilterAdapter(Context context, int resource, Enum<?>[] values, String allOption) {
        super(context, resource, values);
        this.values = values;
        this.allOption = allOption;
    }

    @Override
    public int getCount() {
        return values.length + 1; // +1 for the "All" option
    }

    @Override
    public Enum<?> getItem(int position) {
        if (position == 0) {
            return null; // Represents the "All" option
        }
        return values[position - 1];
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        if (position == 0) {
            view.setText(allOption);
        } else {
            view.setText(values[position - 1].toString());
        }
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        if (position == 0) {
            view.setText(allOption);
        } else {
            view.setText(values[position - 1].toString());
        }
        return view;
    }
}
