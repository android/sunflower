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

package com.google.samples.apps.sunflower.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Repository module for handling data operations.
 */
public class PlantRepository {

    private final AppDatabase database;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile PlantRepository instance;

    private PlantRepository(AppDatabase database) {
        this.database = database;
    }

    public synchronized static PlantRepository getInstance(AppDatabase database) {
        PlantRepository result = instance;
        if (result == null) {
            synchronized (LOCK) {
                result = instance;
                if (result == null) {
                    instance = result = new PlantRepository(database);
                }
            }
        }
        return result;
    }

    public LiveData<Plant> getPlant(String plantId) {
        return database.plantDao().getPlant(plantId);
    }

    public LiveData<List<Plant>> getPlants() {
        return database.plantDao().getPlants();
    }

}
