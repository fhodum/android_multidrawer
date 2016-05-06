package com.multidrawer.fhodum.testdrawerapplication;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multidrawer.fhodum.multidrawer.Drawer;
import com.multidrawer.fhodum.multidrawer.MultiDrawerView;
import com.multidrawer.fhodum.multidrawer.TopBottomMultiDrawerView;

import java.util.ArrayList;
import java.util.List;


public class TestMultiDrawer extends AppCompatActivity {

    View vw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_multi_drawer);
        MultiDrawerView leftDrawerView = (MultiDrawerView)findViewById(R.id.multiDrawerLeft);
        View button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
        View body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);

//        ((TextView)body.findViewById(R.id.text_view)).setText("One");
//        ((TextView)button.findViewById(R.id.button_text)).setText("One");
//
        Drawer.Builder builder = new Drawer.Builder();
//        builder.setBody(body);
//        builder.setButton(button);
//
//
//        leftDrawerView.addDrawer(builder.createDrawer());
//
//        button = LayoutInflater.from(this).inflate(R.layout.first_button, leftDrawerView , false);
//        body = LayoutInflater.from(this).inflate(R.layout.first_body, leftDrawerView , false);
//
//        ((TextView)body.findViewById(R.id.text_view)).setText("Two");
//        ((TextView)button.findViewById(R.id.button_text)).setText("Two");
//
//        builder = new Drawer.Builder();
//        builder.setBody(body);
//        builder.setButton(button);
//
//        leftDrawerView.addDrawer(builder.createDrawer());
//
//
//
//        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
//        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);
//
//        ((TextView)body.findViewById(R.id.text_view)).setText("Three");
//        ((TextView)button.findViewById(R.id.button_text)).setText("Three");
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
//        ((TextView)body.findViewById(R.id.text_view)).setText("Four");
//        ((TextView)button.findViewById(R.id.button_text)).setText("Four");
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
//        ((TextView)body.findViewById(R.id.text_view)).setText("Five");
//        ((TextView)button.findViewById(R.id.button_text)).setText("Five");
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
//        ((TextView)body.findViewById(R.id.text_view)).setText("Six");
//        ((TextView)button.findViewById(R.id.button_text)).setText("Six");
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
//        ((TextView)body.findViewById(R.id.text_view)).setText("Seven");
//        ((TextView)button.findViewById(R.id.button_text)).setText("Seven");
//
//        builder = new Drawer.Builder();
//        builder.setBody(body);
//        builder.setButton(button);
//
//        leftDrawerView.addDrawer(builder.createDrawer());
//




        //Right

        MultiDrawerView rightDrawerView = (MultiDrawerView)findViewById(R.id.multiDrawerRight);
        button = LayoutInflater.from(this).inflate(R.layout.first_button,rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("One");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);


        rightDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button, rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body, rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Two");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        rightDrawerView.addDrawer(builder.createDrawer());



        button = LayoutInflater.from(this).inflate(R.layout.first_button,rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Three");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        rightDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button,rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Four");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        rightDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button,rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Five");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        rightDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button,rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Six");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        rightDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button,rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Seven");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        rightDrawerView.addDrawer(builder.createDrawer());



        TopBottomMultiDrawerView bottomDrawerView = (TopBottomMultiDrawerView)findViewById(R.id.multiDrawerBottom);
        //Bottom
        button = LayoutInflater.from(this).inflate(R.layout.bottom_button,rightDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.bottom_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Bottom Body");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        bottomDrawerView.addDrawer(builder.createDrawer());
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





    public class ViewGroupPagerAdapter extends PagerAdapter {
        public ViewGroupPagerAdapter(ViewGroup viewGroup) {
            while (viewGroup.getChildCount() > 0) {
                views.add(viewGroup.getChildAt(0));
                viewGroup.removeViewAt(0);
            }
        }

        private List<View> views = new ArrayList<View>();

        @Override
        public Object instantiateItem(ViewGroup parent, int position) {
            View view = views.get(position);
            ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
            lp.width = ViewPager.LayoutParams.FILL_PARENT;
            lp.height = ViewPager.LayoutParams.FILL_PARENT;
            view.setLayoutParams(lp);
            parent.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup parent, int position, Object object) {
            View view = (View) object;
            parent.removeView(view);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
