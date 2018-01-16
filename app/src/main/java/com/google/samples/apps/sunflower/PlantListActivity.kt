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

package com.google.samples.apps.sunflower

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.samples.apps.sunflower.adapters.PlantAdapter
import com.google.samples.apps.sunflower.databinding.ActivityPlantListBinding
import com.google.samples.apps.sunflower.utilities.InjectorUtils
import com.google.samples.apps.sunflower.viewmodels.PlantListViewModel

/**
 * An activity representing a list of Plants. This activity has different presentations for handset
 * and tablet-size devices. On handsets, the activity presents a list of items, which when touched,
 * lead to a [PlantDetailActivity] representing item details. On tablets, the activity presents the
 * list of items and item details side-by-side using two vertical panes.
 */
class PlantListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private var isTwoPane: Boolean = false

    private lateinit var adapter: PlantAdapter
    private lateinit var viewModel: PlantListViewModel

    private var arePlantsFiltered = false // TODO remove this, used for development

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityPlantListBinding>(this,
                R.layout.activity_plant_list)
        setSupportActionBar(binding.toolbar)
        binding.run {
            toolbar.title = title
        }

        if (binding.plantListFrame?.plantDetailContainer != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPane = true
        }
        adapter = PlantAdapter(this, isTwoPane)
        binding.plantListFrame?.plantList?.adapter = adapter

        val factory = InjectorUtils.providePlantListViewModelFactory(application)
        viewModel = ViewModelProviders.of(this, factory).get(PlantListViewModel::class.java)
        subscribeUi()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_plant_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.filter_zone -> {
            updateData()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun subscribeUi() {
        viewModel.getPlants().observe(this, Observer { plants ->
            if (plants != null) adapter.values = plants
        })
    }

    // TODO add filter selector
    private fun updateData() {
        arePlantsFiltered = if (arePlantsFiltered) {
            viewModel.clearGrowZoneNumber()
            false
        } else {
            viewModel.setGrowZoneNumber(1)
            true
        }
    }

}
