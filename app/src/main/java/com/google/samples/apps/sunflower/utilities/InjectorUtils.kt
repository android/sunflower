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

package com.google.samples.apps.sunflower.utilities

import android.app.Application
import com.google.samples.apps.sunflower.data.AppDatabase
import com.google.samples.apps.sunflower.data.PlantRepository
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModelFactory
import com.google.samples.apps.sunflower.viewmodels.PlantListViewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun provideRepository(application: Application): PlantRepository {
        return PlantRepository.getInstance(AppDatabase.getInstance(application).plantDao())
    }

    @JvmStatic fun providePlantListViewModelFactory(
            application: Application
    ): PlantListViewModelFactory {
        val repository = provideRepository(application)
        return PlantListViewModelFactory(repository)
    }

    @JvmStatic fun providePlantDetailViewModelFactory(
            application: Application,
            plantId: String
    ): PlantDetailViewModelFactory {
        val repository = provideRepository(application)
        return PlantDetailViewModelFactory(repository, plantId)
    }

}
