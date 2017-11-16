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

import com.google.samples.apps.sunflower.data.Plant;
import com.google.samples.apps.sunflower.data.PlantRepository;

import java.util.List;

/**
 * The ViewModel for PlantListActivity
 */
public class PlantListViewModel extends ViewModel {

    private MutableLiveData<List<Plant>> mPlants;

    public PlantListViewModel () {
        if (mPlants == null) {
            mPlants = new MutableLiveData<>();
            mPlants.setValue(PlantRepository.getInstance().getPlants());
        }
    }

    public LiveData<List<Plant>> getPlants() {
        return mPlants;
    }
}
