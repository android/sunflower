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

package com.google.samples.apps.sunflower.viewmodels

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.databinding.ObservableField
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.PlantAndGardenPlantings
import java.text.SimpleDateFormat
import java.util.Locale

class PlantAndGardenPlantingsViewModel(
    context: Context,
    plantings: PlantAndGardenPlantings
) : ViewModel() {

    private val plant = checkNotNull(plantings.plant)
    private val gardenPlanting = plantings.gardenPlantings[0]

    private val dateFormat by lazy { SimpleDateFormat("MMM d, yyyy", Locale.US) }
    private val plantDateString by lazy { dateFormat.format(gardenPlanting.plantDate.time) }
    private val waterDateString by lazy { dateFormat.format(gardenPlanting.lastWateringDate.time) }
    private val wateringPrefix by lazy {
        context.getString(
            R.string.watering_next_prefix,
            waterDateString
        )
    }
    private val wateringSuffix by lazy {
        context.resources.getQuantityString(
            R.plurals.watering_next_suffix,
            plant.wateringInterval, plant.wateringInterval
        )
    }

    val imageUrl = ObservableField<String>(plant.imageUrl)

    val plantDate = ObservableField<String>(
        context.getString(
            R.string.planted_date, plant.name,
            plantDateString
        )
    )

    val waterDate = ObservableField<String>("$wateringPrefix - $wateringSuffix")
}