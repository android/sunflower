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

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.samples.apps.sunflower.HomeViewPagerFragmentDirections
import com.google.samples.apps.sunflower.compose.plantlist.PlantListItemView
import com.google.samples.apps.sunflower.data.Animal
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.data.UnsplashPhoto

/**
 * Adapter for the [RecyclerView] in [PlantListFragment].
 */
class AnimalAdapter : ListAdapter<Animal, RecyclerView.ViewHolder>(AnimalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AnimalViewHolder(ComposeView(parent.context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val animal = getItem(position)
        (holder as AnimalViewHolder).bind(animal)
    }

    class AnimalViewHolder(
            composeView: ComposeView
    ) : RecyclerView.ViewHolder(composeView) {
        fun bind(animal: Animal) {
            (itemView as ComposeView).setContent {
                MdcTheme {
                    AnimalListItemView(animal = animal) {

                    }
                }
            }
        }

        private fun AnimalListItemView(animal: Animal, onClick: () -> Unit) {

        }
    }
}

private class AnimalDiffCallback : DiffUtil.ItemCallback<Animal>() {
    override fun areItemsTheSame(oldItem: Animal, newItem: Animal): Boolean {
        return oldItem.animalName == newItem.animalName
    }

    override fun areContentsTheSame(oldItem: Animal, newItem: Animal): Boolean {
        return oldItem == newItem
    }
}

