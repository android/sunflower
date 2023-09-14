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

package com.google.samples.apps.sunflower.data

import com.google.samples.apps.sunflower.test.CalendarMatcher.Companion.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.Calendar

internal class GardenPlantingTest {

    @Test
    fun testDefaultValues() {
        val gardenPlanting = GardenPlanting("1")
        val calendar = Calendar.getInstance()
        assertThat(gardenPlanting.plantDate, equalTo(calendar))
        assertThat(gardenPlanting.lastWateringDate, equalTo(calendar))
        assertThat(gardenPlanting.gardenPlantingId, `is`(0L))
    }
}
