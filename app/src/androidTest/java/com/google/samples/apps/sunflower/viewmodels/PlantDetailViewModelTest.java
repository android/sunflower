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

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.google.samples.apps.sunflower.data.AppDatabase;
import com.google.samples.apps.sunflower.data.GardenPlantingRepository;
import com.google.samples.apps.sunflower.data.PlantRepository;
import com.google.samples.apps.sunflower.utilities.LiveDataTestUtil;
import com.google.samples.apps.sunflower.utilities.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PlantDetailViewModelTest {

    private AppDatabase appDatabase;
    private PlantDetailViewModel viewModel;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        PlantRepository plantRepository = PlantRepository.getInstance(appDatabase.plantDao());
        GardenPlantingRepository gardenPlantingRepository = GardenPlantingRepository.getInstance(
                appDatabase.gardenPlantingDao());
        viewModel = new PlantDetailViewModel(plantRepository, gardenPlantingRepository,
                TestUtils.testPlant.getPlantId());
    }

    @After
    public void tearDown() {
        appDatabase.close();
    }

    @Test
    public void testDefaultValues() throws InterruptedException {
        assertFalse(LiveDataTestUtil.getValue(viewModel.isPlanted()));
    }

}