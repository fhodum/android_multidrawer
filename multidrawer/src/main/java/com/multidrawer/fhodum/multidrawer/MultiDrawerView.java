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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MultiDrawerView extends RelativeLayout {

    private ScrollView buttonScrollView;
    private LinearLayout buttonLinearLayout;
    private LinearLayout bodyLayout;
    private Vector<Drawer> drawers = null;


    private boolean isDrawerOpen = true;
    private boolean isAnimating = false;
    private View lastClickedButton = null;

    private int buttonSelectedBackgroundColor = Color.TRANSPARENT;
    private int animationOpenCloseTime = 500;
    private int deselectedButtonFadeTime = 500;

    private int side = 1;

    private float paddingLeft = 0;
    private float paddingTop = 0;
    private float paddingBottom = 0;
    private float paddingRight = 0;
    private float buttonLayoutHeight = LayoutParams.WRAP_CONTENT;
    private float buttonLayoutWidth = LayoutParams.WRAP_CONTENT;

    private static final int RIGHT = 1;
    private static final int LEFT = 0;

    private Map<View,View> buttonToBodyView = new HashMap<>();


    public MultiDrawerView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public MultiDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, R.attr.multiDrawerViewStyle, 0);
    }

    public MultiDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, R.attr.multiDrawerViewStyle);

        init(context, attrs, R.attr.multiDrawerViewStyle, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultiDrawerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, R.attr.multiDrawerViewStyle, defStyleRes);

        init(context, attrs, R.attr.multiDrawerViewStyle, defStyleRes);
    }


    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        if(drawers == null){
            drawers = new Vector<>();
        }
        removeAllViews();
        drawers.clear();

        //setOrientation(LinearLayout.HORIZONTAL);
        //setGravity(Gravity.RIGHT);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiDrawerView, defStyleAttr, defStyleRes);

        //Get left or right from configuration set. For right now, assume right
        side  = a.getInteger(R.styleable.MultiDrawerView_side,0);
        System.out.println("Side: " + side);


        for (int i=0; i < a.getIndexCount(); i++){
            int attr = a.getIndex(i);
            if(attr == R.styleable.MultiDrawerView_buttonSelectBackgroundColor){
                buttonSelectedBackgroundColor = a.getColor(R.styleable.MultiDrawerView_buttonSelectBackgroundColor, 0);
            }else if(attr == R.styleable.MultiDrawerView_buttonLayoutWidth) {
                buttonLayoutWidth = a.getDimension(R.styleable.MultiDrawerView_buttonLayoutWidth, LayoutParams.WRAP_CONTENT);

            }else if(attr == R.styleable.MultiDrawerView_buttonLayoutHeight) {
                buttonLayoutHeight = a.getDimension(R.styleable.MultiDrawerView_buttonLayoutHeight, LayoutParams.WRAP_CONTENT);

            }else if(attr == R.styleable.MultiDrawerView_animationOpenCloseTime) {
                animationOpenCloseTime = a.getInt(R.styleable.MultiDrawerView_animationOpenCloseTime, 500);

            } else if(attr == R.styleable.MultiDrawerView_deselectedButtonFadeTime) {
                deselectedButtonFadeTime = a.getInt(R.styleable.MultiDrawerView_deselectedButtonFadeTime, 500);

            }else if(attr == R.styleable.MultiDrawerView_buttonPaddingTop){
                paddingTop = a.getDimension(R.styleable.MultiDrawerView_buttonPaddingTop,0.0f);

            }else if(attr == R.styleable.MultiDrawerView_buttonPaddingLeft){
                paddingLeft = a.getDimension(R.styleable.MultiDrawerView_buttonPaddingLeft,0);

            }else if(attr == R.styleable.MultiDrawerView_buttonPaddingBottom){
                paddingBottom = a.getDimension(R.styleable.MultiDrawerView_buttonPaddingBottom,0);

            }else if(attr == R.styleable.MultiDrawerView_buttonPaddingRight){
                paddingRight = a.getDimension(R.styleable.MultiDrawerView_buttonPaddingRight,0);
            }

        }

//        Drawable backgroundDrawable = a.getDrawable(R.styleable.MultiDrawerView_buttonSelectionBackgroundDrawable);

        buttonScrollView = new VerticalOnlyCustomScrollView(context, attrs);//requestLayout();
        buttonScrollView.setId(View.generateViewId());
        buttonScrollView.setBackgroundColor(Color.TRANSPARENT);
        buttonScrollView.setFadingEdgeLength(60);
        buttonScrollView.setVerticalFadingEdgeEnabled(true);


        buttonLinearLayout = new LinearLayout(context, attrs,defStyleAttr,defStyleRes);
        buttonLinearLayout.setOrientation(LinearLayout.VERTICAL);
        buttonScrollView.addView(buttonLinearLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        bodyLayout  = new LinearLayout(context, attrs,defStyleAttr,defStyleRes);

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

    }

    public  void addDrawer(Drawer drawer) {

        View button = drawer.getButton();
       //button.setOnClickListener(new RightSideButtonClickListener());
        LinearLayout buttonWrapper = new LinearLayout(getContext());
        buttonWrapper.setOrientation(LinearLayout.VERTICAL);
        buttonWrapper.setGravity(Gravity.CENTER);

        buttonWrapper.setTag(drawer);
        final GestureDetector gestureDetector;
        gestureDetector = new GestureDetector(getContext(),new MyGestureDetector(button));
        button.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return false;
                } else {
                    return true;
                }
            }
        });
        LinearLayout.LayoutParams buttonWrapperLayoutParams = new LinearLayout.LayoutParams((int)(buttonLayoutWidth+0.5f), (int)(buttonLayoutHeight + 0.5f));
        buttonWrapperLayoutParams.setMargins((int)(paddingLeft+0.5f),(int)(paddingTop+0.5f),(int)(paddingRight+0.5f),(int)(paddingBottom+0.5f));
        buttonWrapperLayoutParams.gravity = Gravity.CENTER;
        buttonWrapper.addView(button,buttonWrapperLayoutParams);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
//        layoutParams.setMargins((int)(paddingLeft+0.5f),(int)(paddingTop+0.5f),(int)(paddingRight+0.5f),(int)(paddingBottom+0.5f));
//        buttonWrapper.setPadding((int)(paddingLeft+0.5f),(int)(paddingTop+0.5f),(int)(paddingRight+0.5f),(int)(paddingBottom+0.5f));
        buttonLinearLayout.addView(buttonWrapper, layoutParams);

        View body = drawer.getBody();

        buttonToBodyView.put(button,body);
        drawers.add(drawer);
        if(lastClickedButton == null) {
            lastClickedButton = button;
            isDrawerOpen = true;
            bodyLayout.addView(body);
        }
//        }else{
//            body.setVisibility(GONE);
//        }
        requestLayout();
    }



//    private class RightSideButtonClickListener implements View.OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//
//            final View oldLastClcikedButton = lastClickedButton;
//            lastClickedButton = v;
//
//            if(isDrawerOpen == true && oldLastClcikedButton != v && !isAnimating){  //Changing open tabs
//
//                buttonToBodyView.get(oldLastClcikedButton).setVisibility(GONE);
//                Drawer.DrawerCallbacks notifyClosed = ((Drawer)((View)oldLastClcikedButton.getParent()).getTag()).getDrawerCallbackHandler();
//                if(notifyClosed  != null){
//                    notifyClosed .drawerOpened();
//                }
//                //((View)oldLastClcikedButton.getParent()).setBackgroundColor(Color.TRANSPARENT);
//
//                int colorFrom = buttonSelectedBackgroundColor;
//                int colorTo = Color.TRANSPARENT;
//                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
//                colorAnimation.setDuration(deselectedButtonFadeTime); // milliseconds
//                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animator) {
//                        ((View)oldLastClcikedButton.getParent()).setBackgroundColor((int) animator.getAnimatedValue());
//                    }
//
//                });
//                colorAnimation.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        ((View)oldLastClcikedButton.getParent()).setBackground(null);
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//                });
//                colorAnimation.start();
//                bodyLayout.removeAllViews();
//                bodyLayout.addView(buttonToBodyView.get(lastClickedButton));
//                Drawer.DrawerCallbacks notifyOpen = ((Drawer)((View) lastClickedButton.getParent()).getTag()).getDrawerCallbackHandler();
//                if(notifyOpen != null){
//                    notifyOpen.drawerOpened();
//                }
//                ((View) lastClickedButton.getParent()).setBackgroundColor(buttonSelectedBackgroundColor);
//
//            } else if (isDrawerOpen == true && !isAnimating) {  //Closing drawer
//                closeDrawer();
//
//            } else if(isDrawerOpen == false && !isAnimating) {  //Opening Drawer
//
//                openDrawer();
//            }
//        }
//    }

    private void openDrawer() {
        int colorFrom = Color.TRANSPARENT;
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
        Drawer.DrawerCallbacks notifyOpen = ((Drawer)((View) lastClickedButton.getParent()).getTag()).getDrawerCallbackHandler();
        if(notifyOpen != null){
            notifyOpen.drawerOpened();
        }
        isAnimating = true;
        if(side == RIGHT) {

            ObjectAnimator.ofFloat(bodyLayout, "x", bodyLayout.getX(), bodyLayout.getX() - bodyLayout.getWidth())
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
    private void closeDrawer(){
        isAnimating = true;
        int colorFrom = buttonSelectedBackgroundColor;
        int colorTo = Color.TRANSPARENT;
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
                    ((View) lastClickedButton.getParent()).setBackground(null);
                    Drawer.DrawerCallbacks notifyClosed = ((Drawer) ((View) lastClickedButton.getParent()).getTag()).getDrawerCallbackHandler();
                    if (notifyClosed != null) {
                        notifyClosed.drawerClosed();
                    }
                    bodyLayout.removeAllViews();
//                    buttonToBodyView.get(lastClickedButton).setVisibility(GONE);
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
                    ((View) lastClickedButton.getParent()).setBackground(null);
                    Drawer.DrawerCallbacks notifyClosed = ((Drawer) ((View) lastClickedButton.getParent()).getTag()).getDrawerCallbackHandler();
                    if (notifyClosed != null) {
                        notifyClosed.drawerClosed();
                    }
                    bodyLayout.removeAllViews();
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

            final View oldLastClcikedButton = lastClickedButton;
            lastClickedButton = myView.get();

            if(isDrawerOpen && oldLastClcikedButton != lastClickedButton && !isAnimating){  //Changing open tabs

                //buttonToBodyView.get(oldLastClcikedButton).setVisibility(GONE);
                Drawer.DrawerCallbacks notifyClosed = ((Drawer)((View)oldLastClcikedButton.getParent()).getTag()).getDrawerCallbackHandler();
                if(notifyClosed  != null){
                    notifyClosed .drawerOpened();
                }
                //((View)oldLastClcikedButton.getParent()).setBackgroundColor(Color.TRANSPARENT);

                int colorFrom = buttonSelectedBackgroundColor;
                int colorTo = Color.TRANSPARENT;
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(deselectedButtonFadeTime); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        ((View)oldLastClcikedButton.getParent()).setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ((View)oldLastClcikedButton.getParent()).setBackground(null);
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
                Drawer.DrawerCallbacks notifyOpen = ((Drawer)((View) lastClickedButton.getParent()).getTag()).getDrawerCallbackHandler();
                if(notifyOpen != null){
                    notifyOpen.drawerOpened();
                }
                ((View) lastClickedButton.getParent()).setBackgroundColor(buttonSelectedBackgroundColor);

            } else if (isDrawerOpen == true && !isAnimating) {  //Closing drawer
                closeDrawer();

            } else if(isDrawerOpen == false && !isAnimating) {  //Opening Drawer

                openDrawer();
            }
            return true;
        }
    }

}