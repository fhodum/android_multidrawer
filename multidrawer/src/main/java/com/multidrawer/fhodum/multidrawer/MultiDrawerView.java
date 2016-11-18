package com.multidrawer.fhodum.multidrawer;

import android.animation.Animator;
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
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by fhodum on 5/11/16.
 */
public class MultiDrawerView extends RelativeLayout implements IMultiDrawer {

    protected IDrawerController drawerController = null;
    protected Vector<Drawer> drawers = null;
    protected Map<View,View> buttonToBodyView = new HashMap<>();

    protected ViewGroup buttonScrollView;
    protected List<Drawer> removingDrawersQueue = new Vector<>();
    protected LinearLayout buttonLinearLayout;
    protected LinearLayout bodyLayout;

    protected boolean isDrawerOpen = false;
    protected boolean isAnimating = false;
    //Specifically set as package private/protected so the gesture listeners can modify it
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
    protected float buttonLayoutHeight = RelativeLayout.LayoutParams.WRAP_CONTENT;
    protected float buttonLayoutWidth = RelativeLayout.LayoutParams.WRAP_CONTENT;
    protected int side = 0;

    protected static final int RIGHT = 0;
    protected static final int LEFT = 1;
    protected static final int TOP = 2;
    protected static final int BOTTOM = 3;

    public MultiDrawerView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public MultiDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs,  R.attr.multiDrawerViewStyle, 0);
    }

    public MultiDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, R.attr.multiDrawerViewStyle);

        init(context, attrs,  R.attr.multiDrawerViewStyle, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultiDrawerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs,  R.attr.multiDrawerViewStyle, defStyleRes);

        init(context, attrs, R.attr.multiDrawerViewStyle, defStyleRes);
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
                buttonLayoutWidth = a.getDimension(R.styleable.BaseDrawerAttributes_multiDrawerButtonLayoutWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);

            } else if (attr == R.styleable.BaseDrawerAttributes_multiDrawerButtonLayoutHeight) {
                buttonLayoutHeight = a.getDimension(R.styleable.BaseDrawerAttributes_multiDrawerButtonLayoutHeight, RelativeLayout.LayoutParams.WRAP_CONTENT);

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
        a.recycle();

        TypedArray b = context.obtainStyledAttributes(attrs, R.styleable.MultiDrawerView,
                defStyleAttr, defStyleRes);

        //Get left or right from configuration set. For right now, assume right
        side  = b.getInteger(R.styleable.MultiDrawerView_side,0);
        if(side == LEFT){
            drawerController = new LeftRightController(this, this.getContext(), attrs, defStyleAttr, defStyleRes);
            buttonScrollView.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        } else if (side == RIGHT){
            drawerController = new LeftRightController(this, this.getContext(), attrs, defStyleAttr, defStyleRes);
        } else if (side == TOP) {
            drawerController = new TopBottomController(this, this.getContext(), attrs, defStyleAttr, defStyleRes);
        } else if(side == BOTTOM){
            drawerController = new TopBottomController(this, this.getContext(), attrs, defStyleAttr, defStyleRes);
        }
    }

    protected GestureDetector.SimpleOnGestureListener getGestureDetector(View vw){
        GestureDetector.SimpleOnGestureListener listener = null;

        listener = drawerController.getGestureDetector(vw);

        return listener;
    }

    protected void handleSingleTap(View vw){
        drawerController.handleSingleTap(vw);
    }



    public void addDrawer(Drawer drawer) {

        View button = drawer.getButton();
        //button.setOnClickListener(new RightSideButtonClickListener());
        if(button!=null) {
            LinearLayout buttonWrapper = new LinearLayout(getContext());
            buttonWrapper.setOrientation(LinearLayout.VERTICAL);
            buttonWrapper.setGravity(Gravity.CENTER);
            buttonWrapper.setBackgroundColor(buttonDeSelectedBackgroundColor);
            buttonWrapper.setTag(drawer);
            final GestureDetector gestureDetector;
            gestureDetector = new GestureDetector(getContext(), getGestureDetector(button));
            button.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    if (gestureDetector.onTouchEvent(event)) {
                        return false;
                    } else {
                        return true;
                    }
                }
            });
            int showhandle = (drawer.isPanel()?0:1);
            LinearLayout.LayoutParams buttonWrapperLayoutParams = new LinearLayout.LayoutParams((int) (buttonLayoutWidth + 0.5f), (int) ((showhandle) * (buttonLayoutHeight + 0.5f)));
            if(!drawer.isPanel()) {
                buttonWrapper.setPadding((int) (paddingLeft + 0.5f), (int) (paddingTop + 0.5f), (int) (paddingRight + 0.5f), (int) (paddingBottom + 0.5f));
            }
            buttonWrapperLayoutParams.gravity = Gravity.CENTER;
            buttonWrapper.addView(button, buttonWrapperLayoutParams);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            buttonLinearLayout.addView(buttonWrapper, layoutParams);
        }
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
    public void toggleDrawer(Drawer drawer) {
        if(lastClickedButton!= null && lastClickedButton.equals(drawer.getButton()) && drawers.contains(drawer)){
            if(isDrawerOpen){
                closeDrawers();
            }else{
                openDrawer(drawer);
            }
        }else {
            lastClickedButton = drawer.getButton();
            openDrawer(drawer);
        }
    }


    @Override
    public boolean openDrawer(Drawer drawer) {
        boolean retVal = false;
        if (!isDrawerOpen ||( lastClickedButton!= null && !lastClickedButton.equals(drawer.getButton()) && drawers.contains(drawer))) {
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
            drawerController.closeDrawer();
        }
    }



    protected Animator getButtonRemovalAnimator(Drawer drawer){
        return drawerController.getButtonRemovalAnimator(drawer);
    }

    protected void completeRemoveDrawer(Drawer drawer){
        ViewParent viewParent = drawer.getButton().getParent();

        if(viewParent != null){
            drawers.remove(drawer);
            buttonLinearLayout.removeView((View)viewParent);
            bodyLayout.removeView(drawer.getBody());
            ((ViewGroup)viewParent).removeView(drawer.getButton());

        }
    }

    @Override
    public boolean removeDrawer(Drawer drawer){
        if(isDrawerOpen){
            closeDrawers();
            queueRemove(drawer);
        }else{
            queueRemove(drawer);
            processQueuedRemovals();
        }
        boolean retVal = false;
        ViewParent viewParent = drawer.getButton().getParent();
        if(viewParent != null){
            retVal = true;
        }
        return retVal;
    }

    //abstract protected void animateRemovalOfDrawerButton(ValueAnimator animator, Drawer drawer);

    synchronized protected void queueRemove(Drawer drawer){
        removingDrawersQueue.add(drawer);
    }

    synchronized protected void processQueuedRemovals(){

        for(final Drawer drawer:removingDrawersQueue){
            if(drawer.getButton().getParent() != null){
                isAnimating = true;
                Animator va = getButtonRemovalAnimator(drawer);
                va.start();
            }
        }
        removingDrawersQueue.clear();
    }

}
