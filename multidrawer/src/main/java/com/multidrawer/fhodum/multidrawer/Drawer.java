package com.multidrawer.fhodum.multidrawer;

import android.view.View;

/**
 * Created by fhodum on 4/30/16.
 */
public class Drawer {

    public interface DrawerCallbacks {

        public void drawerOpened();

        public void drawerClosed();
    }




    private View button;
    private View body;
    private DrawerCallbacks callbackHandler = null;


    private Drawer(View button, View body){
        this.button = button;
        this.body = body;
    }

    private Drawer(View button, View body, DrawerCallbacks callbacks){
        this.button = button;
        this.body = body;
        this.callbackHandler = callbacks;
    }

    public static class Builder {

        private View buttonView = null;
        private View bodyView = null;
        private DrawerCallbacks drawerCallbacks = null;

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

        public Builder setDrawerCallback(DrawerCallbacks callback){
            this.drawerCallbacks = callback;
            return this;
        }

        public Drawer createDrawer() {
            Drawer retVal = null;
            if (buttonView != null && bodyView != null) {
                if(drawerCallbacks != null) {
                    retVal = new Drawer(buttonView,bodyView,drawerCallbacks);
                }
                else {
                    retVal = new Drawer(buttonView, bodyView);
                }
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

    DrawerCallbacks getDrawerCallbackHandler(){ return callbackHandler; }

}
