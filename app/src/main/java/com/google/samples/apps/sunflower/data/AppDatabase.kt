/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import com.google.samples.apps.sunflower.utilities.DATABASE_NAME
import com.google.samples.apps.sunflower.utilities.runOnIoThread

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
                            runOnIoThread { seedDatabase(getInstance(context)) }
                        }
                    })
                    .build()
        }

        private fun seedDatabase(database: AppDatabase) {
            database.plantDao().insertAll(ArrayList<Plant>(4).apply {
                add(Plant("1", "Apple", "A red fruit", 1,
                        "https://upload.wikimedia.org/wikipedia/commons/5/55" +
                                "/Apple_orchard_in_Tasmania.jpg"))
                add(Plant("2", "Beet", "A red root vegetable", 1,
                        "https://static.pexels.com/photos/264101/pexels-photo-264101.jpeg"))
                add(Plant("3", "Celery", "A green vegetable", 2,
                        "https://upload.wikimedia.org/wikipedia/commons/5/51" +
                                "/A_scene_of_Coriander_leaves.JPG"))
                add(Plant("4", "Tomato", "A red vegetable", 3,
                        "https://upload.wikimedia.org/wikipedia/commons/1/17" +
                                "/Cherry_tomatoes_red_and_green_2009_16x9.jpg"))
            })
        }
    }
}
