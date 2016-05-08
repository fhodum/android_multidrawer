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
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.lang.ref.WeakReference;


public class LeftRightMultiDrawerView extends MultiDrawerBase {

    private ScrollView buttonScrollView;

    private int side = 1;

    private static final int RIGHT = 1;
    private static final int LEFT = 0;



    public LeftRightMultiDrawerView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public LeftRightMultiDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, R.attr.leftRightMultiDrawerViewStyle, 0);
    }

    public LeftRightMultiDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, R.attr.leftRightMultiDrawerViewStyle);

        init(context, attrs, R.attr.leftRightMultiDrawerViewStyle, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LeftRightMultiDrawerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, R.attr.leftRightMultiDrawerViewStyle, defStyleRes);

        init(context, attrs, R.attr.leftRightMultiDrawerViewStyle, defStyleRes);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super.init(context,attrs,defStyleAttr,defStyleRes);
        TypedArray b = context.obtainStyledAttributes(attrs, R.styleable.LeftRightMultiDrawerView, defStyleAttr, defStyleRes);

        //Get left or right from configuration set. For right now, assume right
        side  = b.getInteger(R.styleable.LeftRightMultiDrawerView_leftRightDrawerSide,0);
        System.out.println("Side: " + side);
        b.recycle();

        buttonScrollView = new VerticalOnlyCustomScrollView(context, attrs);//requestLayout();
        buttonScrollView.setId(View.generateViewId());
        buttonScrollView.setBackgroundColor(Color.TRANSPARENT);
        buttonScrollView.setFadingEdgeLength(60);
        buttonScrollView.setVerticalFadingEdgeEnabled(true);
        //Move scrollbar to left hand side if drawer is on left
        if(side == LEFT){
            buttonScrollView.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        }

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            buttonLinearLayout = new LinearLayout(context, attrs, defStyleAttr, defStyleRes);
        }else{
            buttonLinearLayout = new LinearLayout(context, attrs, defStyleAttr);
        }

        buttonLinearLayout.setOrientation(LinearLayout.VERTICAL);
        buttonScrollView.addView(buttonLinearLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bodyLayout = new LinearLayout(context, attrs, defStyleAttr, defStyleRes);
        }else {
            bodyLayout = new LinearLayout(context, attrs, defStyleAttr);
        }

        if(side == RIGHT) {
            LayoutParams layoutParams =new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            addView(buttonScrollView, layoutParams);

            layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.RIGHT_OF, buttonScrollView.getId());
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            bodyLayout.setLayoutParams(layoutParams);
            addView(bodyLayout);
        }else if (side == LEFT) {
            LayoutParams layoutParams =new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            addView(buttonScrollView, layoutParams);

            layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.LEFT_OF, buttonScrollView.getId());

            bodyLayout.setLayoutParams(layoutParams);
            addView(bodyLayout);
        }

        ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    LeftRightMultiDrawerView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if(side == RIGHT){
                        bodyLayout.setX(buttonScrollView.getWidth() +  bodyLayout.getWidth());
                        buttonScrollView.setX( bodyLayout.getWidth());
                    }else {
                        bodyLayout.setX(-buttonScrollView.getX());
                        buttonScrollView.setX(0);
                    }
                }
            });
        }
    }

    @Override
    protected GestureDetector.SimpleOnGestureListener getGestureDetector(View vw) {
        return new MyGestureDetector(vw);
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
        Drawer drawer = ((Drawer)((View) lastClickedButton.getParent()).getTag());
        Drawer.DrawerCallbacks notifyOpen = drawer.getDrawerCallbackHandler();
        if(notifyOpen != null){
            notifyOpen.drawerOpened(drawer);
        }
        isAnimating = true;
        lastClickedButton.setActivated(true);
        if(side == RIGHT) {

            ObjectAnimator.ofFloat(bodyLayout, "x", bodyLayout.getX(), buttonScrollView.getWidth())
                    .setDuration(animationOpenCloseTime)
                    .start();
            ObjectAnimator animator = ObjectAnimator.ofFloat(buttonScrollView, "x", buttonScrollView.getX(), buttonScrollView.getX() - bodyLayout.getWidth())
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
            ObjectAnimator.ofFloat(bodyLayout, "x", -bodyLayout.getWidth(),0)
                    .setDuration(animationOpenCloseTime)
                    .start();
            ObjectAnimator animator = ObjectAnimator.ofFloat(buttonScrollView, "x", 0,  bodyLayout.getWidth())
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

        if(side == RIGHT) {

            ObjectAnimator.ofFloat(bodyLayout, "x", bodyLayout.getX(), bodyLayout.getX() + bodyLayout.getWidth())
                    .setDuration(animationOpenCloseTime)
                    .start();
            ObjectAnimator animator = ObjectAnimator.ofFloat(buttonScrollView, "x", buttonScrollView.getX(), bodyLayout.getWidth())
                    .setDuration(animationOpenCloseTime);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isDrawerOpen = false;
                    isAnimating = false;
//                    ((View) lastClickedButton.getParent()).setBackground(null);
                    Drawer drawer = (Drawer) ((View) lastClickedButton.getParent()).getTag();
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
            ObjectAnimator.ofFloat(bodyLayout, "x", 0, -buttonScrollView.getX())
                    .setDuration(animationOpenCloseTime)
                    .start();
            ObjectAnimator animator = ObjectAnimator.ofFloat(buttonScrollView, "x", buttonScrollView.getX(), 0)
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


    @Override
    public boolean removeDrawer(Drawer drawer) {
        return false;
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
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                return false;
            }
            View oldLastClicked = lastClickedButton;
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                lastClickedButton = myView.get();
                if(!isDrawerOpen && side==RIGHT){
                    openDrawer();
                }else if(isDrawerOpen && side == LEFT){
                    closeDrawer();
                    bodyLayout.removeAllViews();
                    bodyLayout.addView(buttonToBodyView.get(lastClickedButton));
                    if(!myView.get().equals(oldLastClicked)){
                        ((View)oldLastClicked.getParent()).setBackground(null);
                    }
                }

            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                lastClickedButton = myView.get();
                if(!isDrawerOpen && side==LEFT){
                    openDrawer();
                }else if(isDrawerOpen && side == RIGHT){
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

    @Override
    protected void handleSingleTap(View myView) {
        final View oldLastClickedButton = lastClickedButton;
        lastClickedButton = myView;

        if(isDrawerOpen && oldLastClickedButton != lastClickedButton && !isAnimating){  //Changing open tabs
            lastClickedButton.setActivated(true);
            oldLastClickedButton.setActivated(false);
            Drawer drawer = ((Drawer)((View)oldLastClickedButton.getParent()).getTag());
            Drawer.DrawerCallbacks notifyClosed = drawer.getDrawerCallbackHandler();
            if(notifyClosed  != null){
                notifyClosed.drawerClosed(drawer);
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
            Drawer drawerOpen =  ((Drawer)((View) lastClickedButton.getParent()).getTag());
            Drawer.DrawerCallbacks notifyOpen =drawerOpen.getDrawerCallbackHandler();
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