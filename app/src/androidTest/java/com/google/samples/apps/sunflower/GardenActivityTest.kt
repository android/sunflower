package com.google.samples.apps.sunflower

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.DrawerMatchers.isClosed
import android.support.test.espresso.contrib.DrawerMatchers.isOpen
import android.support.test.espresso.contrib.NavigationViewActions.navigateTo
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.view.Gravity
import com.google.samples.apps.sunflower.utilities.getToolbarNavigationContentDescription
import org.junit.Rule
import org.junit.Test

class GardenActivityTest {

    @Rule @JvmField
    var activityTestRule = ActivityTestRule(GardenActivity::class.java)

    @Test fun clickOnAndroidHomeIcon_OpensAndClosesNavigation() {
        // Check that drawer is closed at startup
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START)))

        clickOnHomeIconToOpenNavigationDrawer()
        checkDrawerIsOpen()
    }

    @Test fun onRotate_NavigationStaysOpen() {
        clickOnHomeIconToOpenNavigationDrawer()

        // Rotate device to landscape
        activityTestRule.activity.requestedOrientation = SCREEN_ORIENTATION_LANDSCAPE
        checkDrawerIsOpen()

        // Rotate device back to portrait
        activityTestRule.activity.requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
        checkDrawerIsOpen()
    }

    @Test fun clickOnPlantListDrawerMenuItem_StartsPlantListActivity() {
        clickOnHomeIconToOpenNavigationDrawer()

        // Press on Plant List navigation item
        onView(withId(R.id.navigation_view))
                .perform(navigateTo(R.id.plant_list_navigation_menu_item))

        // Check that the PlantListActivity is visible
        onView(withId(R.id.plant_list_frame)).check(matches(isDisplayed()))
    }

    private fun clickOnHomeIconToOpenNavigationDrawer() {
        onView(withContentDescription(getToolbarNavigationContentDescription(
                activityTestRule.activity, R.id.toolbar))).perform(click())
    }

    private fun checkDrawerIsOpen() {
        onView(withId(R.id.drawer_layout)).check(matches(isOpen(Gravity.START)))
    }

}