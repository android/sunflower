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

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Bundle
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.Navigation.findNavController
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.samples.apps.sunflower.utilities.chooser
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlantDetailFragmentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<GardenActivity>()

    @Before
    fun jumpToPlantDetailFragment() {
        composeTestRule.activityRule.scenario.onActivity {
            val bundle = Bundle().apply { putString("plantId", "malus-pumila") }
            findNavController(it, R.id.nav_host).navigate(R.id.plant_detail_fragment, bundle)
        }
    }

    @Test
    fun screen_launches() {
        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
    }

    @Test
    fun testShareTextIntent() {
        Intents.init()

        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Share").assertIsDisplayed().performClick()

        intended(
            chooser(
                allOf(
                    hasAction(Intent.ACTION_SEND),
                    hasType("text/plain"),
                    hasExtra(
                        Intent.EXTRA_TEXT,
                        "Check out the Apple plant in the Android Sunflower app"
                    )
                )
            )
        )
        Intents.release()

        // dismiss the Share Dialog
        InstrumentationRegistry.getInstrumentation()
            .uiAutomation
            .performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
    }
}
