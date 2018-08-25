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

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.annotation.UiThreadTest
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.AppDatabase
import com.google.samples.apps.sunflower.data.GardenPlantingRepository
import com.google.samples.apps.sunflower.data.PlantRepository
import com.google.samples.apps.sunflower.utilities.getValue
import com.google.samples.apps.sunflower.utilities.testPlant
import com.google.samples.apps.sunflower.utilities.testPlants
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

sealed class BasePlantDetailViewModelTest {
    protected lateinit var appDatabase: AppDatabase
    protected lateinit var viewModel: PlantDetailViewModel

    @Before
    open fun setUp() {
        val context = InstrumentationRegistry.getTargetContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        val plantRepo = PlantRepository.getInstance(appDatabase.plantDao())
        val gardenPlantingRepo = GardenPlantingRepository.getInstance(
            appDatabase.gardenPlantingDao()
        )
        viewModel = PlantDetailViewModel(plantRepo, gardenPlantingRepo, testPlant.plantId)
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    abstract fun testShare()
}

class PlantDetailViewModelTestWithoutData : BasePlantDetailViewModelTest() {
    @Test
    @Throws(InterruptedException::class)
    fun testDefaultValues() {
        assertFalse(getValue(viewModel.isPlanted))
    }

    @Test
    @UiThreadTest
    @Throws(InterruptedException::class)
    override fun testShare() {
        with(InstrumentationRegistry.getTargetContext()) {
            viewModel.share(this)
            val expected = getString(R.string.share_text_plant, "")
            assertEquals(expected, getValue(viewModel.share))
        }
    }
}

class PlantDetailViewModelTestWithData : BasePlantDetailViewModelTest() {

    @Before
    override fun setUp() {
        super.setUp()
        appDatabase.plantDao().insertAll(testPlants)
        TimeUnit.SECONDS.sleep(1)
    }

    @Test
    @UiThreadTest
    @Throws(InterruptedException::class)
    override fun testShare() {
        with(InstrumentationRegistry.getTargetContext()) {
            viewModel.share(this)
            val expected = getString(R.string.share_text_plant, testPlant.name)
            assertEquals(expected, getValue(viewModel.share))
        }
    }
}