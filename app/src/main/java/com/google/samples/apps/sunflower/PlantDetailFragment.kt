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

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.samples.apps.sunflower.databinding.FragmentPlantDetailBinding
import com.google.samples.apps.sunflower.utilities.InjectorUtils
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel

/**
 * A fragment representing a single Plant detail screen.
 */
class PlantDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentPlantDetailBinding.inflate(inflater, container, false).run {
        setLifecycleOwner(this@PlantDetailFragment)
        val plantId = requireNotNull(arguments).getString(ARG_ITEM_ID)

        val factory =
            InjectorUtils.providePlantDetailViewModelFactory(requireActivity(), plantId)
        val plantDetailViewModel = ViewModelProviders.of(this@PlantDetailFragment, factory)
            .get(PlantDetailViewModel::class.java)
        viewModel = plantDetailViewModel

        fab.setOnClickListener { view ->
            plantDetailViewModel.addPlantToGarden()
            Snackbar.make(view, R.string.added_plant_to_garden, Snackbar.LENGTH_LONG).show()
        }

        val appCompatActivity = requireActivity() as AppCompatActivity
        appCompatActivity.setSupportActionBar(detailToolbar)

        // Show the Up button in the action bar.
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        root
    }

    companion object {

        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"

        /**
         * Create a new instance of PlantDetailFragment, initialized with a plant ID.
         */
        fun newInstance(plantId: String): PlantDetailFragment {
            // Supply plant ID as an argument.
            val bundle = Bundle().apply { putString(ARG_ITEM_ID, plantId) }
            return PlantDetailFragment().apply { arguments = bundle }
        }
    }
}
