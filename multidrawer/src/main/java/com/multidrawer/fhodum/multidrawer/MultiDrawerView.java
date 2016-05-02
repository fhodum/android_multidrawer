package com.multidrawer.fhodum.multidrawer;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MultiDrawerView extends LinearLayout {

    private ScrollView buttonScrollView;
    private LinearLayout buttonLinearLayout;
    private LinearLayout bodyLayout;
    private Vector<Drawer> drawers = null;


    private boolean isDrawerOpen = true;
    private boolean isAnimating = false;
    private View lastClcikedButton = null;

    private int buttonSelectedBackgroundColor = Color.TRANSPARENT;
    private int animationOpenCloseTime = 500;
    private int deselectedButtonFadeTime = 500;

    private float paddingLeft = 0;
    private float paddingTop = 0;
    private float paddingBottom = 0;
    private float paddingRight = 0;
    private float buttonLayoutHeight = LayoutParams.WRAP_CONTENT;
    private float buttonLayoutWidth = LayoutParams.WRAP_CONTENT;

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

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.RIGHT);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiDrawerView, defStyleAttr, defStyleRes);

        //Get left or right from configuration set. For right now, assume right
        int side = a.getInteger(R.styleable.MultiDrawerView_side,0);
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

        Drawable backgroundDrawable = a.getDrawable(R.styleable.MultiDrawerView_buttonSelectionBackgroundDrawable);

        buttonScrollView = new ScrollView(context, attrs,defStyleAttr,defStyleRes) ;//requestLayout();
        buttonScrollView.setBackgroundColor(Color.TRANSPARENT);
        buttonScrollView.setFadingEdgeLength(60);
        buttonScrollView.setVerticalFadingEdgeEnabled(true);
        addView(buttonScrollView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

        buttonLinearLayout = new LinearLayout(context, attrs,defStyleAttr,defStyleRes);
        buttonLinearLayout.setOrientation(VERTICAL);
        buttonScrollView.addView(buttonLinearLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        bodyLayout  = new LinearLayout(context, attrs,defStyleAttr,defStyleRes);
        addView(bodyLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));


    }

    public  void addDrawer(Drawer drawer){
        View button = drawer.getButton();
        button.setOnClickListener(new RightSideButtonClickListener());
        LinearLayout buttonWrapper = new LinearLayout(getContext());
        buttonWrapper.setOrientation(VERTICAL);
        buttonWrapper.setGravity(Gravity.CENTER);

        buttonWrapper.setTag(drawer);

        LinearLayout.LayoutParams buttonWrapperLayoutParams = new LayoutParams((int)(buttonLayoutWidth+0.5f), (int)(buttonLayoutHeight + 0.5f));
        buttonWrapperLayoutParams.setMargins((int)(paddingLeft+0.5f),(int)(paddingTop+0.5f),(int)(paddingRight+0.5f),(int)(paddingBottom+0.5f));
        buttonWrapperLayoutParams.gravity = Gravity.CENTER;
        buttonWrapper.addView(button,buttonWrapperLayoutParams);

        LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
//        layoutParams.setMargins((int)(paddingLeft+0.5f),(int)(paddingTop+0.5f),(int)(paddingRight+0.5f),(int)(paddingBottom+0.5f));
//        buttonWrapper.setPadding((int)(paddingLeft+0.5f),(int)(paddingTop+0.5f),(int)(paddingRight+0.5f),(int)(paddingBottom+0.5f));
        buttonLinearLayout.addView(buttonWrapper, layoutParams);

        View body = drawer.getBody();
        bodyLayout.addView(body);
        buttonToBodyView.put(button,body);
        drawers.add(drawer);
        if(lastClcikedButton == null){
            lastClcikedButton = button;
            isDrawerOpen = true;
        }else{
            body.setVisibility(GONE);
        }
    }


    private class RightSideButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            final View oldLastClcikedButton = lastClcikedButton;
            lastClcikedButton = v;

            if(isDrawerOpen == true && oldLastClcikedButton != v && !isAnimating){  //Changing open tabs

                lastClcikedButton = v;
                buttonToBodyView.get(oldLastClcikedButton).setVisibility(GONE);
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
                buttonToBodyView.get(lastClcikedButton).setVisibility(VISIBLE);
                Drawer.DrawerCallbacks notifyOpen = ((Drawer)((View)lastClcikedButton.getParent()).getTag()).getDrawerCallbackHandler();
                if(notifyOpen != null){
                    notifyOpen.drawerOpened();
                }
                ((View)lastClcikedButton.getParent()).setBackgroundColor(buttonSelectedBackgroundColor);

            } else if (isDrawerOpen == true && !isAnimating) {  //Closing drawer
                isAnimating = true;
                int colorFrom = buttonSelectedBackgroundColor;
                int colorTo = Color.TRANSPARENT;
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(animationOpenCloseTime - 50); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        ((View)lastClcikedButton.getParent()).setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();
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
                        ((View)oldLastClcikedButton.getParent()).setBackground(null);
                        Drawer.DrawerCallbacks notifyClosed = ((Drawer)((View)lastClcikedButton.getParent()).getTag()).getDrawerCallbackHandler();
                        if(notifyClosed != null){
                            notifyClosed.drawerClosed();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();

            } else if(isDrawerOpen == false && !isAnimating) {  //Opening Drawer

                int colorFrom = Color.TRANSPARENT;
                int colorTo = buttonSelectedBackgroundColor;
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(animationOpenCloseTime -50); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        ((View)lastClcikedButton.getParent()).setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();
                buttonToBodyView.get(oldLastClcikedButton).setVisibility(GONE);

                buttonToBodyView.get(lastClcikedButton).setVisibility(VISIBLE);
                Drawer.DrawerCallbacks notifyOpen = ((Drawer)((View)lastClcikedButton.getParent()).getTag()).getDrawerCallbackHandler();
                if(notifyOpen != null){
                    notifyOpen.drawerOpened();
                }
                isAnimating = true;
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
            }
        }
    }

}