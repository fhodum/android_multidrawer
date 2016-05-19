package com.multidrawer.fhodum.testdrawerapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multidrawer.fhodum.multidrawer.Drawer;
import com.multidrawer.fhodum.multidrawer.MultiDrawerView;

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

            Drawer.Builder builder = new Drawer.Builder();
            builder.setBody(body);
            builder.setButton(button);
            rightDrawerView.addDrawer(builder.createDrawer());
        }


        {
            final MultiDrawerView leftDrawerView = (MultiDrawerView) findViewById(R.id.multiDrawerLeft);
            View button = LayoutInflater.from(this).inflate(R.layout.first_button, leftDrawerView, false);
            View body = LayoutInflater.from(this).inflate(R.layout.first_body, leftDrawerView, false);

            ((TextView) body.findViewById(R.id.text_view)).setText("One");
            ((TextView) button.findViewById(R.id.button_text)).setText("One");

            Drawer.Builder builder = new Drawer.Builder();
            builder.setBody(body);
            builder.setButton(button);
            leftDrawerView.addDrawer(builder.createDrawer());
        }

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
