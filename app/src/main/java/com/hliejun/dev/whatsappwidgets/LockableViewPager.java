package com.hliejun.dev.whatsappwidgets;

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
        if (this.isSwipeEnabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.isSwipeEnabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setSwipeable(boolean swipeable) {
        this.isSwipeEnabled = swipeable;
    }

}
