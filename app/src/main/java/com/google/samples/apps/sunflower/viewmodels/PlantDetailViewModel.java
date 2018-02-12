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

package com.google.samples.apps.sunflower.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.google.samples.apps.sunflower.PlantDetailActivity;
import com.google.samples.apps.sunflower.PlantDetailFragment;
import com.google.samples.apps.sunflower.data.GardenPlantingRepository;
import com.google.samples.apps.sunflower.data.Plant;
import com.google.samples.apps.sunflower.data.PlantRepository;

/**
 * The ViewModel used in {@link PlantDetailActivity} and {@link PlantDetailFragment}.
 */
public class PlantDetailViewModel extends ViewModel {

    private GardenPlantingRepository gardenPlantingRepository;
    private LiveData<Boolean> isPlanted;
    private LiveData<Plant> plant;
    private String plantId;

    PlantDetailViewModel(PlantRepository plantRepository,
                         GardenPlantingRepository gardenPlantingRepository,
                         String plantId) {
        plant = plantRepository.getPlant(plantId);
        this.gardenPlantingRepository = gardenPlantingRepository;
        this.plantId = plantId;

        /* The getGardenPlantingForPlant method returns a LiveData from querying the database. The
         * method can return null in two cases: when the database query is running and if no records
         * are found. In these cases isPlanted is false. If a record is found then isPlanted is
         * true. */
        isPlanted = Transformations.map(gardenPlantingRepository.getGardenPlantingForPlant(plantId),
                result -> (result != null));
    }

    public LiveData<Plant> getPlant() {
        return plant;
    }

    public LiveData<Boolean> isPlanted() {
        return isPlanted;
    }

    public void addPlantToGarden() {
        gardenPlantingRepository.createGardenPlanting(plantId);
    }
}
