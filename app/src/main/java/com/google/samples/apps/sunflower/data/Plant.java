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

import android.support.annotation.NonNull;

/**
 * A class that represents a Plant along with its name and details.
 */
public class Plant {

    public final String id;
    public final String name;
    public final String details;

    /**
     * Use this constructor to create a new Plant.
     *
     * @param id      id of the plant
     * @param name    name of the plant
     * @param details details of the plant
     */
    Plant(@NonNull String id, @NonNull String name, @NonNull String details) {
        this.id = id;
        this.name = name;
        this.details = details;
    }

    @Override
    public String toString() {
        return name;
    }
}