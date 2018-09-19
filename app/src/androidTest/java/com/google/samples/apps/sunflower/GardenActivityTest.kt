/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower

import android.view.Gravity
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import com.google.samples.apps.sunflower.utilities.getToolbarNavigationContentDescription
import org.junit.Assert.assertEquals
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

        with(UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())) {
                // Rotate device to landscape
                setOrientationLeft()
                checkDrawerIsOpen()

                // Rotate device back to portrait
                setOrientationRight()
                checkDrawerIsOpen()
        }
    }

    @Test fun clickOnPlantListDrawerMenuItem_StartsPlantListActivity() {
        clickOnHomeIconToOpenNavigationDrawer()

        // Press on Plant List navigation item
        onView(withId(R.id.navigation_view))
                .perform(navigateTo(R.id.plant_list_fragment))

        // Check that the PlantListFragment is visible
        onView(withId(R.id.plant_list)).check(matches(isDisplayed()))
    }

    @Test fun pressDeviceBack_CloseDrawer_Then_PressBack_Close_App() {
        clickOnHomeIconToOpenNavigationDrawer()
        onView(isRoot()).perform(ViewActions.pressBack())
        checkDrawerIsNotOpen()
        assertEquals(activityTestRule.activity.isFinishing, false)
        assertEquals(activityTestRule.activity.isDestroyed, false)
    }

    private fun clickOnHomeIconToOpenNavigationDrawer() {
        onView(withContentDescription(getToolbarNavigationContentDescription(
                activityTestRule.activity, R.id.toolbar))).perform(click())
    }

    private fun checkDrawerIsOpen() {
        onView(withId(R.id.drawer_layout)).check(matches(isOpen(Gravity.START)))
    }

    private fun checkDrawerIsNotOpen() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START)))
    }
}
