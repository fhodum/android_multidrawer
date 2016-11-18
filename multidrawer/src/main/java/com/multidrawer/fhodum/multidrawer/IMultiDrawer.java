package com.multidrawer.fhodum.multidrawer;

/**
 * Created by fhodum on 5/7/16.
 */
public interface IMultiDrawer {

    /**
     * Mechanism to open a particular drawer. It will return true if the
     * drawer was found and the drawer was opened.
     *
     * @param drawer - the reference to the drawer to open
     * @return - true if the drawer was found and opened,or false if it was not found
     */
    boolean openDrawer(Drawer drawer);

    /**
     * Open the drawer by the number/position in the list of drawers. Again returns true or false
     * if the drawer was found and opened.
     *
     * @param drawerNumber - the number of the drawer to open from top to bottom or left to right.
     * @return boolean - true or false to indicate if the drawer opened
     */
    boolean openDrawer(int drawerNumber);

    /**
     * Close the drawer
     */
    void closeDrawers();

    /**
     * Add a new drawer to the multi drawer set.
     *
     * @param drawer
     */
    void addDrawer(Drawer drawer);

    /**
     * Remove the drawer from the set of multidrawers.
     *
     * @param drawer - drawer to remove
     * @return boolean to indicate success or failure of removal
     */
    boolean removeDrawer(Drawer drawer);

    void toggleDrawer(Drawer drawer);
}
