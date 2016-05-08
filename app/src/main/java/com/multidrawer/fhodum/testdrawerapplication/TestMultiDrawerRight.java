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
import com.multidrawer.fhodum.multidrawer.TopBottomMultiDrawerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class TestMultiDrawerRight extends AppCompatActivity {

    View vw;


    List<Drawer> leftDrawers = new ArrayList<>(2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_multi_drawer_right);





        //Right

        LeftRightMultiDrawerView rightDrawerView = (LeftRightMultiDrawerView)findViewById(R.id.multiDrawerRight);
        View button = LayoutInflater.from(this).inflate(R.layout.right_button,rightDrawerView , false);
        View body = LayoutInflater.from(this).inflate(R.layout.first_body,rightDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("One");

        Drawer.Builder builder = new Drawer.Builder();
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


    }





    public class MyAdapter extends PagerAdapter {
        public MyAdapter(){
        }

        List<View> views = new Vector<View>(0);

        @Override
        public Object instantiateItem(ViewGroup parent, int position) {
            View view = null;
            if(views.size()<= position){
                view = LayoutInflater.from(TestMultiDrawerRight.this).inflate(R.layout.page,parent,false);
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
