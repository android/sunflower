/*
 * Copyright 2022 Google LLC
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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.samples.apps.sunflower.data.Animal
import com.google.samples.apps.sunflower.data.AnimalRepository
import javax.inject.Inject

class AnimalViewModel @Inject internal constructor(
        animalRepository: AnimalRepository,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val animals: List<Animal> =
            animalRepository.getAnimals()
}