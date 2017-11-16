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

import java.util.List;

/**
 * Repository module for handling data operations.
 */
public class PlantRepository {

    private static final PlantRepository INSTANCE = new PlantRepository();

    private PlantRepository() {}

    public static PlantRepository getInstance() {
        return INSTANCE;
    }

    public List<Plant> getPlants() {
        return PlantContent.ITEMS;
    }

    public Plant getPlant(String plantId) {
        return PlantContent.ITEM_MAP.get(plantId);
    }
}
