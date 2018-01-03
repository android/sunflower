/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.google.samples.apps.sunflower.utilities.testPlant
import com.google.samples.apps.sunflower.utilities.withCollapsingToolbarTitle
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class PlantListActivityTest {

    @Rule @JvmField
    var activityTestRule = ActivityTestRule(PlantListActivity::class.java)

    @Test fun plantListFrameIsDisplayed() {
        onView(withId(R.id.plant_list_frame)).check(matches(isDisplayed()))
    }

    @Test fun clickOnFirstListElement_NavigatesToPlantDetailsActivityWithPlantData() {
        onView(ViewMatchers.withId(R.id.plant_list_frame))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.toolbar_layout))
                .check(matches(withCollapsingToolbarTitle(testPlant.name)))
        onView(withId(R.id.plant_detail)).check(matches(withText(testPlant.description)))
    }

}
