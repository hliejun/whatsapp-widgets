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

// TODO: Refactor to cut down common code chunks

public class StylingFragment extends SectionFragment {

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
        final PaletteGridAdapter adapter = new PaletteGridAdapter(inflatedView.getContext());
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
