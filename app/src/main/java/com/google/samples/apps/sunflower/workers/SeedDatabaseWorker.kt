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

package com.google.samples.apps.sunflower.workers

import android.util.Log
import androidx.work.Worker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.samples.apps.sunflower.data.AppDatabase
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.utilities.readJson

class SeedDatabaseWorker : Worker() {
    private val TAG = SeedDatabaseWorker::class.java.simpleName

    override fun doWork(): WorkerResult {
        val plantType = object : TypeToken<List<Plant>>() {}.type

        return try {
            val plantList: List<Plant> = Gson().fromJson(readJson(applicationContext), plantType)
            val database = AppDatabase.getInstance(applicationContext)
            database.plantDao().insertAll(plantList)
            WorkerResult.SUCCESS
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            WorkerResult.FAILURE
        }
    }
}