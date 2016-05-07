package com.multidrawer.fhodum.multidrawer;

/**
 * Created by fhodum on 5/7/16.
 */
public interface IMultiDrawer {

    boolean openDrawer(Drawer drawer);

    boolean openDrawer(int drawerNumber);

    void closeDrawers();

    void addDrawer(Drawer drawer);

    boolean removeDrawer(Drawer drawer);
}
