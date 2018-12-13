package com.hliejun.dev.widgets.adapters;

import android.annotation.SuppressLint;

import android.content.Context;
import android.graphics.Color;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import com.hliejun.dev.widgets.R;
import com.hliejun.dev.widgets.models.PaletteColor;

public class PaletteGridAdapter extends BaseAdapter {

    private Context mContext;

    private PaletteColor[] colors;

    private static int selectionIndex = -1;

    public PaletteGridAdapter(Context context, PaletteColor[] colors) {
        mContext = context;
        this.colors = colors;
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
        checkView.setImageResource(colors[index].isLightColor() ? R.drawable.ic_check_black : R.drawable.ic_check);

        return gridItem;
    }

    public void setSelection(int index) {
        selectionIndex = index;
    }

    public static void resetSelection() {
        selectionIndex = -1;
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