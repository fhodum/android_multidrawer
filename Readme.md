## Synopsis

This is a widget library that provides a set of drawers that can slide in and out othe sides or top and bottom of the screen. There are callbacks each time a drawer is opened or closed, as well as a set of XML theme attributes to specify many aspects of the look and feel of the drawers.

## Code Example

- [Right Hand Drawer]
    - Drawers

        ![](https://github.com/fhodum/android_multidrawer/RightDrawers.gif)



There is an example application in the apps directory that contains an activity with drawers on all four sides of the screen.

To add to an XML layout file you add the following code:

    <com.multidrawer.fhodum.multidrawer.LeftRightMultiDrawerView
        android:id="@+id/multiDrawerLeft"
        android:layout_width="200dp"
        android:layout_height="match_parent"
        android:background="@android:color/transparent"
        android:layout_alignParentLeft="true"
        style="@style/MyAppMultiDrawerStyleLeft"
        />


The style for setting many of the attributes looks like:

    <style name="MyAppMultiDrawerStyleLeft" parent="@style/MultiDrawerTheme.LeftRightMultiDrawerViewStyle">
        <item name="multiDrawerButtonLayoutHeight">@dimen/drawerButtonHeight</item>
        <item name="multiDrawerButtonLayoutWidth">@dimen/drawerButtonWidth</item>
        <item name="multiDrawerDeselectedButtonFadeTime">300</item>
        <item name="multiDrawerButtonPaddingTop" > 3dp</item>
        <item name="multiDrawerButtonPaddingLeft" > 0dp</item>
        <item name="multiDrawerButtonPaddingBottom"> 3dp</item>
        <item name="multiDrawerButtonPaddingRight">2dp</item>
        <item name="multiDrawerSpaceBetweenDrawerTabs"> 0dp</item>
        <item name="leftRightDrawerSide">left</item>
    </style>

## Motivation

The primary motivation for me was to replace the long deprecated but never really replaces Sliding Drawer. The current crop of Drawers such as NavDrawer is really meant for navigation. The use case for this type of widget is one where you have a set of tools that you need to keep bringing up, and then applying to the main window. Think a drawing tool with a color palette that you can hide while drawing then slide out to select a another color.

Additionally, this library allows for adding more tabs or drawers than can fit in the screen with a sliding horizontal or vertical view.

## Installation

To add this code to your project, make sure to have jcenter in your repo list and add the following to your build.gradle file:

compile 'com.fhodum.multidraw:multidraw:0.0.1'



## Contributors

Let people know how they can dive into the project, include important links to things like issue trackers, irc, twitter accounts if applicable.

## License

This software is licensed under the Apache 2.0 license.
