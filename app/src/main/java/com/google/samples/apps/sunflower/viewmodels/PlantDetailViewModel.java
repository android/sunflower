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

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.google.samples.apps.sunflower.data.Plant;
import com.google.samples.apps.sunflower.data.PlantRepository;

/**
 * The ViewModel for PlantDetailFragment
 */
public class PlantDetailViewModel extends ViewModel {

    private MutableLiveData<Plant> mPlant;

    private PlantDetailViewModel(String plantId) {
        if (mPlant == null) {
            mPlant = new MutableLiveData<>();
            mPlant.setValue(PlantRepository.getInstance().getPlant(plantId));
        }
    }

    public LiveData<Plant> getPlant() {
        return mPlant;
    }

    /**
     * This creator is used to inject the plant ID into PlantDetailViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final String mPlantId;

        public Factory(@NonNull String plantId) {
            mPlantId = plantId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new PlantDetailViewModel(mPlantId);
        }
    }
}
