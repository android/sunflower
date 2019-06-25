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
import com.google.samples.apps.sunflower.ViewPagerFragmentDirections
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.databinding.ListItemPlantBinding

/**
 * Adapter for the [RecyclerView] in [PlantListFragment].
 */
class PlantAdapter : ListAdapter<Plant, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    private val headerPositions: IntArray = intArrayOf(0)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position in headerPositions) {
            val holderViewParams = (holder as HeaderViewHolder).itemView.layoutParams
            val layoutParams = holderViewParams as StaggeredGridLayoutManager.LayoutParams
            layoutParams.isFullSpan = true
        } else {
            val plant = getItem(position)
            (holder as PlantViewHolder).apply {
                bind(createOnClickListener(plant.plantId), plant)
                itemView.tag = plant
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            in headerPositions -> R.layout.plant_list_header
            else -> R.layout.list_item_plant
        }
    }

    override fun getItemCount(): Int {
        return if (currentList.size == 0) {
            0
        } else {
            currentList.size + headerPositions.size
        }
    }

    override fun getItem(position: Int): Plant {
        return currentList[position - headerPositions.size]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            R.layout.plant_list_header -> HeaderViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.plant_list_header, parent, false))

            R.layout.list_item_plant -> PlantViewHolder(ListItemPlantBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))

            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    private fun createOnClickListener(plantId: String): View.OnClickListener {
        return View.OnClickListener {
            val direction = ViewPagerFragmentDirections.actionViewPagerFragmentToPlantDetailFragment(plantId)
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