/*
 * Copyright 2019 Google LLC
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

package com.google.samples.apps.sunflower.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.work.ListenableWorker.Result
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.google.samples.apps.sunflower.testdobules.TestPlantDao
import com.google.samples.apps.sunflower.data.PlantDao
import com.google.samples.apps.sunflower.utilities.AssetManager
import com.google.samples.apps.sunflower.utilities.JsonAssetManager
import com.google.samples.apps.sunflower.utilities.PLANT_DATA_FILENAME
import com.google.samples.apps.sunflower.workers.SeedDatabaseWorker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Test

class RefreshMainDataWorkTest {

    private lateinit var context: Context
    private lateinit var plantDao: PlantDao
    private lateinit var assetManager: AssetManager
    private lateinit var testSeedDatabaseWorkerFactory: TestSeedDatabaseWorkerFactory

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        plantDao = TestPlantDao()
        assetManager = JsonAssetManager(context)
        testSeedDatabaseWorkerFactory = TestSeedDatabaseWorkerFactory(plantDao, assetManager)
    }

    @Test
    fun testRefreshMainDataWork() {
        // Get the ListenableWorker
        val worker = TestListenableWorkerBuilder<SeedDatabaseWorker>(
            context = context,
            inputData = workDataOf(SeedDatabaseWorker.KEY_FILENAME to PLANT_DATA_FILENAME)
        )
            .setWorkerFactory(testSeedDatabaseWorkerFactory)
            .build()

        runTest {
            // Start the work synchronously
            val result = worker.doWork()

            assert(plantDao.getPlants().first().isNotEmpty())
            assertThat(result, `is`(Result.Success()))
        }
    }
}
