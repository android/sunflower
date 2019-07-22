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

package com.google.samples.apps.sunflower.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.samples.apps.sunflower.PlantListFragment
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.HomeViewPagerFragmentDirections
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.databinding.ListItemPlantBinding

/**
 * Adapter for the [RecyclerView] in [PlantListFragment].
 */
class PlantAdapter : ListAdapter<Plant, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    /**
     * Int array of all the positions with header-style cards
     */
    private val headerPositions = intArrayOf(0)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        // if current position is a header element
        if (position in headerPositions) {

            // cast generic RecyclerView.ViewHolder to header view holder
            val holderViewParams = (holder as HeaderViewHolder).itemView.layoutParams
            val layoutParams = holderViewParams as StaggeredGridLayoutManager.LayoutParams

            // set width of header to span entire screen
            layoutParams.isFullSpan = true
        } else { // Must be a plant type
            val plant = getItem(position)

            // cast generic RecyclerView.ViewHolder to Plant view holder
            (holder as PlantViewHolder).apply {
                bind(createOnClickListener(plant.plantId), plant)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        // if current position is where a header should go, return header view type
        return when (position) {
            in headerPositions -> R.layout.plant_list_header
            else -> R.layout.list_item_plant
        }
    }

    override fun getItemCount(): Int {

        // If no available plants, don't print header either, otherwise return number of cards needed
        return if (currentList.size == 0) {
            0
        } else {
            currentList.size + headerPositions.size
        }
    }

    override fun getItem(position: Int): Plant {

        // index in plant list is position subtracted by offset of number of headers
        return currentList[position - headerPositions.size]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        // Return a specific viewholder type depending on current viewType
        return when (viewType) {

            // Generic layout inflate from Layout XML file
            R.layout.plant_list_header -> HeaderViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.plant_list_header, parent, false))

            R.layout.list_item_plant -> PlantViewHolder(ListItemPlantBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))

            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    private fun createOnClickListener(plantId: String): View.OnClickListener {
        return View.OnClickListener {
            val direction = HomeViewPagerFragmentDirections.actionViewPagerFragmentToPlantDetailFragment(plantId)
            it.findNavController().navigate(direction)
        }
    }

    class PlantViewHolder(
        private val binding: ListItemPlantBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: Plant) {
            binding.apply {
                clickListener = listener
                plant = item
                executePendingBindings()
            }
        }
    }

    /**
     * HeaderViewHolder class is a basic implementation of RecyclerView.ViewHolder
     * Adapt this class if custom bindings are necessary
     */
    class HeaderViewHolder(
        headerView: View
    ) : RecyclerView.ViewHolder(headerView)
}

private class PlantDiffCallback : DiffUtil.ItemCallback<Plant>() {

    override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
        return oldItem.plantId == newItem.plantId
    }

    override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
        return oldItem == newItem
    }
}