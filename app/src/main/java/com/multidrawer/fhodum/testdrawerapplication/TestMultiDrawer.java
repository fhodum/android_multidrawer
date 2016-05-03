package com.multidrawer.fhodum.testdrawerapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.multidrawer.fhodum.multidrawer.Drawer;
import com.multidrawer.fhodum.multidrawer.MultiDrawerView;


public class TestMultiDrawer extends AppCompatActivity {

    View vw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_multi_drawer);
        MultiDrawerView leftDrawerView = (MultiDrawerView)findViewById(R.id.multiDrawerLeft);
        View button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
        View body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("One");

        Drawer.Builder builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);


        leftDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button, leftDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body, leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Two");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        leftDrawerView.addDrawer(builder.createDrawer());



        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Three");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        leftDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Four");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        leftDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Five");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        leftDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Six");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        leftDrawerView.addDrawer(builder.createDrawer());

        button = LayoutInflater.from(this).inflate(R.layout.first_button,leftDrawerView , false);
        body = LayoutInflater.from(this).inflate(R.layout.first_body,leftDrawerView , false);

        ((TextView)body.findViewById(R.id.text_view)).setText("Seven");

        builder = new Drawer.Builder();
        builder.setBody(body);
        builder.setButton(button);

        leftDrawerView.addDrawer(builder.createDrawer());





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
}
