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

package integration

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.InstrumentationRegistry
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.samples.apps.sunflower.data.AppDatabase
import integration.test.AddFlowTest
import integration.test.CheckGardenTest
import com.google.samples.apps.sunflower.workers.SeedDatabaseWorker
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        AddFlowTest::class,
        CheckGardenTest::class
)
object TestSuite {

    @BeforeClass
    @JvmStatic
    fun setUp() {
        // Prepare some utils, inject test DI components
        val context = InstrumentationRegistry.getTargetContext()
        AppDatabase.setInstance(Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                        WorkManager.getInstance().enqueue(request)
                    }
                })
                .build())
    }
}