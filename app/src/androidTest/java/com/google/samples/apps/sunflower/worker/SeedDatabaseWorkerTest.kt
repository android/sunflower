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
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.ListenableWorker.Result
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import com.google.samples.apps.sunflower.utilities.PLANT_DATA_FILENAME
import com.google.samples.apps.sunflower.workers.SeedDatabaseWorker
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RefreshMainDataWorkTest {
    private lateinit var workManager: WorkManager
    private lateinit var context: Context
    private lateinit var configuration: Configuration

    @Before
    fun setup() {
        // Configure WorkManager
        configuration = Configuration.Builder()
            // Set log level to Log.DEBUG to make it easier to debug
            .setMinimumLoggingLevel(Log.DEBUG)
            // Use a SynchronousExecutor here to make it easier to write tests
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        context = InstrumentationRegistry.getInstrumentation().targetContext
        WorkManagerTestInitHelper.initializeTestWorkManager(context, configuration)
        workManager = WorkManager.getInstance(context)
    }

    @Test
    fun testRefreshMainDataWork() {
        // Get the ListenableWorker
        val worker = TestListenableWorkerBuilder<SeedDatabaseWorker>(
            context = context,
            inputData = workDataOf(SeedDatabaseWorker.KEY_FILENAME to PLANT_DATA_FILENAME)
        ).build()

        // Start the work synchronously
        val future = worker.startWork()
        val result = future.get()

        assertThat(result, `is`(Result.Success()))
    }
}