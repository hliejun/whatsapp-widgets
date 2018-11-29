package com.hliejun.dev.whatsappwidgets.fragments;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import com.hliejun.dev.whatsappwidgets.R;

public class SectionFragment extends AsyncFragment {

    protected static final String ARG_SECTION_NUMBER = "section_number";

    public SectionFragment() {
        super();
    }

    public static SectionFragment newInstance(int sectionNumber) {
        SectionFragment fragment = new SectionFragment();
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
    protected void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState) {}

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.fragment_example;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onLoad() {}

}