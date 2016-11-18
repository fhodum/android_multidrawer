package com.multidrawer.fhodum.multidrawer;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

/**
 * Created by fhodum on 5/11/16.
 */
public class TopBottomController implements IDrawerController {

    private WeakReference<MultiDrawerView> myParentView = null;
    private HorizontalScrollView buttonScrollView = null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopBottomController(MultiDrawerView parentView, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes ){
        myParentView = new WeakReference<>(parentView);
        buttonScrollView = new HorizontalOnlyCustomScrollView(context, attrs);
        buttonScrollView.setId(View.generateViewId());
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
           parentView.buttonLinearLayout = new LinearLayout(context, attrs, defStyleAttr, defStyleRes);
        }else{
            parentView.buttonLinearLayout = new LinearLayout(context, attrs, defStyleAttr);
        }
        parentView.buttonLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonScrollView.addView(parentView.buttonLinearLayout, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        parentView.buttonScrollView = this.buttonScrollView;


        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            parentView.bodyLayout  = new LinearLayout(context, attrs,defStyleAttr,defStyleRes);
        } else{
            parentView.bodyLayout  = new LinearLayout(context, attrs,defStyleAttr);
        }


        if(parentView.side == MultiDrawerView.BOTTOM) {
            RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL|RelativeLayout.ALIGN_PARENT_BOTTOM);
            parentView.addView(buttonScrollView, layoutParams);

            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.BELOW, buttonScrollView.getId());
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            parentView.bodyLayout.setLayoutParams(layoutParams);
            parentView.addView(parentView.bodyLayout);
        }else if (parentView.side == MultiDrawerView.TOP) {
            RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            parentView.addView(buttonScrollView, layoutParams);

            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ABOVE, buttonScrollView.getId());
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            parentView.bodyLayout.setLayoutParams(layoutParams);
            parentView.addView(myParentView.get().bodyLayout, layoutParams);
        }

        ViewTreeObserver viewTreeObserver = parentView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    myParentView.get().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if(myParentView.get().side == MultiDrawerView.BOTTOM){
                        myParentView.get().bodyLayout.setY(myParentView.get().getHeight());
                        buttonScrollView.setY( myParentView.get().getHeight() - buttonScrollView.getHeight());
                    }else {
                        myParentView.get().bodyLayout.setY(-myParentView.get().bodyLayout.getHeight());
                        buttonScrollView.setY(0);
                    }
                }
            });
        }
    }

    @Override
    public GestureDetector.SimpleOnGestureListener getGestureDetector(View gestureView) {
        return new TopBottomGestureDetector(gestureView);
    }

    void openDrawer() {
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
            if (multiDrawerView.side == MultiDrawerView.BOTTOM) {

                ObjectAnimator.ofFloat(multiDrawerView.bodyLayout, "y", multiDrawerView.bodyLayout.getY(), multiDrawerView.buttonScrollView.getHeight())
                        .setDuration(multiDrawerView.animationOpenCloseTime)
                        .start();
                ObjectAnimator animator = ObjectAnimator.ofFloat(multiDrawerView.buttonScrollView, "y", multiDrawerView.buttonScrollView.getY(), 0)
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
                ObjectAnimator.ofFloat(multiDrawerView.bodyLayout, "y", -multiDrawerView.bodyLayout.getHeight(), 0)
                        .setDuration(multiDrawerView.animationOpenCloseTime)
                        .start();
                ObjectAnimator animator = ObjectAnimator.ofFloat(multiDrawerView.buttonScrollView, "y", 0, multiDrawerView.getHeight() - multiDrawerView.buttonScrollView.getHeight())
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
            multiDrawerView.lastClickedButton.setActivated(true);
            if (multiDrawerView.side == MultiDrawerView.BOTTOM) {

                ObjectAnimator.ofFloat(multiDrawerView.bodyLayout, "y", multiDrawerView.bodyLayout.getY(), multiDrawerView.bodyLayout.getY() + multiDrawerView.bodyLayout.getHeight())
                        .setDuration(multiDrawerView.animationOpenCloseTime)
                        .start();
                ObjectAnimator animator = ObjectAnimator.ofFloat(multiDrawerView.buttonScrollView, "y", multiDrawerView.buttonScrollView.getY(), multiDrawerView.bodyLayout.getHeight())
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
            } else {
                ObjectAnimator.ofFloat(multiDrawerView.bodyLayout, "y", 0, -multiDrawerView.bodyLayout.getHeight())
                        .setDuration(multiDrawerView.animationOpenCloseTime)
                        .start();
                ObjectAnimator animator = ObjectAnimator.ofFloat(multiDrawerView.buttonScrollView, "y", multiDrawerView.buttonScrollView.getY(), 0)
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

    private class TopBottomGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        private WeakReference<View> myView = null;

        public TopBottomGestureDetector(View ownView){
            myView = new WeakReference<>(ownView);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (myParentView.get() != null) {
                final MultiDrawerView multiDrawerView = myParentView.get();
                System.out.println(" in onFling() :: ");
                if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH) {
                    return false;
                }
                View oldLastClicked = multiDrawerView.lastClickedButton;
                if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {

                    multiDrawerView.lastClickedButton = myView.get();
                    if (!multiDrawerView.isDrawerOpen && multiDrawerView.side == MultiDrawerView.BOTTOM) {
                        openDrawer();
                    } else if (multiDrawerView.isDrawerOpen && multiDrawerView.side == MultiDrawerView.TOP) {
                        closeDrawer();
                        multiDrawerView.bodyLayout.removeAllViews();
                        multiDrawerView.bodyLayout.addView(multiDrawerView.buttonToBodyView.get(multiDrawerView.lastClickedButton));
                        if (!myView.get().equals(oldLastClicked)) {
                            ((View) oldLastClicked.getParent()).setBackground(null);
                        }
                    }

                } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    multiDrawerView.lastClickedButton = myView.get();
                    if (!multiDrawerView.isDrawerOpen && multiDrawerView.side == MultiDrawerView.TOP) {
                        openDrawer();
                    } else if (multiDrawerView.isDrawerOpen && multiDrawerView.side == MultiDrawerView.BOTTOM) {
                        closeDrawer();
                        multiDrawerView.bodyLayout.removeAllViews();
                        multiDrawerView.bodyLayout.addView(multiDrawerView.buttonToBodyView.get(multiDrawerView.lastClickedButton));
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

        @Override
        public boolean onScroll( MotionEvent e1, MotionEvent e2, float dX, float dY){
            return false;
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

                Drawer drawer = ((Drawer) ((View) multiDrawerView.lastClickedButton.getParent()).getTag());
                Drawer.DrawerCallbacks notifyClosed = drawer.getDrawerCallbackHandler();
                if (notifyClosed != null) {
                    notifyClosed.drawerOpened(drawer);
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
                Drawer.DrawerCallbacks notifyOpen = drawer.getDrawerCallbackHandler();
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

    public Animator getButtonRemovalAnimator(final Drawer drawer) {
        ValueAnimator va = null;
        if (myParentView.get() != null) {
            final MultiDrawerView multiDrawerView = myParentView.get();

            View pv = (View) drawer.getButton().getParent();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pv.getWidth(), pv.getHeight());
            pv.setLayoutParams(params);
            va = ValueAnimator.ofInt(pv.getWidth(), 0);
            va.setDuration(200);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    ((View) drawer.getButton().getParent()).getLayoutParams().width = value.intValue();
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

}
