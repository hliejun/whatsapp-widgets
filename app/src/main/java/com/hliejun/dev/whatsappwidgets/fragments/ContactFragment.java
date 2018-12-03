package com.hliejun.dev.whatsappwidgets.fragments;

import android.app.Activity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;

import android.net.Uri;

import android.os.Bundle;
import android.os.Handler;

import android.provider.ContactsContract;

import android.support.v4.content.ContextCompat;

import android.view.View;

import android.widget.Button;

import java.util.List;

import com.hliejun.dev.whatsappwidgets.R;
import com.hliejun.dev.whatsappwidgets.ConfigurationActivity;
import com.hliejun.dev.whatsappwidgets.NotificationManager;
import com.hliejun.dev.whatsappwidgets.interfaces.ContactInterface;
import com.hliejun.dev.whatsappwidgets.interfaces.OptionsInterface;
import com.hliejun.dev.whatsappwidgets.views.LockableViewPager;
import com.hliejun.dev.whatsappwidgets.models.Contact;

import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

public class ContactFragment extends SectionFragment {

    private static final String PACKAGE_NAME_WHATSAPP = "com.whatsapp";
    private static final int CONTACT_PICKER_REQUEST = 1;
    private static final int CONTACT_SEGUE_DELAY = 700;

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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void reset() {
        super.reset();
    }

    /*** Handlers ***/

    private void handleContactRequest() {
        ContactInterface contactTransaction = (ContactInterface) getActivity();
        if (contactTransaction != null) {
            contactTransaction.writeContact(null);
        }

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
        String whatsAppNumber = null;

        if (resultCode == Activity.RESULT_OK) {
            // Get contact details
            List<ContactResult> results = MultiContactPicker.obtainResult(data);
            ContactResult result = results.get(0);
            String contactId = result.getContactID();

            // Get WhatsApp contact
            String[] projection = new String[] { ContactsContract.Data.RAW_CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER };
            String selection = ContactsContract.Data.CONTACT_ID + " = ? AND "
                    + ContactsContract.Data.MIMETYPE + "='"
                    + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    + "' AND " + ContactsContract.RawContacts.ACCOUNT_TYPE + "= ?";
            String[] selectionArgs = new String[] { contactId, PACKAGE_NAME_WHATSAPP };
            Cursor cursor = getContext().getContentResolver()
                    .query(ContactsContract.Data.CONTENT_URI, projection, selection, selectionArgs, null);
            boolean hasWhatsApp = cursor.moveToNext();
            if (hasWhatsApp) {
                whatsAppContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
                whatsAppNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }

            // Handle WhatsApp contact by validity
            if (whatsAppContactId != null && whatsAppNumber != null) {
                String name = result.getDisplayName();

                Uri photoUri = getPhotoUri(contactId, getContext());
                Contact contact = new Contact(whatsAppContactId, name, whatsAppNumber, photoUri);
                ContactInterface contactTransaction = (ContactInterface) getActivity();
                if (contactTransaction != null) {
                    contactTransaction.writeContact(contact);
                }

                OptionsInterface optionsTransaction = (OptionsInterface) getActivity();
                if (optionsTransaction != null) {
                    optionsTransaction.writeLabel(name);
                }

                final Handler handler = new Handler();
                handler.postDelayed(advancePage, CONTACT_SEGUE_DELAY);
            } else {
                NotificationManager.showSnackbar(
                        getContext(),
                        getString(R.string.contact_no_whatsapp),
                        getString(R.string.contact_retry),
                        contactListener
                );
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            NotificationManager.showSnackbar(
                    getContext(),
                    getString(R.string.contact_incomplete),
                    getString(R.string.contact_retry),
                    contactListener
            );
        }
    }

    private Uri getPhotoUri(String id, Context context) {
        long contactId = Long.parseLong(id);
        ContentResolver contentResolver = context.getContentResolver();

        try {
            Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null,
                    ContactsContract.Data.CONTACT_ID
                            + "=" + contactId + " AND " + ContactsContract.Data.MIMETYPE + "='"
                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'",
                    null, null);
            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }

        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);

        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
    }

}
