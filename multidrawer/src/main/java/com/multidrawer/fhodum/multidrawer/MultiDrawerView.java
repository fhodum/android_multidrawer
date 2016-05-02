package com.multidrawer.fhodum.multidrawer;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
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

    private int animationTime = 500;

    private Map<View,View> buttonToBodyView = new HashMap<>();


    public MultiDrawerView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public MultiDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0, 0);
    }

    public MultiDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultiDrawerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes);
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
        buttonWrapper.addView(button);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (5*scale + 0.5f);
        buttonWrapper.setPadding(dpAsPixels,dpAsPixels,0,dpAsPixels);
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
            if(isDrawerOpen == true && oldLastClcikedButton != v && !isAnimating){

                lastClcikedButton = v;
                buttonToBodyView.get(oldLastClcikedButton).setVisibility(GONE);
                //((View)oldLastClcikedButton.getParent()).setBackgroundColor(Color.TRANSPARENT);

                int colorFrom = Color.BLACK;
                int colorTo = Color.TRANSPARENT;
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(((long)2*animationTime/3)); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        ((View)oldLastClcikedButton.getParent()).setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();
                buttonToBodyView.get(lastClcikedButton).setVisibility(VISIBLE);
                ((View)lastClcikedButton.getParent()).setBackgroundColor(Color.BLACK);

            } else if (isDrawerOpen == true && !isAnimating) {
                isAnimating = true;
                int colorFrom = Color.BLACK;
                int colorTo = Color.TRANSPARENT;
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(animationTime - 50); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        ((View)lastClcikedButton.getParent()).setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();
                ObjectAnimator.ofFloat(bodyLayout, "x", bodyLayout.getX(), bodyLayout.getX() + bodyLayout.getWidth())
                        .setDuration(animationTime)
                        .start();
                ObjectAnimator animator = ObjectAnimator.ofFloat(buttonScrollView, "x", buttonScrollView.getX(), bodyLayout.getWidth())
                        .setDuration(animationTime);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isDrawerOpen = false;
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

            } else if(isDrawerOpen == false && !isAnimating) {

                int colorFrom = Color.TRANSPARENT;
                int colorTo = Color.BLACK;
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(animationTime-50); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        ((View)lastClcikedButton.getParent()).setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();
                buttonToBodyView.get(oldLastClcikedButton).setVisibility(GONE);
                buttonToBodyView.get(lastClcikedButton).setVisibility(VISIBLE);

                isAnimating = true;
                ObjectAnimator.ofFloat(bodyLayout, "x", bodyLayout.getX(), bodyLayout.getX() - bodyLayout.getWidth())
                        .setDuration(animationTime)
                        .start();
                ObjectAnimator animator = ObjectAnimator.ofFloat(buttonScrollView, "x", buttonScrollView.getX(), buttonScrollView.getX() - bodyLayout.getWidth())
                        .setDuration(animationTime);

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