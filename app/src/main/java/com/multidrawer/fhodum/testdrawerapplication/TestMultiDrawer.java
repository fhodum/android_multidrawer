package com.multidrawer.fhodum.testdrawerapplication;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.multidrawer.fhodum.multidrawer.Drawer;
import com.multidrawer.fhodum.multidrawer.LeftRightMultiDrawerView;
import com.multidrawer.fhodum.multidrawer.MultiDrawerBase;
import com.multidrawer.fhodum.multidrawer.TopBottomMultiDrawerView;

import java.util.List;
import java.util.Vector;


public class TestMultiDrawer extends AppCompatActivity {

    View vw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_multi_drawer);
        LeftRightMultiDrawerView leftDrawerView = (LeftRightMultiDrawerView)findViewById(R.id.multiDrawerLeft);
        View button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
        View body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("One");
        ((TextView)button.findViewById(R.id.button_text)).setText("One");

        Drawer.Builder builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);


        leftDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button, leftDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body, leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Two");
        ((TextView)button.findViewById(R.id.button_text)).setText("Two");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        leftDrawerView.addDrawer(builder.createDrawer());



        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Three");
        ((TextView)button.findViewById(R.id.button_text)).setText("Three");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        leftDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Four");
        ((TextView)button.findViewById(R.id.button_text)).setText("Four");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        leftDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Five");
        ((TextView)button.findViewById(R.id.button_text)).setText("Five");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        leftDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Six");
        ((TextView)button.findViewById(R.id.button_text)).setText("Six");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        leftDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Seven");
        ((TextView)button.findViewById(R.id.button_text)).setText("Seven");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        leftDrawerView.addDrawer(builder.createDrawer());





        //Right

        LeftRightMultiDrawerView rightDrawerView = (LeftRightMultiDrawerView)findViewById(R.id.multiDrawerRight);
        button = LayoutInflater.from(this).inflate(R.layout.right_button,rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("One");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);


        rightDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.right_button, rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body, rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Two");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        rightDrawerView.addDrawer(builder.createDrawer());



        button = LayoutInflater.from(this).inflate(R.layout.right_button,rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Three");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        rightDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.right_button,rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Four");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        rightDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.right_button,rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Five");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        rightDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.right_button,rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Six");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        rightDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.right_button,rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Seven");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        rightDrawerView.addDrawer(builder.createDrawer());


        //BOTTOM
        //Bottom masquerading as a botom sheet by having a wide, short button
        TopBottomMultiDrawerView bottomDrawerView = (TopBottomMultiDrawerView)findViewById(R.id.multiDrawerBottom);
//        //Bottom
        button = LayoutInflater.from(this).inflate(R.layout.bottom_button,bottomDrawerView , false);
        ((TextView)button.findViewById(R.id.button_text)).setText("");
        body = LayoutInflater.from(this).inflate(R.layout.bottom_body,bottomDrawerView, false);

        ViewPager vp = (ViewPager)body.findViewById(R.id.view_pager);
        vp.setAdapter(new MyAdapter());

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        bottomDrawerView.addDrawer(builder.createDrawer());



        //TOP
        TopBottomMultiDrawerView topDrawerView = (TopBottomMultiDrawerView)findViewById(R.id.multiDrawerTop);
        //TOP
        final View buttonTop = LayoutInflater.from(this).inflate(R.layout.top_button,topDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.bottom_body,topDrawerView, false);

        ((TextView)buttonTop.findViewById(R.id.button_text)).setText("Top");
        vp = (ViewPager)body.findViewById(R.id.view_pager);
        vp.setAdapter(new MyAdapter());

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(buttonTop);
        builder.setDrawerCallback(new Drawer.DrawerCallbacks() {
            @Override
            public void drawerOpened() {
                ((TextView)buttonTop.findViewById(R.id.button_text)).setText("Open");
            }

            @Override
            public void drawerClosed() {
                ((TextView)buttonTop.findViewById(R.id.button_text)).setText("Closed");
            }
        });

        topDrawerView.addDrawer(builder.createDrawer());

        ((Button)findViewById(R.id.openTop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TopBottomMultiDrawerView)findViewById(R.id.multiDrawerTop)).openDrawer(0);
            }
        });

        ((Button)findViewById(R.id.closeTop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TopBottomMultiDrawerView)findViewById(R.id.multiDrawerTop)).closeDrawers();
            }
        });
//
//        button = LayoutInflater.from(this).inflate(R.layout.first_button,rightDrawerView , false);
//        body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);
//
//        ((TextView)body.findViewById(R.id.text_view)).setText("Eight");
//
//        builder = new Drawer.Builder();
//        builder.setBody(body);
//        builder.setButton(button);
//
//        leftDrawerView.addDrawer(builder.createDrawer());
//
//        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
//        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);
//
//        ((TextView)body.findViewById(R.id.text_view)).setText("Nine");
//
//        builder = new Drawer.Builder();
//        builder.setBody(body);
//        builder.setButton(button);
//
//        leftDrawerView.addDrawer(builder.createDrawer());
//
//        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
//        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);
//
//        ((TextView)body.findViewById(R.id.text_view)).setText("Ten");
//
//        builder = new Drawer.Builder();
//        builder.setBody(body);
//        builder.setButton(button);
//
//        leftDrawerView.addDrawer(builder.createDrawer());
//
//        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
//        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);
//
//        ((TextView)body.findViewById(R.id.text_view)).setText("Eleven");
//
//        builder = new Drawer.Builder();
//        builder.setBody(body);
//        builder.setButton(button);
//
//        leftDrawerView.addDrawer(builder.createDrawer());

    }





    public class MyAdapter extends PagerAdapter {
        public MyAdapter(){
        }

        List<View> views = new Vector<View>(0);

        @Override
        public Object instantiateItem(ViewGroup parent, int position) {
            View view = null;
            if(views.size()<= position){
                view = LayoutInflater.from(TestMultiDrawer.this).inflate(R.layout.page,parent,false);
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
