package com.hliejun.dev.whatsappwidgets.fragments;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import com.hliejun.dev.whatsappwidgets.R;
import com.hliejun.dev.whatsappwidgets.ConfigurationActivity;
import com.hliejun.dev.whatsappwidgets.views.LockableViewPager;

public class SplashFragment extends SectionFragment {

    /*** Listeners ***/

    View.OnClickListener startListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // TODO: Check for WhatsApp and read contact permission
            // TODO: Pop snackbar with actions to download WhatsApp or change settings
            // TODO: Advance page if no issues found

            LockableViewPager pager = ((ConfigurationActivity)getActivity()).getPager();
            pager.incrementPage();
            pager.setSwipeable(true);
        }
    };

    /*** Lifecycle ***/

    public SplashFragment() {
        super();
    }

    public static SplashFragment newInstance(int sectionNumber) {
        SplashFragment fragment = new SplashFragment();
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
        Button mStartButton = inflatedView.findViewById(R.id.section_button);
        mStartButton.setOnClickListener(startListener);
    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.fragment_splash;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // TODO: Handle activity result(s) if applicable

    }

}
