/*
 * Copyright 2023 Google LLC
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

package com.google.samples.apps.sunflower.compose.garden

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.samples.apps.sunflower.data.PlantAndGardenPlantings
import com.google.samples.apps.sunflower.utilities.testPlantAndGardenPlanting
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GardenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun garden_emptyGarden() {
        startGarden(emptyList())
        composeTestRule.onNodeWithText("Add plant").assertIsDisplayed()
    }

    @Test
    fun garden_notEmptyGarden() {
        startGarden(listOf(testPlantAndGardenPlanting))
        composeTestRule.onNodeWithText("Add plant").assertDoesNotExist()
        composeTestRule.onNodeWithText(testPlantAndGardenPlanting.plant.name).assertIsDisplayed()
    }

    private fun startGarden(gardenPlantings: List<PlantAndGardenPlantings>) {
        composeTestRule.setContent {
            GardenScreen(gardenPlants = gardenPlantings)
        }
    }
}