package com.hliejun.dev.whatsappwidgets.fragments;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;
import android.widget.GridView;

import com.hliejun.dev.whatsappwidgets.R;
import com.hliejun.dev.whatsappwidgets.adapters.PaletteGridAdapter;

public class StylingFragment extends SectionFragment {

    static String styleColor = null;

    /*** Listeners */

    private AdapterView.OnItemClickListener gridItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
            PaletteGridAdapter adapter = (PaletteGridAdapter) adapterView.getAdapter();
            String prevColor = adapter.getSelectedColor();
            adapter.setSelection(index);

            String currColor = adapter.getSelectedColor();
            if (currColor != null && !currColor.equals(prevColor)) {
                styleColor = currColor;
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

        // TODO: Save fragment instance state

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Restore fragment instance state

    }

    @Override
    protected void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState) {
        final PaletteGridAdapter adapter = new PaletteGridAdapter(inflatedView.getContext());
        GridView gridView = inflatedView.findViewById(R.id.section_styling_grid);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(gridItemListener);
    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.fragment_styling;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // TODO: Handle activity result(s) if applicable

    }

}
