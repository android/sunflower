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

package com.google.samples.apps.sunflower.data

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PlantTest {

    private lateinit var plant: Plant

    @Before
    fun setUp() {
        plant = Plant("1", "Tomato", "A red vegetable", 1, "")
    }

    @Test
    fun test_toString() {
        assertEquals("Tomato", plant.toString())
    }

}