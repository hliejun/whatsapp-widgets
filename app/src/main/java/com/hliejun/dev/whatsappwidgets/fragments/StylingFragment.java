package com.hliejun.dev.whatsappwidgets.fragments;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;
import android.widget.GridView;

import com.hliejun.dev.whatsappwidgets.R;
import com.hliejun.dev.whatsappwidgets.adapters.PaletteGridAdapter;
import com.hliejun.dev.whatsappwidgets.interfaces.StylingInterface;
import com.hliejun.dev.whatsappwidgets.models.PaletteColor;

public class StylingFragment extends SectionFragment {

    final public static PaletteColor DEFAULT_COLOR =  new PaletteColor("Indigo", "#5C6BC0");

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
            new PaletteColor("Yellow", "#FBC02D", PaletteColor.ColorType.LIGHT),
            new PaletteColor("Orange", "#F4511E"),
            new PaletteColor("Brown", "#8D6E63"),
            new PaletteColor("Gray", "#9E9E9E"),
            new PaletteColor("Metal", "#90A4AE"),
            new PaletteColor("White", "#F5F5F5", PaletteColor.ColorType.LIGHT),
            new PaletteColor("Black", "#263238")
    };

    /*** Listeners */

    private AdapterView.OnItemClickListener gridItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
            PaletteGridAdapter adapter = (PaletteGridAdapter) adapterView.getAdapter();
            String prevColor = adapter.getSelectedColor() != null ? adapter.getSelectedColor().getHex() : null;
            adapter.setSelection(index);

            String currColor = adapter.getSelectedColor().getHex();
            if (currColor != null && !currColor.equals(prevColor)) {
                StylingInterface stylingTransaction = (StylingInterface) getActivity();
                stylingTransaction.writeColor(adapter.getSelectedColor());
                adapter.notifyDataSetChanged();
            }
        }
    };

    /*** Lifecycle ***/

    public StylingFragment() {
        super();
    }

    public static StylingFragment newInstance(int sectionNumber) {
        StylingFragment fragment = new StylingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState) {
        final PaletteGridAdapter adapter = new PaletteGridAdapter(inflatedView.getContext(), colors);
        GridView gridView = inflatedView.findViewById(R.id.section_styling_grid);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(gridItemListener);

        StylingInterface stylingTransaction = (StylingInterface) getActivity();
        PaletteColor color = stylingTransaction.readColor();
        if (color != null) {
            int index = adapter.getColorIndex(color);
            adapter.setSelection(index);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.fragment_styling;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        GridView gridView = getView().findViewById(R.id.section_styling_grid);
        PaletteGridAdapter adapter = (PaletteGridAdapter) gridView.getAdapter();
        StylingInterface stylingTransaction = (StylingInterface) getActivity();
        PaletteColor color = stylingTransaction.readColor();
        if (color != null) {
            int index = adapter.getColorIndex(color);
            adapter.setSelection(index);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void reset() {
        super.reset();

        PaletteGridAdapter.resetSelection();

        View fragmentView = getView();
        if (fragmentView == null) {
            return;
        }

        GridView gridView = fragmentView.findViewById(R.id.section_styling_grid);
        if (gridView == null) {
            return;
        }

        PaletteGridAdapter adapter = (PaletteGridAdapter) gridView.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

}
