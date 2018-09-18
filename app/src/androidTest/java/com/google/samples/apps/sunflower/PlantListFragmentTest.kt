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

import androidx.navigation.findNavController
import androidx.test.InstrumentationRegistry
import androidx.test.annotation.UiThreadTest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import androidx.test.uiautomator.UiDevice
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlantListFragmentTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(GardenActivity::class.java)

    @Before
    @UiThreadTest
    fun openPlantListFragment() {
        activityTestRule.activity.apply {
            findNavController(R.id.garden_nav_fragment).navigate(R.id.plant_list_fragment)
        }
    }

    @Test
    fun should_Dismiss_LoadingUI_After_Data_Loaded() {
        with(UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())) {
            this.waitForIdle(500)
            onView(withId(R.id.loading_ui)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        }
    }
}