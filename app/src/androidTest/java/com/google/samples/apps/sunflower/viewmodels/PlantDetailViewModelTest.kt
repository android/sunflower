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
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.samples.apps.sunflower.data.AppDatabase
import com.google.samples.apps.sunflower.data.repository.GardenPlantingRepository
import com.google.samples.apps.sunflower.data.repository.PlantRepository
import com.google.samples.apps.sunflower.utilities.getValue
import com.google.samples.apps.sunflower.utilities.testPlant
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PlantDetailViewModelTest {

    private val hiltRule: HiltAndroidRule by lazy { HiltAndroidRule(this) }
    private val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val chain: RuleChain = RuleChain
            .outerRule(hiltRule)
            .around(instantTaskExecutorRule)

    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: PlantDetailViewModel

    @Inject lateinit var gardenPlantingRepository: GardenPlantingRepository
    @Inject lateinit var plantRepository: PlantRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        viewModel = PlantDetailViewModel(
                plantRepository,
                gardenPlantingRepository,
                testPlant.plantId
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun testDefaultValues() {
        assertFalse(getValue(viewModel.isPlanted))
    }

}