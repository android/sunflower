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

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.PlantAndGardenPlantings
import java.text.SimpleDateFormat
import java.util.Locale

class GardenPlantingAdapter(
        val context: Context
) : ListAdapter<PlantAndGardenPlantings, GardenPlantingAdapter.ViewHolder>(GardenPlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_garden_planting, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)

        val plant = checkNotNull(getItem(position).plant)
        val gardenPlanting = getItem(position).gardenPlantings[0]
        val plantDateString = dateFormat.format(gardenPlanting.plantDate.time)
        val waterDateString = dateFormat.format(gardenPlanting.lastWateringDate.time)
        val wateringPrefix = context.getString(R.string.watering_next_prefix, waterDateString)
        val wateringSuffix = context.resources.getQuantityString(R.plurals.watering_next_suffix,
                plant.wateringInterval, plant.wateringInterval)

        holder.apply {
            Glide.with(context)
                    .load(plant.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(plantImageView)
            plantDateView.text = context.getString(R.string.planted_date, plant.name,
                    plantDateString)
            waterDateView.text = "$wateringPrefix - $wateringSuffix"
            itemView.tag = getItem(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plantImageView: ImageView = itemView.findViewById(R.id.imageView)
        val plantDateView: TextView = itemView.findViewById(R.id.plant_date)
        val waterDateView: TextView = itemView.findViewById(R.id.water_date)
    }
}