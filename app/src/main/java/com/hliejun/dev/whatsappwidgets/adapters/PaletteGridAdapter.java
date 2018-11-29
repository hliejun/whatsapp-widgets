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
import com.hliejun.dev.whatsappwidgets.models.PaletteColor;

public class PaletteGridAdapter extends BaseAdapter {

    private Context mContext;

    final private PaletteColor[] colors = {
            new PaletteColor("Red", "#D32F2F"),
            new PaletteColor("Pink", "#FF4081"),
            new PaletteColor("Purple", "#AB47BC"),
            new PaletteColor("Lavender", "#7E57C2"),
            new PaletteColor("Indigo", "#5C6BC0"),
            new PaletteColor("Blue", "#1E88E5"),
            new PaletteColor("Cyan", "#0097A7"),
            new PaletteColor("Teal", "#009688"),
            new PaletteColor("Green", "#43A047"),
            new PaletteColor("Olive", "#827717"),
            new PaletteColor("Yellow", "#FBC02D"),
            new PaletteColor("Orange", "#F4511E"),
            new PaletteColor("Brown", "#8D6E63"),
            new PaletteColor("Gray", "#9E9E9E"),
            new PaletteColor("Metal", "#90A4AE")
    };

    // TODO: Consider moving selectionIndex to fragment and use restore/save instance state

    private static int selectionIndex = -1;

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
            gridItem = convertView;
        }

        TextView textView = gridItem.findViewById(R.id.grid_item_label);
        textView.setText(colors[index].getName());

        ImageView imageView = gridItem.findViewById(R.id.grid_item_preview);
        imageView.setBackgroundColor(Color.parseColor(colors[index].getHex()));

        ImageView checkView = gridItem.findViewById(R.id.grid_item_check);
        checkView.setVisibility(selectionIndex == index ? View.VISIBLE : View.GONE);

        return gridItem;
    }

    public void setSelection(int index) {
        selectionIndex = index;
    }

    public PaletteColor getSelectedColor() {
        return selectionIndex == -1 ? null : colors[selectionIndex];
    }

    public int getColorIndex(PaletteColor color) {
        for (int index = 0; index < colors.length; index++) {
            if (color.getHex().equals(colors[index].getHex())) {
                return index;
            }
        }

        return -1;
    }

}