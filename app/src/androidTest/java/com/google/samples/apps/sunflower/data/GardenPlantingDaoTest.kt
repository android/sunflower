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

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.matcher.ViewMatchers.assertThat
import com.google.samples.apps.sunflower.utilities.getValue
import com.google.samples.apps.sunflower.utilities.testCalendar
import com.google.samples.apps.sunflower.utilities.testPlant
import com.google.samples.apps.sunflower.utilities.testPlants
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test

class GardenPlantingDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var gardenPlantingDao: GardenPlantingDao
    private val gardenPlanting = GardenPlanting("1", testPlant.plantId, testCalendar, testCalendar)

    @Before fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        gardenPlantingDao = database.gardenPlantingDao()

        database.plantDao().insertAll(testPlants)
        gardenPlantingDao.insertGardenPlanting(gardenPlanting)
    }

    @After fun closeDb() {
        database.close()
    }

    @Test fun testGetGardenPlantings() {
        val gardenPlanting2 = GardenPlanting("2", testPlants[1].plantId, testCalendar, testCalendar)
        gardenPlantingDao.insertGardenPlanting(gardenPlanting2)
        assertThat(getValue(gardenPlantingDao.getGardenPlantings()).size, equalTo(2))
    }

    @Test fun testGetGardenPlanting() {
        assertThat(getValue(gardenPlantingDao.getGardenPlanting(gardenPlanting.gardenPlantingId)),
                equalTo(gardenPlanting))
    }
}