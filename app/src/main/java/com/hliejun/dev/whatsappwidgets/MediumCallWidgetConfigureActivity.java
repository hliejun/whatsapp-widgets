//package com.hliejun.dev.whatsappwidgets;
//
//import android.app.Activity;
//import android.appwidget.AppWidgetManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//
///**
// * The configuration screen for the {@link MediumCallWidget MediumCallWidget} AppWidget.
// */
//public class MediumCallWidgetConfigureActivity extends Activity {
//
//    private static final String PREFS_NAME = "com.hliejun.dev.whatsappwidgets.MediumCallWidget";
//    private static final String PREF_PREFIX_KEY = "appwidget_";
//    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
//    EditText mAppWidgetText;
//    View.OnClickListener mOnClickListener = new View.OnClickListener() {
//        public void onClick(View v) {
//            final Context context = MediumCallWidgetConfigureActivity.this;
//
//            // When the button is clicked, store the string locally
//            String widgetText = mAppWidgetText.getText().toString();
//            saveTitlePref(context, mAppWidgetId, widgetText);
//
//            // It is the responsibility of the configuration activity to update the app widget
//            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//            MediumCallWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);
//
//            // Make sure we pass back the original appWidgetId
//            Intent resultValue = new Intent();
//            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
//            setResult(RESULT_OK, resultValue);
//            finish();
//        }
//    };
//
//    public MediumCallWidgetConfigureActivity() {
//        super();
//    }
//
//    // Write the prefix to the SharedPreferences object for this widget
//    static void saveTitlePref(Context context, int appWidgetId, String text) {
//        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
//        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
//        prefs.apply();
//    }
//
//    // Read the prefix from the SharedPreferences object for this widget.
//    // If there is no preference saved, get the default from a resource
//    static String loadTitlePref(Context context, int appWidgetId) {
//        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
//        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
//        if (titleValue != null) {
//            return titleValue;
//        } else {
//            return context.getString(R.string.appwidget_text);
//        }
//    }
//
//    static void deleteTitlePref(Context context, int appWidgetId) {
//        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
//        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
//        prefs.apply();
//    }
//
//    @Override
//    public void onCreate(Bundle icicle) {
//        super.onCreate(icicle);
//
//        // Set the result to CANCELED.  This will cause the widget host to cancel
//        // out of the widget placement if the user presses the back button.
//        setResult(RESULT_CANCELED);
//
//        setContentView(R.layout.medium_call_widget_configure);
//        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
//        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);
//
//        // Find the widget id from the intent.
//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            mAppWidgetId = extras.getInt(
//                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
//        }
//
//        // If this activity was started with an intent without an app widget ID, finish with an error.
//        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
//            finish();
//            return;
//        }
//
//        mAppWidgetText.setText(loadTitlePref(MediumCallWidgetConfigureActivity.this, mAppWidgetId));
//    }
//}
//


package com.hliejun.dev.whatsappwidgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import java.util.List;

import com.hliejun.dev.whatsappwidgets.views.LockableViewPager;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

public class MediumCallWidgetConfigureActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link LockableViewPager} that will host the section contents.
     */
    private static LockableViewPager mViewPager;

    /**
     * The {@link android.widget.Button} to start configuration.
     */
    private Button mStartButton;

    /**
     * The {@link android.widget.Button} to navigate backwards.
     */
    private Button mPrevButton;

    /**
     * The {@link android.widget.Button} to navigate forward.
     */
    private Button mNextButton;

    /**
     * The {@link android.widget.Button} to complete configuration.
     */
    private Button mCreateButton;

    /**
     * The number of fragment pages in this configuration sequence.
     */
    // TODO: Update section count
    private int numOfSections = 4;

    // Static constants
    private static int CONTACT_PICKER_REQUEST = 1;
    private static int CONTACT_SEGUE_DELAY = 700;
    private static final String PREFS_NAME = "com.hliejun.dev.whatsappwidgets.MediumCallWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    // Widget properties
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = MediumCallWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
//            String widgetText = mAppWidgetText.getText().toString();
//            saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            MediumCallWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public MediumCallWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medium_call_widget_configure);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Bind views
        final View mNavTray = (View) findViewById(R.id.nav_container);
        mPrevButton = (Button) findViewById(R.id.nav_button_prev);
        mNextButton = (Button) findViewById(R.id.nav_button_next);
        mCreateButton = (Button) findViewById(R.id.nav_button_create);

        // Set up the ViewPager with the sections adapter.
        DotsIndicator mDotsIndicator = (DotsIndicator) findViewById(R.id.nav_dots_indicator);
        mDotsIndicator.setDotsClickable(false);
        mViewPager = (LockableViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setSwipeable(false);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                // TODO: Animate between pages (colours, etc.)

            }

            @Override
            public void onPageSelected(int position) {

                // TODO: Hide/show navigations

                mPrevButton.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                mNextButton.setVisibility(position == numOfSections - 1 || position == 0 ? View.GONE : View.VISIBLE);
                mCreateButton.setVisibility(position == numOfSections - 1 ? View.VISIBLE : View.GONE);
                mViewPager.setSwipeable(position != 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                // TODO: Page scroll state dependent logic

            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page = mViewPager.getCurrentItem();
                if (page > 0) {
                    page -= 1;
                }
                mViewPager.setCurrentItem(page, true);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page = mViewPager.getCurrentItem();
                if (page < numOfSections - 1) {
                    page += 1;
                }
                mViewPager.setCurrentItem(page, true);
            }
        });

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: Check if contact has been set correctly
                // TODO: Complete config and create widget with acquired fields
                // TODO: Snackbar display if no contact, with fix option to go to contacts

            }
        });

        mDotsIndicator.setViewPager(mViewPager);

//        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
//        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {

            // TODO: Reset configuration fields for current widget

            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            // TODO: Go to application settings (show/hide launcher icon, etc.)

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            int sectionIndex = getArguments().getInt(ARG_SECTION_NUMBER);

            // TODO: Inflate fragment layouts by section index
            // TODO: Fill in field values

            switch (sectionIndex) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_splash, container, false);
                    Button mStartButton = rootView.findViewById(R.id.section_button);
                    mStartButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // TODO: Check for WhatsApp and read contact permission
                            // TODO: Pop snackbar with actions to download WhatsApp or change settings
                            // TODO: Advance page if no issues found

                            int page = mViewPager.getCurrentItem();
                            page += 1;
                            mViewPager.setCurrentItem(page, true);
                        }
                    });
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_contact, container, false);
                    Button mContactButton = rootView.findViewById(R.id.section_button);
                    mContactButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            invokeContactSearch();
                        }
                    });
                    break;
                default:
                    rootView = inflater.inflate(R.layout.fragment_example, container, false);
                    TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                    textView.setText(getString(R.string.section_format, sectionIndex));
                    break;
            }

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == CONTACT_PICKER_REQUEST){
                if(resultCode == RESULT_OK) {
                    String whatsAppContactId = null;
                    List<ContactResult> results = MultiContactPicker.obtainResult(data);

                    // Get contact ID
                    String contactId = results.get(0).getContactID();
                    Log.d("Selected Contact", contactId);

                    // Check if contact has WhatsApp
                    String[] projection = new String[] { ContactsContract.RawContacts._ID };
                    String selection = ContactsContract.Data.CONTACT_ID + " = ? AND account_type IN (?)";
                    String[] selectionArgs = new String[] { contactId, "com.whatsapp" };
                    Cursor cursor = getContext().getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projection, selection, selectionArgs, null);
                    boolean hasWhatsApp = cursor.moveToNext();
                    if (hasWhatsApp) {
                        whatsAppContactId = cursor.getString(0);
                    }

                    if (whatsAppContactId != null) {
                        Log.d("WhatsApp ID", whatsAppContactId);

                        // TODO: Update selected contact data (ID, name, number, photo (opt))

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int page = mViewPager.getCurrentItem();
                                page += 1;
                                mViewPager.setCurrentItem(page, true);
                            }
                        }, CONTACT_SEGUE_DELAY);
                    } else {
                        Log.d("WhatsApp ID", "null");
                        Snackbar.make(getView(), "Contact has no WhatsApp", Snackbar.LENGTH_LONG)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        invokeContactSearch();
                                    }
                                }).show();
                    }


                } else if(resultCode == RESULT_CANCELED) {
                    Snackbar.make(getView(), "No contact selected", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    invokeContactSearch();
                                }
                            }).show();
                }
            }
        }

        public void invokeContactSearch() {

            // TODO: Contact button to invoke contact picker activity
            // TODO: Set fragment/activity to listen for results

            new MultiContactPicker.Builder(PlaceholderFragment.this)
                    .setLoadingType(MultiContactPicker.LOAD_SYNC)
                    .setChoiceMode(MultiContactPicker.CHOICE_MODE_SINGLE)
                    .limitToColumn(LimitColumn.PHONE)
                    .theme(R.style.AppTheme_NoActionBar)
                    .handleColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                    .bubbleColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                    .setActivityAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out,
                            android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    .showPickerForResult(CONTACT_PICKER_REQUEST);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a SectionFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return numOfSections;
        }
    }
}