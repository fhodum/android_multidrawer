## Synopsis

This is a widget library that provides a set of drawers that can slide in and out othe sides or top and bottom of the screen. There are callbacks each time a drawer is opened or closed, as well as a set of XML theme attributes to specify many aspects of the look and feel of the drawers.

## Code Example

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

A short description of the motivation behind the creation and maintenance of the project. This should explain **why** the project exists.

## Installation

Provide code examples and explanations of how to get the project.

## API Reference

Depending on the size of the project, if it is small and simple enough the reference docs can be added to the README. For medium size to larger projects it is important to at least provide a link to where the API reference docs live.

## Tests

Describe and show how to run the tests with code examples.

## Contributors

Let people know how they can dive into the project, include important links to things like issue trackers, irc, twitter accounts if applicable.

## License

A short snippet describing the license (MIT, Apache, etc.)