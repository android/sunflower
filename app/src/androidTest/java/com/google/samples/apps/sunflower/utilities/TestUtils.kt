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

@file:JvmName("TestUtils")

package com.google.samples.apps.sunflower.utilities

import android.app.Activity
import android.support.design.widget.CollapsingToolbarLayout
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v7.widget.Toolbar
import android.view.View
import com.google.samples.apps.sunflower.data.Plant
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * [Plant] object used for tests.
 */
@JvmField val testPlant = Plant("1", "Apple", "A red fruit", 1, 1, "")

/**
 * Matches the toolbar title with a specific string.
 *
 * @param string the string to match
 */
fun withCollapsingToolbarTitle(string: String): Matcher<View> {
    return object : BoundedMatcher<View, CollapsingToolbarLayout>(
            CollapsingToolbarLayout::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("with toolbar title: $string")
        }

        override fun matchesSafely(toolbar: CollapsingToolbarLayout) = (string == toolbar.title)
    }
}

/**
 * Returns the content description for the navigation button view in the toolbar.
 */
fun getToolbarNavigationContentDescription(activity: Activity, toolbarId: Int) =
        activity.findViewById<Toolbar>(toolbarId).navigationContentDescription as String