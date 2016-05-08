package com.multidrawer.fhodum.multidrawer;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.EventLog;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class TopBottomMultiDrawerView extends MultiDrawerBase {

    private HorizontalScrollView buttonScrollView;

    private int side = 1;

    private static final int TOP = 1;
    private static final int BOTTOM = 0;


    public TopBottomMultiDrawerView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public TopBottomMultiDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, R.attr.topBottomMultiDrawerViewStyle, 0);
    }

    public TopBottomMultiDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, R.attr.topBottomMultiDrawerViewStyle);

        init(context, attrs, R.attr.topBottomMultiDrawerViewStyle, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopBottomMultiDrawerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, R.attr.topBottomMultiDrawerViewStyle, defStyleRes);

        init(context, attrs, R.attr.topBottomMultiDrawerViewStyle, defStyleRes);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super.init(context,attrs,defStyleAttr,defStyleRes);

        TypedArray b = context.obtainStyledAttributes(attrs, R.styleable.TopBottomMultiDrawerView, defStyleAttr, defStyleRes);

        //Get left or right from configuration set. For right now, assume right
        side  = b.getInteger(R.styleable.TopBottomMultiDrawerView_topBottomDrawerSide,0);
        System.out.println("Side: " + side);



        buttonScrollView = new HorizontalOnlyCustomScrollView(context, attrs);//requestLayout();
        buttonScrollView.setId(View.generateViewId());
        buttonScrollView.setBackgroundColor(Color.TRANSPARENT);
        buttonScrollView.setFadingEdgeLength(60);
        buttonScrollView.setVerticalFadingEdgeEnabled(true);



        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            buttonLinearLayout = new LinearLayout(context, attrs, defStyleAttr, defStyleRes);
        }else{
            buttonLinearLayout = new LinearLayout(context, attrs, defStyleAttr);
        }
        buttonLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonScrollView.addView(buttonLinearLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));


        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            bodyLayout  = new LinearLayout(context, attrs,defStyleAttr,defStyleRes);
        } else{
            bodyLayout  = new LinearLayout(context, attrs,defStyleAttr);
        }


        if(side == BOTTOM) {
            LayoutParams layoutParams =new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL|RelativeLayout.ALIGN_PARENT_TOP);
            addView(buttonScrollView, layoutParams);

            layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.BELOW, buttonScrollView.getId());
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            bodyLayout.setLayoutParams(layoutParams);
            addView(bodyLayout);
        }else if (side == TOP) {
            LayoutParams layoutParams =new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            addView(buttonScrollView, layoutParams);

            layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ABOVE, buttonScrollView.getId());
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            bodyLayout.setLayoutParams(layoutParams);
            addView(bodyLayout);
        }

        ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    TopBottomMultiDrawerView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if(side == BOTTOM){
                        bodyLayout.setY(getHeight());
                        buttonScrollView.setY( getHeight() - buttonScrollView.getHeight());
                    }else {
                        bodyLayout.setY(-bodyLayout.getHeight());
                        buttonScrollView.setY(0);
                    }
                }
            });
        }
    }

    @Override
    protected GestureDetector.SimpleOnGestureListener getGestureDetector(View vw) {
        return new MyGestureDetector(vw);
    }


    @Override
    public boolean removeDrawer(Drawer drawer) {
        return false;
    }


    private void openDrawer() {
        int colorFrom = buttonDeSelectedBackgroundColor;
        int colorTo = buttonSelectedBackgroundColor;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(animationOpenCloseTime -50); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                ((View) lastClickedButton.getParent()).setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();


        bodyLayout.addView(buttonToBodyView.get(lastClickedButton));
        Drawer drawer =  ((Drawer)((View) lastClickedButton.getParent()).getTag());
        Drawer.DrawerCallbacks notifyOpen =drawer.getDrawerCallbackHandler();
        if(notifyOpen != null){
            notifyOpen.drawerOpened(drawer);
        }
        isAnimating = true;
        lastClickedButton.setActivated(true);
        if(side == BOTTOM) {

            ObjectAnimator.ofFloat(bodyLayout, "y", bodyLayout.getY(), buttonScrollView.getHeight())
                    .setDuration(animationOpenCloseTime)
                    .start();
            ObjectAnimator animator = ObjectAnimator.ofFloat(buttonScrollView, "y", buttonScrollView.getY(), 0)
                    .setDuration(animationOpenCloseTime);

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isDrawerOpen = true;
                    isAnimating = false;

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
            ObjectAnimator.ofFloat(bodyLayout, "y", -bodyLayout.getHeight(),0)
                    .setDuration(animationOpenCloseTime)
                    .start();
            ObjectAnimator animator = ObjectAnimator.ofFloat(buttonScrollView, "y", 0,  getHeight()-buttonScrollView.getHeight())
                    .setDuration(animationOpenCloseTime);

            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isDrawerOpen = true;
                    isAnimating = false;
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
    protected void closeDrawer(){
        isAnimating = true;
        int colorFrom = buttonSelectedBackgroundColor;
        int colorTo = buttonDeSelectedBackgroundColor;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(animationOpenCloseTime - 50); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                ((View) lastClickedButton.getParent()).setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
        lastClickedButton.setActivated(true);
        if(side == BOTTOM) {

            ObjectAnimator.ofFloat(bodyLayout, "y", bodyLayout.getY(), bodyLayout.getY() + bodyLayout.getHeight())
                    .setDuration(animationOpenCloseTime)
                    .start();
            ObjectAnimator animator = ObjectAnimator.ofFloat(buttonScrollView, "y", buttonScrollView.getY(), bodyLayout.getHeight())
                    .setDuration(animationOpenCloseTime);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isDrawerOpen = false;
                    isAnimating = false;
                    Drawer drawer = ((Drawer) ((View) lastClickedButton.getParent()).getTag());
                    Drawer.DrawerCallbacks notifyClosed = drawer.getDrawerCallbackHandler();
                    if (notifyClosed != null) {
                        notifyClosed.drawerClosed(drawer);
                    }
                    bodyLayout.removeAllViews();
                    lastClickedButton.setActivated(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        }else{
            ObjectAnimator.ofFloat(bodyLayout, "y", 0, -bodyLayout.getHeight())
                    .setDuration(animationOpenCloseTime)
                    .start();
            ObjectAnimator animator = ObjectAnimator.ofFloat(buttonScrollView, "y", buttonScrollView.getY(), 0)
                    .setDuration(animationOpenCloseTime);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isDrawerOpen = false;
                    isAnimating = false;
                    Drawer drawer = ((Drawer) ((View) lastClickedButton.getParent()).getTag());
                    Drawer.DrawerCallbacks notifyClosed = drawer.getDrawerCallbackHandler();
                    if (notifyClosed != null) {
                        notifyClosed.drawerClosed(drawer);
                    }
                    bodyLayout.removeAllViews();
                    lastClickedButton.setActivated(false);
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

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        private WeakReference<View> myView = null;

        public MyGestureDetector(View ownView){
            myView = new WeakReference<>(ownView);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            System.out.println(" in onFling() :: ");
            if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH) {
                return false;
            }
            View oldLastClicked = lastClickedButton;
            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {

                lastClickedButton = myView.get();
                if(!isDrawerOpen && side==BOTTOM){
                    openDrawer();
                }else if(isDrawerOpen && side == TOP){
                    closeDrawer();
                    bodyLayout.removeAllViews();
                    bodyLayout.addView(buttonToBodyView.get(lastClickedButton));
                    if(!myView.get().equals(oldLastClicked)){
                        ((View)oldLastClicked.getParent()).setBackground(null);
                    }
                }

            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                lastClickedButton = myView.get();
                if(!isDrawerOpen && side==TOP){
                    openDrawer();
                }else if(isDrawerOpen && side == BOTTOM){
                    closeDrawer();
                    bodyLayout.removeAllViews();
                    bodyLayout.addView(buttonToBodyView.get(lastClickedButton));
                    if(!myView.get().equals(oldLastClicked)){
                        ((View)oldLastClicked.getParent()).setBackground(null);
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

    protected void handleSingleTap(View myView){
        final View oldLastClickedButton = lastClickedButton;

        lastClickedButton = myView;


        if(isDrawerOpen && oldLastClickedButton != lastClickedButton && !isAnimating){  //Changing open tabs
            lastClickedButton.setActivated(true);
            oldLastClickedButton.setActivated(false);

            Drawer drawer = ((Drawer) ((View) lastClickedButton.getParent()).getTag());
            Drawer.DrawerCallbacks notifyClosed = drawer.getDrawerCallbackHandler();
            if(notifyClosed  != null){
                notifyClosed.drawerOpened(drawer);
            }

            int colorFrom = buttonSelectedBackgroundColor;
            int colorTo = buttonDeSelectedBackgroundColor;
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(deselectedButtonFadeTime); // milliseconds
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    ((View)oldLastClickedButton.getParent()).setBackgroundColor((int) animator.getAnimatedValue());
                }

            });
            colorAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ((View)oldLastClickedButton.getParent()).setBackgroundColor(buttonDeSelectedBackgroundColor);

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            colorAnimation.start();
            bodyLayout.removeAllViews();
            bodyLayout.addView(buttonToBodyView.get(lastClickedButton));
            Drawer drawerOpen = ((Drawer)((View) lastClickedButton.getParent()).getTag());
            Drawer.DrawerCallbacks notifyOpen = drawer.getDrawerCallbackHandler();
            if(notifyOpen != null){
                notifyOpen.drawerOpened(drawerOpen);
            }
            ((View) lastClickedButton.getParent()).setBackgroundColor(buttonSelectedBackgroundColor);

        } else if (isDrawerOpen == true && !isAnimating) {  //Closing drawer
            closeDrawer();

        } else if(isDrawerOpen == false && !isAnimating) {  //Opening Drawer

            openDrawer();
        }
    }

}