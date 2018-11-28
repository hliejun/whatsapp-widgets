package com.hliejun.dev.whatsappwidgets.fragments;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hliejun.dev.whatsappwidgets.ConfigurationActivity;
import com.hliejun.dev.whatsappwidgets.R;
import com.hliejun.dev.whatsappwidgets.views.LockableViewPager;

public class OptionsFragment extends SectionFragment {

    /*** Lifecycle ***/

    public OptionsFragment() {
        super();
    }

    public static OptionsFragment newInstance(int sectionNumber) {
        OptionsFragment fragment = new OptionsFragment();
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
        LockableViewPager pager = ((ConfigurationActivity)getActivity()).getPager();
        final EditText labelEditText = (EditText) inflatedView.findViewById(R.id.section_options_label);
        final EditText descriptionEditText = (EditText) inflatedView.findViewById(R.id.section_options_description);
        final EditText[] editTexts = { labelEditText, descriptionEditText };

        setKeyDone(descriptionEditText);

        setKeyDismiss(descriptionEditText);

        setViewDismiss(inflatedView, editTexts);
        setViewDismiss(pager, editTexts);
        setViewDismiss(((ConfigurationActivity) getActivity()).getNavBar(), editTexts);

        setPageDismiss(pager, editTexts);
    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.fragment_options;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // TODO: Handle activity result(s) if applicable

    }

    /*** Listeners ***/

    public void setKeyDismiss(final EditText editText) {
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    View focusedView = getActivity().getCurrentFocus();
                    if (inputManager != null && focusedView != null) {
                        inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                    }
                    editText.clearFocus();
                }

                return false;
            }
        });
    }

    public void setViewDismiss(View view, final EditText[] editTexts) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                View focusedView = getActivity().getCurrentFocus();
                if (inputManager != null && focusedView != null) {
                    inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                }

                for (EditText editText : editTexts) {
                    if (editText != null) {
                        editText.clearFocus();
                    }
                }

                return false;
            }
        });
    }

    public void setPageDismiss(ViewPager pager, final EditText[] editTexts) {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                View focusedView = getActivity().getCurrentFocus();
                if (inputManager != null && focusedView != null) {
                    inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                }

                for (EditText editText : editTexts) {
                    if (editText != null) {
                        editText.clearFocus();
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                View focusedView = getActivity().getCurrentFocus();
                if (inputManager != null && focusedView != null) {
                    inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                }

                for (EditText editText : editTexts) {
                    if (editText != null) {
                        editText.clearFocus();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public void setKeyDone(final EditText editText) {
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    View focusedView = getActivity().getCurrentFocus();
                    if (inputManager != null && focusedView != null) {
                        inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                    }
                    editText.clearFocus();
                }

                return false;
            }
        });
    }

}
