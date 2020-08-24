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

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

class GardenPlantingTest {

    @Test
    fun testDefaultValues() {
        val gardenPlanting = GardenPlanting("1")
        val cal = Calendar.getInstance()
        assertYMD(cal, gardenPlanting.plantDate)
        assertYMD(cal, gardenPlanting.lastWateringDate)
        assertEquals(0L, gardenPlanting.gardenPlantingId)
    }

    // Only Year/Month/Day precision is needed for comparing GardenPlanting Calendar entries
    private fun assertYMD(expectedCal: Calendar, actualCal: Calendar) {
        assertThat(actualCal.get(YEAR), equalTo(expectedCal.get(YEAR)))
        assertThat(actualCal.get(MONTH), equalTo(expectedCal.get(MONTH)))
        assertThat(actualCal.get(DAY_OF_MONTH), equalTo(expectedCal.get(DAY_OF_MONTH)))
    }
}
