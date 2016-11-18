package com.multidrawer.fhodum.multidrawer;

import android.view.View;

/**
 * Created by fhodum on 4/30/16.
 *
 * Class that holds the Views for the body and the button that make up the Drawer.
 * It also defines the callback interface that the MultiDrawer code uses to notify
 * when this particular drawer is opened or closed.
 */
public class PanelDrawer extends Drawer{

    public interface PanelCallbacks {

        /**
         * The drawer that was opened, it is called at the beginning of the open animation.
         *
         * @param drawer - drawer being opened
         */
        public void panelOpened(PanelDrawer drawer);

        /**
         * The drawer that was closed, it is called at the beginning of the open animation.
         *
         * @param drawer - drawer being closed
         */
        public void panelClosed(PanelDrawer drawer);
    }


    private PanelCallbacks callbackHandler = null;


    private PanelDrawer(View body){
        super(new View(body.getContext()) ,body);
//        getButton().setVisibility(View.GONE);
    }

    private PanelDrawer(View body, PanelCallbacks callbacks){
        super(new View(body.getContext()),body);
        this.callbackHandler = callbacks;
    }

    public static class Builder {

        private View bodyView = null;
        private PanelCallbacks drawerCallbacks = null;

        public Builder(){

        }


        public Builder setBody(View vw){
            this.bodyView = vw;
            return this;
        }

        public Builder setPanelCallback(PanelCallbacks callback){
            this.drawerCallbacks = callback;
            return this;
        }

        public PanelDrawer createDrawer() {
            PanelDrawer retVal = null;
            if (bodyView != null) {
                if(drawerCallbacks != null) {
                    retVal = new PanelDrawer(bodyView,drawerCallbacks);
                }
                else {
                    retVal = new PanelDrawer( bodyView);
                }
            } else {
                throw new IllegalStateException("Please set both the bosy and the button view before creating the drawer");
            }
            return retVal;
        }

    }


    PanelCallbacks getPanelCallbackHandler(){ return callbackHandler; }

    boolean isPanel() { return true;}

}
