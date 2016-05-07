package com.multidrawer.fhodum.multidrawer;

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
import android.widget.Space;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by fhodum on 5/7/16.
 */
public abstract  class MultiDrawerBase extends RelativeLayout implements IMultiDrawer{

    protected Vector<Drawer> drawers = null;
    protected Map<View,View> buttonToBodyView = new HashMap<>();

    protected LinearLayout buttonLinearLayout;
    protected LinearLayout bodyLayout;

    protected boolean isDrawerOpen = false;
    protected boolean isAnimating = false;
    protected View lastClickedButton = null;

    protected int buttonSelectedBackgroundColor = Color.TRANSPARENT;
    protected int buttonDeSelectedBackgroundColor = Color.TRANSPARENT;
    protected int animationOpenCloseTime = 500;
    protected int deselectedButtonFadeTime = 500;

    protected float paddingLeft = 0;
    protected float paddingTop = 0;
    protected float paddingBottom = 0;
    protected float paddingRight = 0;
    protected float spaceBetweenTabs = 0;
    protected float buttonLayoutHeight = LayoutParams.WRAP_CONTENT;
    protected float buttonLayoutWidth = LayoutParams.WRAP_CONTENT;

    public MultiDrawerBase(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public MultiDrawerBase(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, R.attr.leftRightMultiDrawerViewStyle, 0);
    }

    public MultiDrawerBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, R.attr.leftRightMultiDrawerViewStyle);

        init(context, attrs, R.attr.leftRightMultiDrawerViewStyle, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultiDrawerBase(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, R.attr.leftRightMultiDrawerViewStyle, defStyleRes);

        init(context, attrs, R.attr.leftRightMultiDrawerViewStyle, defStyleRes);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        if (drawers == null) {
            drawers = new Vector<>();
        }
        removeAllViews();
        drawers.clear();


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseDrawerAttributes, defStyleAttr, defStyleRes);

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.BaseDrawerAttributes_multiDrawerButtonSelectedBackgroundColor) {
                buttonSelectedBackgroundColor = a.getColor(R.styleable.BaseDrawerAttributes_multiDrawerButtonSelectedBackgroundColor, 0);
            }else if (attr == R.styleable.BaseDrawerAttributes_multiDrawerButtonDeSelectedBackgroundColor) {
                    buttonDeSelectedBackgroundColor = a.getColor(R.styleable.BaseDrawerAttributes_multiDrawerButtonDeSelectedBackgroundColor, 0);
            } else if (attr == R.styleable.BaseDrawerAttributes_multiDrawerButtonLayoutWidth) {
                buttonLayoutWidth = a.getDimension(R.styleable.BaseDrawerAttributes_multiDrawerButtonLayoutWidth, LayoutParams.WRAP_CONTENT);

            } else if (attr == R.styleable.BaseDrawerAttributes_multiDrawerButtonLayoutHeight) {
                buttonLayoutHeight = a.getDimension(R.styleable.BaseDrawerAttributes_multiDrawerButtonLayoutHeight, LayoutParams.WRAP_CONTENT);

            } else if (attr == R.styleable.BaseDrawerAttributes_multiDrawerAnimationOpenCloseTime) {
                animationOpenCloseTime = a.getInt(R.styleable.BaseDrawerAttributes_multiDrawerAnimationOpenCloseTime, 500);

            } else if (attr == R.styleable.BaseDrawerAttributes_multiDrawerDeselectedButtonFadeTime) {
                deselectedButtonFadeTime = a.getInt(R.styleable.BaseDrawerAttributes_multiDrawerDeselectedButtonFadeTime, 500);

            } else if (attr == R.styleable.BaseDrawerAttributes_multiDrawerButtonPaddingTop) {
                paddingTop = a.getDimension(R.styleable.BaseDrawerAttributes_multiDrawerButtonPaddingTop, 0.0f);

            } else if (attr == R.styleable.BaseDrawerAttributes_multiDrawerButtonPaddingLeft) {
                paddingLeft = a.getDimension(R.styleable.BaseDrawerAttributes_multiDrawerButtonPaddingLeft, 0);

            } else if (attr == R.styleable.BaseDrawerAttributes_multiDrawerButtonPaddingBottom) {
                paddingBottom = a.getDimension(R.styleable.BaseDrawerAttributes_multiDrawerButtonPaddingBottom, 0);

            } else if (attr == R.styleable.BaseDrawerAttributes_multiDrawerButtonPaddingRight) {
                paddingRight = a.getDimension(R.styleable.BaseDrawerAttributes_multiDrawerButtonPaddingRight, 0);
            } else if (attr == R.styleable.BaseDrawerAttributes_multiDrawerSpaceBetweenDrawerTabs) {
                spaceBetweenTabs = a.getDimension(R.styleable.BaseDrawerAttributes_multiDrawerSpaceBetweenDrawerTabs, 0);
            }

        }
    }

    abstract protected GestureDetector.SimpleOnGestureListener getGestureDetector(View vw);

    abstract protected void handleSingleTap(View vw);

    abstract void closeDrawer();

    public void addDrawer(Drawer drawer) {

        View button = drawer.getButton();
        //button.setOnClickListener(new RightSideButtonClickListener());
        LinearLayout buttonWrapper = new LinearLayout(getContext());
        buttonWrapper.setOrientation(LinearLayout.VERTICAL);
        buttonWrapper.setGravity(Gravity.CENTER);
        buttonWrapper.setBackgroundColor(buttonDeSelectedBackgroundColor);
        buttonWrapper.setTag(drawer);
        final GestureDetector gestureDetector;
        gestureDetector = new GestureDetector(getContext(),getGestureDetector(button));
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
        buttonWrapper.setPadding((int)(paddingLeft+0.5f),(int)(paddingTop+0.5f),(int)(paddingRight+0.5f),(int)(paddingBottom+0.5f));
        buttonWrapperLayoutParams.gravity = Gravity.CENTER;
        buttonWrapper.addView(button,buttonWrapperLayoutParams);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        buttonLinearLayout.addView(buttonWrapper, layoutParams);

        View body = drawer.getBody();

        buttonToBodyView.put(button,body);
        drawers.add(drawer);
        if(lastClickedButton == null) {
            lastClickedButton = button;
        }
        Space spacer = new Space(getContext());
        spacer.setMinimumWidth((int)spaceBetweenTabs);
        spacer.setMinimumHeight((int)spaceBetweenTabs);
        buttonLinearLayout.addView(spacer);
        requestLayout();
    }

    @Override
    public boolean openDrawer(Drawer drawer) {
        boolean retVal = false;
        if (!isDrawerOpen && lastClickedButton!= null && !lastClickedButton.equals(drawer.getButton()) && drawers.contains(drawer)) {
            handleSingleTap((View)drawer.getButton());
            retVal = true;
        }
        return retVal;
    }

    @Override
    public boolean openDrawer(int drawerNumber) {
        boolean retVal = false;
        if(!isDrawerOpen && drawerNumber < drawers.size() && lastClickedButton!= null && !lastClickedButton.equals(drawers.get(drawerNumber)) ){
            handleSingleTap((View)drawers.get(drawerNumber).getButton());
            retVal = true;
        }
        return retVal;
    }

    @Override
    public void closeDrawers() {
        if(isDrawerOpen){
            closeDrawer();
        }
    }
}
