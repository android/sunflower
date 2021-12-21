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
import androidx.room.PrimaryKey
import java.util.*
import java.util.Calendar.DAY_OF_YEAR

/**
 * 植物表
 */
@Entity(tableName = "plants")
data class Plant(
    /**
     * 植物的id
     */
    @PrimaryKey
    @ColumnInfo(name = "id")
    val plantId: String,

    /**
     * 植物名称
     */
    val name: String,

    /**
     * 描述
     */
    val description: String,

    /**
     * 区域成长数量
     */
    val growZoneNumber: Int,

    /**
     * 浇水间隔天数
     */
    val wateringInterval: Int = 7,

    /**
     * 植物图片URL
     */
    val imageUrl: String = ""
) {

    /**
     * Determines if the plant should be watered.  Returns true if [since]'s date > date of last
     * watering + watering Interval; false otherwise.
     */
    fun shouldBeWatered(since: Calendar, lastWateringDate: Calendar) : Boolean {
        return since > lastWateringDate.apply { add(DAY_OF_YEAR, wateringInterval) }
    }
}
