package com.hliejun.dev.whatsappwidgets.fragments;

import android.app.Activity;
import android.content.Intent;

import android.database.Cursor;

import android.os.Bundle;
import android.os.Handler;

import android.provider.ContactsContract;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;

import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import com.hliejun.dev.whatsappwidgets.ConfigurationActivity;
import com.hliejun.dev.whatsappwidgets.R;

import com.hliejun.dev.whatsappwidgets.views.LockableViewPager;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

public class ContactFragment extends SectionFragment {

    private static final int CONTACT_PICKER_REQUEST = 1;
    private static final int CONTACT_SEGUE_DELAY = 700;
    private static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";

    /*** Listeners ***/

    View.OnClickListener contactListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleContactRequest();
        }
    };

    /*** Jobs ***/

    Runnable advancePage = new Runnable() {
        @Override
        public void run() {
            LockableViewPager pager = ((ConfigurationActivity)getActivity()).getPager();
            pager.incrementPage();
        }
    };

    /*** Lifecycle ***/

    public ContactFragment() {
        super();
    }

    public static ContactFragment newInstance(int sectionNumber) {
        ContactFragment fragment = new ContactFragment();
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
        Button mContactButton = inflatedView.findViewById(R.id.section_button);
        mContactButton.setOnClickListener(contactListener);
    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.fragment_contact;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_PICKER_REQUEST) {
            handleContactResponse(resultCode, data);
        }
    }

    /*** Handlers ***/

    private void handleContactRequest() {
        int primaryColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        new MultiContactPicker.Builder(ContactFragment.this)
                .setLoadingType(MultiContactPicker.LOAD_SYNC)
                .setChoiceMode(MultiContactPicker.CHOICE_MODE_SINGLE)
                .limitToColumn(LimitColumn.PHONE)
                .theme(R.style.AppTheme_NoActionBar)
                .handleColor(primaryColor)
                .bubbleColor(primaryColor)
                .showPickerForResult(CONTACT_PICKER_REQUEST);
    }

    private void handleContactResponse(int resultCode, Intent data) {
        String whatsAppContactId = null;

        // Complete request
        if (resultCode == Activity.RESULT_OK) {
            // Get contact details
            List<ContactResult> results = MultiContactPicker.obtainResult(data);
            String contactId = results.get(0).getContactID();

            // Get WhatsApp contact
            String[] projection = new String[] { ContactsContract.RawContacts._ID };
            String selection = ContactsContract.Data.CONTACT_ID + " = ? AND account_type IN (?)";
            String[] selectionArgs = new String[] { contactId, WHATSAPP_PACKAGE_NAME };
            Cursor cursor = getContext().getContentResolver()
                    .query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
            boolean hasWhatsApp = cursor.moveToNext();
            if (hasWhatsApp) {
                whatsAppContactId = cursor.getString(0);
            }

            // Handle valid WhatsApp contact
            if (whatsAppContactId != null) {

                // TODO: Update selected contact data (ID, name, number, photo (opt))

                final Handler handler = new Handler();
                handler.postDelayed(advancePage, CONTACT_SEGUE_DELAY);
            }

            // Handle invalid WhatsApp contact
            else {
                Snackbar snackbar = Snackbar.make(getView(), R.string.contact_no_whatsapp, Snackbar.LENGTH_LONG)
                        .setAction(R.string.contact_retry, contactListener);
                formatSnackbar(snackbar);
                snackbar.show();
            }
        }

        // Incomplete request
        else if (resultCode == Activity.RESULT_CANCELED) {
            Snackbar snackbar = Snackbar.make(getView(), R.string.contact_incomplete, Snackbar.LENGTH_LONG)
                    .setAction(R.string.contact_retry, contactListener);
            formatSnackbar(snackbar);
            snackbar.show();
        }
    }

    private void formatSnackbar(Snackbar snackbar) {
        TextView snackbarTextView = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackbarTextView.setTextSize(12);
        TextView snackbarActionTextView = snackbar.getView().findViewById( android.support.design.R.id.snackbar_action );
        snackbarActionTextView.setTextSize(11);
    }

}
