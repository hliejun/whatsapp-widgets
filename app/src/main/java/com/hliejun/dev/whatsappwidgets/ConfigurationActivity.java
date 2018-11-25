package com.hliejun.dev.whatsappwidgets;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class ConfigurationActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

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

                mNavTray.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                mNextButton.setVisibility(position == numOfSections - 1 ? View.GONE : View.VISIBLE);
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

                // TODO: Complete config and create widget with acquired fields

            }
        });

        mDotsIndicator.setViewPager(mViewPager);
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

                                // TODO: Start button listener to perform checks and notify

                                int page = mViewPager.getCurrentItem();
                                page += 1;
                                mViewPager.setCurrentItem(page, true);
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
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return numOfSections;
        }
    }
}