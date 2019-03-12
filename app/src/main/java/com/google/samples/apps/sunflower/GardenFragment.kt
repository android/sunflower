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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import com.google.samples.apps.sunflower.adapters.GardenPlantingAdapter
import com.google.samples.apps.sunflower.adapters.GardenPlantingDetailsLookup
import com.google.samples.apps.sunflower.databinding.FragmentGardenBinding
import com.google.samples.apps.sunflower.utilities.InjectorUtils
import com.google.samples.apps.sunflower.viewmodels.GardenPlantingListViewModel

class GardenFragment : Fragment() {

    private lateinit var selectionTracker: SelectionTracker<Long>
    private lateinit var selectionObserver: GardenSelectionObserver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGardenBinding.inflate(inflater, container, false)
        val adapter = GardenPlantingAdapter()
        binding.gardenList.adapter = adapter
        subscribeUi(adapter, binding)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Add a listener to the back button here with the fragment as the lifecycler owner so
        // that it will automatically be removed in onDestroy.
        requireActivity().addOnBackPressedCallback(this, selectionObserver)
    }

    private fun subscribeUi(adapter: GardenPlantingAdapter, binding: FragmentGardenBinding) {
        val factory = InjectorUtils.provideGardenPlantingListViewModelFactory(requireContext())
        val viewModel = ViewModelProviders.of(this, factory)
            .get(GardenPlantingListViewModel::class.java)

        viewModel.gardenPlantings.observe(viewLifecycleOwner, Observer { plantings ->
            binding.hasPlantings = !plantings.isNullOrEmpty()
        })

        viewModel.plantAndGardenPlantings.observe(viewLifecycleOwner, Observer { result ->
            if (!result.isNullOrEmpty())
                adapter.submitList(result)
        })

        val recyclerView = binding.gardenList
        selectionTracker = SelectionTracker.Builder<Long>(
            GardenFragment::javaClass.name,
            recyclerView,
            StableIdKeyProvider(recyclerView),
            GardenPlantingDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        adapter.tracker = selectionTracker

        selectionObserver = GardenSelectionObserver(selectionTracker) { selectedCount ->
            onSelectionChanged(selectedCount)
        }
        selectionTracker.addObserver(selectionObserver)
    }

    private fun onSelectionChanged(selectedCount: Int) {
        // TODO: Enable/disable ActionMode when items are selected.
    }

    /**
     * Class to handle the selection of items in one's garden.
     */
    private class GardenSelectionObserver(
        private val selectionTracker: SelectionTracker<Long>,
        private val onSelectionChangedListener: (Int) -> Unit
    ) :
        SelectionTracker.SelectionObserver<Long>(),
        OnBackPressedCallback {

        override fun onSelectionChanged() {
            super.onSelectionChanged()
            onSelectionChangedListener(selectionTracker.selection?.size() ?: 0)
        }

        // If the back button is pressed and there's a selection, clear the selection, rather
        // than potentially exiting the app.
        override fun handleOnBackPressed() =
            if (selectionTracker.hasSelection()) {
                selectionTracker.clearSelection()
                true
            } else {
                false
            }
    }
}
