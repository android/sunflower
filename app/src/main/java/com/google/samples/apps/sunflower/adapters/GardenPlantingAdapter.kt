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

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.databinding.ViewDataBinding
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.samples.apps.sunflower.BR
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.GardenPlanting
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.data.PlantAndGardenPlantings
import java.text.SimpleDateFormat
import java.util.Locale

class GardenPlantingAdapter(
    val context: Context
) : ListAdapter<PlantAndGardenPlantings, GardenPlantingAdapter.ViewHolder>(GardenPlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_garden_planting, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).apply {
            val plant = checkNotNull(this.plant)
            val gardenPlanting = gardenPlantings[0]
            holder.itemView.tag = this

            with(holder.binding) {
                setVariable(BR.vm, ItemViewModel(context, plant, gardenPlanting))
                executePendingBindings()
            }
        }
    }

    class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    class ItemViewModel(
        context: Context,
        plant: Plant,
        gardenPlanting: GardenPlanting
    ) : ViewModel() { // Actually no need, however, for unified namespace with other [ViewModels]s I consider it.

        private val dateFormat by lazy { SimpleDateFormat("MMM d, yyyy", Locale.US) }
        private val plantDateString by lazy { dateFormat.format(gardenPlanting.plantDate.time) }
        private val waterDateString by lazy { dateFormat.format(gardenPlanting.lastWateringDate.time) }
        private val wateringPrefix by lazy {
            context.getString(R.string.watering_next_prefix, waterDateString)
        }
        private val wateringSuffix by lazy {
            context.resources.getQuantityString(
                R.plurals.watering_next_suffix,
                plant.wateringInterval, plant.wateringInterval
            )
        }

        val imageUrl = ObservableField<String>(plant.imageUrl)

        val plantDate = ObservableField<String>(
            context.getString(
                R.string.planted_date, plant.name,
                plantDateString
            )
        )

        val waterDate = ObservableField<String>("$wateringPrefix - $wateringSuffix")
    }
}