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

package com.google.samples.apps.sunflower.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.google.samples.apps.sunflower.data.AppDatabase
import com.google.samples.apps.sunflower.data.GardenPlantingRepository
import com.google.samples.apps.sunflower.data.PlantRepository
import com.google.samples.apps.sunflower.utilities.getValue
import com.google.samples.apps.sunflower.utilities.testPlant
import com.google.samples.apps.sunflower.utilities.testPlants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class PlantDetailViewModelTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: PlantDetailViewModel
    private lateinit var plantRepo: PlantRepository
    private lateinit var gardenPlantingRepo: GardenPlantingRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getTargetContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        plantRepo = PlantRepository.getInstance(appDatabase.plantDao())
        gardenPlantingRepo = GardenPlantingRepository.getInstance(appDatabase.gardenPlantingDao())
        appDatabase.plantDao().insertAll(testPlants)
        viewModel = PlantDetailViewModel(plantRepo, gardenPlantingRepo, testPlant.plantId)
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testDefaultValues() {
        assertFalse(getValue(viewModel.isPlanted))
    }

    @Test
    fun shouldAddPlantToGarden() = runBlocking {
        val plantToAdd = testPlants[1]
        viewModel = PlantDetailViewModel(plantRepo, gardenPlantingRepo, plantToAdd.plantId)

        launch {
            viewModel.addPlantToGarden()
            // DB needs time to update itself.
            delay(TimeUnit.SECONDS.toMillis(1))
        }.join()

        val plantAdded =
            getValue(gardenPlantingRepo.getGardenPlantingForPlant(plantToAdd.plantId))
        assertNotNull(plantAdded)
    }

    @Test
    fun shouldCancelAddingPlantToGarden() = runBlocking {
        val plantToAdd = testPlants[2]
        viewModel = PlantDetailViewModel(plantRepo, gardenPlantingRepo, plantToAdd.plantId)

        launch {
            viewModel.addPlantToGarden().cancel()

            // Manually wait 1 sec, the adding should not happen due to call the cancel().
            delay(TimeUnit.SECONDS.toMillis(1))
        }.join()

        val plantJustAdded =
            getValue(gardenPlantingRepo.getGardenPlantingForPlant(plantToAdd.plantId))
        assertNull(plantJustAdded)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun shouldCancelAddingPlantToGardenByViewModel() = runBlocking {
        val plantToAdd = testPlants[2]
        viewModel = PlantDetailViewModel(plantRepo, gardenPlantingRepo, plantToAdd.plantId)

        launch {
            viewModel.addPlantToGarden()
        }

        launch {
            viewModel.viewModelScope.cancel()

            // Manually wait 1 sec, the adding should not happen due to call the cancel().
            delay(TimeUnit.SECONDS.toMillis(1))
        }.join()

        val plantJustAdded =
            getValue(gardenPlantingRepo.getGardenPlantingForPlant(plantToAdd.plantId))
        assertNull(plantJustAdded)
    }
}