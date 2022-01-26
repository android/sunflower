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

package com.google.samples.apps.sunflower.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

/**
 * 花园种植表
 */
@Entity(tableName = "garden_plantings", indices = [Index("plant_id")])
data class GardenPlanting(

    /**
     * 植物的id
     */
    @ColumnInfo(name = "plant_id")
    @ForeignKey(entity = Plant::class, parentColumns = ["id"], childColumns = ["plant_id"])
    val plantId: String,

    /**
     * 植物被种植的时间，当植物该收获时，用于显示通知。
     */
    @ColumnInfo(name = "plant_date")
    val plantDate: Calendar = Calendar.getInstance(),

    /**
     * 植物上次浇水的时间，当植物该浇水时，用于显示通知。
     */
    @ColumnInfo(name = "last_watering_date")
    val lastWateringDate: Calendar = Calendar.getInstance()
) {

    /**
     * 花园种植中的植物唯一id
     */
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var gardenPlantingId: Long = 0
}
