package com.hliejun.dev.whatsappwidgets.fragments;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.SwitchCompat;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.hliejun.dev.whatsappwidgets.ConfigurationActivity;
import com.hliejun.dev.whatsappwidgets.R;
import com.hliejun.dev.whatsappwidgets.interfaces.ContactInterface;
import com.hliejun.dev.whatsappwidgets.interfaces.OptionsInterface;
import com.hliejun.dev.whatsappwidgets.models.Contact;
import com.hliejun.dev.whatsappwidgets.views.LockableViewPager;

// TODO: Refactor to cut down common code chunks

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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState) {
        LockableViewPager pager = ((ConfigurationActivity)getActivity()).getPager();

        ContactInterface contactTransaction = (ContactInterface) getActivity();
        Contact contact = contactTransaction.readContact();

        OptionsInterface optionsTransaction = (OptionsInterface) getActivity();
        String label = optionsTransaction.readLabel();
        String description = optionsTransaction.readDescription();
        boolean shouldUseNumber = optionsTransaction.readNumberOption();
        boolean shouldUseAvatar = optionsTransaction.readAvatarOption();
        boolean shouldUseLargeText = optionsTransaction.readLargeTextOption();

        final EditText labelEditText = inflatedView.findViewById(R.id.section_options_label);
        if (label != null) {
            labelEditText.setText(label);
        } else if (contact != null) {
            labelEditText.setText(contact.getName());
            optionsTransaction.writeLabel(contact.getName());
        }
        bindText(labelEditText);

        final EditText descriptionEditText = inflatedView.findViewById(R.id.section_options_description);
        if (description != null) {
            descriptionEditText.setText(description);
        }
        bindText(descriptionEditText);
        setKeyDone(descriptionEditText);
        setKeyDismiss(descriptionEditText);

        final EditText[] editTexts = { labelEditText, descriptionEditText };
        setViewDismiss(inflatedView, editTexts);
        setViewDismiss(pager, editTexts);
        setViewDismiss(((ConfigurationActivity) getActivity()).getNavBar(), editTexts);
        setPageDismiss(pager, editTexts);

        final SwitchCompat numberSwitch = inflatedView.findViewById(R.id.section_options_number_toggle);
        numberSwitch.setChecked(shouldUseNumber);
        bindSwitch(numberSwitch);

        final SwitchCompat avatarSwitch = inflatedView.findViewById(R.id.section_options_avatar_toggle);
        avatarSwitch.setChecked(shouldUseAvatar);
        bindSwitch(avatarSwitch);

        final SwitchCompat largeTextSwitch = inflatedView.findViewById(R.id.section_options_large_toggle);
        largeTextSwitch.setChecked(shouldUseLargeText);
        bindSwitch(largeTextSwitch);
    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.fragment_options;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        ContactInterface contactTransaction = (ContactInterface) getActivity();
        Contact contact = contactTransaction.readContact();

        OptionsInterface optionsTransaction = (OptionsInterface) getActivity();
        String label = optionsTransaction.readLabel();
        String description = optionsTransaction.readDescription();
        boolean shouldUseNumber = optionsTransaction.readNumberOption();
        boolean shouldUseAvatar = optionsTransaction.readAvatarOption();
        boolean shouldUseLargeText = optionsTransaction.readLargeTextOption();

        View view = getView();

        EditText labelEditText = view.findViewById(R.id.section_options_label);
        if (label != null) {
            labelEditText.setText(label);
        } else if (contact != null) {
            labelEditText.setText(contact.getName());
            optionsTransaction.writeLabel(contact.getName());
        }

        EditText descriptionEditText = view.findViewById(R.id.section_options_description);
        if (description != null) {
            descriptionEditText.setText(description);
        }

        SwitchCompat numberSwitch = view.findViewById(R.id.section_options_number_toggle);
        numberSwitch.setChecked(shouldUseNumber);

        SwitchCompat avatarSwitch = view.findViewById(R.id.section_options_avatar_toggle);
        avatarSwitch.setChecked(shouldUseAvatar);

        SwitchCompat largeTextSwitch = view.findViewById(R.id.section_options_large_toggle);
        largeTextSwitch.setChecked(shouldUseLargeText);
    }

    /*** Listener Setters ***/

    private void setKeyDismiss(final EditText editText) {
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

    private void setViewDismiss(View view, final EditText[] editTexts) {
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

    private void setPageDismiss(ViewPager pager, final EditText[] editTexts) {
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

    private void setKeyDone(final EditText editText) {
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

    private void bindText(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                OptionsInterface optionsTransaction = (OptionsInterface) getActivity();
                switch(editText.getId()) {
                    case R.id.section_options_label:
                        optionsTransaction.writeLabel(editable.toString());
                        break;
                    case R.id.section_options_description:
                        optionsTransaction.writeDescription(editable.toString());
                        break;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void bindSwitch(final SwitchCompat toggle) {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                OptionsInterface optionsTransaction = (OptionsInterface) getActivity();
                switch(toggle.getId()) {
                    case R.id.section_options_number_toggle:
                        optionsTransaction.writeNumberOption(isChecked);
                        break;
                    case R.id.section_options_avatar_toggle:
                        optionsTransaction.writeAvatarOption(isChecked);
                        break;
                    case R.id.section_options_large_toggle:
                        optionsTransaction.writeLargeTextOption(isChecked);
                        break;
                }
            }
        });
    }

}
