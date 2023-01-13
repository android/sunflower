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

package com.google.samples.apps.sunflower.compose.plantdetail

import android.content.ContentResolver
import android.net.Uri
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.core.net.toUri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.samples.apps.sunflower.compose.plantdetail.PlantDetails
import com.google.samples.apps.sunflower.compose.plantdetail.PlantDetailsCallbacks
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.test.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlantDetailComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun plantDetails_checkIsNotPlanted() {
        startPlantDetails(isPlanted = false)
        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Add plant").assertIsDisplayed()
    }

    @Test
    fun plantDetails_checkIsPlanted() {
        startPlantDetails(isPlanted = true)
        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Add plant").assertDoesNotExist()
    }

    @Test
    fun plantDetails_checkGalleryNotShown() {
        startPlantDetails(isPlanted = true, hasUnsplashKey = false)
        composeTestRule.onNodeWithContentDescription("Gallery Icon").assertDoesNotExist()
    }

    @Test
    fun plantDetails_checkGalleryIsShown() {
        startPlantDetails(isPlanted = true, hasUnsplashKey = true)
        composeTestRule.onNodeWithContentDescription("Gallery Icon").assertIsDisplayed()
    }

    private fun startPlantDetails(isPlanted: Boolean, hasUnsplashKey: Boolean = false) {
        composeTestRule.setContent {
            PlantDetails(
                plant = plantForTesting(),
                isPlanted = isPlanted,
                callbacks = PlantDetailsCallbacks({ }, { }, { }, { }),
                hasValidUnsplashKey = hasUnsplashKey
            )
        }
    }
}

@Composable
internal fun plantForTesting(): Plant {
    return Plant(
        plantId = "malus-pumila",
        name = "Apple",
        description = "An apple is a sweet, edible fruit produced by an apple tree (Malus pumila). Apple trees are cultivated worldwide, and are the most widely grown species in the genus Malus. The tree originated in Central Asia, where its wild ancestor, Malus sieversii, is still found today. Apples have been grown for thousands of years in Asia and Europe, and were brought to North America by European colonists. Apples have religious and mythological significance in many cultures, including Norse, Greek and European Christian traditions.<br><br>Apple trees are large if grown from seed. Generally apple cultivars are propagated by grafting onto rootstocks, which control the size of the resulting tree. There are more than 7,500 known cultivars of apples, resulting in a range of desired characteristics. Different cultivars are bred for various tastes and uses, including cooking, eating raw and cider production. Trees and fruit are prone to a number of fungal, bacterial and pest problems, which can be controlled by a number of organic and non-organic means. In 2010, the fruit's genome was sequenced as part of research on disease control and selective breeding in apple production.<br><br>Worldwide production of apples in 2014 was 84.6 million tonnes, with China accounting for 48% of the total.<br><br>(From <a href=\\\"https://en.wikipedia.org/wiki/Apple\\\">Wikipedia</a>)",
        growZoneNumber = 3,
        wateringInterval = 30,
        imageUrl = rawUri(R.raw.apple).toString()
    )
}

/**
 * Returns the Uri of a given raw resource
 */
@Composable
private fun rawUri(@RawRes id: Int): Uri {
    return "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${LocalContext.current.packageName}/$id"
        .toUri()
}