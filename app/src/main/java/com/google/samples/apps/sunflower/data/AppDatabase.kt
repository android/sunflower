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

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.support.annotation.VisibleForTesting
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.samples.apps.sunflower.utilities.DATABASE_NAME
import com.google.samples.apps.sunflower.utilities.runOnIoThread
import java.io.IOException
import java.nio.charset.Charset

/**
 * The Room database for this app
 */
@Database(entities = [Plant::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: deleteAndBuildDatabase(context).also { instance = it }
            }
        }

        // Reset the database to have new data on every app launch
        // TODO / STOPSHIP: This is only used for development; remove prior to shipping
        private fun deleteAndBuildDatabase(context: Context): AppDatabase {
            context.deleteDatabase(DATABASE_NAME)
            return buildDatabase(context)
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            runOnIoThread { seedDatabase(context) }
                        }
                    })
                    .build()
        }

        private fun seedDatabase(context: Context) {
            val plantType = object : TypeToken<List<Plant>>() {}.type
            val plantList: List<Plant> = Gson().fromJson(readJson(context), plantType)
            val database = getInstance(context)
            database.plantDao().insertAll(plantList)
        }

        @VisibleForTesting internal fun readJson(
            context: Context,
            fileName: String = "plants.json"
        ): String {
            return try {
                val inputStream = context.assets.open(fileName)
                val buffer = ByteArray(inputStream.available())
                inputStream.run {
                    read(buffer)
                    close()
                }
                String(buffer, Charset.defaultCharset())
            } catch (ex: IOException) {
                Log.i("AppDatabase", "Error reading JSON: $ex")
                ""
            }
        }
    }
}
