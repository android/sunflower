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

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlantListBenchmarks {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun openPlantList() = openPlantList(CompilationMode.None())

    @Test
    fun plantListCompilationPartial() = openPlantList(CompilationMode.Partial())

    @Test
    fun plantListCompilationFull() = openPlantList(CompilationMode.Full())

    private fun openPlantList(compilationMode: CompilationMode) =
        benchmarkRule.measureRepeated(
            packageName = PACKAGE_NAME,
            metrics = listOf(FrameTimingMetric()),
            compilationMode = compilationMode,
            iterations = 5,
            startupMode = StartupMode.COLD,
            setupBlock = {
                pressHome()
                // Start the default activity, but don't measure the frames yet
                startActivityAndWait()
            }
        ) {
            goToPlantListTab()
        }
}

fun MacrobenchmarkScope.goToPlantListTab() {
    // Find the tab with plants list
    val plantListTab = device.findObject(By.descContains("Plant list"))
    plantListTab.click()

    // Wait until plant list has children
    val recyclerHasChild = By.hasChild(By.res(packageName, "plant_list"))
    device.wait(Until.hasObject(recyclerHasChild), 5_000)

    // Wait until idle
    device.waitForIdle()
}
