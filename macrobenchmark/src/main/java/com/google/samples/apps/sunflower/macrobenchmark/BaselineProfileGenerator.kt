/*
 * Copyright 2022 Google LLC
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

package com.google.samples.apps.sunflower.macrobenchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun startPlantListPlantDetail() {
        rule.collect(PACKAGE_NAME) {
            // start the app flow
            pressHome()
            startActivityAndWait()

            // go to plant list flow
            val plantListTab = device.findObject(By.descContains("Plant list"))
            plantListTab.click()
            device.waitForIdle()
            // sleep for animations to settle
            Thread.sleep(500)

            // go to plant detail flow
            val plantList = device.findObject(By.res(packageName, "plant_list"))
            val listItem = plantList.children[0]
            listItem.click()
            device.wait(Until.gone(By.res(packageName, "plant_list")), 5_000)
        }
    }
}
