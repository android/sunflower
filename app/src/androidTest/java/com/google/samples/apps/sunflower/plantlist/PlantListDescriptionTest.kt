/*
 * Copyright 2020 Google LLC
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

package com.google.samples.apps.sunflower.plantlist

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.ui.test.*
import com.google.samples.apps.sunflower.compose.ProvideDisplayInsets
import com.google.samples.apps.sunflower.compose.plantlist.PlantListScreen
import com.google.samples.apps.sunflower.compose.plantlist.plants
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * [PlantListDescription] Blackbox testing
 */
@RunWith(AndroidJUnit4::class)
class PlantListDescriptionTest {

    @get:Rule
    val composeRule = createComposeRule(disableTransitions = true)
    private val fakeData = plants.subList(0, 4)

    @Test
    fun assertThereIsCorrectDataDisplayedOnScreen() {
        createSomeFakeData()
        fakeData.forEachIndexed { index, plant ->
            composeRule.onNodeWithText(plant.name).assertIsDisplayed()
            composeRule.onAllNodesWithLabel("Picture of plant")[index].assertIsDisplayed() //For accessibility
        }
    }

    private fun createSomeFakeData() {
        composeRule.setContent {
            ProvideDisplayInsets {
                PlantListScreen(plants = fakeData, onClick = {})
            }
        }
    }
}