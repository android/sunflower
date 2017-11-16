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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace this with a Content Provider.
 */
class PlantContent {

    private static final int NUM_ITEMS = 4;

    /**
     * An array of sample (dummy) items.
     */
    static final List<Plant> ITEMS = new ArrayList<>(NUM_ITEMS);

    /**
     * A map of sample (dummy) items, by ID.
     */
    static final Map<String, Plant> ITEM_MAP = new HashMap<>(NUM_ITEMS);

    static {
        addItem(new Plant("1", "Apple", "A red fruit"));
        addItem(new Plant("2", "Beet", "A red root vegetable"));
        addItem(new Plant("3", "Cucumber", "A green vegetable"));
        addItem(new Plant("4", "Tomato", "A red vegetable"));
    }

    private static void addItem(Plant item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getPlantId(), item);
    }
}
