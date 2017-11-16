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

package com.google.samples.apps.sunflower;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.google.samples.apps.sunflower.adapters.PlantAdapter;
import com.google.samples.apps.sunflower.databinding.ActivityPlantListBinding;
import com.google.samples.apps.sunflower.viewmodels.PlantListViewModel;

/**
 * An activity representing a list of Plants. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PlantDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PlantListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean isTwoPane;

    private ActivityPlantListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_plant_list);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle(getTitle());

        binding.fab.setOnClickListener(view ->
                Snackbar.make(view, "TODO: Add new plant to plant list", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());

        if (binding.plantListFrame.plantDetailContainer != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPane = true;
        }

        PlantListViewModel viewModel = ViewModelProviders.of(this)
                .get(PlantListViewModel.class);
        subscribeUi(viewModel);
    }

    private void subscribeUi(PlantListViewModel viewModel) {
        viewModel.getPlants().observe(this, plants -> {
            if (plants != null) {
                PlantAdapter adapter = new PlantAdapter(this, plants, isTwoPane);
                binding.plantListFrame.plantList.setAdapter(adapter);
            }
        });
    }

}
