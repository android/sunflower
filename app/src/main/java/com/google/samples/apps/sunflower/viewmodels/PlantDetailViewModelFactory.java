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

package com.google.samples.apps.sunflower.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.google.samples.apps.sunflower.data.Plant;
import com.google.samples.apps.sunflower.data.PlantRepository;

/**
 * Factory for creating a {@link PlantDetailViewModel} with a constructor that takes a
 * {@link PlantRepository} and an ID for the current {@link Plant}.
 */
public class PlantDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final PlantRepository repository;

    @NonNull
    private final String plantId;

    public PlantDetailViewModelFactory(@NonNull PlantRepository repository,
            @NonNull String plantId) {
        this.repository = repository;
        this.plantId = plantId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new PlantDetailViewModel(repository, plantId);
    }

}
