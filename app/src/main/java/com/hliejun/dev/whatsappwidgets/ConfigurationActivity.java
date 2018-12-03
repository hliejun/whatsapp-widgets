package com.hliejun.dev.whatsappwidgets;

import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;

import android.os.Bundle;

import android.content.Context;
import android.content.SharedPreferences;

import android.provider.Settings;

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
import com.hliejun.dev.whatsappwidgets.fragments.StylingFragment;
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

    private static final String PREFS_NAME = "com.hliejun.dev.whatsappwidgets.MediumWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String STATE_CONFIG_KEY = "configuration";

    public static final String KEY_WHATSAPP_ID = "whatsappid";
    public static final String KEY_NAME = "name";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_PHOTO_URI = "photouri";
    public static final String KEY_LABEL = "label";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_OPTION_NUMBER = "optnumber";
    public static final String KEY_OPTION_AVATAR = "optavatar";
    public static final String KEY_OPTION_LARGE_TEXT = "optlargetext";
    public static final String KEY_COLOR_NAME = "colorname";
    public static final String KEY_COLOR_HEX = "colorhex";
    public static final String KEY_COLOR_IS_LIGHT = "islightcolor";

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
            if (configuration.getContact() == null) {
                NotificationManager.showSnackbar(
                        view.getContext(),
                        getString(R.string.create_no_contact),
                        getString(R.string.create_fix),
                        getPageBrowseListener(1)
                );
            } else if (configuration.getLabel() == null || configuration.getLabel().equals("")) {
                NotificationManager.showSnackbar(
                        view.getContext(),
                        getString(R.string.create_no_label),
                        getString(R.string.create_fix),
                        getPageBrowseListener(2)
                );
            } else {
                if (configuration.getColor() == null) {
                    configuration.setColor(StylingFragment.DEFAULT_COLOR);
                }
                Context context = view.getContext();
                saveWidgetPref(context, mAppWidgetId, configuration);

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                MediumWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        }
    };

    private DialogInterface.OnClickListener resetListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            configuration = new WidgetData();
            recreate();
            mSectionsPagerAdapter.notifyDataSetChanged();
            mSectionsPagerAdapter.reloadFragments();
            mViewPager.setCurrentItem(0, true);
        }
    };

    private DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    /*** Listener Getters ***/

    private View.OnClickListener getPageBrowseListener(final int index) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(index, true);
            }
        };
    }

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

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
             finish();
             return;
        }

        if (savedInstanceState != null) {
            configuration = (WidgetData) savedInstanceState.getSerializable(STATE_CONFIG_KEY);
        } else {
            WidgetData data = loadWidgetPref(ConfigurationActivity.this, mAppWidgetId);
            configuration = data == null ? new WidgetData() : data;
        }
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
        Context context = ConfigurationActivity.this;

        int id = item.getItemId();
        if (id == R.id.action_reset) {
            NotificationManager.showDialog(
                    context,
                    getString(R.string.menu_reset_title),
                    getString(R.string.menu_reset_confirmation),
                    null,
                    null,
                    getString(R.string.menu_reset),
                    resetListener,
                    getString(R.string.dialog_cancel),
                    cancelListener
            );

            return true;
        }

        if (id == R.id.action_settings) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.getPackageName(), null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*** Widget Preferences ***/

    static void saveWidgetPref(Context context, int appWidgetId, WidgetData data) {
        Contact contact = data.getContact();
        PaletteColor color = data.getColor();
        Uri photo = contact.getPhoto();

        String PREF_POSTFIX_KEY = "_" + appWidgetId;
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + KEY_WHATSAPP_ID + PREF_POSTFIX_KEY, contact.getWhatsAppId());
        prefs.putString(PREF_PREFIX_KEY + KEY_NAME + PREF_POSTFIX_KEY, contact.getName());
        prefs.putString(PREF_PREFIX_KEY + KEY_NUMBER + PREF_POSTFIX_KEY, contact.getNumber());
        prefs.putString(PREF_PREFIX_KEY + KEY_PHOTO_URI + PREF_POSTFIX_KEY, photo != null ? photo.toString() : null);
        prefs.putString(PREF_PREFIX_KEY + KEY_LABEL + PREF_POSTFIX_KEY, data.getLabel());
        prefs.putString(PREF_PREFIX_KEY + KEY_DESCRIPTION + PREF_POSTFIX_KEY, data.getDescription());
        prefs.putBoolean(PREF_PREFIX_KEY + KEY_OPTION_NUMBER + PREF_POSTFIX_KEY, data.isShouldUseNumber());
        prefs.putBoolean(PREF_PREFIX_KEY + KEY_OPTION_AVATAR + PREF_POSTFIX_KEY, data.isShouldUseAvatar());
        prefs.putBoolean(PREF_PREFIX_KEY + KEY_OPTION_LARGE_TEXT + PREF_POSTFIX_KEY, data.isShouldUseLargeText());
        prefs.putString(PREF_PREFIX_KEY + KEY_COLOR_NAME + PREF_POSTFIX_KEY, color.getName());
        prefs.putString(PREF_PREFIX_KEY + KEY_COLOR_HEX + PREF_POSTFIX_KEY, color.getHex());
        prefs.putBoolean(PREF_PREFIX_KEY + KEY_COLOR_IS_LIGHT + PREF_POSTFIX_KEY, color.isLightColor());
        prefs.apply();
    }

    static WidgetData loadWidgetPref(Context context, int appWidgetId) {
        WidgetData data = new WidgetData();

        String PREF_POSTFIX_KEY = "_" + appWidgetId;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);

        String id = prefs.getString(PREF_PREFIX_KEY + KEY_WHATSAPP_ID + PREF_POSTFIX_KEY, null);

        if (id == null) {
            return null;
        }

        String name = prefs.getString(PREF_PREFIX_KEY + KEY_NAME + PREF_POSTFIX_KEY, null);
        String number = prefs.getString(PREF_PREFIX_KEY + KEY_NUMBER + PREF_POSTFIX_KEY, null);
        String photoUri = prefs.getString(PREF_PREFIX_KEY + KEY_PHOTO_URI + PREF_POSTFIX_KEY, null);
        Uri photo = photoUri != null ? Uri.parse(photoUri) : null;
        String label = prefs.getString(PREF_PREFIX_KEY + KEY_LABEL + PREF_POSTFIX_KEY, null);
        String description = prefs.getString(PREF_PREFIX_KEY + KEY_DESCRIPTION + PREF_POSTFIX_KEY, null);
        boolean shouldUseNumber = prefs.getBoolean(PREF_PREFIX_KEY + KEY_OPTION_NUMBER + PREF_POSTFIX_KEY, false);
        boolean shouldUseAvatar = prefs.getBoolean(PREF_PREFIX_KEY + KEY_OPTION_AVATAR + PREF_POSTFIX_KEY, false);
        boolean shouldUseLargeText = prefs.getBoolean(PREF_PREFIX_KEY + KEY_OPTION_LARGE_TEXT + PREF_POSTFIX_KEY, false);
        String colorName = prefs.getString(PREF_PREFIX_KEY + KEY_COLOR_NAME + PREF_POSTFIX_KEY, null);
        String colorHex = prefs.getString(PREF_PREFIX_KEY + KEY_COLOR_HEX + PREF_POSTFIX_KEY, null);
        boolean isLightColor = prefs.getBoolean(PREF_PREFIX_KEY + KEY_COLOR_IS_LIGHT + PREF_POSTFIX_KEY, false);

        // Create contact
        Contact contact = new Contact(id, name, number, photo);
        data.setContact(contact);

        // Create options
        data.setLabel(label);
        data.setDescription(description);
        data.setShouldUseNumber(shouldUseNumber);
        data.setShouldUseAvatar(shouldUseAvatar);
        data.setShouldUseLargeText(shouldUseLargeText);

        // Create color
        PaletteColor color = new PaletteColor(colorName, colorHex, isLightColor ? PaletteColor.ColorType.LIGHT : PaletteColor.ColorType.DARK);
        data.setColor(color);

        return data;
    }

    static void deleteWidgetPref(Context context, int appWidgetId) {
        String PREF_POSTFIX_KEY = "_" + appWidgetId;
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + KEY_WHATSAPP_ID + PREF_POSTFIX_KEY);
        prefs.remove(PREF_PREFIX_KEY + KEY_NAME + PREF_POSTFIX_KEY);
        prefs.remove(PREF_PREFIX_KEY + KEY_NUMBER + PREF_POSTFIX_KEY);
        prefs.remove(PREF_PREFIX_KEY + KEY_PHOTO_URI + PREF_POSTFIX_KEY);
        prefs.remove(PREF_PREFIX_KEY + KEY_LABEL + PREF_POSTFIX_KEY);
        prefs.remove(PREF_PREFIX_KEY + KEY_DESCRIPTION + PREF_POSTFIX_KEY);
        prefs.remove(PREF_PREFIX_KEY + KEY_OPTION_NUMBER + PREF_POSTFIX_KEY);
        prefs.remove(PREF_PREFIX_KEY + KEY_OPTION_AVATAR + PREF_POSTFIX_KEY);
        prefs.remove(PREF_PREFIX_KEY + KEY_OPTION_LARGE_TEXT + PREF_POSTFIX_KEY);
        prefs.remove(PREF_PREFIX_KEY + KEY_COLOR_NAME + PREF_POSTFIX_KEY);
        prefs.remove(PREF_PREFIX_KEY + KEY_COLOR_HEX + PREF_POSTFIX_KEY);
        prefs.remove(PREF_PREFIX_KEY + KEY_COLOR_IS_LIGHT + PREF_POSTFIX_KEY);
        prefs.apply();
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