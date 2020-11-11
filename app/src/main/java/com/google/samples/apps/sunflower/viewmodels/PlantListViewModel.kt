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

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.samples.apps.sunflower.PlantListFragment
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.data.PlantRepository
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest

/**
 * The ViewModel for [PlantListFragment].
 */
class PlantListViewModel @ViewModelInject internal constructor(
    plantRepository: PlantRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val growZoneChannel = ConflatedBroadcastChannel<Int>()

    val plants: LiveData<List<Plant>> = growZoneChannel.asFlow()
            .flatMapLatest { growZone ->
                if (growZone == NO_GROW_ZONE) {
                    plantRepository.getPlants()
                } else {
                    plantRepository.getPlantsWithGrowZoneNumber(growZone)
                }
            }.asLiveData()

    init {
        getSavedGrowZoneNumberOrDefault()
    }

    fun setGrowZoneNumber(num: Int) {
        growZoneChannel.offer(num)
        savedStateHandle.set(GROW_ZONE_SAVED_STATE_KEY, num)
    }

    fun clearGrowZoneNumber() {
        growZoneChannel.offer(NO_GROW_ZONE)
        savedStateHandle.set(GROW_ZONE_SAVED_STATE_KEY, NO_GROW_ZONE)
    }

    fun isFiltered() = getSavedGrowZoneNumberOrDefault().value != NO_GROW_ZONE

    /** @return saved Grow Zone (if available); otherwise return [NO_GROW_ZONE] as the default */
    private fun getSavedGrowZoneNumberOrDefault(): MutableLiveData<Int> {
        val liveData = savedStateHandle.getLiveData(GROW_ZONE_SAVED_STATE_KEY, NO_GROW_ZONE)
        growZoneChannel.offer(liveData.value ?: NO_GROW_ZONE)
        return liveData
    }

    companion object {
        private const val NO_GROW_ZONE = -1
        private const val GROW_ZONE_SAVED_STATE_KEY = "GROW_ZONE_SAVED_STATE_KEY"
    }
}
