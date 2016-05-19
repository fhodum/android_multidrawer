package com.multidrawer.fhodum.multidrawer;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;

import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

/**
 * Created by fhodum on 5/11/16.
 */
public class LeftRightController implements IDrawerController{
    private WeakReference<MultiDrawerView> myParentView = null;

    private VerticalOnlyCustomScrollView internalButtonScrollView = null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LeftRightController(MultiDrawerView parentView, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        myParentView = new WeakReference<>(parentView);
        internalButtonScrollView = new VerticalOnlyCustomScrollView(context, attrs);

        internalButtonScrollView = new VerticalOnlyCustomScrollView(context, attrs);//requestLayout();
        internalButtonScrollView.setId(View.generateViewId());
        internalButtonScrollView.setBackgroundColor(Color.TRANSPARENT);
        internalButtonScrollView.setFadingEdgeLength(60);
        internalButtonScrollView.setVerticalFadingEdgeEnabled(true);
        //Move scrollbar to left hand side if drawer is on left
        if(myParentView.get().side == MultiDrawerView.LEFT){
            internalButtonScrollView.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        }
        parentView.buttonScrollView = this.internalButtonScrollView;

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            parentView.buttonLinearLayout = new LinearLayout(context, attrs, defStyleAttr, defStyleRes);
        }else{
            parentView.buttonLinearLayout = new LinearLayout(context, attrs, defStyleAttr);
        }

        parentView.buttonLinearLayout.setOrientation(LinearLayout.VERTICAL);
        internalButtonScrollView.addView(parentView.buttonLinearLayout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            parentView.bodyLayout = new LinearLayout(context, attrs, defStyleAttr, defStyleRes);
        }else {
            parentView.bodyLayout = new LinearLayout(context, attrs, defStyleAttr);
        }

        if(parentView.side == MultiDrawerView.RIGHT) {
            RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            parentView.addView(internalButtonScrollView, layoutParams);

            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.RIGHT_OF, internalButtonScrollView.getId());
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            parentView.bodyLayout.setLayoutParams(layoutParams);
            parentView.addView(parentView.bodyLayout,layoutParams);
        }else if (parentView.side == MultiDrawerView.LEFT) {
            RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            parentView.addView(internalButtonScrollView, layoutParams);

            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.LEFT_OF, internalButtonScrollView.getId());

            parentView.bodyLayout.setLayoutParams(layoutParams);
            parentView.addView(parentView.bodyLayout);
        }

        ViewTreeObserver viewTreeObserver = parentView.getViewTreeObserver();
        if (viewTreeObserver.isAlive() && myParentView.get() != null) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    myParentView.get().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if(myParentView.get().side == MultiDrawerView.RIGHT){
                        myParentView.get().bodyLayout.setX(internalButtonScrollView.getWidth() + myParentView.get().bodyLayout.getWidth());
                        internalButtonScrollView.setX( myParentView.get().bodyLayout.getWidth());
                    }else {
                        myParentView.get().bodyLayout.setX(-myParentView.get().bodyLayout.getX());
                        internalButtonScrollView.setX(0);
                    }
                }
            });
        }
    }

    public GestureDetector.SimpleOnGestureListener getGestureDetector(View gestureView){
        return new LeftRightGestureListener(gestureView);
    }

    public ViewGroup getButtonScrollView(){
        return internalButtonScrollView;
    }

    private class LeftRightGestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        private WeakReference<View> myView = null;


        public LeftRightGestureListener(View ownView) {

            myView = new WeakReference<>(ownView);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            System.out.println(" in onFling() :: ");
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                return false;
            }
            if (myParentView.get() != null) {
                MultiDrawerView drawer = myParentView.get();

                View oldLastClicked = drawer.lastClickedButton;
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                    drawer.lastClickedButton = myView.get();
                    if (!drawer.isDrawerOpen && drawer.side == MultiDrawerView.RIGHT) {
                        openDrawer();
                    } else if (drawer.isDrawerOpen && drawer.side == MultiDrawerView.LEFT) {
                        closeDrawer();
                        drawer.bodyLayout.removeAllViews();
                        drawer.bodyLayout.addView(drawer.buttonToBodyView.get(drawer.lastClickedButton));
                        if (!myView.get().equals(oldLastClicked)) {
                            ((View) oldLastClicked.getParent()).setBackground(null);
                        }
                    }

                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    drawer.lastClickedButton = myView.get();
                    if (!drawer.isDrawerOpen && drawer.side == MultiDrawerView.LEFT) {
                        openDrawer();
                    } else if (drawer.isDrawerOpen && drawer.side == MultiDrawerView.RIGHT) {
                        closeDrawer();
                        drawer.bodyLayout.removeAllViews();
                        drawer.bodyLayout.addView(drawer.buttonToBodyView.get(drawer.lastClickedButton));
                        if (!myView.get().equals(oldLastClicked)) {
                            ((View) oldLastClicked.getParent()).setBackground(null);
                        }
                    }
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {

            handleSingleTap(myView.get());
            return true;
        }
    }

    public void handleSingleTap(View myView) {
        if (myParentView.get() != null) {
            final MultiDrawerView multiDrawerView = myParentView.get();

            final View oldLastClickedButton = multiDrawerView.lastClickedButton;
            multiDrawerView.lastClickedButton = myView;

            if (multiDrawerView.isDrawerOpen && oldLastClickedButton != multiDrawerView.lastClickedButton && !multiDrawerView.isAnimating) {  //Changing open tabs
                multiDrawerView.lastClickedButton.setActivated(true);
                oldLastClickedButton.setActivated(false);
                Drawer drawer = ((Drawer) ((View) oldLastClickedButton.getParent()).getTag());
                Drawer.DrawerCallbacks notifyClosed = drawer.getDrawerCallbackHandler();
                if (notifyClosed != null) {
                    notifyClosed.drawerClosed(drawer);
                }

                int colorFrom = multiDrawerView.buttonSelectedBackgroundColor;
                int colorTo = multiDrawerView.buttonDeSelectedBackgroundColor;
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(multiDrawerView.deselectedButtonFadeTime); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        ((View) oldLastClickedButton.getParent()).setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ((View) oldLastClickedButton.getParent()).setBackgroundColor(multiDrawerView.buttonDeSelectedBackgroundColor);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                colorAnimation.start();
                multiDrawerView.bodyLayout.removeAllViews();
                multiDrawerView.bodyLayout.addView(multiDrawerView.buttonToBodyView.get(multiDrawerView.lastClickedButton));
                Drawer drawerOpen = ((Drawer) ((View) multiDrawerView.lastClickedButton.getParent()).getTag());
                Drawer.DrawerCallbacks notifyOpen = drawerOpen.getDrawerCallbackHandler();
                if (notifyOpen != null) {
                    notifyOpen.drawerOpened(drawerOpen);
                }
                ((View) multiDrawerView.lastClickedButton.getParent()).setBackgroundColor(multiDrawerView.buttonSelectedBackgroundColor);

            } else if (multiDrawerView.isDrawerOpen == true && !multiDrawerView.isAnimating) {  //Closing drawer
                closeDrawer();

            } else if (multiDrawerView.isDrawerOpen == false && !multiDrawerView.isAnimating) {  //Opening Drawer

                openDrawer();
            }
        }
    }

    @Override
    public Animator getButtonRemovalAnimator(final Drawer drawer) {

        ValueAnimator va = null;
        if(myParentView.get() != null) {
            final MultiDrawerView multiDrawerView = myParentView.get();

            View pv = (View) drawer.getButton().getParent();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pv.getWidth(), pv.getHeight());
            pv.setLayoutParams(params);
            va = ValueAnimator.ofInt(pv.getHeight(), 0);
            va.setDuration(200);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    ((View) drawer.getButton().getParent()).getLayoutParams().height = value.intValue();
                    ((View) drawer.getButton().getParent()).requestLayout();
                }
            });
            va.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    multiDrawerView.completeRemoveDrawer(drawer);
                    multiDrawerView.isAnimating = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return va;
    }



    private void openDrawer() {
        if (myParentView.get() != null) {
            final MultiDrawerView multiDrawerView = myParentView.get();
            int colorFrom = multiDrawerView.buttonDeSelectedBackgroundColor;
            int colorTo = multiDrawerView.buttonSelectedBackgroundColor;
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(multiDrawerView.animationOpenCloseTime - 50); // milliseconds
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    ((View) multiDrawerView.lastClickedButton.getParent()).setBackgroundColor((int) animator.getAnimatedValue());
                }

            });
            colorAnimation.start();


            multiDrawerView.bodyLayout.addView(multiDrawerView.buttonToBodyView.get(multiDrawerView.lastClickedButton));
            Drawer drawer = ((Drawer) ((View) multiDrawerView.lastClickedButton.getParent()).getTag());
            Drawer.DrawerCallbacks notifyOpen = drawer.getDrawerCallbackHandler();
            if (notifyOpen != null) {
                notifyOpen.drawerOpened(drawer);
            }
            multiDrawerView.isAnimating = true;
            multiDrawerView.lastClickedButton.setActivated(true);
            if (multiDrawerView.side == multiDrawerView.RIGHT) {

                ObjectAnimator.ofFloat(multiDrawerView.bodyLayout, "x", multiDrawerView.bodyLayout.getX(), multiDrawerView.buttonScrollView.getWidth())
                        .setDuration(multiDrawerView.animationOpenCloseTime)
                        .start();
                ObjectAnimator animator = ObjectAnimator.ofFloat(multiDrawerView.buttonScrollView, "x", multiDrawerView.buttonScrollView.getX(),
                        0)
                        .setDuration(multiDrawerView.animationOpenCloseTime);

                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        multiDrawerView.isDrawerOpen = true;
                        multiDrawerView.isAnimating = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
            } else {
                ObjectAnimator.ofFloat(multiDrawerView.bodyLayout, "x", -multiDrawerView.bodyLayout.getWidth(), 0)
                        .setDuration(multiDrawerView.animationOpenCloseTime)
                        .start();
                ObjectAnimator animator = ObjectAnimator.ofFloat(multiDrawerView.buttonScrollView, "x", 0, multiDrawerView.bodyLayout.getWidth())
                        .setDuration(multiDrawerView.animationOpenCloseTime);

                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        multiDrawerView.isDrawerOpen = true;
                        multiDrawerView.isAnimating = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
            }
        }
    }

    public void closeDrawer() {
        if (myParentView.get() != null) {
            final MultiDrawerView multiDrawerView = myParentView.get();
            multiDrawerView.isAnimating = true;
            int colorFrom = multiDrawerView.buttonSelectedBackgroundColor;
            int colorTo = multiDrawerView.buttonDeSelectedBackgroundColor;
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(multiDrawerView.animationOpenCloseTime - 50); // milliseconds
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    ((View) multiDrawerView.lastClickedButton.getParent()).setBackgroundColor((int) animator.getAnimatedValue());
                }

            });
            colorAnimation.start();

            if (multiDrawerView.side == multiDrawerView.RIGHT) {

                ObjectAnimator.ofFloat(multiDrawerView.bodyLayout, "x", multiDrawerView.bodyLayout.getX(), multiDrawerView.bodyLayout.getX() + multiDrawerView.bodyLayout.getWidth())
                        .setDuration(multiDrawerView.animationOpenCloseTime)
                        .start();
                ObjectAnimator animator = ObjectAnimator.ofFloat(multiDrawerView.buttonScrollView, "x", multiDrawerView.buttonScrollView.getX(), multiDrawerView.bodyLayout.getWidth())
                        .setDuration(multiDrawerView.animationOpenCloseTime);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        multiDrawerView.isDrawerOpen = false;
                        multiDrawerView.isAnimating = false;
//                    ((View) lastClickedButton.getParent()).setBackground(null);
                        Drawer drawer = (Drawer) ((View) multiDrawerView.lastClickedButton.getParent()).getTag();
                        Drawer.DrawerCallbacks notifyClosed = drawer.getDrawerCallbackHandler();
                        if (notifyClosed != null) {
                            notifyClosed.drawerClosed(drawer);
                        }
                        multiDrawerView.bodyLayout.removeAllViews();
                        multiDrawerView.lastClickedButton.setActivated(false);
                        multiDrawerView.processQueuedRemovals();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
            } else {
                ObjectAnimator.ofFloat(multiDrawerView.bodyLayout, "x", 0, -multiDrawerView.buttonScrollView.getX())
                        .setDuration(multiDrawerView.animationOpenCloseTime)
                        .start();
                ObjectAnimator animator = ObjectAnimator.ofFloat(multiDrawerView.buttonScrollView, "x", multiDrawerView.buttonScrollView.getX(), 0)
                        .setDuration(multiDrawerView.animationOpenCloseTime);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        multiDrawerView.isDrawerOpen = false;
                        multiDrawerView.isAnimating = false;
                        Drawer drawer = ((Drawer) ((View) multiDrawerView.lastClickedButton.getParent()).getTag());
                        Drawer.DrawerCallbacks notifyClosed = drawer.getDrawerCallbackHandler();
                        if (notifyClosed != null) {
                            notifyClosed.drawerClosed(drawer);
                        }
                        multiDrawerView.bodyLayout.removeAllViews();
                        multiDrawerView.lastClickedButton.setActivated(false);
                        multiDrawerView.processQueuedRemovals();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
            }
        }
    }
}