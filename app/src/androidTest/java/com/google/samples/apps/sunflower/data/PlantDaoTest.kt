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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlantDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var plantDao: PlantDao
    private val plantA = Plant("1", "A", "", 1, 1, "")
    private val plantB = Plant("2", "B", "", 1, 1, "")
    private val plantC = Plant("3", "C", "", 2, 2, "")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        plantDao = database.plantDao()

        // Insert plants in non-alphabetical order to test that results are sorted by name
        plantDao.upsertAll(listOf(plantB, plantC, plantA))
    }

    @After fun closeDb() {
        database.close()
    }

    @Test fun testGetPlants() = runBlocking {
        val plantList = plantDao.getPlants().first()
        assertThat(plantList.size, equalTo(3))

        // Ensure plant list is sorted by name
        assertThat(plantList[0], equalTo(plantA))
        assertThat(plantList[1], equalTo(plantB))
        assertThat(plantList[2], equalTo(plantC))
    }

    @Test fun testGetPlantsWithGrowZoneNumber() = runBlocking {
        val plantList = plantDao.getPlantsWithGrowZoneNumber(1).first()
        assertThat(plantList.size, equalTo(2))
        assertThat(plantDao.getPlantsWithGrowZoneNumber(2).first().size, equalTo(1))
        assertThat(plantDao.getPlantsWithGrowZoneNumber(3).first().size, equalTo(0))

        // Ensure plant list is sorted by name
        assertThat(plantList[0], equalTo(plantA))
        assertThat(plantList[1], equalTo(plantB))
    }

    @Test fun testGetPlant() = runBlocking {
        assertThat(plantDao.getPlant(plantA.plantId).first(), equalTo(plantA))
    }
}
