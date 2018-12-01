package com.hliejun.dev.whatsappwidgets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hliejun.dev.whatsappwidgets.R;

public class ExampleFragment extends SectionFragment {

    public ExampleFragment() {}

    public static ExampleFragment newInstance(int sectionNumber) {
        ExampleFragment fragment = new ExampleFragment();
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
        int sectionIndex = getArguments().getInt(ARG_SECTION_NUMBER);
        TextView textView = (TextView) inflatedView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, sectionIndex));
    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.fragment_example;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void reset() {
        super.reset();
    }

}
