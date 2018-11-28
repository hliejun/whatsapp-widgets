package com.hliejun.dev.whatsappwidgets.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hliejun.dev.whatsappwidgets.R;
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
}