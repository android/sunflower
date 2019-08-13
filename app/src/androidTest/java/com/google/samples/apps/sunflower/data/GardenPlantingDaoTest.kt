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

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import com.google.samples.apps.sunflower.utilities.getValue
import com.google.samples.apps.sunflower.utilities.registerTaskExecutor
import com.google.samples.apps.sunflower.utilities.testCalendar
import com.google.samples.apps.sunflower.utilities.testGardenPlanting
import com.google.samples.apps.sunflower.utilities.testPlant
import com.google.samples.apps.sunflower.utilities.testPlants
import com.google.samples.apps.sunflower.utilities.unRegisterTaskExecutor
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GardenPlantingDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var gardenPlantingDao: GardenPlantingDao
    private var testGardenPlantingId: Long = 0

    @Before fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        gardenPlantingDao = database.gardenPlantingDao()

        database.plantDao().insertAll(testPlants)
        testGardenPlantingId = gardenPlantingDao.insertGardenPlanting(testGardenPlanting)

        registerTaskExecutor()
    }

    @After fun closeDb() {
        database.close()

        unRegisterTaskExecutor()
    }

    @Test fun testGetGardenPlantings() {
        val gardenPlanting2 = GardenPlanting(
            testPlants[1].plantId,
            testCalendar,
            testCalendar
        ).also { it.gardenPlantingId = 2 }
        gardenPlantingDao.insertGardenPlanting(gardenPlanting2)
        assertThat(getValue(gardenPlantingDao.getGardenPlantings()).size, equalTo(2))
    }

    @Test
    fun testGetGardenPlanting() {
        assertThat(
            getValue(gardenPlantingDao.getGardenPlanting(testGardenPlantingId)),
            equalTo(testGardenPlanting)
        )
    }

    @Test fun testDeleteGardenPlanting() {
        val gardenPlanting2 = GardenPlanting(
                testPlants[1].plantId,
                testCalendar,
                testCalendar
        ).also { it.gardenPlantingId = 2 }
        gardenPlantingDao.insertGardenPlanting(gardenPlanting2)
        assertThat(getValue(gardenPlantingDao.getGardenPlantings()).size, equalTo(2))
        gardenPlantingDao.deleteGardenPlanting(gardenPlanting2)
        assertThat(getValue(gardenPlantingDao.getGardenPlantings()).size, equalTo(1))
    }

    @Test fun testGetGardenPlantingForPlant() {
        assertThat(getValue(gardenPlantingDao.getGardenPlantingForPlant(testPlant.plantId)),
                equalTo(testGardenPlanting))
    }

    @Test fun testGetGardenPlantingForPlant_notFound() {
        assertNull(getValue(gardenPlantingDao.getGardenPlantingForPlant(testPlants[2].plantId)))
    }

    @Test fun testGetPlantAndGardenPlantings() {
        val plantAndGardenPlantings = getValue(gardenPlantingDao.getPlantAndGardenPlantings())
        assertThat(plantAndGardenPlantings.size, equalTo(3))

        /**
         * Only the [testPlant] has been planted, and thus has an associated [GardenPlanting]
         */
        assertThat(plantAndGardenPlantings[0].plant, equalTo(testPlant))
        assertThat(plantAndGardenPlantings[0].gardenPlantings.size, equalTo(1))
        assertThat(plantAndGardenPlantings[0].gardenPlantings[0], equalTo(testGardenPlanting))

        // The other plants in the database have not been planted and thus have no GardenPlantings
        assertThat(plantAndGardenPlantings[1].gardenPlantings.size, equalTo(0))
        assertThat(plantAndGardenPlantings[2].gardenPlantings.size, equalTo(0))
    }
}