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

import android.content.Intent
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.google.samples.apps.sunflower.utilities.testPlant
import com.google.samples.apps.sunflower.utilities.withCollapsingToolbarTitle
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class PlantDetailActivityTest {

    @Rule @JvmField
    val activityTestRule: ActivityTestRule<PlantDetailActivity> =
            object : ActivityTestRule<PlantDetailActivity>(PlantDetailActivity::class.java) {
        override fun getActivityIntent() : Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            val result = Intent(targetContext, PlantDetailActivity::class.java)
            result.putExtra(PlantDetailFragment.ARG_ITEM_ID, testPlant.plantId)
            return result
        }
    }

    @Test
    fun viewTextPersistAfterOrientationChange() {
        onView(withId(R.id.toolbar_layout))
                .check(matches(withCollapsingToolbarTitle(testPlant.name)))

        activityTestRule.activity.requestedOrientation = SCREEN_ORIENTATION_LANDSCAPE
        onView(withId(R.id.toolbar_layout))
                .check(matches(withCollapsingToolbarTitle(testPlant.name)))
    }

}
