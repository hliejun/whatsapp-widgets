package com.hliejun.dev.whatsappwidgets;

import android.os.Bundle;

import android.content.Context;
import android.content.SharedPreferences;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.appwidget.AppWidgetManager;

import android.widget.Button;
import android.widget.FrameLayout;

import com.hliejun.dev.whatsappwidgets.adapters.SectionsPagerAdapter;
import com.hliejun.dev.whatsappwidgets.fragments.SectionFragment;
import com.hliejun.dev.whatsappwidgets.interfaces.ContactInterface;
import com.hliejun.dev.whatsappwidgets.interfaces.OptionsInterface;
import com.hliejun.dev.whatsappwidgets.interfaces.StylingInterface;
import com.hliejun.dev.whatsappwidgets.models.Contact;
import com.hliejun.dev.whatsappwidgets.models.PaletteColor;
import com.hliejun.dev.whatsappwidgets.models.WidgetData;
import com.hliejun.dev.whatsappwidgets.views.LockableViewPager;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class ConfigurationActivity extends AppCompatActivity implements ContactInterface, OptionsInterface, StylingInterface {

    private static final int[] SECTIONS = {
            R.layout.fragment_splash,
            R.layout.fragment_contact,
            R.layout.fragment_options,
            R.layout.fragment_styling
    };

    private static final String PREFS_NAME = "com.hliejun.dev.whatsappwidgets.MediumCallWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String STATE_CONFIG_KEY = "configuration";

    private static int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private LockableViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private FrameLayout mNavBar;

    private Button mPrevButton;
    private Button mNextButton;
    private Button mCreateButton;

    /*** Settings ***/

    WidgetData configuration;

    /*** Listeners ***/

    private ViewPager.OnPageChangeListener pagingListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mPrevButton.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            mNextButton.setVisibility(position == SECTIONS.length - 1 || position == 0 ? View.GONE : View.VISIBLE);
            mCreateButton.setVisibility(position == SECTIONS.length - 1 ? View.VISIBLE : View.GONE);
            mViewPager.setSwipeable(position != 0);

            SectionFragment currFragment = mSectionsPagerAdapter.getCurrentFragment();
            if (currFragment != null) {
                currFragment.onLoad();
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    private View.OnClickListener prevListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mViewPager.decrementPage();
        }
    };

    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mViewPager.incrementPage();
        }
    };

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // TODO: Check if contact has been set correctly
            // TODO: Complete config and create widget with acquired fields (save prefs with widget ID)
            // TODO: Snackbar display if no contact, with fix option to go to contacts

        }
    };

    /*** Lifecycle ***/

    public ConfigurationActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuration);
        setResult(RESULT_CANCELED);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), SECTIONS);

        // Set up view pager
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setSwipeable(false);
        mViewPager.setOffscreenPageLimit(SECTIONS.length);
        mViewPager.addOnPageChangeListener(pagingListener);

        // Set up page indicator
        DotsIndicator mDotsIndicator = findViewById(R.id.nav_dots_indicator);
        mDotsIndicator.setDotsClickable(false);
        mDotsIndicator.setViewPager(mViewPager);

        // Bind buttons
        mPrevButton = findViewById(R.id.nav_button_prev);
        mNextButton = findViewById(R.id.nav_button_next);
        mCreateButton = findViewById(R.id.nav_button_create);

        // Set up buttons
        mPrevButton.setOnClickListener(prevListener);
        mNextButton.setOnClickListener(nextListener);
        mCreateButton.setOnClickListener(saveListener);

        // Bind navigation bar
        mNavBar = findViewById(R.id.nav_container);

        if (savedInstanceState != null) {
            configuration = (WidgetData) savedInstanceState.getSerializable(STATE_CONFIG_KEY);
        } else {
            configuration = new WidgetData();
        }

        // Find the widget ID from the intent
        /*
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        */

        // Terminate if configuration not launched from widget
        /*
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        */
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_CONFIG_KEY, configuration);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        configuration = (WidgetData) savedInstanceState.getSerializable(STATE_CONFIG_KEY);
    }

    /*** Menu Control ***/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_configuration, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {

            // TODO: Reset configuration fields for current widget

            return true;
        }

        if (id == R.id.action_settings) {

            // TODO: Go to application settings (show/hide launcher icon, etc.)

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*** Widget Preferences ***/

    static void saveWidgetPref(Context context, int appWidgetId, WidgetData data) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();

        // TODO: Save widget preferences with unique widget ID

    }

    static String loadWidgetPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);

        // TODO: Load widget preferences with unique widget ID

        return null;
    }

    static void deleteWidgetPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();

        // TODO: Delete widget preferences with unique widget ID

    }

    /*** Getters ***/

    public LockableViewPager getPager() {
        return mViewPager;
    }

    public FrameLayout getNavBar() {
        return mNavBar;
    }

    /*** Contact Interface ***/

    @Override
    public void writeContact(Contact contact) {
        configuration.setContact(contact);
    }

    @Override
    public Contact readContact() {
        return configuration.getContact();
    }

    /*** Options Interface ***/

    @Override
    public void writeLabel(String label) {
        configuration.setLabel(label);
    }

    @Override
    public String readLabel() {
        return configuration.getLabel();
    }

    @Override
    public void writeDescription(String description) {
        configuration.setDescription(description);
    }

    @Override
    public String readDescription() {
        return configuration.getDescription();
    }

    @Override
    public void writeNumberOption(boolean shouldUseNumber) {
        configuration.setShouldUseNumber(shouldUseNumber);
    }

    @Override
    public boolean readNumberOption() {
        return configuration.isShouldUseNumber();
    }

    @Override
    public void writeAvatarOption(boolean shouldUseAvatar) {
        configuration.setShouldUseAvatar(shouldUseAvatar);
    }

    @Override
    public boolean readAvatarOption() {
        return configuration.isShouldUseAvatar();
    }

    @Override
    public void writeLargeTextOption(boolean shouldUseLargeText) {
        configuration.setShouldUseLargeText(shouldUseLargeText);
    }

    @Override
    public boolean readLargeTextOption() {
        return configuration.isShouldUseLargeText();
    }

    /*** Styling Interface ***/

    @Override
    public void writeColor(PaletteColor color) {
        configuration.setColor(color);
    }

    @Override
    public PaletteColor readColor() {
        return configuration.getColor();
    }

}