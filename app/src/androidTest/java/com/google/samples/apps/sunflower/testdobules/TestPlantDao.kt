/*
 * Copyright 2024 Google LLC
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

package com.google.samples.apps.sunflower.testdobules

import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.data.PlantDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TestPlantDao : PlantDao {

    private val entitiesFlow = MutableStateFlow(emptyList<Plant>())

    override fun getPlants(): Flow<List<Plant>> = entitiesFlow

    override fun getPlantsWithGrowZoneNumber(growZoneNumber: Int): Flow<List<Plant>> =
        getPlants().map { plants ->
            plants.filter { plant -> plant.growZoneNumber == growZoneNumber }
        }

    override fun getPlant(plantId: String): Flow<Plant> =
        getPlants().map { plants ->
            plants.first { plant -> plant.plantId == plantId }
        }

    override suspend fun upsertAll(plants: List<Plant>) {
        entitiesFlow.update { oldValues ->
            (oldValues + plants).distinctBy(Plant::plantId)
        }
    }
}
