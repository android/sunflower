/*
 * Copyright 2021 Google LLC
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

package com.google.samples.apps.sunflower.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.samples.apps.sunflower.data.dao.GardenPlantingDao
import com.google.samples.apps.sunflower.data.dao.PlantDao
import com.google.samples.apps.sunflower.data.model.GardenPlanting
import com.google.samples.apps.sunflower.data.model.Plant
import com.google.samples.apps.sunflower.common.DB_NAME
import com.google.samples.apps.sunflower.common.DB_VERSION
import com.google.samples.apps.sunflower.common.PLANT_DATA_FILENAME
import com.google.samples.apps.sunflower.workers.SeedDatabaseWorker
import com.google.samples.apps.sunflower.workers.SeedDatabaseWorker.Companion.KEY_FILENAME

/**
 * App rom数据库
 */
@TypeConverters(Converters::class)
@Database(entities = [GardenPlanting::class, Plant::class], version = DB_VERSION, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile private var instance: AppDatabase? = null

        /**
         * 获取单例
         */
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // 创建并预填充数据库。详情请参阅本文: https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
                            .setInputData(workDataOf(KEY_FILENAME to PLANT_DATA_FILENAME))
                            .build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
//                .allowMainThreadQueries()
                .build()
        }
    }


    /**
     * 获取植物dao类
     */
    abstract fun getPlantDao(): PlantDao

    /**
     * 获取花园种植的dao类
     */
    abstract fun getGardenPlantingDao(): GardenPlantingDao
}
