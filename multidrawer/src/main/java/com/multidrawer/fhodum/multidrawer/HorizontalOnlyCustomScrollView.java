package com.multidrawer.fhodum.multidrawer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by fhodum on 5/6/16.
 */
public class HorizontalOnlyCustomScrollView extends HorizontalScrollView{
    private GestureDetector gestureDetector;


    public HorizontalOnlyCustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new XScrollDetector());
        setFadingEdgeLength(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //Call super first because it does some hidden motion event handling
        boolean result = super.onInterceptTouchEvent(ev);
        //Now see if we are scrolling vertically with the custom gesture detector
        if (gestureDetector.onTouchEvent(ev)) {
            return result;
        }
        //If not scrolling vertically (more y than x), don't hijack the event.
        else {
            return false;
        }
    }

    // Return false if we're scrolling in the y direction
    class XScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float     distanceY) {
            try {
                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }
}
