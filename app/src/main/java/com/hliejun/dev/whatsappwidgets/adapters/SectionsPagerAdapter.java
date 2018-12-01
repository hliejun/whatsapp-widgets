package com.hliejun.dev.whatsappwidgets.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.view.ViewGroup;

import com.hliejun.dev.whatsappwidgets.R;
import com.hliejun.dev.whatsappwidgets.fragments.SectionFragment;
import com.hliejun.dev.whatsappwidgets.fragments.SplashFragment;
import com.hliejun.dev.whatsappwidgets.fragments.ContactFragment;
import com.hliejun.dev.whatsappwidgets.fragments.OptionsFragment;
import com.hliejun.dev.whatsappwidgets.fragments.StylingFragment;
import com.hliejun.dev.whatsappwidgets.fragments.ExampleFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private int[] sections;
    private SectionFragment mCurrFragment;

    public SectionsPagerAdapter(FragmentManager manager, int[] sections) {
        super(manager);
        this.sections = sections;
    }

    @Override
    public Fragment getItem(int position) {
        int layoutId = sections[position];
        switch(layoutId) {
            case R.layout.fragment_splash:
                return SplashFragment.newInstance(position + 1);
            case R.layout.fragment_contact:
                return ContactFragment.newInstance(position + 1);
            case R.layout.fragment_options:
                return OptionsFragment.newInstance(position + 1);
            case R.layout.fragment_styling:
                return StylingFragment.newInstance(position + 1);
            default:
                return ExampleFragment.newInstance(position + 1);
        }
    }

    @Override
    public int getCount() {
        return sections.length;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (getCurrentFragment() != object) {
            mCurrFragment = (SectionFragment) object;
        }
    }

    public SectionFragment getCurrentFragment() {
        return mCurrFragment;
    }

    public void reloadFragments() {
        for (int index = 0; index < sections.length; index++) {
            SectionFragment fragment = (SectionFragment) getItem(index);
            if (fragment != null) {
                fragment.reset();
            }
        }
    }

}