package com.hliejun.dev.whatsappwidgets;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.annotation.LayoutRes;
import android.support.annotation.CallSuper;

import android.view.ViewStub;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

public abstract class AsyncFragment extends Fragment {
    private Bundle mSavedInstanceState;
    private boolean mHasInflated = false;
    private ViewStub mViewStub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stub, container, false);
        mViewStub = (ViewStub) view.findViewById(R.id.view_stub);
        mViewStub.setLayoutResource(getViewStubLayoutResource());
        mSavedInstanceState = savedInstanceState;

        if (getUserVisibleHint() && !mHasInflated) {
            View inflatedView = mViewStub.inflate();
            onCreateViewAfterViewStubInflated(inflatedView, mSavedInstanceState);
            afterViewStubInflated(view);
        }

        return view;
    }

    protected abstract void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState);

    /**
     * The layout ID associated with this ViewStub
     * @see ViewStub#setLayoutResource(int)
     * @return
     */
    @LayoutRes
    protected abstract int getViewStubLayoutResource();

    /**
     *
     * @param originalViewContainerWithViewStub
     */
    @CallSuper
    protected void afterViewStubInflated(View originalViewContainerWithViewStub) {
        mHasInflated = true;

        if (originalViewContainerWithViewStub != null) {
            View progressBar = originalViewContainerWithViewStub.findViewById(R.id.progress_bar);
            progressBar.setAlpha(1f);
            progressBar.animate()
                       .alpha(0f)
                       .setDuration(100)
                       .setListener(null);
            progressBar.setVisibility(View.GONE);
            originalViewContainerWithViewStub.setAlpha(0f);
            originalViewContainerWithViewStub.setVisibility(View.VISIBLE);
            originalViewContainerWithViewStub.animate()
                                             .alpha(1f)
                                             .setDuration(200)
                                             .setListener(null);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && mViewStub != null && !mHasInflated) {
            View inflatedView = mViewStub.inflate();
            onCreateViewAfterViewStubInflated(inflatedView, mSavedInstanceState);
            afterViewStubInflated(getView());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHasInflated = false;
    }
}