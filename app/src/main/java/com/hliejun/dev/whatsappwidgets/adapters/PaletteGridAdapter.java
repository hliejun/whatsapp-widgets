package com.hliejun.dev.whatsappwidgets.adapters;

import android.annotation.SuppressLint;

import android.content.Context;
import android.graphics.Color;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import com.hliejun.dev.whatsappwidgets.R;

public class PaletteGridAdapter extends BaseAdapter {
    private Context mContext;

    // TODO: Make palette objects and move values to fragment

    final String[] labels = {
        "Red",
        "Pink",
        "Purple",
        "Lavender",
        "Indigo",
        "Blue",
        "Cyan",
        "Teal",
        "Green",
        "Olive",
        "Yellow",
        "Orange",
        "Brown",
        "Gray",
        "Metal"
    };

    final String[] colors = {
        "#D32F2F",
        "#FF4081",
        "#AB47BC",
        "#7E57C2",
        "#5C6BC0",
        "#1E88E5",
        "#0097A7",
        "#009688",
        "#43A047",
        "#827717",
        "#FBC02D",
        "#F4511E",
        "#8D6E63",
        "#9E9E9E",
        "#90A4AE"
    };

    private int selectionIndex = -1;

    public PaletteGridAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public Object getItem(int index) {
        return null;
    }

    @Override
    public long getItemId(int index) {
        return 0;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        View gridItem;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            gridItem = inflater.inflate(R.layout.view_grid_item, null);
        } else {
            gridItem = (View) convertView;
        }

        TextView textView = (TextView) gridItem.findViewById(R.id.grid_item_label);
        textView.setText(labels[index]);

        ImageView imageView = (ImageView)gridItem.findViewById(R.id.grid_item_preview);
        imageView.setBackgroundColor(Color.parseColor(colors[index]));

        ImageView checkView = (ImageView)gridItem.findViewById(R.id.grid_item_check);
        checkView.setVisibility(selectionIndex == index ? View.VISIBLE : View.GONE);

        return gridItem;
    }

    public void setSelection(int index) {
        selectionIndex = index;
    }

    public String getSelectedColor() {
        return selectionIndex == -1 ? null : colors[selectionIndex];
    }
}