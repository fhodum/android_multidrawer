package com.multidrawer.fhodum.multidrawer;

import android.animation.Animator;
import android.view.GestureDetector;
import android.view.View;

/**
 * Created by fhodum on 5/11/16.
 */
public interface IDrawerController {

    GestureDetector.SimpleOnGestureListener getGestureDetector(View gestureView);

    void handleSingleTap(View myView);

    Animator getButtonRemovalAnimator(Drawer drawer);

    void closeDrawer();

}
