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

package com.google.samples.apps.sunflower

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.ViewGroup
import com.google.samples.apps.sunflower.adapters.PlantAdapter
import com.google.samples.apps.sunflower.databinding.FragmentPlantListBinding
import com.google.samples.apps.sunflower.utilities.InjectorUtils
import com.google.samples.apps.sunflower.viewmodels.PlantListViewModel

class PlantListFragment : Fragment() {

    private lateinit var viewModel: PlantListViewModel
    private var arePlantsFiltered = false // TODO remove this, used for development
    private lateinit var binding: FragmentPlantListBinding
    private val BUNDLE_RECYCLER_LAST_STATE by lazy { "recycler.last.state" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPlantListBinding.inflate(inflater, container, false).run {
        val context = context ?: return root
        setLifecycleOwner(this@PlantListFragment)
        binding = this

        val factory = InjectorUtils.providePlantListViewModelFactory(context)
        viewModel = ViewModelProviders.of(this@PlantListFragment, factory)
            .get(PlantListViewModel::class.java)

        with(PlantAdapter()) {
            plantList.adapter = this
            subscribeUi(binding, this, getRecyclerViewState(savedInstanceState))
        }

        setHasOptionsMenu(true)
        root
    }

    private fun getRecyclerViewState(savedInstanceState: Bundle?): Parcelable? =
        savedInstanceState?.getParcelable(BUNDLE_RECYCLER_LAST_STATE)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            BUNDLE_RECYCLER_LAST_STATE,
            binding.plantList.layoutManager.onSaveInstanceState()
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_plant_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_zone -> {
                updateData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeUi(
        databinding: FragmentPlantListBinding,
        adapter: PlantAdapter,
        recyclerViewState: Parcelable?
    ) {
        viewModel.getPlants().observe(this, Observer { plants ->
            if (plants != null) {
                adapter.values = plants
                databinding.loadingUi.visibility = GONE

                recyclerViewState?.apply {
                    databinding.plantList.layoutManager.onRestoreInstanceState(recyclerViewState)
                }
            }
        })
    }

    private fun updateData() {
        arePlantsFiltered = if (arePlantsFiltered) {
            viewModel.clearGrowZoneNumber()
            false
        } else {
            viewModel.setGrowZoneNumber(9)
            true
        }
    }
}