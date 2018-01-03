/*
 * Copyright (C) 2017 The Android Open Source Project
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

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

/**
 * The Data Access Object for the Plant class.
 */
@Dao
interface PlantDao {
    @Query("SELECT * FROM plants")
    fun getPlants(): LiveData<List<Plant>>

    @Query("SELECT * FROM plants WHERE growZoneNumber = :growZoneNumber")
    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int): LiveData<List<Plant>>

    @Query("SELECT * FROM plants WHERE id = :plantId")
    fun getPlant(plantId: String): LiveData<Plant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(plants: List<Plant>)
}
