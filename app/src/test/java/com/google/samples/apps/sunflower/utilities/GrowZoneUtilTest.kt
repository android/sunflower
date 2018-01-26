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

package com.google.samples.apps.sunflower.utilities

import org.junit.Assert.assertEquals
import org.junit.Test

class GrowZoneUtilTest {

    @Test fun getZoneForLatitude() {
        assertEquals(13, getZoneForLatitude(0.0))
        assertEquals(13, getZoneForLatitude(7.0))
        assertEquals(12, getZoneForLatitude(7.1))
        assertEquals(1, getZoneForLatitude(84.1))
        assertEquals(1, getZoneForLatitude(90.0))
    }

    @Test fun getZoneForLatitude_negativeLatitudes() {
        assertEquals(13, getZoneForLatitude(-7.0))
        assertEquals(12, getZoneForLatitude(-7.1))
        assertEquals(1, getZoneForLatitude(-84.1))
        assertEquals(1, getZoneForLatitude(-90.0))
    }
}