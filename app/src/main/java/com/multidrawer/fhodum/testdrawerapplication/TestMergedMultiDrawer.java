package com.multidrawer.fhodum.testdrawerapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.multidrawer.fhodum.multidrawer.Drawer;
import com.multidrawer.fhodum.multidrawer.MultiDrawerView;
import com.multidrawer.fhodum.multidrawer.PanelDrawer;

import java.util.List;
import java.util.Vector;

/**
 * Created by fhodum on 5/11/16.
 */
public class TestMergedMultiDrawer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_merged_multi_drawer);
        {
            final MultiDrawerView rightDrawerView = (MultiDrawerView) findViewById(R.id.multiDrawerRight);
            View button = LayoutInflater.from(this).inflate(R.layout.first_button, rightDrawerView, false);
            View body = LayoutInflater.from(this).inflate(R.layout.first_body, rightDrawerView, false);

            ((TextView) body.findViewById(R.id.text_view)).setText("One");
            ((TextView) button.findViewById(R.id.button_text)).setText("One");

            PanelDrawer.Builder builder = new PanelDrawer.Builder();
            builder.setBody(body);


            final PanelDrawer drawer = builder.createDrawer();
            rightDrawerView.addDrawer(drawer);

            ((Button)findViewById(R.id.toggleRightButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rightDrawerView.toggleDrawer(drawer);
                }
            });

        }

        {
            final MultiDrawerView leftDrawerView = (MultiDrawerView) findViewById(R.id.multiDrawerLeft);
            View button = LayoutInflater.from(this).inflate(R.layout.first_button, leftDrawerView, false);
            View body1 = LayoutInflater.from(this).inflate(R.layout.first_body, leftDrawerView, false);
            View body2 = LayoutInflater.from(this).inflate(R.layout.first_body, leftDrawerView, false);

            ((TextView) body1.findViewById(R.id.text_view)).setText("One");
            ((TextView) body2.findViewById(R.id.text_view)).setText("Two");



            PanelDrawer.Builder builder = new PanelDrawer.Builder();
            builder.setBody(body1);


            final PanelDrawer drawer = builder.createDrawer();
            leftDrawerView.addDrawer(drawer);

            builder = new PanelDrawer.Builder();
            builder.setBody(body2);
            final PanelDrawer drawer2= builder.createDrawer();

            leftDrawerView.addDrawer(drawer2);


            ((Button)findViewById(R.id.toggleLeftButton1)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftDrawerView.toggleDrawer(drawer);
                }
            });

            ((Button)findViewById(R.id.toggleLeftButton2)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftDrawerView.toggleDrawer(drawer2);
                }
            });

        }

//        {
//            final MultiDrawerView leftDrawerView = (MultiDrawerView) findViewById(R.id.multiDrawerLeft);
//            View button = LayoutInflater.from(this).inflate(R.layout.first_button, leftDrawerView, false);
//            View body = LayoutInflater.from(this).inflate(R.layout.first_body, leftDrawerView, false);
//
//            ((TextView) body.findViewById(R.id.text_view)).setText("One");
//            ((TextView) button.findViewById(R.id.button_text)).setText("One");
//
//            Drawer.Builder builder = new Drawer.Builder();
//            builder.setBody(body);
//            builder.setButton(button);
//            leftDrawerView.addDrawer(builder.createDrawer());
//        }

        {
            MultiDrawerView topDrawerView = (MultiDrawerView)findViewById(R.id.multiDrawerTop);
            //TOP
            final View buttonTop = LayoutInflater.from(this).inflate(R.layout.top_button,topDrawerView , false);
            View body = LayoutInflater.from(this).inflate(R.layout.bottom_body,topDrawerView, false);

            ((TextView)buttonTop.findViewById(R.id.button_text)).setText("Top");
            ViewPager vp = (ViewPager)body.findViewById(R.id.view_pager);
            vp.setAdapter(new MyAdapter());

            Drawer.Builder builder = new Drawer.Builder();
            builder.setBody(body);
            builder.setButton(buttonTop);
            builder.setDrawerCallback(new Drawer.DrawerCallbacks() {
                @Override
                public void drawerOpened(Drawer drawer) {
                    ((TextView)buttonTop.findViewById(R.id.button_text)).setText("Open");
                }

                @Override
                public void drawerClosed(Drawer drawer) {
                    ((TextView)buttonTop.findViewById(R.id.button_text)).setText("Closed");
                }
            });
            topDrawerView.addDrawer(builder.createDrawer());
        }

        {
            final MultiDrawerView bottomDrawerView = (MultiDrawerView)findViewById(R.id.multiDrawerBottom);
            //BOTTOM
            View body = LayoutInflater.from(this).inflate(R.layout.bottom_body,bottomDrawerView, false);


            PanelDrawer.Builder builder = new PanelDrawer.Builder();
            builder.setBody(body);

            final PanelDrawer drawer = builder.createDrawer();
            bottomDrawerView.addDrawer(drawer);

            ((Button)findViewById(R.id.toggleButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomDrawerView.toggleDrawer(drawer);
                }
            });
        }
    }


    public class MyAdapter extends PagerAdapter {
        public MyAdapter(){
        }

        List<View> views = new Vector<View>(0);

        @Override
        public Object instantiateItem(ViewGroup parent, int position) {
            View view = null;
            if(views.size()<= position){
                view = LayoutInflater.from(TestMergedMultiDrawer.this).inflate(R.layout.page,parent,false);
                ((TextView)view.findViewById(R.id.pagerTextId)).setText(String.valueOf(position));
                ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
                lp.width = ViewPager.LayoutParams.FILL_PARENT;
                lp.height = ViewPager.LayoutParams.FILL_PARENT;
                view.setLayoutParams(lp);
                parent.addView(view);
            }else{
                view = views.get(position);
                parent.addView(view);
            }

            return view;
        }

        @Override
        public void destroyItem(ViewGroup parent, int position, Object object) {
            View view = (View) object;
            parent.removeView(view);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public String getPageTitle(int position){
            return String.valueOf(position);
        }
    }
}
