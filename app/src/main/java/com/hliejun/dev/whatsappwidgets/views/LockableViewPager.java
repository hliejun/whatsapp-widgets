package com.hliejun.dev.whatsappwidgets.views;

import android.support.v4.view.ViewPager;

import android.content.Context;

import android.util.AttributeSet;

import android.view.MotionEvent;

public class LockableViewPager extends ViewPager {

    private boolean isSwipeEnabled;

    public LockableViewPager(Context context) {
        super(context);
    }

    public LockableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isSwipeEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isSwipeEnabled && super.onTouchEvent(event);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isSwipeEnabled && super.onInterceptTouchEvent(event);
    }

    /*** Interface ***/

    public void setSwipeable(boolean swipeable) {
        this.isSwipeEnabled = swipeable;
    }

    public void incrementPage() {
        int page = getCurrentItem();
        if (page < getAdapter().getCount() - 1) {
            setCurrentItem(page + 1, true);
        }
    }

    public void decrementPage() {
        int page = getCurrentItem();
        if (page > 0) {
            setCurrentItem(page - 1, true);
        }
    }

}
