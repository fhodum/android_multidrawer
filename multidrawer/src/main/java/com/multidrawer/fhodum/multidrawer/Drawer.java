package com.multidrawer.fhodum.multidrawer;

import android.view.View;

/**
 * Created by fhodum on 4/30/16.
 */
public class Drawer {

    private View button;
    private View body;


    private Drawer(View button, View body){
        this.button = button;
        this.body = body;
    }

    public static class Builder {

        private View buttonView = null;
        private View bodyView = null;

        public Builder(){

        }

        public Builder setButton(View vw){
            this.buttonView = vw;
            return this;
        }

        public Builder setBody(View vw){
            this.bodyView = vw;
            return this;
        }

        public Drawer createDrawer() {
            Drawer retVal = null;
            if (buttonView != null && bodyView != null) {
                retVal = new Drawer(buttonView, bodyView);
            } else {
                throw new IllegalStateException("Please set both the bosy and the button view before creating the drawer");
            }
            return retVal;
        }

    }

    View getBody() {
        return body;
    }

    View getButton() {
        return button;
    }
}
