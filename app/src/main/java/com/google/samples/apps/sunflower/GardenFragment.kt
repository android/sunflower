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
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.google.samples.apps.sunflower.adapters.GardenPlantingAdapter
import com.google.samples.apps.sunflower.adapters.PLANT_LIST_PAGE_INDEX
import com.google.samples.apps.sunflower.databinding.FragmentGardenBinding
import com.google.samples.apps.sunflower.viewmodels.GardenPlantingListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GardenFragment : Fragment() {

    private lateinit var binding: FragmentGardenBinding

    private val viewModel: GardenPlantingListViewModel by viewModels()

    private var menu: Menu? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGardenBinding.inflate(inflater, container, false)
        val adapter = GardenPlantingAdapter()
        binding.gardenList.adapter = adapter

        binding.addPlant.setOnClickListener {
            navigateToPlantListPage()
        }

        subscribeUi(adapter, binding)
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun subscribeUi(adapter: GardenPlantingAdapter, binding: FragmentGardenBinding) {
        viewModel.plantAndGardenPlantings.observe(viewLifecycleOwner) { result ->
            binding.hasPlantings = !result.isNullOrEmpty()
            adapter.submitList(result)
        }
        viewModel.nightMode.observe(viewLifecycleOwner, Observer { mode ->
            val nightMode = mode ?: AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
            // Update in case no theme changes were applied, e.g. system theme is same as light.
            menu?.let { updateMenuThemeSelection(it, nightMode) }
        })
    }

    // TODO: convert to data binding if applicable
    private fun navigateToPlantListPage() {
        requireActivity().findViewById<ViewPager2>(R.id.view_pager).currentItem =
            PLANT_LIST_PAGE_INDEX
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_garden, menu)
        this.menu = menu
        val nightMode = viewModel.nightMode.value ?: AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
        updateMenuThemeSelection(menu, nightMode)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val mode = when (item.itemId) {
            R.id.action_follow_system -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            R.id.action_light_theme -> AppCompatDelegate.MODE_NIGHT_NO
            R.id.action_dark_theme -> AppCompatDelegate.MODE_NIGHT_YES
            else -> return false
        }
        viewModel.setNightMode(mode)
        return true
    }

    private fun updateMenuThemeSelection(menu: Menu, nightMode: Int) {
        val checkItemId = when (nightMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> R.id.action_light_theme
            AppCompatDelegate.MODE_NIGHT_YES -> R.id.action_dark_theme
            else -> R.id.action_follow_system
        }
        menu.findItem(checkItemId)?.isChecked = true
    }

}
